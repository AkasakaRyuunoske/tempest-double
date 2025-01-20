package tempest_double;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import tempest_double.entity.Scenario.Scenario;
import tempest_double.entity.Scenario.ScenarioRepository;
import tempest_double.entity.Scenario.ScenarioServiceImplementation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScenarioServiceImplementationTests {

    @Mock
    private ScenarioRepository scenarioRepository;

    @InjectMocks
    private ScenarioServiceImplementation scenarioService;

    public ScenarioServiceImplementationTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetScenarios() {
        when(scenarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<Scenario> scenarios = scenarioService.getScenarios();

        assertNotNull(scenarios);
        assertTrue(scenarios.isEmpty());
        verify(scenarioRepository, times(1)).findAll();
    }

    @Test
    void testGetScenario() {
        int id = 1;
        Scenario scenario = new Scenario();
        when(scenarioRepository.findById(id)).thenReturn(java.util.Optional.of(scenario));

        Scenario result = scenarioService.getScenario(id);

        assertNotNull(result);
        assertEquals(scenario, result);
        verify(scenarioRepository, times(1)).findById(id);
    }

    @Test
    void testGetScenarioByName() {
        String name = "TestScenario";
        Scenario scenario = new Scenario();
        when(scenarioRepository.findByName(name)).thenReturn(scenario);

        ResponseEntity<Object> response = scenarioService.getScenarioByName(name);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(scenario, response.getBody());
        verify(scenarioRepository, times(1)).findByName(name);
    }

    @Test
    void testGetScenarioByNameNotFound() {
        String name = "NonExistent";
        when(scenarioRepository.findByName(name)).thenReturn(null);

        ResponseEntity<Object> response = scenarioService.getScenarioByName(name);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Scenario not found", response.getBody());
        verify(scenarioRepository, times(1)).findByName(name);
    }

    @Test
    void testPostScenario() {
        Scenario scenario = new Scenario();
        when(scenarioRepository.save(scenario)).thenReturn(scenario);

        ResponseEntity<Map<String, String>> response = scenarioService.postScenario(scenario);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().get("message"));
        assertEquals("200", response.getBody().get("status"));
        verify(scenarioRepository, times(1)).save(scenario);
    }

    @Test
    void testDeleteScenario() {
        int id = 1;
        when(scenarioRepository.existsById(id)).thenReturn(true);
        doNothing().when(scenarioRepository).deleteById(id);

        ResponseEntity<Map<String, String>> response = scenarioService.deleteScenario(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().get("message"));
        assertEquals("200", response.getBody().get("status"));
        verify(scenarioRepository, times(1)).existsById(id);
        verify(scenarioRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteScenarioNotFound() {
        int id = 1;
        when(scenarioRepository.existsById(id)).thenReturn(false);

        ResponseEntity<Map<String, String>> response = scenarioService.deleteScenario(id);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Scenario not found", response.getBody().get("message"));
        verify(scenarioRepository, times(1)).existsById(id);
    }

