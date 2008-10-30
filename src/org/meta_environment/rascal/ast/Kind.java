package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Kind extends AbstractAST
{
  public class Module extends Kind
  {
/* "module" -> Kind {cons("Module")} */
    private Module ()
    {
    }
    /*package */ Module (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitKindModule (this);
    }
  }
  public class Ambiguity extends Kind
  {
    private final List < Kind > alternatives;
    public Ambiguity (List < Kind > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < Kind > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Function extends Kind
  {
/* "function" -> Kind {cons("Function")} */
    private Function ()
    {
    }
    /*package */ Function (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitKindFunction (this);
    }
  }
  public class Variable extends Kind
  {
/* "variable" -> Kind {cons("Variable")} */
    private Variable ()
    {
    }
    /*package */ Variable (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitKindVariable (this);
    }
  }
  public class Data extends Kind
  {
/* "data" -> Kind {cons("Data")} */
    private Data ()
    {
    }
    /*package */ Data (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitKindData (this);
    }
  }
  public class View extends Kind
  {
/* "view" -> Kind {cons("View")} */
    private View ()
    {
    }
    /*package */ View (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitKindView (this);
    }
  }
  public class Type extends Kind
  {
/* "type" -> Kind {cons("Type")} */
    private Type ()
    {
    }
    /*package */ Type (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitKindType (this);
    }
  }
  public class Anno extends Kind
  {
/* "anno" -> Kind {cons("Anno")} */
    private Anno ()
    {
    }
    /*package */ Anno (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitKindAnno (this);
    }
  }
  public class Tag extends Kind
  {
/* "tag" -> Kind {cons("Tag")} */
    private Tag ()
    {
    }
    /*package */ Tag (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitKindTag (this);
    }
  }
  public class All extends Kind
  {
/* "all" -> Kind {cons("All")} */
    private All ()
    {
    }
    /*package */ All (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitKindAll (this);
    }
  }
}
