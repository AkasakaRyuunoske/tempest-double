package tempest_double;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tempest_double.backEndAPI.service.SimulationAPI;
import tempest_double.entity.Scenario.Scenario;
import tempest_double.entity.Simulation.Simulation;
import tempest_double.entity.Simulation.SimulationServiceImplementation;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SimulationAPI.class)
public class SimulationAPITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SimulationServiceImplementation simulationService;

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
        Mockito.when(simulationService.getSimulations()).thenReturn(mockSimulations);

        mockMvc.perform(get("/api/v1/simulations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

}

