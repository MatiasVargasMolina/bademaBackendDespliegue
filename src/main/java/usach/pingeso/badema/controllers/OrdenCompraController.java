package usach.pingeso.badema.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.OrdenCompraDocument;
import usach.pingeso.badema.dtos.ordencompra.CrearOrdenCompraDTO;
import usach.pingeso.badema.dtos.ordencompra.DocumentoOrdenCompraDTO;
import usach.pingeso.badema.dtos.ordencompra.OrdenCompraDTO;
import usach.pingeso.badema.services.mongodb.OrdenCompraArchivoService;
import usach.pingeso.badema.services.postgresql.OrdenCompraService;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/ordencompra")
public class OrdenCompraController {
    final OrdenCompraService ordenCompraService;
    final OrdenCompraArchivoService ordenCompraArchivoService;

    public OrdenCompraController(OrdenCompraService ordenCompraService, OrdenCompraArchivoService ordenCompraArchivoService){
        this.ordenCompraService = ordenCompraService;
        this.ordenCompraArchivoService = ordenCompraArchivoService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<OrdenCompraDTO> saveOrden(@RequestBody CrearOrdenCompraDTO orden){
        OrdenCompraDTO ordenGuardada = ordenCompraService.saveOrden(orden);
        return ResponseEntity.ok(ordenGuardada);
    }

    @GetMapping("/documento/{id}")
    public ResponseEntity<DocumentoOrdenCompraDTO> getDocumentoOrden(@PathVariable Long id) {
        return ResponseEntity.ok(ordenCompraService.generarDocumentoOrdenCompra(id));
    }

    // Archivos
    @PostMapping("/subir/{idOrdenCompra}")
    public ResponseEntity<OrdenCompraDocument> subirArchivo(@RequestParam("file") MultipartFile file,
                                                       @PathVariable Long idOrdenCompra) throws IOException {
        OrdenCompraDocument ordenCompraArchivo = ordenCompraArchivoService.guardarArchivo(file, idOrdenCompra);
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenCompraArchivo);
    }

    @GetMapping("/archivos/{id}")
    public ResponseEntity<List<OrdenCompraDocument>> obtenerArchivos(@PathVariable Long id){
        List<OrdenCompraDocument> archivos = ordenCompraArchivoService.getArchivosByIdOrdenCompra(id);
        if (archivos == null) return  ResponseEntity.notFound().build();
        return ResponseEntity.ok(archivos);
    }

    @GetMapping("/archivos/pdf/{id}")
    public ResponseEntity<Resource> descargarPdf(@PathVariable String id) {
        try {
            OrdenCompraDocument archivo = ordenCompraArchivoService.obtenerArchivoPorId(id);
            Resource resource = ordenCompraArchivoService.obtenerArchivoComoResource(id);

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
