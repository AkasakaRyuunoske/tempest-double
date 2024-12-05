package tempest_double.entity.Scenario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Integer> {

    Scenario findByName(String name);

    int deleteByName(String name);
}
