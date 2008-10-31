package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class HexLongLiteral extends AbstractAST
{
  static public class Lexical extends HexLongLiteral
  {
    /* [0] [xX] [0-9a-fA-F]+ [lL] -> HexLongLiteral  */
  }
  public class Ambiguity extends HexLongLiteral
  {
    private final java.util.List < HexLongLiteral > alternatives;
    public Ambiguity (java.util.List < HexLongLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < HexLongLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
