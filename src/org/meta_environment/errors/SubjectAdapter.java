package org.meta_environment.errors;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.ValueFactoryFactory;

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
			return org.meta_environment.locations.Factory.getInstance().toSourceLocation(ValueFactoryFactory.getValueFactory(), (IConstructor) subject.get("location"));
		}
		return null;
	}
	
	public String getPath() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getURL().getPath();
		}
		return null;
	}
	
	public int getStartColumn() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getBeginColumn();
		}
		return 0;
	}
	
	public int getEndColumn() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getEndColumn();
		}
		return 0;
	}
	
	public int getStartLine() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getBeginLine();
		}
		return 1;
	}
	
	public int getEndLine() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getEndLine();
		}
		return 1;
	}
	
	public int getOffset() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getOffset();
		}
		return 0;
	}
	
	public int getLength() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getLength();
		}
		return 0;
	}
	
	public String getDescription() {
		return ((IString) subject.get("description")).getValue();
	}
}
