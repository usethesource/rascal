package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Backslash extends AbstractAST
{
  static public class Lexical extends Backslash
  {
    /* [\\] -> Backslash  */
  }
  static public class Ambiguity extends Backslash
  {
    public Backslash.Ambiguity makeBackslashAmbiguity (java.util.List <
						       Backslash >
						       alternatives)
    {
      Backslash.Ambiguity amb = new Backslash.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Backslash.Ambiguity) table.get (amb);
    }
    private final java.util.List < Backslash > alternatives;
    public Ambiguity (java.util.List < Backslash > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Backslash > getAlternatives ()
    {
      return alternatives;
    }
  }
}
