package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import systems.nope.worldseed.model.Document;
import systems.nope.worldseed.model.LastDocument;
import systems.nope.worldseed.model.World;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Document.Pk> {

    @Query(
            "select "
                    + "new systems.nope.worldseed.model.LastDocument(d.id.id, max(d.id.version)) "
                    + "from Document d "
                    + "where d.id.id = ?1 and d.world = ?2 "
                    + "group by d.id.id"
    )
    LastDocument getLatest(Integer id, World world);

    @Query(
            "select "
                    + "new systems.nope.worldseed.model.LastDocument(d.id.id, max(d.id.version)) "
                    + "from Document d "
                    + "where d.world = ?1 "
                    + "group by d.id.id"
    )
    List<LastDocument> getLatestForWorld(World world);

    @Query(
            "select "
                    + "new systems.nope.worldseed.model.LastDocument(d.id.id, d.id.version) "
                    + "from Document d "
                    + "where d.id.id = ?1 and d.world = ?2"
    )
    List<LastDocument> getVersionsVorDocument(Integer id, World world);
}
