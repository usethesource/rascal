package org.rascalmpl.interpreter.utils;

import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.impl.AbstractValueFactoryAdapter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.staticErrors.ArgumentsMismatch;
import org.rascalmpl.interpreter.staticErrors.UndeclaredFunction;
import org.rascalmpl.values.IRascalValueFactory;

/**
 * Using this ValueFactory, constructor functions will be called in the current scope instead
 * of direct construction. This way Rascal functions can override constructor functions, even
 * when terms are constructed by library functions such as IO::readFile.
 */
public final class NormalFormValueFactory extends AbstractValueFactoryAdapter {
  private final IEvaluatorContext ctx;

  public NormalFormValueFactory(IRascalValueFactory factory, IEvaluatorContext ctx) {
    super(factory);
    this.ctx = ctx;
  }

  @Override
  public IConstructor constructor(Type cons) {
    try {
      return (IConstructor) ctx.getEvaluator().call(cons.getAbstractDataType().getName(), cons.getName());
    }
    catch (UndeclaredFunction | ArgumentsMismatch e) {
      // TODO this makes this very robust, but also may hide issues. Not sure what is best here yet.
      return adapted.constructor(cons);
    }
  }

  @Override
  public IConstructor constructor(Type cons, IValue... children) throws FactTypeUseException {
    try {
      return (IConstructor) ctx.getEvaluator().call(cons.getAbstractDataType().getName(), cons.getName(), children);
    }
    catch (UndeclaredFunction | ArgumentsMismatch e) {
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
    catch (UndeclaredFunction | ArgumentsMismatch e) {
      // TODO this makes this very robust, but also may hide issues. Not sure what is best here yet.
      return adapted.constructor(cons, annotations, children);
    }
  }
}
