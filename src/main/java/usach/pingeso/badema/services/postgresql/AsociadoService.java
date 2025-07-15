package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.obra.AsociadosDTO;
import usach.pingeso.badema.dtos.obra.AsociadosListDTO;
import usach.pingeso.badema.entities.AsociadosEntity;
import usach.pingeso.badema.entities.ObraEntity;
import usach.pingeso.badema.repositories.postgresql.AsociadoRepository;
import usach.pingeso.badema.repositories.postgresql.ObraRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AsociadoService {
    private final AsociadoRepository asociadoRepository;
    private final ObraRepository obraRepository;

    public AsociadoService(AsociadoRepository asociadoRepository, ObraRepository obraRepository) {
        this.asociadoRepository = asociadoRepository;
        this.obraRepository = obraRepository;
    }

    public List<AsociadosListDTO> getAsociadosListDTOByIdObra (Long idObra){
        return asociadoRepository.getAsociadosListDTOByObraId(idObra);
    }

    public AsociadosDTO getAsociadoDetalleById(Long id){
        return asociadoRepository.getAsociadoDetalleById(id);
    }

    public void guardarAsociadoConObra(Long idObraExistente, AsociadosDTO asociadoDTO) {
        // 1. Buscar la obra (verificamos que exista)
        Optional<ObraEntity> optionalObra = obraRepository.findById(idObraExistente);
        if (optionalObra.isPresent()) {
            ObraEntity obra = optionalObra.get();

            // Verificación si ya existe un asociado con ese rut
            if (asociadoRepository.findByRut(asociadoDTO.getRut()).isPresent()) {
                throw new IllegalArgumentException("Ya existe un asociado registrado con este rut");
            }

            // 2. Crear el subcontratado y asignarle la obra
            AsociadosEntity asociado = new AsociadosEntity();
            asociado.setNombre(asociadoDTO.getNombre());
            asociado.setApellidos(asociadoDTO.getApellidos());
            asociado.setRut(asociadoDTO.getRut());
            asociado.setEmail(asociadoDTO.getEmail());
            asociado.setTelefono(asociadoDTO.getTelefono());
            asociado.setRol(asociadoDTO.getRol());
            asociado.setObra(obra);

            // 3. Guardar el subcontratado
            asociadoRepository.save(asociado);
        } else {
            throw new EntityNotFoundException("La obra con ID " + idObraExistente + " no existe.");
        }
    }

    public void deleteAsociado(Long id){
        try{
            asociadoRepository.deleteById(id);
        }
        catch(Exception e){
            throw new RuntimeException("No se pudo eliminar el subcontratado con el id" + id + e.getMessage());
        }
    }

    public void updateSubcontratado(Long id, AsociadosDTO asociadoDTO){
        Optional<AsociadosEntity> optionalAsociado = asociadoRepository.findById(id);
        if (optionalAsociado.isPresent()) {
            AsociadosEntity asociado = optionalAsociado.get();
            asociado.setNombre(asociadoDTO.getNombre());
            asociado.setApellidos(asociadoDTO.getApellidos());
            asociado.setRut(asociadoDTO.getRut());
            asociado.setEmail(asociadoDTO.getEmail());
            asociado.setTelefono(asociadoDTO.getTelefono());
            asociado.setRol(asociadoDTO.getRol());
            asociadoRepository.save(asociado);
        }
        else{
            throw new EntityNotFoundException("No se encuentra el subcontratado con ese id");
        }
    }
}
