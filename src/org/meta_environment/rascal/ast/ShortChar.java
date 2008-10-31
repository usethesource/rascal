package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class ShortChar extends AbstractAST
{
  static public class Lexical extends ShortChar
  {
    /* character:[a-zA-Z0-9] -> ShortChar  */
  } static public class Ambiguity extends ShortChar
  {
    public ShortChar.Ambiguity makeShortCharAmbiguity (java.util.List <
						       ShortChar >
						       alternatives)
    {
      ShortChar.Ambiguity amb = new ShortChar.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (ShortChar.Ambiguity) table.get (amb);
    }
    private final java.util.List < ShortChar > alternatives;
    public Ambiguity (java.util.List < ShortChar > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < ShortChar > getAlternatives ()
    {
      return alternatives;
    }
  }
}
