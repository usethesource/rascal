package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class FunctionModifiers extends AbstractAST
{
  static public class List extends FunctionModifiers
  {
/* modifiers:FunctionModifier* -> FunctionModifiers {cons("List")} */
    private List ()
    {
    }
    /*package */ List (ITree tree,
		       java.util.List < FunctionModifier > modifiers)
    {
      this.tree = tree;
      this.modifiers = modifiers;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFunctionModifiersList (this);
    }
    private java.util.List < FunctionModifier > modifiers;
    public java.util.List < FunctionModifier > getModifiers ()
    {
      return modifiers;
    }
    private void $setModifiers (java.util.List < FunctionModifier > x)
    {
      this.modifiers = x;
    }
    public List setModifiers (java.util.List < FunctionModifier > x)
    {
      List z = new List ();
      z.$setModifiers (x);
      return z;
    }
  }
  static public class Ambiguity extends FunctionModifiers
  {
    private final java.util.List < FunctionModifiers > alternatives;
    public Ambiguity (java.util.List < FunctionModifiers > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < FunctionModifiers > getAlternatives ()
    {
      return alternatives;
    }
  }
}
