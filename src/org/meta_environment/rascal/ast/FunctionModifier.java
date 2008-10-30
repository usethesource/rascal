package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class FunctionModifier extends AbstractAST
{
  public class Java extends FunctionModifier
  {
/* "java" -> FunctionModifier {cons("Java")} */
    private Java ()
    {
    }
    /*package */ Java (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFunctionModifierJava (this);
    }
  }
  public class Ambiguity extends FunctionModifier
  {
    private final List < FunctionModifier > alternatives;
    public Ambiguity (List < FunctionModifier > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < FunctionModifier > getAlternatives ()
    {
      return alternatives;
    }
  }
}
