package systems.nope.worldseed.dto.response;

import systems.nope.worldseed.model.stat.StatSheet;

public class StatSheetResponse {
    private StatSheet sheet;

    public StatSheetResponse() {
    }

    public StatSheetResponse(StatSheet sheet) {
        this.sheet = sheet;
    }

    public StatSheet getSheet() {
        return sheet;
    }

    public void setSheet(StatSheet sheet) {
        this.sheet = sheet;
    }
}
