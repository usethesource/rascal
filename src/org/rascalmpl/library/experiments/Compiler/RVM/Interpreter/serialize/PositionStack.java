package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EmptyStackException;

public class PositionStack<V, K extends IteratorKind> {

    private K[] kinds;
    private V[] leafs;
    private boolean[] beginnings;
    private int mark = -1;
    private final Class<V> vclass;
    private final Class<K> kclass;
    
    public PositionStack(Class<V> vclass, Class<K> kclass) {
        this(vclass, kclass, 1024);
    }
    
	@SuppressWarnings("unchecked")
    public PositionStack(Class<V> vclass, Class<K> kclass, int initialSize) {
        kinds = (K[]) Array.newInstance(kclass, initialSize);
        leafs = (V[])  Array.newInstance(vclass,initialSize);
        beginnings = new boolean[initialSize];
        this.vclass = vclass;
        this.kclass = kclass;
    }

    public K currentKind() {
        assert mark >= 0;
        return kinds[mark];
    }
    public V currentIValue() {
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
    
    public void push(V leaf, K kind, boolean beginning) {
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
            V[] newLeafs = (V[])  Array.newInstance(vclass, newSize);
            System.arraycopy(leafs, 0, newLeafs, 0, mark + 1);
            leafs = newLeafs;
            K[] newKinds = (K[]) Array.newInstance(kclass, newSize);
            System.arraycopy(kinds, 0, newKinds, 0, mark + 1);
            kinds = newKinds;
            boolean[] newBeginnings = new boolean[newSize];
            System.arraycopy(beginnings, 0, newBeginnings, 0, mark + 1);
            beginnings = newBeginnings;
        }
    }
}
