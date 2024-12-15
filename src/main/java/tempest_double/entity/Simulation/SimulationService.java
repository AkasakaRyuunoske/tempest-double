package tempest_double.entity.Simulation;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SimulationService {
    Simulation getSimulationById(int id);

    List<Simulation> getSimulations();

    ResponseEntity<String> postSimulation(int scenario_id);
}
