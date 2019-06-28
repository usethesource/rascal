package org.rascalmpl.interpreter.utils;

import java.util.Map;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.ArgumentMismatch;
import org.rascalmpl.interpreter.staticErrors.UndeclaredFunction;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.impl.fields.AbstractValueFactoryAdapter;
import io.usethesource.vallang.type.Type;

/**
 * Using this ValueFactory, constructor functions will be called in the current scope instead
 * of direct construction. This way Rascal functions can override constructor functions, even
 * when terms are constructed by library functions such as IO::readFile.
 */
public final class NormalFormValueFactory extends AbstractValueFactoryAdapter {
  private final IEvaluatorContext ctx;

  public NormalFormValueFactory(IValueFactory factory, IEvaluatorContext ctx) {
    super(factory);
    this.ctx = ctx;
  }

  @Override
  public IConstructor constructor(Type cons) {
    try {
      return (IConstructor) ctx.getEvaluator().call(cons.getAbstractDataType().getName(), cons.getName());
    }
    catch (UndeclaredFunction | ArgumentMismatch e) {
      // TODO this makes this very robust, but also may hide issues. Not sure what is best here yet.
      return adapted.constructor(cons);
    }
  }

  @Override
  public IConstructor constructor(Type cons, IValue... children) throws FactTypeUseException {
    try {
      return (IConstructor) ctx.getEvaluator().call(cons.getAbstractDataType().getName(), cons.getName(), children);
    }
    catch (UndeclaredFunction | ArgumentMismatch e) {
      // TODO this makes this very robust, but also may hide issues. Not sure what is best here yet.
      return adapted.constructor(cons, children);
    }
  }

  @Override
  public IConstructor constructor(Type cons, Map<String, IValue> annotations, IValue... children)
      throws FactTypeUseException {
    try {
      IConstructor result = (IConstructor) ctx.getEvaluator().call(cons.getAbstractDataType().getName(), cons.getName(), children);
      return result.asAnnotatable().setAnnotations(annotations);
    }
    catch (UndeclaredFunction | ArgumentMismatch e) {
      // TODO this makes this very robust, but also may hide issues. Not sure what is best here yet.
      return adapted.constructor(cons, annotations, children);
    }
  }
}
