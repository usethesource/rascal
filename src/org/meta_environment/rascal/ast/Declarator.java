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
			  java.util.List <
			  org.meta_environment.rascal.ast.Variable >
			  variables)
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
      Default z = new Default ();
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
      Default z = new Default ();
      z.$setVariables (x);
      return z;
    }
  }
  static public class Ambiguity extends Declarator
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.Declarator > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.Declarator >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.Declarator >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
