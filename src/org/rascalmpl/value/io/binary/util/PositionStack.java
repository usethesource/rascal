package org.rascalmpl.value.io.binary.util;

import java.util.Arrays;
import java.util.EmptyStackException;

import org.rascalmpl.value.IValue;

public class PositionStack {
    private IValue[] items;
    private boolean[] beginnings;
    private int mark = -1;
    
    public PositionStack() {
        this(1024);
    }
    
    public PositionStack(int initialSize) {
        items = new IValue[initialSize];
        beginnings = new boolean[initialSize];
    }

    public IValue currentIValue() {
        assert mark >= 0;
        return items[mark];
    }
    public boolean currentBeginning() {
        assert mark >= 0;
        return beginnings[mark];
    }
    
    public boolean isEmpty() {
        return mark == -1;
    }
    
    public void push(IValue item, boolean beginning) {
        grow(mark + 2);
        mark++;
        items[mark] = item;
        beginnings[mark] = beginning;
    }
    
    public void pop() {
        if (mark > -1) {
            mark--;
        }
        else {
            throw new EmptyStackException();
        }
    }

	private void grow(int desiredSize) {
        if (desiredSize > items.length) {
            int newSize = (int)Math.min(items.length * 2L, 0x7FFFFFF7); // max array size used by array list
            assert desiredSize <= newSize;
            items = Arrays.copyOf(items, newSize);
            beginnings = Arrays.copyOf(beginnings, newSize);
        }
    }
}
