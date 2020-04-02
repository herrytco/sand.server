package systems.nope.worldseed.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    @Query("select max(d.id.id) from Document d")
    Integer lastId();

}
