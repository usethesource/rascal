package org.rascalmpl.semantics.dynamic;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IValue;

public class ListComprehensionWriter extends ComprehensionWriter {


	public ListComprehensionWriter(java.util.List<Expression> resultExprs,
			IEvaluator<Result<IValue>> __eval) {
		super(resultExprs, __eval);
		this.writer = VF.listWriter();
		this.elementType1 = TF.voidType();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void append() {
		for (Expression resExpr : this.resultExprs) {
			if(resExpr.isSplice() || resExpr.isSplicePlus()){
				Result<IValue> list = resExpr.getArgument().interpret(this.ev);
				if (list.getType().isList() || list.getType().isSet()) {
					elementType1 = elementType1.lub(list.getType().getElementType());
					((IListWriter)writer).appendAll((Iterable<IValue>)list.getValue());
				}
				else {
					// original code supported slicing on no list?
					elementType1 = elementType1.lub(list.getType());
					((IListWriter)writer).append(list.getValue());
				}
			}
			else {
				Result<IValue> res = resExpr.interpret(this.ev);
				elementType1 = elementType1.lub(res.getType());
				((IListWriter)writer).append(res.getValue());
			}
		}
	}

	@Override
	public Result<IValue> done() {
		return (this.writer == null) ? Comprehension.makeResult(TF
				.listType(TF.voidType()), VF.list(), this
				.getContext(this.resultExprs.get(0))) : Comprehension.makeResult(TF
				.listType(this.elementType1), this.writer.done(), this
				.getContext(this.resultExprs.get(0)));
	}
}