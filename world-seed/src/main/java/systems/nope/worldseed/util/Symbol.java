package systems.nope.worldseed.util;

import lombok.Data;

@Data
public class Symbol {
    private final String symbol;
    private final Integer value;

    public Symbol(String symbol, Integer value) {
        this.symbol = symbol;
        this.value = value;
    }
}
