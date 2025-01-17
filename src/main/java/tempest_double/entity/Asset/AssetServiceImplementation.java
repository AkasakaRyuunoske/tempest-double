package tempest_double.entity.Asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImplementation implements AssetService{
    @Autowired
    private AssetRepository assetRepository;

    @Override
    public String printAsset() {
        return "implemented for test";
    }

    @Override
    public List<Asset> getAllAssets(){
        return assetRepository.findAll();
    }

    @Override
    public Asset getAsset(int id){
        return assetRepository.findById(id);
    }

    @Override
    public ResponseEntity<String> postAsset(Asset asset){
        try{
            assetRepository.save(asset);
            return new ResponseEntity<>("Asset saved without errors", HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity<>("Unexpected Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteAsset(int id){
        assetRepository.deleteById(id);

        return new ResponseEntity<>("Asset deleted without errors", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateAsset(int id, Asset asset){

        boolean doesAssetExists = assetRepository.existsById(id);

        if (!doesAssetExists) return new ResponseEntity<>("Error: Asset with provided ID not found.", HttpStatus.NOT_FOUND);

        asset.setId(id);
        assetRepository.save(asset);

        return new ResponseEntity<>("Updated without errors", HttpStatus.OK);
    }
}
