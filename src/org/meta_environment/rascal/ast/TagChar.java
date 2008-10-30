package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class TagChar extends AbstractAST
{
  public class Lexical extends TagChar
  {
    /* ~[\}] -> TagChar  */
  }
  public class Ambiguity extends TagChar
  {
    private final List < TagChar > alternatives;
    public Ambiguity (List < TagChar > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < TagChar > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends TagChar
  {
    /* [\\] [\\\}] -> TagChar  */
  }
}
