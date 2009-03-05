package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.type.Type;

public class SourceLocationResult extends AbstractResult {

	private ISourceLocation loc;

	protected SourceLocationResult(Type type, ISourceLocation loc) {
		super(type, loc);
		this.loc = loc;
	}

	@Override
	public ISourceLocation getValue() {
		return loc;
	}

}
