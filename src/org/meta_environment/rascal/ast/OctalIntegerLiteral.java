package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class OctalIntegerLiteral extends AbstractAST
{
  static public class Lexical extends OctalIntegerLiteral
  {
    private String string;
    /*package */ Lexical (ITree tree, String string)
    {
      this.tree = tree;
      this.string = string;
    }
    public String getString ()
    {
      return string;
    }
  }
  static public class Ambiguity extends OctalIntegerLiteral
  {
    private final java.util.LisT <
      org.meta_environment.rascal.ast.OctalIntegerLiteral > alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.OctalIntegerLiteral >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT <
      org.meta_environment.rascal.ast.OctalIntegerLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
