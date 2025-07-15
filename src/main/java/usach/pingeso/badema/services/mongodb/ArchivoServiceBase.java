package usach.pingeso.badema.services.mongodb;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.ArchivoBaseDocument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;
import java.util.function.BiFunction;

public abstract class ArchivoServiceBase<T extends ArchivoBaseDocument> {

    private final MongoRepository<T, String> repository;

    protected ArchivoServiceBase(MongoRepository<T, String> repository) {
        this.repository = repository;
    }

    public T guardarArchivo(MultipartFile file, String uploadDir, BiFunction<String, String, T> creator) throws IOException {
        Path dirPath = Paths.get("uploads", uploadDir.split("/"));
        File directory = dirPath.toFile();

        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("No se pudo crear el directorio: " + dirPath);
            }
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String uniqueFilename = UUID.randomUUID() + extension;
        Path filePath = dirPath.resolve(uniqueFilename);

        file.transferTo(filePath.toFile());

        String path = filePath.toString().replace("\\", "/");

        T archive = creator.apply(path, originalFilename);
        archive.setRutaArchivo(path);
        archive.setFechaSubidaArchivo(LocalDate.now());
        archive.setNombreArchivo(originalFilename);

        return repository.save(archive);
    }

    // Método para obtener el archivo físico como Resource
    public Resource obtenerArchivoComoResource(String id) {
        ArchivoBaseDocument archivo = obtenerArchivoPorId(id);

        File file = new File(archivo.getRutaArchivo());
        if (!file.exists()) {
            throw new RuntimeException("Archivo físico no encontrado");
        }

        return new FileSystemResource(file);
    }

    // Método para obtener metadata (lo asumo, lo puedes implementar)
    public T obtenerArchivoPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));
    }
}