package systems.nope.worldseed.stat;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.stat.model.StatSheet;

public interface StatSheetRepository extends JpaRepository<StatSheet, Integer> {
}
