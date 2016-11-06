package org.rascalmpl.value.io;

import org.rascalmpl.value.io.binary.util.OpenAddressingLastWritten;
import org.rascalmpl.value.io.binary.util.TrackLastWritten;

public class OpenAddressingLastWrittenReferenceTest extends TrackWritesTestBase {
    @Override
    public TrackLastWritten<Object> getWritesWindow(int size) {
        return OpenAddressingLastWritten.referenceEquality(size);
    }
}
