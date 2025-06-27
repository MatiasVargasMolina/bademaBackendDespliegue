package usach.pingeso.badema.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorCreateDTO;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorListDTO;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorUpdateDTO;
import usach.pingeso.badema.services.postgresql.ProveedorService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/proveedor")
public class ProveedorController {
    final
    ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService){this.proveedorService = proveedorService;}

    @GetMapping("/proveedores")
    public ResponseEntity<List<ProveedorListDTO>> getAllProveedores(){
        List<ProveedorListDTO> proveedores = proveedorService.getAllProveedores();
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProveedorListDTO> getProveedorById(@PathVariable Long id){
        ProveedorListDTO proveedor = proveedorService.getProveedorById(id);
        if (proveedor == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(proveedor);
    }

    @PostMapping("/guardar")
    public ResponseEntity<ProveedorCreateDTO> saveProveedor(@RequestBody ProveedorCreateDTO proveedor){
        ProveedorCreateDTO newProveedor = proveedorService.saveProveedor(proveedor);
        return ResponseEntity.ok(newProveedor);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProveedorUpdateDTO> updateProveedor(@RequestBody ProveedorUpdateDTO proveedor, @PathVariable Long id) {
        ProveedorUpdateDTO proveedorActualizado = proveedorService.updateProveedor(proveedor, id);
        if (proveedorActualizado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(proveedorActualizado);
    }
}
