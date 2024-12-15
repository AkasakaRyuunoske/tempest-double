package tempest_double;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tempest_double.entity.Asset.AssetServiceImplementation;
import tempest_double.frontEndAPI.AssetsController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssetsController.class)
public class AssetsAPITests {
    @MockBean
    AssetServiceImplementation assetServiceImplementation;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPostAssetExceptToSuccess() throws Exception {

        Mockito.when(assetServiceImplementation.printAsset()).thenReturn("implemented for test"); // Mock service behavior

        mockMvc.perform(get("/assets")).andExpect(status().isOk()); // Checks for HTTP 200 status
    }
}
