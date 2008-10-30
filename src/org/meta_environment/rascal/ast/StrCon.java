package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class StrCon extends AbstractAST
{
  public class Lexical extends StrCon
  {
    /* [\"] chars:StrChar* [\"] -> StrCon  */
  }
  public class Ambiguity extends StrCon
  {
    private final List < StrCon > alternatives;
    public Ambiguity (List < StrCon > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < StrCon > getAlternatives ()
    {
      return alternatives;
    }
  }
}
