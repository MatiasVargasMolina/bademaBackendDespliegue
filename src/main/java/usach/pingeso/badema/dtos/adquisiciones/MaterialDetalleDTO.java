package usach.pingeso.badema.dtos.adquisiciones;

import lombok.Getter;
import lombok.Setter;
import usach.pingeso.badema.documents.EspecificacionMaterialDocument;

import java.util.List;

@Getter
@Setter
public class MaterialDetalleDTO {
    private Long id;
    private String nombre;
    private EspecificacionMaterialDocument especificacionesMaterial;
    private List<ProveedorDetalleDTO> proveedores;
}
