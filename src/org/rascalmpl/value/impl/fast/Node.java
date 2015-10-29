/*******************************************************************************
* Copyright (c) 2009 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Arnold Lankamp - interfaces and implementation
*    Paul Klint - Implemented replace
*******************************************************************************/
package org.rascalmpl.value.impl.fast;

import java.util.Iterator;
import java.util.Map;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.impl.AbstractNode;
import org.rascalmpl.value.impl.func.NodeFunctions;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.visitors.IValueVisitor;

import io.usethesource.capsule.ArrayIterator;

/**
 * Implementation of INode.
 * 
 * @author Arnold Lankamp
 */
/*package*/ class Node extends AbstractNode implements INode {
	protected final static Type NODE_TYPE = TypeFactory.getInstance().nodeType();
	protected final static Type VALUE_TYPE = TypeFactory.getInstance().valueType();
	
	protected final String name;
	protected final IValue[] children;

	/*package*/ static INode newNode(String name, IValue[] children) {
		return new Node(name, children);
	}
	
	private Node(String name, IValue[] children) {
		super();
		
		this.name = (name != null ? name.intern() : null); // Handle (weird) special case.
		this.children = children;
	}

	/*package*/ static INode newNode(String name, IList children) {
		return new Node(name, children);
	}
	
	private Node(String name, IList children) {
		super();
		IValue[] childArray = new IValue[children.length()];
		this.name = (name != null ? name.intern() : null); // Handle (weird) special case.
		for(int i = 0; i < childArray.length; i++){
			childArray[i] = children.get(i);
		}
		this.children = childArray;
	}
	
	/*package*/ static INode newNode(String name, IValue[] children, Map<String, IValue> keyArgValues) {
		INode node = new Node(name, children, keyArgValues);
		
		if (keyArgValues != null && keyArgValues.size() > 0) {
		  return node.asWithKeywordParameters().setParameters(keyArgValues);
		}
		
		return node;
	}
	
	private Node(String name, IValue[] children, Map<String, IValue> keyArgValues) {
		super();
		this.name = (name != null ? name.intern() : null); // Handle (weird) special case.
		this.children = children;
	}

	@Override
	public Type getType(){
		return NODE_TYPE;
	}
	
	@Override
	public int arity(){
		return children.length;
	}

	@Override
	protected IValueFactory getValueFactory() {
		return ValueFactory.getInstance();
	}

	@Override
	public IValue get(int i){
		return children[i];
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	@Override
	public Iterator<IValue> iterator(){
		return ArrayIterator.of(children);
	}
	
	@Override
	public Iterable<IValue> getChildren(){
		return this;
	}

	@Override
	public INode set(int i, IValue arg){
		IValue[] newChildren = children.clone();
		newChildren[i] = arg;
		
		return newNode(name, newChildren);
	}
	
	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E{
		return v.visitNode(this);
	}	

	@Override
	public int hashCode(){
		int hash = name.hashCode();
		
		for(int i = children.length - 1; i >= 0; i--){
			hash = (hash << 23) + (hash >> 5);
			hash ^= children[i].hashCode();
		}
		return hash;
	}

	@Override
	public boolean equals(Object o){
		if (o == this) {
		  return true;
		}
		if (o == null) {
		  return false;
		}
		
		if (o.getClass() != getClass()) {
		  return false;
		}
		
		Node other = (Node) o;

		// Yes '!=' works here, since it has been interned.
		if (name != other.name) {
		  return false; 
		}

		IValue[] otherChildren = other.children;
		int nrOfChildren = children.length;

		if (otherChildren.length != nrOfChildren) {
		  return false;
		}

		for (int i = nrOfChildren - 1; i >= 0; i--) {
		  if (!otherChildren[i].equals(children[i])) {
		    return false;
		  }
		}

		return true;
	}
	
	/**
	 * TODO: Check if it is easily possible to cast annotatable's content to
	 * List and to reuse old isEqual.
	 */
	@Override
	public boolean isEqual(IValue value){
		return NodeFunctions.isEqual(getValueFactory(), this, value);
	}
}
