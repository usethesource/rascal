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
	 * @throws IOException
	 */
	void watch(ISourceLocation root, Consumer<ISourceLocationChanged> watcher) throws IOException;

	public interface ISourceLocationChanged {
		ISourceLocation getLocation();
		ISourceLocationChangeType getChangeType();
		ISourceLocationType getType();

		default boolean isCreated() {
			return getChangeType() == ISourceLocationChangeType.CREATED;
		}

		default boolean isDeleted() {
			return getChangeType() == ISourceLocationChangeType.DELETED;
		}

		default boolean isChanged() {
			return getChangeType() == ISourceLocationChangeType.MODIFIED;
		}

		default boolean isFile() {
			return getType() == ISourceLocationType.FILE;
		}

		default boolean isDirectory() {
			return getType() == ISourceLocationType.DIRECTORY;
		}
	}

	public enum ISourceLocationChangeType {
		CREATED(),
		DELETED(),
		MODIFIED()
	}

	public enum ISourceLocationType {
		FILE(),
		DIRECTORY()
	}

	static ISourceLocationChanged fileCreated(ISourceLocation loc) {
		return makeChange(loc, ISourceLocationChangeType.CREATED, ISourceLocationType.FILE);
	}

	static ISourceLocationChanged directoryCreated(ISourceLocation loc) {
		return makeChange(loc, ISourceLocationChangeType.CREATED, ISourceLocationType.DIRECTORY);
	}

	static ISourceLocationChanged fileDeleted(ISourceLocation loc) {
		return makeChange(loc, ISourceLocationChangeType.DELETED, ISourceLocationType.FILE);
	}

	static ISourceLocationChanged directoryDeleted(ISourceLocation loc) {
		return makeChange(loc, ISourceLocationChangeType.DELETED, ISourceLocationType.DIRECTORY);
	}

	static ISourceLocationChanged fileModified(ISourceLocation loc) {
		return makeChange(loc, ISourceLocationChangeType.MODIFIED, ISourceLocationType.FILE);
	}

	static ISourceLocationChanged directoryModified(ISourceLocation loc) {
		return makeChange(loc, ISourceLocationChangeType.MODIFIED, ISourceLocationType.DIRECTORY);
	}

	static ISourceLocationChanged makeChange(final ISourceLocation loc, final ISourceLocationChangeType changeType, final ISourceLocationType fileType) {
		return new ISourceLocationChanged() {
			@Override
			public ISourceLocationChangeType getChangeType() {
				return changeType;
			}

			@Override
			public ISourceLocation getLocation() {
				return loc;
			}

			@Override
			public ISourceLocationType getType() {
				return fileType;
			}
		};
	}
}
