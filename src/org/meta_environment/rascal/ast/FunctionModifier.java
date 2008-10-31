package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class FunctionModifier extends AbstractAST
{
  static public class Java extends FunctionModifier
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
  static public class Ambiguity extends FunctionModifier
  {
    public FunctionModifier.Ambiguity makeFunctionModifierAmbiguity (java.
								     util.
								     List <
								     FunctionModifier
								     >
								     alternatives)
    {
      FunctionModifier.Ambiguity amb =
	new FunctionModifier.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (FunctionModifier.Ambiguity) table.get (amb);
    }
    private final java.util.List < FunctionModifier > alternatives;
    public Ambiguity (java.util.List < FunctionModifier > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < FunctionModifier > getAlternatives ()
    {
      return alternatives;
    }
  }
}
