package usach.pingeso.badema.dtos.pedido;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class PedidoCreateDTO {
    private Long idResponsable;
    private Long idObra;
    private String nombre;
    private int estado;
    private LocalDate fechaEstimadaLlegada;
    private List<PedidoMaterialDTO> materiales;
}
