package usach.pingeso.badema.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "session")
public class SessionEntity {
    @Id
    private Long userId;

    @Id
    private Long obraId;
}
