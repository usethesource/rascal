package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
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
    public Type gettype ()
    {
      return type;
    }
    private void $settype (Type x)
    {
      this.type = x;
    }
    public NoThrows settype (Type x)
    {
      NoThrows z = new NoThrows ();
      z.$settype (x);
      return z;
    }
    private FunctionModifiers modifiers;
    public FunctionModifiers getmodifiers ()
    {
      return modifiers;
    }
    private void $setmodifiers (FunctionModifiers x)
    {
      this.modifiers = x;
    }
    public NoThrows setmodifiers (FunctionModifiers x)
    {
      NoThrows z = new NoThrows ();
      z.$setmodifiers (x);
      return z;
    }
    private FunctionName name;
    public FunctionName getname ()
    {
      return name;
    }
    private void $setname (FunctionName x)
    {
      this.name = x;
    }
    public NoThrows setname (FunctionName x)
    {
      NoThrows z = new NoThrows ();
      z.$setname (x);
      return z;
    }
    private Parameters parameters;
    public Parameters getparameters ()
    {
      return parameters;
    }
    private void $setparameters (Parameters x)
    {
      this.parameters = x;
    }
    public NoThrows setparameters (Parameters x)
    {
      NoThrows z = new NoThrows ();
      z.$setparameters (x);
      return z;
    }
  }
  public class Ambiguity extends Signature
  {
    private final List < Signature > alternatives;
    public Ambiguity (List < Signature > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Signature > getAlternatives ()
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
			     Parameters parameters, List < Type > exceptions)
    {
      this.tree = tree;
      this.type = type;
      this.modifiers = modifiers;
      this.name = name;
      this.parameters = parameters;
      this.exceptions = exceptions;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSignatureWithThrows (this);
    }
    private Type type;
    public Type gettype ()
    {
      return type;
    }
    private void $settype (Type x)
    {
      this.type = x;
    }
    public WithThrows settype (Type x)
    {
      WithThrows z = new WithThrows ();
      z.$settype (x);
      return z;
    }
    private FunctionModifiers modifiers;
    public FunctionModifiers getmodifiers ()
    {
      return modifiers;
    }
    private void $setmodifiers (FunctionModifiers x)
    {
      this.modifiers = x;
    }
    public WithThrows setmodifiers (FunctionModifiers x)
    {
      WithThrows z = new WithThrows ();
      z.$setmodifiers (x);
      return z;
    }
    private FunctionName name;
    public FunctionName getname ()
    {
      return name;
    }
    private void $setname (FunctionName x)
    {
      this.name = x;
    }
    public WithThrows setname (FunctionName x)
    {
      WithThrows z = new WithThrows ();
      z.$setname (x);
      return z;
    }
    private Parameters parameters;
    public Parameters getparameters ()
    {
      return parameters;
    }
    private void $setparameters (Parameters x)
    {
      this.parameters = x;
    }
    public WithThrows setparameters (Parameters x)
    {
      WithThrows z = new WithThrows ();
      z.$setparameters (x);
      return z;
    }
    private List < Type > exceptions;
    public List < Type > getexceptions ()
    {
      return exceptions;
    }
    private void $setexceptions (List < Type > x)
    {
      this.exceptions = x;
    }
    public WithThrows setexceptions (List < Type > x)
    {
      WithThrows z = new WithThrows ();
      z.$setexceptions (x);
      return z;
    }
  }
}
