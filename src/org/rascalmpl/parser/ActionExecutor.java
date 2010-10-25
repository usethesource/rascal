package org.rascalmpl.parser;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.LanguageAction;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.Insert;
import org.rascalmpl.interpreter.control_exceptions.Return;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.parser.sgll.util.HashMap;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

/**
 * This class filters a parse forest using parser actions declared in the grammar.
 * 
 * It depends on many features of the Rascal implementation, including the parser generator and the kind of code it generates.
 */
public class ActionExecutor {
	private final Evaluator eval;
	private final IParserInfo info;
	private final HashMap<IConstructor, IConstructor> cache;
	private boolean changed = false;
	private static IConstructor filtered = (IConstructor) TypeFactory.getInstance().constructor(new TypeStore(Factory.uptr), Factory.Tree, "filtered").make(ValueFactoryFactory.getValueFactory());
	
	public ActionExecutor(Evaluator eval, IParserInfo info) {
		this.eval = eval;
		this.info = info;
		this.cache = new HashMap<IConstructor,IConstructor>();
	}
	
	/**
	 * Executes all actions on a forest, possibly filtering it and/or having effect on the given scope.
	 */
	public IConstructor execute(IConstructor forest) {
		if (forest.getConstructorType() == Factory.ParseTree_Top) {
			IConstructor result = (IConstructor) forest.get("top");
			result = rec(result);
			if (result == filtered) {
				// TODO: proper error messaging
				throw new ImplementationError("all trees where filtered");
			}
			return forest.set("top", result);
		}
		else {
			IConstructor result = rec(forest);
			if (result == filtered) {
				// TODO: proper error messaging
				throw new ImplementationError("all trees where filtered");
			}
			return result;
		}
	}
	
	private IConstructor rec(IConstructor forest) {
		IConstructor result = cache.get(forest);
		
		if (result != null) {
			return cache.get(forest);
		}
		
		if (forest.getConstructorType() == Factory.Tree_Appl) {
			result = recAppl(forest);
		}
		else if (forest.getConstructorType() == Factory.Tree_Amb) {
			result = recAmb(forest);
		}
		else {
			result = forest;
		}
		
		cache.putUnsafe(forest, result);
		return result;
	}

	private IConstructor recAmb(IConstructor forest) {
		ISetWriter newAlternatives = eval.getValueFactory().setWriter(Factory.Tree);
		boolean oneChanged = false;
		changed = false;
		
		for (IValue alt : TreeAdapter.getAlternatives(forest)) {
			IConstructor newAlt = rec((IConstructor) alt);
			if (changed) {
				oneChanged = true;
			}
			if (newAlt != filtered) {
				newAlternatives.insert(newAlt);
			}
			changed = false;
		}
		
		changed = oneChanged;
		
		if (changed) {
			if (newAlternatives.size() != 0) {
				return forest.set("alternatives", newAlternatives.done());
			}
			else {
				return filtered;
			}
		}
		else {
			return forest;
		}
	}

	private IConstructor recAppl(IConstructor forest) {
		IConstructor result;
		IList children = TreeAdapter.getArgs(forest);
		IListWriter newChildren = eval.getValueFactory().listWriter(Factory.Tree);
		boolean oneChanged = false;
		changed = false;
		
		for (IValue child : children) {
			IConstructor newChild = rec((IConstructor) child);
			if (newChild == filtered) {
				return filtered;
			}
			newChildren.append(newChild);
			if (changed) {
				oneChanged = true;
			}
			changed = false;
		}
		
		changed = oneChanged;
		
		IConstructor prod = TreeAdapter.getProduction(forest);
		LanguageAction action = info.getAction(prod);
		
		if (action != null) {
			if (changed) {
				result = call(forest.set("args", newChildren.done()), action);
			}
			else {
				result = call(forest, action);
			}
		}
		else if (changed) {
			result = forest.set("args", newChildren.done());
		}
		else {
			result = forest;
		}
		return result;
	}

	/**
	 * call takes care of executing an action and knowing whether something changed, and the scope management.
	 */
	private IConstructor call(IConstructor tree, LanguageAction action) {
		Environment old = eval.getCurrentEnvt();
		AbstractAST oldAST = eval.getCurrentAST();
		
		try {
			// TODO: remove this hack and rather store the module names with the actions in the grammar representation
			String modName = eval.getHeap().getModuleForURI(action.getLocation().getURI());
			ModuleEnvironment scope;
			if (modName != null) {
				scope = eval.getHeap().getModule(modName);
			}
			else {
				// TODO: see above, this should be fixed if every action knows to which module it belongs
				System.err.println("WARNING: could not retrieve a module environment for action for " + TreeAdapter.getProduction(tree));
				scope = new ModuleEnvironment("***no module environment for action***");
			}
			
			eval.setCurrentAST(action);
			eval.setCurrentEnvt(new Environment(scope, eval.getCurrentEnvt(), eval.getCurrentAST().getLocation(), action.getLocation(), "Production"));
			eval.pushEnv();
			assignItAndFields(tree);
			
			if (action.isBuild()) {
				// TODO add type checking
				eval.setCurrentAST(action.getExpression());
				return (IConstructor) action.getExpression().accept(eval).getValue();
			}
			else {
				for (Statement s : action.getStatements()) {
					eval.setCurrentAST(s);
					s.accept(eval);
				}
			}
			
			// nothing happens to the tree, but side-effects may have occurred
			return tree;
		}
		catch (Insert e) {
			// TODO add type checking!
			changed = true;
			return (IConstructor) e.getValue().getValue();
		}
		catch (Return e) {
			// TODO add type checking!
			changed = true;
		    return (IConstructor) e.getValue().getValue();	
		}
		catch (Failure e) {
			changed = true;
			return filtered;
		}
		finally {
			eval.setCurrentEnvt(old);
			eval.setCurrentAST(oldAST);
		}
	}

	private void assignItAndFields(IConstructor tree) {
		IConstructor prod = TreeAdapter.getProduction(tree);
		Type nonTerminalType = RascalTypeFactory.getInstance().nonTerminalType(ProductionAdapter.getRhs(prod));
		
		eval.getCurrentEnvt().storeVariable(Evaluator.IT, makeResult(nonTerminalType, tree, eval));
		
		IList args = TreeAdapter.getArgs(tree);
		IList lhs = ProductionAdapter.getLhs(prod);
		for (int i = 0; i < lhs.length(); i++) {
			IConstructor sym = (IConstructor) lhs.get(i);
			if (SymbolAdapter.isLabel(sym)) {
				Type argType = RascalTypeFactory.getInstance().nonTerminalType(SymbolAdapter.getSymbol(sym));
				Result<IValue> val = makeResult(argType, args.get(i), eval);
				eval.getCurrentEnvt().storeVariable(SymbolAdapter.getName(sym), val);
			}
		}
	}

}
