package tempest_double;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import tempest_double.entity.Scenario.Scenario;
import tempest_double.entity.Scenario.ScenarioRepository;
import tempest_double.entity.Scenario.ScenarioServiceImplementation;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ScenarioTests {

    @MockBean
    private ScenarioServiceImplementation scenarioServiceImplementation;

    @MockBean
    private ScenarioRepository scenarioRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Test per ottenere un singolo Scenario
    @Test
    public void testGetScenarioById_ShouldReturnScenario() throws Exception {
        // Given
        Map<String, Object> envConfig = new HashMap<>();
        envConfig.put("key", "value");
        Scenario scenario = new Scenario(1, envConfig, null, null, "Test Scenario", "Test Description");

        Mockito.when(scenarioServiceImplementation.getScenario(1)).thenReturn(scenario);

        // When + Then
        mockMvc.perform(get("/api/v1/scenario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Scenario"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        Mockito.verify(scenarioServiceImplementation).getScenario(1);
    }

    @Test
    public void testGetScenarioById_ShouldReturnNotFound_WhenScenarioDoesNotExist() throws Exception {
        // Given
        Mockito.when(scenarioServiceImplementation.getScenario(99)).thenReturn(null);

        // When + Then
        mockMvc.perform(get("/api/v1/scenario/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        Mockito.verify(scenarioServiceImplementation).getScenario(99);
    }

    // Test per ottenere tutti gli Scenarios
    @Test
    public void testGetAllScenarios_ShouldReturnListOfScenarios() throws Exception {
        // Given
        Scenario scenario1 = new Scenario(1, null, null, null, "Scenario1", "Description1");
        Scenario scenario2 = new Scenario(2, null, null, null, "Scenario2", "Description2");
        List<Scenario> scenarios = Arrays.asList(scenario1, scenario2);

        Mockito.when(scenarioServiceImplementation.getScenarios()).thenReturn(scenarios);

        // When + Then
        mockMvc.perform(get("/api/v1/scenarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Scenario1"))
                .andExpect(jsonPath("$[1].name").value("Scenario2"));

        Mockito.verify(scenarioServiceImplementation).getScenarios();
    }

    @Test
    public void testGetAllScenarios_ShouldReturnEmptyList_WhenNoScenariosExist() throws Exception {
        // Given
        Mockito.when(scenarioServiceImplementation.getScenarios()).thenReturn(Collections.emptyList());

        // When + Then
        mockMvc.perform(get("/api/v1/scenarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        Mockito.verify(scenarioServiceImplementation).getScenarios();
    }

    // Test per creare uno Scenario
    @Test
    public void testPostScenario_ShouldReturnSuccess() throws Exception {
        // Given
        Scenario scenario = new Scenario(0, null, null, null, "New Scenario", "Description");

        Mockito.when(scenarioServiceImplementation.postScenario(any(Scenario.class)))
                .thenReturn(ResponseEntity.ok(Map.of("message", "Success", "status", "200")));

        String jsonBody = objectMapper.writeValueAsString(scenario);

        // When + Then
        mockMvc.perform(post("/api/v1/scenario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.status").value("200"));

        Mockito.verify(scenarioServiceImplementation).postScenario(any(Scenario.class));
    }

    @Test
    public void testPostScenario_ShouldReturnBadRequest_WhenInputIsInvalid() throws Exception {
        // Given
        String jsonBody = "{}"; // Empty JSON

        // When + Then
        mockMvc.perform(post("/api/v1/scenario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest());

        Mockito.verify(scenarioServiceImplementation, times(0)).postScenario(any(Scenario.class));
    }

    // Test per aggiornare uno Scenario
    @Test
    public void testUpdateScenario_ShouldReturnSuccess() throws Exception {
        // Given
        Scenario updatedScenario = new Scenario(1, null, null, null, "Updated Scenario", "Updated Description");

        Mockito.when(scenarioServiceImplementation.updateScenario(Mockito.eq(1), any(Scenario.class)))
                .thenReturn(ResponseEntity.ok(Map.of("message", "Success", "status", "200")));

        String jsonBody = objectMapper.writeValueAsString(updatedScenario);

        // When + Then
        mockMvc.perform(put("/api/v1/scenario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.status").value("200"));

        Mockito.verify(scenarioServiceImplementation).updateScenario(Mockito.eq(1), any(Scenario.class));
    }

    @Test
    public void testUpdateScenario_ShouldReturnNotFound_WhenScenarioDoesNotExist() throws Exception {
        // Given
        Scenario updatedScenario = new Scenario(99, null, null, null, "Updated Scenario", "Updated Description");

        Mockito.when(scenarioServiceImplementation.updateScenario(Mockito.eq(99), any(Scenario.class)))
                .thenReturn(ResponseEntity.status(404).body(Map.of("message", "Error: Scenario with provided ID not found.")));

        String jsonBody = objectMapper.writeValueAsString(updatedScenario);

        // When + Then
        mockMvc.perform(put("/api/v1/scenario/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Error: Scenario with provided ID not found."));

        Mockito.verify(scenarioServiceImplementation).updateScenario(Mockito.eq(99), any(Scenario.class));
    }

    // Test per eliminare uno Scenario
    @Test
    public void testDeleteScenario_ShouldReturnSuccess() throws Exception {
        // Mock service
        Mockito.when(scenarioServiceImplementation.deleteScenario(1))
                .thenReturn(ResponseEntity.ok(Map.of("message", "Success", "status", "200")));

        // When + Then
        mockMvc.perform(delete("/api/v1/scenario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.status").value("200"));

        Mockito.verify(scenarioServiceImplementation).deleteScenario(1);
    }

    @Test
    public void testDeleteScenario_ShouldReturnNotFound_WhenScenarioDoesNotExist() throws Exception {
        // Mock service
        Mockito.when(scenarioServiceImplementation.deleteScenario(99))
                .thenReturn(ResponseEntity.status(404).body(Map.of("message", "Scenario not found")));

        // When + Then
        mockMvc.perform(delete("/api/v1/scenario/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Scenario not found"));

        Mockito.verify(scenarioServiceImplementation).deleteScenario(99);
    }
}
