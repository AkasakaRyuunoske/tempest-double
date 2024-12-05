package tempest_double.entity.Scenario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Integer> {

    Optional<Scenario> findByName(String name);

    void deleteByName(String name);
}
