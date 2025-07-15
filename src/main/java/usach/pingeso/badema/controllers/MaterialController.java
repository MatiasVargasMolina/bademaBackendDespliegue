package usach.pingeso.badema.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usach.pingeso.badema.dtos.adquisiciones.MaterialDetalleDTO;
import usach.pingeso.badema.dtos.adquisiciones.MaterialListAdqDTO;
import usach.pingeso.badema.dtos.pedido.MaterialUpdateDTO;
import usach.pingeso.badema.services.PedidosAdqService;
import usach.pingeso.badema.services.postgresql.MaterialService;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/material")
public class MaterialController {
    final
    MaterialService materialService;
    private final PedidosAdqService pedidosAdqService;

    public MaterialController(MaterialService materialService, PedidosAdqService pedidosAdqService){this.materialService = materialService;
        this.pedidosAdqService = pedidosAdqService;
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<MaterialDetalleDTO> getMaterialById(@PathVariable Long id){
        MaterialDetalleDTO dto = pedidosAdqService.getMaterialByIdAdq(id);
        if(dto == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<MaterialListAdqDTO> getMaterialByNombre(@PathVariable String nombre){
        MaterialListAdqDTO material = materialService.getMaterialByNombre(nombre);
        if (material == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(material);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<MaterialUpdateDTO> updateMaterial(@RequestBody MaterialUpdateDTO material, @PathVariable Long id) {
        MaterialUpdateDTO materialActualizado = materialService.updateMaterial(material, id);
        if (materialActualizado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(materialActualizado);
    }
}
