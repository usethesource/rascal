package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.TraversalEvaluator;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.types.NonTerminalType;
import org.meta_environment.rascal.interpreter.types.RascalTypeFactory;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.ProductionAdapter;
import org.meta_environment.uptr.SymbolAdapter;
import org.meta_environment.uptr.TreeAdapter;

public class ConcreteConstructorFunction extends ConstructorFunction {
	private final TraversalEvaluator re;

	public ConcreteConstructorFunction(AbstractAST ast, Evaluator eval,
			Environment env) {
		super(ast, eval, env, Factory.Tree_Appl);
		this.re = new TraversalEvaluator(eval.vf, eval);
	}
	
	@Override
	public Result<?> call(Type[] actualTypes, IValue[] actuals,
			IEvaluatorContext ctx) {
		IValue prod = actuals[0];
		IList args = (IList) actuals[1];
		
		ProductionAdapter prodAdapter = new ProductionAdapter((IConstructor) prod);
		if (prodAdapter.isList()) {
			actuals[1] = flatten(prodAdapter, args);
		}
		
		IConstructor newAppl = (IConstructor) Factory.Tree_Appl.make(getValueFactory(), actuals);
		newAppl = newAppl.setAnnotation("loc", eval.getCurrentAST().getLocation());
		
		Result<?> appl = makeResult(Factory.Tree_Appl, newAppl, ctx);
	    NonTerminalType concreteType = (NonTerminalType) RascalTypeFactory.getInstance().nonTerminalType((IConstructor) appl.getValue());
	    
		return re.applyRules(ResultFactory.makeResult(concreteType, (IConstructor) appl.getValue(), ctx), ctx);
	}

	private IValue flatten(ProductionAdapter prodAdapter, IList args) {
		IListWriter result = Factory.Args.writer(VF);
		int delta = getDelta(prodAdapter);
		
		for (int i = 0; i < args.length(); i++) {
			IValue elem = args.get(i);
			TreeAdapter tree = new TreeAdapter((IConstructor) elem);
			if (tree.isList() && tree.isAppl()) {
				if (shouldFlatten(tree.getProduction(), prodAdapter)) {
					IList nestedArgs = tree.getArgs();
					if (nestedArgs.length() > 0) {
						result.appendAll(nestedArgs);
					}
					else {
						// skip following separators
						i += delta;
					}
				}
				else {
					result.append(elem);
				}
			}
			else {
				result.append(elem);
			}
		}
		
		return result.done();
	}

	private boolean shouldFlatten(ProductionAdapter nested, ProductionAdapter surrounding) {
		if (nested.isList()) {
			SymbolAdapter nestedRhs = nested.getRhs();
			SymbolAdapter surroundingRhs = surrounding.getRhs();
			
			if (surroundingRhs.getTree().isEqual(nestedRhs.getTree())) {
				return true;
			}
			
			if ((surroundingRhs.isCf() && nestedRhs.isCf()) || (surroundingRhs.isLex() && nestedRhs.isLex())) {
				nestedRhs = nestedRhs.getSymbol();
				surroundingRhs = surroundingRhs.getSymbol();
			}
			
			if ((surroundingRhs.isIterPlusSep() && nestedRhs.isIterStarSep()) || (surroundingRhs.isIterStarSep() && nestedRhs.isIterPlusSep())) {
				return surroundingRhs.getSymbol().equals(nestedRhs.getSymbol()) && surroundingRhs.getSeparator().equals(nestedRhs.getSeparator());
			}

			if ((surroundingRhs.isIterPlus() && nestedRhs.isIterStar()) || (surroundingRhs.isIterStar() && nestedRhs.isIterPlus())) {
				return surroundingRhs.getSymbol().equals(nestedRhs.getSymbol());
			}
		}
		return false;
	}

	private int getDelta(ProductionAdapter prodAdapter) {
		SymbolAdapter rhs = prodAdapter.getRhs();
		
		if (rhs.isLex()) {
			rhs = rhs.getSymbol();
			
			if (rhs.isIterPlusSep() || rhs.isIterStarSep()) {
				return 1;
			}
			return 0;
		}
		else if (rhs.isCf()) {
			rhs = rhs.getSymbol();

			if (rhs.isIterPlusSep() || rhs.isIterStarSep()) {
				return 3;
			}
			return 1;
		}
		else {
			return 0;
		}
	}
}
