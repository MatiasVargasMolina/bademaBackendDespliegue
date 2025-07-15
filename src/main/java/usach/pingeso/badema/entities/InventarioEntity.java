package usach.pingeso.badema.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventarios")
@ToString(exclude = {"detalleOrdenCompra"})
public class InventarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaIngreso;
    private int cantidad;
    private int cantidadInstalada;
    private LocalDate fechaUltimaModificacion;

    @OneToOne(mappedBy = "inventario")
    private DetalleOrdenCompraEntity detalleOrdenCompra;
}