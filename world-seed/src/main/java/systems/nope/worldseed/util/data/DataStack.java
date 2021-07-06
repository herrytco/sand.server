package systems.nope.worldseed.util;

import systems.nope.worldseed.exception.DataMissmatchException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Spliterator;
import java.util.function.Consumer;

public class DataStack<T> implements DataStructure<T> {

    private final LinkedList<T> data = new LinkedList<>();

    public void push(T value) {
        data.add(0, value);
    }

    public T pop() throws DataMissmatchException {
        if (data.size() == 0)
            throw new DataMissmatchException("Stack was emtpy!");

        return data.removeFirst();
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
        for(T t : this)
            action.accept(t);
    }

    @Override
    public Spliterator<T> spliterator() {
        return DataStructure.super.spliterator();
    }
}
