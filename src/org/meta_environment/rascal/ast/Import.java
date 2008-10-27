package org.meta_environment.rascal.ast;
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitImportImport (this);
    }
    private final ImportedModule module;
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
      z = new Import ();
      z.privateSetmodule (x);
      return z;
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitExtendImport (this);
    }
    private final ImportedModule module;
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
      z = new Extend ();
      z.privateSetmodule (x);
      return z;
    }
  }
}
