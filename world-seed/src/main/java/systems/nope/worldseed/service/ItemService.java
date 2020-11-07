package systems.nope.worldseed.service;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.repository.item.ItemRepository;

import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item get(Integer itemId) {
        Optional<Item> itemOptional = find(itemId);

        if(itemOptional.isEmpty())
            throw new NotFoundException(itemId);

        return itemOptional.get();
    }

    public Optional<Item> find(Integer itemId) {
        return itemRepository.findById(itemId);
    }
}
