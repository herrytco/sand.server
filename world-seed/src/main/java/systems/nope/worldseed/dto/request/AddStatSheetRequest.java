package systems.nope.worldseed.dto.request;

public class AddStatSheetRequest extends AddNamedResourceRequest {
    private Integer parentId;

    public AddStatSheetRequest() {
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
