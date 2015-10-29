package org.rascalmpl.values.uptr;

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.values.uptr.visitors.TreeVisitor;

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
	
	@Override
	public IWithKeywordParameters<ITree> asWithKeywordParameters();
	
	<E extends Throwable> ITree accept(TreeVisitor<E> v) throws E;
}
