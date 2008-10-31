package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Comment extends AbstractAST
{
  static public class Lexical extends Comment
  {
    /* "//" ~[\n]* [\n] -> Comment {category("Comment")} */
  } static public class Ambiguity extends Comment
  {
    public Comment.Ambiguity makeCommentAmbiguity (java.util.List < Comment >
						   alternatives)
    {
      Comment.Ambiguity amb = new Comment.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Comment.Ambiguity) table.get (amb);
    }
    private final java.util.List < Comment > alternatives;
    public Ambiguity (java.util.List < Comment > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Comment > getAlternatives ()
    {
      return alternatives;
    }
  }
}
