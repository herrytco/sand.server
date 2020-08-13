package systems.nope.worldseed.stat.requests;

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
}
