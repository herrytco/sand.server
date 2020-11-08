package systems.nope.worldseed.dto;

import lombok.Data;
import systems.nope.worldseed.model.Action;
import systems.nope.worldseed.model.stat.StatSheet;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ActionDto {
    private Integer id;

    private String name;

    private String description;

    private String formula;

    private Set<Integer> requiredStatSheets;

    private String invokeMessageTemplate;

    public static ActionDto fromAction(Action action) {
        ActionDto dto = new ActionDto();

        dto.setId(action.getId());
        dto.setName(action.getName());
        dto.setDescription(action.getDescription());
        dto.setFormula(action.getFormula());
        dto.setInvokeMessageTemplate(action.getInvokeMessage());

        dto.setRequiredStatSheets(
                action.getRequiredStatSheets()
                        .stream().map(StatSheet::getId)
                        .collect(Collectors.toSet())
        );

        return dto;
    }
}
