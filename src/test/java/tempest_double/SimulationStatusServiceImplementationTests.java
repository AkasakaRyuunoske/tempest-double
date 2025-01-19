package tempest_double;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tempest_double.entity.Scenario.Scenario;
import tempest_double.entity.Scenario.ScenarioRepository;
import tempest_double.entity.Simulation.Simulation;
import tempest_double.entity.Simulation.SimulationRepository;
import tempest_double.entity.SimulationStatus.SimulationStatus;
import tempest_double.entity.SimulationStatus.SimulationStatusRepository;
import tempest_double.entity.SimulationStatus.SimulationStatusServiceImplementation;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SimulationStatusServiceImplementationTests {

    @Mock
    private SimulationStatusRepository simulationStatusRepository;

    @Mock
    private ScenarioRepository scenarioRepository;

    @Mock
    private SimulationRepository simulationRepository;

    @InjectMocks
    private SimulationStatusServiceImplementation simulationStatusService;


    @Test
    void testGetAllSimulationStatus() {
        SimulationStatus status1 = new SimulationStatus();
        status1.setAssets_results(Map.of("asset1", "value1"));
        status1.setEnvironmental_changes(Map.of("env1", "change1"));

        SimulationStatus status2 = new SimulationStatus();
        status2.setAssets_results(Map.of("asset2", "value2"));
        status2.setEnvironmental_changes(Map.of("env2", "change2"));

        List<SimulationStatus> mockStatuses = List.of(status1, status2);
        when(simulationStatusRepository.findAll()).thenReturn(mockStatuses);

        List<SimulationStatus> result = simulationStatusService.getAllSimulationStatus();

        assertEquals(2, result.size());
        assertEquals("value1", result.get(0).getAssets_results().get("asset1"));
        assertEquals("value2", result.get(1).getAssets_results().get("asset2"));
        verify(simulationStatusRepository, times(1)).findAll();
    }

    @Test
    void testDeleteAllByName() {
        Scenario mockScenario = new Scenario();
        mockScenario.setId(1);
        when(scenarioRepository.findByName("testScenario")).thenReturn(mockScenario);

        Simulation mockSimulation1 = new Simulation();
        mockSimulation1.setId(100);

        Simulation mockSimulation2 = new Simulation();
        mockSimulation2.setId(101);

        List<Simulation> mockSimulations = List.of(mockSimulation1, mockSimulation2);
        when(simulationRepository.findAllByScenarioId(1)).thenReturn(mockSimulations);

        String result = simulationStatusService.deleteALlByName("testScenario");

        assertEquals("Deleted without errors!", result);
        verify(scenarioRepository, times(1)).findByName("testScenario");
        verify(simulationRepository, times(1)).findAllByScenarioId(1);
        verify(simulationStatusRepository, times(1)).deleteAllSimulationStatusBySimulationId(100);
        verify(simulationStatusRepository, times(1)).deleteAllSimulationStatusBySimulationId(101);
        verify(simulationStatusRepository, times(1)).deleteByScenarioId(1);
    }
}
