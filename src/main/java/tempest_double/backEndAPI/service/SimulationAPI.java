package tempest_double.backEndAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tempest_double.entity.Simulation.Simulation;
import tempest_double.entity.Simulation.SimulationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class SimulationAPI {
    @Autowired
    SimulationService simulationService;

    @GetMapping("/simulation/{id}")
    ResponseEntity<Simulation> getSimulationById(@PathVariable int id) {
        Simulation simulation = simulationService.getSimulationById(id);

        if (simulation == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        return ResponseEntity.ok(simulation);
    }

    @GetMapping("/simulations")
    ResponseEntity<List<Simulation>> getSimulations() {
        List<Simulation> simulations = simulationService.getSimulations();

        if (simulations == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        return ResponseEntity.ok(simulations);
    }

    @PostMapping("/simulation")
    ResponseEntity<String> postSimulation(@RequestBody int scenario_id) {
        return simulationService.postSimulation(scenario_id);
    }

    @DeleteMapping("/simulations")
    ResponseEntity<String> deleteSimulations(@RequestBody int... simulation_ids) {
        return simulationService.deleteSimulations(simulation_ids);
    }

    @DeleteMapping("/simulation/{id}")
    ResponseEntity<String> deleteSimulation(@PathVariable int id) {
        return simulationService.deleteSimulation(id);
    }
}
