package systems.nope.worldseed.dto.request;

public class AddStatRequest extends AddNamedResourceRequest {
    private String nameShort;
    private String unit;

    public AddStatRequest() {
    }

    public AddStatRequest(String name, String nameShort, String unit) {
        super(name);
        this.nameShort = nameShort;
        this.unit = unit;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
