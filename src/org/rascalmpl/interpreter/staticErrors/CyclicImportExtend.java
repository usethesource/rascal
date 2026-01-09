/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import java.util.List;
import java.util.stream.Collectors;

import io.usethesource.vallang.ISourceLocation;

public class CyclicImportExtend extends StaticError {
    private static final long serialVersionUID = -3820319837749195535L;
    private final List<ISourceLocation> cycle;

    public CyclicImportExtend(String name, List<ISourceLocation> cycle, ISourceLocation loc) {
        super("Extend/import cycle detected:\n* " + cycle.stream().map(Object::toString).collect(Collectors.joining("\n* ")), loc);
        this.cycle = cycle;
    }

    public List<ISourceLocation> getCycle() {
        return cycle;
    }
}
