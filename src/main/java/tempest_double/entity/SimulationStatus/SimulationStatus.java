package tempest_double.entity.SimulationStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tempest_double.entity.JsonConverter;
import tempest_double.entity.Simulation.Simulation;

import java.util.Map;

@Entity
@Table(name = "simulation_status")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SimulationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @Column(columnDefinition = "JSON", nullable = false)
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> assets_results;

    @Column(columnDefinition = "JSON", nullable = false)
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> environmental_changes;

    @ManyToOne
    @JoinColumn(
            name = "simulation_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_simulation_status_simulation_id",
                    foreignKeyDefinition = "FOREIGN KEY (simulation_id) REFERENCES simulations(id) ON DELETE CASCADE"
            )
    )
    private Simulation simulation;
}
