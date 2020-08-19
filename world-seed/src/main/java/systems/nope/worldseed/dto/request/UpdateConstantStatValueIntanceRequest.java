package systems.nope.worldseed.dto.request;

public class UpdateConstantStatValueIntanceRequest {
    Integer valueNew;

    public UpdateConstantStatValueIntanceRequest() {
    }

    public UpdateConstantStatValueIntanceRequest(Integer valueNew) {
        this.valueNew = valueNew;
    }

    public Integer getValueNew() {
        return valueNew;
    }

    public void setValueNew(Integer valueNew) {
        this.valueNew = valueNew;
    }

    @Override
    public String toString() {
        return "UpdateConstantStatValueIntanceRequest{" +
                "valueNew=" + valueNew +
                '}';
    }
}
