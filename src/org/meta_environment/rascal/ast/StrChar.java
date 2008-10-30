package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class StrChar extends AbstractAST
{
  public class newline extends StrChar
  {
/* "\\n" -> StrChar {cons("newline")} */
    private newline ()
    {
    }
    /*package */ newline (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStrCharnewline (this);
    }
  }
  public class Ambiguity extends StrChar
  {
    private final List < StrChar > alternatives;
    public Ambiguity (List < StrChar > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < StrChar > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends StrChar
  {
    /* "\\t" -> StrChar  */
  }
  public class Lexical extends StrChar
  {
    /* "\\\"" -> StrChar  */
  }
  public class Lexical extends StrChar
  {
    /* "\\\\" -> StrChar  */
  }
  public class Lexical extends StrChar
  {
    /* "\\" a:[0-9]b:[0-9]c:[0-9] -> StrChar  */
  }
  public class Lexical extends StrChar
  {
    /* ~[\0-\31\n\t\"\\] -> StrChar  */
  }
}
