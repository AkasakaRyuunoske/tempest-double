package tempest_double.entity.Scenario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
