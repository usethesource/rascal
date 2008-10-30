package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class StringLiteral extends AbstractAST
{
  public class Lexical extends StringLiteral
  {
    /* "\"" StringCharacter* "\"" -> StringLiteral  */
  }
  public class Ambiguity extends StringLiteral
  {
    private final List < StringLiteral > alternatives;
    public Ambiguity (List < StringLiteral > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < StringLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
