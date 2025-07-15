package usach.pingeso.badema.dtos.pedido;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class PedidoMaterialDTO {
    private String nombreMaterial;
    private String comentarios;
    private int cantidad;
    private int estado;
    private Map<String, String> especificaciones = new HashMap<>();
}
