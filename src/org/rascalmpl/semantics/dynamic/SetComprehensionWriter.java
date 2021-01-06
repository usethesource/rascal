package org.rascalmpl.semantics.dynamic;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.NonVoidTypeRequired;

import io.usethesource.vallang.IValue;

public class SetComprehensionWriter extends ComprehensionWriter {

	public SetComprehensionWriter(java.util.List<Expression> resultExprs,
			IEvaluator<Result<IValue>> ev) {
		super(resultExprs, ev);
		this.writer = VF.setWriter();
		this.elementType1 = TF.voidType();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void append() {
		for (Expression resExpr : this.resultExprs) {
			if(resExpr.isSplice() || resExpr.isSplicePlus()){
				Result<IValue> set = resExpr.getArgument().interpret(this.ev);
				if (set.getStaticType().isSet() || set.getStaticType().isList()) {
					elementType1 = elementType1.lub(set.getStaticType().getElementType());
					writer.insertAll((Iterable<IValue>)set.getValue());
				}
				else {
					// original code supported slicing on no set?
					elementType1 = elementType1.lub(set.getStaticType());
					writer.insert(set.getValue());
				}
			}
			else {
				Result<IValue> res = resExpr.interpret(this.ev);
				
				if (res == null || res.getStaticType().isBottom()) {
                    throw new NonVoidTypeRequired(ev.getCurrentAST());
                }
				else {
				    elementType1 = elementType1.lub(res.getStaticType());
				    writer.insert(res.getValue());
				}
			}
		}
	}

	@Override
	public Result<IValue> done() {
		return (this.writer == null) ? Comprehension.makeResult(
				TF.setType(TF.voidType()), VF.set(), this
						.getContext(this.resultExprs.get(0))) : Comprehension.makeResult(
				TF.setType(this.elementType1), this.writer.done(), this
						.getContext(this.resultExprs.get(0)));
	}
}