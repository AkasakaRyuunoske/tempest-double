package tempest_double.entity.Simulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimulationServiceImplementation implements SimulationService {

    @Autowired
    private SimulationRepository simulationRepository;

    @Override
    public List<Simulation> getSimulations() {
        return simulationRepository.findAll();
    }

    @Override
    public Simulation getSimulation(int id) {
        return simulationRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<Map<String, String>> postSimulation(Simulation simulation) {
        simulationRepository.save(simulation);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Simulation created successfully");
        response.put("status", "201");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteSimulation(int id) {
        if (!simulationRepository.existsById(id)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Simulation not found");
            response.put("status", "404");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        simulationRepository.deleteById(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Simulation deleted successfully");
        response.put("status", "200");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, String>> updateSimulation(int id, Simulation simulation) {
        if (!simulationRepository.existsById(id)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Simulation with the provided ID not found");
            response.put("status", "404");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Set the ID of the simulation, to match the one being updated
        simulation.setSimulationId(id);
        simulationRepository.save(simulation);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Simulation updated successfully");
        response.put("status", "200");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
