package usach.pingeso.badema.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.PedidoDocument;
import usach.pingeso.badema.dtos.pedido.PedidoCreateDTO;
import usach.pingeso.badema.dtos.adquisiciones.PedidoListAdqDTO;
import usach.pingeso.badema.dtos.pedido.PedidoDetallePopDTO;
import usach.pingeso.badema.services.CreatePedidoService;
import usach.pingeso.badema.services.PedidosAdqService;
import usach.pingeso.badema.services.mongodb.PedidoArchivoService;
import usach.pingeso.badema.services.postgresql.PedidoService;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/pedido")
public class PedidoController {
    final PedidoService pedidoService;
    private final PedidosAdqService pedidosAdqService;
    PedidoArchivoService pedidoArchivoService;
    CreatePedidoService createPedidoService;

    public PedidoController(PedidoService pedidoService, PedidoArchivoService pedidoArchivoService, CreatePedidoService createPedidoService, PedidosAdqService pedidosAdqService){
        this.pedidoService = pedidoService;
        this.pedidoArchivoService = pedidoArchivoService;
        this.createPedidoService = createPedidoService;
        this.pedidosAdqService = pedidosAdqService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<PedidoCreateDTO> savePedido(@RequestBody PedidoCreateDTO pedido){
        PedidoCreateDTO newPedido = createPedidoService.savePedidoWithEverything(pedido);
        return ResponseEntity.ok(newPedido);
    }
    @GetMapping("/pedidos/adquisiciones/{idObra}")
    public ResponseEntity<List<PedidoListAdqDTO>> getPedidosAdqByObraId(@PathVariable Long idObra){
        List<PedidoListAdqDTO> pedidos = pedidosAdqService.getListPedidosAdqByObraId(idObra);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/detallePop/{id}")
    public ResponseEntity<PedidoDetallePopDTO> getDetallePedidoPop(@PathVariable Long id) {
        PedidoDetallePopDTO detalle = pedidoService.getDetallePedidoPop(id);
        return ResponseEntity.ok(detalle);
    }

//
//    @GetMapping("/id/{id}")
//    public ResponseEntity<Optional<PedidoEntity>> getPedidoById(@PathVariable Long id){
//        Optional<PedidoEntity> pedido = pedidoService.getPedidoById(id);
//        return ResponseEntity.ok(pedido);
//    }
//
//    @GetMapping("/estado/{estado}")
//    public ResponseEntity<List<PedidosAAtrabajarListDTO>> getPedidoByEstado(@PathVariable String estado){
//        List<PedidosAAtrabajarListDTO> pedidos = pedidoService.getPedidoByEstado(estado);
//        return ResponseEntity.ok(pedidos);
//    }
//
//
//    @PutMapping("/actualizar/{id}")
//    public ResponseEntity<PedidoEntity> updatePedido(@RequestBody PedidoUpdateDTO pedido, @PathVariable Long id){
//        return pedidoService.updatePedido(pedido, id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(null));
//    }

    // Archivos

    @PostMapping("/subir/{idPedido}")
    public ResponseEntity<PedidoDocument> subirArchivo(@RequestParam("file") MultipartFile file,
                                                       @PathVariable Long idPedido) throws IOException {
        PedidoDocument pedidoArchivo = pedidoArchivoService.guardarArchivo(file, idPedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoArchivo);
    }

    @GetMapping("/archivos/{id}")
    public ResponseEntity<List<PedidoDocument>> obtenerArchivos(@PathVariable Long id){
        List<PedidoDocument> archivos = pedidoArchivoService.getArchivosByIdPedido(id);
        if (archivos == null) return  ResponseEntity.notFound().build();
        return ResponseEntity.ok(archivos);
    }

    @GetMapping("/archivos/pdf/{id}")
    public ResponseEntity<Resource> descargarPdf(@PathVariable String id) {
        try {
            PedidoDocument archivo = pedidoArchivoService.obtenerArchivoPorId(id);
            Resource resource = pedidoArchivoService.obtenerArchivoComoResource(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + archivo.getNombreArchivo() + "\"")
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (RuntimeException | IOException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
