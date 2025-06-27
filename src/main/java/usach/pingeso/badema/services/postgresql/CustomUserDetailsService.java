package usach.pingeso.badema.services.postgresql;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.auth.UsuarioDTO;
import usach.pingeso.badema.entities.UsuarioEntity;
import usach.pingeso.badema.repositories.postgresql.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioObraService usuarioObraService;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository, UsuarioObraService usuarioObraService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioObraService = usuarioObraService;
    }

    // NO se usa, da igual este
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsuarioEntity> user = usuarioRepository.findByCorreo(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.get().getCorreo())
                .password(user.get().getContrasena())
                .roles()
                .build();
    }

    public UserDetails loadUserByIdUsuarioAndIdObra(Long idUsuario, Long idObra) {
        UsuarioDTO user = usuarioObraService.getUsuarioYRolByObraIdAndUsuarioId(idObra, idUsuario);

        // 1) Asegurate que user.getRol() venga sin prefijo, por ejemplo "Gerencia"
        String rolBase = user.getRol();
        // 2) Armás la autoridad con el prefijo exacto que necesita Spring:
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + rolBase)
        );
        System.out.println(rolBase);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getCorreo())
                .password(user.getContrasena())
                .authorities(authorities)   // usamos authorities en vez de roles
                .build();
    }
}
