package systems.nope.worldseed.dto;

import lombok.Data;
import systems.nope.worldseed.model.Action;

@Data
public class InvokeActionDto {
    private ActionDto action;

    private Integer value;

    private String invokeMessage;

    public static InvokeActionDto fromAction(Action action, Integer value, String invokeMessage) {
        InvokeActionDto dto = new InvokeActionDto();

        dto.setAction(ActionDto.fromAction(action));
        dto.setValue(value);
        dto.setInvokeMessage(invokeMessage);

        return dto;
    }
}
