package tempest_double.entity.Scenario;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import tempest_double.entity.Simulation.Simulation;
import tempest_double.entity.Simulation.SimulationRepository;
import tempest_double.entity.SimulationStatus.SimulationStatusRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class ScenarioServiceImplementation implements ScenarioService {
    @Autowired
    private ScenarioRepository scenarioRepository;

    @Autowired
    private SimulationRepository simulationRepository;

    @Autowired
    private SimulationStatusRepository simulationStatusRepository;


    @Override
    public List<Scenario> getScenarios() {
        return scenarioRepository.findAll();
    }

    @Override
    public Scenario getScenario(int id) {
        return scenarioRepository.findById(id).orElse(null);
    }

    private String message = "message";
    private String status = "status";

    @Override
    public ResponseEntity<Object> getScenarioByName(String name) {
        Scenario scenario = scenarioRepository.findByName(name);
        if (scenario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Scenario not found");
        }
        return ResponseEntity.ok(scenario);
    }

    @Override
    public ResponseEntity<Map<String, String>> postScenario(Scenario scenario) {
        scenarioRepository.save(scenario);

        Map<String, String> response = new HashMap<>();
        response.put(message, "Success");
        response.put(status, "200");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteScenario(int id) {
        Map<String, String> response = new HashMap<>();

        if (scenarioRepository.existsById(id)) {
            scenarioRepository.deleteById(id);

            response.put(message, "Success");
            response.put(status, "200");

            return new ResponseEntity<>(response, HttpStatus.OK);

        } else {
            response.put(message, "Scenario not found");

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ResponseEntity<Map<String, String>> updateScenario(int id, @RequestBody Scenario scenario) {
        Map<String, String> response = new HashMap<>();
        if (!scenarioRepository.existsById(id)) {
            response.put(message, "Error: Scenario with provided ID not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        scenario.setId(id);
        scenarioRepository.save(scenario);

        response.put(message, "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, String>> deleteScenarioByName(String name) {
        Scenario scenario = scenarioRepository.findByName(name);
        Map<String, String> response = new HashMap<>();

        if (scenario == null) {
            response.put(message, "Scenario not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        List<Simulation> simulations = simulationRepository.findAllByScenarioId(scenario.getId());

        if (simulations == null) {
            scenarioRepository.deleteByName(name);

            response.put(message, "Scenario deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        for (Simulation simulation : simulations) {
            simulationStatusRepository.deleteAllSimulationStatusBySimulationId(simulation.getId());
        }

        for (Simulation simulation : simulations) {
            simulationRepository.deleteById(simulation.getId());
        }

        scenarioRepository.deleteByName(name);

        response.put(message, "Scenario deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
