package usach.pingeso.badema.dtos.adquisiciones;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoListAdqDTO {
    private Long id;
    private String nombre;
    private String nombreResponsable;
    private String apellidoResponsable;
    private LocalDate fechaEsperada;
    private List<MaterialListAdqDTO> materiales;
}