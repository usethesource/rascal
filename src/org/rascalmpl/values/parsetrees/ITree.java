package org.rascalmpl.values.parsetrees;

import org.rascalmpl.values.parsetrees.visitors.TreeVisitor;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;

public interface ITree extends IConstructor {
    
	default boolean isAppl() {
		return false;
	}
	
	default boolean isAmb() {
		return false;
	}
	
	default boolean isChar() {
		return false;
	}
	
	default boolean isCycle() {
		return false;
	}
	
	default IConstructor getProduction() {
		throw new UnsupportedOperationException();
	}
	
	default ISet getAlternatives() {
		throw new UnsupportedOperationException();
	}
	
	default IList getArgs() {
		throw new UnsupportedOperationException();
	}
	
	default IInteger getCharacter() {
		throw new UnsupportedOperationException();
	}
	
	<E extends Throwable> ITree accept(TreeVisitor<E> v) throws E;
	
	@Override
	default INode setChildren(IValue[] childArray) {
	    INode result = this;

	    for (int i = 0; i < arity(); i++) {
	        result = result.set(i, childArray[i]);
	    }

	    return result;
	}
}