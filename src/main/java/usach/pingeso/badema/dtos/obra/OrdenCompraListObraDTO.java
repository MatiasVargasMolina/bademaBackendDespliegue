package usach.pingeso.badema.dtos.obra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdenCompraListObraDTO {
    private Long id;
    private String numeroOrden;
    private LocalDate fechaEmision;
    private int estado;
}
