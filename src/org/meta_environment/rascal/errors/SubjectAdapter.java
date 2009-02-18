package org.meta_environment.rascal.errors;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.ISourceRange;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.ValueFactoryFactory;

public class SubjectAdapter {
	private IConstructor subject;
	
	public SubjectAdapter(IValue subject) {
		this.subject = (IConstructor) subject;
	}
	
	public boolean isLocalized() {
		return subject.getConstructorType() == Factory.Subject_Localized;
	}

	public ISourceLocation getLocation() {
		if (isLocalized()) {
			return org.meta_environment.rascal.locations.Factory.getInstance().toSourceLocation(ValueFactoryFactory.getValueFactory(), (IConstructor) subject.get("location"));
		}
		return null;
	}
	
	public ISourceRange getRange() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getRange();
		}
		return null;
	}
	
	public String getPath() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getPath();
		}
		return null;
	}
	
	public String getDescription() {
		return ((IString) subject.get("description")).getValue();
	}
}
