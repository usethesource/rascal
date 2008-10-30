package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class HexLongLiteral extends AbstractAST
{
  public class Lexical extends HexLongLiteral
  {
    /* [0] [xX] [0-9a-fA-F]+ [lL] -> HexLongLiteral  */
  }
  public class Ambiguity extends HexLongLiteral
  {
    private final List < HexLongLiteral > alternatives;
    public Ambiguity (List < HexLongLiteral > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < HexLongLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
