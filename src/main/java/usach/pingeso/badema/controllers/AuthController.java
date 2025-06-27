package usach.pingeso.badema.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import usach.pingeso.badema.dtos.auth.LoginDTO;
import usach.pingeso.badema.dtos.auth.RegisterDTO;
import usach.pingeso.badema.dtos.auth.UsuarioDTO;
import usach.pingeso.badema.entities.UsuarioEntity;
import usach.pingeso.badema.services.postgresql.UsuarioObraService;
import usach.pingeso.badema.services.postgresql.UsuarioService;
import usach.pingeso.badema.webconfig.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService userService;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;
    private final UsuarioObraService usuarioObraService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsuarioService userService,
                          PasswordEncoder passwordEncoder,
                          JdbcTemplate jdbcTemplate, UsuarioObraService usuarioObraService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
        this.usuarioObraService = usuarioObraService;
    }

    // Login sin idObra
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO loginDto) {
        try {
            UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.getCorreo(), loginDto.getContrasena());
            authenticationManager.authenticate(login);

            UsuarioEntity userEntity = userService.findByCorreo(loginDto.getCorreo());

            String jwt = jwtUtil.create(userEntity.getCorreo(), userEntity.getId());

            // Inicialmente no asignamos obra en sesion, puede ser null o 0
            // Actualizamos la tabla sesion con la obra activa para ese usuario
            int updated = jdbcTemplate.update(
                    "UPDATE public.session SET obra_id = -1 WHERE user_id = ?",
                    userEntity.getId()
            );

            // Si no existía sesión para ese usuario, hacemos un INSERT (por si acaso)
            if (updated == 0) {
                jdbcTemplate.update(
                        "INSERT INTO public.session(user_id, obra_id) VALUES (?, -1)",
                        userEntity.getId()
                );
            }

            Map<String, Object> response = new HashMap<>();
            response.put("userId", userEntity.getId());
            response.put("correo", userEntity.getCorreo());
            response.put("isGerencia", userEntity.isGerencia());
            
            if(userEntity.isGerencia()) response.put("rol", "Gerencia");
            else response.put("rol", "Usuario");

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Nuevo endpoint para asignar obra seleccionada
    @PostMapping("/seleccionar-obra")
    public ResponseEntity<Map<String, Object>> seleccionarObra(@RequestHeader("Authorization") String authHeader, @RequestParam Long idObra) {
        try {
            // Sacamos el JWT del header "Bearer <token>
            String token = authHeader.replace("Bearer ", "").trim();

            // Obtenemos el idUsuario desde el JWT
            Long idUsuario = jwtUtil.getIdUsuario(token);

            // Actualizamos la tabla sesion con la obra activa para ese usuario
            int updated = jdbcTemplate.update(
                    "UPDATE public.session SET obra_id = ? WHERE user_id = ?",
                    idObra,
                    idUsuario
            );

            // Si no existía sesión para ese usuario, hacemos un INSERT (por si acaso)
            if (updated == 0) {
                jdbcTemplate.update(
                        "INSERT INTO public.session(user_id, obra_id) VALUES (?, ?)",
                        idUsuario,
                        idObra
                );
            }

            UsuarioDTO usuarioObra = usuarioObraService.getUsuarioYRolByObraIdAndUsuarioId(idObra, idUsuario);

            Map<String, Object> response = new HashMap<>();
            response.put("rol", usuarioObra.getRol());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO registerDto) {
        UsuarioEntity newUser = new UsuarioEntity();
        newUser.setNombre(registerDto.getNombre());
        newUser.setApellido(registerDto.getApellido());
        newUser.setCorreo(registerDto.getCorreo());
        newUser.setTelefono(registerDto.getTelefono());
        newUser.setContrasena(passwordEncoder.encode(registerDto.getContrasena()));
        newUser.setGerencia(false);
        userService.createCliente(newUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean isValid = jwtUtil.isValid(token);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Access granted");
    }
}
