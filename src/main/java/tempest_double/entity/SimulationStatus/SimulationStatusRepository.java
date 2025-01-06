package tempest_double.entity.SimulationStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SimulationStatusRepository extends JpaRepository<SimulationStatus, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Simulation s WHERE s.scenario.id = :scenarioId")
    void deleteByScenarioId(@Param("scenarioId") int scenarioId);

    @Modifying
    @Transactional
    void deleteAllSimulationStatusBySimulationId(int id);
}
