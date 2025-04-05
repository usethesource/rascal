package org.rascalmpl.parser.uptr.action;

import java.util.Map;

import org.rascalmpl.interpreter.result.JavaMethod;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class JavaFunctionActionExecutor extends AbstractFunctionActionExecutor {
    private final JavaMethod method;
    private final IValue[] extraArgs;
    private final Map<String,IValue> kwParams;

    public JavaFunctionActionExecutor(JavaMethod method, IValue[] extraArgs, Map<String,IValue> kwParams) {
        this.method = method;
        this.extraArgs = extraArgs;
        this.kwParams = kwParams;
    }

    @Override
    public ITree filter(ITree tree) {
        Type kwTypes = method.getFunctionType().getKeywordParameterTypes();
		int amountOfKWArguments = kwTypes.getArity();

        IValue[] args = new IValue[1 + extraArgs.length + amountOfKWArguments];

        int index = 0;
        args[index++] = tree;
        for (int i=0; i<extraArgs.length; i++) {
            args[index++] = extraArgs[i];
        }

        for (int i=0; i<amountOfKWArguments; i++) {
            String name = kwTypes.getFieldName(i);
            IValue value = kwParams.get(name);
            if (value == null) {
                throw new RuntimeException("Missing keyword argument: " + name);
            }
            args[index++] = value;
        }

        return (ITree) method.invoke(args);
    }

    @Override
    public boolean isImpure(Object rhs) {
        return false;
    }
}
