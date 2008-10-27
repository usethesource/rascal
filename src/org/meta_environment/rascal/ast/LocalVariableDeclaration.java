package org.meta_environment.rascal.ast;
public abstract class LocalVariableDeclaration extends AbstractAST
{
  public class DefaultScope extends LocalVariableDeclaration
  {
    private Type type;
    private List < Variable > variables;

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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultScopeLocalVariableDeclaration (this);
    }
    private final Type type;
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
      z = new DefaultScope ();
      z.privateSettype (x);
      return z;
    }
    private final List < Variable > variables;
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
      z = new DefaultScope ();
      z.privateSetvariables (x);
      return z;
    }
  }
  public class GivenScope extends LocalVariableDeclaration
  {
    private Scope scope;
    private Type type;
    private List < Variable > variables;

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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitGivenScopeLocalVariableDeclaration (this);
    }
    private final Scope scope;
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
      z = new GivenScope ();
      z.privateSetscope (x);
      return z;
    }
    private final Type type;
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
      z = new GivenScope ();
      z.privateSettype (x);
      return z;
    }
    private final List < Variable > variables;
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
      z = new GivenScope ();
      z.privateSetvariables (x);
      return z;
    }
  }
}
