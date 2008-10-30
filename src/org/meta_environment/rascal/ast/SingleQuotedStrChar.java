package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class SingleQuotedStrChar extends AbstractAST
{
  public class Lexical extends SingleQuotedStrChar
  {
    /* "\\n" -> SingleQuotedStrChar  */
  }
  public class Ambiguity extends SingleQuotedStrChar
  {
    private final List < SingleQuotedStrChar > alternatives;
    public Ambiguity (List < SingleQuotedStrChar > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < SingleQuotedStrChar > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends SingleQuotedStrChar
  {
    /* "\\t" -> SingleQuotedStrChar  */
  }
  public class Lexical extends SingleQuotedStrChar
  {
    /* "\\'" -> SingleQuotedStrChar  */
  }
  public class Lexical extends SingleQuotedStrChar
  {
    /* "\\\\" -> SingleQuotedStrChar  */
  }
  public class Lexical extends SingleQuotedStrChar
  {
    /* "\\" a:[0-9]b:[0-9]c:[0-9] -> SingleQuotedStrChar  */
  }
  public class Lexical extends SingleQuotedStrChar
  {
    /* ~[\0-\31\n\t\'\\] -> SingleQuotedStrChar  */
  }
}
