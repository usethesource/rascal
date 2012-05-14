package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.result.Result;

public class ListComprehensionWriter extends ComprehensionWriter {


	public ListComprehensionWriter(java.util.List<Expression> resultExprs,
			org.rascalmpl.interpreter.Evaluator ev) {
		super(resultExprs, ev);
		this.writer = VF.listWriter();
		this.elementType1 = TF.voidType();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void append() {
		for (Expression resExpr : this.resultExprs) {
			if(resExpr.isSplice() || resExpr.isSplicePlus()){
				Result<IValue> list = resExpr.getArgument().interpret(this.ev);
				if (list.getType().isListType() || list.getType().isSetType()) {
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