package usach.pingeso.badema.dtos.seguimiento;

import lombok.Getter;
import lombok.Setter;
import usach.pingeso.badema.dtos.ordencompra.ItemOrdenCompraDTO;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrdenCompraSeguimientoDTO {
    private Long id;
    private String numeroOrden;
    private LocalDate fechaEmision;
    private String estado;
    private String responsable;
    private String nombreProveedor;
    private List<ItemOrdenCompraDTO> items;
}
