package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Area extends AbstractAST { 
public class Area extends Area {
/* "area" "(" beginLine:Expression "," beginColumn:Expression "," endLine:Expression "," endColumn:Expression "," offset:Expression "," length:Expression ")" -> Area {cons("Area")} */
	private Area() { }
	/*package*/ Area(ITree tree, Expression beginLine, Expression beginColumn, Expression endLine, Expression endColumn, Expression offset, Expression length) {
		this.tree = tree;
		this.beginLine = beginLine;
		this.beginColumn = beginColumn;
		this.endLine = endLine;
		this.endColumn = endColumn;
		this.offset = offset;
		this.length = length;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitAreaArea(this);
	}
private Expression beginLine;
	public Expression getbeginLine() { return beginLine; }
	private void privateSetbeginLine(Expression x) { this.beginLine = x; }
	public Area setbeginLine(Expression x) { 
		Area z = new Area();
 		z.privateSetbeginLine(x);
		return z;
	}
	private Expression beginColumn;
	public Expression getbeginColumn() { return beginColumn; }
	private void privateSetbeginColumn(Expression x) { this.beginColumn = x; }
	public Area setbeginColumn(Expression x) { 
		Area z = new Area();
 		z.privateSetbeginColumn(x);
		return z;
	}
	private Expression endLine;
	public Expression getendLine() { return endLine; }
	private void privateSetendLine(Expression x) { this.endLine = x; }
	public Area setendLine(Expression x) { 
		Area z = new Area();
 		z.privateSetendLine(x);
		return z;
	}
	private Expression endColumn;
	public Expression getendColumn() { return endColumn; }
	private void privateSetendColumn(Expression x) { this.endColumn = x; }
	public Area setendColumn(Expression x) { 
		Area z = new Area();
 		z.privateSetendColumn(x);
		return z;
	}
	private Expression offset;
	public Expression getoffset() { return offset; }
	private void privateSetoffset(Expression x) { this.offset = x; }
	public Area setoffset(Expression x) { 
		Area z = new Area();
 		z.privateSetoffset(x);
		return z;
	}
	private Expression length;
	public Expression getlength() { return length; }
	private void privateSetlength(Expression x) { this.length = x; }
	public Area setlength(Expression x) { 
		Area z = new Area();
 		z.privateSetlength(x);
		return z;
	}	
}
public class Ambiguity extends Area {
  private final List<Area> alternatives;
  public Ambiguity(List<Area> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<Area> getAlternatives() {
	return alternatives;
  }
}
}