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

