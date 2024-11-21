package tempest_double.entity.Asset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tempest_double.entity.JsonConverter;

import java.util.Map;

@Entity
@Table(name = "assets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "JSON", nullable = false)
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> configuration;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String role;

    @Override
    public String toString(){
        return "{'id':" + id + ", 'type':'" + type + "', 'configuration':'" + configuration + "', 'name':'" + name + "', 'role': '" + role + "'}";
    }
}
