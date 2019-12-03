package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
}
