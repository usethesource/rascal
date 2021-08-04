package org.rascalmpl.library.util;

import org.rascalmpl.ideservices.IDEServices;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValueFactory;

public class IDEServicesLibrary {
    private final IDEServices services;
    private final IValueFactory values;

    public IDEServicesLibrary(IDEServices services, IValueFactory vf) {
        this.services = services;
        this.values = vf;
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

	
}
