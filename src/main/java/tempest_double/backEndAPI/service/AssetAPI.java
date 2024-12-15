package tempest_double.backEndAPI.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tempest_double.entity.Asset.Asset;
import tempest_double.entity.Asset.AssetRepository;
import tempest_double.entity.Asset.AssetService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")  // prefix for all controllers
@Log4j2
public class AssetAPI {
    // itrodotto per non violare PMD
    private final String message = "message";
    @Autowired
    AssetService assetService;

    @GetMapping("/assets")
    public List<Asset> getAssets() {
        return assetService.getAllAssets();
    }

    @GetMapping("/asset/{id}")
    public Asset getAssets(@PathVariable int id) {
        return assetService.getAsset(id);
    }

    @PostMapping("/asset")
    public ResponseEntity<String> postMapping(@RequestBody Asset asset) {
        return assetService.postAsset(asset);
    }

    @DeleteMapping("/asset/{id}")
    public ResponseEntity<String> deleteAsset(@PathVariable int id) {
        return assetService.deleteAsset(id);
    }

    @PutMapping("/asset/{id}")
    public ResponseEntity<String> updateAsset(@PathVariable int id, @RequestBody Asset asset) {
        return assetService.updateAsset(id, asset);
    }
}
