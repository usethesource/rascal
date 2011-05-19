/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.result.uptr.action;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.LanguageAction;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.Insert;
import org.rascalmpl.interpreter.control_exceptions.Return;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.parser.IParserInfo;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.action.IEnvironment;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

/**
 * This class filters a parse forest using parser actions declared in the grammar.
 * 
 * It depends on many features of the Rascal implementation, including the parser generator and the kind of code it generates.
 */
public class RascalActionExecutor implements IActionExecutor{
	private final Evaluator eval;
	private final Environment rootEnvironment;
	private final IParserInfo info;
	
	public RascalActionExecutor(Evaluator eval, Environment rootEnvironment, IParserInfo info) {
		this.eval = eval;
		this.rootEnvironment = rootEnvironment;
		this.info = info;
	}
	
	public IEnvironment createRootEnvironment(){
		return rootEnvironment;
	}
	
	public void completed(IEnvironment environment, boolean filtered){
		eval.setCurrentEnvt(rootEnvironment); // The given environment is the rootEnvironment.
	}
	
	public IEnvironment enteringProduction(IConstructor production, IEnvironment environment){
		LanguageAction action = info.getAction(production);
		if(action != null){
			// TODO: Store the module names with the actions in the grammar representation
			String modName = eval.getHeap().getModuleForURI(action.getLocation().getURI());
			ModuleEnvironment scope;
			if(modName != null){
				scope = eval.getHeap().getModule(modName);
			}else{
				scope = new ModuleEnvironment("***no module environment for action***", eval.getHeap());
			}
			Environment env = new Environment(scope, (Environment) environment, null, action.getLocation(), "Production");
			eval.setCurrentEnvt(env);
			
			return env;
		}
		
		return environment; // Reuse the environment if there are no actions associated with the given production.
	}
	
	public IEnvironment enteringNode(IConstructor production, int index, IEnvironment environment){
		// TODO Implement.
		
		return environment; // Temp.
	}
	
	public void exitedProduction(IConstructor production, boolean filtered, IEnvironment environment){
		// TODO Implement.
	}
	
	public IConstructor filterProduction(IConstructor forest, IEnvironment environment){
		if (TreeAdapter.isAppl(forest)){ 
			IConstructor production = TreeAdapter.getProduction(forest);
			LanguageAction action = info.getAction(production);
			if(action != null){
				return call(forest, action);
			}
		}
		
		return forest;
	}
	
	public IConstructor filterAmbiguity(IConstructor ambCluster, IEnvironment environment){
		// TODO Implement.
		return ambCluster;
	}
	
	public IConstructor filterCycle(IConstructor cycle, IEnvironment environment){
		// TODO Implement.
		return cycle;
	}
	
	public boolean mayHaveSideEffects(IConstructor rhs){
		// TODO Implement.
		return true; // Return true for now, so at least we don't break anything.
	}

	/**
	 * call takes care of executing an action and knowing whether something changed, and the scope management.
	 */
	private IConstructor call(IConstructor tree, LanguageAction action){
		AbstractAST oldAST = eval.getCurrentAST();
		
		try{
			eval.setCurrentAST(action);
			assignItAndFields(tree);
			
			// TODO: replace when
			// TODO: when
			if(action.isReplace()){
				// TODO add type checking
				eval.setCurrentAST(action.getExpression());
				return (IConstructor) action.getExpression().interpret(eval).getValue();
			}
			for(Statement s : action.getStatements()){
				eval.setCurrentAST(s);
				s.interpret(eval);
			}
			
			// nothing happens to the tree, but side-effects may have occurred
			return tree;
		}catch(Insert e){
			// TODO add type checking!
			return (IConstructor) e.getValue().getValue();
		}catch(Return e){
			// TODO add type checking!
		    return (IConstructor) e.getValue().getValue();	
		}catch(Failure e){
			return null;
		}finally{
			eval.setCurrentAST(oldAST);
		}
	}

	private void assignItAndFields(IConstructor tree){
		IConstructor prod = TreeAdapter.getProduction(tree);
		Type nonTerminalType = RascalTypeFactory.getInstance().nonTerminalType(ProductionAdapter.getRhs(prod));
		
		eval.getCurrentEnvt().storeVariable(Evaluator.IT, makeResult(nonTerminalType, tree, eval));
		
		IList args = TreeAdapter.getArgs(tree);
		IList lhs = ProductionAdapter.getLhs(prod);
		for (int i = lhs.length() - 1; i >= 0; --i){
			IConstructor sym = (IConstructor) lhs.get(i);
			if (SymbolAdapter.isLabel(sym)){
				Type argType = RascalTypeFactory.getInstance().nonTerminalType(SymbolAdapter.getLabeledSymbol(sym));
				Result<IValue> val = makeResult(argType, args.get(i), eval);
				eval.getCurrentEnvt().storeVariable(SymbolAdapter.getLabelName(sym), val);
			}
		}
	}
}
