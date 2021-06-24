package systems.nope.worldseed.dto.request.action;

import systems.nope.worldseed.dto.request.AddNamedResourceRequest;

public class UpdateActionRequest extends AddNamedResourceRequest {
    private String description;
    private String formula;
    private String invokeMessage;

    public UpdateActionRequest() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getInvokeMessage() {
        return invokeMessage;
    }

    public void setInvokeMessage(String invokeMessage) {
        this.invokeMessage = invokeMessage;
    }
}
