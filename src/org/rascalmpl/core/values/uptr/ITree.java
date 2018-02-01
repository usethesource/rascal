package org.rascalmpl.core.values.uptr;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import org.rascalmpl.core.values.uptr.visitors.TreeVisitor;

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
}