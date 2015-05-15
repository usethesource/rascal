package org.rascalmpl.values.uptr;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.AbstractDefaultAnnotatable;
import org.eclipse.imp.pdb.facts.util.ImmutableMap;
import org.rascalmpl.values.uptr.RascalValueFactory.AnnotatedTreeFacade;
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
	default IAnnotatable<? extends IConstructor> asAnnotatable() {
		return new AbstractDefaultAnnotatable<ITree>(this) {
			@Override
			protected ITree wrap(ITree content,
					ImmutableMap<String, IValue> annotations) {
				return new AnnotatedTreeFacade(content, annotations);
			}
		};
	}
	
	<E extends Throwable> ITree accept(TreeVisitor<E> v) throws E;
}