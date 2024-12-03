package tempest_double.entity.Simulation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @Column(name = "simulation_id")
    private int simulationId;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;
}
