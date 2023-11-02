package org.rascalmpl.values.parsetrees;

import org.rascalmpl.values.parsetrees.visitors.TreeVisitor;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.visitors.IValueVisitor;

public interface ITree extends IConstructor, IExternalValue {
    
	@Override
	default IConstructor encodeAsConstructor() {
		return this;
	}

	@Override
	default <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
	    return v.visitExternal(this);
	}

	@Override
	default int getMatchFingerprint() {
		// ITrees must simulate their constructor prints in case
		// we pattern match on the abstract Tree data-type
		return IConstructor.super.getMatchFingerprint();
	}

	/**
	 * Concrete patterns need another layer of fingerprinting on top
	 * of `getMatchFingerprint`. The reason is that _the same IValue_
	 * can be matched against an abstract pattern of the Tree data-type,
	 * and against concrete patterns. 
	 * 
	 * Like before, the match-fingerprint contract is:
	 *   if pattern.match(tree) ==> pattern.fingerprint() == match.fingerprint();
	 * 
	 * @return a unique code for each outermost ITree node
	 */
	int getConcreteMatchFingerprint();

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
