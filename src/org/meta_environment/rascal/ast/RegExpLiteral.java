package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class RegExpLiteral extends AbstractAST
{
  public class Lexical extends RegExpLiteral
  {
    /* "/" RegExp* "/" RegExpModifier? -> RegExpLiteral  */
  }
  public class Ambiguity extends RegExpLiteral
  {
    private final java.util.List < RegExpLiteral > alternatives;
    public Ambiguity (java.util.List < RegExpLiteral > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < RegExpLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
