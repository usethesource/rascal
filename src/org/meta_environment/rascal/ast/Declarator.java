package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Declarator extends AbstractAST
{
  static public class Default extends Declarator
  {
/* type:Type variables:{Variable ","}+ -> Declarator {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  org.meta_environment.rascal.ast.Type type,
			  java.util.List < Variable > variables)
    {
      this.tree = tree;
      this.type = type;
      this.variables = variables;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDeclaratorDefault (this);
    }
    private org.meta_environment.rascal.ast.Type type;
    public org.meta_environment.rascal.ast.Type getType ()
    {
      return type;
    }
    private void $setType (org.meta_environment.rascal.ast.Type x)
    {
      this.type = x;
    }
    public Default setType (org.meta_environment.rascal.ast.Type x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setType (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Variable >
      variables;
    public java.util.List < org.meta_environment.rascal.ast.Variable >
      getVariables ()
    {
      return variables;
    }
    private void $setVariables (java.util.List <
				org.meta_environment.rascal.ast.Variable > x)
    {
      this.variables = x;
    }
    public Default setVariables (java.util.List <
				 org.meta_environment.rascal.ast.Variable > x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setVariables (x);
      return z;
    }
  }
  static public class Ambiguity extends Declarator
  {
    public Declarator.Ambiguity makeDeclaratorAmbiguity (java.util.List <
							 Declarator >
							 alternatives)
    {
      Declarator.Ambiguity amb = new Declarator.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Declarator.Ambiguity) table.get (amb);
    }
    private final java.util.List < Declarator > alternatives;
    public Ambiguity (java.util.List < Declarator > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Declarator > getAlternatives ()
    {
      return alternatives;
    }
  }
}
