package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Import extends AbstractAST
{
  static public class Default extends Import
  {
/* "import" module:ImportedModule ";" -> Import {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, ImportedModule module)
    {
      this.tree = tree;
      this.module = module;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitImportDefault (this);
    }
    private ImportedModule module;
    public ImportedModule getModule ()
    {
      return module;
    }
    private void $setModule (ImportedModule x)
    {
      this.module = x;
    }
    public Default setModule (ImportedModule x)
    {
      Default z = new Default ();
      z.$setModule (x);
      return z;
    }
  }
  static public class Ambiguity extends Import
  {
    private final java.util.List < Import > alternatives;
    public Ambiguity (java.util.List < Import > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Import > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Extend extends Import
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
    public ImportedModule getModule ()
    {
      return module;
    }
    private void $setModule (ImportedModule x)
    {
      this.module = x;
    }
    public Extend setModule (ImportedModule x)
    {
      Extend z = new Extend ();
      z.$setModule (x);
      return z;
    }
  }
}
