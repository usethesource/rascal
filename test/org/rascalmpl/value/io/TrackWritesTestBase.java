package org.rascalmpl.value.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.rascalmpl.value.io.binary.util.TrackLastWritten;

public abstract class TrackWritesTestBase {

    public abstract TrackLastWritten<Object> getWritesWindow(int size);

    @Test
    public void canFindFirstObject() {
        Object a = new Object();
        TrackLastWritten<Object> w = getWritesWindow(5);
        w.write(a);
        assertEquals(0, w.howLongAgo(a));
    }

    @Test
    public void canFindOldObject() {
        Object a = new Object();
        Object b = new Object();
        TrackLastWritten<Object> w = getWritesWindow(5);
        w.write(a);
        w.write(b);
        assertEquals(1, w.howLongAgo(a));
    }
    
    @Test
    public void canFindObjectAtEdge() {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        TrackLastWritten<Object> w = getWritesWindow(5);
        w.write(a);
        w.write(b);
        w.write(c);
        assertEquals(2, w.howLongAgo(a));
    }

    @Test
    public void dropsObject() {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        Object d = new Object();
        TrackLastWritten<Object> w = getWritesWindow(3);
        w.write(a);
        w.write(b);
        w.write(c);
        w.write(d);
        assertEquals(-1, w.howLongAgo(a));
        assertEquals(2, w.howLongAgo(b));
        assertEquals(1, w.howLongAgo(c));
        assertEquals(0, w.howLongAgo(d));
    }
    
    @Test
    public void dropsMany() {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        Object d = new Object();
        TrackLastWritten<Object> w = getWritesWindow(3);
        for (int i =0; i < 10000; i++) {
            w.write(new Object());
        }
        w.write(a);
        w.write(b);
        w.write(c);
        w.write(d);
        assertEquals(-1, w.howLongAgo(a));
        assertEquals(2, w.howLongAgo(b));
        assertEquals(1, w.howLongAgo(c));
        assertEquals(0, w.howLongAgo(d));
    }
    
    @Test
    public void randomTest() {
        Object[] elements = new Object[10000];;
        for (int i = 0; i < elements.length; i++) {
            elements[i] = new Object();
        }
        final int windowSize = elements.length / 2;
        TrackLastWritten<Object> w = getWritesWindow(windowSize);
        for (int i = 0; i < elements.length; i++) {
            w.write(elements[i]);
            for (int j = 0; j < Math.min(windowSize, i); j++) {
                assertEquals("Looking back: "+ j + " after " + i + "written", j, w.howLongAgo(elements[i - j]));
            }
        }
    }
}
