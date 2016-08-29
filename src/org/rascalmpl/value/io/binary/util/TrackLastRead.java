package org.rascalmpl.value.io.binary.util;

public interface TrackLastRead<T> {
    void read(T obj);
    T lookBack(int elements);
}
