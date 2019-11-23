package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.WorldAssignment;

public interface WorldAssignmentRepository extends JpaRepository<WorldAssignment, WorldAssignment.Pk> {
}
