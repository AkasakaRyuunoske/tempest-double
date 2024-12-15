package tempest_double;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tempest_double.backEndAPI.service.AssetAPI;
import tempest_double.entity.Asset.Asset;
import tempest_double.entity.Asset.AssetRepository;
import tempest_double.entity.Asset.AssetServiceImplementation;
import tempest_double.frontEndAPI.AssetsController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssetAPI.class)
public class AssetsAPITests {
    @MockBean
    AssetServiceImplementation assetServiceImplementation;

    @MockBean
    AssetRepository assetRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPostAssetExceptToSuccess() throws Exception {
        Map<String, Object> assetConfiguration = new HashMap<>();
        assetConfiguration.put("nominal_power", 4500);

        Asset asset = new Asset(1, "Solar Panel", assetConfiguration, "Panello solare", "producer");

        Mockito.when(assetServiceImplementation.getAsset(1)).thenReturn(asset); // Mock service behavior

        mockMvc.perform(get("/api/v1/asset/1"))
                .andExpect(status().isOk()) // Check for HTTP 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("Solar Panel"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.configuration.nominal_power").value(4500))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Panello solare"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("producer"));

        Mockito.verify(assetServiceImplementation).getAsset(1);
    }
}
