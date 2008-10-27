package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Catch extends AbstractAST
{
  public class Catch extends Catch
  {
/* "catch" body:Statement -> Catch {cons("Catch")} */
    private Catch ()
    {
    }
    /*package */ Catch (ITree tree, Statement body)
    {
      this.tree = tree;
      this.body = body;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCatchCatch (this);
    }
    private Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void privateSetbody (Statement x)
    {
      this.body = x;
    }
    public Catch setbody (Statement x)
    {
      Catch z = new Catch ();
      z.privateSetbody (x);
      return z;
    }
  }
  public class BindingCatch extends Catch
  {
/* "catch" "(" type:Type name:Name ")" body:Statement -> Catch {cons("BindingCatch")} */
    private BindingCatch ()
    {
    }
    /*package */ BindingCatch (ITree tree, Type type, Name name,
			       Statement body)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
      this.body = body;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBindingCatchCatch (this);
    }
    private Type type;
    public Type gettype ()
    {
      return type;
    }
    private void privateSettype (Type x)
    {
      this.type = x;
    }
    public BindingCatch settype (Type x)
    {
      BindingCatch z = new BindingCatch ();
      z.privateSettype (x);
      return z;
    }
    private Name name;
    public Name getname ()
    {
      return name;
    }
    private void privateSetname (Name x)
    {
      this.name = x;
    }
    public BindingCatch setname (Name x)
    {
      BindingCatch z = new BindingCatch ();
      z.privateSetname (x);
      return z;
    }
    private Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void privateSetbody (Statement x)
    {
      this.body = x;
    }
    public BindingCatch setbody (Statement x)
    {
      BindingCatch z = new BindingCatch ();
      z.privateSetbody (x);
      return z;
    }
  }
}
