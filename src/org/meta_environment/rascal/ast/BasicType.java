package org.meta_environment.rascal.ast;
public abstract class BasicType extends AbstractAST
{
  public class Bool extends BasicType
  {
/* "bool" -> BasicType {cons("Bool")} */
    private Bool ()
    {
    }
    /*package */ Bool (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitBoolBasicType (this);
    }
  }
  public class Int extends BasicType
  {
/* "int" -> BasicType {cons("Int")} */
    private Int ()
    {
    }
    /*package */ Int (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitIntBasicType (this);
    }
  }
  public class Double extends BasicType
  {
/* "double" -> BasicType {cons("Double")} */
    private Double ()
    {
    }
    /*package */ Double (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDoubleBasicType (this);
    }
  }
  public class String extends BasicType
  {
/* "str" -> BasicType {cons("String")} */
    private String ()
    {
    }
    /*package */ String (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitStringBasicType (this);
    }
  }
  public class Value extends BasicType
  {
/* "value" -> BasicType {cons("Value")} */
    private Value ()
    {
    }
    /*package */ Value (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitValueBasicType (this);
    }
  }
  public class Term extends BasicType
  {
/* "term" -> BasicType {cons("Term")} */
    private Term ()
    {
    }
    /*package */ Term (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTermBasicType (this);
    }
  }
  public class Void extends BasicType
  {
/* "void" -> BasicType {cons("Void")} */
    private Void ()
    {
    }
    /*package */ Void (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitVoidBasicType (this);
    }
  }
  public class Loc extends BasicType
  {
/* "loc" -> BasicType {cons("Loc")} */
    private Loc ()
    {
    }
    /*package */ Loc (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitLocBasicType (this);
    }
  }
}
