package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class DecimalIntegerLiteral extends AbstractAST
{
  public class Lexical extends DecimalIntegerLiteral
  {
    /* "0" -> DecimalIntegerLiteral  */
  }
  public class Ambiguity extends DecimalIntegerLiteral
  {
    private final java.util.List < DecimalIntegerLiteral > alternatives;
    public Ambiguity (java.util.List < DecimalIntegerLiteral > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < DecimalIntegerLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends DecimalIntegerLiteral
  {
    /* [1-9] [0-9]* -> DecimalIntegerLiteral  */
  }
}
