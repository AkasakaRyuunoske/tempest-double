package tempest_double;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tempest_double.backEndAPI.service.SimulationStatusAPI;
import tempest_double.entity.Simulation.Simulation;
import tempest_double.entity.SimulationStatus.SimulationStatus;
import tempest_double.entity.SimulationStatus.SimulationStatusService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(SimulationStatusAPI.class)
class SimulationStatusAPITests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimulationStatusService simulationStatusService;

    @Test
    void testSimulationStatusId() {
        SimulationStatus status = new SimulationStatus();
        status.setId(1);

        assertEquals(1, status.getId());
    }

    @Test
    void testGetAllSimulationStatusWithoutId() throws Exception {
        SimulationStatus status1 = new SimulationStatus();
        status1.setAssets_results(Map.of("asset1", "value1"));
        status1.setEnvironmental_changes(Map.of("env1", "change1"));
        status1.setSimulation(new Simulation());

        List<SimulationStatus> mockStatuses = List.of(status1);

        when(simulationStatusService.getAllSimulationStatus()).thenReturn(mockStatuses);

        mockMvc.perform(get("/api/v1/simulations_status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].assets_results.asset1").value("value1"))
                .andExpect(jsonPath("$[0].environmental_changes.env1").value("change1"));

        verify(simulationStatusService, times(1)).getAllSimulationStatus();
    }

    @Test
    void testDeleteAllSimulationStatus() throws Exception {
        String[] namesToDelete = {"Simulation1", "Simulation2"};
        when(simulationStatusService.deleteALlByName(namesToDelete)).thenReturn("Deleted successfully");

        mockMvc.perform(delete("/api/v1/simulations_status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(namesToDelete)))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted successfully"));

        verify(simulationStatusService, times(1)).deleteALlByName(namesToDelete);
    }
}
