/*******************************************************************************
 * Copyright (c) 2021 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 */
package org.rascalmpl.library.util;

import org.rascalmpl.ideservices.IDEServices;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;

public class IDEServicesLibrary {
    private final IDEServices services;

    public IDEServicesLibrary(IDEServices services) {
        this.services = services;
    }

    public void browse(ISourceLocation uri) {
        services.browse(uri.getURI());
    }

    public void edit(ISourceLocation path) {
        services.edit(path);
    }

    public ISourceLocation resolveProjectLocation(ISourceLocation input) {
        return services.resolveProjectLocation(input);
    }

    public void registerLanguage(IConstructor language) {
        services.registerLanguage(language);
    }

    public void applyDocumentsEdits(IList edits) {
        services.applyDocumentsEdits(edits);
    }

	public void showInteractiveContent(IConstructor content) {
        services.showInteractiveContent(content);
    }
}
