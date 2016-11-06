package org.rascalmpl.value.io;

import org.rascalmpl.value.io.binary.util.OpenAddressingLastWritten;
import org.rascalmpl.value.io.binary.util.TrackLastWritten;

public class OpenAddressingLastWrittenBrokenHashTest extends TrackWritesTestBase {
    @Override
    public TrackLastWritten<Object> getWritesWindow(int size) {
        return new OpenAddressingLastWritten<Object>(size) {
            @Override
            protected boolean equals(Object a, Object b) {
                return a == b;
            }

            @Override
            protected int hash(Object obj) {
                return System.identityHashCode(obj) % Math.max(2, size / 2);
            }
        };
    }
}
