package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class NamedRegExp extends AbstractAST
{
  public class Lexical extends NamedRegExp
  {
    /* ~[\>\\] -> NamedRegExp  */
  }
  public class Ambiguity extends NamedRegExp
  {
    private final java.util.List < NamedRegExp > alternatives;
    public Ambiguity (java.util.List < NamedRegExp > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < NamedRegExp > getAlternatives ()
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
