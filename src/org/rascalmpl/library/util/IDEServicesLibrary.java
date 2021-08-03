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

	public void startJob(IString name) {
        services.startJob(name.getValue());
    }
	
	public void startJob(IString name, IInteger totalWork) {
        services.startJob(name.getValue(), totalWork.intValue());
    }

	public void startJob(IString name, IInteger workShare, IInteger totalWork) {
        services.startJob(name.getValue(), workShare.intValue(), totalWork.intValue());
    }
	
	public void event(IString name) {
        services.event(name.getValue());
    }
	
	public void event(IString name, IInteger inc) {
        services.event(name.getValue(), inc.intValue());
    }
	
	public void event(IInteger inc) {
        services.event(inc.intValue());
    }
	
	public IInteger endJob(IBool succeeded) {
        return values.integer(services.endJob(succeeded.getValue()));
    }
	
	/**
	 * @return True if cancellation has been requested for this job
	 */
	public IBool isCanceled() {
        return values.bool(services.isCanceled());
    }
	
	public void todo(IInteger work) {
        services.todo(work.intValue());
    }
	
	public void warning(IString message, ISourceLocation src) {
        services.warning(message.getValue(), src);
    }
}
