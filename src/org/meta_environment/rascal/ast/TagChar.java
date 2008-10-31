package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class TagChar extends AbstractAST
{
  static public class Lexical extends TagChar
  {
    /* ~[\}] -> TagChar  */
  } static public class Ambiguity extends TagChar
  {
    private final java.util.List < TagChar > alternatives;
    public Ambiguity (java.util.List < TagChar > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < TagChar > getAlternatives ()
    {
      return alternatives;
    }
  }
}
