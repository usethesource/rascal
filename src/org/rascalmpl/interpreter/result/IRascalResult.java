package org.rascalmpl.interpreter.result;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

public interface IRascalResult {
  Type getType();
  IValue getValue();
}
