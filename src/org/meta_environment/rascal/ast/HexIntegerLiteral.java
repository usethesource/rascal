package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class HexIntegerLiteral extends AbstractAST
{
  public class Lexical extends HexIntegerLiteral
  {
    /* [0] [xX] [0-9a-fA-F]+ -> HexIntegerLiteral  */
  }
  public class Ambiguity extends HexIntegerLiteral
  {
    private final java.util.List < HexIntegerLiteral > alternatives;
    public Ambiguity (java.util.List < HexIntegerLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < HexIntegerLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
