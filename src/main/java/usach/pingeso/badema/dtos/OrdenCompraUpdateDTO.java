package usach.pingeso.badema.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrdenCompraUpdateDTO {
    private LocalDate fechaEntrega;
    private int estado;
    private String observaciones;
}
