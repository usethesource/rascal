package org.rascalmpl.interpreter.result;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;

public interface IRascalResult {
  Type getType();
  IValue getValue();
}
