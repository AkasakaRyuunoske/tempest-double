package tempest_double;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tempest_double.entity.Asset.Asset;
import tempest_double.entity.Asset.AssetRepository;
import tempest_double.entity.Scenario.Scenario;
import tempest_double.entity.Scenario.ScenarioRepository;
import tempest_double.entity.Simulation.Simulation;
import tempest_double.entity.Simulation.SimulationRepository;
import tempest_double.entity.Simulation.SimulationServiceImplementation;
import tempest_double.entity.SimulationStatus.SimulationStatusRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SimulationServiceImplementationTests {

    @InjectMocks
    private SimulationServiceImplementation simulationService;

    @Mock
    private SimulationRepository simulationRepository;

    @Mock
    private ScenarioRepository scenarioRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private SimulationStatusRepository simulationStatusRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetSimulationById() {
        int id = 1;
        Simulation mockSimulation = new Simulation();
        when(simulationRepository.findById(id)).thenReturn(mockSimulation);

        Simulation result = simulationService.getSimulationById(id);

        assertEquals(mockSimulation, result);
        verify(simulationRepository, times(1)).findById(id);
    }

    @Test
    void testGetSimulations() {
        List<Simulation> mockSimulations = List.of(new Simulation(), new Simulation());
        when(simulationRepository.findAll()).thenReturn(mockSimulations);

        List<Simulation> result = simulationService.getSimulations();

        assertEquals(mockSimulations.size(), result.size());
        verify(simulationRepository, times(1)).findAll();
    }

    @Test
    void testPostSimulationSuccess() throws Exception {
        String scenarioJson = "{\"name\":\"testScenario\"}";
        Scenario mockScenario = new Scenario();
        mockScenario.setTopology(Map.of("nodes", List.of(Map.of("name", "asset1"))));
        when(scenarioRepository.findByName("testScenario")).thenReturn(mockScenario);

        Asset mockAsset = new Asset();
        mockAsset.setName("asset1");
        mockAsset.setType("solar_panel");
        mockAsset.setConfiguration(Map.of("area", "10", "efficiency", "0.9", "nominal-power", "1000"));
        when(assetRepository.findByName("asset1")).thenReturn(mockAsset);

        ResponseEntity<Map<String, Object>> response = simulationService.postSimulation(scenarioJson);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("Asset_0"));
        verify(simulationRepository, times(1)).save(any(Simulation.class));
    }

}
