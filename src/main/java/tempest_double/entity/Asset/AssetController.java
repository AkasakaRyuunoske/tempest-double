package tempest_double.entity.Asset;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")  // prefix for all controllers
@Log4j2
public class AssetController {
    @Autowired
    private AssetRepository assetRepository;

    @GetMapping("/assets")
    public List<Asset> getAssets() {
        return assetRepository.findAll();
    }

    @PostMapping("/asset")
    public ResponseEntity<Map<String, String>> postMapping(@RequestBody Asset asset) {
        assetRepository.save(asset);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Success");
        response.put("status", "200");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/asset/{id}")
    public ResponseEntity<Map<String, String>> deleteAsset(@PathVariable int id) {
        assetRepository.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", String.valueOf(id));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/asset/{id}")
    public ResponseEntity<Map<String, String>> updateAsset(@PathVariable int id, @RequestBody Asset asset) {
        Map<String, String> response = new HashMap<>();

        boolean doesAssetExists = assetRepository.existsById(id);

        if (!doesAssetExists){
            response.put("message", "Error: Asset with provided ID not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        asset.setId(id);
        assetRepository.save(asset);

        response.put("message", "Asset with ID=" + id + " was successfully updated.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
