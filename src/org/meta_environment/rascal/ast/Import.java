package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Import extends AbstractAST
{
  public class Import extends Import
  {
/* "import" module:ImportedModule ";" -> Import {cons("Import")} */
    private Import ()
    {
    }
    /*package */ Import (ITree tree, ImportedModule module)
    {
      this.tree = tree;
      this.module = module;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitImportImport (this);
    }
    private ImportedModule module;
    public ImportedModule getmodule ()
    {
      return module;
    }
    private void privateSetmodule (ImportedModule x)
    {
      this.module = x;
    }
    public Import setmodule (ImportedModule x)
    {
      Import z = new Import ();
      z.privateSetmodule (x);
      return z;
    }
  }
  public class Ambiguity extends Import
  {
    private final List < Import > alternatives;
    public Ambiguity (List < Import > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < Import > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Extend extends Import
  {
/* "extend" module:ImportedModule ";" -> Import {cons("Extend")} */
    private Extend ()
    {
    }
    /*package */ Extend (ITree tree, ImportedModule module)
    {
      this.tree = tree;
      this.module = module;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitImportExtend (this);
    }
    private ImportedModule module;
    public ImportedModule getmodule ()
    {
      return module;
    }
    private void privateSetmodule (ImportedModule x)
    {
      this.module = x;
    }
    public Extend setmodule (ImportedModule x)
    {
      Extend z = new Extend ();
      z.privateSetmodule (x);
      return z;
    }
  }
}
