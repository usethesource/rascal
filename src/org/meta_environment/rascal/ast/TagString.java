package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class TagString extends AbstractAST
{
  public class Lexical extends TagString
  {
    /* "{" TagChar* "}" -> TagString  */
  }
  public class Ambiguity extends TagString
  {
    private final java.util.List < TagString > alternatives;
    public Ambiguity (java.util.List < TagString > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < TagString > getAlternatives ()
    {
      return alternatives;
    }
  }
}
