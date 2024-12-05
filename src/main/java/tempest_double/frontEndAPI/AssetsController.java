package tempest_double.frontEndAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import tempest_double.entity.Asset.AssetServiceImplementation;

@Controller
public class AssetsController {
    @Autowired
    AssetServiceImplementation assetServiceImplementation;

    @GetMapping("/assets")
    public String assets() {
        assetServiceImplementation.printAsset();
        return "assets";
    }
}
