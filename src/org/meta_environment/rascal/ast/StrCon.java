package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class StrCon extends AbstractAST
{
  static public class Lexical extends StrCon
  {
    /* [\"] chars:StrChar* [\"] -> StrCon  */
    private String string;
    /*package */ Lexical (ITree tree, String string)
    {
      this.tree = tree;
      this.string = arg;
    }
    public String getString ()
    {
      return string;
    }
  }
  static public class Ambiguity extends StrCon
  {
    private final java.util.List < org.meta_environment.rascal.ast.StrCon >
      alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.StrCon > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.StrCon >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
