package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.IOException;

public abstract class PrePostIterator<Item, Kind extends IteratorKind>  {
    protected PositionStack<Item, Kind> stack;
    protected Kind kind;
    protected Item item;
    protected boolean beginning;

    public PrePostIterator(int stackSize) {
        stack = new PositionStack<Item, Kind>(stackSize);
        beginning = false; // start out at fake end
    }

    public boolean hasNext() {
        return !stack.isEmpty() || (beginning && kind != null && kind.isCompound());
    }

    abstract public Kind next() throws IOException ;
    
    public Kind skipItem() {
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

    public Item getItem() {
        return item;
    }

}