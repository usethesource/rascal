package org.rascalmpl.uri;

import java.io.IOException;

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
	ISourceLocation resolve(ISourceLocation input) throws IOException;
	String scheme();

	/**
	 * @return A non-empty string if this is an authority-specific resolver. The empty string if this is a default
	 * resolver.
	 */
	String authority();
}
