package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapLastWritten<T> implements TrackLastWritten<T> {
    public static class IdentityWrapper<T> {
        private final T wrapped;
        private final int hashCode;
        public IdentityWrapper(T wrapped) {
            this.wrapped = wrapped;
            this.hashCode = System.identityHashCode(wrapped);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            return ((IdentityWrapper<T>)obj).wrapped == wrapped;
        }
    }
    private final Map<IdentityWrapper<T>, Long> lookupData;
    private long written;

    @SuppressWarnings("serial")
    public MapLastWritten(final int size) {
        lookupData = new LinkedHashMap<IdentityWrapper<T>, Long>(size + 1, 1.0f, false) {
            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry<IdentityWrapper<T>, Long> eldest) {
                return super.size() > size;
            }
        };
        written = 0;
    }
    
    @Override
    public int howLongAgo(T obj) {
        Long writtenAt = lookupData.get(new IdentityWrapper<>(obj));
        if (writtenAt != null) {
            return (int) ((written - writtenAt) - 1);
        }
        return -1;
    }
    
    @Override
    public void write(T obj) {
        lookupData.put(new IdentityWrapper<>(obj), written++);
    }


}
