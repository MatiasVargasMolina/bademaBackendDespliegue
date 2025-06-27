package usach.pingeso.badema.dtos.adquisiciones;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProveedorMaterialDetalleDTO {
    private Long idMaterial;
    private String nombreMaterial;
    private Long idProveedor;
    private String nombreProveedor;
    private String condicion;
    private int precio;
    private String restricciones;
    private String comentarios;
    //private int nuevasEspecificaciones; //Especificaciones de proveedor-material
}
