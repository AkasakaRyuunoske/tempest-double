package tempest_double.backEndAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tempest_double.entity.Simulation.Simulation;
import tempest_double.entity.Simulation.SimulationService;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/")
@RestController
public class SimulationAPI {

    @Autowired
    private SimulationService simulationService;

    @GetMapping("/simulations")
    public List<Simulation> getSimulations() {
        return simulationService.getSimulations();
    }

    @GetMapping("/simulation/{id}")
    public Simulation getSimulation(@PathVariable int id) {
        return simulationService.getSimulation(id);
    }

    @PostMapping("/simulation")
    public ResponseEntity<Map<String, String>> postSimulation(@RequestBody Simulation simulation) {
        return simulationService.postSimulation(simulation);
    }

    @DeleteMapping("/simulation/{id}")
    public ResponseEntity<Map<String, String>> deleteSimulation(@PathVariable int id) {
        return simulationService.deleteSimulation(id);
    }

    @PutMapping("/simulation/{id}")
    public ResponseEntity<Map<String, String>> updateSimulation(@PathVariable int id, @RequestBody Simulation simulation) {
        return simulationService.updateSimulation(id, simulation);
    }
}
