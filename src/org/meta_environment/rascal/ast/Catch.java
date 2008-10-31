package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Catch extends AbstractAST
{
  static public class Default extends Catch
  {
/* "catch" body:Statement -> Catch {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  org.meta_environment.rascal.ast.Statement body)
    {
      this.tree = tree;
      this.body = body;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCatchDefault (this);
    }
    private org.meta_environment.rascal.ast.Statement body;
    public org.meta_environment.rascal.ast.Statement getBody ()
    {
      return body;
    }
    private void $setBody (org.meta_environment.rascal.ast.Statement x)
    {
      this.body = x;
    }
    public Default setBody (org.meta_environment.rascal.ast.Statement x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setBody (x);
      return z;
    }
  }
  static public class Ambiguity extends Catch
  {
    public Catch.Ambiguity makeCatchAmbiguity (java.util.List < Catch >
					       alternatives)
    {
      Catch.Ambiguity amb = new Catch.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Catch.Ambiguity) table.get (amb);
    }
    private final java.util.List < Catch > alternatives;
    public Ambiguity (java.util.List < Catch > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Catch > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Binding extends Catch
  {
/* "catch" "(" type:Type name:Name ")" body:Statement -> Catch {cons("Binding")} */
    private Binding ()
    {
    }
    /*package */ Binding (ITree tree,
			  org.meta_environment.rascal.ast.Type type,
			  org.meta_environment.rascal.ast.Name name,
			  org.meta_environment.rascal.ast.Statement body)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
      this.body = body;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCatchBinding (this);
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
    public Binding setType (org.meta_environment.rascal.ast.Type x)
    {
      org.meta_environment.rascal.ast.Binding z = new Binding ();
      z.$setType (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Name name;
    public org.meta_environment.rascal.ast.Name getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.Name x)
    {
      this.name = x;
    }
    public Binding setName (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.Binding z = new Binding ();
      z.$setName (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Statement body;
    public org.meta_environment.rascal.ast.Statement getBody ()
    {
      return body;
    }
    private void $setBody (org.meta_environment.rascal.ast.Statement x)
    {
      this.body = x;
    }
    public Binding setBody (org.meta_environment.rascal.ast.Statement x)
    {
      org.meta_environment.rascal.ast.Binding z = new Binding ();
      z.$setBody (x);
      return z;
    }
  }
}
