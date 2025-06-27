package usach.pingeso.badema.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "detalle_pedido")
@ToString(exclude = {"pedido","material"})
public class DetallePedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentarios;
    private int cantidad;
    private int estado;

    // Un Detalle pedido tiene..

    // Un pedido asociado
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoEntity pedido;

    // Un material asociado
    @ManyToOne
    @JoinColumn(name = "id_material")
    private MaterialEntity material;
}
