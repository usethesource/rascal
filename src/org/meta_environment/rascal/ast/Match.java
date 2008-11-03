package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Match extends AbstractAST
{
  public org.meta_environment.rascal.ast.Expression getMatch ()
  {
    throw new UnsupportedOperationException ();
  }
  public org.meta_environment.rascal.ast.Expression getReplacement ()
  {
    throw new UnsupportedOperationException ();
  }
  static public class Replacing extends Match
  {
/* match:Expression "=>" replacement:Expression -> Match {cons("Replacing")} */
    private Replacing ()
    {
    }
    /*package */ Replacing (ITree tree,
			    org.meta_environment.rascal.ast.Expression match,
			    org.meta_environment.rascal.ast.
			    Expression replacement)
    {
      this.tree = tree;
      this.match = match;
      this.replacement = replacement;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitMatchReplacing (this);
    }
    private org.meta_environment.rascal.ast.Expression match;
    public org.meta_environment.rascal.ast.Expression getMatch ()
    {
      return match;
    }
    private void $setMatch (org.meta_environment.rascal.ast.Expression x)
    {
      this.match = x;
    }
    public Replacing setMatch (org.meta_environment.rascal.ast.Expression x)
    {
      Replacing z = new Replacing ();
      z.$setMatch (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression replacement;
    public org.meta_environment.rascal.ast.Expression getReplacement ()
    {
      return replacement;
    }
    private void $setReplacement (org.meta_environment.rascal.ast.
				  Expression x)
    {
      this.replacement = x;
    }
    public Replacing setReplacement (org.meta_environment.rascal.ast.
				     Expression x)
    {
      Replacing z = new Replacing ();
      z.$setReplacement (x);
      return z;
    }
  }
  static public class Ambiguity extends Match
  {
    private final java.util.List < org.meta_environment.rascal.ast.Match >
      alternatives;
    public Ambiguity (java.util.List < org.meta_environment.rascal.ast.Match >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.Match >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  public org.meta_environment.rascal.ast.Statement getStatement ()
  {
    throw new UnsupportedOperationException ();
  }
  static public class Arbitrary extends Match
  {
/* match:Expression ":" statement:Statement -> Match {cons("Arbitrary")} */
    private Arbitrary ()
    {
    }
    /*package */ Arbitrary (ITree tree,
			    org.meta_environment.rascal.ast.Expression match,
			    org.meta_environment.rascal.ast.
			    Statement statement)
    {
      this.tree = tree;
      this.match = match;
      this.statement = statement;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitMatchArbitrary (this);
    }
    private org.meta_environment.rascal.ast.Expression match;
    public org.meta_environment.rascal.ast.Expression getMatch ()
    {
      return match;
    }
    private void $setMatch (org.meta_environment.rascal.ast.Expression x)
    {
      this.match = x;
    }
    public Arbitrary setMatch (org.meta_environment.rascal.ast.Expression x)
    {
      Arbitrary z = new Arbitrary ();
      z.$setMatch (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Statement statement;
    public org.meta_environment.rascal.ast.Statement getStatement ()
    {
      return statement;
    }
    private void $setStatement (org.meta_environment.rascal.ast.Statement x)
    {
      this.statement = x;
    }
    public Arbitrary setStatement (org.meta_environment.rascal.ast.
				   Statement x)
    {
      Arbitrary z = new Arbitrary ();
      z.$setStatement (x);
      return z;
    }
  }
}
