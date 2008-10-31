package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Statement extends AbstractAST
{
  static public class Solve extends Statement
  {
/* "with" declarations:{Declarator ";"}+ ";" "solve" body:Statement -> Statement {cons("Solve")} */
    private Solve ()
    {
    }
    /*package */ Solve (ITree tree,
			java.util.List < Declarator > declarations,
			Statement body)
    {
      this.tree = tree;
      this.declarations = declarations;
      this.body = body;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementSolve (this);
    }
    private java.util.List < Declarator > declarations;
    public java.util.List < Declarator > getDeclarations ()
    {
      return declarations;
    }
    private void $setDeclarations (java.util.List < Declarator > x)
    {
      this.declarations = x;
    }
    public Solve setDeclarations (java.util.List < Declarator > x)
    {
      Solve z = new Solve ();
      z.$setDeclarations (x);
      return z;
    }
    private Statement body;
    public Statement getBody ()
    {
      return body;
    }
    private void $setBody (Statement x)
    {
      this.body = x;
    }
    public Solve setBody (Statement x)
    {
      Solve z = new Solve ();
      z.$setBody (x);
      return z;
    }
  }
  public class Ambiguity extends Statement
  {
    private final java.util.List < Statement > alternatives;
    public Ambiguity (java.util.List < Statement > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Statement > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class For extends Statement
  {
/* label:Label "for" "(" generators:{Generator ","}+ ")" body:Statement -> Statement {cons("For")} */
    private For ()
    {
    }
    /*package */ For (ITree tree, Label label,
		      java.util.List < Generator > generators, Statement body)
    {
      this.tree = tree;
      this.label = label;
      this.generators = generators;
      this.body = body;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementFor (this);
    }
    private Label label;
    public Label getLabel ()
    {
      return label;
    }
    private void $setLabel (Label x)
    {
      this.label = x;
    }
    public For setLabel (Label x)
    {
      For z = new For ();
      z.$setLabel (x);
      return z;
    }
    private java.util.List < Generator > generators;
    public java.util.List < Generator > getGenerators ()
    {
      return generators;
    }
    private void $setGenerators (java.util.List < Generator > x)
    {
      this.generators = x;
    }
    public For setGenerators (java.util.List < Generator > x)
    {
      For z = new For ();
      z.$setGenerators (x);
      return z;
    }
    private Statement body;
    public Statement getBody ()
    {
      return body;
    }
    private void $setBody (Statement x)
    {
      this.body = x;
    }
    public For setBody (Statement x)
    {
      For z = new For ();
      z.$setBody (x);
      return z;
    }
  }
  static public class While extends Statement
  {
/* label:Label "while" "(" condition:Expression ")" body:Statement -> Statement {cons("While")} */
    private While ()
    {
    }
    /*package */ While (ITree tree, Label label, Expression condition,
			Statement body)
    {
      this.tree = tree;
      this.label = label;
      this.condition = condition;
      this.body = body;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementWhile (this);
    }
    private Label label;
    public Label getLabel ()
    {
      return label;
    }
    private void $setLabel (Label x)
    {
      this.label = x;
    }
    public While setLabel (Label x)
    {
      While z = new While ();
      z.$setLabel (x);
      return z;
    }
    private Expression condition;
    public Expression getCondition ()
    {
      return condition;
    }
    private void $setCondition (Expression x)
    {
      this.condition = x;
    }
    public While setCondition (Expression x)
    {
      While z = new While ();
      z.$setCondition (x);
      return z;
    }
    private Statement body;
    public Statement getBody ()
    {
      return body;
    }
    private void $setBody (Statement x)
    {
      this.body = x;
    }
    public While setBody (Statement x)
    {
      While z = new While ();
      z.$setBody (x);
      return z;
    }
  }
  static public class DoWhile extends Statement
  {
/* label:Label "do" body:Statement "while" "(" condition:Expression ")" ";" -> Statement {cons("DoWhile")} */
    private DoWhile ()
    {
    }
    /*package */ DoWhile (ITree tree, Label label, Statement body,
			  Expression condition)
    {
      this.tree = tree;
      this.label = label;
      this.body = body;
      this.condition = condition;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementDoWhile (this);
    }
    private Label label;
    public Label getLabel ()
    {
      return label;
    }
    private void $setLabel (Label x)
    {
      this.label = x;
    }
    public DoWhile setLabel (Label x)
    {
      DoWhile z = new DoWhile ();
      z.$setLabel (x);
      return z;
    }
    private Statement body;
    public Statement getBody ()
    {
      return body;
    }
    private void $setBody (Statement x)
    {
      this.body = x;
    }
    public DoWhile setBody (Statement x)
    {
      DoWhile z = new DoWhile ();
      z.$setBody (x);
      return z;
    }
    private Expression condition;
    public Expression getCondition ()
    {
      return condition;
    }
    private void $setCondition (Expression x)
    {
      this.condition = x;
    }
    public DoWhile setCondition (Expression x)
    {
      DoWhile z = new DoWhile ();
      z.$setCondition (x);
      return z;
    }
  }
  static public class IfThenElse extends Statement
  {
/* label:Label "if" "(" conditions:{Expression ","}+ ")" thenStatement:Statement "else" elseStatement:Statement -> Statement {cons("IfThenElse")} */
    private IfThenElse ()
    {
    }
    /*package */ IfThenElse (ITree tree, Label label,
			     java.util.List < Expression > conditions,
			     Statement thenStatement, Statement elseStatement)
    {
      this.tree = tree;
      this.label = label;
      this.conditions = conditions;
      this.thenStatement = thenStatement;
      this.elseStatement = elseStatement;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementIfThenElse (this);
    }
    private Label label;
    public Label getLabel ()
    {
      return label;
    }
    private void $setLabel (Label x)
    {
      this.label = x;
    }
    public IfThenElse setLabel (Label x)
    {
      IfThenElse z = new IfThenElse ();
      z.$setLabel (x);
      return z;
    }
    private java.util.List < Expression > conditions;
    public java.util.List < Expression > getConditions ()
    {
      return conditions;
    }
    private void $setConditions (java.util.List < Expression > x)
    {
      this.conditions = x;
    }
    public IfThenElse setConditions (java.util.List < Expression > x)
    {
      IfThenElse z = new IfThenElse ();
      z.$setConditions (x);
      return z;
    }
    private Statement thenStatement;
    public Statement getThenStatement ()
    {
      return thenStatement;
    }
    private void $setThenStatement (Statement x)
    {
      this.thenStatement = x;
    }
    public IfThenElse setThenStatement (Statement x)
    {
      IfThenElse z = new IfThenElse ();
      z.$setThenStatement (x);
      return z;
    }
    private Statement elseStatement;
    public Statement getElseStatement ()
    {
      return elseStatement;
    }
    private void $setElseStatement (Statement x)
    {
      this.elseStatement = x;
    }
    public IfThenElse setElseStatement (Statement x)
    {
      IfThenElse z = new IfThenElse ();
      z.$setElseStatement (x);
      return z;
    }
  }
  static public class IfThen extends Statement
  {
/* label:Label "if" "(" conditions:{Expression ","}+ ")" thenStatement:Statement NoElseMayFollow -> Statement {cons("IfThen")} */
    private IfThen ()
    {
    }
    /*package */ IfThen (ITree tree, Label label,
			 java.util.List < Expression > conditions,
			 Statement thenStatement)
    {
      this.tree = tree;
      this.label = label;
      this.conditions = conditions;
      this.thenStatement = thenStatement;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementIfThen (this);
    }
    private Label label;
    public Label getLabel ()
    {
      return label;
    }
    private void $setLabel (Label x)
    {
      this.label = x;
    }
    public IfThen setLabel (Label x)
    {
      IfThen z = new IfThen ();
      z.$setLabel (x);
      return z;
    }
    private java.util.List < Expression > conditions;
    public java.util.List < Expression > getConditions ()
    {
      return conditions;
    }
    private void $setConditions (java.util.List < Expression > x)
    {
      this.conditions = x;
    }
    public IfThen setConditions (java.util.List < Expression > x)
    {
      IfThen z = new IfThen ();
      z.$setConditions (x);
      return z;
    }
    private Statement thenStatement;
    public Statement getThenStatement ()
    {
      return thenStatement;
    }
    private void $setThenStatement (Statement x)
    {
      this.thenStatement = x;
    }
    public IfThen setThenStatement (Statement x)
    {
      IfThen z = new IfThen ();
      z.$setThenStatement (x);
      return z;
    }
  }
  static public class Switch extends Statement
  {
/* label:Label "switch" "(" expression:Expression ")" "{" cases:Case+ "}" -> Statement {cons("Switch")} */
    private Switch ()
    {
    }
    /*package */ Switch (ITree tree, Label label, Expression expression,
			 java.util.List < Case > cases)
    {
      this.tree = tree;
      this.label = label;
      this.expression = expression;
      this.cases = cases;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementSwitch (this);
    }
    private Label label;
    public Label getLabel ()
    {
      return label;
    }
    private void $setLabel (Label x)
    {
      this.label = x;
    }
    public Switch setLabel (Label x)
    {
      Switch z = new Switch ();
      z.$setLabel (x);
      return z;
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public Switch setExpression (Expression x)
    {
      Switch z = new Switch ();
      z.$setExpression (x);
      return z;
    }
    private java.util.List < Case > cases;
    public java.util.List < Case > getCases ()
    {
      return cases;
    }
    private void $setCases (java.util.List < Case > x)
    {
      this.cases = x;
    }
    public Switch setCases (java.util.List < Case > x)
    {
      Switch z = new Switch ();
      z.$setCases (x);
      return z;
    }
  }
  static public class All extends Statement
  {
/* label:Label "all" "(" conditions:{Expression ","}+ ")" body:Statement -> Statement {cons("All")} */
    private All ()
    {
    }
    /*package */ All (ITree tree, Label label,
		      java.util.List < Expression > conditions,
		      Statement body)
    {
      this.tree = tree;
      this.label = label;
      this.conditions = conditions;
      this.body = body;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementAll (this);
    }
    private Label label;
    public Label getLabel ()
    {
      return label;
    }
    private void $setLabel (Label x)
    {
      this.label = x;
    }
    public All setLabel (Label x)
    {
      All z = new All ();
      z.$setLabel (x);
      return z;
    }
    private java.util.List < Expression > conditions;
    public java.util.List < Expression > getConditions ()
    {
      return conditions;
    }
    private void $setConditions (java.util.List < Expression > x)
    {
      this.conditions = x;
    }
    public All setConditions (java.util.List < Expression > x)
    {
      All z = new All ();
      z.$setConditions (x);
      return z;
    }
    private Statement body;
    public Statement getBody ()
    {
      return body;
    }
    private void $setBody (Statement x)
    {
      this.body = x;
    }
    public All setBody (Statement x)
    {
      All z = new All ();
      z.$setBody (x);
      return z;
    }
  }
  static public class First extends Statement
  {
/* label:Label "first" "(" conditions:{Expression ","}+ ")" body:Statement -> Statement {cons("First")} */
    private First ()
    {
    }
    /*package */ First (ITree tree, Label label,
			java.util.List < Expression > conditions,
			Statement body)
    {
      this.tree = tree;
      this.label = label;
      this.conditions = conditions;
      this.body = body;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementFirst (this);
    }
    private Label label;
    public Label getLabel ()
    {
      return label;
    }
    private void $setLabel (Label x)
    {
      this.label = x;
    }
    public First setLabel (Label x)
    {
      First z = new First ();
      z.$setLabel (x);
      return z;
    }
    private java.util.List < Expression > conditions;
    public java.util.List < Expression > getConditions ()
    {
      return conditions;
    }
    private void $setConditions (java.util.List < Expression > x)
    {
      this.conditions = x;
    }
    public First setConditions (java.util.List < Expression > x)
    {
      First z = new First ();
      z.$setConditions (x);
      return z;
    }
    private Statement body;
    public Statement getBody ()
    {
      return body;
    }
    private void $setBody (Statement x)
    {
      this.body = x;
    }
    public First setBody (Statement x)
    {
      First z = new First ();
      z.$setBody (x);
      return z;
    }
  }
  static public class Expression extends Statement
  {
/* expression:Expression ";" -> Statement {cons("Expression")} */
    private Expression ()
    {
    }
    /*package */ Expression (ITree tree, Expression expression)
    {
      this.tree = tree;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementExpression (this);
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public Expression setExpression (Expression x)
    {
      Expression z = new Expression ();
      z.$setExpression (x);
      return z;
    }
  }
  static public class Visit extends Statement
  {
/* visit:Visit -> Statement {cons("Visit")} */
    private Visit ()
    {
    }
    /*package */ Visit (ITree tree, Visit visit)
    {
      this.tree = tree;
      this.visit = visit;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementVisit (this);
    }
    private Visit visit;
    public Visit getVisit ()
    {
      return visit;
    }
    private void $setVisit (Visit x)
    {
      this.visit = x;
    }
    public Visit setVisit (Visit x)
    {
      Visit z = new Visit ();
      z.$setVisit (x);
      return z;
    }
  }
  static public class Assignment extends Statement
  {
/* assignables:{Assignable ","}+ operator:Assignment expressions:{Expression ","}+ ";" -> Statement {cons("Assignment")} */
    private Assignment ()
    {
    }
    /*package */ Assignment (ITree tree,
			     java.util.List < Assignable > assignables,
			     Assignment operator,
			     java.util.List < Expression > expressions)
    {
      this.tree = tree;
      this.assignables = assignables;
      this.operator = operator;
      this.expressions = expressions;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementAssignment (this);
    }
    private java.util.List < Assignable > assignables;
    public java.util.List < Assignable > getAssignables ()
    {
      return assignables;
    }
    private void $setAssignables (java.util.List < Assignable > x)
    {
      this.assignables = x;
    }
    public Assignment setAssignables (java.util.List < Assignable > x)
    {
      Assignment z = new Assignment ();
      z.$setAssignables (x);
      return z;
    }
    private Assignment operator;
    public Assignment getOperator ()
    {
      return operator;
    }
    private void $setOperator (Assignment x)
    {
      this.operator = x;
    }
    public Assignment setOperator (Assignment x)
    {
      Assignment z = new Assignment ();
      z.$setOperator (x);
      return z;
    }
    private java.util.List < Expression > expressions;
    public java.util.List < Expression > getExpressions ()
    {
      return expressions;
    }
    private void $setExpressions (java.util.List < Expression > x)
    {
      this.expressions = x;
    }
    public Assignment setExpressions (java.util.List < Expression > x)
    {
      Assignment z = new Assignment ();
      z.$setExpressions (x);
      return z;
    }
  }
  static public class Break extends Statement
  {
/* Break -> Statement {cons("Break")} */
    private Break ()
    {
    }
    /*package */ Break (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementBreak (this);
    }
  }
  static public class Fail extends Statement
  {
/* Fail -> Statement {cons("Fail")} */
    private Fail ()
    {
    }
    /*package */ Fail (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementFail (this);
    }
  }
  static public class Return extends Statement
  {
/* Return -> Statement {cons("Return")} */
    private Return ()
    {
    }
    /*package */ Return (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementReturn (this);
    }
  }
  static public class Continue extends Statement
  {
/* "continue" ";" -> Statement {cons("Continue")} */
    private Continue ()
    {
    }
    /*package */ Continue (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementContinue (this);
    }
  }
  static public class Assert extends Statement
  {
/* "assert" label:StringLiteral ":" expression:Expression ";" -> Statement {cons("Assert")} */
    private Assert ()
    {
    }
    /*package */ Assert (ITree tree, StringLiteral label,
			 Expression expression)
    {
      this.tree = tree;
      this.label = label;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementAssert (this);
    }
    private StringLiteral label;
    public StringLiteral getLabel ()
    {
      return label;
    }
    private void $setLabel (StringLiteral x)
    {
      this.label = x;
    }
    public Assert setLabel (StringLiteral x)
    {
      Assert z = new Assert ();
      z.$setLabel (x);
      return z;
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public Assert setExpression (Expression x)
    {
      Assert z = new Assert ();
      z.$setExpression (x);
      return z;
    }
  }
  static public class Insert extends Statement
  {
/* "insert" expression:Expression ";" -> Statement {cons("Insert")} */
    private Insert ()
    {
    }
    /*package */ Insert (ITree tree, Expression expression)
    {
      this.tree = tree;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementInsert (this);
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public Insert setExpression (Expression x)
    {
      Insert z = new Insert ();
      z.$setExpression (x);
      return z;
    }
  }
  static public class Throw extends Statement
  {
/* "throw" expression:Expression ";" -> Statement {cons("Throw")} */
    private Throw ()
    {
    }
    /*package */ Throw (ITree tree, Expression expression)
    {
      this.tree = tree;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementThrow (this);
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public Throw setExpression (Expression x)
    {
      Throw z = new Throw ();
      z.$setExpression (x);
      return z;
    }
  }
  static public class Try extends Statement
  {
/* "try" body:Statement handlers:Catch+ -> Statement {non-assoc, cons("Try")} */
    private Try ()
    {
    }
    /*package */ Try (ITree tree, Statement body,
		      java.util.List < Catch > handlers)
    {
      this.tree = tree;
      this.body = body;
      this.handlers = handlers;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementTry (this);
    }
    private Statement body;
    public Statement getBody ()
    {
      return body;
    }
    private void $setBody (Statement x)
    {
      this.body = x;
    }
    public Try setBody (Statement x)
    {
      Try z = new Try ();
      z.$setBody (x);
      return z;
    }
    private java.util.List < Catch > handlers;
    public java.util.List < Catch > getHandlers ()
    {
      return handlers;
    }
    private void $setHandlers (java.util.List < Catch > x)
    {
      this.handlers = x;
    }
    public Try setHandlers (java.util.List < Catch > x)
    {
      Try z = new Try ();
      z.$setHandlers (x);
      return z;
    }
  }
  static public class TryFinally extends Statement
  {
/* "try" body:Statement handlers:Catch+ "finally" finallyBody:Statement -> Statement {cons("TryFinally")} */
    private TryFinally ()
    {
    }
    /*package */ TryFinally (ITree tree, Statement body,
			     java.util.List < Catch > handlers,
			     Statement finallyBody)
    {
      this.tree = tree;
      this.body = body;
      this.handlers = handlers;
      this.finallyBody = finallyBody;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementTryFinally (this);
    }
    private Statement body;
    public Statement getBody ()
    {
      return body;
    }
    private void $setBody (Statement x)
    {
      this.body = x;
    }
    public TryFinally setBody (Statement x)
    {
      TryFinally z = new TryFinally ();
      z.$setBody (x);
      return z;
    }
    private java.util.List < Catch > handlers;
    public java.util.List < Catch > getHandlers ()
    {
      return handlers;
    }
    private void $setHandlers (java.util.List < Catch > x)
    {
      this.handlers = x;
    }
    public TryFinally setHandlers (java.util.List < Catch > x)
    {
      TryFinally z = new TryFinally ();
      z.$setHandlers (x);
      return z;
    }
    private Statement finallyBody;
    public Statement getFinallyBody ()
    {
      return finallyBody;
    }
    private void $setFinallyBody (Statement x)
    {
      this.finallyBody = x;
    }
    public TryFinally setFinallyBody (Statement x)
    {
      TryFinally z = new TryFinally ();
      z.$setFinallyBody (x);
      return z;
    }
  }
  static public class Block extends Statement
  {
/* label:Label "{" statements:Statement* "}" -> Statement {cons("Block")} */
    private Block ()
    {
    }
    /*package */ Block (ITree tree, Label label,
			java.util.List < Statement > statements)
    {
      this.tree = tree;
      this.label = label;
      this.statements = statements;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementBlock (this);
    }
    private Label label;
    public Label getLabel ()
    {
      return label;
    }
    private void $setLabel (Label x)
    {
      this.label = x;
    }
    public Block setLabel (Label x)
    {
      Block z = new Block ();
      z.$setLabel (x);
      return z;
    }
    private java.util.List < Statement > statements;
    public java.util.List < Statement > getStatements ()
    {
      return statements;
    }
    private void $setStatements (java.util.List < Statement > x)
    {
      this.statements = x;
    }
    public Block setStatements (java.util.List < Statement > x)
    {
      Block z = new Block ();
      z.$setStatements (x);
      return z;
    }
  }
  static public class FunctionDeclaration extends Statement
  {
/* functionDeclaration:FunctionDeclaration -> Statement {cons("FunctionDeclaration")} */
    private FunctionDeclaration ()
    {
    }
    /*package */ FunctionDeclaration (ITree tree,
				      FunctionDeclaration functionDeclaration)
    {
      this.tree = tree;
      this.functionDeclaration = functionDeclaration;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementFunctionDeclaration (this);
    }
    private FunctionDeclaration functionDeclaration;
    public FunctionDeclaration getFunctionDeclaration ()
    {
      return functionDeclaration;
    }
    private void $setFunctionDeclaration (FunctionDeclaration x)
    {
      this.functionDeclaration = x;
    }
    public FunctionDeclaration setFunctionDeclaration (FunctionDeclaration x)
    {
      FunctionDeclaration z = new FunctionDeclaration ();
      z.$setFunctionDeclaration (x);
      return z;
    }
  }
  static public class VariableDeclaration extends Statement
  {
/* declaration:LocalVariableDeclaration ";" -> Statement {cons("VariableDeclaration")} */
    private VariableDeclaration ()
    {
    }
    /*package */ VariableDeclaration (ITree tree,
				      LocalVariableDeclaration declaration)
    {
      this.tree = tree;
      this.declaration = declaration;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementVariableDeclaration (this);
    }
    private LocalVariableDeclaration declaration;
    public LocalVariableDeclaration getDeclaration ()
    {
      return declaration;
    }
    private void $setDeclaration (LocalVariableDeclaration x)
    {
      this.declaration = x;
    }
    public VariableDeclaration setDeclaration (LocalVariableDeclaration x)
    {
      VariableDeclaration z = new VariableDeclaration ();
      z.$setDeclaration (x);
      return z;
    }
  }
  static public class GlobalDirective extends Statement
  {
/* "global" type:Type names:{QualifiedName ","}+ ";" -> Statement {cons("GlobalDirective")} */
    private GlobalDirective ()
    {
    }
    /*package */ GlobalDirective (ITree tree, Type type,
				  java.util.List < QualifiedName > names)
    {
      this.tree = tree;
      this.type = type;
      this.names = names;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementGlobalDirective (this);
    }
    private Type type;
    public Type getType ()
    {
      return type;
    }
    private void $setType (Type x)
    {
      this.type = x;
    }
    public GlobalDirective setType (Type x)
    {
      GlobalDirective z = new GlobalDirective ();
      z.$setType (x);
      return z;
    }
    private java.util.List < QualifiedName > names;
    public java.util.List < QualifiedName > getNames ()
    {
      return names;
    }
    private void $setNames (java.util.List < QualifiedName > x)
    {
      this.names = x;
    }
    public GlobalDirective setNames (java.util.List < QualifiedName > x)
    {
      GlobalDirective z = new GlobalDirective ();
      z.$setNames (x);
      return z;
    }
  }
}
