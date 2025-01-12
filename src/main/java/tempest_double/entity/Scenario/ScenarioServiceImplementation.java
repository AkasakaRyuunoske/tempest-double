package tempest_double.entity.Scenario;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class ScenarioServiceImplementation implements ScenarioService{
    @Autowired
    private ScenarioRepository scenarioRepository;

    @Override
    public List<Scenario> getScenarios() {
        return scenarioRepository.findAll();
    }
//    @Override
//    public ResponseEntity<Object> getScenario(int id) {
//        Scenario scenario = scenarioRepository.findById(id).orElse(null);
//        if (scenario == null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Scenario not found");
//        }
//        return ResponseEntity.ok(scenario);
//    }

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
        Map<String, String> response = new HashMap<>();
        if (scenario.getId() == 0){
            response.put("Error", "scenario appears to be empty.");
            response.put(status, "400");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        scenarioRepository.save(scenario);

        response.put(message, "Success");
        response.put(status, "200");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteScenario(int id) {
        scenarioRepository.deleteById(id);

        Map<String, String> response = new HashMap<>();
        response.put(message, "Success");
        response.put(status, "200");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, String>> updateScenario(int id, @RequestBody Scenario scenario) {
        Map<String, String> response = new HashMap<>();
        if (!scenarioRepository.existsById(id)){
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
        int deletedCount = scenarioRepository.deleteByName(name);
        Map<String, String> response = new HashMap<>();

        if (deletedCount == 0) {
            response.put(message, "Scenario not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put(message, "Scenario deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
