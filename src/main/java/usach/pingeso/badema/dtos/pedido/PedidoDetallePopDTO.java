package usach.pingeso.badema.dtos.pedido;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PedidoDetallePopDTO {
    private Long id;
    private String nombre;
    private String estado;
    private String motivoRechazo;
    private String responsable;
    private LocalDate fechaCreacion;
    private LocalDate fechaEstimadaLlegada;
}