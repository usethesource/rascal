package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Area extends AbstractAST
{
  public org.meta_environment.rascal.ast.Expression getBeginLine ()
  {
    throw new UnsupportedOperationException ();
  }
  public org.meta_environment.rascal.ast.Expression getBeginColumn ()
  {
    throw new UnsupportedOperationException ();
  }
  public org.meta_environment.rascal.ast.Expression getEndLine ()
  {
    throw new UnsupportedOperationException ();
  }
  public org.meta_environment.rascal.ast.Expression getEndColumn ()
  {
    throw new UnsupportedOperationException ();
  }
  public org.meta_environment.rascal.ast.Expression getOffset ()
  {
    throw new UnsupportedOperationException ();
  }
  public org.meta_environment.rascal.ast.Expression getLength ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasBeginLine ()
  {
    return false;
  }
  public boolean hasBeginColumn ()
  {
    return false;
  }
  public boolean hasEndLine ()
  {
    return false;
  }
  public boolean hasEndColumn ()
  {
    return false;
  }
  public boolean hasOffset ()
  {
    return false;
  }
  public boolean hasLength ()
  {
    return false;
  }
  public boolean isDefault ()
  {
    return false;
  }
  static public class Default extends Area
  {
/* "area" "(" beginLine:Expression "," beginColumn:Expression "," endLine:Expression "," endColumn:Expression "," offset:Expression "," length:Expression ")" -> Area {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  org.meta_environment.rascal.ast.
			  Expression beginLine,
			  org.meta_environment.rascal.ast.
			  Expression beginColumn,
			  org.meta_environment.rascal.ast.Expression endLine,
			  org.meta_environment.rascal.ast.
			  Expression endColumn,
			  org.meta_environment.rascal.ast.Expression offset,
			  org.meta_environment.rascal.ast.Expression length)
    {
      this.tree = tree;
      this.beginLine = beginLine;
      this.beginColumn = beginColumn;
      this.endLine = endLine;
      this.endColumn = endColumn;
      this.offset = offset;
      this.length = length;
    }
    public < T > T accept (IASTVisitor < T > visitor)
    {
      return visitor.visitAreaDefault (this);
    }

    public boolean isDefault ()
    {
      return true;
    }

    public boolean hasBeginLine ()
    {
      return true;
    }
    public boolean hasBeginColumn ()
    {
      return true;
    }
    public boolean hasEndLine ()
    {
      return true;
    }
    public boolean hasEndColumn ()
    {
      return true;
    }
    public boolean hasOffset ()
    {
      return true;
    }
    public boolean hasLength ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Expression beginLine;
    public org.meta_environment.rascal.ast.Expression getBeginLine ()
    {
      return beginLine;
    }
    private void $setBeginLine (org.meta_environment.rascal.ast.Expression x)
    {
      this.beginLine = x;
    }
    public Default setBeginLine (org.meta_environment.rascal.ast.Expression x)
    {
      Default z = new Default ();
      z.$setBeginLine (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression beginColumn;
    public org.meta_environment.rascal.ast.Expression getBeginColumn ()
    {
      return beginColumn;
    }
    private void $setBeginColumn (org.meta_environment.rascal.ast.
				  Expression x)
    {
      this.beginColumn = x;
    }
    public Default setBeginColumn (org.meta_environment.rascal.ast.
				   Expression x)
    {
      Default z = new Default ();
      z.$setBeginColumn (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression endLine;
    public org.meta_environment.rascal.ast.Expression getEndLine ()
    {
      return endLine;
    }
    private void $setEndLine (org.meta_environment.rascal.ast.Expression x)
    {
      this.endLine = x;
    }
    public Default setEndLine (org.meta_environment.rascal.ast.Expression x)
    {
      Default z = new Default ();
      z.$setEndLine (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression endColumn;
    public org.meta_environment.rascal.ast.Expression getEndColumn ()
    {
      return endColumn;
    }
    private void $setEndColumn (org.meta_environment.rascal.ast.Expression x)
    {
      this.endColumn = x;
    }
    public Default setEndColumn (org.meta_environment.rascal.ast.Expression x)
    {
      Default z = new Default ();
      z.$setEndColumn (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression offset;
    public org.meta_environment.rascal.ast.Expression getOffset ()
    {
      return offset;
    }
    private void $setOffset (org.meta_environment.rascal.ast.Expression x)
    {
      this.offset = x;
    }
    public Default setOffset (org.meta_environment.rascal.ast.Expression x)
    {
      Default z = new Default ();
      z.$setOffset (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression length;
    public org.meta_environment.rascal.ast.Expression getLength ()
    {
      return length;
    }
    private void $setLength (org.meta_environment.rascal.ast.Expression x)
    {
      this.length = x;
    }
    public Default setLength (org.meta_environment.rascal.ast.Expression x)
    {
      Default z = new Default ();
      z.$setLength (x);
      return z;
    }
  }
  static public class Ambiguity extends Area
  {
    private final java.util.List < org.meta_environment.rascal.ast.Area >
      alternatives;
    public Ambiguity (java.util.List < org.meta_environment.rascal.ast.Area >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.Area >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
