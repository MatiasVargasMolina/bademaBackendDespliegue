package usach.pingeso.badema.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sub_contratos")
@ToString(exclude = "obra")
public class SubContratoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellidos;
    private String rut;
    private String email;
    private String telefono;
    private String areaTrabajo;

    // Un Subcontratado tiene..

    // una obra asociada
    @ManyToOne
    @JoinColumn(name = "id_obra")
    private ObraEntity obra;
}
