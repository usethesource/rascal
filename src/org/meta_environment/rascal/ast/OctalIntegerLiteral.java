package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class OctalIntegerLiteral extends AbstractAST
{
  static public class Lexical extends OctalIntegerLiteral
  {
    /* [0] [0-7]+ -> OctalIntegerLiteral  */
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
  static public class Ambiguity extends OctalIntegerLiteral
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.OctalIntegerLiteral > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.OctalIntegerLiteral >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List <
      org.meta_environment.rascal.ast.OctalIntegerLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
