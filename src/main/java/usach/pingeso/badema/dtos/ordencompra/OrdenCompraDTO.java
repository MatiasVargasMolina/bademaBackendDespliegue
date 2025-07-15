package usach.pingeso.badema.dtos.ordencompra;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrdenCompraDTO {
    private Long id;
    private String numeroOrden;
    private LocalDate fechaEmision;
    private LocalDate fechaEntrega;
    private int total;
    private int estado;
    private Long idProveedor;
    private Long idResponsable;
    private List<ItemOrdenCompraDTO> items;
}