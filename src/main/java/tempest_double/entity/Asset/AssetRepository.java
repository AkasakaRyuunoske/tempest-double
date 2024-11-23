package tempest_double.entity.Asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Integer> {
    Asset findById(int id);
}
