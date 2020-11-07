package systems.nope.worldseed.dto;

import lombok.Data;
import systems.nope.worldseed.dto.stat.StatValueItemInstanceDto;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.item.StatValueItemInstance;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ItemDto {
    private Integer id;

    private String name;

    private Set<Integer> requiredStatSheets;

    private Set<Integer> statSheets;

    private String description;

    private Set<StatValueItemInstanceDto> statValueInstances;

    public static ItemDto fromItem(Item item) {
        ItemDto dto = new ItemDto();

        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setRequiredStatSheets(
                item.getRequiredStatSheets().stream()
                        .map(StatSheet::getId)
                        .collect(Collectors.toSet())
        );
        dto.setStatSheets(
                item.getStatSheets().stream()
                        .map(StatSheet::getId)
                        .collect(Collectors.toSet())
        );
        dto.setStatValueInstances(
                item.getStatValues().stream()
                        .map(StatValueItemInstanceDto::fromInstance)
                        .collect(Collectors.toSet())
        );
        dto.setDescription(
                item.getDescriptionDocument() != null ? item.getDescriptionDocument().getRichtext() : ""
        );

        return dto;
    }
}
