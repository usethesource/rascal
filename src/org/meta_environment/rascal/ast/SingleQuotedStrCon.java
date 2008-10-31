package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class SingleQuotedStrCon extends AbstractAST
{
  public class Lexical extends SingleQuotedStrCon
  {
    /* [\'] chars:SingleQuotedStrChar* [\'] -> SingleQuotedStrCon  */
  }
  public class Ambiguity extends SingleQuotedStrCon
  {
    private final java.util.List < SingleQuotedStrCon > alternatives;
    public Ambiguity (java.util.List < SingleQuotedStrCon > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < SingleQuotedStrCon > getAlternatives ()
    {
      return alternatives;
    }
  }
}
