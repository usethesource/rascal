package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Declarator extends AbstractAST
{
  public class Default extends Declarator
  {
/* type:Type variables:{Variable ","}+ -> Declarator {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, Type type, List < Variable > variables)
    {
      this.tree = tree;
      this.type = type;
      this.variables = variables;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDeclaratorDefault (this);
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
    public Default settype (Type x)
    {
      Default z = new Default ();
      z.$settype (x);
      return z;
    }
    private List < Variable > variables;
    public List < Variable > getvariables ()
    {
      return variables;
    }
    private void $setvariables (List < Variable > x)
    {
      this.variables = x;
    }
    public Default setvariables (List < Variable > x)
    {
      Default z = new Default ();
      z.$setvariables (x);
      return z;
    }
  }
  public class Ambiguity extends Declarator
  {
    private final List < Declarator > alternatives;
    public Ambiguity (List < Declarator > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Declarator > getAlternatives ()
    {
      return alternatives;
    }
  }
}
