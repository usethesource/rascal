/*******************************************************************************
 * Copyright (c) 2009-2025 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *******************************************************************************/

package org.rascalmpl.uri;

import java.io.IOException;

import io.usethesource.vallang.ISourceLocation;

/**
 * A logical resolver translates locations keyed by their scheme and (optionally) their authority
 * to a lower level scheme. Logical resolvers may translate to other logical resolvers, but
 * most of them map directly to a physical location like file:/// 
 */
public interface ILogicalSourceLocationResolver {
	ISourceLocation resolve(ISourceLocation input) throws IOException;
	String scheme();
	String authority();

	/**
	 * Some logical resolver may collect entries from more than one location,
	 * hence first resolving and then calling ISourceLocationInput.list(..) won't do.
	 * An example is the PATH:/// resolver.
	 */
	default String[] resolveList(ISourceLocation input) throws IOException {
		return URIResolverRegistry.getInstance().listEntries(resolve(input));
	}
}
