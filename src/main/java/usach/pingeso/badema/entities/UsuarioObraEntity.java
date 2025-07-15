package usach.pingeso.badema.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"usuario","obra", "pedidos", "ordenesCompra"})
@Table(name = "usuario_obra")
public class UsuarioObraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rol;
    private LocalDate fechaAsignacion;

    // Un UsuarioObra (Usuario con rol) tiene...

    // Un usuario asociado
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioEntity usuario;

    // Una obra asociada
    @ManyToOne
    @JoinColumn(name = "id_obra")
    private ObraEntity obra;

    // Varios pedidos realizado
    @OneToMany(mappedBy = "responsable", fetch = FetchType.LAZY)
    private List<PedidoEntity> pedidos = new ArrayList<>();

    // Varias ordenes de compra realizadas
    @OneToMany(mappedBy = "responsable", fetch = FetchType.LAZY)
    private List<OrdenCompraEntity> ordenesCompra = new ArrayList<>();
}
