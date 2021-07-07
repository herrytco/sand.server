package systems.nope.worldseed.dto.request.stat;

import systems.nope.worldseed.dto.request.AddNamedResourceRequest;

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
