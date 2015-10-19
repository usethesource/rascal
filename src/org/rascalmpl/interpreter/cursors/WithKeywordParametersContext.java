package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.library.util.Cursor;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.IWithKeywordParameters;

public class WithKeywordParametersContext extends Context {
	private Context ctx;
	private String label;
	private IWithKeywordParameters<? extends IConstructor> kwp;

	public WithKeywordParametersContext(Context ctx, String label, IWithKeywordParameters<? extends IConstructor> kwp) {
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
