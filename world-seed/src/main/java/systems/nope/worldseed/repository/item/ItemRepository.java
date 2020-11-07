package systems.nope.worldseed.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.item.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByWorld(World world);
}
