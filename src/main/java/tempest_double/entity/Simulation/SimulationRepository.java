package tempest_double.entity.Simulation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, Integer> {
    Simulation findById(int id);

    Simulation findTopByOrderByIdDesc();

    List<Simulation> findAllByScenarioId(int scenario_id);
}
