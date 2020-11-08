package systems.nope.worldseed.dto;

import lombok.Data;
import systems.nope.worldseed.dto.stat.item.StatValueItemInstanceDto;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.stat.StatSheet;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ItemDto {
    private Integer id;

    private String name;

    private Set<Integer> statSheets;

    private String description;

    private Set<StatValueItemInstanceDto> statValueInstances;

    private Set<ActionDto> actions;

    public static ItemDto fromItem(Item item) {
        ItemDto dto = new ItemDto();

        dto.setId(item.getId());
        dto.setName(item.getName());
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

        dto.setActions(
                item.getActions().stream()
                        .map(ActionDto::fromAction)
                        .collect(Collectors.toSet())
        );

        return dto;
    }
}
