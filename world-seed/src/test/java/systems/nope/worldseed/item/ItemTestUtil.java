package systems.nope.worldseed.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.repository.item.ItemRepository;
import systems.nope.worldseed.service.ItemService;
import systems.nope.worldseed.world.WorldTestUtil;

import java.util.Optional;

@Service
public class ItemTestUtil {
    @Autowired
    private WorldTestUtil worldTestUtil;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    private Item ensuredItem;

    public Item getEnsuredItem() {
        return ensuredItem;
    }

    public Item ensureTestItemExists() {
        World testWorld = worldTestUtil.ensureTestWorldExists();
        return ensureTestItemExists(testWorld, ItemTestConstants.volatileItemName, ItemTestConstants.volatileItemDescription);
    }

    public Item ensureTestItemExists(World world, String name, String description) {
        Optional<Item> optionalItem = itemRepository.findByWorldAndName(world, name);

        if (optionalItem.isPresent()) {
            ensuredItem = optionalItem.get();
            return ensuredItem;
        }

        ensuredItem = itemService.add(world, name, description);
        return ensuredItem;
    }
}
