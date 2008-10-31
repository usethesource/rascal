package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class StrCon extends AbstractAST
{
  static public class Lexical extends StrCon
  {
    /* [\"] chars:StrChar* [\"] -> StrCon  */
  }
  static public class Ambiguity extends StrCon
  {
    public StrCon.Ambiguity makeStrConAmbiguity (java.util.List < StrCon >
						 alternatives)
    {
      StrCon.Ambiguity amb = new StrCon.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (StrCon.Ambiguity) table.get (amb);
    }
    private final java.util.List < StrCon > alternatives;
    public Ambiguity (java.util.List < StrCon > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < StrCon > getAlternatives ()
    {
      return alternatives;
    }
  }
}
