package systems.nope.worldseed.dto.request.stat;

import lombok.Data;

@Data
public class UpdateConstantStatValueInstanceRequest {
    Integer valueNew;
    Integer modifierNew;

    public UpdateConstantStatValueInstanceRequest(Integer valueNew, Integer modifierNew) {
        this.valueNew = valueNew;
        this.modifierNew = modifierNew;
    }

    @Override
    public String toString() {
        return "UpdateConstantStatValueIntanceRequest{" +
                "valueNew=" + valueNew +
                ", modifierNew=" + modifierNew +
                '}';
    }
}
