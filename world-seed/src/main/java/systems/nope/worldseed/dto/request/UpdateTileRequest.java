package systems.nope.worldseed.dto.request;

import lombok.Data;

@Data
public class UpdateTileRequest {
    private String name;

    private String descriptionShort;

    private String descriptionLong;

    private String textColor;

    public UpdateTileRequest(String name, String descriptionShort, String descriptionLong, String textColor) {
        this.name = name;
        this.descriptionShort = descriptionShort;
        this.descriptionLong = descriptionLong;
        this.textColor = textColor;
    }
}
