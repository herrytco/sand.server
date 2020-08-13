package systems.nope.worldseed.stat.requests;

public class AddConstantStatRequest extends AddStatRequest {
    private Integer initialValue;

    public AddConstantStatRequest() {
    }

    public AddConstantStatRequest(String name, String nameShort, String unit, Integer initialValue) {
        super(name, nameShort, unit);
        this.initialValue = initialValue;
    }

    public Integer getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Integer initialValue) {
        this.initialValue = initialValue;
    }
}
