package systems.nope.worldseed.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.worldseed.model.Item;
import systems.nope.worldseed.repository.ItemRepository;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    private final ItemRepository itemRepository;

    @GetMapping
    private List<Item> all() {
        return itemRepository.findAll();
    }
}
