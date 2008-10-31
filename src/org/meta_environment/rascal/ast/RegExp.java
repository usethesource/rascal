package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class RegExp extends AbstractAST
{
  static public class Lexical extends RegExp
  {
    /* Backslash -> RegExp  */
  } public class Ambiguity extends RegExp
  {
    private final java.util.List < RegExp > alternatives;
    public Ambiguity (java.util.List < RegExp > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < RegExp > getAlternatives ()
    {
      return alternatives;
    }
  }
}
