package org.rascalmpl.values.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
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