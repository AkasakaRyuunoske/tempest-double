package tempest_double.entity.Scenario;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Map;

public interface ScenarioService {

    List<Scenario> getScenarios();

    Scenario getScenario(int id);

    ResponseEntity<Object> getScenarioByNameResponse(String name);

    ResponseEntity<Map<String, String>> postScenario(Scenario scenario);

    ResponseEntity<Map<String, String>> deleteScenario(int id);

    ResponseEntity<Map<String, String>> deleteScenarioByName(String name);

    ResponseEntity<Map<String, String>> updateScenario(int id, @RequestBody Scenario scenario);
}
