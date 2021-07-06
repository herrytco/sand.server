package systems.nope.worldseed.util;

import java.util.LinkedList;
import java.util.List;

public class StatContext {
    private final List<Symbol> symbols;

    public StatContext() {
        this.symbols = new LinkedList<>();
    }

    public StatContext(List<Symbol> symbols) {
        this.symbols = symbols;
    }

    public void addSymbol(Symbol symbol) {
        List<Symbol> toRemove = new LinkedList<>();
        for(Symbol s : symbols) {
            if(s.getSymbol().equals(symbol.getSymbol()))
                toRemove.add(s);
        }
        symbols.removeAll(toRemove);

        symbols.add(symbol);
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }
}
