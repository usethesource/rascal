package org.rascalmpl.library.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.JavaMethod;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.FunctionType;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class CurriedFunction extends AbstractFunction {
    private final IFunction func;
    private final IValue[] extraArgs;
    private final Map<String, IValue> keywordParams;

    public static CurriedFunction create(IEvaluator<Result<IValue>> eval, IFunction originalFunc, IValue extraArg, IMap kwParams) {
        Map<String, IValue> keywordParams = new HashMap<>();
        kwParams.entryIterator().forEachRemaining(entry -> {
            String name = ((IString) entry.getKey()).getValue();
            IValue value = entry.getValue();
            keywordParams.put(name, value);
        });
    
        Type functionType = createCurriedFunctionType(originalFunc, extraArg, keywordParams);
        Environment env = eval.getCurrentEnvt();
        Type dynamicFunctionType = functionType.instantiate(env.getDynamicTypeBindings());
        List<KeywordFormal> initializers = Collections.emptyList(); // TODO: handle keyword parameter initializers correctly

        IValue[] extraArgs = extraArg == null ? new IValue[0] : new IValue[] { extraArg };
        return new CurriedFunction(eval.getCurrentAST(), eval, functionType, dynamicFunctionType, initializers, eval.getCurrentEnvt(), originalFunc, extraArgs, keywordParams);
    }

    private static final Type createCurriedFunctionType(IFunction originalFunc, IValue extraArg, Map<String, IValue> fixedKewywordParams) {
        FunctionType originalType = (FunctionType) originalFunc.getType();

        //Type originalKeywordTypes = originalType.getKeywordParameterTypes();
        // TODO: check original keyword parameters against kwParams and handle initializers

        var typeFactory = TypeFactory.getInstance();

        Type paramTypes = originalType.getFieldTypes();
        Type newParamTypes;
        if (extraArg == null) {
            newParamTypes = paramTypes;
        } else {
            int arity = paramTypes.getArity();
            if (arity == 0) {
                throw new IllegalArgumentException("Cannot curry a function with arity 0");
            }

            Type[] newParamTypeArray = new Type[arity - 1];
            for (int i = 0; i < arity - 1; i++) {
                newParamTypeArray[i] = paramTypes.getFieldType(i);
            }
            newParamTypes = typeFactory.tupleType(newParamTypeArray);
        }

        Type keywordParamTypes = originalType.getKeywordParameterTypes();
        Type newKeywordParamTypes = keywordParamTypes;

        return typeFactory.functionType(originalType.getReturnType(), newParamTypes, newKeywordParamTypes);
    }

    private CurriedFunction(AbstractAST ast, IEvaluator<Result<IValue>> eval, Type functionType, Type dynamicFunctionType,
        List<KeywordFormal> initializers, Environment env, IFunction func, IValue[] extraArgs, Map<String, IValue> keywordParams) {
        super(ast, eval, functionType, dynamicFunctionType, initializers, false, env);

        this.func = func;
        this.extraArgs = extraArgs;
        this.keywordParams = keywordParams;
    }

    @Override
    public ICallableValue cloneInto(Environment env) {
        return new CurriedFunction(getAst(), eval, getFunctionType(), getType(), Collections.emptyList(), env, func, extraArgs, keywordParams);
    }

    @Override
    public Result<IValue> call(Type[] actualStaticTypes, IValue[] actuals, Map<String, IValue> keyArgValues) {
        IValue[] actualArgs;
        actualArgs = new IValue[actuals.length + extraArgs.length];
        int index = 0;
        for (int i=0; i < actuals.length; i++) {
            actualArgs[index++] = actuals[i];
        }
        for (int i=0; i<extraArgs.length; i++) {
            actualArgs[index++] = extraArgs[i];
        }

        Environment env = ctx.getCurrentEnvt();
        Map<Type, Type> renamings = new HashMap<>();
        Map<Type, Type> dynamicRenamings = new HashMap<>();

        Type actualTypesTuple = TF.tupleType(actualStaticTypes);
        bindTypeParameters(actualTypesTuple, actualArgs, getFormals(), renamings, dynamicRenamings, env);

        Map<String,IValue> newKeyArgValues;
        if (keywordParams.isEmpty()) {
            newKeyArgValues = keyArgValues;
        } else {
            newKeyArgValues = new HashMap<>(keyArgValues);
            for (Map.Entry<String, IValue> entry : keywordParams.entrySet()) {
                newKeyArgValues.put(entry.getKey(), entry.getValue());
            }
        }

        IValue result = func.call(newKeyArgValues, actualArgs);

        Type resultType = getReturnType().instantiate(env.getStaticTypeBindings());
        resultType = unrenameType(renamings, resultType);

        return ResultFactory.makeResult(resultType, result, eval);
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    public IFunction getFunction() {
        return func;
    }

    
    public boolean isJavaFunction() {
        return func instanceof JavaMethod;
    }

    public IFunction getFunc() {
        return func;
    }

    public IValue[] getExtraArgs() {
        return extraArgs;
    }

    public Map<String, IValue> getKeywordParams() {
        return keywordParams;
    }
    
}

