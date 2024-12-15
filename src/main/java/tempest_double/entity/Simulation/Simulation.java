package tempest_double.entity.Simulation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import tempest_double.entity.Scenario.Scenario;

import java.time.LocalDateTime;

@Entity
@Table(name = "simulations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Simulation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;
}
