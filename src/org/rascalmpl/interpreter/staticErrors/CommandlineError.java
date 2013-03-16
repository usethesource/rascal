package org.rascalmpl.interpreter.staticErrors;

import java.util.List;

import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.env.KeywordParameter;
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
    List<KeywordParameter> kwps = main.getKeywordParameterDefaults();
    
    if (kwps.size() > 1) {
      b.append(" [options]\n\nOptions:\n");
    
      for (KeywordParameter param : kwps) {
        b.append("\t-");
        b.append(param.getName());
        if (param.getType().isSubtypeOf(tf.boolType())) {
          b.append("\t[arg]: one of nothing (true), \'1\', \'0\', \'true\' or \'false\';\n");
        }
        else {
          b.append("\t[arg]: " + param.getType() + " argument;\n");
        }
      }
    }
    else {
      b.append('\n');
    }
    
    return b.toString();
  }
}
