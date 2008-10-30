package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class ModuleWord extends AbstractAST
{
  public class Lexical extends ModuleWord
  {
    /* letters:[A-Za-z0-9\_\-]+ -> ModuleWord  */
  }
  public class Ambiguity extends ModuleWord
  {
    private final java.util.List < ModuleWord > alternatives;
    public Ambiguity (java.util.List < ModuleWord > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < ModuleWord > getAlternatives ()
    {
      return alternatives;
    }
  }
}
