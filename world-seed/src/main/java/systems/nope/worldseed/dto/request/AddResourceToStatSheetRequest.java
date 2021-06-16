package systems.nope.worldseed.dto.request;

public class AddResourceToStatSheetRequest {
    private int targetId;
    private int statsheetId;

    public AddResourceToStatSheetRequest() {
    }

    public AddResourceToStatSheetRequest(int targetId, int statsheetId) {
        this.targetId = targetId;
        this.statsheetId = statsheetId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getStatsheetId() {
        return statsheetId;
    }

    public void setStatsheetId(int statsheetId) {
        this.statsheetId = statsheetId;
    }

    @Override
    public String toString() {
        return "AddPersonStatsheetRequest{" +
                "personId=" + targetId +
                ", statsheetId=" + statsheetId +
                '}';
    }
}
