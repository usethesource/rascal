package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;


import java.io.IOException;

public abstract class PrePostIterator<Item, Kind extends IteratorKind>  {
    protected PositionStack<Item, Kind> stack;
    protected Kind kind;
    protected Item item;
    protected boolean beginning;

    public PrePostIterator(Class<Item> iclass, Class<Kind> kclass, int stackSize) throws IOException {
        stack = new PositionStack<Item, Kind>(iclass, kclass, stackSize);
        beginning = false; // start out at fake end
    }

    public boolean hasNext() {
        return !stack.isEmpty() || (beginning && kind != null && kind.isCompound());
    }

    abstract public Kind next() throws IOException ;
    
    public Kind skipValue() {
        assert beginning;
        beginning = false;
        return kind;
    }

    public boolean atBeginning() {
        return beginning;
    }

  
    public Kind currentKind() {
        return kind;
    }

    public Item getValue() {
        return item;
    }

}

