package systems.nope.worldseed.dto.response;

import systems.nope.worldseed.model.stat.value.StatValue;

public class AddStatResponse {
    private StatValue stat;

    public AddStatResponse() {
    }

    public AddStatResponse(StatValue stat) {
        this.stat = stat;
    }

    public StatValue getStat() {
        return stat;
    }

    public void setStat(StatValue stat) {
        this.stat = stat;
    }
}
