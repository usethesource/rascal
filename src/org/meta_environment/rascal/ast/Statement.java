package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Statement extends AbstractAST
{
  public class Solve extends Statement
  {
/* "with" declarations:{Declarator ";"}+ ";" "solve" body:Statement -> Statement {cons("Solve")} */
    private Solve ()
    {
    }
    /*package */ Solve (ITree tree, List < Declarator > declarations,
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
    private List < Declarator > declarations;
    public List < Declarator > getdeclarations ()
    {
      return declarations;
    }
    private void $setdeclarations (List < Declarator > x)
    {
      this.declarations = x;
    }
    public Solve setdeclarations (List < Declarator > x)
    {
      Solve z = new Solve ();
      z.$setdeclarations (x);
      return z;
    }
    private Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void $setbody (Statement x)
    {
      this.body = x;
    }
    public Solve setbody (Statement x)
    {
      Solve z = new Solve ();
      z.$setbody (x);
      return z;
    }
  }
  public class Ambiguity extends Statement
  {
    private final List < Statement > alternatives;
    public Ambiguity (List < Statement > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Statement > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class For extends Statement
  {
/* label:Label "for" "(" generators:{Generator ","}+ ")" body:Statement -> Statement {cons("For")} */
    private For ()
    {
    }
    /*package */ For (ITree tree, Label label, List < Generator > generators,
		      Statement body)
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
    public Label getlabel ()
    {
      return label;
    }
    private void $setlabel (Label x)
    {
      this.label = x;
    }
    public For setlabel (Label x)
    {
      For z = new For ();
      z.$setlabel (x);
      return z;
    }
    private List < Generator > generators;
    public List < Generator > getgenerators ()
    {
      return generators;
    }
    private void $setgenerators (List < Generator > x)
    {
      this.generators = x;
    }
    public For setgenerators (List < Generator > x)
    {
      For z = new For ();
      z.$setgenerators (x);
      return z;
    }
    private Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void $setbody (Statement x)
    {
      this.body = x;
    }
    public For setbody (Statement x)
    {
      For z = new For ();
      z.$setbody (x);
      return z;
    }
  }
  public class While extends Statement
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
    public Label getlabel ()
    {
      return label;
    }
    private void $setlabel (Label x)
    {
      this.label = x;
    }
    public While setlabel (Label x)
    {
      While z = new While ();
      z.$setlabel (x);
      return z;
    }
    private Expression condition;
    public Expression getcondition ()
    {
      return condition;
    }
    private void $setcondition (Expression x)
    {
      this.condition = x;
    }
    public While setcondition (Expression x)
    {
      While z = new While ();
      z.$setcondition (x);
      return z;
    }
    private Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void $setbody (Statement x)
    {
      this.body = x;
    }
    public While setbody (Statement x)
    {
      While z = new While ();
      z.$setbody (x);
      return z;
    }
  }
  public class DoWhile extends Statement
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
    public Label getlabel ()
    {
      return label;
    }
    private void $setlabel (Label x)
    {
      this.label = x;
    }
    public DoWhile setlabel (Label x)
    {
      DoWhile z = new DoWhile ();
      z.$setlabel (x);
      return z;
    }
    private Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void $setbody (Statement x)
    {
      this.body = x;
    }
    public DoWhile setbody (Statement x)
    {
      DoWhile z = new DoWhile ();
      z.$setbody (x);
      return z;
    }
    private Expression condition;
    public Expression getcondition ()
    {
      return condition;
    }
    private void $setcondition (Expression x)
    {
      this.condition = x;
    }
    public DoWhile setcondition (Expression x)
    {
      DoWhile z = new DoWhile ();
      z.$setcondition (x);
      return z;
    }
  }
  public class IfThenElse extends Statement
  {
/* label:Label "if" "(" condition:Condition ")" thenStatement:Statement "else" elseStatement:Statement -> Statement {cons("IfThenElse")} */
    private IfThenElse ()
    {
    }
    /*package */ IfThenElse (ITree tree, Label label, Condition condition,
			     Statement thenStatement, Statement elseStatement)
    {
      this.tree = tree;
      this.label = label;
      this.condition = condition;
      this.thenStatement = thenStatement;
      this.elseStatement = elseStatement;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementIfThenElse (this);
    }
    private Label label;
    public Label getlabel ()
    {
      return label;
    }
    private void $setlabel (Label x)
    {
      this.label = x;
    }
    public IfThenElse setlabel (Label x)
    {
      IfThenElse z = new IfThenElse ();
      z.$setlabel (x);
      return z;
    }
    private Condition condition;
    public Condition getcondition ()
    {
      return condition;
    }
    private void $setcondition (Condition x)
    {
      this.condition = x;
    }
    public IfThenElse setcondition (Condition x)
    {
      IfThenElse z = new IfThenElse ();
      z.$setcondition (x);
      return z;
    }
    private Statement thenStatement;
    public Statement getthenStatement ()
    {
      return thenStatement;
    }
    private void $setthenStatement (Statement x)
    {
      this.thenStatement = x;
    }
    public IfThenElse setthenStatement (Statement x)
    {
      IfThenElse z = new IfThenElse ();
      z.$setthenStatement (x);
      return z;
    }
    private Statement elseStatement;
    public Statement getelseStatement ()
    {
      return elseStatement;
    }
    private void $setelseStatement (Statement x)
    {
      this.elseStatement = x;
    }
    public IfThenElse setelseStatement (Statement x)
    {
      IfThenElse z = new IfThenElse ();
      z.$setelseStatement (x);
      return z;
    }
  }
  public class IfThen extends Statement
  {
/* label:Label "if" "(" condition:Condition ")" thenStatement:Statement NoElseMayFollow -> Statement {cons("IfThen")} */
    private IfThen ()
    {
    }
    /*package */ IfThen (ITree tree, Label label, Condition condition,
			 Statement thenStatement)
    {
      this.tree = tree;
      this.label = label;
      this.condition = condition;
      this.thenStatement = thenStatement;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementIfThen (this);
    }
    private Label label;
    public Label getlabel ()
    {
      return label;
    }
    private void $setlabel (Label x)
    {
      this.label = x;
    }
    public IfThen setlabel (Label x)
    {
      IfThen z = new IfThen ();
      z.$setlabel (x);
      return z;
    }
    private Condition condition;
    public Condition getcondition ()
    {
      return condition;
    }
    private void $setcondition (Condition x)
    {
      this.condition = x;
    }
    public IfThen setcondition (Condition x)
    {
      IfThen z = new IfThen ();
      z.$setcondition (x);
      return z;
    }
    private Statement thenStatement;
    public Statement getthenStatement ()
    {
      return thenStatement;
    }
    private void $setthenStatement (Statement x)
    {
      this.thenStatement = x;
    }
    public IfThen setthenStatement (Statement x)
    {
      IfThen z = new IfThen ();
      z.$setthenStatement (x);
      return z;
    }
  }
  public class Switch extends Statement
  {
/* label:Label "switch" "(" expression:Expression ")" "{" cases:Case+ "}" -> Statement {cons("Switch")} */
    private Switch ()
    {
    }
    /*package */ Switch (ITree tree, Label label, Expression expression,
			 List < Case > cases)
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
    public Label getlabel ()
    {
      return label;
    }
    private void $setlabel (Label x)
    {
      this.label = x;
    }
    public Switch setlabel (Label x)
    {
      Switch z = new Switch ();
      z.$setlabel (x);
      return z;
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public Switch setexpression (Expression x)
    {
      Switch z = new Switch ();
      z.$setexpression (x);
      return z;
    }
    private List < Case > cases;
    public List < Case > getcases ()
    {
      return cases;
    }
    private void $setcases (List < Case > x)
    {
      this.cases = x;
    }
    public Switch setcases (List < Case > x)
    {
      Switch z = new Switch ();
      z.$setcases (x);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementExpression (this);
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public Expression setexpression (Expression x)
    {
      Expression z = new Expression ();
      z.$setexpression (x);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementVisit (this);
    }
    private Visit visit;
    public Visit getvisit ()
    {
      return visit;
    }
    private void $setvisit (Visit x)
    {
      this.visit = x;
    }
    public Visit setvisit (Visit x)
    {
      Visit z = new Visit ();
      z.$setvisit (x);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementAssignment (this);
    }
    private List < Assignable > assignables;
    public List < Assignable > getassignables ()
    {
      return assignables;
    }
    private void $setassignables (List < Assignable > x)
    {
      this.assignables = x;
    }
    public Assignment setassignables (List < Assignable > x)
    {
      Assignment z = new Assignment ();
      z.$setassignables (x);
      return z;
    }
    private Assignment operator;
    public Assignment getoperator ()
    {
      return operator;
    }
    private void $setoperator (Assignment x)
    {
      this.operator = x;
    }
    public Assignment setoperator (Assignment x)
    {
      Assignment z = new Assignment ();
      z.$setoperator (x);
      return z;
    }
    private List < Expression > expressions;
    public List < Expression > getexpressions ()
    {
      return expressions;
    }
    private void $setexpressions (List < Expression > x)
    {
      this.expressions = x;
    }
    public Assignment setexpressions (List < Expression > x)
    {
      Assignment z = new Assignment ();
      z.$setexpressions (x);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementAssert (this);
    }
    private StringLiteral label;
    public StringLiteral getlabel ()
    {
      return label;
    }
    private void $setlabel (StringLiteral x)
    {
      this.label = x;
    }
    public Assert setlabel (StringLiteral x)
    {
      Assert z = new Assert ();
      z.$setlabel (x);
      return z;
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public Assert setexpression (Expression x)
    {
      Assert z = new Assert ();
      z.$setexpression (x);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementReturn (this);
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public Return setexpression (Expression x)
    {
      Return z = new Return ();
      z.$setexpression (x);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementInsert (this);
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public Insert setexpression (Expression x)
    {
      Insert z = new Insert ();
      z.$setexpression (x);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementThrow (this);
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public Throw setexpression (Expression x)
    {
      Throw z = new Throw ();
      z.$setexpression (x);
      return z;
    }
  }
  public class Break extends Statement
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
  public class Continue extends Statement
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementReturnVoid (this);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementFail (this);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementTry (this);
    }
    private Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void $setbody (Statement x)
    {
      this.body = x;
    }
    public Try setbody (Statement x)
    {
      Try z = new Try ();
      z.$setbody (x);
      return z;
    }
    private List < Catch > handlers;
    public List < Catch > gethandlers ()
    {
      return handlers;
    }
    private void $sethandlers (List < Catch > x)
    {
      this.handlers = x;
    }
    public Try sethandlers (List < Catch > x)
    {
      Try z = new Try ();
      z.$sethandlers (x);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementTryFinally (this);
    }
    private Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void $setbody (Statement x)
    {
      this.body = x;
    }
    public TryFinally setbody (Statement x)
    {
      TryFinally z = new TryFinally ();
      z.$setbody (x);
      return z;
    }
    private List < Catch > handlers;
    public List < Catch > gethandlers ()
    {
      return handlers;
    }
    private void $sethandlers (List < Catch > x)
    {
      this.handlers = x;
    }
    public TryFinally sethandlers (List < Catch > x)
    {
      TryFinally z = new TryFinally ();
      z.$sethandlers (x);
      return z;
    }
    private Statement finallyBody;
    public Statement getfinallyBody ()
    {
      return finallyBody;
    }
    private void $setfinallyBody (Statement x)
    {
      this.finallyBody = x;
    }
    public TryFinally setfinallyBody (Statement x)
    {
      TryFinally z = new TryFinally ();
      z.$setfinallyBody (x);
      return z;
    }
  }
  public class Block extends Statement
  {
/* label:Label "{" statements:Statement* "}" -> Statement {cons("Block")} */
    private Block ()
    {
    }
    /*package */ Block (ITree tree, Label label,
			List < Statement > statements)
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
    public Label getlabel ()
    {
      return label;
    }
    private void $setlabel (Label x)
    {
      this.label = x;
    }
    public Block setlabel (Label x)
    {
      Block z = new Block ();
      z.$setlabel (x);
      return z;
    }
    private List < Statement > statements;
    public List < Statement > getstatements ()
    {
      return statements;
    }
    private void $setstatements (List < Statement > x)
    {
      this.statements = x;
    }
    public Block setstatements (List < Statement > x)
    {
      Block z = new Block ();
      z.$setstatements (x);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementFunctionDeclaration (this);
    }
    private FunctionDeclaration functionDeclaration;
    public FunctionDeclaration getfunctionDeclaration ()
    {
      return functionDeclaration;
    }
    private void $setfunctionDeclaration (FunctionDeclaration x)
    {
      this.functionDeclaration = x;
    }
    public FunctionDeclaration setfunctionDeclaration (FunctionDeclaration x)
    {
      FunctionDeclaration z = new FunctionDeclaration ();
      z.$setfunctionDeclaration (x);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStatementVariableDeclaration (this);
    }
    private LocalVariableDeclaration declaration;
    public LocalVariableDeclaration getdeclaration ()
    {
      return declaration;
    }
    private void $setdeclaration (LocalVariableDeclaration x)
    {
      this.declaration = x;
    }
    public VariableDeclaration setdeclaration (LocalVariableDeclaration x)
    {
      VariableDeclaration z = new VariableDeclaration ();
      z.$setdeclaration (x);
      return z;
    }
  }
  public class GlobalDirective extends Statement
  {
/* "global" type:Type names:{QualifiedName ","}+ ";" -> Statement {cons("GlobalDirective")} */
    private GlobalDirective ()
    {
    }
    /*package */ GlobalDirective (ITree tree, Type type,
				  List < QualifiedName > names)
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
    public Type gettype ()
    {
      return type;
    }
    private void $settype (Type x)
    {
      this.type = x;
    }
    public GlobalDirective settype (Type x)
    {
      GlobalDirective z = new GlobalDirective ();
      z.$settype (x);
      return z;
    }
    private List < QualifiedName > names;
    public List < QualifiedName > getnames ()
    {
      return names;
    }
    private void $setnames (List < QualifiedName > x)
    {
      this.names = x;
    }
    public GlobalDirective setnames (List < QualifiedName > x)
    {
      GlobalDirective z = new GlobalDirective ();
      z.$setnames (x);
      return z;
    }
  }
}
