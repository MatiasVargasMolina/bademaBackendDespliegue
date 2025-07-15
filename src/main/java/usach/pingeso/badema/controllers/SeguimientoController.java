package usach.pingeso.badema.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.dtos.seguimiento.ActualizarSeguimientoDetalleDTO;
import usach.pingeso.badema.dtos.seguimiento.OrdenCompraSeguimientoDTO;
import usach.pingeso.badema.dtos.seguimiento.SeguimientoDetalleOrdenDTO;
import usach.pingeso.badema.documents.malhecho.GuiaDespachoDocument;
import usach.pingeso.badema.services.postgresql.SeguimientoService;
import usach.pingeso.badema.services.malimplementado.GuiaDespachoService;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/seguimiento")
public class SeguimientoController {

    private final SeguimientoService seguimientoService;

    @Autowired
    private GuiaDespachoService guiaDespachoService;

    public SeguimientoController(SeguimientoService seguimientoService) {
        this.seguimientoService = seguimientoService;
    }

    // Obtener la lista de órdenes de compra para el seguimiento (lado izquierdo)
    @GetMapping("/ordenes/{idObra}")
    public ResponseEntity<List<OrdenCompraSeguimientoDTO>> getOrdenesSeguimiento(@PathVariable Long idObra) {
        return ResponseEntity.ok(seguimientoService.getOrdenesSeguimiento(idObra));
    }

    // Obtener el detalle de seguimiento de un ítem (lado derecho)
    @GetMapping("/detalle/{idOrden}/{idMaterial}")
    public ResponseEntity<SeguimientoDetalleOrdenDTO> getDetalleOrdenSeguimiento(
            @PathVariable Long idOrden,
            @PathVariable Long idMaterial) {
        return ResponseEntity.ok(seguimientoService.getDetalleOrdenSeguimiento(idOrden, idMaterial));
    }

    // Actualizar una nueva recepción (entregada e instalada)
    @PutMapping("/detalle/actualizar/{idOrden}/{idMaterial}")
    public ResponseEntity<Void> actualizarDetalleOrdenSeguimiento(@PathVariable Long idOrden,
            @PathVariable Long idMaterial,
            @RequestBody ActualizarSeguimientoDetalleDTO datos) {
        seguimientoService.actualizarDetalleOrdenSeguimiento(idOrden, idMaterial, datos);
        return ResponseEntity.ok().build();
    }

    // -------------------------
    // GUÍAS DE DESPACHO - MONGODB
    // -------------------------

    // Subir archivo guía de despacho
    @PostMapping("/guias/subir/{idDetalleOrden}")
    public ResponseEntity<GuiaDespachoDocument> subirGuiaDespacho(@RequestParam("file") MultipartFile file,
            @PathVariable Long idDetalleOrden) throws IOException {
        GuiaDespachoDocument guia = guiaDespachoService.guardarArchivo(file, idDetalleOrden);
        return ResponseEntity.status(HttpStatus.CREATED).body(guia);
    }

    @GetMapping("/guias/archivos/{idDetalleOrden}")
    public ResponseEntity<List<GuiaDespachoDocument>> obtenerGuiasDespacho(@PathVariable Long idDetalleOrden) {
        return ResponseEntity.ok(guiaDespachoService.getArchivosByIdDetalleOrdenCompra(idDetalleOrden));
    }

    // Descargar/visualizar un archivo guía de despacho PDF
    @GetMapping("/guias/archivos/pdf/{id}")
    public ResponseEntity<Resource> descargarGuiaDespacho(@PathVariable String id) {
        try {
            GuiaDespachoDocument archivo = guiaDespachoService.obtenerArchivoPorId(id);
            Resource resource = guiaDespachoService.obtenerArchivoComoResource(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + archivo.getNombreArchivo() + "\"")
                    .contentLength(resource.contentLength())
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (RuntimeException | IOException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
