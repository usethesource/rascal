package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class SingleQuotedStrCon extends AbstractAST
{
  public class Lexical extends SingleQuotedStrCon
  {
    /* [\'] chars:SingleQuotedStrChar* [\'] -> SingleQuotedStrCon  */
  }
  public class Ambiguity extends SingleQuotedStrCon
  {
    private final List < SingleQuotedStrCon > alternatives;
    public Ambiguity (List < SingleQuotedStrCon > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < SingleQuotedStrCon > getAlternatives ()
    {
      return alternatives;
    }
  }
}
