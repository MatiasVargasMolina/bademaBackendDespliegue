package usach.pingeso.badema.services.mongodb;

import org.springframework.stereotype.Service;
import usach.pingeso.badema.documents.HitosObraDocument;
import usach.pingeso.badema.dtos.obra.ObraCreateDTO;
import usach.pingeso.badema.repositories.mongodb.HitosObraRepository;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
public class HitosObraService {
    private final HitosObraRepository hitosObraRepository;
    public HitosObraService(HitosObraRepository hitosObraRepository) {
        this.hitosObraRepository = hitosObraRepository;
    }

    public HitosObraDocument insertarHitos(long idObra, ObraCreateDTO obra) {
        HitosObraDocument hitos = new HitosObraDocument();
        hitos.setHitos(obra.getHitos().getHitos());
        hitos.setIdObra(idObra);
        return hitosObraRepository.save(hitos);
    }

    public HitosObraDocument findHitosByObraId(Long idObra){
        return hitosObraRepository.findByIdObra(idObra);
    }

    public void agregarOActualizarHito(String idHito, String hito, LocalDate fecha) {
        HitosObraDocument doc = hitosObraRepository.findById(idHito)
                .orElseThrow(() -> new NoSuchElementException("Hito no encontrado con ID: " + idHito));

        doc.getHitos().put(hito, fecha);
        hitosObraRepository.save(doc);
    }

    public void eliminarHito(String idHito, String hito) {
        HitosObraDocument doc = hitosObraRepository.findById(idHito)
                .orElseThrow(() -> new NoSuchElementException("Hito no encontrado con ID: " + idHito));

        if (doc.getHitos().containsKey(hito)) {
            doc.getHitos().remove(hito);
            hitosObraRepository.save(doc);
        } else {
            throw new IllegalArgumentException("No existe el hito: " + hito);
        }
    }
}
