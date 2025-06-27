package usach.pingeso.badema.dtos.adquisiciones;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoMaterialAdqDTO {
    private Long pedidoId;
    private String pedidoNombre;
    private String nombreResponsable;
    private String apellidoResponsable;
    private LocalDate fechaEsperada;
    private Long materialId;
    private String materialNombre;
    private Long proveedoresAsignados;
}
