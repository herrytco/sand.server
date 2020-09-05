package systems.nope.worldseed.dto.request;

import lombok.Data;

@Data
public class UpdateTileRequest {
    private String name;

    private String descriptionShort;

    private String descriptionLong;

    private String textColor;
}
