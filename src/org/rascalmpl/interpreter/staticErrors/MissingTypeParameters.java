/*******************************************************************************
 * Copyright (c) 2009-2019 NWO-I CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;
import io.usethesource.vallang.type.Type;

public class MissingTypeParameters extends StaticError {
    private static final long serialVersionUID = 2530527747503284558L;

    public MissingTypeParameters(Type forType, AbstractAST node) {
		super("Type parameters missing for: " + forType, node);
	}
}
