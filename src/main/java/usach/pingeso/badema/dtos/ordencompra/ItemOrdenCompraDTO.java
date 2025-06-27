package usach.pingeso.badema.dtos.ordencompra;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemOrdenCompraDTO {
    private Long idMaterial;
    private String nombreMaterial;
    private int cantidad;
    private int precioTotal; //Precio unitario que ofrece el proveedor * cantidad
    private String observaciones;
}