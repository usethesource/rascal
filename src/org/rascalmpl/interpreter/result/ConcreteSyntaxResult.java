package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class ConcreteSyntaxResult extends ConstructorResult {

	public ConcreteSyntaxResult(Type type, IConstructor cons,
			IEvaluatorContext ctx) {
		super(type, cons, ctx);
	}
	
	@Override
	public <U extends IValue> Result<U> is(Name name) {
		if (TreeAdapter.isAppl(getValue())) {
			String consName = TreeAdapter.getConstructorName(getValue());
			if (consName != null) {
				return ResultFactory.bool(Names.name(name).equals(consName), ctx);
			}
		}
		return ResultFactory.bool(false, ctx);
	}
	
	@Override
	public <U extends IValue> Result<U> has(Name name) {
		if (TreeAdapter.isAppl(getValue())) {
			IConstructor prod = TreeAdapter.getProduction(getValue());
			IList syms = ProductionAdapter.getLhs(prod);
			String tmp = Names.name(name);
			
			// TODO: find deeper into optionals, checking the actual arguments for presence/absence of optional trees.
			for (IValue sym : syms) {
				if (SymbolAdapter.isLabel((IConstructor) sym)) {
					if (SymbolAdapter.getLabel((IConstructor) sym).equals(tmp)) {
						return ResultFactory.bool(true, ctx);
					}
				}
			}
		}
		return ResultFactory.bool(false, ctx); 
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToConcreteSyntax(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToConcreteSyntax(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToConcreteSyntax(
			ConcreteSyntaxResult that) {
		return equalToConcreteSyntax(that).negate();
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToConcreteSyntax(ConcreteSyntaxResult that) {
		IConstructor left = this.getValue();
		IConstructor right = that.getValue();
		
		if (TreeAdapter.isLayout(left) && TreeAdapter.isLayout(right)) {
			return bool(true, ctx);
		}
		
		if (TreeAdapter.isAppl(left) && TreeAdapter.isAppl(right)) {
			IConstructor p1 = TreeAdapter.getProduction(left);
			IConstructor p2 = TreeAdapter.getProduction(right);
			
			if (!p1.isEqual(p2)) {
				return bool(false, ctx);
			}
			
			IList l1 = TreeAdapter.getArgs(left);
			IList l2 = TreeAdapter.getArgs(right);
			
			if (l1.length() != l2.length()) {
				return bool(false, ctx);
			}
			for (int i = 0; i < l1.length(); i++) {
				IValue kid1 = l1.get(i);
				IValue kid2 = l2.get(i);
				// Recurse here on kids to reuse layout handling etc.
				Result<IBool> result = makeResult(kid1.getType(), kid1, ctx).equals(makeResult(kid2.getType(), kid2, ctx));
				if (!result.getValue().getValue()) {
					return bool(false, ctx);
				}
				if (TreeAdapter.isContextFree(left)) {
					i++; // skip layout
				}
			}
			return bool(true, ctx);
		}
		
		
		if (TreeAdapter.isChar(left) && TreeAdapter.isChar(right)) {
			return bool((TreeAdapter.getCharacter(left) == TreeAdapter.getCharacter(right)), ctx);
		}
		
		if (TreeAdapter.isAmb(left) && TreeAdapter.isAmb(right)) {
			ISet alts1 = TreeAdapter.getAlternatives(left);
			ISet alts2 = TreeAdapter.getAlternatives(right);

			if (alts1.size() != alts2.size()) {
				return bool(false, ctx);
			}
			
			// TODO: this is very inefficient
			again: for (IValue alt1: alts1) {
				for (IValue alt2: alts2) {
					Result<IBool> result = makeResult(alt1.getType(), alt1, ctx).equals(makeResult(alt2.getType(), alt2, ctx));
					if (result.getValue().getValue()) {
						// As soon an alt1 is equal to an alt2
						// continue the outer loop.
						continue again;
					}
				}
				// If an alt1 is not equal to any of the the alt2's return false;
				return bool(false, ctx);
			}
			return bool(true, ctx);
		}

		return bool(false, ctx);
	}

}
