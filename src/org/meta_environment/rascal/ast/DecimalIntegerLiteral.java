package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class DecimalIntegerLiteral extends AbstractAST
{
  static public class Lexical extends DecimalIntegerLiteral
  {
    /* "0" -> DecimalIntegerLiteral  */
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
  static public class Ambiguity extends DecimalIntegerLiteral
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.DecimalIntegerLiteral > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.DecimalIntegerLiteral >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List <
      org.meta_environment.rascal.ast.DecimalIntegerLiteral >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
