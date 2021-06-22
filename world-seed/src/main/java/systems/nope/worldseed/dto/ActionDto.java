package systems.nope.worldseed.dto;

import lombok.Data;
import systems.nope.worldseed.model.Action;
import systems.nope.worldseed.model.stat.StatSheet;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ActionDto {

    private Integer id;
    private Integer worldId;
    private Integer itemId;

    private String name;
    private String description;
    private String formula;
    private Set<Integer> requiredStatSheets;
    private String invokeMessageTemplate;

    public static ActionDto fromAction(Action action) {
        ActionDto dto = new ActionDto();

        dto.setId(action.getId());
        dto.setWorldId(action.getItem().getWorld().getId());

        dto.setName(action.getName());
        dto.setDescription(action.getDescription());
        dto.setFormula(action.getFormula());
        dto.setInvokeMessageTemplate(action.getInvokeMessage());
        dto.setItemId(action.getItem().getId());

        if (dto.getRequiredStatSheets() != null)
            dto.setRequiredStatSheets(
                    action.getRequiredStatSheets()
                            .stream().map(StatSheet::getId)
                            .collect(Collectors.toSet())
            );
        else
            dto.setRequiredStatSheets(new HashSet<>());

        return dto;
    }
}
