package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class DateTimeLiteral extends AbstractAST { 
  public org.meta_environment.rascal.ast.JustDate getDate() { throw new UnsupportedOperationException(); }
public boolean hasDate() { return false; }
public boolean isDateLiteral() { return false; }
static public class DateLiteral extends DateTimeLiteral {
/** date:JustDate -> DateTimeLiteral {prefer, cons("DateLiteral")} */
	public DateLiteral(INode node, org.meta_environment.rascal.ast.JustDate date) {
		this.node = node;
		this.date = date;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDateTimeLiteralDateLiteral(this);
	}

	public boolean isDateLiteral() { return true; }

	public boolean hasDate() { return true; }

private final org.meta_environment.rascal.ast.JustDate date;
	public org.meta_environment.rascal.ast.JustDate getDate() { return date; }	
}
static public class Ambiguity extends DateTimeLiteral {
  private final java.util.List<org.meta_environment.rascal.ast.DateTimeLiteral> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.DateTimeLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.DateTimeLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitDateTimeLiteralAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.JustTime getTime() { throw new UnsupportedOperationException(); }
public boolean hasTime() { return false; }
public boolean isTimeLiteral() { return false; }
static public class TimeLiteral extends DateTimeLiteral {
/** time:JustTime -> DateTimeLiteral {prefer, cons("TimeLiteral")} */
	public TimeLiteral(INode node, org.meta_environment.rascal.ast.JustTime time) {
		this.node = node;
		this.time = time;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDateTimeLiteralTimeLiteral(this);
	}

	public boolean isTimeLiteral() { return true; }

	public boolean hasTime() { return true; }

private final org.meta_environment.rascal.ast.JustTime time;
	public org.meta_environment.rascal.ast.JustTime getTime() { return time; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.DateAndTime getDateAndTime() { throw new UnsupportedOperationException(); }
public boolean hasDateAndTime() { return false; }
public boolean isDateAndTimeLiteral() { return false; }
static public class DateAndTimeLiteral extends DateTimeLiteral {
/** dateAndTime:DateAndTime -> DateTimeLiteral {prefer, cons("DateAndTimeLiteral")} */
	public DateAndTimeLiteral(INode node, org.meta_environment.rascal.ast.DateAndTime dateAndTime) {
		this.node = node;
		this.dateAndTime = dateAndTime;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDateTimeLiteralDateAndTimeLiteral(this);
	}

	public boolean isDateAndTimeLiteral() { return true; }

	public boolean hasDateAndTime() { return true; }

private final org.meta_environment.rascal.ast.DateAndTime dateAndTime;
	public org.meta_environment.rascal.ast.DateAndTime getDateAndTime() { return dateAndTime; }	
}
}