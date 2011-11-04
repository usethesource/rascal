package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.NonVoidTypeRequired;

public class MapComprehensionWriter extends ComprehensionWriter {

	public MapComprehensionWriter(java.util.List<Expression> resultExprs,
			org.rascalmpl.interpreter.Evaluator ev) {
		super(resultExprs, ev);
		if (resultExprs.size() != 2)
			throw new ImplementationError(
					"Map comprehensions needs two result expressions");
	}

	@Override
	public void append() {
		Result<IValue> r1 = this.resultExprs.get(0).interpret(this.ev);
		Result<IValue> r2 = this.resultExprs.get(1).interpret(this.ev);
		
		if (r1.getType().isVoidType()) {
			throw new NonVoidTypeRequired(resultExprs.get(0));
		}
		if (r2.getType().isVoidType()) {
			throw new NonVoidTypeRequired(resultExprs.get(1));
		}
		
		if (this.writer == null) {
			this.elementType1 = r1.getType();
			this.elementType2 = r2.getType();
			this.resultType = TF.mapType(this.elementType1,
					this.elementType2);
			this.writer = this.resultType.writer(VF);
		}
		this.check(r1, this.elementType1, "map", this.resultExprs.get(0));
		this.check(r2, this.elementType2, "map", this.resultExprs.get(1));
		((IMapWriter) this.writer).put(r1.getValue(), r2.getValue());
	}

	@Override
	public Result<IValue> done() {
		return (this.writer == null) ? Comprehension.makeResult(TF.mapType(TF.voidType(),
				TF.voidType()), VF.map(TF.voidType(), TF.voidType()), this
				.getContext(this.resultExprs.get(0))) : Comprehension.makeResult(TF
				.mapType(this.elementType1, this.elementType2), this.writer
				.done(), this.getContext(this.resultExprs.get(0)));
	}
}