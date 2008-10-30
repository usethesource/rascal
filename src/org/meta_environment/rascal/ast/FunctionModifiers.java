package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFunctionModifiersList (this);
    }
    private List < FunctionModifier > modifiers;
    public List < FunctionModifier > getmodifiers ()
    {
      return modifiers;
    }
    private void $setmodifiers (List < FunctionModifier > x)
    {
      this.modifiers = x;
    }
    public List setmodifiers (List < FunctionModifier > x)
    {
      List z = new List ();
      z.$setmodifiers (x);
      return z;
    }
  }
  public class Ambiguity extends FunctionModifiers
  {
    private final List < FunctionModifiers > alternatives;
    public Ambiguity (List < FunctionModifiers > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < FunctionModifiers > getAlternatives ()
    {
      return alternatives;
    }
  }
}
