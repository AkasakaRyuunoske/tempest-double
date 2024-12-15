package tempest_double.backEndAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tempest_double.entity.Scenario.ScenarioService;
import tempest_double.entity.Simulation.Simulation;
import tempest_double.entity.Simulation.SimulationService;

@RestController
@RequestMapping("/api/v1/")
public class SimulationAPI {
    @Autowired
    SimulationService simulationService;

    @GetMapping("/simulation/{id}")
    ResponseEntity<Simulation> getSimulationById(@PathVariable int id){
        Simulation simulation = simulationService.getSimulationById(id);

        if (simulation == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        return ResponseEntity.ok(simulation);
    }
}
