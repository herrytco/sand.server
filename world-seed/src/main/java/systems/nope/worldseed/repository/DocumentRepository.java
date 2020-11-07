package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import systems.nope.worldseed.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    @Query("select max(d.i) from Document d")
    Integer lastId();

}
