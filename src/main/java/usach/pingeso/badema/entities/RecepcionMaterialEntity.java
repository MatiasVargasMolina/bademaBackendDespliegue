package usach.pingeso.badema.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recepcion_materiales")
@ToString(exclude = {"detalleOrdenCompra"})
public class RecepcionMaterialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaRecepcion;
    private int cantidadRecibida;
    private String incidencias;

    @ManyToOne
    @JoinColumn(name = "id_detalle_orden_compra")
    private DetalleOrdenCompraEntity detalleOrdenCompra;
}
