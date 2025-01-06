package tempest_double.entity.SimulationStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationStatusRepository extends JpaRepository<SimulationStatus, Integer> {

}
