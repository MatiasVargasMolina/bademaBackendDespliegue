package usach.pingeso.badema.dtos.ordencompra;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CrearOrdenCompraDTO {
    private Long idProveedor;
    private Long idResponsable;
    private LocalDate fechaEntrega;
    private List<ItemOrdenCompraDTO> items;
}