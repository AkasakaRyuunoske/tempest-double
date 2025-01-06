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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tempest_double.backEndAPI.service.SimulationAPI;
import tempest_double.entity.Scenario.Scenario;
import tempest_double.entity.Simulation.Simulation;
import tempest_double.entity.Simulation.SimulationServiceImplementation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SimulationAPI.class)
public class SimulationAPITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SimulationServiceImplementation simulationService;

    @Test
    void getSimulationById_WhenExists_ShouldReturnSimulation() throws Exception {
        Simulation simulation = new Simulation();
        simulation.setId(1);
        simulation.setScenario(new Scenario());

        when(simulationService.getSimulationById(1)).thenReturn(simulation);

        mockMvc.perform(get("/api/v1/simulation/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(simulationService).getSimulationById(1);
    }

    @Test
    void getSimulationById_WhenNotExists_ShouldReturn404() throws Exception {
        when(simulationService.getSimulationById(999)).thenReturn(null);

        mockMvc.perform(get("/api/v1/simulation/999"))
                .andExpect(status().isNotFound());

        verify(simulationService).getSimulationById(999);
    }

    @Test
    void getSimulations_WhenExist_ShouldReturnList() throws Exception {
        List<Simulation> simulations = Arrays.asList(
                new Simulation(),
                new Simulation()
        );
        when(simulationService.getSimulations()).thenReturn(simulations);

        mockMvc.perform(get("/api/v1/simulations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(simulationService).getSimulations();
    }

    @Test
    void getSimulations_WhenEmpty_ShouldReturn404() throws Exception {
        when(simulationService.getSimulations()).thenReturn(null);

        mockMvc.perform(get("/api/v1/simulations"))
                .andExpect(status().isNotFound());

        verify(simulationService).getSimulations();
    }

    @Test
    public void getAllSimulations_shouldReturnAllSimulations() throws Exception {

        Scenario mockScenario = new Scenario();
        Simulation simulation1 = new Simulation();
        simulation1.setId(1);
        simulation1.setScenario(mockScenario);

        Simulation simulation2 = new Simulation();
        simulation2.setId(2);
        simulation2.setScenario(mockScenario);

        List<Simulation> mockSimulations = Arrays.asList(simulation1, simulation2);
        when(simulationService.getSimulations()).thenReturn(mockSimulations);

        mockMvc.perform(get("/api/v1/simulations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void postSimulation_shouldCreateSimulation() throws Exception {
        Map<String, Object> mockResponse = Map.of("status", "success");
        String inputJson = "{ \"name\": \"Scenario1\" }";

        when(simulationService.postSimulation(Mockito.anyString())).thenReturn(ResponseEntity.ok(mockResponse));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/simulation/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));

        verify(simulationService, times(1)).postSimulation(Mockito.anyString());
    }

    @Test
    public void testDeleteSimulation_ShouldReturnOk() throws Exception {
        when(simulationService.deleteSimulation(1))
                .thenReturn(ResponseEntity.ok("Deleted without errors"));

        mockMvc.perform(delete("/api/v1/simulation/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted without errors"));

        verify(simulationService, times(1)).deleteSimulation(1);
    }

}

