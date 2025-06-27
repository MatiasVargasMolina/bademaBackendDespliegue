package usach.pingeso.badema.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrdenCompraListMaterialesDTO {
    private Long id;
    private String numeroOrden;
    private LocalDate fechaEmision;
    private LocalDate fechaEntrega;
    private int estado;
    private String observaciones;
    //private DetalleOrdenCompraDTO materiales;
}
