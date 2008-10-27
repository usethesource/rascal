package org.meta_environment.rascal.ast;
public abstract class Area extends AbstractAST
{
  public class Area extends Area
  {
    private Expression beginLine;
    private Expression beginColumn;
    private Expression endLine;
    private Expression endColumn;
    private Expression offset;
    private Expression length;

    private Area ()
    {
    }
    /*package */ Area (ITree tree, Expression beginLine,
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAreaArea (this);
    }
    private final Expression beginLine;
    public Expression getbeginLine ()
    {
      return beginLine;
    }
    private void privateSetbeginLine (Expression x)
    {
      this.beginLine = x;
    }
    public Area setbeginLine (Expression x)
    {
      z = new Area ();
      z.privateSetbeginLine (x);
      return z;
    }
    private final Expression beginColumn;
    public Expression getbeginColumn ()
    {
      return beginColumn;
    }
    private void privateSetbeginColumn (Expression x)
    {
      this.beginColumn = x;
    }
    public Area setbeginColumn (Expression x)
    {
      z = new Area ();
      z.privateSetbeginColumn (x);
      return z;
    }
    private final Expression endLine;
    public Expression getendLine ()
    {
      return endLine;
    }
    private void privateSetendLine (Expression x)
    {
      this.endLine = x;
    }
    public Area setendLine (Expression x)
    {
      z = new Area ();
      z.privateSetendLine (x);
      return z;
    }
    private final Expression endColumn;
    public Expression getendColumn ()
    {
      return endColumn;
    }
    private void privateSetendColumn (Expression x)
    {
      this.endColumn = x;
    }
    public Area setendColumn (Expression x)
    {
      z = new Area ();
      z.privateSetendColumn (x);
      return z;
    }
    private final Expression offset;
    public Expression getoffset ()
    {
      return offset;
    }
    private void privateSetoffset (Expression x)
    {
      this.offset = x;
    }
    public Area setoffset (Expression x)
    {
      z = new Area ();
      z.privateSetoffset (x);
      return z;
    }
    private final Expression length;
    public Expression getlength ()
    {
      return length;
    }
    private void privateSetlength (Expression x)
    {
      this.length = x;
    }
    public Area setlength (Expression x)
    {
      z = new Area ();
      z.privateSetlength (x);
      return z;
    }
  }
}
