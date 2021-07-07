package systems.nope.worldseed.dto.request.stat;

public class AddConstantStatRequest extends AddStatRequest {
    private Integer initialValue;
    private Boolean isResource;

    public AddConstantStatRequest() {
    }

    public AddConstantStatRequest(String name, String nameShort, String unit, Integer initialValue, Boolean isResource) {
        super(name, nameShort, unit);
        this.initialValue = initialValue;
        this.isResource = isResource;
    }

    public Integer getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Integer initialValue) {
        this.initialValue = initialValue;
    }

    public Boolean getResource() {
        return isResource;
    }

    public void setResource(Boolean resource) {
        isResource = resource;
    }
}
