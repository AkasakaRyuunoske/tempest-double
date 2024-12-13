package tempest_double.entity.Asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetServiceImplementation implements AssetService{
    @Autowired
    private AssetRepository assetRepository;

    @Override
    public String printAsset() {
        return "implemented for test";
    }
}
