package tempest_double.entity.Scenario;

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
    @Override
    public Scenario getScenario(int id) {
        return scenarioRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<Map<String, String>> postScenario(Scenario scenario) {
        scenarioRepository.save(scenario);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Success");
        response.put("status", "200");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteScenario(int id) {
        scenarioRepository.deleteById(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Success");
        response.put("status", "200");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map<String, String>> updateScenario(int id, @RequestBody Scenario scenario) {
        Map<String, String> response = new HashMap<>();
        if (!scenarioRepository.existsById(id)){
            response.put("message", "Error: Scenario with provided ID not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        scenario.setId(id);
        scenarioRepository.save(scenario);

        response.put("message", "Success");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
