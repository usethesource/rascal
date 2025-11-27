/*******************************************************************************
 * Copyright (c) 2009-2024 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 ******************************************************************************/
package org.rascalmpl.uri.file;

import java.nio.file.Paths;

/**
 * For reading and writing files relative to the current working drive.
 * This is different from the current working directory, namely it is the 
 * root of the current working directory.
 */
public class CurrentWorkingDriveResolver extends AliasedFileResolver {

	public CurrentWorkingDriveResolver() {
		super("cwdrive", deriveCurrentWorkingDrive(System.getProperty("user.dir")));
	}

	private static String deriveCurrentWorkingDrive(String userDir) {
		return Paths.get(userDir).toAbsolutePath().getRoot().toString();
	}
}
