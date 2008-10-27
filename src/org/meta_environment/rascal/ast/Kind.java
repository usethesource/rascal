package org.meta_environment.rascal.ast;
public abstract class Kind extends AbstractAST
{
  public class Module extends Kind
  {
    private Module ()
    {
    }
    /*package */ Module (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitModuleKind (this);
    }
  }
  public class Function extends Kind
  {
    private Function ()
    {
    }
    /*package */ Function (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitFunctionKind (this);
    }
  }
  public class Variable extends Kind
  {
    private Variable ()
    {
    }
    /*package */ Variable (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitVariableKind (this);
    }
  }
  public class Data extends Kind
  {
    private Data ()
    {
    }
    /*package */ Data (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDataKind (this);
    }
  }
  public class View extends Kind
  {
    private View ()
    {
    }
    /*package */ View (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitViewKind (this);
    }
  }
  public class Type extends Kind
  {
    private Type ()
    {
    }
    /*package */ Type (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTypeKind (this);
    }
  }
  public class Anno extends Kind
  {
    private Anno ()
    {
    }
    /*package */ Anno (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAnnoKind (this);
    }
  }
  public class Tag extends Kind
  {
    private Tag ()
    {
    }
    /*package */ Tag (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTagKind (this);
    }
  }
  public class All extends Kind
  {
    private All ()
    {
    }
    /*package */ All (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAllKind (this);
    }
  }
}
