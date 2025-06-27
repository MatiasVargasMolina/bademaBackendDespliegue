package usach.pingeso.badema.services.postgresql;

import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorCreateDTO;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorDetalleDTO;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorListDTO;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorUpdateDTO;
import usach.pingeso.badema.entities.ProveedorEntity;
import usach.pingeso.badema.repositories.postgresql.ProveedorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {
    final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    // Auxiliar para mapear a lista de DTO's
    private List<ProveedorListDTO> mapToProveedorListDTO(List<ProveedorEntity> proveedores) {
        List<ProveedorListDTO> proveedorDTOs = new ArrayList<>();

        for (ProveedorEntity proveedor : proveedores) {
            ProveedorListDTO dto = new ProveedorListDTO();
            dto.setId(proveedor.getId());
            dto.setNombreProveedor(proveedor.getNombreProveedor());
            dto.setRutProveedor(proveedor.getRutProveedor());
            dto.setTelefonoProveedor(proveedor.getTelefonoProveedor());
            dto.setNombreVendedor(proveedor.getNombreVendedor());
            dto.setTelefonoVendedor(proveedor.getTelefonoVendedor());
            dto.setEmailVendedor(proveedor.getEmailVendedor());
            dto.setDireccionProveedor(proveedor.getDireccionProveedor());
            dto.setCondiciones(proveedor.getCondiciones());
            dto.setRestricciones(proveedor.getRestricciones());
            proveedorDTOs.add(dto);
        }

        return proveedorDTOs;
    }

    // Auxiliar para mapear de DTO a entidad
    private ProveedorEntity mapDTOToEntity(ProveedorCreateDTO proveedor) {
        ProveedorEntity entidad = new ProveedorEntity();
        entidad.setNombreProveedor(proveedor.getNombreProveedor());
        entidad.setRutProveedor(proveedor.getRutProveedor());
        entidad.setTelefonoProveedor(proveedor.getTelefonoProveedor());
        entidad.setNombreVendedor(proveedor.getNombreVendedor());
        entidad.setTelefonoVendedor(proveedor.getTelefonoVendedor());
        entidad.setEmailVendedor(proveedor.getEmailVendedor());
        entidad.setDireccionProveedor(proveedor.getDireccionProveedor());
        entidad.setCondiciones(proveedor.getCondiciones());
        entidad.setRestricciones(proveedor.getRestricciones());
        return entidad;
    }

    //Auxiliar para mapear una entidad a un dto create
    private ProveedorCreateDTO mapEntityToSingleCreateDTO(ProveedorEntity entidad) {
        ProveedorCreateDTO dto = new ProveedorCreateDTO();
        dto.setNombreProveedor(entidad.getNombreProveedor());
        dto.setRutProveedor(entidad.getRutProveedor());
        dto.setTelefonoProveedor(entidad.getTelefonoProveedor());
        dto.setNombreVendedor(entidad.getNombreVendedor());
        dto.setTelefonoVendedor(entidad.getTelefonoVendedor());
        dto.setEmailVendedor(entidad.getEmailVendedor());
        dto.setDireccionProveedor(entidad.getDireccionProveedor());
        dto.setCondiciones(entidad.getCondiciones());
        dto.setRestricciones(entidad.getRestricciones());
        return dto;
    }

    //Auxiliar para mapear una entidad a un dto list
    private ProveedorListDTO mapEntityToSingleListDTO(ProveedorEntity entidad) {
        ProveedorListDTO dto = new ProveedorListDTO();
        dto.setNombreProveedor(entidad.getNombreProveedor());
        dto.setRutProveedor(entidad.getRutProveedor());
        dto.setTelefonoProveedor(entidad.getTelefonoProveedor());
        dto.setNombreVendedor(entidad.getNombreVendedor());
        dto.setTelefonoVendedor(entidad.getTelefonoVendedor());
        dto.setEmailVendedor(entidad.getEmailVendedor());
        dto.setDireccionProveedor(entidad.getDireccionProveedor());
        dto.setCondiciones(entidad.getCondiciones());
        dto.setRestricciones(entidad.getRestricciones());
        return dto;
    }

    public List<ProveedorListDTO> getAllProveedores() {
        List<ProveedorEntity> proveedores = proveedorRepository.findAll();
        return mapToProveedorListDTO(proveedores);
    }

    public ProveedorListDTO getProveedorById(Long id){
        Optional<ProveedorEntity> proveedor = proveedorRepository.findById(id);
        return proveedor.map(this::mapEntityToSingleListDTO).orElse(null);
    }

    public ProveedorCreateDTO saveProveedor(ProveedorCreateDTO proveedor){
        ProveedorEntity entidad = mapDTOToEntity(proveedor);
        ProveedorEntity entidadGuardada = proveedorRepository.save(entidad);
        return mapEntityToSingleCreateDTO(entidadGuardada);
    }

    public ProveedorUpdateDTO updateProveedor(ProveedorUpdateDTO proveedor, Long id) {
        Optional<ProveedorEntity> proveedorExistente = proveedorRepository.findById(id);
        if (proveedorExistente.isEmpty()) {
            return null;
        }
        ProveedorEntity entidadActualizada = proveedorExistente.get();
        entidadActualizada.setNombreProveedor(proveedor.getNombreProveedor());
        entidadActualizada.setRutProveedor(proveedor.getRutProveedor());
        entidadActualizada.setTelefonoProveedor(proveedor.getTelefonoProveedor());
        entidadActualizada.setNombreVendedor(proveedor.getNombreVendedor());
        entidadActualizada.setTelefonoVendedor(proveedor.getTelefonoVendedor());
        entidadActualizada.setEmailVendedor(proveedor.getEmailVendedor());
        entidadActualizada.setDireccionProveedor(proveedor.getDireccionProveedor());
        entidadActualizada.setCondiciones(proveedor.getCondiciones());
        entidadActualizada.setRestricciones(proveedor.getRestricciones());
        proveedorRepository.save(entidadActualizada);
        return proveedor;
    }

    public List<ProveedorDetalleDTO> getProveedoresMaterialByMaterialId(Long idMaterial){
        return proveedorRepository.getProveedoresByMaterialId(idMaterial);
    }
}
