package org.meta_environment.rascal.ast;
public abstract class FunctionName extends AbstractAST
{
  public class Name extends FunctionName
  {
    private Name name;

    private Name ()
    {
    }
    /*package */ Name (ITree tree, Name name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNameFunctionName (this);
    }
    private final Name name;
    public Name getname ()
    {
      return name;
    }
    private void privateSetname (Name x)
    {
      this.name = x;
    }
    public Name setname (Name x)
    {
      z = new Name ();
      z.privateSetname (x);
      return z;
    }
  }
  public class Operator extends FunctionName
  {
    private StandardOperator operator;

    private Operator ()
    {
    }
    /*package */ Operator (ITree tree, StandardOperator operator)
    {
      this.tree = tree;
      this.operator = operator;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitOperatorFunctionName (this);
    }
    private final StandardOperator operator;
    public StandardOperator getoperator ()
    {
      return operator;
    }
    private void privateSetoperator (StandardOperator x)
    {
      this.operator = x;
    }
    public Operator setoperator (StandardOperator x)
    {
      z = new Operator ();
      z.privateSetoperator (x);
      return z;
    }
  }
}
