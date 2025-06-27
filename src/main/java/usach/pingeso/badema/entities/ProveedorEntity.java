package usach.pingeso.badema.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "proveedores")
@ToString(exclude = {"materialesAsociados"})
public class ProveedorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // Un proveedor tiene..

    // varias clasificaciones de proveedor con materiales
    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY)
    private List<ProveedorMaterialEntity> materialesAsociados;
}
