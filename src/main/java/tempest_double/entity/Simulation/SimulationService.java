package tempest_double.entity.Simulation;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface SimulationService {
    Simulation getSimulationById(int id);

    List<Simulation> getSimulations();

    ResponseEntity<Map<String, Object>> postSimulation(String scenario_json);

    ResponseEntity<Map<String, Object>> simulate(String scenario_name);

    ResponseEntity<String> deleteSimulations(int... simulationIds);

    ResponseEntity<String> deleteSimulation(int scenarioId);
}
