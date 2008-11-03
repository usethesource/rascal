package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class FloatingPointLiteral extends AbstractAST
{
  static public class Lexical extends FloatingPointLiteral
  {
    /* [0-9]+ "." [0-9]* ( [eE] [\+\-]? [0-9]+ )? [fF] -> FloatingPointLiteral  */
    private String string;
    /*package */ Lexical (ITree tree, String string)
    {
      this.tree = tree;
      this.string = arg;
    }
    public String getString ()
    {
      return string;
    }
  }
  static public class Ambiguity extends FloatingPointLiteral
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.FloatingPointLiteral > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.FloatingPointLiteral >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List <
      org.meta_environment.rascal.ast.FloatingPointLiteral >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
