/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression.Closure;
import org.rascalmpl.ast.Expression.VoidClosure;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.Parameters;
import org.rascalmpl.ast.Variant;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.KeywordParameter;
import org.rascalmpl.interpreter.env.Pair;
import org.rascalmpl.interpreter.staticErrors.NoKeywordParameters;
import org.rascalmpl.interpreter.staticErrors.UndeclaredKeywordParameter;
import org.rascalmpl.interpreter.staticErrors.UnexpectedKeywordArgumentType;
import org.rascalmpl.interpreter.types.FunctionType;

abstract public class NamedFunction extends AbstractFunction {
	protected final String name;
	//protected final List<Pair<String, Result<IValue>>> keywordParameterDefaults;
	protected Type keywordParameterTypes[];
	
	public NamedFunction(AbstractAST ast, IEvaluator<Result<IValue>> eval, FunctionType functionType, String name,
			boolean varargs, List<KeywordParameter> keyargs, Environment env) {
		super(ast, eval, functionType, varargs, keyargs, env);
		this.name = name;
		this.keywordParameterDefaults = (keyargs == null) ? computeKeywordParameterDefaults() : keyargs;
		this.hasKeyArgs = keywordParameterDefaults != null && keywordParameterDefaults.size() > 0;
	}

	@Override
	public String getName() {
		return name;
	}
	
	private List<KeywordFormal> getKeywordDefaults(){
		//System.err.println(getName() + ast.getClass());
		Parameters params = null;
		if (ast instanceof FunctionDeclaration) {
			params = ((FunctionDeclaration) ast).getSignature().getParameters();
		}
		else if(ast instanceof Variant){
			Variant var = ((Variant) ast);
			if(var.getKeywordArguments().isDefault()){
				return var.getKeywordArguments().getKeywordFormalList();
			}	
		}
		else if (ast instanceof Closure) {
			params = ((Closure) ast).getParameters();
		}
		else if (ast instanceof VoidClosure) {
			params = ((VoidClosure) ast).getParameters();
		}
		else {
			throw new ImplementationError("Unexpected kind of Rascal function: " + ast);
		}
		if(params != null){
			if(params.getKeywordFormals().isDefault())
				return params.getKeywordFormals().getKeywordFormalList();
		}
		return null;
	}
	
	private List<KeywordParameter> computeKeywordParameterDefaults(){
		LinkedList<KeywordParameter> kwdefaults = null;

		List<KeywordFormal> kwformals = getKeywordDefaults();
		
		if(kwformals != null && kwformals.size() > 0){
			keywordParameterTypes = new Type[kwformals.size()];
			kwdefaults = new LinkedList<KeywordParameter>();
			
			for(int i = 0; i < kwformals.size(); i++){
				KeywordFormal kwf = kwformals.get(i);
				keywordParameterTypes[i] = kwf.getType().typeOf(eval.getCurrentEnvt());
				Result<IValue> r = kwf.getExpression().interpret(this.eval);
				if(!r.getType().isSubtypeOf(keywordParameterTypes[i])){
					throw new UnexpectedKeywordArgumentType(kwf.getName().toString(), keywordParameterTypes[i], r.getType(), ast);
				}
				kwdefaults.add(new KeywordParameter(kwf.getName().toString(), keywordParameterTypes[i], r));
			}
		}
		return kwdefaults;
//		if(kwdefaults == null){
//			return kwInherited;
//		} else {
//			if(kwInherited != null)
//				kwdefaults.addAll(kwInherited);
//			return kwdefaults;
//		}
	}
	
	protected void bindKeywordArgs(Map<String, Result<IValue>> keyArgValues){
		Environment env = ctx.getCurrentEnvt();
		if(keyArgValues == null){
			if(keywordParameterDefaults != null){
				for(KeywordParameter pair : keywordParameterDefaults){
					String kwparam= pair.getName();
					Result<IValue> r = pair.getDefault();
					env.declareVariable(r.getType(), kwparam);
					env.storeVariable(kwparam,r);
				}
			}
			return;
		}
		if(keywordParameterDefaults == null)
			throw new NoKeywordParameters(getName(), ctx.getCurrentAST());
		
		int nBoundKeywordArgs = 0;
		int k = 0;
		for(KeywordParameter kw: keywordParameterDefaults){
			String kwparam = kw.getName();
			if(keyArgValues.containsKey(kwparam)){
				nBoundKeywordArgs++;
				Result<IValue> r = keyArgValues.get(kwparam);
				if(!r.getType().isSubtypeOf(keywordParameterTypes[k])){
					throw new UnexpectedKeywordArgumentType(kwparam, keywordParameterTypes[k], r.getType(), ctx.getCurrentAST());
				}
				env.declareVariable(r.getType(), kwparam);
				env.storeVariable(kwparam, r);
			} else {
				Result<IValue> r = kw.getDefault();
				env.declareVariable(r.getType(), kwparam);
				env.storeVariable(kwparam, r);
			}
			k++;
		}
		if(nBoundKeywordArgs != keyArgValues.size()){
			main:
			for(String kwparam : keyArgValues.keySet())
				for(KeywordParameter kw : keywordParameterDefaults){
					if(kwparam.equals(kw.getName()))
							continue main;
					throw new UndeclaredKeywordParameter(getName(), kwparam, ctx.getCurrentAST());
				}
		}
	}
	
	public String getHeader(){
		String sep = "";
		String strFormals = "";
		for(Type tp : getFormals()){
			strFormals = strFormals + sep + tp;
			sep = ", ";
		}
		
		String name = getName();
		if (name == null) {
			name = "";
		}
		
		
		String kwFormals = "";
		
		if(keywordParameterDefaults != null){
			sep = (strFormals.length() > 0) ? ", " : "";
				
			for(KeywordParameter kw : keywordParameterDefaults){
				Result<IValue> r = kw.getDefault();
				kwFormals += sep + r.getType() + " " + kw.getName() + "=" + r.getValue();
				sep = ", ";
			}
		}
		
		return getReturnType() + " " + name + "(" + strFormals + kwFormals + ")";
	}

}
