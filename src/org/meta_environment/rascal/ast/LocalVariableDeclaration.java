package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class LocalVariableDeclaration extends AbstractAST
{
  public class DefaultScope extends LocalVariableDeclaration
  {
/* type:Type variables:{Variable ","}+ -> LocalVariableDeclaration {cons("DefaultScope")} */
    private DefaultScope ()
    {
    }
    /*package */ DefaultScope (ITree tree, Type type,
			       List < Variable > variables)
    {
      this.tree = tree;
      this.type = type;
      this.variables = variables;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDefaultScopeLocalVariableDeclaration (this);
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
    public DefaultScope settype (Type x)
    {
      DefaultScope z = new DefaultScope ();
      z.privateSettype (x);
      return z;
    }
    private List < Variable > variables;
    public List < Variable > getvariables ()
    {
      return variables;
    }
    private void privateSetvariables (List < Variable > x)
    {
      this.variables = x;
    }
    public DefaultScope setvariables (List < Variable > x)
    {
      DefaultScope z = new DefaultScope ();
      z.privateSetvariables (x);
      return z;
    }
  }
  public class GivenScope extends LocalVariableDeclaration
  {
/* scope:Scope type:Type variables:{Variable ","}+ -> LocalVariableDeclaration {cons("GivenScope")} */
    private GivenScope ()
    {
    }
    /*package */ GivenScope (ITree tree, Scope scope, Type type,
			     List < Variable > variables)
    {
      this.tree = tree;
      this.scope = scope;
      this.type = type;
      this.variables = variables;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitGivenScopeLocalVariableDeclaration (this);
    }
    private Scope scope;
    public Scope getscope ()
    {
      return scope;
    }
    private void privateSetscope (Scope x)
    {
      this.scope = x;
    }
    public GivenScope setscope (Scope x)
    {
      GivenScope z = new GivenScope ();
      z.privateSetscope (x);
      return z;
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
    public GivenScope settype (Type x)
    {
      GivenScope z = new GivenScope ();
      z.privateSettype (x);
      return z;
    }
    private List < Variable > variables;
    public List < Variable > getvariables ()
    {
      return variables;
    }
    private void privateSetvariables (List < Variable > x)
    {
      this.variables = x;
    }
    public GivenScope setvariables (List < Variable > x)
    {
      GivenScope z = new GivenScope ();
      z.privateSetvariables (x);
      return z;
    }
  }
}
