package usach.pingeso.badema.dtos.adquisiciones;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProveedorMaterialDTO {
    private Long id;
    private Long idMaterial;
    private String nombreMaterial;
    private Long idProveedor;
    private String nombreProveedor;
    private int precio;
    private String comentarios;
}