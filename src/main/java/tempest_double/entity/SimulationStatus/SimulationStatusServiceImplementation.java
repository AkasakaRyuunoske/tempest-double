package tempest_double.entity.SimulationStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulationStatusServiceImplementation implements SimulationStatusService {

    @Autowired
    SimulationStatusRepository simulationStatusRepository;

    @Override
    public List<SimulationStatus> getAllSimulationStatus() {
        return simulationStatusRepository.findAll();
    }
}
