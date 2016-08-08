package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.EmptyStackException;

public class PositionStack<Item, Kind extends IteratorKind> {

    private Kind[] kinds;
    private Item[] items;
    private boolean[] beginnings;
    private int mark = -1;
    
    public PositionStack(Class<Item> iclass, Class<Kind> kclass) {
        this(iclass, kclass, 1024);
    }
    
	@SuppressWarnings("unchecked")
    public PositionStack(Class<Item> iclass, Class<Kind> kclass, int initialSize) {
        kinds = (Kind[]) Array.newInstance(kclass, initialSize);
        items = (Item[]) Array.newInstance(iclass,initialSize);
        beginnings = new boolean[initialSize];
    }

    public Kind currentKind() {
        assert mark >= 0;
        return kinds[mark];
    }
    public Item currentItem() {
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
    
    public void push(Item item, Kind kind, boolean beginning) {
        grow(mark + 2);
        mark++;
        items[mark] = item;
        kinds[mark] = kind;
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
            kinds = Arrays.copyOf(kinds, newSize);
            beginnings = Arrays.copyOf(beginnings, newSize);
        }
    }
}
