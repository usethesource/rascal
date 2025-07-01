/*******************************************************************************
 * Copyright (c) 2009-2021 NWO-I CWI
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
import java.util.function.Consumer;

import io.usethesource.vallang.ISourceLocation;

public interface ISourceLocationWatcher {
	/**
	 * Declares the scheme that this watcher governs
	 */
	String scheme();

	/**
	 * Register a watcher callback for the current scheme at the given root
	 * @param root        the resource to watch
	 * @param watcher     the callback to call when something happens to the registred resource
	 * @param recursive   also watch all nested directories for changes (use {@link #supportsRecursiveWatch()} to verify it's supported)
	 * @throws IOException
	 */
	void watch(ISourceLocation root, Consumer<ISourceLocationChanged> watcher, boolean recursive) throws IOException;

	/**
	 * Unregister a watcher callback for a specific uri (note, there can be multiple watchers per resource, this only clears this specific watcher)
	 * @param root the resource to unwatch
	 * @param watcher the specific callback to unregister
	 * @param recursive  was the watch registered with a recursive flag (use {@link #supportsRecursiveWatch()} to verify it's supported)
	 * @throws IOException
	 */
	void unwatch(ISourceLocation root, Consumer<ISourceLocationChanged> watcher, boolean recursive) throws IOException;

	/**
	 * Does this watcher support a recursive watch request.
	 */
	boolean supportsRecursiveWatch();


	public interface ISourceLocationChanged {
		ISourceLocation getLocation();
		ISourceLocationChangeType getChangeType();

		default boolean isCreated() {
			return getChangeType() == ISourceLocationChangeType.CREATED;
		}

		default boolean isDeleted() {
			return getChangeType() == ISourceLocationChangeType.DELETED;
		}

		default boolean isChanged() {
			return getChangeType() == ISourceLocationChangeType.MODIFIED;
		}
	}

	public enum ISourceLocationChangeType {
		CREATED(),
		DELETED(),
		MODIFIED()
	}

	static ISourceLocationChanged created(ISourceLocation loc) {
		return makeChange(loc, ISourceLocationChangeType.CREATED);
	}

	static ISourceLocationChanged deleted(ISourceLocation loc) {
		return makeChange(loc, ISourceLocationChangeType.DELETED);
	}

	static ISourceLocationChanged modified(ISourceLocation loc) {
		return makeChange(loc, ISourceLocationChangeType.MODIFIED);
	}

	static ISourceLocationChanged makeChange(final ISourceLocation loc, final ISourceLocationChangeType changeType) {
		return new ISourceLocationChanged() {
			@Override
			public ISourceLocationChangeType getChangeType() {
				return changeType;
			}

			@Override
			public ISourceLocation getLocation() {
				return loc;
			}
		};
	}
}
