/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression.Closure;
import org.rascalmpl.ast.Expression.VoidClosure;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.FunctionModifier;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.Parameters;
import org.rascalmpl.ast.Signature;
import org.rascalmpl.ast.Tag;
import org.rascalmpl.ast.TagString;
import org.rascalmpl.ast.Tags;
import org.rascalmpl.ast.Variant;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.KeywordParameter;
import org.rascalmpl.interpreter.result.util.MemoizationCache;
import org.rascalmpl.interpreter.staticErrors.NoKeywordParameters;
import org.rascalmpl.interpreter.staticErrors.UndeclaredKeywordParameter;
import org.rascalmpl.interpreter.staticErrors.UnexpectedKeywordArgumentType;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.utils.Names;

abstract public class NamedFunction extends AbstractFunction {
	private static final String RESOURCE_TAG = "resource";
  private static final String RESOLVER_TAG = "resolver";
  protected final String name;
  protected final boolean isDefault;
  protected final boolean isTest;
  protected final boolean isStatic;
  protected final String resourceScheme;
  protected final String resolverScheme;
  protected final Map<String, String> tags;
	protected final Type[] keywordParameterTypes;
	private SoftReference<MemoizationCache> memoization;
  protected final boolean hasMemoization;
  
	public NamedFunction(AbstractAST ast, IEvaluator<Result<IValue>> eval, FunctionType functionType, String name,
			boolean varargs, boolean isDefault, boolean isTest, List<KeywordParameter> keyargs, Environment env) {
		super(ast, eval, functionType, varargs, (keyargs == null) ? computeKeywordParameterDefaults(ast, eval) : keyargs, env);
		this.name = name;
		this.isDefault = isDefault;
		this.isTest = isTest;
		this.hasKeyArgs = keywordParameterDefaults != null && keywordParameterDefaults.size() > 0;
		this.keywordParameterTypes = computeKeywordParameterTypes(ast, eval);
	  this.isStatic = env.isRootScope() && eval.__getRootScope() != env;
	  
		if (ast instanceof FunctionDeclaration) {
      tags = parseTags((FunctionDeclaration) ast);
      this.resourceScheme = getResourceScheme((FunctionDeclaration) ast);
      this.resolverScheme = getResolverScheme((FunctionDeclaration) ast);
      this.hasMemoization = checkMemoization((FunctionDeclaration) ast);
    } else {
      tags = new HashMap<String, String>();
      this.resourceScheme = null;
      this.resolverScheme = null;
      this.hasMemoization = false;
    }
	}

	protected static boolean hasTestMod(Signature sig) {
	  for (FunctionModifier m : sig.getModifiers().getModifiers()) {
	    if (m.isTest()) {
	      return true;
	    }
	  }

	  return false;
	}
	 
	@Override
	public String getName() {
		return name;
	}
	
	protected Result<IValue> getMemoizedResult(IValue[] argValues, Map<String, IValue> keyArgValues) {
		if (hasMemoization()) {
			MemoizationCache memoizationActual = getMemoizationCache(false);
			if (memoizationActual == null) {
				return null;
			}
			return memoizationActual.getStoredResult(argValues, keyArgValues);
		}
		return null;
	}

	private MemoizationCache getMemoizationCache(boolean returnFresh) {
		MemoizationCache result = null;
		if (memoization == null) {
			result = new MemoizationCache();
			memoization = new SoftReference<>(result);
			return returnFresh ? result : null;

		}
		result = memoization.get();
		if (result == null ) {
			result = new MemoizationCache();
			memoization = new SoftReference<>(result);
			return returnFresh ? result : null;
		}
		return result;
	}
	
	protected Result<IValue> storeMemoizedResult(IValue[] argValues, Map<String, IValue> keyArgValues, Result<IValue> result) {
		if (hasMemoization()) {
			getMemoizationCache(true).storeResult(argValues, keyArgValues, result);
		}
		return result;
	}
	
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues,
			Map<String, IValue> keyArgValues) throws MatchFailed {
		Result<IValue> result = getMemoizedResult(argValues, keyArgValues);
		if (result == null) {
			result = super.call(argTypes, argValues, keyArgValues);
			storeMemoizedResult(argValues, keyArgValues, result);
		}
		return result;
	}
	
	
	private static List<KeywordFormal> getKeywordDefaults(AbstractAST ast){
	 
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
	
	private static List<KeywordParameter> computeKeywordParameterDefaults(AbstractAST ast, IEvaluator<Result<IValue>> eval){
		LinkedList<KeywordParameter> kwdefaults = null;

		List<KeywordFormal> kwformals = getKeywordDefaults(ast);
		
		if(kwformals != null && kwformals.size() > 0){
			kwdefaults = new LinkedList<KeywordParameter>();
			
			for(int i = 0; i < kwformals.size(); i++){
				KeywordFormal kwf = kwformals.get(i);
				Result<IValue> r = kwf.getExpression().interpret(eval);
				Type kwType = kwf.getType().typeOf(eval.getCurrentEnvt(), true);
        if(!r.getType().isSubtypeOf(kwType)) {
					throw new UnexpectedKeywordArgumentType(Names.name(kwf.getName()), kwType, r.getType(), ast);
				}
				kwdefaults.add(new KeywordParameter(Names.name(kwf.getName()), kwType, r));
			}
		}
		return kwdefaults;
	}
	
	private static Type[] computeKeywordParameterTypes(AbstractAST ast, IEvaluator<Result<IValue>> eval){
    List<KeywordFormal> kwformals = getKeywordDefaults(ast);
    
    if (kwformals == null) {
      return null;
    }
    
    Type[] kwTypes = new Type[kwformals.size()];
    
    if(kwformals != null && kwformals.size() > 0){
      for(int i = 0; i < kwformals.size(); i++){
        KeywordFormal kwf = kwformals.get(i);
        kwTypes[i] = kwf.getType().typeOf(eval.getCurrentEnvt(), true);
      }
    }
    return kwTypes;
  }
	
	protected static String getResourceScheme(FunctionDeclaration declaration) {
  	return getScheme(RESOURCE_TAG, declaration);
  }

  protected static String getResolverScheme(FunctionDeclaration declaration) {
  	return getScheme(RESOLVER_TAG, declaration);
  }

  protected boolean checkMemoization(FunctionDeclaration func) {
  	for (Tag tag : func.getTags().getTags()) {
  		if (Names.name(tag.getName()).equals("memo")) {
  			return true;
  		}
  	}
  	return false;
  }

  protected boolean hasMemoization() {
    return hasMemoization;
  }

  protected Map<String, String> parseTags(FunctionDeclaration declaration) {
  	Map<String, String> result = new HashMap<String, String>();
  	Tags tags = declaration.getTags();
  	if (tags.hasTags()) {
  		for (Tag tag : tags.getTags()) {
  			if(tag.hasContents()){
  				String key = Names.name(tag.getName());
  				String value = ((TagString.Lexical) tag.getContents()).getString();
  				if (value.length() > 2 && value.startsWith("{")) {
  					value = value.substring(1, value.length() - 1);
  				}
  				result.put(key, value);
  			}
  		}
  	}
  	return result;
  }

  @Override
  public String getTag(String key) {
  	return tags.get(key);
  }

  @Override
  public boolean hasTag(String key) {
  	return tags.containsKey(key);
  }

  private static String getScheme(String schemeTag, FunctionDeclaration declaration) {
  	Tags tags = declaration.getTags();
  	
  	if (tags.hasTags()) {
  		for (Tag tag : tags.getTags()) {
  			if (Names.name(tag.getName()).equals(schemeTag)) {
  				String contents = ((TagString.Lexical) tag.getContents()).getString();
  				
  				if (contents.length() > 2 && contents.startsWith("{")) {
  					contents = contents.substring(1, contents.length() - 1);
  				}
  				return contents;
  			}
  		}
  	}
  	
  	return null;
  }

  protected static boolean isDefault(FunctionDeclaration func) {
  	List<FunctionModifier> mods = func.getSignature().getModifiers().getModifiers();
  	for (FunctionModifier m : mods) {
  		if (m.isDefault()) {
  			return true;
  		}
  	}
  	return false;
  }

  protected void bindKeywordArgs(Map<String, IValue> keyArgValues){
    Environment env = ctx.getCurrentEnvt();
    if(keyArgValues.isEmpty()){
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
        IValue r = keyArgValues.get(kwparam);
        if(!r.getType().isSubtypeOf(keywordParameterTypes[k])){
          throw new UnexpectedKeywordArgumentType(kwparam, keywordParameterTypes[k], r.getType(), ctx.getCurrentAST());
        }
        env.declareVariable(keywordParameterTypes[k], kwparam);
        env.storeVariable(kwparam, ResultFactory.makeResult(keywordParameterTypes[k], r, ctx));
      } else {
        Result<IValue> r = kw.getDefault();
        env.declareVariable(r.getType(), kwparam);
        env.storeVariable(kwparam, r);
      }
      k++;
    }
    if(nBoundKeywordArgs != keyArgValues.size()){
      main:for (String kwparam : keyArgValues.keySet()) {
        for (KeywordParameter kw : keywordParameterDefaults){
          if (kwparam.equals(kw.getName())) {
              continue main;
          }
          throw new UndeclaredKeywordParameter(getName(), kwparam, ctx.getCurrentAST());
        }
      }
    }
  }
	
	@Override
  public boolean isTest() {
  	return isTest;
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

  @Override
  public boolean isDefault() {
  	return isDefault;
  }

  @Override
  public String getResourceScheme() {
  	return this.resourceScheme;
  }

  @Override
  public boolean hasResourceScheme() {
  	return this.resourceScheme != null;
  }

  @Override
  public boolean hasResolverScheme() {
  	return this.resolverScheme != null;
  }

  @Override
  public String getResolverScheme() {
  	return this.resolverScheme;
  }

}
