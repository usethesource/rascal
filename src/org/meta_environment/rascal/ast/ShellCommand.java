package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode;
public abstract class ShellCommand extends AbstractAST { 
  public boolean isHelp() { return false; }
static public class Help extends ShellCommand {
/** "help" -> ShellCommand {cons("Help")} */
	private Help() {
		super();
	}
	public Help(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandHelp(this);
	}

	public boolean isHelp() { return true; }	
}
static public class Ambiguity extends ShellCommand {
  private final java.util.List<org.meta_environment.rascal.ast.ShellCommand> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ShellCommand> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.ShellCommand> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitShellCommandAmbiguity(this);
  }
} 
public boolean isQuit() { return false; }
static public class Quit extends ShellCommand {
/** "quit" -> ShellCommand {cons("Quit")} */
	private Quit() {
		super();
	}
	public Quit(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandQuit(this);
	}

	public boolean isQuit() { return true; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.QualifiedName getName() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean isEdit() { return false; }
static public class Edit extends ShellCommand {
/** "edit" name:QualifiedName -> ShellCommand {cons("Edit")} */
	private Edit() {
		super();
	}
	public Edit(INode node, org.meta_environment.rascal.ast.QualifiedName name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandEdit(this);
	}

	public boolean isEdit() { return true; }

	public boolean hasName() { return true; }

private org.meta_environment.rascal.ast.QualifiedName name;
	public org.meta_environment.rascal.ast.QualifiedName getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.QualifiedName x) { this.name = x; }
	public Edit setName(org.meta_environment.rascal.ast.QualifiedName x) { 
		Edit z = new Edit();
 		z.$setName(x);
		return z;
	}	
} 
public boolean isListModules() { return false; }
static public class ListModules extends ShellCommand {
/** "modules" -> ShellCommand {cons("ListModules")} */
	private ListModules() {
		super();
	}
	public ListModules(INode node) {
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
	private ListDeclarations() {
		super();
	}
	public ListDeclarations(INode node) {
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
	private Test() {
		super();
	}
	public Test(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandTest(this);
	}

	public boolean isTest() { return true; }	
} public boolean isUnimport() { return false; }
static public class Unimport extends ShellCommand {
/** "unimport" name:QualifiedName -> ShellCommand {cons("Unimport")} */
	private Unimport() {
		super();
	}
	public Unimport(INode node, org.meta_environment.rascal.ast.QualifiedName name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandUnimport(this);
	}

	public boolean isUnimport() { return true; }

	public boolean hasName() { return true; }

private org.meta_environment.rascal.ast.QualifiedName name;
	public org.meta_environment.rascal.ast.QualifiedName getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.QualifiedName x) { this.name = x; }
	public Unimport setName(org.meta_environment.rascal.ast.QualifiedName x) { 
		Unimport z = new Unimport();
 		z.$setName(x);
		return z;
	}	
} public boolean isUndeclare() { return false; }
static public class Undeclare extends ShellCommand {
/** "undeclare" name:QualifiedName -> ShellCommand {cons("Undeclare")} */
	private Undeclare() {
		super();
	}
	public Undeclare(INode node, org.meta_environment.rascal.ast.QualifiedName name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandUndeclare(this);
	}

	public boolean isUndeclare() { return true; }

	public boolean hasName() { return true; }

private org.meta_environment.rascal.ast.QualifiedName name;
	public org.meta_environment.rascal.ast.QualifiedName getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.QualifiedName x) { this.name = x; }
	public Undeclare setName(org.meta_environment.rascal.ast.QualifiedName x) { 
		Undeclare z = new Undeclare();
 		z.$setName(x);
		return z;
	}	
} 
public boolean isHistory() { return false; }
static public class History extends ShellCommand {
/** "history" -> ShellCommand {cons("History")} */
	private History() {
		super();
	}
	public History(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandHistory(this);
	}

	public boolean isHistory() { return true; }	
} public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public boolean hasExpression() { return false; }
public boolean isSetOption() { return false; }
static public class SetOption extends ShellCommand {
/** "set" name:QualifiedName expression:Expression -> ShellCommand {cons("SetOption")} */
	private SetOption() {
		super();
	}
	public SetOption(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.Expression expression) {
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

private org.meta_environment.rascal.ast.QualifiedName name;
	public org.meta_environment.rascal.ast.QualifiedName getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.QualifiedName x) { this.name = x; }
	public SetOption setName(org.meta_environment.rascal.ast.QualifiedName x) { 
		SetOption z = new SetOption();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public SetOption setExpression(org.meta_environment.rascal.ast.Expression x) { 
		SetOption z = new SetOption();
 		z.$setExpression(x);
		return z;
	}	
}
}