package usach.pingeso.badema.dtos.obra;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ObraUpdateDTO {
    private String nombre;
    private String estado;
    private LocalDate fechaTermino;
}
