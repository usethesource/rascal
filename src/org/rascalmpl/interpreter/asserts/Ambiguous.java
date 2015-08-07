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
package org.rascalmpl.interpreter.asserts;

import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.rascalmpl.values.uptr.TreeAdapter;


public final class Ambiguous extends AssertionError {
	private static final long serialVersionUID = -8740312542969306482L;
	private final ISourceLocation loc;
	private final IConstructor tree;

	public Ambiguous(IConstructor tree) {
		super("Ambiguous code (internal error), " + TreeAdapter.yield(tree, 100));
		this.loc = (ISourceLocation) tree.asAnnotatable().getAnnotation("loc");
		this.tree = tree;
	}
	
	public Ambiguous(ISourceLocation loc) {
		super("Ambiguous code (internal error)");
		this.loc = loc;
		this.tree = null;
	}

	public ISourceLocation getLocation() {
		return loc;
	}
	
	public IConstructor getTree() {
		return tree;
	}
}
