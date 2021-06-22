package systems.nope.worldseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.Action;
import systems.nope.worldseed.model.item.Item;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Integer> {

    List<Action> findAllByItem(Item item);

}
