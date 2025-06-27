package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.adquisiciones.MaterialListAdqDTO;
import usach.pingeso.badema.dtos.pedido.MaterialUpdateDTO;
import usach.pingeso.badema.entities.MaterialEntity;
import usach.pingeso.badema.repositories.postgresql.MaterialRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {
    final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    // Auxiliar para mapear de entidad a DTO
    private List<MaterialListAdqDTO> mapToMaterialListDTO(List<MaterialEntity> materiales) {
        List<MaterialListAdqDTO> materialDTOs = new ArrayList<>();

        for (MaterialEntity material : materiales) {
            MaterialListAdqDTO dto = new MaterialListAdqDTO();
            dto.setId(material.getId());
            dto.setNombre(material.getNombre());
            materialDTOs.add(dto);
        }

        return materialDTOs;
    }

    //Auxiliar para mapear una entidad a un dto detalle
//    private MaterialDetalleDTO mapEntityToSingleDetalleDTO(MaterialEntity entidad) {
//        MaterialDetalleDTO dto = new MaterialDetalleDTO();
//        dto.setNombre(entidad.getNombre());
//        return dto;
//    }

    //Auxiliar para mapear una entidad a un dto list
    private MaterialListAdqDTO mapEntityToSingleListDTO(MaterialEntity entidad) {
        MaterialListAdqDTO dto = new MaterialListAdqDTO();
        dto.setNombre(entidad.getNombre());
        return dto;
    }

    public List<MaterialListAdqDTO> getAllMateriales() {
        List<MaterialEntity> materiales = materialRepository.findAll();
        return mapToMaterialListDTO(materiales);
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
