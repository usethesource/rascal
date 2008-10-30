package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class NamedRegExp extends AbstractAST
{
  public class Lexical extends NamedRegExp
  {
    /* ~[\>\\] -> NamedRegExp  */
  }
  public class Ambiguity extends NamedRegExp
  {
    private final List < NamedRegExp > alternatives;
    public Ambiguity (List < NamedRegExp > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < NamedRegExp > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends NamedRegExp
  {
    /* [\\][\>\\] -> NamedRegExp  */
  }
  public class Lexical extends NamedRegExp
  {
    /* NamedBackslash -> NamedRegExp  */
  }
}
