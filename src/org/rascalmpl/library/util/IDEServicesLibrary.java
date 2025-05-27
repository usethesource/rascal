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

import java.io.IOException;
import java.util.function.Function;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.repl.http.REPLContentServer;
import org.rascalmpl.repl.http.REPLContentServerManager;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;

public class IDEServicesLibrary {
    private final REPLContentServerManager contentManager = new REPLContentServerManager();
    private final IDEServices services;

    public IDEServicesLibrary(IDEServices services) {
        this.services = services;
    }

    public void browse(ISourceLocation uri, IString title, IInteger viewColumn) {
        services.browse(uri.getURI(), title.getValue(), viewColumn.intValue());
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

	public void showInteractiveContent(IConstructor provider, IString title, IInteger viewColumn) {
        try {
            String id;
            Function<IValue, IValue> target;

            if (provider.has("id")) {
                id = ((IString) provider.get("id")).getValue();
                target = (r) -> ((IFunction) provider.get("callback")).call(r);
            } else {
                id = "*static content*";
                target = (r) -> provider.get("response");
            }

            // this installs the provider such that subsequent requests are handled.
            REPLContentServer contentServer = contentManager.addServer(id, target);

            browse(
                URIUtil.correctLocation("http", "localhost:" + contentServer.getListeningPort(), "/"),
                title.length() == 0? IRascalValueFactory.getInstance().string(id) : title,
                viewColumn);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e.getMessage());
        }
    }

    public void showMessage(IConstructor message) {
        services.showMessage(message);
    }

    public void logMessage(IConstructor message) {
        services.logMessage(message);
    }


    public void registerDiagnostics(IList messages) {
        services.registerDiagnostics(messages);
    }

    public void unregisterDiagnostics(IList resources) {
        services.unregisterDiagnostics(resources);
    }
}
