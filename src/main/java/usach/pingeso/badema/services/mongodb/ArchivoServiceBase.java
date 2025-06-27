package usach.pingeso.badema.services.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.ArchivoBaseDocument;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.function.BiFunction;

public abstract class ArchivoServiceBase<T extends ArchivoBaseDocument> {

    private final MongoRepository<T, String> repository;

    protected ArchivoServiceBase(MongoRepository<T, String> repository) {
        this.repository = repository;
    }

    public T guardarArchivo(MultipartFile file, String uploadDir, BiFunction<String, String, T> creator) throws IOException {
        String dir = "uploads/" + uploadDir;
        File directory = new File(dir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("No se pudo crear el directorio: " + dir);
            }
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String uniqueFilename = UUID.randomUUID() + extension;
        String path = dir + "/" + uniqueFilename;
        file.transferTo(new File(path));

        T archive = creator.apply(path, originalFilename);
        archive.setRutaArchivo(path);
        archive.setFechaSubidaArchivo(LocalDate.now());
        archive.setNombreArchivo(originalFilename);

        return repository.save(archive);
    }

}

