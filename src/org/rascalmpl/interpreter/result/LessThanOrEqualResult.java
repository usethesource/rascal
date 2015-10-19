/*******************************************************************************
 * Copyright (c) 2012-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen Vinju - Jurgen.Vinju@cwi.nl - CWI
 *
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.type.TypeFactory;

public class LessThanOrEqualResult extends BoolResult {
  private final boolean less;
  private final boolean equal;

  public LessThanOrEqualResult(boolean less, boolean equal, IEvaluatorContext ctx) {
    super(TypeFactory.getInstance().boolType(), ctx.getValueFactory().bool(less || equal), ctx);
    this.less = less;
    this.equal = equal;
    if (less && equal) {
      throw new ImplementationError("something can not be both less and equal at the same time");
    }
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
