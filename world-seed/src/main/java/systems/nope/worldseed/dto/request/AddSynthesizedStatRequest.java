package systems.nope.worldseed.dto.request;

public class AddSynthesizedStatRequest extends AddStatRequest {
    private String formula;
    private Boolean isResource;

    public AddSynthesizedStatRequest() {
    }

    public AddSynthesizedStatRequest(String name, String nameShort, String unit, String formula, Boolean isResource) {
        super(name, nameShort, unit);
        this.formula = formula;
        this.isResource = isResource;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Boolean getResource() {
        return isResource;
    }

    public void setResource(Boolean resource) {
        isResource = resource;
    }
}
