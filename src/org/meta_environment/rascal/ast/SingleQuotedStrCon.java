package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class SingleQuotedStrCon extends AbstractAST
{
  static public class Lexical extends SingleQuotedStrCon
  {
    /* [\'] chars:SingleQuotedStrChar* [\'] -> SingleQuotedStrCon  */
  }
  static public class Ambiguity extends SingleQuotedStrCon
  {
    public SingleQuotedStrCon.Ambiguity makeSingleQuotedStrConAmbiguity (java.
									 util.
									 List
									 <
									 SingleQuotedStrCon
									 >
									 alternatives)
    {
      SingleQuotedStrCon.Ambiguity amb =
	new SingleQuotedStrCon.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (SingleQuotedStrCon.Ambiguity) table.get (amb);
    }
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
