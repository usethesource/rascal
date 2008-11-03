package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class FunctionBody extends AbstractAST
{
  public java.util.List < org.meta_environment.rascal.ast.Statement >
    getStatements ()
  {
    throw new UnsupportedOperationException ();
  }
  static public class Default extends FunctionBody
  {
/* "{" statements:Statement* "}" -> FunctionBody {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  java.util.List <
			  org.meta_environment.rascal.ast.Statement >
			  statements)
    {
      this.tree = tree;
      this.statements = statements;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFunctionBodyDefault (this);
    }
    private java.util.List < org.meta_environment.rascal.ast.Statement >
      statements;
    public java.util.List < org.meta_environment.rascal.ast.Statement >
      getStatements ()
    {
      return statements;
    }
    private void $setStatements (java.util.List <
				 org.meta_environment.rascal.ast.Statement >
				 x)
    {
      this.statements = x;
    }
    public Default setStatements (java.util.List <
				  org.meta_environment.rascal.ast.Statement >
				  x)
    {
      Default z = new Default ();
      z.$setStatements (x);
      return z;
    }
  }
  static public class Ambiguity extends FunctionBody
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.FunctionBody > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.FunctionBody >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.FunctionBody >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
