package tempest_double.entity.Asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetServiceImplementation implements AssetService{
    @Autowired
    private AssetRepository assetRepository;

    @Override
    public void printAsset() {
        Asset asset = assetRepository.findById(2);
        System.out.println("Printing found asset!");
        System.out.println(asset.getConfiguration());
    }
}
