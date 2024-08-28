/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.parser.gtd.stack;

import org.rascalmpl.parser.gtd.result.AbstractNode;

@SuppressWarnings({"unchecked", "cast"})
public class RecoveryPointStackNode<P> extends AbstractStackNode<P>{
	private final String name;
	private final P parent;

	public RecoveryPointStackNode(int id, P parent, AbstractStackNode<P> robustNode){
		super(id, robustNode, robustNode.startLocation);
		this.prefixesMap = robustNode.prefixesMap;

		// Modify the production, so it ends 'here'.
		int productionLength = robustNode.dot + 1;
		if(productionLength == robustNode.production.length){
			this.production = robustNode.production;
		}else{
			this.production = (AbstractStackNode<P>[]) new AbstractStackNode[productionLength];
			System.arraycopy(robustNode.production, 0, this.production, 0, productionLength);
		}

		this.parent = parent;
		this.name = "recovery " + id;
		this.edgesMap = robustNode.edgesMap;
	}

	@Override
	public P getParentProduction() {
		return parent;
	}

	@Override
	public boolean isRecovered() {
		return true;
	}

	@Override
	public boolean isEmptyLeafNode(){
		return false;
	}

	@Override
	public boolean isEndNode() {
		return true;
	}

	@Override
	public String getName(){
		return "***robust:" + name + "***";
	}

	public AbstractNode match(int[] input, int location){
		throw new UnsupportedOperationException();
	}

	public AbstractStackNode<P> getCleanCopy(int startLocation){
		throw new UnsupportedOperationException();
	}

	public AbstractStackNode<P> getCleanCopyWithResult(int startLocation, AbstractNode result){
		throw new UnsupportedOperationException();
	}

	public int getLength(){
		throw new UnsupportedOperationException();
	}

	public AbstractStackNode<P>[] getChildren(){
		throw new UnsupportedOperationException();
	}

	public boolean canBeEmpty(){
		throw new UnsupportedOperationException();
	}

	public AbstractStackNode<P> getEmptyChild(){
		throw new UnsupportedOperationException();
	}

	public AbstractNode getResult(){
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		sb.append(':');
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');

		return sb.toString();
	}

	@Override
	public int hashCode(){
		return getName().hashCode();
	}

	@Override
	public boolean equals(Object peer) {
		return super.equals(peer);
	}

	public boolean isEqual(AbstractStackNode<P> stackNode){
		if(!(stackNode instanceof RecoveryPointStackNode)) return false;

		RecoveryPointStackNode<P> otherNode = (RecoveryPointStackNode<P>) stackNode;

		return otherNode.name.equals(name) && otherNode.startLocation == startLocation;
	}

	void accept(StackNodeVisitor<P> visitor) {
		visitor.visit(this);
	}

}
