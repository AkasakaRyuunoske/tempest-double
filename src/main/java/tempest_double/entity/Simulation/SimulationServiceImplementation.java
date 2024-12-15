package tempest_double.entity.Simulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tempest_double.entity.Scenario.Scenario;
import tempest_double.entity.Scenario.ScenarioRepository;

import java.util.List;

@Service
public class SimulationServiceImplementation implements SimulationService{
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
    public ResponseEntity<String> postSimulation(int scenario_id) {
        Scenario scenario = scenarioRepository.findById(scenario_id).get();
        Simulation simulation = new Simulation();
        simulation.setScenario(scenario);
        simulationRepository.save(simulation);

        return ResponseEntity.ok("Saved without errors");
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
