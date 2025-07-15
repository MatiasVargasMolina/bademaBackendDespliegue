package usach.pingeso.badema.services.malimplementado;

import usach.pingeso.badema.documents.malhecho.GuiaDespachoDocument;
import usach.pingeso.badema.repositories.malimplementado.GuiaDespachoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class GuiaDespachoService {

    private GuiaDespachoRepository guiaRepository;

    public GuiaDespachoDocument guardarArchivo(MultipartFile file, Long idDetalleOrdenCompra) throws IOException {
    GuiaDespachoDocument doc = new GuiaDespachoDocument();
    doc.setNombreArchivo(file.getOriginalFilename());
    doc.setTipoArchivo(file.getContentType());
    doc.setContenido(file.getBytes());
    doc.setFechaSubida(LocalDate.now());
    doc.setIdDetalleOrdenCompra(idDetalleOrdenCompra);
    return guiaRepository.save(doc);
}

    public GuiaDespachoDocument obtenerArchivoPorId(String id) {
        return guiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));
    }

    public Resource obtenerArchivoComoResource(String id) {
        GuiaDespachoDocument doc = obtenerArchivoPorId(id);
        return new ByteArrayResource(doc.getContenido());
    }
    public List<GuiaDespachoDocument> getArchivosByIdDetalleOrdenCompra(Long idDetalleOrden) {
    return guiaRepository.findByIdDetalleOrdenCompra(idDetalleOrden);
}
}
