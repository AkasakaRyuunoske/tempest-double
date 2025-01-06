package tempest_double.backEndAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tempest_double.entity.SimulationStatus.SimulationStatus;
import tempest_double.entity.SimulationStatus.SimulationStatusService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class SimulationStatusAPI {
    @Autowired
    SimulationStatusService simulationStatusService;

    @GetMapping("/simulations_status")
    public List<SimulationStatus> getAll(){
        return simulationStatusService.getAllSimulationStatus();
    }
}
