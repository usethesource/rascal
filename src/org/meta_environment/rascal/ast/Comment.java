package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Comment extends AbstractAST
{
  static public class Lexical extends Comment
  {
    /* "//" ~[\n]* [\n] -> Comment {category("Comment")} */
  } public class Ambiguity extends Comment
  {
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
