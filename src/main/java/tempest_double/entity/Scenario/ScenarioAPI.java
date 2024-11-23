package tempest_double.entity.Scenario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tempest_double.entity.Asset.Asset;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/")
@RestController
public class ScenarioAPI {
    @Autowired
    ScenarioService scenarioService;

    @GetMapping("/scenarios")
    List<Scenario> getScenarios(){
        return scenarioService.getScenarios();
    }

    @GetMapping("/scenario/{id}")
    Scenario getScenario(@PathVariable int id){
        return scenarioService.getScenario(id);
    }

    @PostMapping("/scenario")
    ResponseEntity<Map<String, String>> postScenario(@RequestBody Scenario scenario){
        return scenarioService.postScenario(scenario);
    }

    @DeleteMapping("/scenario/{id}")
    ResponseEntity<Map<String, String>> deleteScenario(@PathVariable int id){
        return scenarioService.deleteScenario(id);
    }

    @PutMapping("/scenario/{id}")
    ResponseEntity<Map<String, String>> postScenario(@RequestBody Scenario scenario, @PathVariable int id){
        return scenarioService.updateScenario(id, scenario);
    }
}
