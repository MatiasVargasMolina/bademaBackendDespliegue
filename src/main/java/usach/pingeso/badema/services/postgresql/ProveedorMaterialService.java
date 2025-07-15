package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorMaterialDTO;
import usach.pingeso.badema.entities.MaterialEntity;
import usach.pingeso.badema.entities.ProveedorEntity;
import usach.pingeso.badema.entities.ProveedorMaterialEntity;
import usach.pingeso.badema.repositories.postgresql.MaterialRepository;
import usach.pingeso.badema.repositories.postgresql.ProveedorMaterialRepository;
import usach.pingeso.badema.repositories.postgresql.ProveedorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProveedorMaterialService {
    final
    ProveedorMaterialRepository proveedorMaterialRepository;
    ProveedorRepository proveedorRepository;
    MaterialRepository materialRepository;

    public ProveedorMaterialService(ProveedorMaterialRepository proveedorMaterialRepository,
                                    ProveedorRepository proveedorRepository,
                                    MaterialRepository materialRepository){
        this.proveedorMaterialRepository = proveedorMaterialRepository;
        this.proveedorRepository = proveedorRepository;
        this.materialRepository = materialRepository;
    }

    // Auxiliar para mapear de entidad a una lista de DTOs
    private List<ProveedorMaterialDTO> mapToProveedorMaterialDTOList(List<ProveedorMaterialEntity> unionesPM) {
        List<ProveedorMaterialDTO> unionPMDTOs = new ArrayList<>();

        for (ProveedorMaterialEntity unionPM : unionesPM) {
            ProveedorMaterialDTO dto = mapEntityToSingleDTO(unionPM);
            unionPMDTOs.add(dto);
        }

        return unionPMDTOs;
    }

    // Auxiliar para mapear de DTO a entidad
    private ProveedorMaterialEntity mapDTOToEntity(ProveedorMaterialDTO unionPM) {
        ProveedorMaterialEntity entidad = new ProveedorMaterialEntity();
        entidad.setPrecio(unionPM.getPrecio());
        entidad.setComentarios(unionPM.getComentarios());

        //Se busca con el IdProveedor del DTO el proveedor y JPA obtiene el ID mediante la entidad
        Optional<ProveedorEntity> proveedor = proveedorRepository.findById(unionPM.getIdProveedor());
        if (proveedor.isPresent()) entidad.setProveedor(proveedor.get());
        else throw new EntityNotFoundException("Proveedor no encontrado");

        //Se busca con el IdMaterial del DTO el material y JPA obtiene el ID mediante la entidad
        Optional<MaterialEntity> material = materialRepository.findById(unionPM.getIdMaterial());
        if (material.isPresent()) entidad.setMaterial(material.get());
        else throw new EntityNotFoundException("Material no encontrado");

        return entidad;
    }

    //Auxiliar para mapear una entidad a un dto
    private ProveedorMaterialDTO mapEntityToSingleDTO(ProveedorMaterialEntity entidad) {
        ProveedorMaterialDTO dto = new ProveedorMaterialDTO();
        ProveedorEntity proveedor = entidad.getProveedor();
        MaterialEntity material = entidad.getMaterial();

        dto.setIdMaterial(material.getId());
        dto.setNombreMaterial(material.getNombre());
        dto.setIdProveedor(proveedor.getId());
        dto.setNombreProveedor(proveedor.getNombreProveedor());
        dto.setPrecio(entidad.getPrecio());
        dto.setComentarios(entidad.getComentarios());
        return dto;
    }

    public List<ProveedorMaterialDTO> getAllProveedorMaterial() {
        List<ProveedorMaterialEntity> unionesPM = proveedorMaterialRepository.findAll();
        return mapToProveedorMaterialDTOList(unionesPM);
    }

    public List<ProveedorMaterialEntity> getProveedoresPorMaterialId(Long idMaterial) {
        return proveedorMaterialRepository.findByMaterialId(idMaterial);
    }

    public List<ProveedorMaterialEntity> getMaterialesByProveedorId(Long idProveedor) {
        return proveedorMaterialRepository.findByProveedorId(idProveedor);
    }

    public List<ProveedorMaterialDTO> getProveedorMaterialByMaterialId(Long idMaterial) {
        List<ProveedorMaterialEntity> unionesPM = proveedorMaterialRepository.findByMaterialId(idMaterial);
        return mapToProveedorMaterialDTOList(unionesPM);
    }

    public ProveedorMaterialEntity getByProveedorAndMaterial(Long idProveedor, Long idMaterial) {
        return proveedorMaterialRepository.findByProveedorIdAndMaterialId(idProveedor, idMaterial)
                .orElseThrow(() -> new RuntimeException("No se encontró la relación proveedor-material"));
    }

    public ProveedorMaterialEntity saveProveedorMaterial(Long idProveedor, Long idMaterial,
                                                      ProveedorMaterialDTO unionPM){
        // Verificar que exista el proveedor
        Optional<ProveedorEntity> proveedor = proveedorRepository.findById(idProveedor);
        if (proveedor.isEmpty()) {
            throw new EntityNotFoundException("El proveedor con ID " + idProveedor + " no existe");
        }

        // Verificar que exista el material
        Optional<MaterialEntity> material = materialRepository.findById(idMaterial);
        if (material.isEmpty()) {
            throw new EntityNotFoundException("El material con ID " + idMaterial + " no existe");
        }

        //Creamos el DTO o unión
        unionPM.setIdMaterial(material.get().getId());
        unionPM.setNombreMaterial(material.get().getNombre());
        unionPM.setIdProveedor(proveedor.get().getId());
        unionPM.setNombreProveedor(proveedor.get().getNombreProveedor());
        unionPM.setPrecio(unionPM.getPrecio());
        unionPM.setComentarios(unionPM.getComentarios());

        ProveedorMaterialEntity entidad = mapDTOToEntity(unionPM);
        return proveedorMaterialRepository.save(entidad);
    }

    public ProveedorMaterialDTO updateProveedorMaterial(Long idProveedor, Long idMaterial,
                                                        ProveedorMaterialDTO unionPM) {
        // Verificar que exista el proveedor
        Optional<ProveedorEntity> proveedor = proveedorRepository.findById(idProveedor);
        if (proveedor.isEmpty()) {
            throw new RuntimeException("El proveedor con ID " + idProveedor + " no existe");
        }

        // Verificar que exista el material
        Optional<MaterialEntity> material = materialRepository.findById(idMaterial);
        if (material.isEmpty()) {
            throw new EntityNotFoundException("El material con ID " + idMaterial + " no existe");
        }

        // Verificar que la relación exista
        Optional<ProveedorMaterialEntity> existingUnion = proveedorMaterialRepository
                .findByProveedorIdAndMaterialId(idProveedor, idMaterial);
        if (existingUnion.isEmpty()) {
            throw new EntityNotFoundException("La relación entre el proveedor " + idProveedor +
                    " y el material " + idMaterial + " no existe");
        }
        // Actualizar la relación existente
        ProveedorMaterialEntity entidadExistente = existingUnion.get();
        entidadExistente.setPrecio(unionPM.getPrecio());
        entidadExistente.setComentarios(unionPM.getComentarios());

        // Guardar y devolver la entidad actualizada
        ProveedorMaterialEntity entidadActualizada = proveedorMaterialRepository.save(entidadExistente);
        return mapEntityToSingleDTO(entidadActualizada);
    }

    @Transactional
    public void deleteProveedorMaterial(Long idProveedor, Long idMaterial) {
        Optional<ProveedorMaterialEntity> existingUnion = proveedorMaterialRepository
                .findByProveedorIdAndMaterialId(idProveedor, idMaterial);

        //Verificar que la union proveedor-material exista
        if (existingUnion.isEmpty()) {
            throw new EntityNotFoundException("La relación entre el proveedor " + idProveedor +
                    " y el material " + idMaterial + " no existe");
        }

        //Se desvincula al proveedor del material
        proveedorMaterialRepository.deleteByProveedorIdAndMaterialId(idProveedor, idMaterial);
    }
}