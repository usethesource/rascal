package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util;

public class LinearCircularLookupWindow<T> implements TrackLastRead<T>, TrackLastWritten<T> {
    private final T[] data;
    private long written;

    @SuppressWarnings("unchecked")
    public LinearCircularLookupWindow(int size) {
        data = (T[]) new Object[size];
        written = 0;
    }
    
    @Override
    public int howLongAgo(T obj) {
        long stop = Math.max(0, written - data.length);
        for (long ix = written -1; ix >= stop; ix--) {
            if (data[translate(ix)] == obj) {
                return ((int)(written - ix) - 1);
            }
        }
        return -1;
    }
    
    private int translate(long index) {
        return (int) (index % data.length);
    }
    
    
    @Override
    public T lookBack(int offset) {
        assert offset + 1 <= written;
        return data[translate(written - (offset + 1))];
        
    }
    
    @Override
    public void read(T obj) {
        add(obj);
    }
    @Override
    public void write(T obj) {
        add(obj);
    }
    protected T add(T obj) {
        int newIndex = translate(written++);
        T old = data[newIndex];
        data[newIndex] = obj;
        return old;
    }

}
