package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.adquisiciones.MaterialListAdqDTO;
import usach.pingeso.badema.dtos.pedido.MaterialUpdateDTO;
import usach.pingeso.badema.dtos.seguimiento.MaterialConOrdenDTO;
import usach.pingeso.badema.entities.MaterialEntity;
import usach.pingeso.badema.repositories.postgresql.MaterialRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {
    final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    //Auxiliar para mapear una entidad a un dto list
    private MaterialListAdqDTO mapEntityToSingleListDTO(MaterialEntity entidad) {
        MaterialListAdqDTO dto = new MaterialListAdqDTO();
        dto.setNombre(entidad.getNombre());
        return dto;
    }

    public MaterialEntity getMaterialById(Long id){
        Optional<MaterialEntity> material = materialRepository.findById(id);
        if (material.isPresent()) return material.get();
        else throw new EntityNotFoundException("Material no encontrado");
    }

    public MaterialListAdqDTO getMaterialByNombre(String nombre){
        Optional<MaterialEntity> material = materialRepository.findByNombre(nombre);
        return material.map(this::mapEntityToSingleListDTO).orElse(null);
    }

    public List<MaterialConOrdenDTO> getMaterialesConOrdenes(Long idObra) {
        return materialRepository.getMaterialesConOrdenes(idObra);
    }

    public MaterialEntity saveMaterial(String nombre){
        MaterialEntity entidad = new MaterialEntity();
        entidad.setNombre(nombre);
        return materialRepository.save(entidad);
    }

    public MaterialUpdateDTO updateMaterial(MaterialUpdateDTO material, Long id) {
        Optional<MaterialEntity> materialExistente = materialRepository.findById(id);
        if (materialExistente.isEmpty()) {
            return null;
        }
        MaterialEntity entidadActualizada = materialExistente.get();
        entidadActualizada.setNombre(material.getNombre());
        materialRepository.save(entidadActualizada);
        return material;
    }
}
