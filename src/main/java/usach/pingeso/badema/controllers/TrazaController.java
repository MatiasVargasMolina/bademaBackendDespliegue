package usach.pingeso.badema.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usach.pingeso.badema.dtos.seguimiento.MaterialConOrdenDTO;
import usach.pingeso.badema.dtos.seguimiento.OrdenCompraTrazaDTO;
import usach.pingeso.badema.services.postgresql.TrazaService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/traza")
public class TrazaController {
    private final TrazaService trazaService;

    public TrazaController(TrazaService trazaService) {
        this.trazaService = trazaService;
    }

    //Obtener lista de materiales con cantidad de órdenes asociadas
    @GetMapping("/materiales/{idObra}")
    public ResponseEntity<List<MaterialConOrdenDTO>> getMaterialesConOrdenes(@PathVariable Long idObra) {
        return ResponseEntity.ok(trazaService.getMaterialesConOrdenes(idObra));
    }

    //Obtener la lista de órdenes relacionadas a cada material
    @GetMapping("/ordenes/{idObra}/{idMaterial}")
    public ResponseEntity<List<OrdenCompraTrazaDTO>> getOrdenesByMaterial(@PathVariable Long idObra,
                                                                          @PathVariable Long idMaterial) {
        return ResponseEntity.ok(trazaService.getOrdenesByMaterial(idObra, idMaterial));
    }
}
