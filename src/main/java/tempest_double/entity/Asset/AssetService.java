package tempest_double.entity.Asset;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AssetService {
    String printAsset();

    List<Asset> getAllAssets();

    Asset getAsset(int id);

    ResponseEntity<String> postAsset(Asset asset);

    ResponseEntity<String> deleteAsset(int id);

    ResponseEntity<String> updateAsset(int id, Asset asset);
}
