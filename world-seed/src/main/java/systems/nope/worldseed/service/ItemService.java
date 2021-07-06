package systems.nope.worldseed.service;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.exception.AlreadyExistingException;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.Document;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.StatValueInstance;
import systems.nope.worldseed.model.stat.value.StatValue;
import systems.nope.worldseed.model.stat.value.StatValueConstant;
import systems.nope.worldseed.model.stat.value.StatValueSynthesized;
import systems.nope.worldseed.repository.item.ItemRepository;

import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final DocumentService documentService;
    private final StatValueInstanceService statValueInstanceService;

    public ItemService(ItemRepository itemRepository, DocumentService documentService,
                       StatValueInstanceService statValueInstanceService) {
        this.itemRepository = itemRepository;
        this.documentService = documentService;
        this.statValueInstanceService = statValueInstanceService;
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
        // recursively add all parent stat sheets
        if (sheet.getParent() != null)
            addStatSheetToItem(item, sheet.getParent());

        if (item.getStatSheets().contains(sheet)) {
            item.getStatSheets().remove(sheet);
            itemRepository.save(item);
        }

        for (StatValue value : sheet.getStatValues()) {
            StatValueInstance instance = null;

            boolean duplicate = false;

            for (StatValueInstance i : item.getStatValueInstances())
                if (i.getStatValue().getId() == value.getId()) {
                    duplicate = true;
                    break;
                }

            if (duplicate)
                continue;

            if (value instanceof StatValueConstant)
                instance = statValueInstanceService.add(
                        item.getWorld(),
                        value,
                        ((StatValueConstant) value).getInitalValue()
                );
            else if (value instanceof StatValueSynthesized)
                instance = statValueInstanceService.add(
                        item.getWorld(),
                        value
                );

            item.getStatValueInstances().add(instance);
            itemRepository.save(item);
        }

        item.getStatSheets().add(sheet);
        itemRepository.save(item);
    }
}
