package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class ModuleName extends AbstractAST
{
  static public class Lexical extends ModuleName
  {
    /* ModuleWord -> ModuleName  */
  } static public class Ambiguity extends ModuleName
  {
    public ModuleName.Ambiguity makeModuleNameAmbiguity (java.util.List <
							 ModuleName >
							 alternatives)
    {
      ModuleName.Ambiguity amb = new ModuleName.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (ModuleName.Ambiguity) table.get (amb);
    }
    private final java.util.List < ModuleName > alternatives;
    public Ambiguity (java.util.List < ModuleName > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < ModuleName > getAlternatives ()
    {
      return alternatives;
    }
  }
}
