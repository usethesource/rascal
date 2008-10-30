package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class CommentChar extends AbstractAST
{
  public class Lexical extends CommentChar
  {
    /* ~[\*] -> CommentChar  */
  }
  public class Ambiguity extends CommentChar
  {
    private final List < CommentChar > alternatives;
    public Ambiguity (List < CommentChar > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < CommentChar > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends CommentChar
  {
    /* Asterisk -> CommentChar  */
  }
}
