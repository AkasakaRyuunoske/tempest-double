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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tempest_double.backEndAPI.service.AssetAPI;
import tempest_double.entity.Asset.Asset;
import tempest_double.entity.Asset.AssetRepository;
import tempest_double.entity.Asset.AssetServiceImplementation;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssetAPI.class)
public class AssetsAPITests {

    @MockBean
    AssetServiceImplementation assetServiceImplementation;

    @MockBean
    AssetRepository assetRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAssetById_ShouldReturnAsset() throws Exception {
        // Given
        Map<String, Object> assetConfiguration = new HashMap<>();
        assetConfiguration.put("nominal_power", 4500);

        Asset asset = new Asset(1, "Solar Panel", assetConfiguration, "Panello solare", "producer");

        // Mock service behavior
        Mockito.when(assetServiceImplementation.getAsset(1)).thenReturn(asset);

        // When + Then
        mockMvc.perform(get("/api/v1/asset/1"))
                .andExpect(status().isOk()) // HTTP 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("Solar Panel"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.configuration.nominal_power").value(4500))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Panello solare"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("producer"));

        Mockito.verify(assetServiceImplementation).getAsset(1);
    }

    @Test
    public void testGetAllAssets_ShouldReturnListOfAssets() throws Exception {
        // Given
        Asset asset1 = new Asset(1, "Solar Panel", null, "Panello solare", "producer");
        Asset asset2 = new Asset(2, "Wind Turbine", null, "Turbina eolica", "producer");
        List<Asset> mockAssets = Arrays.asList(asset1, asset2);

        Mockito.when(assetServiceImplementation.getAllAssets()).thenReturn(mockAssets);

        // When + Then
        mockMvc.perform(get("/api/v1/assets"))
                .andExpect(status().isOk()) // HTTP 200
                .andExpect(jsonPath("$.length()").value(2)) // We expect 2 in the array
                .andExpect(jsonPath("$[0].type").value("Solar Panel"))
                .andExpect(jsonPath("$[1].type").value("Wind Turbine"));

        Mockito.verify(assetServiceImplementation).getAllAssets();
    }

    @Test
    public void testPostAsset_ShouldReturnOkAndMessage() throws Exception {
        // Given
        Asset newAsset = new Asset(0, "Solar Panel", null, "New Panel", "producer");

        // Mock a successful response
        Mockito.when(assetServiceImplementation.postAsset(any(Asset.class)))
                .thenReturn(ResponseEntity.ok("Asset saved without errors"));

        // Convert object to JSON
        String jsonBody = objectMapper.writeValueAsString(newAsset);

        // When + Then
        mockMvc.perform(post("/api/v1/asset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk()) // HTTP 200
                .andExpect(content().string("Asset saved without errors"));

        Mockito.verify(assetServiceImplementation).postAsset(any(Asset.class));
    }

    @Test
    public void testUpdateAsset_ShouldReturnOkOnSuccess() throws Exception {
        // Given
        Asset updatedAsset = new Asset(1, "Solar Panel", null, "Updated Panel", "producer");

        // Mock success response
        Mockito.when(assetServiceImplementation.updateAsset(Mockito.eq(1), any(Asset.class)))
                .thenReturn(ResponseEntity.ok("Updated without errors"));

        String jsonBody = objectMapper.writeValueAsString(updatedAsset);

        // When + Then
        mockMvc.perform(put("/api/v1/asset/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated without errors"));

        Mockito.verify(assetServiceImplementation).updateAsset(Mockito.eq(1), any(Asset.class));
    }

    @Test
    public void testDeleteAsset_ShouldReturnOk() throws Exception {
        // Mock the service
        Mockito.when(assetServiceImplementation.deleteAsset(10))
                .thenReturn(ResponseEntity.ok("Asset deleted without errors"));

        // When + Then
        mockMvc.perform(delete("/api/v1/asset/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Asset deleted without errors"));

        Mockito.verify(assetServiceImplementation, times(1)).deleteAsset(10);
    }
}
