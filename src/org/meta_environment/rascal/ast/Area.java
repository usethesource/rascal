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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAreaDefault (this);
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
    public org.meta_environment.rascal.ast.Default setBeginLine (org.
								 meta_environment.
								 rascal.ast.
								 Expression x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
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
    public org.meta_environment.rascal.ast.Default setBeginColumn (org.
								   meta_environment.
								   rascal.ast.
								   Expression
								   x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
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
    public org.meta_environment.rascal.ast.Default setEndLine (org.
							       meta_environment.
							       rascal.ast.
							       Expression x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
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
    public org.meta_environment.rascal.ast.Default setEndColumn (org.
								 meta_environment.
								 rascal.ast.
								 Expression x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
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
    public org.meta_environment.rascal.ast.Default setOffset (org.
							      meta_environment.
							      rascal.ast.
							      Expression x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
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
    public org.meta_environment.rascal.ast.Default setLength (org.
							      meta_environment.
							      rascal.ast.
							      Expression x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setLength (x);
      return z;
    }
  }
  static public class Ambiguity extends Area
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
