package usach.pingeso.badema.dtos.seguimiento;

import lombok.Getter;
import lombok.Setter;
import usach.pingeso.badema.documents.NuevasEspecificacionesDocument;

import java.time.LocalDate;

@Getter
@Setter
public class SeguimientoDetalleOrdenDTO {
    private Long idDetalleOrden;
    private String nombreMaterial;
    private String nombreProveedor;
    private int cantidadOrdenada;
    private int precioTotal;
    private String estado;
    private LocalDate fechaCompra;
    private LocalDate fechaEstimadaEntrega;
    private int cantidadEntregada;
    private int cantidadInstalada;
    private NuevasEspecificacionesDocument especificaciones;
}
