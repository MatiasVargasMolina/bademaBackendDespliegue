package usach.pingeso.badema.webconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilterr) {
        this.jwtFilter = jwtFilterr;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors((cors) -> {})
                .authorizeHttpRequests(auth -> auth
                        //Obra
                        .requestMatchers("/badema/api/obra/obras/**").permitAll()
                        .requestMatchers("/badema/api/obra/nombre/**").hasAnyRole("Gerencia", "Usuario", "ADMIN")
                        .requestMatchers("/badema/api/obra/estado/**").hasAnyRole("Gerencia", "Usuario", "ADMIN")
                        .requestMatchers("/badema/api/obra/fechaInicio/**").hasAnyRole("Gerencia", "Usuario", "ADMIN")
                        .requestMatchers("/badema/api/obra/fechaTermino/**").hasAnyRole("Gerencia", "Usuario", "ADMIN")
                        .requestMatchers("/badema/api/obra/empresa/**").hasAnyRole("Gerencia", "Usuario", "ADMIN")
                        .requestMatchers("/badema/api/obra/id/**").permitAll()
                        .requestMatchers("/badema/api/obra/guardar").permitAll()
                        .requestMatchers("/badema/api/obra/actualizar/**").hasAnyRole("Gerencia","ADMIN")
                        .requestMatchers("/badema/api/obra/subir/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")
                        .requestMatchers("/badema/api/obra/archivos/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")
                        .requestMatchers("/badema/api/obra/archivos/pdf/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")

                        //Hitos
                        .requestMatchers("/badema/api/hito/agregar/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")
                        .requestMatchers("/badema/api/hito/eliminar/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")

                        //Asociado
                        .requestMatchers("/badema/api/asociado/id/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")
                        .requestMatchers("/badema/api/asociado/guardar/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")
                        .requestMatchers("/badema/api/asociado/delete/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")
                        .requestMatchers("/badema/api/asociado/update/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")

                        //Subcontrato
                        .requestMatchers("/badema/api/subcontrato/id/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")
                        .requestMatchers("/badema/api/subcontrato/guardar/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")
                        .requestMatchers("/badema/api/subcontrato/delete/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")
                        .requestMatchers("/badema/api/subcontrato/update/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")

                        //Administrativo (usuario-obra)
                        .requestMatchers("/badema/api/administrativo/obra/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")
                        .requestMatchers("/badema/api/administrativo/save/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")
                        .requestMatchers("/badema/api/administrativo/update/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")
                        .requestMatchers("/badema/api/administrativo/delete/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")

                        //Pedido
                        .requestMatchers("/badema/api/pedido/guardar").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Oficina tecnica","Prevencionista de riesgos")
                        .requestMatchers("/badema/api/pedido/pedidos/adquisiciones/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica", "Prevencionista de riesgos")
                        .requestMatchers("/badema/api/pedido/detallePop/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica", "Prevencionista de riesgos")
                        .requestMatchers("/badema/api/pedido/subir/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Oficina tecnica")
                        .requestMatchers("/badema/api/pedido/archivos/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Oficina tecnica")
                        .requestMatchers("/badema/api/pedido/archivos/pdf/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Oficina tecnica")

                        //Proveedor
                        .requestMatchers("/badema/api/proveedor/proveedores").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")
                        .requestMatchers("/badema/api/proveedor/id/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")
                        .requestMatchers("/badema/api/proveedor/guardar").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")
                        .requestMatchers("/badema/api/proveedor/actualizar/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")

                        //Material-Provoeedor
                        .requestMatchers("/badema/api/proveedormaterial/unionesPM").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones")
                        .requestMatchers("/badema/api/proveedormaterial/materialproveedor/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones")
                        .requestMatchers("/badema/api/proveedormaterial/guardar/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones")
                        .requestMatchers("/badema/api/proveedormaterial/actualizar/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones")
                        .requestMatchers("/badema/api/proveedormaterial/eliminar/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones")

                        //Materiales
                        .requestMatchers("/badema/api/material/materiales").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")
                        .requestMatchers("/badema/api/material/id/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")
                        .requestMatchers("/badema/api/material/nombre/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")
                        .requestMatchers("/badema/api/material/actualizar/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")

                        //Manejar adquisiciones
                        .requestMatchers("/badema/api/manejarAdquisiciones/pedidos/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")
                        .requestMatchers("/badema/api/manejarAdquisiciones/materiales/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")
                        .requestMatchers("/badema/api/manejarAdquisiciones/materialesPorProveedor/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")
                        .requestMatchers("/badema/api/manejarAdquisiciones/detalleProveedorMaterial/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")
                        .requestMatchers("/badema/api/manejarAdquisiciones/itemOrden/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")

                        //Orden de compra
                        .requestMatchers("/badema/api/ordencompra/guardar").permitAll()
                        .requestMatchers("/badema/api/ordencompra/documento/**").permitAll()
                        .requestMatchers("/badema/api/ordencompra/subir/**").permitAll()
                        .requestMatchers("/badema/api/ordencompra/archivos/**").permitAll()
                        .requestMatchers("/badema/api/ordencompra/archivos/pdf/**").permitAll()

                        //Seguimiento orden de compra
                        .requestMatchers("/badema/api/seguimiento/ordenes/**").permitAll()
                        .requestMatchers("/badema/api/seguimiento/detalle/**").permitAll()
                        .requestMatchers("/badema/api/seguimiento/detalle/actualizar/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")
                        .requestMatchers("/badema/api/seguimiento/guias/subir/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")
                        .requestMatchers("/badema/api/seguimiento/guias/archivos/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")
                        .requestMatchers("/badema/api/seguimiento/guias/archivos/pdf/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra",
                                "Adquisiciones","Oficina tecnica")

                        //Traza
                        .requestMatchers("/badema/api/traza/materiales/**").hasAnyRole(
                                "ADMIN", "Gerencia", "Administrador de obra", "Oficina tecnica", "Adquisiciones"
                        )
                        .requestMatchers("/badema/api/traza/ordenes/**").hasAnyRole(
                                "ADMIN", "Gerencia", "Administrador de obra", "Oficina tecnica", "Adquisiciones"
                        )

                        //Archivos
                        .requestMatchers("/badema/api/archivos/pdf/**").hasAnyRole("ADMIN","Gerencia","Administrador de obra")

                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session // Configura la política de creación de sesiones
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No se crean sesiones
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Agrega el filtro de JWT antes del filtro de autenticación
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}