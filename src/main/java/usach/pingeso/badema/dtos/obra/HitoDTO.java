package usach.pingeso.badema.dtos.obra;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class HitoDTO {
    private Map<String, LocalDate> hitos = new HashMap<>();
}
