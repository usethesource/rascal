package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.uptr.ProductionAdapter;
import org.meta_environment.uptr.TreeAdapter;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;

public class ConcreteSyntaxResult extends ConstructorResult {

	public ConcreteSyntaxResult(Type type, IConstructor cons,
			EvaluatorContext ctx) {
		super(type, cons, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, EvaluatorContext ctx) {
		return that.equalToConcreteSyntax(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, EvaluatorContext ctx) {
		return that.nonEqualToConcreteSyntax(this, ctx);
	}
	
	private boolean isLayout(IValue v){
		if(v instanceof IConstructor){
			IConstructor cons = (IConstructor) v;
			return cons.getName().equals("layout");
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <U extends IValue> Result<U> equalToConcreteSyntax(
			ConcreteSyntaxResult that, EvaluatorContext ctx) {
		IConstructor left = this.getValue();
		IConstructor right = that.getValue();
		
		TreeAdapter t1 = new TreeAdapter(left);
		TreeAdapter t2 = new TreeAdapter(right);
		
		if (t1.isLayout() && t2.isLayout()) {
			return bool(true);
		}
		
		if (t1.isAppl() && t2.isAppl()) {
			ProductionAdapter p1 = t1.getProduction();
			ProductionAdapter p2 = t2.getProduction();
			
			// NB: using ordinary equals here...
			if (!p1.getRhs().getTree().equals(p2.getRhs().getTree())) {
				return bool(false);
			}
			
			if (!t1.isList() && !t2.isList()) {
				if (!p1.getLhs().equals(p2.getLhs())) {
					return bool(false);
				}
			}
			
			if (t1.isList() && !t2.isList()) {
				return bool(false);
			}
			
			if (t2.isList() && !t1.isList()) {
				return bool(false);
			}
			
			if (!p1.getAttributes().equals(p2.getAttributes())) {
				// TODO: attrs are unordered, no?
				return bool(false);
			}
		
			IList l1 = t1.getArgs();
			IList l2 = t2.getArgs();
			
			if (t1.isList() && t2.isList()) {
				if (l1.length() != l2.length()) {
					return bool(false);
				}
			}
			
			for (int i = 0; i < l1.length(); i++) {
				IValue kid1 = l1.get(i);
				IValue kid2 = l2.get(i);
				// Recurse here on kids to reuse layout handling etc.
				Result<IBool> result = makeResult(kid1.getType(), kid1, ctx)
					.equals(makeResult(kid2.getType(), kid2, ctx), ctx);
				if (!result.getValue().getValue()) {
					return bool(false);
				}
				if (t1.isContextFree()) {
					i++; // skip layout
				}
			}
			return bool(true);
		}
		
		
		if (t1.isChar() && t2.isChar()) {
			return bool(t1.getCharacter() == t2.getCharacter());
		}
		
		if (t1.isAmb() && t2.isAmb()) {
			ISet alts1 = t1.getAlternatives();
			ISet alts2 = t2.getAlternatives();

			// TODO: this is very inefficient
			again: for (IValue alt1: alts1) {
				for (IValue alt2: alts2) {
					Result<IBool> result = makeResult(alt1.getType(), alt1, ctx)
						.equals(makeResult(alt2.getType(), alt2, ctx), ctx);
					if (result.getValue().getValue()) {
						// As soon an alt1 is equal to an alt2
						// continue the outer loop.
						continue again;
					}
				}
				// If an alt1 is not equal to any of the the alt2's return false;
				return bool(false);
			}
			return bool(true);
		}

		if (left.getType().equals(right.getType())) {
			throw new ImplementationError("Missing case in equals of concrete syntax");
		}
		
		return bool(false);
		
//		if(!left.getName().equals(right.getName()) || !left.getName().equals("appl")) {
//			throw new ImplementationError("names unequal or not 'appl'");
//		}
//		int i = 0;
//		for (IValue leftKid: left.getChildren()) {
//			IValue rightKid = right.get(i);
//			i++;
//			if(isLayout(leftKid) && isLayout(rightKid))
//				continue;
//			if(!leftKid.equals(rightKid))
//				return (Result<U>) new BoolResult(false, ctx);
//		}
//		return (Result<U>) new BoolResult(true, ctx);
	}

}
