package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class NamedBackslash extends AbstractAST
{
  static public class Lexical extends NamedBackslash
  {
    /* [\\] -> NamedBackslash  */
  }
  static public class Ambiguity extends NamedBackslash
  {
    public NamedBackslash.Ambiguity makeNamedBackslashAmbiguity (java.util.
								 List <
								 NamedBackslash
								 >
								 alternatives)
    {
      NamedBackslash.Ambiguity amb =
	new NamedBackslash.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (NamedBackslash.Ambiguity) table.get (amb);
    }
    private final java.util.List < NamedBackslash > alternatives;
    public Ambiguity (java.util.List < NamedBackslash > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < NamedBackslash > getAlternatives ()
    {
      return alternatives;
    }
  }
}
