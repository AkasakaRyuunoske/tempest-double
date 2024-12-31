package tempest_double.entity.Simulation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tempest_double.entity.Scenario.Scenario;
import tempest_double.entity.Scenario.ScenarioRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimulationServiceImplementation implements SimulationService {
    @Autowired
    SimulationRepository simulationRepository;
    @Autowired
    ScenarioRepository scenarioRepository;


    @Override
    public Simulation getSimulationById(int id) {
        return simulationRepository.findById(id);
    }

    @Override
    public List<Simulation> getSimulations() {
        return simulationRepository.findAll();
    }

    @Override
    public ResponseEntity<Map<String, Object>> postSimulation(String scenario_name) {
        Map<String, Object> response = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse the JSON into a JsonNode
            JsonNode jsonNode = objectMapper.readTree(scenario_name);
            scenario_name = jsonNode.get("name").asText();
        } catch (Exception e) {
            response.put("error", "Error parsing JSON");
            return ResponseEntity.badRequest().body(response);
        }
        System.out.println("Name is: " + scenario_name);
        Scenario scenario = scenarioRepository.findByName(scenario_name);
        System.out.println("scenario is: " + scenario);

        Simulation simulation = new Simulation();
        simulation.setScenario(scenario);
        simulationRepository.save(simulation);

        response.put("data", "some data");
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<String> deleteSimulations(int[] simulation_ids) {
        for (int simulationId : simulation_ids) {
            simulationRepository.deleteById(simulationId);
        }

        return ResponseEntity.ok("Deleted all without errors");
    }

    @Override
    public ResponseEntity<String> deleteSimulation(int simulation_id) {
        simulationRepository.deleteById(simulation_id);

        return ResponseEntity.ok("Deleted without errors");
    }
}
