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
@Table(name = "proveedores_y_materiales")
@ToString(exclude = {"proveedor", "material", "detallesOrdenesCompra"})
public class ProveedorMaterialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;
    private String comentarios;

    // Un provedoor material tiene...

    // Un proveedor asociado
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private ProveedorEntity proveedor;

    // Un material asociado
    @ManyToOne
    @JoinColumn(name = "id_material")
    private MaterialEntity material;

    // varios detalles de ordenes de compra
    @OneToMany(mappedBy = "paridadProveedor", fetch = FetchType.LAZY)
    private List<DetalleOrdenCompraEntity> detallesOrdenesCompra;
}
