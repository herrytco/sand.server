package systems.nope.worldseed.util.data;

import systems.nope.worldseed.exception.DataMissmatchException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Spliterator;
import java.util.function.Consumer;

public class DataQueue<T> implements DataStructure<T> {

    private final LinkedList<T> data = new LinkedList<>();

    public void push(T value) {
        data.add(value);
    }

    public T pop() throws DataMissmatchException {
        if (data.size() == 0)
            throw new DataMissmatchException("Stack was emtpy!");

        return data.removeLast();
    }

    public int size() {
        return data.size();
    }

    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }

    @Override
    public void forEach(Consumer action) {
        data.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return data.spliterator();
    }
}
