package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;


import java.io.IOException;

public abstract class PrePostIterator<V, K extends IteratorKind>  {
    protected PositionStack<V, K> stack;
    protected K kind;
    protected V value;
    protected boolean beginning;

    public PrePostIterator(Class<V> vclass, Class<K> kclass, int stackSize) throws IOException {
        stack = new PositionStack<V, K>(vclass, kclass, stackSize);
        beginning = false; // start out at fake end
    }

    public boolean hasNext() {
        return !stack.isEmpty() || (beginning && kind != null && kind.isCompound());
    }

    abstract public K next() throws IOException ;
    
    public K skipValue() {
        assert beginning;
        beginning = false;
        return kind;
    }

    public boolean atBeginning() {
        return beginning;
    }

  
    public K currentKind() {
        return kind;
    }

    public V getValue() {
        return value;
    }

}

