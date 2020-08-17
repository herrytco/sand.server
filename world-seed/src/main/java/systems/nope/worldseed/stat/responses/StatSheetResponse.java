package systems.nope.worldseed.stat.responses;

import systems.nope.worldseed.stat.sheet.StatSheet;

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
