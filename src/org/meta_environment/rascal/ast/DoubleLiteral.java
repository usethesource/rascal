package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class DoubleLiteral extends AbstractAST
{
  public class Lexical extends DoubleLiteral
  {
    /* [0-9]+ "." [0-9]* ( [eE] [\+\-]? [0-9]+ )? [dD]? -> DoubleLiteral  */
  }
  public class Ambiguity extends DoubleLiteral
  {
    private final java.util.List < DoubleLiteral > alternatives;
    public Ambiguity (java.util.List < DoubleLiteral > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < DoubleLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
