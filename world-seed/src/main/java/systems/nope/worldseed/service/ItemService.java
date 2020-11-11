package systems.nope.worldseed.service;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.exception.AlreadyExistingException;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.Document;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstance;
import systems.nope.worldseed.model.stat.value.StatValue;
import systems.nope.worldseed.repository.item.ItemRepository;

import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final DocumentService documentService;

    public ItemService(ItemRepository itemRepository, DocumentService documentService) {
        this.itemRepository = itemRepository;
        this.documentService = documentService;
    }

    public void delete(Item item) {
        if (item.getDescriptionDocument() != null) {
            documentService.delete(item.getDescriptionDocument());
            item.setDescriptionDocument(null);
        }

        itemRepository.delete(item);
    }

    public Item get(Integer itemId) {
        Optional<Item> itemOptional = find(itemId);

        if (itemOptional.isEmpty())
            throw new NotFoundException(itemId);

        return itemOptional.get();
    }

    public Optional<Item> find(Integer itemId) {
        return itemRepository.findById(itemId);
    }

    public Item add(World world, String name, String description) {
        Optional<Item> referenceItem = itemRepository.findByWorldAndName(world, name);

        if (referenceItem.isPresent())
            throw new AlreadyExistingException(name);

        Document descriptionDocument = documentService.add(world, description);

        Item itemNew = new Item();
        itemNew.setName(name);
        itemNew.setWorld(world);
        itemNew.setDescriptionDocument(descriptionDocument);
        itemRepository.save(itemNew);

        return itemNew;
    }

    /**
     * Adds the stat sheet including its parents to the person. All StatValues get added as well.
     *
     * @param item  - item the stat sheet gets assigned to
     * @param sheet - target stat sheet
     */
    public void addStatSheetToItem(Item item, StatSheet sheet) {
        if (sheet.getParent() != null)
            addStatSheetToItem(item, sheet.getParent());

        if (item.getStatSheets().contains(sheet)) {
            item.getStatSheets().remove(sheet);
            itemRepository.save(item);
        }

        for (StatValue value : sheet.getStatValues()) {
            StatValuePersonInstance instance = null;

            boolean duplicate = false;

//            for (StatValuePersonInstance i : person.getStatValues())
//                if (i.getStatValue().getId() == value.getId()) {
//                    duplicate = true;
//                    break;
//                }
//
//            if (duplicate)
//                continue;
//
//            if (value instanceof StatValueConstant) {
//                instance = new StatValuePersonInstanceConstant(person.getWorld(), value, person, ((StatValueConstant) value).getInitalValue());
//                statValueInstanceConstantRepository.save((StatValuePersonInstanceConstant) instance);
//            } else if (value instanceof StatValueSynthesized) {
//                instance = new StatValuePersonInstanceSynthesized(person.getWorld(), value, person);
//                statValueInstanceSynthesizedRepository.save((StatValuePersonInstanceSynthesized) instance);
//            }
//
//            if (instance != null)
//                person.getStatValues().add(instance);
        }

        item.getStatSheets().add(sheet);
        itemRepository.save(item);
    }
}
