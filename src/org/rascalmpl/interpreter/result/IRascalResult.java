package org.rascalmpl.interpreter.result;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public interface IRascalResult {
  Type getStaticType();
  
  default Type getDynamicType() {
    return getValue().getType();
  }
  
  default Type getStaticUnaliasedType() {
      Type result = getStaticType();
      
      while (result.isAliased()) {
          result = result.getAliased();
      }
      
      return result;
  }
  
  IValue getValue();
}
