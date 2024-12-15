package tempest_double.entity.Simulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimulationServiceImplementation implements SimulationService{
    @Autowired
    SimulationRepository simulationRepository;

    @Override
    public Simulation getSimulationById(int id) {
        return simulationRepository.findById(id);
    }
}
