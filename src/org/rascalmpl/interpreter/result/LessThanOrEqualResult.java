package org.rascalmpl.interpreter.result;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class LessThanOrEqualResult extends BoolResult {
  private final boolean less;
  private final boolean equal;

  public LessThanOrEqualResult(boolean less, boolean equal, IEvaluatorContext ctx) {
    super(TypeFactory.getInstance().boolType(), ctx.getValueFactory().bool(less || equal), ctx);
    this.less = less;
    this.equal = equal;
  }
  
  public boolean getLess() {
    return less;
  }
  
  public boolean getEqual() {
    return equal;
  }
  
  public Result<IBool> isLess() {
    return ResultFactory.bool(less, ctx);
  }
  
  public Result<IBool> isEqual() {
    return ResultFactory.bool(equal, ctx);
  }
}
