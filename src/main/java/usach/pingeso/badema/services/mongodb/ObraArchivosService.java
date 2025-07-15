package usach.pingeso.badema.services.mongodb;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.ObraDocument;
import usach.pingeso.badema.repositories.mongodb.ObraArchivosRepository;

import java.io.IOException;
import java.util.List;

@Service
public class ObraArchivosService extends ArchivoServiceBase<ObraDocument> {
    private final ObraArchivosRepository obraArchivosRepository;


    public ObraArchivosService(ObraArchivosRepository obraArchivosRepository) {
        super(obraArchivosRepository);
        this.obraArchivosRepository = obraArchivosRepository;
    }


    public List<ObraDocument> getArchivosByIdObra(Long idObra){
        return obraArchivosRepository.findByIdObra(idObra);
    }

    public ObraDocument guardarArchivo(MultipartFile file, Long idObra) throws IOException {
        return super.guardarArchivo(file, "obra_" + idObra, (path, name) -> {
            ObraDocument doc = new ObraDocument();
            doc.setIdObra(idObra);
            return doc;
        });
    }
}

