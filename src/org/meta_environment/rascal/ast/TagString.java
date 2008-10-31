package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class TagString extends AbstractAST
{
  static public class Lexical extends TagString
  {
    /* "{" TagChar* "}" -> TagString  */
  }
  static public class Ambiguity extends TagString
  {
    public TagString.Ambiguity makeTagStringAmbiguity (java.util.List <
						       TagString >
						       alternatives)
    {
      TagString.Ambiguity amb = new TagString.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (TagString.Ambiguity) table.get (amb);
    }
    private final java.util.List < TagString > alternatives;
    public Ambiguity (java.util.List < TagString > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < TagString > getAlternatives ()
    {
      return alternatives;
    }
  }
}
