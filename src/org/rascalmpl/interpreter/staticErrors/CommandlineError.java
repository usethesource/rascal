package org.rascalmpl.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.result.AbstractFunction;

public class CommandlineError extends RuntimeException {
  private static final long serialVersionUID = -5679812063174925071L;
  private final AbstractFunction main;
  
  public CommandlineError(String message, AbstractFunction main) {
    super(message);
    this.main = main;
  }
  
  public String help(String command) {
    TypeFactory tf = TypeFactory.getInstance();
    StringBuilder b = new StringBuilder();
    
    b.append("Usage: ");
    b.append(command);
    
    Type kwargs = main.getKeywordArgumentTypes(main.getEnv());
    
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
