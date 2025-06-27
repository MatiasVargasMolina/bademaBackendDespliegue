package usach.pingeso.badema.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"subContratados", "asociados", "administrativos"})
@Table(name = "obras")
public class ObraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String empresaContratista;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private boolean esPublico;
    private int metrosCuadrados;
    private String direccion;
    private int estado;

    // Una obra tiene...

    // varios SubContratados
    @OneToMany(mappedBy = "obra", fetch = FetchType.LAZY)
    private List<SubContratoEntity> subContratados;

    // varios asociados (Jefes de obra, bodega, etc)
    @OneToMany(mappedBy = "obra", fetch = FetchType.LAZY)
    private List<AsociadosEntity> asociados;

    // varios UsuariosObra/Roles (Administrativos)
    @OneToMany(mappedBy = "obra", fetch = FetchType.LAZY)
    private List<UsuarioObraEntity> administrativos;

    // Métodos auxiliares
    // Mapear el estado
    @Transient
    public String getEstadoDescripcion() {
        return switch (estado) {
            case 0 -> "Estudio de propuesta";
            case 1 -> "Proyecto ofertado";
            case 2 -> "Proyecto adjudicado";
            case 3 -> "Proyecto no adjudicado";
            case 4 -> "En ejecución";
            case 5 -> "Finalizada";
            default -> "Desconocido";
        };
    }

    public static int getCodigoEstado(String descripcion) {
        return switch (descripcion) {
            case "Estudio de propuesta" -> 0;
            case "Proyecto ofertado" -> 1;
            case "Proyecto adjudicado" -> 2;
            case "Proyecto no adjudicado" -> 3;
            case "En ejecución" -> 4;
            case "Finalizada" -> 5;
            default -> -1;
        };
    }

    public ObraEntity(Long id, String nombre, String empresaContratista, LocalDate fechaInicio, LocalDate fechaTermino, boolean esPublico, int metrosCuadrados, int estado){
        this.id = id;
        this.nombre = nombre;
        this.empresaContratista = empresaContratista;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
        this.esPublico = esPublico;
        this.metrosCuadrados = metrosCuadrados;
        this.estado = estado;
    }
}