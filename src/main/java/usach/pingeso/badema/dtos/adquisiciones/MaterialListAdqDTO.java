package usach.pingeso.badema.dtos.adquisiciones;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialListAdqDTO {
    private Long id;
    private String nombre;
    private Long proveedoresAsignados;
}