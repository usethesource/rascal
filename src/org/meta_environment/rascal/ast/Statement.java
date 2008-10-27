package org.meta_environment.rascal.ast;
public abstract class Statement extends AbstractAST
{
  public class Solve extends Statement
  {
/* Solve -> Statement {cons("Solve")} */
    private Solve ()
    {
    }
    /*package */ Solve (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSolveStatement (this);
    }
  }
  public class For extends Statement
  {
/* "for" "(" generators:{Generator ","}+ ")" body:Statement -> Statement {cons("For")} */
    private For ()
    {
    }
    /*package */ For (ITree tree, List < Generator > generators,
		      Statement body)
    {
      this.tree = tree;
      this.generators = generators;
      this.body = body;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitForStatement (this);
    }
    private final List < Generator > generators;
    public List < Generator > getgenerators ()
    {
      return generators;
    }
    private void privateSetgenerators (List < Generator > x)
    {
      this.generators = x;
    }
    public For setgenerators (List < Generator > x)
    {
      z = new For ();
      z.privateSetgenerators (x);
      return z;
    }
    private final Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void privateSetbody (Statement x)
    {
      this.body = x;
    }
    public For setbody (Statement x)
    {
      z = new For ();
      z.privateSetbody (x);
      return z;
    }
  }
  public class While extends Statement
  {
/* "while" "(" condition:Expression ")" body:Statement -> Statement {cons("While")} */
    private While ()
    {
    }
    /*package */ While (ITree tree, Expression condition, Statement body)
    {
      this.tree = tree;
      this.condition = condition;
      this.body = body;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitWhileStatement (this);
    }
    private final Expression condition;
    public Expression getcondition ()
    {
      return condition;
    }
    private void privateSetcondition (Expression x)
    {
      this.condition = x;
    }
    public While setcondition (Expression x)
    {
      z = new While ();
      z.privateSetcondition (x);
      return z;
    }
    private final Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void privateSetbody (Statement x)
    {
      this.body = x;
    }
    public While setbody (Statement x)
    {
      z = new While ();
      z.privateSetbody (x);
      return z;
    }
  }
  public class IfThenElse extends Statement
  {
/* "if" "(" condition:Condition ")" thenStatement:Statement "else" elseStatement:Statement -> Statement {cons("IfThenElse")} */
    private IfThenElse ()
    {
    }
    /*package */ IfThenElse (ITree tree, Condition condition,
			     Statement thenStatement, Statement elseStatement)
    {
      this.tree = tree;
      this.condition = condition;
      this.thenStatement = thenStatement;
      this.elseStatement = elseStatement;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitIfThenElseStatement (this);
    }
    private final Condition condition;
    public Condition getcondition ()
    {
      return condition;
    }
    private void privateSetcondition (Condition x)
    {
      this.condition = x;
    }
    public IfThenElse setcondition (Condition x)
    {
      z = new IfThenElse ();
      z.privateSetcondition (x);
      return z;
    }
    private final Statement thenStatement;
    public Statement getthenStatement ()
    {
      return thenStatement;
    }
    private void privateSetthenStatement (Statement x)
    {
      this.thenStatement = x;
    }
    public IfThenElse setthenStatement (Statement x)
    {
      z = new IfThenElse ();
      z.privateSetthenStatement (x);
      return z;
    }
    private final Statement elseStatement;
    public Statement getelseStatement ()
    {
      return elseStatement;
    }
    private void privateSetelseStatement (Statement x)
    {
      this.elseStatement = x;
    }
    public IfThenElse setelseStatement (Statement x)
    {
      z = new IfThenElse ();
      z.privateSetelseStatement (x);
      return z;
    }
  }
  public class IfThen extends Statement
  {
/* "if" "(" condition:Condition ")" thenStatement:Statement NoElseMayFollow -> Statement {cons("IfThen")} */
    private IfThen ()
    {
    }
    /*package */ IfThen (ITree tree, Condition condition,
			 Statement thenStatement)
    {
      this.tree = tree;
      this.condition = condition;
      this.thenStatement = thenStatement;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitIfThenStatement (this);
    }
    private final Condition condition;
    public Condition getcondition ()
    {
      return condition;
    }
    private void privateSetcondition (Condition x)
    {
      this.condition = x;
    }
    public IfThen setcondition (Condition x)
    {
      z = new IfThen ();
      z.privateSetcondition (x);
      return z;
    }
    private final Statement thenStatement;
    public Statement getthenStatement ()
    {
      return thenStatement;
    }
    private void privateSetthenStatement (Statement x)
    {
      this.thenStatement = x;
    }
    public IfThen setthenStatement (Statement x)
    {
      z = new IfThen ();
      z.privateSetthenStatement (x);
      return z;
    }
  }
  public class Switch extends Statement
  {
/* "switch" "(" expression:Expression ")" "{" cases:Case+ "}" -> Statement {cons("Switch")} */
    private Switch ()
    {
    }
    /*package */ Switch (ITree tree, Expression expression,
			 List < Case > cases)
    {
      this.tree = tree;
      this.expression = expression;
      this.cases = cases;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSwitchStatement (this);
    }
    private final Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public Switch setexpression (Expression x)
    {
      z = new Switch ();
      z.privateSetexpression (x);
      return z;
    }
    private final List < Case > cases;
    public List < Case > getcases ()
    {
      return cases;
    }
    private void privateSetcases (List < Case > x)
    {
      this.cases = x;
    }
    public Switch setcases (List < Case > x)
    {
      z = new Switch ();
      z.privateSetcases (x);
      return z;
    }
  }
  public class VariableDeclaration extends Statement
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitVariableDeclarationStatement (this);
    }
    private final LocalVariableDeclaration declaration;
    public LocalVariableDeclaration getdeclaration ()
    {
      return declaration;
    }
    private void privateSetdeclaration (LocalVariableDeclaration x)
    {
      this.declaration = x;
    }
    public VariableDeclaration setdeclaration (LocalVariableDeclaration x)
    {
      z = new VariableDeclaration ();
      z.privateSetdeclaration (x);
      return z;
    }
  }
  public class Expression extends Statement
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitExpressionStatement (this);
    }
    private final Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public Expression setexpression (Expression x)
    {
      z = new Expression ();
      z.privateSetexpression (x);
      return z;
    }
  }
  public class Visit extends Statement
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitVisitStatement (this);
    }
    private final Visit visit;
    public Visit getvisit ()
    {
      return visit;
    }
    private void privateSetvisit (Visit x)
    {
      this.visit = x;
    }
    public Visit setvisit (Visit x)
    {
      z = new Visit ();
      z.privateSetvisit (x);
      return z;
    }
  }
  public class Assignment extends Statement
  {
/* assignables:{Assignable ","}+ operator:Assignment expressions:{Expression ","}+ ";" -> Statement {cons("Assignment")} */
    private Assignment ()
    {
    }
    /*package */ Assignment (ITree tree, List < Assignable > assignables,
			     Assignment operator,
			     List < Expression > expressions)
    {
      this.tree = tree;
      this.assignables = assignables;
      this.operator = operator;
      this.expressions = expressions;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAssignmentStatement (this);
    }
    private final List < Assignable > assignables;
    public List < Assignable > getassignables ()
    {
      return assignables;
    }
    private void privateSetassignables (List < Assignable > x)
    {
      this.assignables = x;
    }
    public Assignment setassignables (List < Assignable > x)
    {
      z = new Assignment ();
      z.privateSetassignables (x);
      return z;
    }
    private final Assignment operator;
    public Assignment getoperator ()
    {
      return operator;
    }
    private void privateSetoperator (Assignment x)
    {
      this.operator = x;
    }
    public Assignment setoperator (Assignment x)
    {
      z = new Assignment ();
      z.privateSetoperator (x);
      return z;
    }
    private final List < Expression > expressions;
    public List < Expression > getexpressions ()
    {
      return expressions;
    }
    private void privateSetexpressions (List < Expression > x)
    {
      this.expressions = x;
    }
    public Assignment setexpressions (List < Expression > x)
    {
      z = new Assignment ();
      z.privateSetexpressions (x);
      return z;
    }
  }
  public class Assert extends Statement
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAssertStatement (this);
    }
    private final StringLiteral label;
    public StringLiteral getlabel ()
    {
      return label;
    }
    private void privateSetlabel (StringLiteral x)
    {
      this.label = x;
    }
    public Assert setlabel (StringLiteral x)
    {
      z = new Assert ();
      z.privateSetlabel (x);
      return z;
    }
    private final Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public Assert setexpression (Expression x)
    {
      z = new Assert ();
      z.privateSetexpression (x);
      return z;
    }
  }
  public class Return extends Statement
  {
/* "return" expression:Expression ";" -> Statement {cons("Return")} */
    private Return ()
    {
    }
    /*package */ Return (ITree tree, Expression expression)
    {
      this.tree = tree;
      this.expression = expression;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitReturnStatement (this);
    }
    private final Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public Return setexpression (Expression x)
    {
      z = new Return ();
      z.privateSetexpression (x);
      return z;
    }
  }
  public class Insert extends Statement
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitInsertStatement (this);
    }
    private final Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public Insert setexpression (Expression x)
    {
      z = new Insert ();
      z.privateSetexpression (x);
      return z;
    }
  }
  public class Throw extends Statement
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitThrowStatement (this);
    }
    private final Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public Throw setexpression (Expression x)
    {
      z = new Throw ();
      z.privateSetexpression (x);
      return z;
    }
  }
  public class ReturnVoid extends Statement
  {
/* "return" ";" -> Statement {cons("ReturnVoid")} */
    private ReturnVoid ()
    {
    }
    /*package */ ReturnVoid (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitReturnVoidStatement (this);
    }
  }
  public class Fail extends Statement
  {
/* "fail" ";" -> Statement {cons("Fail")} */
    private Fail ()
    {
    }
    /*package */ Fail (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitFailStatement (this);
    }
  }
  public class Try extends Statement
  {
/* "try" body:Statement handlers:Catch+ -> Statement {non-assoc, cons("Try")} */
    private Try ()
    {
    }
    /*package */ Try (ITree tree, Statement body, List < Catch > handlers)
    {
      this.tree = tree;
      this.body = body;
      this.handlers = handlers;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTryStatement (this);
    }
    private final Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void privateSetbody (Statement x)
    {
      this.body = x;
    }
    public Try setbody (Statement x)
    {
      z = new Try ();
      z.privateSetbody (x);
      return z;
    }
    private final List < Catch > handlers;
    public List < Catch > gethandlers ()
    {
      return handlers;
    }
    private void privateSethandlers (List < Catch > x)
    {
      this.handlers = x;
    }
    public Try sethandlers (List < Catch > x)
    {
      z = new Try ();
      z.privateSethandlers (x);
      return z;
    }
  }
  public class TryFinally extends Statement
  {
/* "try" body:Statement handlers:Catch+ "finally" finallyBody:Statement -> Statement {cons("TryFinally")} */
    private TryFinally ()
    {
    }
    /*package */ TryFinally (ITree tree, Statement body,
			     List < Catch > handlers, Statement finallyBody)
    {
      this.tree = tree;
      this.body = body;
      this.handlers = handlers;
      this.finallyBody = finallyBody;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTryFinallyStatement (this);
    }
    private final Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void privateSetbody (Statement x)
    {
      this.body = x;
    }
    public TryFinally setbody (Statement x)
    {
      z = new TryFinally ();
      z.privateSetbody (x);
      return z;
    }
    private final List < Catch > handlers;
    public List < Catch > gethandlers ()
    {
      return handlers;
    }
    private void privateSethandlers (List < Catch > x)
    {
      this.handlers = x;
    }
    public TryFinally sethandlers (List < Catch > x)
    {
      z = new TryFinally ();
      z.privateSethandlers (x);
      return z;
    }
    private final Statement finallyBody;
    public Statement getfinallyBody ()
    {
      return finallyBody;
    }
    private void privateSetfinallyBody (Statement x)
    {
      this.finallyBody = x;
    }
    public TryFinally setfinallyBody (Statement x)
    {
      z = new TryFinally ();
      z.privateSetfinallyBody (x);
      return z;
    }
  }
  public class Block extends Statement
  {
/* "{" statements:Statement* "}" -> Statement {cons("Block")} */
    private Block ()
    {
    }
    /*package */ Block (ITree tree, List < Statement > statements)
    {
      this.tree = tree;
      this.statements = statements;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitBlockStatement (this);
    }
    private final List < Statement > statements;
    public List < Statement > getstatements ()
    {
      return statements;
    }
    private void privateSetstatements (List < Statement > x)
    {
      this.statements = x;
    }
    public Block setstatements (List < Statement > x)
    {
      z = new Block ();
      z.privateSetstatements (x);
      return z;
    }
  }
  public class FunctionDeclaration extends Statement
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitFunctionDeclarationStatement (this);
    }
    private final FunctionDeclaration functionDeclaration;
    public FunctionDeclaration getfunctionDeclaration ()
    {
      return functionDeclaration;
    }
    private void privateSetfunctionDeclaration (FunctionDeclaration x)
    {
      this.functionDeclaration = x;
    }
    public FunctionDeclaration setfunctionDeclaration (FunctionDeclaration x)
    {
      z = new FunctionDeclaration ();
      z.privateSetfunctionDeclaration (x);
      return z;
    }
  }
}
