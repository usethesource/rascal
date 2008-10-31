package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class RegExp extends AbstractAST
{
  static public class Lexical extends RegExp
  {
    /* Backslash -> RegExp  */
  } static public class Ambiguity extends RegExp
  {
    public RegExp.Ambiguity makeRegExpAmbiguity (java.util.List < RegExp >
						 alternatives)
    {
      RegExp.Ambiguity amb = new RegExp.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (RegExp.Ambiguity) table.get (amb);
    }
    private final java.util.List < RegExp > alternatives;
    public Ambiguity (java.util.List < RegExp > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < RegExp > getAlternatives ()
    {
      return alternatives;
    }
  }
}
