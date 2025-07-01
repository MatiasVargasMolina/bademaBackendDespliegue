package usach.pingeso.badema.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usach.pingeso.badema.documents.ValorEspecificacion;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorMaterialDTO;
import usach.pingeso.badema.entities.ProveedorMaterialEntity;
import usach.pingeso.badema.services.mongodb.NuevasEspecificacionesService;
import usach.pingeso.badema.services.postgresql.ProveedorMaterialService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/proveedormaterial")
public class ProveedorMaterialController {
    final ProveedorMaterialService proveedorMaterialService;
    private final NuevasEspecificacionesService nuevasEspecificacionesService;

    public ProveedorMaterialController(ProveedorMaterialService proveedorMaterialService, NuevasEspecificacionesService nuevasEspecificacionesService) {
        this.proveedorMaterialService = proveedorMaterialService;
        this.nuevasEspecificacionesService = nuevasEspecificacionesService;
    }

    @GetMapping("/unionesPM")
    public ResponseEntity<List<ProveedorMaterialDTO>> getAllProveedorMaterial(){
        List<ProveedorMaterialDTO> unionesPM = proveedorMaterialService.getAllProveedorMaterial();
        return ResponseEntity.ok(unionesPM);
    }

    @GetMapping("/materialproveedor/{id}")
    public ResponseEntity<List<ProveedorMaterialDTO>> getProveedorMaterialByMaterialId(@PathVariable Long id){
        List<ProveedorMaterialDTO> unionesPM = proveedorMaterialService.getProveedorMaterialByMaterialId(id);
        return ResponseEntity.ok(unionesPM);
    }

    @PostMapping("/guardar/{idProveedor}/{idMaterial}")
    public ResponseEntity<Void> saveProveedorMaterial(@RequestBody ProveedorMaterialDTO proveedorMaterial,
                                                                      @RequestBody Map<String, ValorEspecificacion> nuevasEspecificacionesDocument,
                                                                      @PathVariable Long idProveedor,
                                                                      @PathVariable Long idMaterial){
        ProveedorMaterialEntity newProveedorMaterial = proveedorMaterialService.saveProveedorMaterial(idProveedor, idMaterial, proveedorMaterial);
        if(nuevasEspecificacionesService.insertarNuevasEspecificaciones(nuevasEspecificacionesDocument, newProveedorMaterial)){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/actualizar/{idProveedor}/{idMaterial}")
    public ResponseEntity<ProveedorMaterialDTO> updateProveedorMaterial(@RequestBody ProveedorMaterialDTO proveedorMaterial,
                                                                        @PathVariable Long idProveedor,
                                                                        @PathVariable Long idMaterial) {
        ProveedorMaterialDTO unionPMActualizada = proveedorMaterialService.updateProveedorMaterial(idProveedor, idMaterial, proveedorMaterial);
        if (unionPMActualizada == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(unionPMActualizada);
    }

    @DeleteMapping("/eliminar/{idProveedor}/{idMaterial}")
    public ResponseEntity<Void> deleteProveedorMaterial(@PathVariable Long idProveedor, @PathVariable Long idMaterial) {
        proveedorMaterialService.deleteProveedorMaterial(idProveedor, idMaterial);
        return ResponseEntity.noContent().build();
    }
}