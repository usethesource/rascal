package org.meta_environment.rascal.ast;
public abstract class Expression extends AbstractAST
{
  public class Closure extends Expression
  {
    private Type type;
    private List < Statement > statements;

    private Closure ()
    {
    }
    /*package */ Closure (ITree tree, Type type,
			  List < Statement > statements)
    {
      this.tree = tree;
      this.type = type;
      this.statements = statements;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitClosureExpression (this);
    }
    private final Type type;
    public Type gettype ()
    {
      return type;
    }
    private void privateSettype (Type x)
    {
      this.type = x;
    }
    public Closure settype (Type x)
    {
      z = new Closure ();
      z.privateSettype (x);
      return z;
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
    public Closure setstatements (List < Statement > x)
    {
      z = new Closure ();
      z.privateSetstatements (x);
      return z;
    }
  }
  prod2class ("(" Expression ")"->Expression
	      {
	      bracket}
  )public class ClosureCall extends Expression
  {
    private Expression closure;
    private List < Expression > arguments;

    private ClosureCall ()
    {
    }
    /*package */ ClosureCall (ITree tree, Expression closure,
			      List < Expression > arguments)
    {
      this.tree = tree;
      this.closure = closure;
      this.arguments = arguments;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitClosureCallExpression (this);
    }
    private final Expression closure;
    public Expression getclosure ()
    {
      return closure;
    }
    private void privateSetclosure (Expression x)
    {
      this.closure = x;
    }
    public ClosureCall setclosure (Expression x)
    {
      z = new ClosureCall ();
      z.privateSetclosure (x);
      return z;
    }
    private final List < Expression > arguments;
    public List < Expression > getarguments ()
    {
      return arguments;
    }
    private void privateSetarguments (List < Expression > x)
    {
      this.arguments = x;
    }
    public ClosureCall setarguments (List < Expression > x)
    {
      z = new ClosureCall ();
      z.privateSetarguments (x);
      return z;
    }
  }
  public class FieldAccess extends Expression
  {
    private Expression expression;
    private Name field;

    private FieldAccess ()
    {
    }
    /*package */ FieldAccess (ITree tree, Expression expression, Name field)
    {
      this.tree = tree;
      this.expression = expression;
      this.field = field;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitFieldAccessExpression (this);
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
    public FieldAccess setexpression (Expression x)
    {
      z = new FieldAccess ();
      z.privateSetexpression (x);
      return z;
    }
    private final Name field;
    public Name getfield ()
    {
      return field;
    }
    private void privateSetfield (Name x)
    {
      this.field = x;
    }
    public FieldAccess setfield (Name x)
    {
      z = new FieldAccess ();
      z.privateSetfield (x);
      return z;
    }
  }
  public class Subscript extends Expression
  {
    private Expression expression;
    private Expression subscript;

    private Subscript ()
    {
    }
    /*package */ Subscript (ITree tree, Expression expression,
			    Expression subscript)
    {
      this.tree = tree;
      this.expression = expression;
      this.subscript = subscript;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSubscriptExpression (this);
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
    public Subscript setexpression (Expression x)
    {
      z = new Subscript ();
      z.privateSetexpression (x);
      return z;
    }
    private final Expression subscript;
    public Expression getsubscript ()
    {
      return subscript;
    }
    private void privateSetsubscript (Expression x)
    {
      this.subscript = x;
    }
    public Subscript setsubscript (Expression x)
    {
      z = new Subscript ();
      z.privateSetsubscript (x);
      return z;
    }
  }
  public class TransitiveReflexiveClosure extends Expression
  {
    private Expression argument;

    private TransitiveReflexiveClosure ()
    {
    }
    /*package */ TransitiveReflexiveClosure (ITree tree, Expression argument)
    {
      this.tree = tree;
      this.argument = argument;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTransitiveReflexiveClosureExpression (this);
    }
    private final Expression argument;
    public Expression getargument ()
    {
      return argument;
    }
    private void privateSetargument (Expression x)
    {
      this.argument = x;
    }
    public TransitiveReflexiveClosure setargument (Expression x)
    {
      z = new TransitiveReflexiveClosure ();
      z.privateSetargument (x);
      return z;
    }
  }
  public class TransitiveClosure extends Expression
  {
    private Expression argument;

    private TransitiveClosure ()
    {
    }
    /*package */ TransitiveClosure (ITree tree, Expression argument)
    {
      this.tree = tree;
      this.argument = argument;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTransitiveClosureExpression (this);
    }
    private final Expression argument;
    public Expression getargument ()
    {
      return argument;
    }
    private void privateSetargument (Expression x)
    {
      this.argument = x;
    }
    public TransitiveClosure setargument (Expression x)
    {
      z = new TransitiveClosure ();
      z.privateSetargument (x);
      return z;
    }
  }
  public class Annotation extends Expression
  {
    private Expression expression;
    private Name name;

    private Annotation ()
    {
    }
    /*package */ Annotation (ITree tree, Expression expression, Name name)
    {
      this.tree = tree;
      this.expression = expression;
      this.name = name;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAnnotationExpression (this);
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
    public Annotation setexpression (Expression x)
    {
      z = new Annotation ();
      z.privateSetexpression (x);
      return z;
    }
    private final Name name;
    public Name getname ()
    {
      return name;
    }
    private void privateSetname (Name x)
    {
      this.name = x;
    }
    public Annotation setname (Name x)
    {
      z = new Annotation ();
      z.privateSetname (x);
      return z;
    }
  }
  public class Negation extends Expression
  {
    private Expression argument;

    private Negation ()
    {
    }
    /*package */ Negation (ITree tree, Expression argument)
    {
      this.tree = tree;
      this.argument = argument;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNegationExpression (this);
    }
    private final Expression argument;
    public Expression getargument ()
    {
      return argument;
    }
    private void privateSetargument (Expression x)
    {
      this.argument = x;
    }
    public Negation setargument (Expression x)
    {
      z = new Negation ();
      z.privateSetargument (x);
      return z;
    }
  }
  public class Product extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private Product ()
    {
    }
    /*package */ Product (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitProductExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Product setlhs (Expression x)
    {
      z = new Product ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Product setrhs (Expression x)
    {
      z = new Product ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class Intersection extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private Intersection ()
    {
    }
    /*package */ Intersection (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitIntersectionExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Intersection setlhs (Expression x)
    {
      z = new Intersection ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Intersection setrhs (Expression x)
    {
      z = new Intersection ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class Division extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private Division ()
    {
    }
    /*package */ Division (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDivisionExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Division setlhs (Expression x)
    {
      z = new Division ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Division setrhs (Expression x)
    {
      z = new Division ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class Addition extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private Addition ()
    {
    }
    /*package */ Addition (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAdditionExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Addition setlhs (Expression x)
    {
      z = new Addition ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Addition setrhs (Expression x)
    {
      z = new Addition ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class Substraction extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private Substraction ()
    {
    }
    /*package */ Substraction (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSubstractionExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Substraction setlhs (Expression x)
    {
      z = new Substraction ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Substraction setrhs (Expression x)
    {
      z = new Substraction ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class Match extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private Match ()
    {
    }
    /*package */ Match (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitMatchExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Match setlhs (Expression x)
    {
      z = new Match ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Match setrhs (Expression x)
    {
      z = new Match ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class NoMatch extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private NoMatch ()
    {
    }
    /*package */ NoMatch (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNoMatchExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public NoMatch setlhs (Expression x)
    {
      z = new NoMatch ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public NoMatch setrhs (Expression x)
    {
      z = new NoMatch ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class LessThan extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private LessThan ()
    {
    }
    /*package */ LessThan (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitLessThanExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public LessThan setlhs (Expression x)
    {
      z = new LessThan ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public LessThan setrhs (Expression x)
    {
      z = new LessThan ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class LessThanOrEq extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private LessThanOrEq ()
    {
    }
    /*package */ LessThanOrEq (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitLessThanOrEqExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public LessThanOrEq setlhs (Expression x)
    {
      z = new LessThanOrEq ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public LessThanOrEq setrhs (Expression x)
    {
      z = new LessThanOrEq ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class GreaterThan extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private GreaterThan ()
    {
    }
    /*package */ GreaterThan (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitGreaterThanExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public GreaterThan setlhs (Expression x)
    {
      z = new GreaterThan ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public GreaterThan setrhs (Expression x)
    {
      z = new GreaterThan ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class GreaterThanOrEq extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private GreaterThanOrEq ()
    {
    }
    /*package */ GreaterThanOrEq (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitGreaterThanOrEqExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public GreaterThanOrEq setlhs (Expression x)
    {
      z = new GreaterThanOrEq ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public GreaterThanOrEq setrhs (Expression x)
    {
      z = new GreaterThanOrEq ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class Equals extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private Equals ()
    {
    }
    /*package */ Equals (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitEqualsExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Equals setlhs (Expression x)
    {
      z = new Equals ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Equals setrhs (Expression x)
    {
      z = new Equals ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class NonEquals extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private NonEquals ()
    {
    }
    /*package */ NonEquals (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNonEqualsExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public NonEquals setlhs (Expression x)
    {
      z = new NonEquals ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public NonEquals setrhs (Expression x)
    {
      z = new NonEquals ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class NotIn extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private NotIn ()
    {
    }
    /*package */ NotIn (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNotInExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public NotIn setlhs (Expression x)
    {
      z = new NotIn ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public NotIn setrhs (Expression x)
    {
      z = new NotIn ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class In extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private In ()
    {
    }
    /*package */ In (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitInExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public In setlhs (Expression x)
    {
      z = new In ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public In setrhs (Expression x)
    {
      z = new In ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class And extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private And ()
    {
    }
    /*package */ And (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAndExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public And setlhs (Expression x)
    {
      z = new And ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public And setrhs (Expression x)
    {
      z = new And ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class Or extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private Or ()
    {
    }
    /*package */ Or (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitOrExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public Or setlhs (Expression x)
    {
      z = new Or ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public Or setrhs (Expression x)
    {
      z = new Or ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class IfDefined extends Expression
  {
    private Expression lhs;
    private Expression rhs;

    private IfDefined ()
    {
    }
    /*package */ IfDefined (ITree tree, Expression lhs, Expression rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitIfDefinedExpression (this);
    }
    private final Expression lhs;
    public Expression getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Expression x)
    {
      this.lhs = x;
    }
    public IfDefined setlhs (Expression x)
    {
      z = new IfDefined ();
      z.privateSetlhs (x);
      return z;
    }
    private final Expression rhs;
    public Expression getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Expression x)
    {
      this.rhs = x;
    }
    public IfDefined setrhs (Expression x)
    {
      z = new IfDefined ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class IfThenElse extends Expression
  {
    private Expression condition;
    private Expression then;
    private Expression
    else;

    private IfThenElse ()
    {
    }
    /*package */ IfThenElse (ITree tree, Expression condition,
			     Expression then, Expression
			     else
    )
    {
      this.tree = tree;
      this.condition = condition;
      this.then = then;
      this.
      else
      =
      else;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitIfThenElseExpression (this);
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
    public IfThenElse setcondition (Expression x)
    {
      z = new IfThenElse ();
      z.privateSetcondition (x);
      return z;
    }
    private final Expression then;
    public Expression getthen ()
    {
      return then;
    }
    private void privateSetthen (Expression x)
    {
      this.then = x;
    }
    public IfThenElse setthen (Expression x)
    {
      z = new IfThenElse ();
      z.privateSetthen (x);
      return z;
    }
    private final Expression
    else;
    public Expression getelse ()
    {
      return
      else;
    }
    private void privateSetelse (Expression x)
    {
      this.
      else
      = x;
    }
    public IfThenElse setelse (Expression x)
    {
      z = new IfThenElse ();
      z.privateSetelse (x);
      return z;
    }
  }
  public class Operator extends Expression
  {
    private StandardOperator operator;

    private Operator ()
    {
    }
    /*package */ Operator (ITree tree, StandardOperator operator)
    {
      this.tree = tree;
      this.operator = operator;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitOperatorExpression (this);
    }
    private final StandardOperator operator;
    public StandardOperator getoperator ()
    {
      return operator;
    }
    private void privateSetoperator (StandardOperator x)
    {
      this.operator = x;
    }
    public Operator setoperator (StandardOperator x)
    {
      z = new Operator ();
      z.privateSetoperator (x);
      return z;
    }
  }
  public class Literal extends Expression
  {
    private Literal literal;

    private Literal ()
    {
    }
    /*package */ Literal (ITree tree, Literal literal)
    {
      this.tree = tree;
      this.literal = literal;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitLiteralExpression (this);
    }
    private final Literal literal;
    public Literal getliteral ()
    {
      return literal;
    }
    private void privateSetliteral (Literal x)
    {
      this.literal = x;
    }
    public Literal setliteral (Literal x)
    {
      z = new Literal ();
      z.privateSetliteral (x);
      return z;
    }
  }
  public class CallOrTree extends Expression
  {
    private Name name;
    private List < Expression > arguments;

    private CallOrTree ()
    {
    }
    /*package */ CallOrTree (ITree tree, Name name,
			     List < Expression > arguments)
    {
      this.tree = tree;
      this.name = name;
      this.arguments = arguments;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitCallOrTreeExpression (this);
    }
    private final Name name;
    public Name getname ()
    {
      return name;
    }
    private void privateSetname (Name x)
    {
      this.name = x;
    }
    public CallOrTree setname (Name x)
    {
      z = new CallOrTree ();
      z.privateSetname (x);
      return z;
    }
    private final List < Expression > arguments;
    public List < Expression > getarguments ()
    {
      return arguments;
    }
    private void privateSetarguments (List < Expression > x)
    {
      this.arguments = x;
    }
    public CallOrTree setarguments (List < Expression > x)
    {
      z = new CallOrTree ();
      z.privateSetarguments (x);
      return z;
    }
  }
  public class List extends Expression
  {
    private List < Expression > elements;

    private List ()
    {
    }
    /*package */ List (ITree tree, List < Expression > elements)
    {
      this.tree = tree;
      this.elements = elements;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitListExpression (this);
    }
    private final List < Expression > elements;
    public List < Expression > getelements ()
    {
      return elements;
    }
    private void privateSetelements (List < Expression > x)
    {
      this.elements = x;
    }
    public List setelements (List < Expression > x)
    {
      z = new List ();
      z.privateSetelements (x);
      return z;
    }
  }
  public class Range extends Expression
  {
    private Expression from;
    private Expression to;

    private Range ()
    {
    }
    /*package */ Range (ITree tree, Expression from, Expression to)
    {
      this.tree = tree;
      this.from = from;
      this.to = to;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitRangeExpression (this);
    }
    private final Expression from;
    public Expression getfrom ()
    {
      return from;
    }
    private void privateSetfrom (Expression x)
    {
      this.from = x;
    }
    public Range setfrom (Expression x)
    {
      z = new Range ();
      z.privateSetfrom (x);
      return z;
    }
    private final Expression to;
    public Expression getto ()
    {
      return to;
    }
    private void privateSetto (Expression x)
    {
      this.to = x;
    }
    public Range setto (Expression x)
    {
      z = new Range ();
      z.privateSetto (x);
      return z;
    }
  }
  public class StepRange extends Expression
  {
    private Expression from;
    private Expression by;
    private Expression to;

    private StepRange ()
    {
    }
    /*package */ StepRange (ITree tree, Expression from, Expression by,
			    Expression to)
    {
      this.tree = tree;
      this.from = from;
      this.by = by;
      this.to = to;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitStepRangeExpression (this);
    }
    private final Expression from;
    public Expression getfrom ()
    {
      return from;
    }
    private void privateSetfrom (Expression x)
    {
      this.from = x;
    }
    public StepRange setfrom (Expression x)
    {
      z = new StepRange ();
      z.privateSetfrom (x);
      return z;
    }
    private final Expression by;
    public Expression getby ()
    {
      return by;
    }
    private void privateSetby (Expression x)
    {
      this.by = x;
    }
    public StepRange setby (Expression x)
    {
      z = new StepRange ();
      z.privateSetby (x);
      return z;
    }
    private final Expression to;
    public Expression getto ()
    {
      return to;
    }
    private void privateSetto (Expression x)
    {
      this.to = x;
    }
    public StepRange setto (Expression x)
    {
      z = new StepRange ();
      z.privateSetto (x);
      return z;
    }
  }
  public class Set extends Expression
  {
    private List < Expression > elements;

    private Set ()
    {
    }
    /*package */ Set (ITree tree, List < Expression > elements)
    {
      this.tree = tree;
      this.elements = elements;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSetExpression (this);
    }
    private final List < Expression > elements;
    public List < Expression > getelements ()
    {
      return elements;
    }
    private void privateSetelements (List < Expression > x)
    {
      this.elements = x;
    }
    public Set setelements (List < Expression > x)
    {
      z = new Set ();
      z.privateSetelements (x);
      return z;
    }
  }
  public class Tuple extends Expression
  {
    private Expression first;
    private List < Expression > rest;

    private Tuple ()
    {
    }
    /*package */ Tuple (ITree tree, Expression first,
			List < Expression > rest)
    {
      this.tree = tree;
      this.first = first;
      this.rest = rest;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTupleExpression (this);
    }
    private final Expression first;
    public Expression getfirst ()
    {
      return first;
    }
    private void privateSetfirst (Expression x)
    {
      this.first = x;
    }
    public Tuple setfirst (Expression x)
    {
      z = new Tuple ();
      z.privateSetfirst (x);
      return z;
    }
    private final List < Expression > rest;
    public List < Expression > getrest ()
    {
      return rest;
    }
    private void privateSetrest (List < Expression > x)
    {
      this.rest = x;
    }
    public Tuple setrest (List < Expression > x)
    {
      z = new Tuple ();
      z.privateSetrest (x);
      return z;
    }
  }
  public class MapTuple extends Expression
  {
    private Expression from;
    private Expression to;

    private MapTuple ()
    {
    }
    /*package */ MapTuple (ITree tree, Expression from, Expression to)
    {
      this.tree = tree;
      this.from = from;
      this.to = to;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitMapTupleExpression (this);
    }
    private final Expression from;
    public Expression getfrom ()
    {
      return from;
    }
    private void privateSetfrom (Expression x)
    {
      this.from = x;
    }
    public MapTuple setfrom (Expression x)
    {
      z = new MapTuple ();
      z.privateSetfrom (x);
      return z;
    }
    private final Expression to;
    public Expression getto ()
    {
      return to;
    }
    private void privateSetto (Expression x)
    {
      this.to = x;
    }
    public MapTuple setto (Expression x)
    {
      z = new MapTuple ();
      z.privateSetto (x);
      return z;
    }
  }
  public class Location extends Expression
  {
    private Location ()
    {
    }
    /*package */ Location (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitLocationExpression (this);
    }
  }
  public class Area extends Expression
  {
    private Area ()
    {
    }
    /*package */ Area (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAreaExpression (this);
    }
  }
  public class FileLocation extends Expression
  {
    private Expression filename;

    private FileLocation ()
    {
    }
    /*package */ FileLocation (ITree tree, Expression filename)
    {
      this.tree = tree;
      this.filename = filename;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitFileLocationExpression (this);
    }
    private final Expression filename;
    public Expression getfilename ()
    {
      return filename;
    }
    private void privateSetfilename (Expression x)
    {
      this.filename = x;
    }
    public FileLocation setfilename (Expression x)
    {
      z = new FileLocation ();
      z.privateSetfilename (x);
      return z;
    }
  }
  public class AreaLocation extends Expression
  {
    private Expression area;

    private AreaLocation ()
    {
    }
    /*package */ AreaLocation (ITree tree, Expression area)
    {
      this.tree = tree;
      this.area = area;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAreaLocationExpression (this);
    }
    private final Expression area;
    public Expression getarea ()
    {
      return area;
    }
    private void privateSetarea (Expression x)
    {
      this.area = x;
    }
    public AreaLocation setarea (Expression x)
    {
      z = new AreaLocation ();
      z.privateSetarea (x);
      return z;
    }
  }
  public class AreaInFileLocation extends Expression
  {
    private Expression filename;
    private Expression area;

    private AreaInFileLocation ()
    {
    }
    /*package */ AreaInFileLocation (ITree tree, Expression filename,
				     Expression area)
    {
      this.tree = tree;
      this.filename = filename;
      this.area = area;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAreaInFileLocationExpression (this);
    }
    private final Expression filename;
    public Expression getfilename ()
    {
      return filename;
    }
    private void privateSetfilename (Expression x)
    {
      this.filename = x;
    }
    public AreaInFileLocation setfilename (Expression x)
    {
      z = new AreaInFileLocation ();
      z.privateSetfilename (x);
      return z;
    }
    private final Expression area;
    public Expression getarea ()
    {
      return area;
    }
    private void privateSetarea (Expression x)
    {
      this.area = x;
    }
    public AreaInFileLocation setarea (Expression x)
    {
      z = new AreaInFileLocation ();
      z.privateSetarea (x);
      return z;
    }
  }
  public class QualifiedName extends Expression
  {
    private QualifiedName qualifiedName;

    private QualifiedName ()
    {
    }
    /*package */ QualifiedName (ITree tree, QualifiedName qualifiedName)
    {
      this.tree = tree;
      this.qualifiedName = qualifiedName;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitQualifiedNameExpression (this);
    }
    private final QualifiedName qualifiedName;
    public QualifiedName getqualifiedName ()
    {
      return qualifiedName;
    }
    private void privateSetqualifiedName (QualifiedName x)
    {
      this.qualifiedName = x;
    }
    public QualifiedName setqualifiedName (QualifiedName x)
    {
      z = new QualifiedName ();
      z.privateSetqualifiedName (x);
      return z;
    }
  }
  public class Comprehension extends Expression
  {
    private Comprehension comprehension;

    private Comprehension ()
    {
    }
    /*package */ Comprehension (ITree tree, Comprehension comprehension)
    {
      this.tree = tree;
      this.comprehension = comprehension;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitComprehensionExpression (this);
    }
    private final Comprehension comprehension;
    public Comprehension getcomprehension ()
    {
      return comprehension;
    }
    private void privateSetcomprehension (Comprehension x)
    {
      this.comprehension = x;
    }
    public Comprehension setcomprehension (Comprehension x)
    {
      z = new Comprehension ();
      z.privateSetcomprehension (x);
      return z;
    }
  }
  public class Exists extends Expression
  {
    private ValueProducer producer;
    private Expression expression;

    private Exists ()
    {
    }
    /*package */ Exists (ITree tree, ValueProducer producer,
			 Expression expression)
    {
      this.tree = tree;
      this.producer = producer;
      this.expression = expression;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitExistsExpression (this);
    }
    private final ValueProducer producer;
    public ValueProducer getproducer ()
    {
      return producer;
    }
    private void privateSetproducer (ValueProducer x)
    {
      this.producer = x;
    }
    public Exists setproducer (ValueProducer x)
    {
      z = new Exists ();
      z.privateSetproducer (x);
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
    public Exists setexpression (Expression x)
    {
      z = new Exists ();
      z.privateSetexpression (x);
      return z;
    }
  }
  public class Forall extends Expression
  {
    private ValueProducer producers;
    private Expression expression;

    private Forall ()
    {
    }
    /*package */ Forall (ITree tree, ValueProducer producers,
			 Expression expression)
    {
      this.tree = tree;
      this.producers = producers;
      this.expression = expression;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitForallExpression (this);
    }
    private final ValueProducer producers;
    public ValueProducer getproducers ()
    {
      return producers;
    }
    private void privateSetproducers (ValueProducer x)
    {
      this.producers = x;
    }
    public Forall setproducers (ValueProducer x)
    {
      z = new Forall ();
      z.privateSetproducers (x);
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
    public Forall setexpression (Expression x)
    {
      z = new Forall ();
      z.privateSetexpression (x);
      return z;
    }
  }
  public class Visit extends Expression
  {
    private Visit visit;

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
      return visitor.visitVisitExpression (this);
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
}
