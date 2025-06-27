package usach.pingeso.badema.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usach.pingeso.badema.services.mongodb.HitosObraService;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/badema/api/hito")
public class HitoController {
    private final HitosObraService hitosObraService;

    public HitoController(HitosObraService hitosObraService) {
        this.hitosObraService = hitosObraService;
    }

    @PutMapping("/agregar/{idHito}")
    public ResponseEntity<?> agregarOActualizarHito(
            @PathVariable String idHito,
            @RequestParam String nombreHito,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        try {
            hitosObraService.agregarOActualizarHito(idHito, nombreHito, fecha);
            return ResponseEntity.ok("Hito agregado o actualizado correctamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{idHito}")
    public ResponseEntity<?> eliminarHito(
            @PathVariable String idHito,
            @RequestParam String nombreHito
    ) {
        try {
            hitosObraService.eliminarHito(idHito, nombreHito);
            return ResponseEntity.ok("Hito eliminado correctamente.");
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
