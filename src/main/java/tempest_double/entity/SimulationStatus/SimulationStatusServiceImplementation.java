package tempest_double.entity.SimulationStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tempest_double.entity.Scenario.Scenario;
import tempest_double.entity.Scenario.ScenarioRepository;
import tempest_double.entity.Simulation.Simulation;
import tempest_double.entity.Simulation.SimulationRepository;

import java.util.List;

@Service
public class SimulationStatusServiceImplementation implements SimulationStatusService {

    @Autowired
    SimulationStatusRepository simulationStatusRepository;
    @Autowired
    ScenarioRepository scenarioRepository;

    @Autowired
    SimulationRepository simulationRepository;

    @Override
    public List<SimulationStatus> getAllSimulationStatus() {
        return simulationStatusRepository.findAll();
    }

    @Override
    public String deleteALlByName(String ...names) {
        for (String name : names){
            Scenario scenario = scenarioRepository.findByName(name);
            List<Simulation> simulations = simulationRepository.findAllByScenarioId(scenario.getId());

            for(Simulation simulation : simulations){
                simulationStatusRepository.deleteAllSimulationStatusBySimulationId(simulation.getId());
            }

            simulationStatusRepository.deleteByScenarioId(scenario.getId());
        }
        return "Deleted without errors!";
    }
}
