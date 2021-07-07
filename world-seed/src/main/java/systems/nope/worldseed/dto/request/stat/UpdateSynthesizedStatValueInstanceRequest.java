package systems.nope.worldseed.dto.request.stat;

import lombok.Data;

@Data
public class UpdateSynthesizedStatValueInstanceRequest {
    Integer modifierNew;

    public UpdateSynthesizedStatValueInstanceRequest(Integer modifierNew) {
        this.modifierNew = modifierNew;
    }
}
