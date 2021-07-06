package systems.nope.worldseed.util;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.util.data.DataStack;
import systems.nope.worldseed.util.data.DataStructure;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatUtils {

    public String enrichExpression(String expression, Collection<Symbol> symbols) {
        expression = padExpression(expression);

        for (Symbol symbol : symbols)
            expression = expression.replaceAll(
                    padExpression(symbol.getSymbol()),
                    padExpression(symbol.getValue().toString())
            );

        return expression.trim();
    }

    private String padExpression(String expression) {
        return " " + expression.trim() + " ";
    }

    public List<DataStructure<StatSheet>> constructSheetForest(List<StatSheet> sheets) {
        List<DataStructure<StatSheet>> result = new LinkedList<>();

        // 1. find leaf nodes
        List<StatSheet> leaves = findLeafNodes(sheets);

        // 2. complete the tree
        for (StatSheet leaf : leaves)
            result.add(completeLeafToTree(leaf));

        return result;
    }

    public DataStructure<StatSheet> completeLeafToTree(StatSheet sheet) {
        DataStructure<StatSheet> sheetStructure = new DataStack<>();
        StatSheet k = sheet;

        do {
            sheetStructure.push(k);
            k = k.getParent();
        } while (k != null);

        return sheetStructure;
    }

    public List<StatSheet> findLeafNodes(List<StatSheet> sheets) {
        List<StatSheet> result = new LinkedList<>();

        for (StatSheet testee : sheets) {
            List<StatSheet> descendants = sheets.stream()
                    .filter(statSheet -> statSheet.getParent() != null && statSheet.getParent().getId() == testee.getId())
                    .collect(Collectors.toList());

            if (descendants.size() == 0)
                result.add(testee);
        }

        return result;
    }

}
