package usach.pingeso.badema.dtos.ordencompra;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearItemOrdenRequestDTO {
    private int cantidad;
    private String observaciones;
}
