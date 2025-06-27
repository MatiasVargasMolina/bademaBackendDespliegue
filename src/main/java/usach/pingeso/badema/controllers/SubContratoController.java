package usach.pingeso.badema.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usach.pingeso.badema.dtos.obra.SubContratoDTO;
import usach.pingeso.badema.services.postgresql.SubContratoService;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/subcontrato")
public class SubContratoController {
    private final SubContratoService subContratoService;

    public SubContratoController(SubContratoService subContratoService) {
        this.subContratoService = subContratoService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SubContratoDTO> getSubContratoDetalleById(@PathVariable Long id){
        return ResponseEntity.ok(subContratoService.getSubContratoDetalle(id));
    }

    @PostMapping("/guardar/{idObra}")
    public ResponseEntity<Void> saveSubcontratrado(@PathVariable Long idObra, @RequestBody SubContratoDTO subContratoDTO){
        subContratoService.guardarSubContratadoConObra(idObra, subContratoDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubcontratado(@PathVariable Long id){
        subContratoService.deleteSubContrato(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateSubcontratado(@PathVariable Long id, @RequestBody SubContratoDTO subContratoDTO){
        subContratoService.updateSubcontratado(id,subContratoDTO);
        return ResponseEntity.ok().build();
    }
}
