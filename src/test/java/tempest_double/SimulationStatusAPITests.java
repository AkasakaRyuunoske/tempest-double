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
import tempest_double.entity.SimulationStatus.SimulationStatusService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(SimulationStatusAPI.class)
class SimulationStatusAPITests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimulationStatusService simulationStatusService;

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
