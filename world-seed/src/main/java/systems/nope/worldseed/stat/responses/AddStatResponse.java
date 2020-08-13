package systems.nope.worldseed.stat.responses;

import systems.nope.worldseed.stat.model.StatValue;

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
