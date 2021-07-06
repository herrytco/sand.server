package systems.nope.worldseed.util;

import systems.nope.worldseed.exception.DataMissmatchException;

import java.util.LinkedList;

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
}
