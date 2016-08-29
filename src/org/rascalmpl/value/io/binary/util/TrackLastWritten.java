package org.rascalmpl.value.io.binary.util;

public interface TrackLastWritten<T> {
    void write(T obj);
    int howLongAgo(T obj);
}
