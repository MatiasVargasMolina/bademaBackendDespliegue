package usach.pingeso.badema.dtos.adquisiciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class ProveedorDetalleDTO {
    private Long id;
    private String nombreProveedor;
    private String rutProveedor;
    private String telefonoProveedor;
    private String nombreVendedor;
    private String telefonoVendedor;
    private String emailVendedor;
    private String direccionProveedor;
    private String condiciones;
    private String restricciones;
}
