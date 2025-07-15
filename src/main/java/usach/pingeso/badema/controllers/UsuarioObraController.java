package usach.pingeso.badema.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usach.pingeso.badema.dtos.obra.UsuarioListDTO;
import usach.pingeso.badema.dtos.obra.UsuarioObraDTO;
import usach.pingeso.badema.services.postgresql.UsuarioObraService;
import usach.pingeso.badema.services.postgresql.UsuarioService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/administrativo")
public class UsuarioObraController {
    private
    final UsuarioObraService usuarioObraService;
    final UsuarioService usuarioService;

    @Autowired
    public UsuarioObraController(UsuarioObraService usuarioObraService, UsuarioService usuarioService) {
        this.usuarioObraService = usuarioObraService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/obra/{idObra}")
    public ResponseEntity<List<UsuarioListDTO>> getUsuarioListDTONoInObraId(@PathVariable Long idObra){
        List<UsuarioListDTO> dto = usuarioService.getUsuariosNotInObraId(idObra);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/save")
    public ResponseEntity<Void> saveUsuarioObra(@RequestBody UsuarioObraDTO usuario){
        usuarioObraService.saveUsuarioObra(usuario.getIdObra(), usuario.getIdUsuario(), usuario.getRol());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUsuarioObra(@PathVariable Long id){
        usuarioObraService.deleteUsuarioObra(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update/{id}")
    public ResponseEntity<UsuarioObraDTO> updateUsuarioObra(@RequestBody String rol, @PathVariable Long id){
        usuarioObraService.updateRolUsuarioObra(id, rol);
        return ResponseEntity.ok().build();
    }
}
