package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.util.EmptyStackException;

import org.rascalmpl.value.IValue;

public class PositionStack {

    private Kind[] kinds;
    private IValue[] leafs;
    private boolean[] beginnings;
    private int mark = -1;
    
    public PositionStack() {
        this(1024);
    }
    
	public PositionStack(int initialSize) {
        kinds = (Kind[]) new Kind[initialSize];
        leafs = (IValue[]) new IValue[initialSize];
        beginnings = new boolean[initialSize];
    }

    public Kind currentKind() {
        assert mark >= 0;
        return kinds[mark];
    }
    public IValue currentIValue() {
        assert mark >= 0;
        return leafs[mark];
    }
    public boolean currentBeginning() {
        assert mark >= 0;
        return beginnings[mark];
    }
    
    public boolean isEmpty() {
        return mark == -1;
    }
    
    public void push(IValue leaf, Kind kind, boolean beginning) {
        grow(mark + 2);
        mark++;
        leafs[mark] = leaf;
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
        if (desiredSize > leafs.length) {
            int newSize = (int)Math.min(leafs.length * 2L, 0x7FFFFFF7); // max array size used by array list
            assert desiredSize <= newSize;
            IValue[] newLeafs = new IValue[newSize];
            System.arraycopy(leafs, 0, newLeafs, 0, mark + 1);
            leafs = newLeafs;
            Kind[] newKinds = new Kind[newSize];
            System.arraycopy(kinds, 0, newKinds, 0, mark + 1);
            kinds = newKinds;
            boolean[] newBeginnings = new boolean[newSize];
            System.arraycopy(beginnings, 0, newBeginnings, 0, mark + 1);
            beginnings = newBeginnings;
        }
    }
}
