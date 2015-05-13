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

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.FunctionModifier;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Signature;
import org.rascalmpl.ast.Tag;
import org.rascalmpl.ast.TagString;
import org.rascalmpl.ast.Tags;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.util.MemoizationCache;
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
    protected final Map<String, IValue> tags;

    private SoftReference<MemoizationCache> memoization;
    protected final boolean hasMemoization;

    public NamedFunction(AbstractAST ast, IEvaluator<Result<IValue>> eval, FunctionType functionType, List<KeywordFormal> initializers, String name,
            boolean varargs, boolean isDefault, boolean isTest, Environment env) {
        super(ast, eval, functionType, initializers, varargs, env);
        this.name = name;
        this.isDefault = isDefault;
        this.isTest = isTest;
        this.isStatic = env.isRootScope() && eval.__getRootScope() != env;

        if (ast instanceof FunctionDeclaration) {
            tags = parseTags((FunctionDeclaration) ast);
            this.resourceScheme = getResourceScheme((FunctionDeclaration) ast);
            this.resolverScheme = getResolverScheme((FunctionDeclaration) ast);
            this.hasMemoization = checkMemoization((FunctionDeclaration) ast);
        } else {
            tags = new HashMap<String, IValue>();
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

    protected static String getResourceScheme(FunctionDeclaration declaration) {
        return getScheme(RESOURCE_TAG, declaration);
    }

    protected static String getResolverScheme(FunctionDeclaration declaration) {
        return getScheme(RESOLVER_TAG, declaration);
    }

    protected boolean checkMemoization(FunctionDeclaration func) {
        for (Tag tag : func.getTags().getTags()) {
            if (Names.name(tag.getName()).equalsIgnoreCase("memo")) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasMemoization() {
        return hasMemoization;
    }

    protected Map<String, IValue> parseTags(FunctionDeclaration declaration) {
        final Map<String, IValue> result = new HashMap<String, IValue>();
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

        if(keywordParameterDefaults != null){
            sep = (strFormals.length() > 0) ? ", " : "";

            for(String kw : keywordParameterDefaults.keySet()){
                kwFormals += sep + functionType.getKeywordParameterType(kw) + " " + kw + "= ...";
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
