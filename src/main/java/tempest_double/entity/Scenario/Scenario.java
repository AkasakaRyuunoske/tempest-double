package tempest_double.entity.Scenario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tempest_double.entity.JsonConverter;

import java.util.Map;

@Entity
@Table(name = "scenarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Scenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @JsonIgnore
    private int id;

    @Column(columnDefinition = "JSON", nullable = false)
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> environmentConfiguration;

    @Column(columnDefinition = "JSON", nullable = false)
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> assets;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;
}
