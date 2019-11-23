package systems.nope.sand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.sand.model.WorldAssignment;

public interface WorldAssignmentRepository extends JpaRepository<WorldAssignment, WorldAssignment.Pk> {
}
