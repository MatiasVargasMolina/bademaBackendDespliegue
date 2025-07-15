package usach.pingeso.badema.dtos.seguimiento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialConOrdenDTO {
    private Long idMaterial;
    private String nombreMaterial;
    private Long cantidadOrdenesAsociadas;

    public MaterialConOrdenDTO(Long idMaterial, String nombreMaterial, Long cantidadOrdenesAsociadas) {
        this.idMaterial = idMaterial;
        this.nombreMaterial = nombreMaterial;
        this.cantidadOrdenesAsociadas = cantidadOrdenesAsociadas;
    }
}
