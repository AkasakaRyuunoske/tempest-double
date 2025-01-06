package tempest_double.entity.SimulationStatus;

import java.util.List;

public interface SimulationStatusService {
    List<SimulationStatus> getAllSimulationStatus();

    String deleteALlByName(String ...names);
}
