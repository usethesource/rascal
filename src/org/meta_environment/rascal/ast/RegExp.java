package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class RegExp extends AbstractAST
{
  public class Lexical extends RegExp
  {
    /* Backslash -> RegExp  */
  }
  public class Ambiguity extends RegExp
  {
    private final List < RegExp > alternatives;
    public Ambiguity (List < RegExp > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < RegExp > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends RegExp
  {
    /* [\\][\/\<\\] -> RegExp  */
  }
  public class Lexical extends RegExp
  {
    /* ~[\/\<\\] -> RegExp  */
  }
  public class Lexical extends RegExp
  {
    /* "<" Name ":" NamedRegExp* ">" -> RegExp  */
  }
}
