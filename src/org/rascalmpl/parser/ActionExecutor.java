package org.rascalmpl.parser;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
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
	
	public ActionExecutor(Evaluator eval, IParserInfo info) {
		this.eval = eval;
		this.info = info;
		this.cache = new HashMap<IConstructor,IConstructor>();
	}
	
	/**
	 * Executes all actions on a forest, possibly filtering it and/or having effect on the given scope.
	 */
	public IConstructor execute(IConstructor forest) {
		IConstructor result = rec(forest);
		if (result == null) {
			// TODO: proper error messaging
			throw new ImplementationError("all trees where filtered");
		}
		return result;
	}
	
	private IConstructor rec(IConstructor forest) {
		IConstructor result = cache.get(forest);
		if(result != null) return result;
		
		if(forest.getConstructorType() == Factory.Tree_Appl){
			result = recAppl(forest);
		}else if (forest.getConstructorType() == Factory.Tree_Amb){
			result = recAmb(forest);
		}else{
			result = forest;
		}
		
		cache.putUnsafe(forest, result);
		return result;
	}

	private IConstructor recAmb(IConstructor forest) {
		ISetWriter newAlternatives = eval.getValueFactory().setWriter(Factory.Tree);
		IConstructor only = null;
		boolean changed = false;
		
		for (IValue alt : TreeAdapter.getAlternatives(forest)) {
			IConstructor newAlt = rec((IConstructor) alt);
			
			if(newAlt != alt){
				changed = true;
			}
			
			if(newAlt != null){
				newAlternatives.insert(newAlt);
				only = newAlt;
			}
		}
		
		if(changed){
			switch(newAlternatives.size()){
				case 0: return null;
				case 1: return only;
				default:
					return forest.set("alternatives", newAlternatives.done());
			}
		}
		
		return forest;
	}

	private IConstructor recAppl(IConstructor forest) {
		IConstructor result;
		IList children = TreeAdapter.getArgs(forest);
		IListWriter newChildren = eval.getValueFactory().listWriter(Factory.Tree);
		boolean changed = false;
		boolean isList = TreeAdapter.isList(forest);
		IConstructor prod = TreeAdapter.getProduction(forest);
		
		for (IValue child : children) {
			IConstructor newChild = rec((IConstructor) child);
			
			if(newChild != child){
				if(newChild == null){
					return null;
				}
				
				changed = true;
			}
			
			if(!isList){
				newChildren.append(newChild);
			}else{
				if(TreeAdapter.isList(newChild) && ProductionAdapter.shouldFlatten(prod,TreeAdapter.getProduction(newChild))){
					newChildren.appendAll(TreeAdapter.getArgs(newChild));
				}else{
					newChildren.append(newChild);
				}
			}
		}
		
		LanguageAction action = info.getAction(prod);
		
		result = forest;
		if(changed){
			result = forest.set("args", newChildren.done());
		}
		
		if(action != null){
			result = call(result, action);
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
//				System.err.println("WARNING: could not retrieve a module environment for action for " + TreeAdapter.getProduction(tree));
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
			for (Statement s : action.getStatements()) {
				eval.setCurrentAST(s);
				s.accept(eval);
			}
			
			// nothing happens to the tree, but side-effects may have occurred
			return tree;
		}
		catch (Insert e) {
			// TODO add type checking!
			return (IConstructor) e.getValue().getValue();
		}
		catch (Return e) {
			// TODO add type checking!
		    return (IConstructor) e.getValue().getValue();	
		}
		catch (Failure e) {
			return null;
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
