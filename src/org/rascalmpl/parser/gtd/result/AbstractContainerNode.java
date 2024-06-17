/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.result;

import java.net.URI;

import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.util.DebugUtil;

import io.usethesource.vallang.IConstructor;

/**
 * All nodes in the resulting tree that can contain other nodes are a subtype
 * of this class.
 */
public abstract class AbstractContainerNode<P> extends AbstractNode{
	// Location related.
	protected final URI input;
	protected final int offset;
	protected final int endOffset;
	
	// Flags.
	protected final boolean isNullable;
	protected final boolean isSeparator;
	protected final boolean isLayout;
	
	// Children.
	protected Link firstAlternative;
	protected P firstProduction;
	protected ArrayList<Link> alternatives;
	protected ArrayList<P> productions;
	
	public AbstractContainerNode(URI input, int offset, int endOffset, boolean isNullable, boolean isSeparator, boolean isLayout){
		super();
		
		this.input = input;
		this.offset = offset;
		this.endOffset = endOffset;
		
		this.isNullable = isNullable;
		this.isSeparator = isSeparator;
		this.isLayout = isLayout;
	}
	
	/**
	 * Adds a result to this container node.
	 * In case there is only one alternative (i.e. this node does not contain
	 * any ambiguities), only the 'first' fields will be set. In case
	 * additional alternatives are added later on, the lists will be
	 * initialized and used for storage.
	 */
	public void addAlternative(P production, Link children){
		if(firstAlternative == null){
			firstAlternative = children;
			firstProduction = production;
		}else{
			if(alternatives == null){
				alternatives = new ArrayList<Link>(1);
				productions = new ArrayList<P>(1);
			}
			alternatives.add(children);
			productions.add(production);
		}
	}
	
	/**
	 * Returns the input location; may be null.
	 */
	public URI getInput(){
		return input;
	}
	
	/**
	 * Returns the offset in the input string the results in this node start at.
	 */
	public int getOffset(){
		return offset;
	}
	
	/**
	 * Returns the offset in the input string the results in this node end at.
	 */
	public int getEndOffset(){
		return endOffset;
	}
	
	/**
	 * Checks if the results contained in this node are zero length or not.
	 */
	public boolean isEmpty(){
		return isNullable;
	}
	
	/**
	 * Checks if the results contained in this node represent a separator.
	 */
	public boolean isNonterminalSeparator(){
		return isSeparator;
	}
	
	/**
	 * Checks if the results contained in this node represent layout.
	 */
	public boolean isLayout(){
		return isLayout;
	}
	
	/**
	 * Returns the production associated with the first result alternative in
	 * this container node.
	 */
	public P getFirstProduction(){
		return firstProduction;
	}
	
	/**
	 * Returns the first result alternative in this container node.
	 */
	public Link getFirstAlternative(){
		return firstAlternative;
	}
	
	/**
	 * Returns the set of all 'other' productions contained in this node. In
	 * case additional alternative results are present, the order in the
	 * returned list will match with the order of the alternative results in
	 * the accompanied alternatives list. In case this node does not contain
	 * ambiguous results, 'null' will be returned.
	 */
	public ArrayList<P> getAdditionalProductions(){
		return productions;
	}
	
	/**
	 * Returns the set of all 'other' alternative results contained in this
	 * node. In case this node does not contain ambiguous results, 'null' will
	 * be returned. Note that the order in the list of alternative results
	 * matches the order in the associated list of productions.
	 */
	public ArrayList<Link> getAdditionalAlternatives(){
		return alternatives;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("offset=" + offset);
		builder.append(",endOffset=" + endOffset);
		if (isNullable) {
			builder.append(",nullable");
		}
		if (isSeparator) {
			builder.append(",separator");
		}
		if (isLayout) {
			builder.append(",layout");
		}
		if (firstAlternative != null) {
			builder.append(",alternatives=[");
			builder.append(firstAlternative);
			builder.append(":");
			builder.append(DebugUtil.prodToString((IConstructor) firstProduction));

			if (alternatives != null) {
				for (int i=0; i<alternatives.size(); i++) {
					builder.append(",");
					builder.append(alternatives.get(i));
					builder.append(":");
					builder.append(DebugUtil.prodToString((IConstructor) productions.get(i)));
				}
			}
			builder.append("]");
		}
		return builder.toString();
	}
}
