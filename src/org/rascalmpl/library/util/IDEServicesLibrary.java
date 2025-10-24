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
import java.io.StringWriter;
import java.util.function.Function;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.repl.http.REPLContentServer;
import org.rascalmpl.repl.http.REPLContentServerManager;
import org.rascalmpl.uri.URIResolverRegistry;
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

    public void edit(ISourceLocation path, IInteger viewColumn) {
        services.edit(path, viewColumn.intValue());
    }

    public ISourceLocation resolveProjectLocation(ISourceLocation input) {
        return services.resolveProjectLocation(input);
    }

    public void registerLanguage(IConstructor language) {
        services.registerLanguage(language);
    }

    public void applyDocumentsEdits(IList edits) {
        applyFileSystemEdits(edits);
    }

    public void applyFileSystemEdits(IList edits) {
        var registry = URIResolverRegistry.getInstance();
        var vf = IRascalValueFactory.getInstance();
        edits.stream().map(IConstructor.class::cast).forEach(c -> {
            try {
                switch (c.getName()) {
                    case "removed": {
                        var file = (ISourceLocation) c.get("file");
                        registry.remove(file.top(), false);
                        break;
                    }
                    case "created": {
                        var file = (ISourceLocation) c.get("file");
                        if (registry.exists(file)) {
                            registry.setLastModified(file, System.currentTimeMillis());
                        } else {
                            try (var out = registry.getCharacterWriter(file.top(), registry.detectCharset(file).name(), false)) {
                                out.write("");
                            }
                        }
                        break;
                    }
                    case "renamed": {
                        var from = (ISourceLocation) c.get("from");
                        var to = (ISourceLocation) c.get("to");
                        registry.rename(from.top(), to.top(), true);
                        break;
                    }
                    case "changed": {
                        var file = (ISourceLocation) c.get("file");
                        if (c.has("edits")) {
                            var textEdits = (IList) c.get("edits");
                            var charset = registry.detectCharset(file).name();
                            var contents = Prelude.readFile(vf, false, ((ISourceLocation) c.get("file")).top(), charset, false);
                            for (var e : textEdits.reverse()) {
                                var edit = (IConstructor) e;
                                var range = (ISourceLocation) edit.get("range");
                                var prefix = contents.substring(0, range.getOffset());
                                var replacement = (IString) edit.get("replacement");
                                var postfix = contents.substring(range.getOffset() + range.getLength());
                                contents = prefix.concat(replacement).concat(postfix);
                            };
                            try (var writer = registry.getCharacterWriter(file.top(), charset, false)) {
                                contents.write(writer);
                            }
                        } else {
                            registry.setLastModified(file, System.currentTimeMillis());
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                services.warning("Could not execute FileSystemChange due to " + e.getMessage(), URIUtil.rootLocation("unknown"));
            }
        });
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
