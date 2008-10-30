package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class TagChar extends AbstractAST
{
  public class Lexical extends TagChar
  {
    /* ~[\}] -> TagChar  */
  }
  public class Ambiguity extends TagChar
  {
    private final java.util.List < TagChar > alternatives;
    public Ambiguity (java.util.List < TagChar > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < TagChar > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends TagChar
  {
    /* [\\] [\\\}] -> TagChar  */
  }
}
