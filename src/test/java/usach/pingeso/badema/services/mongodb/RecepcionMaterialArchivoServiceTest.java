package usach.pingeso.badema.services.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.RecepcionMaterialDocument;
import usach.pingeso.badema.repositories.mongodb.RecepcionMaterialArchivoRepository;
import usach.pingeso.badema.services.PathResolverService;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecepcionMaterialArchivoServiceTest {

    @Mock private RecepcionMaterialArchivoRepository recepcionMaterialArchivoRepository;
    @Mock private PathResolverService pathResolverService;
    @Mock private MultipartFile multipartFile;
    @InjectMocks private RecepcionMaterialArchivoService recepcionMaterialArchivoService;
    @Captor private ArgumentCaptor<RecepcionMaterialDocument> captor;

    @Test
    void findByRecepcionId() {
        when(recepcionMaterialArchivoRepository.findByIdRecepcionMaterial(8L))
                .thenReturn(List.of(new RecepcionMaterialDocument()));

        var resultado = recepcionMaterialArchivoService.findByRecepcionId(8L);

        assertEquals(1, resultado.size());
        verify(recepcionMaterialArchivoRepository).findByIdRecepcionMaterial(8L);
    }

    @Test
    void guardarArchivo_conPathYIdCorrecto() throws IOException {
        long idRecepcion = 15L;
        when(pathResolverService.resolvePathForRecepcion(idRecepcion))
                .thenReturn("obra_1/recepcion_material_15");
        doNothing().when(multipartFile).transferTo(any(File.class));

        recepcionMaterialArchivoService.guardarArchivo(multipartFile, idRecepcion);

        verify(pathResolverService).resolvePathForRecepcion(idRecepcion);
        verify(recepcionMaterialArchivoRepository).save(captor.capture());

        RecepcionMaterialDocument capturado = captor.getValue();
        assertEquals(idRecepcion, capturado.getIdRecepcionMaterial());
    }


}
