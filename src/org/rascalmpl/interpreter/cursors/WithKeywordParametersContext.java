package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.library.util.Cursor;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.IWithKeywordParameters;

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
