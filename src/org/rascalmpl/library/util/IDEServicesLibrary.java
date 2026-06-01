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
import io.usethesource.vallang.type.TypeFactory;

public class IDEServicesLibrary {
    private final REPLContentServerManager contentManager = new REPLContentServerManager();
    private final IDEServices services;
    private final IRascalValueFactory vf;

    public IDEServicesLibrary(IDEServices services, IRascalValueFactory vf) {
        this.services = services;
        this.vf = vf;
    }

    public void browse(ISourceLocation uri, IString title, IInteger viewColumn) {
        services.browse(uri.getURI(), title, viewColumn);
    }

    public void edit(ISourceLocation path, IInteger viewColumn) {
        services.edit(path, viewColumn.intValue());
    }

    public ISourceLocation resolveProjectLocation(ISourceLocation input) {
        return services.resolveProjectLocation(input);
    }

    public void applyDocumentsEdits(IList edits) {
        applyFileSystemEdits(edits);
    }

    public void applyFileSystemEdits(IList edits) {
        services.applyFileSystemEdits(edits);
    }

	public void showInteractiveContent(IConstructor provider, IString title, IInteger viewColumn) {
        try {
            String id;
            IFunction target;

            if (provider.has("id")) {
                id = ((IString) provider.get("id")).getValue();
                target = (IFunction) provider.get("callback");
            } else {
                id = "*static content*";
                TypeFactory tf = TypeFactory.getInstance();
                
                target = vf.function((args, kwargs) -> provider.get("response"));
            }

            // this installs the provider such that subsequent requests are handled.
            REPLContentServer contentServer = contentManager.addServer(id, target);

            browse(
                URIUtil.correctLocation("http", "localhost:" + contentServer.getListeningPort(), "/"),
                title.length() == 0? IRascalValueFactory.getInstance().string(id) : title,
                viewColumn);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(e);
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
