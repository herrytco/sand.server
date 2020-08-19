package systems.nope.worldseed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
