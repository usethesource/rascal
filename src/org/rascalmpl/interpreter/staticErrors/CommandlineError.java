package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class CommandlineError extends RuntimeException {
  private static final long serialVersionUID = -5679812063174925071L;
  private final Type kwType;
  private final String toolName;
    
    public CommandlineError(String message, Type kwType, String name) {
      super(message);
      this.kwType = kwType;
      this.toolName = name;
  }
  
  public String help(String command) {
    TypeFactory tf = TypeFactory.getInstance();
    StringBuilder b = new StringBuilder();
    
    b.append("Usage: " + toolName);
    b.append(command);
    
    Type kwargs = kwType;
    
    if (kwargs.getArity() > 1) {
      b.append(" [options]\n\nOptions:\n");
    
      for (String param : kwargs.getFieldNames()) {
        b.append("\t-");
        b.append(param);
        if (kwargs.getFieldType(param).isSubtypeOf(tf.boolType())) {
          b.append("\t[arg]: one of nothing (true), \'1\', \'0\', \'true\' or \'false\';\n");
        }
        else {
          b.append("\t[arg]: " + kwargs.getFieldType(param) + " argument;\n");
        }
      }
    }
    else {
      b.append('\n');
    }
    
    return b.toString();
  }
}
