package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Signature extends AbstractAST
{
  static public class NoThrows extends Signature
  {
/* type:Type modifiers:FunctionModifiers name:FunctionName parameters:Parameters -> Signature {cons("NoThrows")} */
    private NoThrows ()
    {
    }
    /*package */ NoThrows (ITree tree,
			   org.meta_environment.rascal.ast.Type type,
			   org.meta_environment.rascal.ast.
			   FunctionModifiers modifiers,
			   org.meta_environment.rascal.ast.FunctionName name,
			   org.meta_environment.rascal.ast.
			   Parameters parameters)
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
    private org.meta_environment.rascal.ast.Type type;
    public org.meta_environment.rascal.ast.Type getType ()
    {
      return type;
    }
    private void $setType (org.meta_environment.rascal.ast.Type x)
    {
      this.type = x;
    }
    public NoThrows setType (org.meta_environment.rascal.ast.Type x)
    {
      NoThrows z = new NoThrows ();
      z.$setType (x);
      return z;
    }
    private org.meta_environment.rascal.ast.FunctionModifiers modifiers;
    public org.meta_environment.rascal.ast.FunctionModifiers getModifiers ()
    {
      return modifiers;
    }
    private void $setModifiers (org.meta_environment.rascal.ast.
				FunctionModifiers x)
    {
      this.modifiers = x;
    }
    public NoThrows setModifiers (org.meta_environment.rascal.ast.
				  FunctionModifiers x)
    {
      NoThrows z = new NoThrows ();
      z.$setModifiers (x);
      return z;
    }
    private org.meta_environment.rascal.ast.FunctionName name;
    public org.meta_environment.rascal.ast.FunctionName getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.FunctionName x)
    {
      this.name = x;
    }
    public NoThrows setName (org.meta_environment.rascal.ast.FunctionName x)
    {
      NoThrows z = new NoThrows ();
      z.$setName (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Parameters parameters;
    public org.meta_environment.rascal.ast.Parameters getParameters ()
    {
      return parameters;
    }
    private void $setParameters (org.meta_environment.rascal.ast.Parameters x)
    {
      this.parameters = x;
    }
    public NoThrows setParameters (org.meta_environment.rascal.ast.
				   Parameters x)
    {
      NoThrows z = new NoThrows ();
      z.$setParameters (x);
      return z;
    }
  }
  static public class Ambiguity extends Signature
  {
    private final java.util.List < org.meta_environment.rascal.ast.Signature >
      alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.Signature >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.Signature >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class WithThrows extends Signature
  {
/* type:Type modifiers:FunctionModifiers name:FunctionName parameters:Parameters "throws" exceptions:{Type ","}+ -> Signature {cons("WithThrows")} */
    private WithThrows ()
    {
    }
    /*package */ WithThrows (ITree tree,
			     org.meta_environment.rascal.ast.Type type,
			     org.meta_environment.rascal.ast.
			     FunctionModifiers modifiers,
			     org.meta_environment.rascal.ast.
			     FunctionName name,
			     org.meta_environment.rascal.ast.
			     Parameters parameters,
			     java.util.List <
			     org.meta_environment.rascal.ast.Type >
			     exceptions)
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
    private org.meta_environment.rascal.ast.Type type;
    public org.meta_environment.rascal.ast.Type getType ()
    {
      return type;
    }
    private void $setType (org.meta_environment.rascal.ast.Type x)
    {
      this.type = x;
    }
    public WithThrows setType (org.meta_environment.rascal.ast.Type x)
    {
      WithThrows z = new WithThrows ();
      z.$setType (x);
      return z;
    }
    private org.meta_environment.rascal.ast.FunctionModifiers modifiers;
    public org.meta_environment.rascal.ast.FunctionModifiers getModifiers ()
    {
      return modifiers;
    }
    private void $setModifiers (org.meta_environment.rascal.ast.
				FunctionModifiers x)
    {
      this.modifiers = x;
    }
    public WithThrows setModifiers (org.meta_environment.rascal.ast.
				    FunctionModifiers x)
    {
      WithThrows z = new WithThrows ();
      z.$setModifiers (x);
      return z;
    }
    private org.meta_environment.rascal.ast.FunctionName name;
    public org.meta_environment.rascal.ast.FunctionName getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.FunctionName x)
    {
      this.name = x;
    }
    public WithThrows setName (org.meta_environment.rascal.ast.FunctionName x)
    {
      WithThrows z = new WithThrows ();
      z.$setName (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Parameters parameters;
    public org.meta_environment.rascal.ast.Parameters getParameters ()
    {
      return parameters;
    }
    private void $setParameters (org.meta_environment.rascal.ast.Parameters x)
    {
      this.parameters = x;
    }
    public WithThrows setParameters (org.meta_environment.rascal.ast.
				     Parameters x)
    {
      WithThrows z = new WithThrows ();
      z.$setParameters (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Type >
      exceptions;
    public java.util.List < org.meta_environment.rascal.ast.Type >
      getExceptions ()
    {
      return exceptions;
    }
    private void $setExceptions (java.util.List <
				 org.meta_environment.rascal.ast.Type > x)
    {
      this.exceptions = x;
    }
    public WithThrows setExceptions (java.util.List <
				     org.meta_environment.rascal.ast.Type > x)
    {
      WithThrows z = new WithThrows ();
      z.$setExceptions (x);
      return z;
    }
  }
}
