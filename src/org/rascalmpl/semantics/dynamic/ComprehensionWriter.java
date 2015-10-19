package org.rascalmpl.semantics.dynamic;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.IWriter;
import org.rascalmpl.value.type.TypeFactory;

public abstract class ComprehensionWriter {
	protected org.rascalmpl.value.type.Type elementType1;
	protected org.rascalmpl.value.type.Type elementType2;
	protected org.rascalmpl.value.type.Type resultType;
	protected final java.util.List<Expression> resultExprs;
	protected IWriter writer;
	protected final org.rascalmpl.interpreter.IEvaluator<Result<IValue>> ev;
	protected final TypeFactory TF;
	protected final IValueFactory VF;

	ComprehensionWriter(java.util.List<Expression> resultExprs,
			IEvaluator<Result<IValue>> ev) {
		this.ev = ev;
		this.resultExprs = resultExprs;
		this.writer = null;
		this.TF = Evaluator.__getTf();
		this.VF = ev.__getVf();
	}

	public void check(Result<IValue> r,
			org.rascalmpl.value.type.Type t, String kind,
			Expression expr) {
		if (!r.getType().isSubtypeOf(t)) {
			throw new UnexpectedType(t, r.getType(), expr);
		}
	}

	public IEvaluatorContext getContext(AbstractAST ast) {
		ev.setCurrentAST(ast);
		return ev;
	}

	public abstract void append();

	public abstract Result<IValue> done();
}