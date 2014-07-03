package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
import org.rascalmpl.library.util.Cursor;

public class WithKeywordParametersContext extends Context {
	private Context ctx;
	private String label;
	private IWithKeywordParameters<IConstructor> kwp;

	public WithKeywordParametersContext(Context ctx, String label, IWithKeywordParameters<IConstructor> kwp) {
		this.ctx = ctx;
		this.label = label;
		this.kwp = kwp;
	}

	@Override
	public IValue up(IValue focus) {
		return new ConstructorCursor(kwp.setParameter(label, focus), ctx);
	}
	
	@Override
	public IList toPath(IValueFactory vf) {
		return ctx.toPath(vf).append(vf.constructor(Cursor.Nav_keywordParam, vf.string(label)));
	}

}
