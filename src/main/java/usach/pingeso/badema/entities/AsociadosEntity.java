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
@Table(name = "asociados")
@ToString(exclude = "obra")
public class AsociadosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellidos;
    private String rut;
    private String email;
    private String telefono;
    private int rol;

    // Un Subcontratado tiene..

    // una obra asociada
    @ManyToOne
    @JoinColumn(name = "id_obra")
    private ObraEntity obra;

    // Métodos auxiliares
    // Mapear el estado
    @Transient
    public String getRolDescripcion() {
        return switch (rol) {
            case 0 -> "Bodega";
            case 1 -> "Jefe de obra";
            default -> "Desconocido";
        };
    }

    public static int getCodigoRol(String rol) {
        return switch (rol) {
            case "Bodega" -> 0;
            case "Jefe de obra" -> 1;
            default -> -1;
        };
    }
}
