package systems.nope.worldseed.dto;

import lombok.Data;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.stat.StatSheet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ItemDto {
    private Integer id;

    private String name;

    private Set<Integer> requiredStatSheets;

    private String description;

    public static ItemDto fromItem(Item item) {
        ItemDto dto = new ItemDto();

        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setRequiredStatSheets(
                item.getRequiredStatSheets().stream()
                        .map(StatSheet::getId)
                        .collect(Collectors.toSet())
        );

        dto.setDescription(
                item.getDescriptionDocument() != null ? item.getDescriptionDocument().getRichtext() : ""
        );

        return dto;
    }
}
