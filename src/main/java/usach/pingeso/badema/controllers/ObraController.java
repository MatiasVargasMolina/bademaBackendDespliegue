package usach.pingeso.badema.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.HitosObraDocument;
import usach.pingeso.badema.documents.ObraDocument;
import usach.pingeso.badema.dtos.obra.ObraCreateDTO;
import usach.pingeso.badema.dtos.obra.ObraDetalleDTO;
import usach.pingeso.badema.dtos.obra.ObraListDTO;
import usach.pingeso.badema.dtos.obra.ObraUpdateDTO;
import usach.pingeso.badema.services.ObraDetalleService;
import usach.pingeso.badema.services.mongodb.HitosObraService;
import usach.pingeso.badema.services.mongodb.ObraArchivosService;
import usach.pingeso.badema.services.postgresql.ObraService;
import usach.pingeso.badema.services.postgresql.PedidoService;
import usach.pingeso.badema.services.postgresql.UsuarioObraService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/obra")
public class ObraController {
    final
    ObraService obraService;
    private final ObraDetalleService obraDetalleService;
    private final UsuarioObraService usuarioObraService;
    ObraArchivosService obraArchivosService;
    HitosObraService hitosObraService;
    PedidoService pedidoService;

    public ObraController(ObraService obraService, ObraArchivosService obraArchivosService, HitosObraService hitosObraService, PedidoService pedidoService, ObraDetalleService obraDetalleService, UsuarioObraService usuarioObraService) {
        this.obraService = obraService;
        this.obraArchivosService = obraArchivosService;
        this.hitosObraService = hitosObraService;
        this.pedidoService = pedidoService;
        this.obraDetalleService = obraDetalleService;
        this.usuarioObraService = usuarioObraService;
    }

    // Lista de obras y sus filtros

    @GetMapping("/obras/{idUsuario}")
    public ResponseEntity<List<ObraListDTO>> getObrasByIdUsuario(@PathVariable Long idUsuario){
        List<ObraListDTO> obras = obraService.getObrasByIdUsuario(idUsuario);
        return ResponseEntity.ok(obras);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ObraListDTO> getObraByNombre(@PathVariable String nombre){
        ObraListDTO obraMini = obraService.getObraByNombre(nombre);
        if (obraMini == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(obraMini);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ObraListDTO>> getObraByEstado(@PathVariable String estado){
        List<ObraListDTO> obras = obraService.getObraByEstado(estado);
        return ResponseEntity.ok(obras);
    }

    @GetMapping("/fechaInicio/{fechaInicio}")
    public ResponseEntity<List<ObraListDTO>> getObraByFechaInicio(@PathVariable LocalDate fechaInicio){
        List<ObraListDTO> obras = obraService.getObraByFechaInicio(fechaInicio);
        return ResponseEntity.ok(obras);
    }

    @GetMapping("/fechaTermino/{fechaTermino}")
    public ResponseEntity<List<ObraListDTO>> getObraByFechaTermino(@PathVariable LocalDate fechaTermino){
        List<ObraListDTO> obras = obraService.getObraByFechaTermino(fechaTermino);
        return ResponseEntity.ok(obras);
    }

    @GetMapping("/empresa/{empresa}")
    public ResponseEntity<List<ObraListDTO>> getObraByEmpresaContratista(@PathVariable String empresa){
        List<ObraListDTO> obraMinis = obraService.getObraByEmpresaContratista(empresa);
        return ResponseEntity.ok(obraMinis);
    }

    // Detalle de una obra: datos de una obra, asociados, adminisatrativos, subcontratos, hitos, pedidos

    @GetMapping("/id/{id}")
    public ResponseEntity<ObraDetalleDTO> getObraById(@PathVariable Long id){
        return ResponseEntity.ok(obraDetalleService.obtenerDetalle(id));
    }

    // Crear obra

    @PostMapping("/guardar")
    public ResponseEntity<HitosObraDocument> saveObra(@RequestBody ObraCreateDTO obra){
        Long idObra = obraService.saveObra(obra);
        usuarioObraService.saveUsuarioObra(idObra, obra.getIdUsuario(), "Gerencia");
        HitosObraDocument hitosObra = hitosObraService.insertarHitos(idObra, obra);
        return ResponseEntity.ok(hitosObra);
    }

    // Updatear obra

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ObraUpdateDTO> updateObra(@RequestBody ObraUpdateDTO obra, @PathVariable Long id) {
        ObraUpdateDTO obraActualizada = obraService.updateObra(obra, id);
        if (obraActualizada == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(obraActualizada);
    }

    // Archivos obra

    @PostMapping("/subir/{idObra}")
    public ResponseEntity<ObraDocument> subirArchivo(@RequestParam("file") MultipartFile file,
                                                     @PathVariable Long idObra) throws IOException {
        ObraDocument obraArchivo = obraArchivosService.guardarArchivo(file, idObra);
        return ResponseEntity.status(HttpStatus.CREATED).body(obraArchivo);
    }

    @GetMapping("/archivos/{id}")
    public ResponseEntity<List<ObraDocument>> obtenerArchivos(@PathVariable Long id) {
        List<ObraDocument> archivos = obraArchivosService.getArchivosByIdObra(id);
        return ResponseEntity.ok(archivos);
    }

    @GetMapping("/archivos/pdf/{id}")
    public ResponseEntity<Resource> descargarPdf(@PathVariable String id) {
        try {
            ObraDocument archivo = obraArchivosService.obtenerArchivoPorId(id);
            Resource resource = obraArchivosService.obtenerArchivoComoResource(id);

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
