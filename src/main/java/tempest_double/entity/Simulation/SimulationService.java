package tempest_double.entity.Simulation;

import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

public interface SimulationService {
    // get all simulations
    List<Simulation> getSimulations();

    // get a specific simulation by ID
    Simulation getSimulation(int id);

    // create a new simulation
    ResponseEntity<Map<String, String>> postSimulation(Simulation simulation);

    // delete a simulation
    ResponseEntity<Map<String, String>> deleteSimulation(int id);

    // update a simulation
    ResponseEntity<Map<String, String>> updateSimulation(int id, Simulation simulation);
}
