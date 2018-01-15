package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Map;

import io.usethesource.vallang.IValue;

/**
 * A Reference identifies a variable in a specific stack frame or a module variable and can be used
 * to retrieve that variable's value or destructively update its value.
 * TODO: Split in two classes LocalReference and GlobalReference that implement IReference
 */
public class Reference implements IReference {

    public final Object[] stack;
    public final int pos;

    Map<IValue, IValue> moduleVariables;
    IValue varName;

    public Reference(Map<IValue, IValue> moduleVariables, IValue varName){
        this.stack = null;
        this.pos = 0;
        this.moduleVariables = moduleVariables;
        this.varName = varName;
    }

    public Reference(final Object[] stack, final int pos) {
        this.stack = stack;
        this.pos = pos;
    }

    /**
     * @return the variable's value
     */
    @Override
    public Object getValue(){
        return stack == null ? moduleVariables.get(varName) : stack[pos];
    }

    /**
     * Set the variable to new value v
     * @param the new value
     */
    @Override
    public void setValue(Object v){
        if(stack == null){
            moduleVariables.put(varName, (IValue) v);
        } else {
            stack[pos] = v;
        }
    }

    /**
     * @return true when this reference refers to a variable with non-null value.
     */
    @Override
    public boolean isDefined(){
        return (stack == null ? moduleVariables.get(varName) : stack[pos]) != null;
    }

    /**
     * Set the variable identified by this reference to undefined (null).
     */
    @Override
    public void undefine(){
        if(stack == null){
            moduleVariables.put(varName, null);
        } else {
            stack[pos] = null;
        }
    }

    /**
     * @return string representation of this reference
     */
    @Override
    public String toString(){
        return stack == null ? "ref["  + varName + " => " + moduleVariables.get(varName) + "]"
                             : "ref[@" + System.identityHashCode(stack) + ":" + pos + " => " + stack[pos] + "]";
    } 
}
