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

import static org.junit.jupiter.api.Assertions.*;
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
    void testPostSimulationWithSolarPanel() throws Exception {
        String scenarioJson = "{\"name\":\"testScenario\"}";

        Scenario mockScenario = new Scenario();
        mockScenario.setTopology(Map.of("nodes", List.of(Map.of("name", "SolarPanel1"))));
        when(scenarioRepository.findByName("testScenario")).thenReturn(mockScenario);

        Asset mockAsset = new Asset();
        mockAsset.setName("SolarPanel1");
        mockAsset.setType("solar_panel");
        mockAsset.setConfiguration(Map.of(
                "area", "10",
                "efficiency", "0.9",
                "nominal-power", "1000"
        ));
        when(assetRepository.findByName("SolarPanel1")).thenReturn(mockAsset);

        ResponseEntity<Map<String, Object>> response = simulationService.postSimulation(scenarioJson);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("Asset_0"));

        Map<String, Object> assetInfo = (Map<String, Object>) response.getBody().get("Asset_0");
        assertEquals("SolarPanel1", assetInfo.get("name"));
        assertEquals("solar_panel", assetInfo.get("type"));
        assertEquals("Producer", assetInfo.get("role"));
        assertEquals("1000.0", assetInfo.get("nominal_power").toString());

        verify(simulationRepository, times(1)).save(any(Simulation.class));
    }

    @Test
    void testPostSimulationWithWindTurbine() throws Exception {
        String scenarioJson = "{\"name\":\"testScenario\"}";
        Scenario mockScenario = new Scenario();
        mockScenario.setTopology(Map.of("nodes", List.of(Map.of("name", "WindTurbine1"))));
        when(scenarioRepository.findByName("testScenario")).thenReturn(mockScenario);

        Asset mockAsset = new Asset();
        mockAsset.setName("WindTurbine1");
        mockAsset.setType("wind_turbine");
        mockAsset.setConfiguration(Map.of(
                "dissipation-factor", "0.05",
                "nominal-power", "1500",
                "blade-length", "50"
        ));
        when(assetRepository.findByName("WindTurbine1")).thenReturn(mockAsset);

        ResponseEntity<Map<String, Object>> response = simulationService.postSimulation(scenarioJson);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("Asset_0"));
        Map<String, Object> assetInfo = (Map<String, Object>) response.getBody().get("Asset_0");
        assertEquals("WindTurbine1", assetInfo.get("name"));
        assertEquals("wind_turbine", assetInfo.get("type"));
        assertEquals("Producer", assetInfo.get("role"));
        assertEquals("1500.0", assetInfo.get("nominal_power").toString());
        verify(simulationRepository, times(1)).save(any(Simulation.class));
    }

    @Test
    void testPostSimulationWithFuelCell() throws Exception {
        String scenarioJson = "{\"name\":\"testScenario\"}";
        Scenario mockScenario = new Scenario();
        mockScenario.setTopology(Map.of("nodes", List.of(Map.of("name", "FuelCell1"))));
        when(scenarioRepository.findByName("testScenario")).thenReturn(mockScenario);

        Asset mockAsset = new Asset();
        mockAsset.setName("FuelCell1");
        mockAsset.setType("fuel_cell");
        mockAsset.setConfiguration(Map.of(
                "nominal-power", "1000",
                "fuel-capacity", "500",
                "current-fuel", "250"
        ));
        when(assetRepository.findByName("FuelCell1")).thenReturn(mockAsset);

        ResponseEntity<Map<String, Object>> response = simulationService.postSimulation(scenarioJson);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("Asset_0"));
        Map<String, Object> assetInfo = (Map<String, Object>) response.getBody().get("Asset_0");
        assertEquals("FuelCell1", assetInfo.get("name"));
        assertEquals("fuel_cell", assetInfo.get("type"));
        assertEquals("Producer", assetInfo.get("role"));
        assertEquals("1000.0", assetInfo.get("nominal_power").toString());
        verify(simulationRepository, times(1)).save(any(Simulation.class));
    }

    @Test
    void testPostSimulationWithGenericConsumer() throws Exception {
        String scenarioJson = "{\"name\":\"testScenario\"}";
        Scenario mockScenario = new Scenario();
        mockScenario.setTopology(Map.of("nodes", List.of(Map.of("name", "GenericConsumer1"))));
        when(scenarioRepository.findByName("testScenario")).thenReturn(mockScenario);

        Asset mockAsset = new Asset();
        mockAsset.setName("GenericConsumer1");
        mockAsset.setType("generic_consumer");
        mockAsset.setConfiguration(Map.of(
                "nominal-power", "800",
                "tau", "0.7",
                "min-consumption", "300"
        ));
        when(assetRepository.findByName("GenericConsumer1")).thenReturn(mockAsset);

        ResponseEntity<Map<String, Object>> response = simulationService.postSimulation(scenarioJson);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("Asset_0"));
        Map<String, Object> assetInfo = (Map<String, Object>) response.getBody().get("Asset_0");
        assertEquals("GenericConsumer1", assetInfo.get("name"));
        assertEquals("generic_consumer", assetInfo.get("type"));
        assertEquals("Consumer", assetInfo.get("role"));
        assertEquals("800.0", assetInfo.get("nominal_power").toString());
        verify(simulationRepository, times(1)).save(any(Simulation.class));
    }

    @Test
    void testPostSimulationWithUnsupportedType() throws Exception {
        String scenarioJson = "{\"name\":\"testScenario\"}";
        Scenario mockScenario = new Scenario();
        mockScenario.setTopology(Map.of("nodes", List.of(Map.of("name", "UnknownAsset"))));
        when(scenarioRepository.findByName("testScenario")).thenReturn(mockScenario);

        Asset mockAsset = new Asset();
        mockAsset.setName("UnknownAsset");
        mockAsset.setType("unknown_type");
        mockAsset.setConfiguration(Map.of());
        when(assetRepository.findByName("UnknownAsset")).thenReturn(mockAsset);

        ResponseEntity<Map<String, Object>> response = simulationService.postSimulation(scenarioJson);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().containsKey("Asset_0")); // Unsupported asset should not be added
        verify(simulationRepository, times(1)).save(any(Simulation.class));
    }

    @Test
    void testPostSimulationJsonParsingError() {
        String invalidJson = "{invalid}";

        ResponseEntity<Map<String, Object>> response = simulationService.postSimulation(invalidJson);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
    }

    @Test
    void testPostSimulationAssetNotFound() {
        String scenarioJson = "{\"name\":\"testScenario\"}";
        Scenario mockScenario = new Scenario();
        mockScenario.setTopology(Map.of("nodes", List.of(Map.of("name", "asset1"))));
        when(scenarioRepository.findByName("testScenario")).thenReturn(mockScenario);

        when(assetRepository.findByName("asset1")).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = simulationService.postSimulation(scenarioJson);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
    }

    @Test
    void testDeleteSimulation() {
        int simulationId = 1;

        ResponseEntity<String> response = simulationService.deleteSimulation(simulationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted without errors", response.getBody());
        verify(simulationRepository, times(1)).deleteById(simulationId);
    }

    @Test
    void testDeleteSimulations() {
        int[] simulationIds = {1, 2, 3};

        ResponseEntity<String> response = simulationService.deleteSimulations(simulationIds);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted all without errors", response.getBody());
        verify(simulationRepository, times(simulationIds.length)).deleteById(anyInt());
    }

}
