package org.meta_environment.rascal.ast;
public abstract class FunctionModifiers extends AbstractAST
{
  public class List extends FunctionModifiers
  {
/* modifiers:FunctionModifier* -> FunctionModifiers {cons("List")} */
    private List ()
    {
    }
    /*package */ List (ITree tree, List < FunctionModifier > modifiers)
    {
      this.tree = tree;
      this.modifiers = modifiers;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitListFunctionModifiers (this);
    }
    private final List < FunctionModifier > modifiers;
    public List < FunctionModifier > getmodifiers ()
    {
      return modifiers;
    }
    private void privateSetmodifiers (List < FunctionModifier > x)
    {
      this.modifiers = x;
    }
    public List setmodifiers (List < FunctionModifier > x)
    {
      z = new List ();
      z.privateSetmodifiers (x);
      return z;
    }
  }
}
