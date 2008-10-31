package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class CommentChar extends AbstractAST
{
  static public class Lexical extends CommentChar
  {
    /* ~[\*] -> CommentChar  */
  } static public class Ambiguity extends CommentChar
  {
    public CommentChar.Ambiguity makeCommentCharAmbiguity (java.util.List <
							   CommentChar >
							   alternatives)
    {
      CommentChar.Ambiguity amb = new CommentChar.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (CommentChar.Ambiguity) table.get (amb);
    }
    private final java.util.List < CommentChar > alternatives;
    public Ambiguity (java.util.List < CommentChar > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < CommentChar > getAlternatives ()
    {
      return alternatives;
    }
  }
}
