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

import org.checkerframework.checker.nullness.qual.Nullable;

import io.usethesource.vallang.ISourceLocation;

/**
 * Resolver of logical locations with a certain {@link #scheme} and {@link #authority}. Implementations of this
 * interface for non-empty authorities are <em>authority-specific resolvers</em>. Implementations for the distinguished
 * empty authority are <em>default resolvers</em>. To resolve a logical location, first, an authority-specific resolver
 * for the scheme is sought. If it doesn't exist, then the default resolver for the scheme is sought instead. If it
 * doesn't exist either, then an exception is thrown. Note: If an authority-specific resolver does exist, but fails,
 * then the default resolver will not be sought instead (i.e., the default resolver resembles the default case of a
 * switch statement with non-fallthrough cases).
 */
public interface ILogicalSourceLocationResolver {

	/**
	 * @return {@code null} only if the authority of {@code input} is not supported/covered by this resolver.
	 * Non-{@code null} otherwise.
	 */
	@Nullable ISourceLocation resolve(ISourceLocation input) throws IOException;

	String scheme();

	/**
	 * @return A non-empty string if this is an authority-specific resolver. The empty string if this is a default
	 * resolver.
	 */
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
