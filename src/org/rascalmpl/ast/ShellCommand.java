package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class ShellCommand extends AbstractAST { 
  public boolean isHelp() { return false; }
static public class Help extends ShellCommand {
/** "help" -> ShellCommand {cons("Help")} */
	protected Help(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandHelp(this);
	}

	public boolean isHelp() { return true; }	
}
static public class Ambiguity extends ShellCommand {
  private final java.util.List<org.rascalmpl.ast.ShellCommand> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ShellCommand> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.ShellCommand> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitShellCommandAmbiguity(this);
  }
} 
public boolean isQuit() { return false; }
static public class Quit extends ShellCommand {
/** "quit" -> ShellCommand {cons("Quit")} */
	protected Quit(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandQuit(this);
	}

	public boolean isQuit() { return true; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.rascalmpl.ast.QualifiedName getName() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean isEdit() { return false; }
static public class Edit extends ShellCommand {
/** "edit" name:QualifiedName -> ShellCommand {cons("Edit")} */
	protected Edit(INode node, org.rascalmpl.ast.QualifiedName name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandEdit(this);
	}

	public boolean isEdit() { return true; }

	public boolean hasName() { return true; }

private final org.rascalmpl.ast.QualifiedName name;
	public org.rascalmpl.ast.QualifiedName getName() { return name; }	
} 
public boolean isListModules() { return false; }
static public class ListModules extends ShellCommand {
/** "modules" -> ShellCommand {cons("ListModules")} */
	protected ListModules(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandListModules(this);
	}

	public boolean isListModules() { return true; }	
} 
public boolean isListDeclarations() { return false; }
static public class ListDeclarations extends ShellCommand {
/** "declarations" -> ShellCommand {cons("ListDeclarations")} */
	protected ListDeclarations(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandListDeclarations(this);
	}

	public boolean isListDeclarations() { return true; }	
} 
public boolean isTest() { return false; }
static public class Test extends ShellCommand {
/** "test" -> ShellCommand {cons("Test")} */
	protected Test(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandTest(this);
	}

	public boolean isTest() { return true; }	
} public boolean isUnimport() { return false; }
static public class Unimport extends ShellCommand {
/** "unimport" name:QualifiedName -> ShellCommand {cons("Unimport")} */
	protected Unimport(INode node, org.rascalmpl.ast.QualifiedName name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandUnimport(this);
	}

	public boolean isUnimport() { return true; }

	public boolean hasName() { return true; }

private final org.rascalmpl.ast.QualifiedName name;
	public org.rascalmpl.ast.QualifiedName getName() { return name; }	
} public boolean isUndeclare() { return false; }
static public class Undeclare extends ShellCommand {
/** "undeclare" name:QualifiedName -> ShellCommand {cons("Undeclare")} */
	protected Undeclare(INode node, org.rascalmpl.ast.QualifiedName name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandUndeclare(this);
	}

	public boolean isUndeclare() { return true; }

	public boolean hasName() { return true; }

private final org.rascalmpl.ast.QualifiedName name;
	public org.rascalmpl.ast.QualifiedName getName() { return name; }	
} 
public boolean isHistory() { return false; }
static public class History extends ShellCommand {
/** "history" -> ShellCommand {cons("History")} */
	protected History(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandHistory(this);
	}

	public boolean isHistory() { return true; }	
} public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public boolean hasExpression() { return false; }
public boolean isSetOption() { return false; }
static public class SetOption extends ShellCommand {
/** "set" name:QualifiedName expression:Expression -> ShellCommand {cons("SetOption")} */
	protected SetOption(INode node, org.rascalmpl.ast.QualifiedName name, org.rascalmpl.ast.Expression expression) {
		this.node = node;
		this.name = name;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandSetOption(this);
	}

	public boolean isSetOption() { return true; }

	public boolean hasName() { return true; }
	public boolean hasExpression() { return true; }

private final org.rascalmpl.ast.QualifiedName name;
	public org.rascalmpl.ast.QualifiedName getName() { return name; }
	private final org.rascalmpl.ast.Expression expression;
	public org.rascalmpl.ast.Expression getExpression() { return expression; }	
}
}