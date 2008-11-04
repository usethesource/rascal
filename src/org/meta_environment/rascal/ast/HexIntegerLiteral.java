package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class HexIntegerLiteral extends AbstractAST
{
  static public class Lexical extends HexIntegerLiteral
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
  static public class Ambiguity extends HexIntegerLiteral
  {
    private final java.util.LisT <
      org.meta_environment.rascal.ast.HexIntegerLiteral > alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.HexIntegerLiteral >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT <
      org.meta_environment.rascal.ast.HexIntegerLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
