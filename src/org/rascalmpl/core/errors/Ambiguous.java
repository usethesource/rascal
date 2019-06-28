/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.core.errors;

import org.rascalmpl.core.uri.URIUtil;
import org.rascalmpl.core.values.uptr.ITree;
import org.rascalmpl.core.values.uptr.TreeAdapter;

import io.usethesource.vallang.ISourceLocation;


public final class Ambiguous extends AssertionError {
	private static final long serialVersionUID = -8740312542969306482L;
	private final ISourceLocation loc;
	private final ITree tree;

	public Ambiguous(ITree tree) {
		super("Ambiguous code (internal error), " + TreeAdapter.yield(tree, 100));
		this.loc = computeLocation(tree);
		this.tree = tree;
	}

    private ISourceLocation computeLocation(ITree tree) {
        ISourceLocation tmp = (ISourceLocation) TreeAdapter.getAlternatives(tree).iterator().next().asAnnotatable().getAnnotation("loc");
        if (tmp == null) {
            return URIUtil.rootLocation("unknown");
        }
        return tmp;
    }
	
	public Ambiguous(ISourceLocation loc) {
		super("Ambiguous code (internal error)");
		this.loc = loc;
		this.tree = null;
	}

	public ISourceLocation getLocation() {
		return loc;
	}
	
	public ITree getTree() {
		return tree;
	}
}
