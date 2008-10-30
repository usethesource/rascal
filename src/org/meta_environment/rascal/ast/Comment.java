package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Comment extends AbstractAST
{
  public class Lexical extends Comment
  {
    /* "//" ~[\n]* [\n] -> Comment {category("Comment")} */
  }
  public class Ambiguity extends Comment
  {
    private final List < Comment > alternatives;
    public Ambiguity (List < Comment > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < Comment > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends Comment
  {
    /* "/*" CommentChar* " * / " -> Comment {category(" Comment ")} */
}
}

