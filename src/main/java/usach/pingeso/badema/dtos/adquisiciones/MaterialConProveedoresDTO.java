package usach.pingeso.badema.dtos.adquisiciones;

import lombok.Getter;
import lombok.Setter;
import usach.pingeso.badema.documents.EspecificacionMaterialDocument;

import java.util.List;

@Getter
@Setter
public class MaterialConProveedoresDTO {
    private Long idMaterial;
    private String nombreMaterial;
    private int cantidad;
    private EspecificacionMaterialDocument especificacionesMaterial;

    private List<ManejarAdquisicionesDTO> proveedores;
}