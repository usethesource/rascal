package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class NamedBackslash extends AbstractAST
{
  public class Lexical extends NamedBackslash
  {
    /* [\\] -> NamedBackslash  */
  }
  public class Ambiguity extends NamedBackslash
  {
    private final List < NamedBackslash > alternatives;
    public Ambiguity (List < NamedBackslash > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < NamedBackslash > getAlternatives ()
    {
      return alternatives;
    }
  }
}
