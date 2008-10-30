package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Signature extends AbstractAST
{
  public class NoThrows extends Signature
  {
/* type:Type modifiers:FunctionModifiers name:FunctionName parameters:Parameters -> Signature {cons("NoThrows")} */
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSignatureNoThrows (this);
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
    public NoThrows setType (Type x)
    {
      NoThrows z = new NoThrows ();
      z.$setType (x);
      return z;
    }
    private FunctionModifiers modifiers;
    public FunctionModifiers getModifiers ()
    {
      return modifiers;
    }
    private void $setModifiers (FunctionModifiers x)
    {
      this.modifiers = x;
    }
    public NoThrows setModifiers (FunctionModifiers x)
    {
      NoThrows z = new NoThrows ();
      z.$setModifiers (x);
      return z;
    }
    private FunctionName name;
    public FunctionName getName ()
    {
      return name;
    }
    private void $setName (FunctionName x)
    {
      this.name = x;
    }
    public NoThrows setName (FunctionName x)
    {
      NoThrows z = new NoThrows ();
      z.$setName (x);
      return z;
    }
    private Parameters parameters;
    public Parameters getParameters ()
    {
      return parameters;
    }
    private void $setParameters (Parameters x)
    {
      this.parameters = x;
    }
    public NoThrows setParameters (Parameters x)
    {
      NoThrows z = new NoThrows ();
      z.$setParameters (x);
      return z;
    }
  }
  public class Ambiguity extends Signature
  {
    private final java.util.List < Signature > alternatives;
    public Ambiguity (java.util.List < Signature > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Signature > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class WithThrows extends Signature
  {
/* type:Type modifiers:FunctionModifiers name:FunctionName parameters:Parameters "throws" exceptions:{Type ","}+ -> Signature {cons("WithThrows")} */
    private WithThrows ()
    {
    }
    /*package */ WithThrows (ITree tree, Type type,
			     FunctionModifiers modifiers, FunctionName name,
			     Parameters parameters,
			     java.util.List < Type > exceptions)
    {
      this.tree = tree;
      this.type = type;
      this.modifiers = modifiers;
      this.name = name;
      this.parameters = parameters;
    params2statements (java.util.List < Type > exceptions)}
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSignatureWithThrows (this);
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
    public WithThrows setType (Type x)
    {
      WithThrows z = new WithThrows ();
      z.$setType (x);
      return z;
    }
    private FunctionModifiers modifiers;
    public FunctionModifiers getModifiers ()
    {
      return modifiers;
    }
    private void $setModifiers (FunctionModifiers x)
    {
      this.modifiers = x;
    }
    public WithThrows setModifiers (FunctionModifiers x)
    {
      WithThrows z = new WithThrows ();
      z.$setModifiers (x);
      return z;
    }
    private FunctionName name;
    public FunctionName getName ()
    {
      return name;
    }
    private void $setName (FunctionName x)
    {
      this.name = x;
    }
    public WithThrows setName (FunctionName x)
    {
      WithThrows z = new WithThrows ();
      z.$setName (x);
      return z;
    }
    private Parameters parameters;
    public Parameters getParameters ()
    {
      return parameters;
    }
    private void $setParameters (Parameters x)
    {
      this.parameters = x;
    }
    public WithThrows setParameters (Parameters x)
    {
      WithThrows z = new WithThrows ();
      z.$setParameters (x);
      return z;
    }
    private java.util.List < Type > exceptions;
    public java.util.List < Type > getExceptions ()
    {
      return exceptions;
    }
    private void $setExceptions (java.util.List < Type > x)
    {
      this.exceptions = x;
    }
    public WithThrows setExceptions (java.util.List < Type > x)
    {
      WithThrows z = new WithThrows ();
      z.$setExceptions (x);
      return z;
    }
  }
}
