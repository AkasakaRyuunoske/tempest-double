package tempest_double.entity.Scenario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScenarioServiceImplementation implements ScenarioService{
    @Autowired
    private ScenarioRepository scenarioRepository;

    public List<Scenario> getScenarios() {
        return scenarioRepository.findAll();
    }
    public Scenario getScenario(int id) {
        return scenarioRepository.findById(id).orElse(null);
    }
}
