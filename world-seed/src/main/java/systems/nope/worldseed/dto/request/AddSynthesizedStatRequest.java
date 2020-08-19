package systems.nope.worldseed.dto.request;

public class AddSynthesizedStatRequest extends AddStatRequest {
    private String formula;

    public AddSynthesizedStatRequest() {
    }

    public AddSynthesizedStatRequest(String name, String nameShort, String unit, String formula) {
        super(name, nameShort, unit);
        this.formula = formula;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
