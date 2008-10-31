package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class NoElseMayFollow extends AbstractAST
{
  static public class Lexical extends NoElseMayFollow
  {
    /*  -> NoElseMayFollow  */
  }
  public class Ambiguity extends NoElseMayFollow
  {
    private final java.util.List < NoElseMayFollow > alternatives;
    public Ambiguity (java.util.List < NoElseMayFollow > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < NoElseMayFollow > getAlternatives ()
    {
      return alternatives;
    }
  }
}
