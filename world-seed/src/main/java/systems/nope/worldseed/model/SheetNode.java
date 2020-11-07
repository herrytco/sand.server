package systems.nope.worldseed.model;

import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.model.stat.instance.person.StatValuePersonInstance;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class SheetNode {
    private final StatSheet sheet;
    private final SheetNode parent;
    private final List<SheetNode> children = new LinkedList<>();

    public SheetNode(StatSheet sheet) {
        this.sheet = sheet;
        parent = null;
    }

    public SheetNode(StatSheet sheet, SheetNode parent) {
        this.sheet = sheet;
        this.parent = parent;
    }

    public void addChild(StatSheet childNew) {
        children.add(new SheetNode(childNew, this));
    }

    public Optional<Stack<SheetNode>> findStackForStatValueInstance(StatValuePersonInstance instance) {
        StatSheet scope = instance.getStatValue().getSheet();

        Optional<SheetNode> containingNode = findSheetNodeContainingSheet(scope);

        if (containingNode.isEmpty())
            return Optional.empty();

        Stack<SheetNode> result = new Stack<>();

        SheetNode k = containingNode.get();

        do {
            result.add(k);
            k = k.getParent();
        } while (k != null);

        return Optional.of(result);
    }

    public Optional<SheetNode> findSheetNodeContainingSheet(StatSheet query) {
        if (query == null)
            return Optional.empty();

        if (query == sheet)
            return Optional.of(this);

        for (SheetNode child : children) {
            if (child.findSheetNodeContainingSheet(query).isPresent())
                return Optional.of(child);
        }

        return Optional.empty();
    }

    public SheetNode getParent() {
        return parent;
    }

    public StatSheet getSheet() {
        return sheet;
    }
}
