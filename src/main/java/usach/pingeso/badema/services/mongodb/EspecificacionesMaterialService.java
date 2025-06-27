package usach.pingeso.badema.services.mongodb;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import usach.pingeso.badema.documents.EspecificacionMaterialDocument;
import usach.pingeso.badema.entities.MaterialEntity;
import usach.pingeso.badema.repositories.mongodb.EspecificacionMaterialRepository;
import usach.pingeso.badema.services.postgresql.MaterialService;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class EspecificacionesMaterialService {
    private final EspecificacionMaterialRepository especificacionMaterialRepository;
    private final MaterialService materialService;

    public EspecificacionesMaterialService(EspecificacionMaterialRepository repository, MaterialService materialService) {
        this.especificacionMaterialRepository = repository;
        this.materialService = materialService;
    }

    public EspecificacionMaterialDocument getEspecificacionesByMaterialId(Long materialId){
        try{
            return especificacionMaterialRepository.findByMaterialId(materialId);
        }
        catch (Exception e){
            throw new EntityNotFoundException("Especificaciones no encontradas" + e.getMessage());
        }
    }

    public Boolean agregarOActualizarEspecificacion(String idDocumento, String tipo, String valor) {
        try {
            EspecificacionMaterialDocument doc = especificacionMaterialRepository.findById(idDocumento)
                    .orElseThrow(() -> new NoSuchElementException("Documento no encontrado con ID: " + idDocumento));

            doc.getEspecificaciones().put(tipo, valor);
            especificacionMaterialRepository.save(doc);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void eliminarEspecificacion(String idDocumento, String tipo) {
        EspecificacionMaterialDocument doc = especificacionMaterialRepository.findById(idDocumento)
                .orElseThrow(() -> new NoSuchElementException("Documento no encontrado con ID: " + idDocumento));

        if (doc.getEspecificaciones().containsKey(tipo)) {
            doc.getEspecificaciones().remove(tipo);
            especificacionMaterialRepository.save(doc);
        } else {
            throw new IllegalArgumentException("No existe la especificación de tipo: " + tipo);
        }
    }

    public Boolean insertarEspecificaciones(Long idMaterial, Map<String, String> nuevasEspecificaciones) {
        try {
            MaterialEntity material = materialService.getMaterialById(idMaterial);
            if (material == null) {
                return false;
            }

            EspecificacionMaterialDocument doc = new EspecificacionMaterialDocument();
            doc.setEspecificaciones(nuevasEspecificaciones);
            doc.setMaterialId(idMaterial);
            especificacionMaterialRepository.save(doc);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
