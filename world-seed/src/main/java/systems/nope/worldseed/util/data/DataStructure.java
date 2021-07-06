package systems.nope.worldseed.util.data;

import systems.nope.worldseed.exception.DataMissmatchException;

public interface DataStructure<T> extends Iterable<T> {
    void push(T value);

    T pop() throws DataMissmatchException;

    int size();
}
