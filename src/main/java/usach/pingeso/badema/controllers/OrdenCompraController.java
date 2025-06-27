package usach.pingeso.badema.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usach.pingeso.badema.dtos.OrdenCompraCreateDTO;
import usach.pingeso.badema.entities.OrdenCompraEntity;
import usach.pingeso.badema.services.postgresql.OrdenCompraService;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/ordencompra")
public class OrdenCompraController {
    final OrdenCompraService ordenCompraService;

    public OrdenCompraController(OrdenCompraService ordenCompraService){
        this.ordenCompraService = ordenCompraService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<OrdenCompraEntity> saveOrden(@RequestBody OrdenCompraCreateDTO orden){
        OrdenCompraEntity newOrden = ordenCompraService.saveOrden(orden);
        return ResponseEntity.ok(newOrden);
    }


}
