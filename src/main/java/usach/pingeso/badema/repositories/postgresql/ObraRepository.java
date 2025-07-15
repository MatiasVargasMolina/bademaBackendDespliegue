package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.entities.ObraEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ObraRepository extends JpaRepository<ObraEntity, Long> {
    List<ObraEntity> findByEstado(int estado);
    Optional<ObraEntity> findByNombre(String nombre);
    List<ObraEntity> findByFechaInicio(LocalDate fechaInicio);
    List<ObraEntity> findByFechaTermino(LocalDate fechaTermino);
    List<ObraEntity> findByEmpresaContratista(String empresaContratista);

    @Query("SELECT new usach.pingeso.badema.entities.ObraEntity(o.id, o.nombre, o.empresaContratista, o.fechaInicio, " +
            "o.fechaTermino, o.esPublico, o.metrosCuadrados, o.estado, o.direccion) " +
            "FROM UsuarioObraEntity uo JOIN uo.obra o "+
            "WHERE uo.usuario.isGerencia = false "+
            "AND uo.usuario.id = :idUsuario "+
            "AND " +
            "( "+
                "("+
                "(uo.rol = 'Administrador de obra' OR uo.rol = 'Oficina tecnica')" +
                " AND o.estado IN (3 ,4 ,5)"+
                ")"+
                "OR " +
                "(o.estado IN (4, 5)" +
            ")" +
            ")")
    List<ObraEntity> findObrasByIdUsuario(Long idUsuario);
}
