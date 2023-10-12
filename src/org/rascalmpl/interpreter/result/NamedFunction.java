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
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.FunctionModifier;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Signature;
import org.rascalmpl.ast.Tag;
import org.rascalmpl.ast.TagString;
import org.rascalmpl.ast.Tags;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.util.ExpiringFunctionResultCache;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

abstract public class NamedFunction extends AbstractFunction {
    private static final String RESOURCE_TAG = "resource";
    private static final String RESOLVER_TAG = "resolver";
    protected final String name;
    protected final boolean isDefault;
    protected final boolean isTest;
    protected final boolean isStatic;
    protected final String resourceScheme;
    protected final String resolverScheme;
    protected final Map<String, IValue> tags;

    private SoftReference<ExpiringFunctionResultCache<Result<IValue>>> memoization;
    protected final boolean hasMemoization;
    private final int memoizationTimeout;
    private final int memoizationMaxEntries;

    public NamedFunction(AbstractAST ast, IEvaluator<Result<IValue>> eval, Type functionType, Type dynamicType, List<KeywordFormal> initializers, String name,
            boolean varargs, boolean isDefault, boolean isTest, Environment env) {
        super(ast, eval, functionType, dynamicType, initializers, varargs, env);
        this.name = name;
        this.isDefault = isDefault;
        this.isTest = isTest;
        this.isStatic = env.isRootScope() && eval.__getRootScope() != env;

        if (ast instanceof FunctionDeclaration) {
            tags = parseTags((FunctionDeclaration) ast);
            this.resourceScheme = getResourceScheme((FunctionDeclaration) ast);
            this.resolverScheme = getResolverScheme((FunctionDeclaration) ast);
            IValue memoTag = tags.get("memo");
            if (memoTag != null) {
                this.hasMemoization = true;
                if (!(memoTag instanceof ISet)) {
                    memoTag = vf.set(memoTag);
                }
                this.memoizationTimeout = getMemoizationTimeout((ISet)memoTag);
                this.memoizationMaxEntries = getMemoizationMaxEntries((ISet)memoTag);
            }
            else {
                this.hasMemoization = false;
                this.memoizationTimeout = (int) TimeUnit.HOURS.toSeconds(1);
                this.memoizationMaxEntries = -1;
            }
        } else {
            tags = new HashMap<String, IValue>();
            this.resourceScheme = null;
            this.resolverScheme = null;
            this.hasMemoization = false;
            this.memoizationTimeout = 0;
            this.memoizationMaxEntries = 0;
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

    public void clearMemoizationCache() {
        if (memoization != null) {
            ExpiringFunctionResultCache<Result<IValue>> m = memoization.get();
            
            if (m != null) {
                m.clear();
                memoization.clear();
            }

            memoization = null;
        }
    }
    
    protected Result<IValue> getMemoizedResult(IValue[] argValues, Map<String, IValue> keyArgValues) {
        if (hasMemoization()) {
            ExpiringFunctionResultCache<Result<IValue>> memoizationActual = getMemoizationCache(false);
            return memoizationActual == null ? null : memoizationActual.lookup(argValues, keyArgValues);
        }
        return null;
    }

    private ExpiringFunctionResultCache<Result<IValue>> getMemoizationCache(boolean returnFresh) {
        ExpiringFunctionResultCache<Result<IValue>> result = memoization == null ? null : memoization.get();
        if (result == null && returnFresh) {
            result = new ExpiringFunctionResultCache<Result<IValue>>(memoizationTimeout, memoizationMaxEntries);
            memoization = new SoftReference<>(result);
            return result;
        }
        return result;
    }

    protected Result<IValue> storeMemoizedResult(IValue[] argValues, Map<String, IValue> keyArgValues, Result<IValue> result) {
        if (hasMemoization()) {
            return getMemoizationCache(true).store(argValues, keyArgValues, result);
        }
        return result;
    }


    @Override
    public Result<IValue> call(Type[] argTypes, IValue[] argValues,
            Map<String, IValue> keyArgValues) throws MatchFailed {
        Result<IValue> result = getMemoizedResult(argValues, keyArgValues);
        if (result == null) {
            result = super.call(argTypes, argValues, keyArgValues);
            return storeMemoizedResult(argValues, keyArgValues, result);
        }
        return result;
    }
    
    protected void checkReturnTypeIsNotVoid(List<Expression> formals, IValue[] actuals, Map<Type,Type> renamings) {
        // this is a dynamic check of the formal type parameters
        Map<Type, Type> bindings = new HashMap<>();
        
        for (int i = 0; i < actuals.length; i++) {
            formals.get(i).typeOf(declarationEnvironment, getEval(), false).match(renameType(actuals[i].getType(), renamings, Integer.toString(ctx.getCurrentAST().getLocation().getOffset())), bindings);
        }
        
        if (!getReturnType().isBottom() && getReturnType().instantiate(bindings).isBottom()) {
            // this means the function has a type parameter &T, but would return `void`
            // and since Rascal does not have a "null" value, this function alternative is unapplicable
            // to the current parameters.
            throw new MatchFailed();
        }
    }

    protected static String getResourceScheme(FunctionDeclaration declaration) {
        return getScheme(RESOURCE_TAG, declaration);
    }

    protected static String getResolverScheme(FunctionDeclaration declaration) {
        return getScheme(RESOLVER_TAG, declaration);
    }

    protected Tag checkMemoization(FunctionDeclaration func) {
        for (Tag tag : func.getTags().getTags()) {
            if (Names.name(tag.getName()).equalsIgnoreCase("memo")) {
                return tag;
            }
        }
        return null;
    }
    private int getMemoizationTimeout(ISet memoTags) {
        for (IValue memoTag : memoTags) {
            if (memoTag instanceof IConstructor && ((IConstructor) memoTag).getName().equals("expireAfter")) {
                Map<String, IValue> kwParams = ((IConstructor)memoTag).asWithKeywordParameters().getParameters();
                IValue value = kwParams.get("seconds");
                if (value instanceof IInteger) {
                    return ((IInteger)value).intValue();
                }
                value = kwParams.get("minutes");
                if (value instanceof IInteger) {
                    return (int) TimeUnit.MINUTES.toSeconds(((IInteger)value).intValue());
                }
                value = kwParams.get("hours");
                if (value instanceof IInteger) {
                    return (int) TimeUnit.HOURS.toSeconds(((IInteger)value).intValue());
                }
            }
        }
        return (int) TimeUnit.HOURS.toSeconds(1);
    }
    
    private int getMemoizationMaxEntries(ISet memoTags) {
        for (IValue memoTag : memoTags) {
            if (memoTag instanceof IConstructor && ((IConstructor) memoTag).getName().equals("maximumSize")) {
                IValue value = ((IConstructor)memoTag).get("entries");
                if (value instanceof IInteger) {
                    return ((IInteger)value).intValue();
                }
            }
        }
        return -1;
    }
    
    
    protected int computeIndexedPosition(AbstractAST node) {
    	if (ast instanceof FunctionDeclaration) {
    		FunctionDeclaration ast = (FunctionDeclaration) node;
    		for (Tag tag : ast.getTags().getTags()) {
    			if (Names.name(tag.getName()).equalsIgnoreCase("index") && tag.hasExpression()) {
    				IInteger anno = (IInteger) tag.getExpression().interpret(getEval()).getValue();
    				int pos = anno.intValue();

    				if (pos >= 0 && pos < 10) {
    					return pos;
    				}
    				else {
    					throw new ImplementationError("indexing only supported for argument 0 to 9");
    				}
    			}
    		}
    	}
        
        
        // fall back to first if no annotation is provided
        return 0;
    }

    public boolean hasMemoization() {
        return hasMemoization;
    }

    protected Map<String, IValue> parseTags(FunctionDeclaration declaration) {
        final Map<String, IValue> result = new HashMap<>();
        Tags tags = declaration.getTags();
        if (tags.hasTags()) {
            for (Tag tag : tags.getTags()) {
                final String key = Names.name(tag.getName());
                result.put(key, tag.accept(new NullASTVisitor<IValue>() {
                    @Override
                    public IValue visitTagDefault(Tag.Default x) {
                        String value = ((TagString.Lexical) x.getContents()).getString();
                        value = value.substring(1, value.length() - 1);
                        return vf.string(value);
                    }

                    @Override
                    public IValue visitTagEmpty(Tag.Empty x) { 
                        return vf.string("");
                    }

                    @Override
                    public IValue visitTagExpression(Tag.Expression x) { 
                        return x.getExpression().interpret(eval).getValue(); 
                    }
                }));
            }
        }
        return result;
    }

    @Override
    public IValue getTag(String key) {
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

        if(keywordParameterDefaults != null && staticFunctionType.hasKeywordParameters()){
            sep = (strFormals.length() > 0) ? ", " : "";

            for(String kw : keywordParameterDefaults.keySet()){
                kwFormals += sep + staticFunctionType.getKeywordParameterType(kw) + " " + kw + "= ...";
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
