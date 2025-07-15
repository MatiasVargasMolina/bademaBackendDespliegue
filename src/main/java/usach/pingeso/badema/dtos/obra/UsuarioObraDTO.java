package usach.pingeso.badema.dtos.obra;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioObraDTO {
    private Long idUsuario;
    private Long idObra;
    private String rol;
}
