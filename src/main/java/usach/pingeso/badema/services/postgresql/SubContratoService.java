package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.obra.SubContratoDTO;
import usach.pingeso.badema.dtos.obra.SubContratoListDTO;
import usach.pingeso.badema.entities.ObraEntity;
import usach.pingeso.badema.entities.SubContratoEntity;
import usach.pingeso.badema.repositories.postgresql.SubContratoRepository;
import usach.pingeso.badema.repositories.postgresql.ObraRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubContratoService {
    private final SubContratoRepository subContratoRepository;
    private final ObraRepository obraRepository;

    public SubContratoService(SubContratoRepository subContratoRepository, ObraRepository obraRepository) {
        this.subContratoRepository = subContratoRepository;
        this.obraRepository = obraRepository;
    }

    public List<SubContratoListDTO> getSubContratosByObraId(Long idObra){
        return subContratoRepository.getSubContratoEntitiesByObraId(idObra);
    }

    public SubContratoDTO getSubContratoDetalle(Long id){
        return subContratoRepository.getSubContratoDetalleById(id);
    }

    public void guardarSubContratadoConObra(Long idObraExistente, SubContratoDTO subContratoDTO) {
        // 1. Buscar la obra (verificamos que exista)
        Optional<ObraEntity> optionalObra = obraRepository.findById(idObraExistente);
        if (optionalObra.isPresent()) {
            // Verificar si ya existe un subcontratado con ese rut
            if (subContratoRepository.findByRut(subContratoDTO.getRut()).isPresent()) {
                throw new IllegalArgumentException("Ya existe un subcontratado registrado con este rut");
            }

            SubContratoEntity subContratado = getSubContratoEntity(subContratoDTO, optionalObra.get());

            // 3. Guardar el subcontratado
            subContratoRepository.save(subContratado);
        } else {
            throw new RuntimeException("La obra con ID " + idObraExistente + " no existe.");
        }
    }

    private static SubContratoEntity getSubContratoEntity(SubContratoDTO subContratoDTO,ObraEntity obra) {

        // 2. Crear el subcontratado y asignarle la obra
        SubContratoEntity subContratado = new SubContratoEntity();
        subContratado.setNombre(subContratoDTO.getNombre());
        subContratado.setApellidos(subContratoDTO.getApellidos());
        subContratado.setRut(subContratoDTO.getRut());
        subContratado.setEmail(subContratoDTO.getEmail());
        subContratado.setTelefono(subContratoDTO.getTelefono());
        subContratado.setAreaTrabajo(subContratoDTO.getAreaTrabajo());
        subContratado.setObra(obra);
        return subContratado;
    }

    public void deleteSubContrato(Long id){
        try{
            subContratoRepository.deleteById(id);
        }
        catch(Exception e){
            throw new RuntimeException("No se pudo eliminar el subcontratado con el id" + id + e.getMessage());
        }
    }

    public void updateSubcontratado(Long id,SubContratoDTO subContratoDTO){
        Optional<SubContratoEntity> optionalSubContrato = subContratoRepository.findById(id);
        if (optionalSubContrato.isPresent()) {
            SubContratoEntity subContratado = optionalSubContrato.get();
            subContratado.setNombre(subContratoDTO.getNombre());
            subContratado.setApellidos(subContratoDTO.getApellidos());
            subContratado.setRut(subContratoDTO.getRut());
            subContratado.setEmail(subContratoDTO.getEmail());
            subContratado.setTelefono(subContratoDTO.getTelefono());
            subContratado.setAreaTrabajo(subContratoDTO.getAreaTrabajo());
            subContratoRepository.save(subContratado);
        }
        else{
            throw new EntityNotFoundException("No se encuentra el subcontratado con ese id");
        }
    }
}
