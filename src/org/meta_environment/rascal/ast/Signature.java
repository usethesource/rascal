package org.meta_environment.rascal.ast;
public abstract class Signature extends AbstractAST
{
  public class NoThrows extends Signature
  {
    private Type type;
    private FunctionModifiers modifiers;
    private FunctionName name;
    private Parameters parameters;

    private NoThrows ()
    {
    }
    /*package */ NoThrows (ITree tree, Type type, FunctionModifiers modifiers,
			   FunctionName name, Parameters parameters)
    {
      this.tree = tree;
      this.type = type;
      this.modifiers = modifiers;
      this.name = name;
      this.parameters = parameters;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNoThrowsSignature (this);
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
    public NoThrows settype (Type x)
    {
      z = new NoThrows ();
      z.privateSettype (x);
      return z;
    }
    private final FunctionModifiers modifiers;
    public FunctionModifiers getmodifiers ()
    {
      return modifiers;
    }
    private void privateSetmodifiers (FunctionModifiers x)
    {
      this.modifiers = x;
    }
    public NoThrows setmodifiers (FunctionModifiers x)
    {
      z = new NoThrows ();
      z.privateSetmodifiers (x);
      return z;
    }
    private final FunctionName name;
    public FunctionName getname ()
    {
      return name;
    }
    private void privateSetname (FunctionName x)
    {
      this.name = x;
    }
    public NoThrows setname (FunctionName x)
    {
      z = new NoThrows ();
      z.privateSetname (x);
      return z;
    }
    private final Parameters parameters;
    public Parameters getparameters ()
    {
      return parameters;
    }
    private void privateSetparameters (Parameters x)
    {
      this.parameters = x;
    }
    public NoThrows setparameters (Parameters x)
    {
      z = new NoThrows ();
      z.privateSetparameters (x);
      return z;
    }
  }
  public class WithThrows extends Signature
  {
    private Type type;
    private FunctionModifiers modifiers;
    private FunctionName name;
    private Parameters parameters;
    private List < Type > exceptions;

    private WithThrows ()
    {
    }
    /*package */ WithThrows (ITree tree, Type type,
			     FunctionModifiers modifiers, FunctionName name,
			     Parameters parameters, List < Type > exceptions)
    {
      this.tree = tree;
      this.type = type;
      this.modifiers = modifiers;
      this.name = name;
      this.parameters = parameters;
      this.exceptions = exceptions;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitWithThrowsSignature (this);
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
    public WithThrows settype (Type x)
    {
      z = new WithThrows ();
      z.privateSettype (x);
      return z;
    }
    private final FunctionModifiers modifiers;
    public FunctionModifiers getmodifiers ()
    {
      return modifiers;
    }
    private void privateSetmodifiers (FunctionModifiers x)
    {
      this.modifiers = x;
    }
    public WithThrows setmodifiers (FunctionModifiers x)
    {
      z = new WithThrows ();
      z.privateSetmodifiers (x);
      return z;
    }
    private final FunctionName name;
    public FunctionName getname ()
    {
      return name;
    }
    private void privateSetname (FunctionName x)
    {
      this.name = x;
    }
    public WithThrows setname (FunctionName x)
    {
      z = new WithThrows ();
      z.privateSetname (x);
      return z;
    }
    private final Parameters parameters;
    public Parameters getparameters ()
    {
      return parameters;
    }
    private void privateSetparameters (Parameters x)
    {
      this.parameters = x;
    }
    public WithThrows setparameters (Parameters x)
    {
      z = new WithThrows ();
      z.privateSetparameters (x);
      return z;
    }
    private final List < Type > exceptions;
    public List < Type > getexceptions ()
    {
      return exceptions;
    }
    private void privateSetexceptions (List < Type > x)
    {
      this.exceptions = x;
    }
    public WithThrows setexceptions (List < Type > x)
    {
      z = new WithThrows ();
      z.privateSetexceptions (x);
      return z;
    }
  }
}
