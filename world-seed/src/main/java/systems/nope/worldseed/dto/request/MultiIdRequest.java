package systems.nope.worldseed.dto.request;

import java.util.List;

public class MultiIdRequest {
    private List<Integer> ids;

    public List<Integer> getIds() {
        return ids;
    }

    public MultiIdRequest() {
    }

    public MultiIdRequest(List<Integer> ids) {
        this.ids = ids;
    }
}
