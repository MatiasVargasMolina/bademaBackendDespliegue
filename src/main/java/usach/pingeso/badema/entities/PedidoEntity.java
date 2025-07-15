package usach.pingeso.badema.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pedidos")
@ToString(exclude = {"responsable", "detallesPedido"})
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private int estado;
    private String motivoRechazo;
    private LocalDate fechaPedido;
    private LocalDate fechaEstimadaLlegada;

    // Un pedido tiene...

    // Un responsable (UsuarioObra)
    @ManyToOne
    @JoinColumn(name = "id_usuario_obra")
    private UsuarioObraEntity responsable;

    // Varios detalles del pedido
    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY)
    private List<DetallePedidoEntity> detallesPedido = new ArrayList<>();


    // Métodos auxiliares
    // Mapear el estado
    @Transient
    public String getEstadoDescripcion() {
        return switch (estado) {
            case 0 -> "Realizado";
            case 1 -> "Finalizado";
            case 2 -> "Rechazado";
            default -> "Desconocido";
        };
    }

    public static int getCodigoEstado(String descripcion){
        return switch (descripcion){
            case "Realizado" -> 0;
            case "Finalizado" -> 1;
            case "Rechazado" -> 2;
            default -> -1;
        };
    }
}
