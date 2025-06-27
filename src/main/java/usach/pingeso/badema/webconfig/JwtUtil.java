package usach.pingeso.badema.webconfig;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
public class JwtUtil {

    // Este es el código secreto que está en la fase de firma del JWT
    // En un ambiente de producción, este valor debe ser guardado en un lugar seguro
    private static final Dotenv dotenv = Dotenv.load();
    private static final String SECRET = dotenv.get("SECRET");
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    private static final String dbName = dotenv.get("DB_NAME");
    // Este metodo crea un JWT con el nombre de usuario
    public String create(String correo, Long idUsuario) {
        return JWT.create()
                .withSubject(correo)
                .withIssuer(dbName)
                .withClaim("idUsuario", idUsuario)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(60)))
                .sign(ALGORITHM);
    }


    // Este metodo verifica si un JWT es válido
    public boolean isValid(String jwt) {
        try {
            JWT.require(ALGORITHM).build().verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println("Token inválido: " + jwt + "error: "+ e.getMessage());
            return false;
        }
    }

    public Long getIdUsuario(String jwt) {
        DecodedJWT decodedJWT = JWT.require(ALGORITHM).build().verify(jwt);
        return decodedJWT.getClaim("idUsuario").asLong();
    }

}
