package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class BooleanLiteral extends AbstractAST
{
  public class Lexical extends BooleanLiteral
  {
    /* "true" -> BooleanLiteral  */
  }
  public class Ambiguity extends BooleanLiteral
  {
    private final java.util.List < BooleanLiteral > alternatives;
    public Ambiguity (java.util.List < BooleanLiteral > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < BooleanLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
