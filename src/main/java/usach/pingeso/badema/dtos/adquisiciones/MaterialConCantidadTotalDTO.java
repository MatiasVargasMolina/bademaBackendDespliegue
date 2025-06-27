package usach.pingeso.badema.dtos.adquisiciones;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialConCantidadTotalDTO {
    private Long idMaterial;
    private String nombre;
    private int cantidad; //Cantidad total del material pedido en toda la obra (puede estar repartido en muchos pedidos)
}