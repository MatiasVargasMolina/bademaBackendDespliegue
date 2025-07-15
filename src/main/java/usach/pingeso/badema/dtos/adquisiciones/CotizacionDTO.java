package usach.pingeso.badema.dtos.adquisiciones;

import lombok.Data;
import usach.pingeso.badema.documents.ValorEspecificacion;

import java.util.Map;

@Data
public class CotizacionDTO {
    private ProveedorMaterialDTO pvo;
    private Map<String, ValorEspecificacion> nuevasEspecificacionesDocument;
}
