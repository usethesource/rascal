package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class TagString extends AbstractAST
{
  public class Lexical extends TagString
  {
    /* "{" TagChar* "}" -> TagString  */
  }
  public class Ambiguity extends TagString
  {
    private final List < TagString > alternatives;
    public Ambiguity (List < TagString > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < TagString > getAlternatives ()
    {
      return alternatives;
    }
  }
}
