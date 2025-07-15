package usach.pingeso.badema.dtos.adquisiciones;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialProveedorPedidoDTO {
    private Long idPedido;             //Para saber a que pedido corresponde
    private String nombrePedido;
    private Long idMaterial;
    private String nombreMaterial;
    private int cantidadRequerida;     //La cantidad requerida por el pedido
    private int cantidadFaltante;      //La cantidad faltante en ese pedido
}