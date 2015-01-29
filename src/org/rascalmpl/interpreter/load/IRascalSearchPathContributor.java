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
package org.rascalmpl.interpreter.load;

import java.util.List;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public interface IRascalSearchPathContributor {
	void contributePaths(List<ISourceLocation> path);
	String getName();
}
