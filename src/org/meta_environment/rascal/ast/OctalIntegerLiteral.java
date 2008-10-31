package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class OctalIntegerLiteral extends AbstractAST
{
  public class Lexical extends OctalIntegerLiteral
  {
    /* [0] [0-7]+ -> OctalIntegerLiteral  */
  }
  public class Ambiguity extends OctalIntegerLiteral
  {
    private final java.util.List < OctalIntegerLiteral > alternatives;
    public Ambiguity (java.util.List < OctalIntegerLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < OctalIntegerLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
