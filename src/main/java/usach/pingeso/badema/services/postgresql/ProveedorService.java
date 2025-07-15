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
import org.springframework.beans.BeanUtils;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class ProveedorService {
    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    // Método general para mapear listas
    private <T, U> List<U> mapList(List<T> origen, Function<T, U> mapper) {
        List<U> resultado = new ArrayList<>();
        for (T item : origen) {
            resultado.add(mapper.apply(item));
        }
        return resultado;
    }

    // Método ultra genérico usando reflection para copiar propiedades con mismo nombre
    private <S, D> D mapWithReflection(S source, Supplier<D> targetSupplier) {
        D target = targetSupplier.get();
        try {
            BeanUtils.copyProperties(source, target); // usa org.springframework.beans.BeanUtils
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear con reflexión", e);
        }
        return target;
    }

    // Métodos públicos del servicio
    public List<ProveedorListDTO> getAllProveedores() {
        List<ProveedorEntity> entidades = proveedorRepository.findAll();
        return mapList(entidades, entidad -> mapWithReflection(entidad, ProveedorListDTO::new));
    }

    public ProveedorListDTO getProveedorById(Long id) {
        return proveedorRepository.findById(id)
                .map(entidad -> mapWithReflection(entidad, ProveedorListDTO::new))
                .orElse(null);
    }

    public ProveedorCreateDTO saveProveedor(ProveedorCreateDTO dto) {
        if (proveedorRepository.findByRutProveedor(dto.getNombreProveedor()).isPresent()){
            throw new RuntimeException("Ya existe un proveedor con el rut del proveedor");
        }
        ProveedorEntity entidad = mapWithReflection(dto, ProveedorEntity::new);
        ProveedorEntity guardada = proveedorRepository.save(entidad);
        return mapWithReflection(guardada, ProveedorCreateDTO::new);
    }

    public ProveedorUpdateDTO updateProveedor(ProveedorUpdateDTO dto, Long id) {
        return proveedorRepository.findById(id).map(entidad -> {
            BeanUtils.copyProperties(dto, entidad);
            proveedorRepository.save(entidad);
            return dto;
        }).orElse(null);
    }

    public List<ProveedorDetalleDTO> getProveedoresMaterialByMaterialId(Long idMaterial) {
        return proveedorRepository.getProveedoresByMaterialId(idMaterial);
    }
}
