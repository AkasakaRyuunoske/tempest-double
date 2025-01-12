package tempest_double.backEndAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tempest_double.entity.Scenario.Scenario;
import tempest_double.entity.Scenario.ScenarioService;
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

//    @GetMapping("/scenario/{id}")
//    ResponseEntity<Object> getScenario(@PathVariable int id){
//        return scenarioService.getScenario(id);
//    }

    @GetMapping("/scenario/name/{name}")
    public ResponseEntity<Object> getScenarioByName(@PathVariable String name) {
        return scenarioService.getScenarioByName(name);
    }

    @PostMapping("/scenario")
    ResponseEntity<Map<String, String>> postScenario(@RequestBody Scenario scenario){
        return scenarioService.postScenario(scenario);
    }

    @DeleteMapping("/scenario/{id}")
    ResponseEntity<Map<String, String>> deleteScenario(@PathVariable int id){
        return scenarioService.deleteScenario(id);
    }

    @DeleteMapping("/scenario/name/{name}")
    public ResponseEntity<Map<String, String>> deleteScenarioByName(@PathVariable String name) {
        return scenarioService.deleteScenarioByName(name);
    }

    @PutMapping("/scenario/{id}")
    ResponseEntity<Map<String, String>> postScenario(@RequestBody Scenario scenario, @PathVariable int id){
        return scenarioService.updateScenario(id, scenario);
    }
}
