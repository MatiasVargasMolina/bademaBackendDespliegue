package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.obra.ObraCreateDTO;
import usach.pingeso.badema.dtos.obra.ObraListDTO;
import usach.pingeso.badema.dtos.obra.ObraUpdateDTO;
import usach.pingeso.badema.entities.ObraEntity;
import usach.pingeso.badema.entities.UsuarioEntity;
import usach.pingeso.badema.repositories.postgresql.ObraRepository;
import usach.pingeso.badema.repositories.postgresql.UsuarioRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ObraService {
    final ObraRepository obraRepository;
    final UsuarioRepository usuarioRepository;

    public ObraService(ObraRepository obraRepository, UsuarioRepository usuarioRepository, UsuarioRepository usuarioRepository1) {
        this.obraRepository = obraRepository;
        this.usuarioRepository = usuarioRepository1;
    }

    // Auxiliar para mapear a lista de DTO's
    private List<ObraListDTO> mapToObraListDTO(List<ObraEntity> obras) {
        List<ObraListDTO> obraDTOs = new ArrayList<>();

        for (ObraEntity obra : obras) {
            ObraListDTO dto = new ObraListDTO();
            dto.setId(obra.getId());
            dto.setNombre(obra.getNombre());
            dto.setEmpresaContratista(obra.getEmpresaContratista());
            dto.setEsPublico(obra.isEsPublico());
            dto.setFechaInicio(obra.getFechaInicio());
            dto.setFechaTermino(obra.getFechaTermino());
            dto.setMetrosCuadrados(obra.getMetrosCuadrados());
            dto.setEstado(obra.getEstadoDescripcion());
            obraDTOs.add(dto);
        }

        return obraDTOs;
    }

    // Auxiliar para mapear de DTO a entidad
    private ObraEntity mapDTOToEntity(ObraCreateDTO obra) {
        ObraEntity entidad = new ObraEntity();
        entidad.setNombre(obra.getNombre());
        entidad.setEmpresaContratista(obra.getEmpresaContratista());
        entidad.setEsPublico(obra.isEsPublico());
        entidad.setEstado(ObraEntity.getCodigoEstado(obra.getEstado()));
        entidad.setMetrosCuadrados(obra.getMetrosCuadrados());
        entidad.setFechaInicio(obra.getFechaInicio());
        entidad.setFechaTermino(obra.getFechaTermino());
        return entidad;
    }

    //Auxiliar para mapear una entidad a un dto list
    private ObraListDTO mapEntityToSingleListDTO(ObraEntity entidad) {
        ObraListDTO dto = new ObraListDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setEmpresaContratista(entidad.getEmpresaContratista());
        dto.setEsPublico(entidad.isEsPublico());
        dto.setEstado(entidad.getEstadoDescripcion());
        dto.setMetrosCuadrados(entidad.getMetrosCuadrados());
        dto.setFechaInicio(entidad.getFechaInicio());
        dto.setFechaTermino(entidad.getFechaTermino());
        return dto;
    }

    public List<ObraListDTO> getObrasByIdUsuario(Long id){
        Optional<UsuarioEntity> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) throw new EntityNotFoundException("No se encontró el usuario");
        if (usuario.get().isGerencia()) return mapToObraListDTO(obraRepository.findAll());
        System.out.println("El usuario es de rol Usuario");
        List<ObraEntity> obras = obraRepository.findObrasByIdUsuario(id);
        return mapToObraListDTO(obras);
    }

    public ObraListDTO getObraById(Long id){
        Optional<ObraEntity> obra = obraRepository.findById(id);
        return obra.map(this::mapEntityToSingleListDTO).orElse(null);
    }

    public ObraListDTO getObraByNombre(String nombre){
        Optional<ObraEntity> obra = obraRepository.findByNombre(nombre);
        return obra.map(this::mapEntityToSingleListDTO).orElse(null);
    }

    public List<ObraListDTO> getObraByEstado(String estado) {
        int estadoInt = ObraEntity.getCodigoEstado(estado);
        if (estadoInt == -1) return Collections.emptyList();

        List<ObraEntity> obras = obraRepository.findByEstado(estadoInt);
        return mapToObraListDTO(obras);
    }

    public List<ObraListDTO> getObraByFechaInicio(LocalDate fechaInicio){
        List<ObraEntity> obras = obraRepository.findByFechaInicio(fechaInicio);
        return mapToObraListDTO(obras);
    }

    public List<ObraListDTO> getObraByFechaTermino(LocalDate fechaTermino){
        List<ObraEntity> obras = obraRepository.findByFechaTermino(fechaTermino);
        return mapToObraListDTO(obras);
    }

    public List<ObraListDTO> getObraByEmpresaContratista(String empresaContratista){
        List<ObraEntity> obras = obraRepository.findByEmpresaContratista(empresaContratista);
        return mapToObraListDTO(obras);
    }

    public Long saveObra(ObraCreateDTO obra){
        ObraEntity entidad = mapDTOToEntity(obra);
        ObraEntity entidadGuardada = obraRepository.save(entidad);
        return entidadGuardada.getId();
    }

    public ObraUpdateDTO updateObra(ObraUpdateDTO obra, Long id) {
        Optional<ObraEntity> obraExistente = obraRepository.findById(id);
        if (obraExistente.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la obra");
        }
        ObraEntity entidadActualizada = obraExistente.get();
        entidadActualizada.setNombre(obra.getNombre());
        entidadActualizada.setEstado(ObraEntity.getCodigoEstado(obra.getEstado()));
        entidadActualizada.setFechaTermino(obra.getFechaTermino());
        obraRepository.save(entidadActualizada);
        return obra;
    }
}
