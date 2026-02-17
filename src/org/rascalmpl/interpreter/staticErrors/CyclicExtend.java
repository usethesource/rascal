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

public class CyclicExtend extends StaticError {
    private static final long serialVersionUID = -7673780054774870139L;
    private final List<ISourceLocation> cycle;

    public CyclicExtend(String name, List<ISourceLocation> cycle, ISourceLocation loc) {
        super("Extend cycle detected (" + name + "):\n* " + cycle.stream().map(Object::toString).collect(Collectors.joining("\n* ")), loc);
        this.cycle = cycle;
    }

    public List<ISourceLocation> getCycle() {
        return cycle;
    }
}
