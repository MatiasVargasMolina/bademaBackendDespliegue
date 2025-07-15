package usach.pingeso.badema.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usach.pingeso.badema.dtos.adquisiciones.*;
import usach.pingeso.badema.services.postgresql.ManejarAdquisicionesService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/manejarAdquisiciones")
public class ManejarAdquisicionesController {

    private final ManejarAdquisicionesService adquisicionesService;

    public ManejarAdquisicionesController(ManejarAdquisicionesService adquisicionesService){
        this.adquisicionesService = adquisicionesService;
    }

    @GetMapping("/pedidos/{idObra}")
    public ResponseEntity<List<PedidoListAdqDTO>> obtenerPedidosPorObra(@PathVariable Long idObra) {
        List<PedidoListAdqDTO> pedidos = adquisicionesService.obtenerPedidosPorObra(idObra);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/materiales/{idPedido}/{idMaterial}")
    public ResponseEntity<MaterialConProveedoresDTO> manejarAdquisicionPorMaterial(
            @PathVariable Long idPedido,
            @PathVariable Long idMaterial) {

        MaterialConProveedoresDTO resultado = adquisicionesService.obtenerMaterialDePedido(idPedido, idMaterial);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/materialesPorProveedor/{idObra}/{idProveedor}")
    public ResponseEntity<List<MaterialProveedorPedidoDTO>> getMaterialesProveedorPorObra(
            @PathVariable Long idProveedor,
            @PathVariable Long idObra) {
        return ResponseEntity.ok(adquisicionesService.getMaterialesProveedorPorObra(idObra, idProveedor));
    }

    @GetMapping("/detalleProveedorMaterial/{idProveedor}/{idMaterial}")
    public ResponseEntity<ProveedorMaterialDetalleDTO> getDetalleProveedorMaterial(
            @PathVariable Long idProveedor,
            @PathVariable Long idMaterial) {
        return ResponseEntity.ok(adquisicionesService.getDetalleProveedorMaterial(idProveedor, idMaterial));
    }
}
