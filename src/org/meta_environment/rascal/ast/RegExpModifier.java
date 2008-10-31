package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class RegExpModifier extends AbstractAST
{
  static public class Lexical extends RegExpModifier
  {
    /* [imsd] -> RegExpModifier  */
  }
  static public class Ambiguity extends RegExpModifier
  {
    public RegExpModifier.Ambiguity makeRegExpModifierAmbiguity (java.util.
								 List <
								 RegExpModifier
								 >
								 alternatives)
    {
      RegExpModifier.Ambiguity amb =
	new RegExpModifier.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (RegExpModifier.Ambiguity) table.get (amb);
    }
    private final java.util.List < RegExpModifier > alternatives;
    public Ambiguity (java.util.List < RegExpModifier > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < RegExpModifier > getAlternatives ()
    {
      return alternatives;
    }
  }
}
