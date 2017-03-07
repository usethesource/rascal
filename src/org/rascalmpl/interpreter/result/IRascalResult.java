package org.rascalmpl.interpreter.result;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public interface IRascalResult {
  Type getType();
  IValue getValue();
}
