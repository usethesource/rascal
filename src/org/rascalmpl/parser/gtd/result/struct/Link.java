/*******************************************************************************
 * Copyright (c) 2009-2025 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.result.struct;

import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.EpsilonNode;
import org.rascalmpl.parser.gtd.result.LiteralNode;
import org.rascalmpl.parser.gtd.result.SkippedNode;
import org.rascalmpl.parser.gtd.util.ArrayList;

import io.usethesource.vallang.IConstructor;

/**
 * A structure that links a result node to a set of prefixes.
 */
public class Link{
	private static final int PREFIX_EMPTINESS_UNKNOWN = 0;
	private static final int PREFIX_CALCULATING = 1;
	private static final int PREFIX_CAN_BE_EMPTY = 2;
	private static final int PREFIX_NOT_EMPTY = 3;

	private static final int CACHEABLE_UNKNOWN  = 0;
	private static final int CACHEABLE_CALCULATING = 1;
	private static final int CACHEABLE_YES = 2;
	private static final int CACHEABLE_NO = 3;

	private final ArrayList<Link> prefixes;
	private final AbstractNode node;

	private int emptyPrefix;
	private int cacheable;

	public Link(ArrayList<Link> prefixes, AbstractNode node){
		super();
		
		this.prefixes = prefixes;
		this.node = node;

		emptyPrefix = PREFIX_EMPTINESS_UNKNOWN;
		cacheable = CACHEABLE_UNKNOWN;
	}
	
	public ArrayList<Link> getPrefixes(){
		return prefixes;
	}
	
	public AbstractNode getNode(){
		return node;
	}

	public boolean isCacheable() {
		switch (cacheable) {
			case CACHEABLE_YES: return true;
			case CACHEABLE_NO: return false;
			case CACHEABLE_CALCULATING:
				cacheable = CACHEABLE_NO; // In a cycle
				return false;
			case CACHEABLE_UNKNOWN:
				cacheable = CACHEABLE_CALCULATING;
				cacheable = checkCacheable() ? CACHEABLE_YES : CACHEABLE_NO;
				return cacheable == CACHEABLE_YES;
			default:
				throw new IllegalStateException("Unknown cacheable state: " + cacheable);
		}
	}

	private boolean checkCacheable() {
		int type = node.getTypeIdentifier();
		if (type == CharNode.ID || type == LiteralNode.ID || type == SkippedNode.ID || type == EpsilonNode.ID) {
			return true;
		}

		if (node.isEmpty()) {
			if (prefixes == null || prefixes.size() == 0) {
				return allAltsCacheable(node);
			}

			for (int i = prefixes.size() - 1; i >= 0; --i) {
				Link prefix = prefixes.get(i);
				if (prefix == null) {
					continue;
				}

				if (!prefix.isCacheable()) {
					return false;
				}
			}

			return true;
		}
		else if (!canPrefixBeEmpty()) {
			return true;
		}

		if (prefixes == null || prefixes.size() == 0) {
			return allAltsCacheable(node);
		}

		return false;
	}

	private boolean allAltsCacheable(AbstractNode node) {
		if (!(node instanceof AbstractContainerNode)) {
			return true;
		}

		@SuppressWarnings("unchecked")
		AbstractContainerNode<IConstructor> containerNode = (AbstractContainerNode<IConstructor>) node;
		if(containerNode.getFirstAlternative() != null && !containerNode.getFirstAlternative().isCacheable()) {
			return false;
		}

		ArrayList<Link> alts = containerNode.getAdditionalAlternatives();
		if (alts == null) {
			return true;
		}

		for (int i=alts.size()-1; i>=0; i--) {
			if (!alts.get(i).isCacheable()) {
				return false;
			}
		}

		return true;
	}

	public boolean canPrefixBeEmpty() {
		switch (emptyPrefix) {
			case PREFIX_CAN_BE_EMPTY: return true;
			case PREFIX_NOT_EMPTY: return false;
			case PREFIX_CALCULATING:
				emptyPrefix = PREFIX_CAN_BE_EMPTY; // In a cycle
				return true;
			case PREFIX_EMPTINESS_UNKNOWN:
				emptyPrefix = PREFIX_CALCULATING;
				emptyPrefix = checkPrefixBeEmpty() ? PREFIX_CAN_BE_EMPTY : PREFIX_NOT_EMPTY;
				return emptyPrefix == PREFIX_CAN_BE_EMPTY;
			default:
				throw new IllegalStateException("Unknown prefix emptiness state: " + emptyPrefix);
		}
	}

	private boolean checkPrefixBeEmpty() {
		if (prefixes == null || prefixes.size() == 0) {
			return true;
		}

		boolean anyNonEmpty = false;

		for (int i=prefixes.size()-1; i>=0; --i) {
			Link prefix = prefixes.get(i);
			if (prefix == null) {
				continue;
			}

			if (!prefix.node.isEmpty()) {
				anyNonEmpty = true;
				continue;
			}

			if (prefix.canPrefixBeEmpty()) {
				return true;
			} else {
				anyNonEmpty = true;
			}
		}

		return !anyNonEmpty;
	}

	public String toString() {
		return "Link[node=" + node + ", prefixes=" + (prefixes == null ? 0 : prefixes.size()) + "]";
	}
}
