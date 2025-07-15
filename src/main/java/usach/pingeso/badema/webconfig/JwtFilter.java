package usach.pingeso.badema.webconfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import usach.pingeso.badema.services.postgresql.CustomUserDetailsService;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JdbcTemplate jdbcTemplate;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, JdbcTemplate jdbcTemplate, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.jdbcTemplate = jdbcTemplate;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (isPublicRoute(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided");
            return;
        }

        String jwt = authHeader.substring(7).trim();

        if (!jwtUtil.isValid(jwt)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }

        Long idUsuario = jwtUtil.getIdUsuario(jwt);

        Long idObra = -1L;
        try {
            idObra = jdbcTemplate.queryForObject(
                    "SELECT obra_id FROM public.session WHERE user_id = ?",
                    Long.class,
                    idUsuario
            );
        } catch (Exception e) {
            // obraId queda en -1 si no existe
        }

        UserDetails userDetails = customUserDetailsService.loadUserByIdUsuarioAndIdObra(idUsuario, idObra);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }



    private boolean isPublicRoute(String requestURI) {
        return requestURI.startsWith("/auth/"); // Agregar aquí más rutas públicas si es necesario
    }
}
