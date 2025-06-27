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
@ToString(exclude = {"recepcionMaterial"})
public class InventarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaIngreso;
    private int estado;
    private int cantidad;
    private int cantidadInstalada;
    private LocalDate fechaUltimaModificacion;

    // Un inventario tiene...

    // Una recepción asociada
    @OneToOne(mappedBy = "inventario", cascade = CascadeType.ALL)
    private RecepcionMaterialEntity recepcionMaterial;
}