package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class ModuleName extends AbstractAST
{
  public class Leaf extends ModuleName
  {
/* ModuleWord -> ModuleName {cons("Leaf")} */
    private Leaf ()
    {
    }
    /*package */ Leaf (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitModuleNameLeaf (this);
    }
  }
  public class Ambiguity extends ModuleName
  {
    private final List < ModuleName > alternatives;
    public Ambiguity (List < ModuleName > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < ModuleName > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Root extends ModuleName
  {
/* sep:"/" basename:ModuleName -> ModuleName {cons("Root")} */
    private Root ()
    {
    }
    /*package */ Root (ITree tree,
		       List < get - sort - from - symbol ("/") > sep,
		       ModuleName basename)
    {
      this.tree = tree;
      this.sep = sep;
      this.basename = basename;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitModuleNameRoot (this);
    }
    private List < get - sort - from - symbol ("/") > sep;
    public List < get - sort - from - symbol ("/") > getsep ()
    {
      return sep;
    }
    private void privateSetsep (List < get - sort - from - symbol ("/") > x)
    {
      this.sep = x;
    }
    public Root setsep (List < get - sort - from - symbol ("/") > x)
    {
      Root z = new Root ();
      z.privateSetsep (x);
      return z;
    }
    private ModuleName basename;
    public ModuleName getbasename ()
    {
      return basename;
    }
    private void privateSetbasename (ModuleName x)
    {
      this.basename = x;
    }
    public Root setbasename (ModuleName x)
    {
      Root z = new Root ();
      z.privateSetbasename (x);
      return z;
    }
  }
  public class Path extends ModuleName
  {
/* dirname:ModuleWord sep:"/" basename:ModuleName -> ModuleName {cons("Path")} */
    private Path ()
    {
    }
    /*package */ Path (ITree tree, ModuleWord dirname,
		       List < get - sort - from - symbol ("/") > sep,
		       ModuleName basename)
    {
      this.tree = tree;
      this.dirname = dirname;
      this.sep = sep;
      this.basename = basename;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitModuleNamePath (this);
    }
    private ModuleWord dirname;
    public ModuleWord getdirname ()
    {
      return dirname;
    }
    private void privateSetdirname (ModuleWord x)
    {
      this.dirname = x;
    }
    public Path setdirname (ModuleWord x)
    {
      Path z = new Path ();
      z.privateSetdirname (x);
      return z;
    }
    private List < get - sort - from - symbol ("/") > sep;
    public List < get - sort - from - symbol ("/") > getsep ()
    {
      return sep;
    }
    private void privateSetsep (List < get - sort - from - symbol ("/") > x)
    {
      this.sep = x;
    }
    public Path setsep (List < get - sort - from - symbol ("/") > x)
    {
      Path z = new Path ();
      z.privateSetsep (x);
      return z;
    }
    private ModuleName basename;
    public ModuleName getbasename ()
    {
      return basename;
    }
    private void privateSetbasename (ModuleName x)
    {
      this.basename = x;
    }
    public Path setbasename (ModuleName x)
    {
      Path z = new Path ();
      z.privateSetbasename (x);
      return z;
    }
  }
}
