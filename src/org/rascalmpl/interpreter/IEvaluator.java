package org.rascalmpl.interpreter;

import org.rascalmpl.ast.IASTVisitor;

/* interface used by the DebuggingDecorator */

public interface IEvaluator<T> extends IASTVisitor<T>, IEvaluatorContext {

}
