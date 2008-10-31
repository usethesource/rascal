package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Area extends AbstractAST
{
  static public class Default extends Area
  {
/* "area" "(" beginLine:Expression "," beginColumn:Expression "," endLine:Expression "," endColumn:Expression "," offset:Expression "," length:Expression ")" -> Area {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, Expression beginLine,
			  Expression beginColumn, Expression endLine,
			  Expression endColumn, Expression offset,
			  Expression length)
    {
      this.tree = tree;
      this.beginLine = beginLine;
      this.beginColumn = beginColumn;
      this.endLine = endLine;
      this.endColumn = endColumn;
      this.offset = offset;
      this.length = length;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAreaDefault (this);
    }
    private Expression beginLine;
    public Expression getBeginLine ()
    {
      return beginLine;
    }
    private void $setBeginLine (Expression x)
    {
      this.beginLine = x;
    }
    public Default setBeginLine (Expression x)
    {
      Default z = new Default ();
      z.$setBeginLine (x);
      return z;
    }
    private Expression beginColumn;
    public Expression getBeginColumn ()
    {
      return beginColumn;
    }
    private void $setBeginColumn (Expression x)
    {
      this.beginColumn = x;
    }
    public Default setBeginColumn (Expression x)
    {
      Default z = new Default ();
      z.$setBeginColumn (x);
      return z;
    }
    private Expression endLine;
    public Expression getEndLine ()
    {
      return endLine;
    }
    private void $setEndLine (Expression x)
    {
      this.endLine = x;
    }
    public Default setEndLine (Expression x)
    {
      Default z = new Default ();
      z.$setEndLine (x);
      return z;
    }
    private Expression endColumn;
    public Expression getEndColumn ()
    {
      return endColumn;
    }
    private void $setEndColumn (Expression x)
    {
      this.endColumn = x;
    }
    public Default setEndColumn (Expression x)
    {
      Default z = new Default ();
      z.$setEndColumn (x);
      return z;
    }
    private Expression offset;
    public Expression getOffset ()
    {
      return offset;
    }
    private void $setOffset (Expression x)
    {
      this.offset = x;
    }
    public Default setOffset (Expression x)
    {
      Default z = new Default ();
      z.$setOffset (x);
      return z;
    }
    private Expression length;
    public Expression getLength ()
    {
      return length;
    }
    private void $setLength (Expression x)
    {
      this.length = x;
    }
    public Default setLength (Expression x)
    {
      Default z = new Default ();
      z.$setLength (x);
      return z;
    }
  }
  public class Ambiguity extends Area
  {
    private final java.util.List < Area > alternatives;
    public Ambiguity (java.util.List < Area > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Area > getAlternatives ()
    {
      return alternatives;
    }
  }
}
