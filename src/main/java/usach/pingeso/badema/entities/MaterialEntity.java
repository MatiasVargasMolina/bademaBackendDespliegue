package usach.pingeso.badema.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "materiales")
@ToString(exclude = {"detallePedido", "paridadProveedor"})
public class MaterialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    // Un material tiene...

    // puede estar en varios detalles de un pedido
    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    private List<DetallePedidoEntity> detallePedido = new ArrayList<>();

    // puede estar clasificado por proveedor
    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    private List<ProveedorMaterialEntity> paridadProveedor = new ArrayList<>();
}
