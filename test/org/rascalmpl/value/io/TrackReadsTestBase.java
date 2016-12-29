package org.rascalmpl.value.io;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.rascalmpl.value.io.binary.util.TrackLastRead;


public abstract class TrackReadsTestBase {
    public abstract TrackLastRead<Object> getLastReadWindow(int size);

    @Test
    public void getMostRecent() {
        Object a = new Object();
        TrackLastRead<Object> w = getLastReadWindow(5);
        w.read(a);
        assertSame(a, w.lookBack(0));
    }

    @Test
    public void getPrevious() {
        Object a = new Object();
        Object b = new Object();
        TrackLastRead<Object> w = getLastReadWindow(5);
        w.read(a);
        w.read(b);
        assertSame(a, w.lookBack(1));
    }
    @Test
    public void getPreviousAfterCircleing() {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        Object d = new Object();
        TrackLastRead<Object> w = getLastReadWindow(5);
        w.read(a);
        w.read(b);
        w.read(c);
        w.read(d);
        assertSame(c, w.lookBack(1));
    }
    
    @Test
    public void testLargeReads() {
        Object[] elements = new Object[10000];;
        for (int i = 0; i < elements.length; i++) {
            elements[i] = new Object();
        }
        final int windowSize = elements.length / 2;
        TrackLastRead<Object> r = getLastReadWindow(windowSize);
        for (int i = 0; i < elements.length; i++) {
            r.read(elements[i]);
            for (int j = 0 ; j <= Math.min(i, windowSize); j++) {
                assertSame("For " + j + "back after " + i + "reads", r.lookBack(j), elements[i - j]);
            }
        }
    }
}
