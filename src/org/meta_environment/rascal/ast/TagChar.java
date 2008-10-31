package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class TagChar extends AbstractAST
{
  static public class Lexical extends TagChar
  {
    /* ~[\}] -> TagChar  */
  } static public class Ambiguity extends TagChar
  {
    public TagChar.Ambiguity makeTagCharAmbiguity (java.util.List < TagChar >
						   alternatives)
    {
      TagChar.Ambiguity amb = new TagChar.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (TagChar.Ambiguity) table.get (amb);
    }
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
