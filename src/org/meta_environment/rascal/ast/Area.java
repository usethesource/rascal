package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Area extends AbstractAST
{
  public class Area extends Area
  {
/* "area" "(" beginLine:Expression "," beginColumn:Expression "," endLine:Expression "," endColumn:Expression "," offset:Expression "," length:Expression ")" -> Area {cons("Area")} */
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAreaArea (this);
    }
    private Expression beginLine;
    public Expression getbeginLine ()
    {
      return beginLine;
    }
    private void $setbeginLine (Expression x)
    {
      this.beginLine = x;
    }
    public Area setbeginLine (Expression x)
    {
      Area z = new Area ();
      z.$setbeginLine (x);
      return z;
    }
    private Expression beginColumn;
    public Expression getbeginColumn ()
    {
      return beginColumn;
    }
    private void $setbeginColumn (Expression x)
    {
      this.beginColumn = x;
    }
    public Area setbeginColumn (Expression x)
    {
      Area z = new Area ();
      z.$setbeginColumn (x);
      return z;
    }
    private Expression endLine;
    public Expression getendLine ()
    {
      return endLine;
    }
    private void $setendLine (Expression x)
    {
      this.endLine = x;
    }
    public Area setendLine (Expression x)
    {
      Area z = new Area ();
      z.$setendLine (x);
      return z;
    }
    private Expression endColumn;
    public Expression getendColumn ()
    {
      return endColumn;
    }
    private void $setendColumn (Expression x)
    {
      this.endColumn = x;
    }
    public Area setendColumn (Expression x)
    {
      Area z = new Area ();
      z.$setendColumn (x);
      return z;
    }
    private Expression offset;
    public Expression getoffset ()
    {
      return offset;
    }
    private void $setoffset (Expression x)
    {
      this.offset = x;
    }
    public Area setoffset (Expression x)
    {
      Area z = new Area ();
      z.$setoffset (x);
      return z;
    }
    private Expression length;
    public Expression getlength ()
    {
      return length;
    }
    private void $setlength (Expression x)
    {
      this.length = x;
    }
    public Area setlength (Expression x)
    {
      Area z = new Area ();
      z.$setlength (x);
      return z;
    }
  }
  public class Ambiguity extends Area
  {
    private final List < Area > alternatives;
    public Ambiguity (List < Area > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Area > getAlternatives ()
    {
      return alternatives;
    }
  }
}
