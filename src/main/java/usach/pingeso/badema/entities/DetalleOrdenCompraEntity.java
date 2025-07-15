package usach.pingeso.badema.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "detalle_orden_de_compra")
@ToString(exclude = {"ordenCompra", "paridadProveedor", "recepciones", "inventario"})
public class DetalleOrdenCompraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String observacion;
    private int cantidad;
    private int precioUnitario;

    // Un detalle de orden de compra tiene...

    // Una orden de compra asociada
    @ManyToOne
    @JoinColumn(name = "id_orden_de_compra")
    private OrdenCompraEntity ordenCompra;

    // Una clasificacion de proveedor por material
    @ManyToOne
    @JoinColumn(name = "id_proveedor_material")
    private ProveedorMaterialEntity paridadProveedor;

    // varias recepciones de materiales
    @OneToMany(mappedBy = "detalleOrdenCompra", fetch = FetchType.LAZY)
    private List<RecepcionMaterialEntity> recepciones;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_inventario")
    private InventarioEntity inventario;
}
