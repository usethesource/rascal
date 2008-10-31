package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Catch extends AbstractAST
{
  public class Default extends Catch
  {
/* "catch" body:Statement -> Catch {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, Statement body)
    {
      this.tree = tree;
      this.body = body;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCatchDefault (this);
    }
    private Statement body;
    public Statement getBody ()
    {
      return body;
    }
    private void $setBody (Statement x)
    {
      this.body = x;
    }
    public Default setBody (Statement x)
    {
      Default z = new Default ();
      z.$setBody (x);
      return z;
    }
  }
  public class Ambiguity extends Catch
  {
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
  public class Binding extends Catch
  {
/* "catch" "(" type:Type name:Name ")" body:Statement -> Catch {cons("Binding")} */
    private Binding ()
    {
    }
    /*package */ Binding (ITree tree, Type type, Name name, Statement body)
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
    private Type type;
    public Type getType ()
    {
      return type;
    }
    private void $setType (Type x)
    {
      this.type = x;
    }
    public Binding setType (Type x)
    {
      Binding z = new Binding ();
      z.$setType (x);
      return z;
    }
    private Name name;
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public Binding setName (Name x)
    {
      Binding z = new Binding ();
      z.$setName (x);
      return z;
    }
    private Statement body;
    public Statement getBody ()
    {
      return body;
    }
    private void $setBody (Statement x)
    {
      this.body = x;
    }
    public Binding setBody (Statement x)
    {
      Binding z = new Binding ();
      z.$setBody (x);
      return z;
    }
  }
}
