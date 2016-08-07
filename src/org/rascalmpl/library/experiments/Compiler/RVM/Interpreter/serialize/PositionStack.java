package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.lang.reflect.Array;
import java.util.EmptyStackException;

public class PositionStack<Item, Kind extends IteratorKind> {

    private Kind[] kinds;
    private Item[] items;
    private boolean[] beginnings;
    private int mark = -1;
    private final Class<Item> iclass;
    private final Class<Kind> kclass;
    
    public PositionStack(Class<Item> iclass, Class<Kind> kclass) {
        this(iclass, kclass, 1024);
    }
    
	@SuppressWarnings("unchecked")
    public PositionStack(Class<Item> iclass, Class<Kind> kclass, int initialSize) {
        kinds = (Kind[]) Array.newInstance(kclass, initialSize);
        items = (Item[])  Array.newInstance(iclass,initialSize);
        beginnings = new boolean[initialSize];
        this.iclass = iclass;
        this.kclass = kclass;
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

    @SuppressWarnings("unchecked")
	private void grow(int desiredSize) {
        if (desiredSize > items.length) {
            int newSize = (int)Math.min(items.length * 2L, 0x7FFFFFF7); // max array size used by array list
            assert desiredSize <= newSize;
            Item[] newItems = (Item[])  Array.newInstance(iclass, newSize);
            System.arraycopy(items, 0, newItems, 0, mark + 1);
            items = newItems;
            Kind[] newKinds = (Kind[]) Array.newInstance(kclass, newSize);
            System.arraycopy(kinds, 0, newKinds, 0, mark + 1);
            kinds = newKinds;
            boolean[] newBeginnings = new boolean[newSize];
            System.arraycopy(beginnings, 0, newBeginnings, 0, mark + 1);
            beginnings = newBeginnings;
        }
    }
}
