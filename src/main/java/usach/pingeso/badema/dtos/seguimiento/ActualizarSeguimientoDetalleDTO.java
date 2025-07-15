package usach.pingeso.badema.dtos.seguimiento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarSeguimientoDetalleDTO {
    private int cantidadEntregada;
    private int cantidadInstalada;
    private String incidencias;
}
