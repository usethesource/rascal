package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class DecimalIntegerLiteral extends AbstractAST
{
  public class Lexical extends DecimalIntegerLiteral
  {
    /* "0" -> DecimalIntegerLiteral  */
  }
  public class Ambiguity extends DecimalIntegerLiteral
  {
    private final List < DecimalIntegerLiteral > alternatives;
    public Ambiguity (List < DecimalIntegerLiteral > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < DecimalIntegerLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends DecimalIntegerLiteral
  {
    /* [1-9] [0-9]* -> DecimalIntegerLiteral  */
  }
}
