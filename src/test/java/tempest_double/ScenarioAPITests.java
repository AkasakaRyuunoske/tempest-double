package tempest_double;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import tempest_double.backEndAPI.service.ScenarioAPI;
import tempest_double.entity.Scenario.Scenario;
import tempest_double.entity.Scenario.ScenarioService;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScenarioAPI.class)
public class ScenarioAPITests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScenarioService scenarioService;

    @Test
    void testGetAllScenarios() throws Exception {
        List<Scenario> mockScenarios = List.of(
                new Scenario(1, Map.of(), Map.of(), Map.of(), "Scenario1", "Description1"),
                new Scenario(2, Map.of(), Map.of(), Map.of(), "Scenario2", "Description2")
        );

        when(scenarioService.getScenarios()).thenReturn(mockScenarios);

        mockMvc.perform(get("/api/scenarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Scenario1"))
                .andExpect(jsonPath("$[1].name").value("Scenario2"));

        verify(scenarioService, times(1)).getScenarios();
    }

    @Test
    void testGetScenarioById() throws Exception {
        Scenario mockScenario = new Scenario(1, Map.of(), Map.of(), Map.of(), "Scenario1", "Description1");
        when(scenarioService.getScenario(1)).thenReturn(mockScenario);

        mockMvc.perform(get("/api/scenarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Scenario1"));

        verify(scenarioService, times(1)).getScenario(1);
    }

    @Test
    void testGetScenarioByIdNotFound() throws Exception {
        when(scenarioService.getScenario(1)).thenReturn(null);

        mockMvc.perform(get("/api/scenarios/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Scenario not found"));

        verify(scenarioService, times(1)).getScenario(1);
    }

    @Test
    void testCreateScenario() throws Exception {
        Scenario scenario = new Scenario(0, Map.of(), Map.of(), Map.of(), "NewScenario", "NewDescription");

        when(scenarioService.postScenario(Mockito.any()))
                .thenReturn(ResponseEntity.ok(Map.of("message", "Success", "status", "200")));

        mockMvc.perform(post("/api/scenarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"NewScenario\"," +
                                "\"description\":\"NewDescription\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));

        verify(scenarioService, times(1)).postScenario(any());
    }

    @Test
    void testUpdateScenario() throws Exception {
        when(scenarioService.updateScenario(eq(1), any()))
                .thenReturn(ResponseEntity.ok(Map.of("message", "Success")));

        mockMvc.perform(put("/api/scenarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"UpdatedScenario\"," +
                                "\"description\":\"UpdatedDescription\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));

        verify(scenarioService, times(1)).updateScenario(eq(1), any());
    }

    @Test
    void testDeleteScenario() throws Exception {
        when(scenarioService.deleteScenario(1))
                .thenReturn(ResponseEntity.ok(Map.of("message", "Success")));

        mockMvc.perform(delete("/api/scenarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));

        verify(scenarioService, times(1)).deleteScenario(1);
    }

    @Test
    void testDeleteScenarioNotFound() throws Exception {
        when(scenarioService.deleteScenario(1))
                .thenReturn(ResponseEntity.status(404).body(Map.of("message", "Scenario not found")));

        mockMvc.perform(delete("/api/scenarios/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Scenario not found"));

        verify(scenarioService, times(1)).deleteScenario(1);
    }
}
