package systems.nope.worldseed.dto.request;

public class AddTilesetRequest extends AddNamedResourceRequest {
    private Integer tileWidth;

    private Integer tileHeight;

    public AddTilesetRequest() {
    }

    public AddTilesetRequest(String name, Integer tileWidth, Integer tileHeight) {
        super(name);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public Integer getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(Integer tileWidth) {
        this.tileWidth = tileWidth;
    }

    public Integer getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(Integer tileHeight) {
        this.tileHeight = tileHeight;
    }
}
