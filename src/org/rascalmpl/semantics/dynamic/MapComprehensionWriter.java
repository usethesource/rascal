package org.rascalmpl.semantics.dynamic;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.IValue;

public class MapComprehensionWriter extends ComprehensionWriter {

	public MapComprehensionWriter(java.util.List<Expression> resultExprs,
			IEvaluator<Result<IValue>> eval) {
		super(resultExprs, eval);
		if (resultExprs.size() != 2)
			throw new ImplementationError(
					"Map comprehensions needs two result expressions");
		this.writer = VF.mapWriter();
		this.elementType1 = TF.voidType();
		this.elementType2 = TF.voidType();
	}

	@Override
	public void append() {
		Result<IValue> r1 = this.resultExprs.get(0).interpret(this.ev);
		Result<IValue> r2 = this.resultExprs.get(1).interpret(this.ev);
		elementType1 = elementType1.lub(r1.getStaticType());
		elementType2 = elementType2.lub(r2.getStaticType());
		((IMapWriter) this.writer).put(r1.getValue(), r2.getValue());
	}

	@Override
	public Result<IValue> done() {
		return (this.writer == null) ? Comprehension.makeResult(TF.mapType(TF.voidType(),
				TF.voidType()), VF.mapWriter().done(), this
				.getContext(this.resultExprs.get(0))) : Comprehension.makeResult(TF
				.mapType(this.elementType1, this.elementType2), this.writer
				.done(), this.getContext(this.resultExprs.get(0)));
	}
}