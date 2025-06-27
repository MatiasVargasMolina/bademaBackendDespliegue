package usach.pingeso.badema.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usach.pingeso.badema.dtos.obra.AsociadosDTO;
import usach.pingeso.badema.services.postgresql.AsociadoService;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/asociado")
public class AsociadosController {
    private final AsociadoService asociadoService;

    public AsociadosController(AsociadoService asociadoService) {
        this.asociadoService = asociadoService;
    }

    @GetMapping("/id/{id}")
    public AsociadosDTO getAsociadoDetalleById(@PathVariable Long id){
        return asociadoService.getAsociadoDetalleById(id);
    }

    @PostMapping("/guardar/{idObra}")
    public ResponseEntity<Void> saveAsociado(@RequestBody AsociadosDTO asociadoDTO, @PathVariable Long idObra){
        asociadoService.guardarAsociadoConObra(idObra, asociadoDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubcontratado(@PathVariable Long id){
        asociadoService.deleteAsociado(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateSubcontratado(@PathVariable Long id, @RequestBody AsociadosDTO asociadoDTO){
        asociadoService.updateSubcontratado(id,asociadoDTO);
        return ResponseEntity.ok().build();
    }
}
