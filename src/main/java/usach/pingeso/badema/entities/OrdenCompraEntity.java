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
@Table(name = "ordenes_de_compra")
@ToString(exclude = {"responsable", "detalles"})
public class OrdenCompraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroOrden;
    private LocalDate fechaEmision;
    private LocalDate fechaEntrega;
    private int estado;
    private String observaciones;

    // Una orden de compra tiene...

    // Un responsable (usuario obra) asociado
    @ManyToOne
    @JoinColumn(name = "id_usuario_obra")
    private UsuarioObraEntity responsable;

    // Varios detalles de orden de compra
    @OneToMany(mappedBy = "ordenCompra", fetch = FetchType.LAZY)
    private List<DetalleOrdenCompraEntity> detalles;

    // Métodos auxiliares
    // Mapear el estado
//    @Transient
//    public String getEstadoDescripcion() {
//        return switch (estado) {
//            case 0 -> "Realizada";
//            case 1 -> "Finalizada";
//            case 2 -> "Cancelada";
//            default -> "Desconocido";
//        };
//    }
//
//    public static int getCodigoEstado(String descripcion){
//        return switch (descripcion){
//            case "Realizada" -> 0;
//            case "Finalizada" -> 1;
//            case "Cancelada" -> 2;
//            default -> -1;
//        };
//    }
}
