package org.meta_environment.rascal.interpreter;

import org.meta_environment.rascal.ast.IASTVisitor;

/* interface used by the DebuggingDecorator */

public interface IEvaluator<T> extends IASTVisitor<T>, IEvaluatorContext {

}
