package usach.pingeso.badema.services.mongodb;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import usach.pingeso.badema.documents.NuevasEspecificacionesDocument;
import usach.pingeso.badema.documents.ValorEspecificacion;
import usach.pingeso.badema.entities.ProveedorMaterialEntity;
import usach.pingeso.badema.repositories.mongodb.NuevasEspecificacionesArchivosRepository;

import java.util.Map;

@Service
public class NuevasEspecificacionesService {
    private final NuevasEspecificacionesArchivosRepository nuevasEspecificacionesArchivosRepository;

    public NuevasEspecificacionesService(NuevasEspecificacionesArchivosRepository nuevasEspecificacionesArchivosRepository) {
        this.nuevasEspecificacionesArchivosRepository = nuevasEspecificacionesArchivosRepository;
    }

    public NuevasEspecificacionesDocument getNuevasEspecificacionesByProveedorMaterialId(Long proveedorMaterialId){
        try{
            return nuevasEspecificacionesArchivosRepository.findByIdProveedorMaterial(proveedorMaterialId);
        }
        catch (Exception e){
            throw new EntityNotFoundException("Nuevas Especificaciones no encontradas" + e.getMessage());
        }
    }

    public boolean insertarNuevasEspecificaciones(Map<String, ValorEspecificacion> nuevasEspecificaciones, ProveedorMaterialEntity paridad) {

        NuevasEspecificacionesDocument doc = new NuevasEspecificacionesDocument();
        doc.setEspecificacionesNuevas(nuevasEspecificaciones);
        doc.setIdProveedorMaterial(paridad.getId());

        try{
            nuevasEspecificacionesArchivosRepository.save(doc);
            return true;
        }
        catch (Exception e){
            throw new RuntimeException("No se pudo insertar la especificacion: ", e);
        }
    }
}
