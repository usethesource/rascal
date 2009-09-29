package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.Evaluator;
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
		this.re = new TraversalEvaluator(eval);
	}
	
	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals) {
		IConstructor prod = (IConstructor) actuals[0];
		IList args = (IList) actuals[1];
		
		if (ProductionAdapter.isList(prod)) {
			actuals[1] = flatten(prod, args);
		}
		
		IConstructor newAppl = (IConstructor) Factory.Tree_Appl.make(getValueFactory(), actuals);
		
	    NonTerminalType concreteType = (NonTerminalType) RascalTypeFactory.getInstance().nonTerminalType(newAppl);
	    
		return makeResult(concreteType, re.applyRules(concreteType, newAppl), ctx);
	}

	private IValue flatten(IConstructor prod, IList args) {
		IListWriter result = Factory.Args.writer(vf);
		int delta = getDelta(prod);
		
		for (int i = 0; i < args.length(); i++) {
			IConstructor tree = (IConstructor) args.get(i);
			if (TreeAdapter.isList(tree) && TreeAdapter.isAppl(tree)) {
				if (shouldFlatten(TreeAdapter.getProduction(tree), prod)) {
					IList nestedArgs = TreeAdapter.getArgs(tree);
					if (nestedArgs.length() > 0) {
						result.appendAll(nestedArgs);
					}
					else {
						// skip following separators
						i += delta;
					}
				}
				else {
					result.append(tree);
				}
			}
			else {
				result.append(tree);
			}
		}
		
		return result.done();
	}

	private boolean shouldFlatten(IConstructor nested, IConstructor surrounding) {
		if (ProductionAdapter.isList(nested)) {
			IConstructor nestedRhs = ProductionAdapter.getRhs(nested);
			IConstructor surroundingRhs = ProductionAdapter.getRhs(surrounding);
			
			if (surroundingRhs.isEqual(nestedRhs)) {
				return true;
			}
			
			if ((SymbolAdapter.isCf(surroundingRhs) && SymbolAdapter.isCf(nestedRhs)) || (SymbolAdapter.isLex(surroundingRhs) && SymbolAdapter.isLex(nestedRhs))) {
				nestedRhs = SymbolAdapter.getSymbol(nestedRhs);
				surroundingRhs = SymbolAdapter.getSymbol(surroundingRhs);
			}
			
			if ((SymbolAdapter.isIterPlusSep(surroundingRhs) && SymbolAdapter.isIterStarSep(nestedRhs)) || (SymbolAdapter.isIterStarSep(surroundingRhs) && SymbolAdapter.isIterPlusSep(nestedRhs))) {
				return SymbolAdapter.getSymbol(surroundingRhs).equals(SymbolAdapter.getSymbol(nestedRhs)) && SymbolAdapter.getSeparator(surroundingRhs).equals(SymbolAdapter.getSeparator(nestedRhs));
			}

			if ((SymbolAdapter.isIterPlus(surroundingRhs) && SymbolAdapter.isIterStar(nestedRhs)) || (SymbolAdapter.isIterStar(surroundingRhs) && SymbolAdapter.isIterPlus(nestedRhs))) {
				return SymbolAdapter.getSymbol(surroundingRhs).equals(SymbolAdapter.getSymbol(nestedRhs));
			}
		}
		return false;
	}

	private int getDelta(IConstructor prod) {
		IConstructor rhs = ProductionAdapter.getRhs(prod);
		
		if (SymbolAdapter.isLex(rhs)) {
			rhs = SymbolAdapter.getSymbol(rhs);
			
			if (SymbolAdapter.isIterPlusSep(rhs) || SymbolAdapter.isIterStarSep(rhs)) {
				return 1;
			}
			return 0;
		}
		else if (SymbolAdapter.isCf(rhs)) {
			rhs = SymbolAdapter.getSymbol(rhs);

			if (SymbolAdapter.isIterPlusSep(rhs) || SymbolAdapter.isIterStarSep(rhs)) {
				return 3;
			}
			return 1;
		}
		else {
			return 0;
		}
	}
}
