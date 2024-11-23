package tempest_double.entity.Scenario;

import java.util.List;

public interface ScenarioService {

    List<Scenario> getScenarios();

    Scenario getScenario(int id);
}
