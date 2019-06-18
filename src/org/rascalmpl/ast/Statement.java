/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.ast;


import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;

@SuppressWarnings(value = {"unused"})
public abstract class Statement extends AbstractAST {
  public Statement(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasCases() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Case> getCases() {
    throw new UnsupportedOperationException();
  }
  public boolean hasHandlers() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Catch> getHandlers() {
    throw new UnsupportedOperationException();
  }
  public boolean hasConditions() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getConditions() {
    throw new UnsupportedOperationException();
  }
  public boolean hasGenerators() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getGenerators() {
    throw new UnsupportedOperationException();
  }
  public boolean hasNames() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.QualifiedName> getNames() {
    throw new UnsupportedOperationException();
  }
  public boolean hasVariables() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.QualifiedName> getVariables() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStatements() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Statement> getStatements() {
    throw new UnsupportedOperationException();
  }
  public boolean hasAssignable() {
    return false;
  }

  public org.rascalmpl.ast.Assignable getAssignable() {
    throw new UnsupportedOperationException();
  }
  public boolean hasOperator() {
    return false;
  }

  public org.rascalmpl.ast.Assignment getOperator() {
    throw new UnsupportedOperationException();
  }
  public boolean hasBound() {
    return false;
  }

  public org.rascalmpl.ast.Bound getBound() {
    throw new UnsupportedOperationException();
  }
  public boolean hasDataTarget() {
    return false;
  }

  public org.rascalmpl.ast.DataTarget getDataTarget() {
    throw new UnsupportedOperationException();
  }
  public boolean hasCondition() {
    return false;
  }

  public org.rascalmpl.ast.Expression getCondition() {
    throw new UnsupportedOperationException();
  }
  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }
  public boolean hasMessage() {
    return false;
  }

  public org.rascalmpl.ast.Expression getMessage() {
    throw new UnsupportedOperationException();
  }
  public boolean hasFunctionDeclaration() {
    return false;
  }

  public org.rascalmpl.ast.FunctionDeclaration getFunctionDeclaration() {
    throw new UnsupportedOperationException();
  }
  public boolean hasLabel() {
    return false;
  }

  public org.rascalmpl.ast.Label getLabel() {
    throw new UnsupportedOperationException();
  }
  public boolean hasDeclaration() {
    return false;
  }

  public org.rascalmpl.ast.LocalVariableDeclaration getDeclaration() {
    throw new UnsupportedOperationException();
  }
  public boolean hasBody() {
    return false;
  }

  public org.rascalmpl.ast.Statement getBody() {
    throw new UnsupportedOperationException();
  }
  public boolean hasElseStatement() {
    return false;
  }

  public org.rascalmpl.ast.Statement getElseStatement() {
    throw new UnsupportedOperationException();
  }
  public boolean hasFinallyBody() {
    return false;
  }

  public org.rascalmpl.ast.Statement getFinallyBody() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStatement() {
    return false;
  }

  public org.rascalmpl.ast.Statement getStatement() {
    throw new UnsupportedOperationException();
  }
  public boolean hasThenStatement() {
    return false;
  }

  public org.rascalmpl.ast.Statement getThenStatement() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTarget() {
    return false;
  }

  public org.rascalmpl.ast.Target getTarget() {
    throw new UnsupportedOperationException();
  }
  public boolean hasType() {
    return false;
  }

  public org.rascalmpl.ast.Type getType() {
    throw new UnsupportedOperationException();
  }
  public boolean hasVisit() {
    return false;
  }

  public org.rascalmpl.ast.Visit getVisit() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isAppend() {
    return false;
  }

  static public class Append extends Statement {
    // Production: sig("Append",[arg("org.rascalmpl.ast.DataTarget","dataTarget"),arg("org.rascalmpl.ast.Statement","statement")],breakable=true)
  
    
    private final org.rascalmpl.ast.DataTarget dataTarget;
    private final org.rascalmpl.ast.Statement statement;
  
    public Append(ISourceLocation src, IConstructor node , org.rascalmpl.ast.DataTarget dataTarget,  org.rascalmpl.ast.Statement statement) {
      super(src, node);
      
      this.dataTarget = dataTarget;
      this.statement = statement;
    }
  
    @Override
    public boolean isAppend() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementAppend(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = dataTarget.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        dataTarget.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = statement.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        statement.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Append)) {
        return false;
      }        
      Append tmp = (Append) o;
      return true && tmp.dataTarget.equals(this.dataTarget) && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 823 + 757 * dataTarget.hashCode() + 89 * statement.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.DataTarget getDataTarget() {
      return this.dataTarget;
    }
  
    @Override
    public boolean hasDataTarget() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Statement getStatement() {
      return this.statement;
    }
  
    @Override
    public boolean hasStatement() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(dataTarget), clone(statement));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isAssert() {
    return false;
  }

  static public class Assert extends Statement {
    // Production: sig("Assert",[arg("org.rascalmpl.ast.Expression","expression")],breakable=true)
  
    
    private final org.rascalmpl.ast.Expression expression;
  
    public Assert(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression expression) {
      super(src, node);
      
      this.expression = expression;
    }
  
    @Override
    public boolean isAssert() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementAssert(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = expression.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        expression.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Assert)) {
        return false;
      }        
      Assert tmp = (Assert) o;
      return true && tmp.expression.equals(this.expression) ; 
    }
   
    @Override
    public int hashCode() {
      return 619 + 401 * expression.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(expression));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isAssertWithMessage() {
    return false;
  }

  static public class AssertWithMessage extends Statement {
    // Production: sig("AssertWithMessage",[arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.Expression","message")],breakable=true)
  
    
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.Expression message;
  
    public AssertWithMessage(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.Expression message) {
      super(src, node);
      
      this.expression = expression;
      this.message = message;
    }
  
    @Override
    public boolean isAssertWithMessage() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementAssertWithMessage(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = expression.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        expression.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = message.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        message.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof AssertWithMessage)) {
        return false;
      }        
      AssertWithMessage tmp = (AssertWithMessage) o;
      return true && tmp.expression.equals(this.expression) && tmp.message.equals(this.message) ; 
    }
   
    @Override
    public int hashCode() {
      return 463 + 317 * expression.hashCode() + 857 * message.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getMessage() {
      return this.message;
    }
  
    @Override
    public boolean hasMessage() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(expression), clone(message));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isAssignment() {
    return false;
  }

  static public class Assignment extends Statement {
    // Production: sig("Assignment",[arg("org.rascalmpl.ast.Assignable","assignable"),arg("org.rascalmpl.ast.Assignment","operator"),arg("org.rascalmpl.ast.Statement","statement")],breakable=true)
  
    
    private final org.rascalmpl.ast.Assignable assignable;
    private final org.rascalmpl.ast.Assignment operator;
    private final org.rascalmpl.ast.Statement statement;
  
    public Assignment(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Assignable assignable,  org.rascalmpl.ast.Assignment operator,  org.rascalmpl.ast.Statement statement) {
      super(src, node);
      
      this.assignable = assignable;
      this.operator = operator;
      this.statement = statement;
    }
  
    @Override
    public boolean isAssignment() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementAssignment(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = assignable.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        assignable.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = operator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        operator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = statement.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        statement.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Assignment)) {
        return false;
      }        
      Assignment tmp = (Assignment) o;
      return true && tmp.assignable.equals(this.assignable) && tmp.operator.equals(this.operator) && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 353 + 53 * assignable.hashCode() + 149 * operator.hashCode() + 127 * statement.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Assignable getAssignable() {
      return this.assignable;
    }
  
    @Override
    public boolean hasAssignable() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Assignment getOperator() {
      return this.operator;
    }
  
    @Override
    public boolean hasOperator() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Statement getStatement() {
      return this.statement;
    }
  
    @Override
    public boolean hasStatement() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(assignable), clone(operator), clone(statement));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isBreak() {
    return false;
  }

  static public class Break extends Statement {
    // Production: sig("Break",[arg("org.rascalmpl.ast.Target","target")],breakable=true)
  
    
    private final org.rascalmpl.ast.Target target;
  
    public Break(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Target target) {
      super(src, node);
      
      this.target = target;
    }
  
    @Override
    public boolean isBreak() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementBreak(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = target.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        target.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Break)) {
        return false;
      }        
      Break tmp = (Break) o;
      return true && tmp.target.equals(this.target) ; 
    }
   
    @Override
    public int hashCode() {
      return 827 + 431 * target.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Target getTarget() {
      return this.target;
    }
  
    @Override
    public boolean hasTarget() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(target));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isContinue() {
    return false;
  }

  static public class Continue extends Statement {
    // Production: sig("Continue",[arg("org.rascalmpl.ast.Target","target")],breakable=true)
  
    
    private final org.rascalmpl.ast.Target target;
  
    public Continue(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Target target) {
      super(src, node);
      
      this.target = target;
    }
  
    @Override
    public boolean isContinue() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementContinue(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = target.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        target.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Continue)) {
        return false;
      }        
      Continue tmp = (Continue) o;
      return true && tmp.target.equals(this.target) ; 
    }
   
    @Override
    public int hashCode() {
      return 131 + 827 * target.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Target getTarget() {
      return this.target;
    }
  
    @Override
    public boolean hasTarget() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(target));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isDoWhile() {
    return false;
  }

  static public class DoWhile extends Statement {
    // Production: sig("DoWhile",[arg("org.rascalmpl.ast.Label","label"),arg("org.rascalmpl.ast.Statement","body"),arg("org.rascalmpl.ast.Expression","condition")],breakable=true)
  
    
    private final org.rascalmpl.ast.Label label;
    private final org.rascalmpl.ast.Statement body;
    private final org.rascalmpl.ast.Expression condition;
  
    public DoWhile(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Label label,  org.rascalmpl.ast.Statement body,  org.rascalmpl.ast.Expression condition) {
      super(src, node);
      
      this.label = label;
      this.body = body;
      this.condition = condition;
    }
  
    @Override
    public boolean isDoWhile() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementDoWhile(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = label.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        label.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = body.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        body.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = condition.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        condition.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DoWhile)) {
        return false;
      }        
      DoWhile tmp = (DoWhile) o;
      return true && tmp.label.equals(this.label) && tmp.body.equals(this.body) && tmp.condition.equals(this.condition) ; 
    }
   
    @Override
    public int hashCode() {
      return 2 + 449 * label.hashCode() + 89 * body.hashCode() + 233 * condition.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Label getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Statement getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getCondition() {
      return this.condition;
    }
  
    @Override
    public boolean hasCondition() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(label), clone(body), clone(condition));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isEmptyStatement() {
    return false;
  }

  static public class EmptyStatement extends Statement {
    // Production: sig("EmptyStatement",[],breakable=false)
  
    
  
    public EmptyStatement(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isEmptyStatement() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementEmptyStatement(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof EmptyStatement)) {
        return false;
      }        
      EmptyStatement tmp = (EmptyStatement) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 617 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isExpression() {
    return false;
  }

  static public class Expression extends Statement {
    // Production: sig("Expression",[arg("org.rascalmpl.ast.Expression","expression")],breakable=true)
  
    
    private final org.rascalmpl.ast.Expression expression;
  
    public Expression(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression expression) {
      super(src, node);
      
      this.expression = expression;
    }
  
    @Override
    public boolean isExpression() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementExpression(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = expression.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        expression.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Expression)) {
        return false;
      }        
      Expression tmp = (Expression) o;
      return true && tmp.expression.equals(this.expression) ; 
    }
   
    @Override
    public int hashCode() {
      return 151 + 19 * expression.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(expression));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isFail() {
    return false;
  }

  static public class Fail extends Statement {
    // Production: sig("Fail",[arg("org.rascalmpl.ast.Target","target")],breakable=true)
  
    
    private final org.rascalmpl.ast.Target target;
  
    public Fail(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Target target) {
      super(src, node);
      
      this.target = target;
    }
  
    @Override
    public boolean isFail() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementFail(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = target.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        target.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Fail)) {
        return false;
      }        
      Fail tmp = (Fail) o;
      return true && tmp.target.equals(this.target) ; 
    }
   
    @Override
    public int hashCode() {
      return 787 + 457 * target.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Target getTarget() {
      return this.target;
    }
  
    @Override
    public boolean hasTarget() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(target));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isFilter() {
    return false;
  }

  static public class Filter extends Statement {
    // Production: sig("Filter",[],breakable=true)
  
    
  
    public Filter(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isFilter() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementFilter(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Filter)) {
        return false;
      }        
      Filter tmp = (Filter) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 283 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isFor() {
    return false;
  }

  static public class For extends Statement {
    // Production: sig("For",[arg("org.rascalmpl.ast.Label","label"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","generators"),arg("org.rascalmpl.ast.Statement","body")],breakable=true)
  
    
    private final org.rascalmpl.ast.Label label;
    private final java.util.List<org.rascalmpl.ast.Expression> generators;
    private final org.rascalmpl.ast.Statement body;
  
    public For(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Expression> generators,  org.rascalmpl.ast.Statement body) {
      super(src, node);
      
      this.label = label;
      this.generators = generators;
      this.body = body;
    }
  
    @Override
    public boolean isFor() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementFor(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = label.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        label.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : generators) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = body.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        body.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof For)) {
        return false;
      }        
      For tmp = (For) o;
      return true && tmp.label.equals(this.label) && tmp.generators.equals(this.generators) && tmp.body.equals(this.body) ; 
    }
   
    @Override
    public int hashCode() {
      return 739 + 547 * label.hashCode() + 97 * generators.hashCode() + 607 * body.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Label getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getGenerators() {
      return this.generators;
    }
  
    @Override
    public boolean hasGenerators() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Statement getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(label), clone(generators), clone(body));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isFunctionDeclaration() {
    return false;
  }

  static public class FunctionDeclaration extends Statement {
    // Production: sig("FunctionDeclaration",[arg("org.rascalmpl.ast.FunctionDeclaration","functionDeclaration")],breakable=true)
  
    
    private final org.rascalmpl.ast.FunctionDeclaration functionDeclaration;
  
    public FunctionDeclaration(ISourceLocation src, IConstructor node , org.rascalmpl.ast.FunctionDeclaration functionDeclaration) {
      super(src, node);
      
      this.functionDeclaration = functionDeclaration;
    }
  
    @Override
    public boolean isFunctionDeclaration() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementFunctionDeclaration(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = functionDeclaration.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        functionDeclaration.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof FunctionDeclaration)) {
        return false;
      }        
      FunctionDeclaration tmp = (FunctionDeclaration) o;
      return true && tmp.functionDeclaration.equals(this.functionDeclaration) ; 
    }
   
    @Override
    public int hashCode() {
      return 73 + 677 * functionDeclaration.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.FunctionDeclaration getFunctionDeclaration() {
      return this.functionDeclaration;
    }
  
    @Override
    public boolean hasFunctionDeclaration() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(functionDeclaration));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isGlobalDirective() {
    return false;
  }

  static public class GlobalDirective extends Statement {
    // Production: sig("GlobalDirective",[arg("org.rascalmpl.ast.Type","type"),arg("java.util.List\<org.rascalmpl.ast.QualifiedName\>","names")],breakable=true)
  
    
    private final org.rascalmpl.ast.Type type;
    private final java.util.List<org.rascalmpl.ast.QualifiedName> names;
  
    public GlobalDirective(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Type type,  java.util.List<org.rascalmpl.ast.QualifiedName> names) {
      super(src, node);
      
      this.type = type;
      this.names = names;
    }
  
    @Override
    public boolean isGlobalDirective() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementGlobalDirective(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = type.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        type.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : names) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof GlobalDirective)) {
        return false;
      }        
      GlobalDirective tmp = (GlobalDirective) o;
      return true && tmp.type.equals(this.type) && tmp.names.equals(this.names) ; 
    }
   
    @Override
    public int hashCode() {
      return 89 + 599 * type.hashCode() + 3 * names.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Type getType() {
      return this.type;
    }
  
    @Override
    public boolean hasType() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.QualifiedName> getNames() {
      return this.names;
    }
  
    @Override
    public boolean hasNames() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(type), clone(names));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isIfThen() {
    return false;
  }

  static public class IfThen extends Statement {
    // Production: sig("IfThen",[arg("org.rascalmpl.ast.Label","label"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions"),arg("org.rascalmpl.ast.Statement","thenStatement")],breakable=true)
  
    
    private final org.rascalmpl.ast.Label label;
    private final java.util.List<org.rascalmpl.ast.Expression> conditions;
    private final org.rascalmpl.ast.Statement thenStatement;
  
    public IfThen(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Expression> conditions,  org.rascalmpl.ast.Statement thenStatement) {
      super(src, node);
      
      this.label = label;
      this.conditions = conditions;
      this.thenStatement = thenStatement;
    }
  
    @Override
    public boolean isIfThen() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementIfThen(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = label.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        label.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : conditions) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = thenStatement.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        thenStatement.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof IfThen)) {
        return false;
      }        
      IfThen tmp = (IfThen) o;
      return true && tmp.label.equals(this.label) && tmp.conditions.equals(this.conditions) && tmp.thenStatement.equals(this.thenStatement) ; 
    }
   
    @Override
    public int hashCode() {
      return 757 + 193 * label.hashCode() + 881 * conditions.hashCode() + 823 * thenStatement.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Label getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getConditions() {
      return this.conditions;
    }
  
    @Override
    public boolean hasConditions() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Statement getThenStatement() {
      return this.thenStatement;
    }
  
    @Override
    public boolean hasThenStatement() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(label), clone(conditions), clone(thenStatement));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isIfThenElse() {
    return false;
  }

  static public class IfThenElse extends Statement {
    // Production: sig("IfThenElse",[arg("org.rascalmpl.ast.Label","label"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions"),arg("org.rascalmpl.ast.Statement","thenStatement"),arg("org.rascalmpl.ast.Statement","elseStatement")],breakable=true)
  
    
    private final org.rascalmpl.ast.Label label;
    private final java.util.List<org.rascalmpl.ast.Expression> conditions;
    private final org.rascalmpl.ast.Statement thenStatement;
    private final org.rascalmpl.ast.Statement elseStatement;
  
    public IfThenElse(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Expression> conditions,  org.rascalmpl.ast.Statement thenStatement,  org.rascalmpl.ast.Statement elseStatement) {
      super(src, node);
      
      this.label = label;
      this.conditions = conditions;
      this.thenStatement = thenStatement;
      this.elseStatement = elseStatement;
    }
  
    @Override
    public boolean isIfThenElse() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementIfThenElse(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = label.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        label.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : conditions) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = thenStatement.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        thenStatement.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = elseStatement.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        elseStatement.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof IfThenElse)) {
        return false;
      }        
      IfThenElse tmp = (IfThenElse) o;
      return true && tmp.label.equals(this.label) && tmp.conditions.equals(this.conditions) && tmp.thenStatement.equals(this.thenStatement) && tmp.elseStatement.equals(this.elseStatement) ; 
    }
   
    @Override
    public int hashCode() {
      return 859 + 181 * label.hashCode() + 677 * conditions.hashCode() + 433 * thenStatement.hashCode() + 67 * elseStatement.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Label getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getConditions() {
      return this.conditions;
    }
  
    @Override
    public boolean hasConditions() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Statement getThenStatement() {
      return this.thenStatement;
    }
  
    @Override
    public boolean hasThenStatement() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Statement getElseStatement() {
      return this.elseStatement;
    }
  
    @Override
    public boolean hasElseStatement() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(label), clone(conditions), clone(thenStatement), clone(elseStatement));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isInsert() {
    return false;
  }

  static public class Insert extends Statement {
    // Production: sig("Insert",[arg("org.rascalmpl.ast.DataTarget","dataTarget"),arg("org.rascalmpl.ast.Statement","statement")],breakable=true)
  
    
    private final org.rascalmpl.ast.DataTarget dataTarget;
    private final org.rascalmpl.ast.Statement statement;
  
    public Insert(ISourceLocation src, IConstructor node , org.rascalmpl.ast.DataTarget dataTarget,  org.rascalmpl.ast.Statement statement) {
      super(src, node);
      
      this.dataTarget = dataTarget;
      this.statement = statement;
    }
  
    @Override
    public boolean isInsert() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementInsert(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = dataTarget.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        dataTarget.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = statement.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        statement.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Insert)) {
        return false;
      }        
      Insert tmp = (Insert) o;
      return true && tmp.dataTarget.equals(this.dataTarget) && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 757 + 479 * dataTarget.hashCode() + 691 * statement.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.DataTarget getDataTarget() {
      return this.dataTarget;
    }
  
    @Override
    public boolean hasDataTarget() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Statement getStatement() {
      return this.statement;
    }
  
    @Override
    public boolean hasStatement() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(dataTarget), clone(statement));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isNonEmptyBlock() {
    return false;
  }

  static public class NonEmptyBlock extends Statement {
    // Production: sig("NonEmptyBlock",[arg("org.rascalmpl.ast.Label","label"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","statements")],breakable=false)
  
    
    private final org.rascalmpl.ast.Label label;
    private final java.util.List<org.rascalmpl.ast.Statement> statements;
  
    public NonEmptyBlock(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Statement> statements) {
      super(src, node);
      
      this.label = label;
      this.statements = statements;
    }
  
    @Override
    public boolean isNonEmptyBlock() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementNonEmptyBlock(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = label.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        label.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : statements) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof NonEmptyBlock)) {
        return false;
      }        
      NonEmptyBlock tmp = (NonEmptyBlock) o;
      return true && tmp.label.equals(this.label) && tmp.statements.equals(this.statements) ; 
    }
   
    @Override
    public int hashCode() {
      return 193 + 113 * label.hashCode() + 607 * statements.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Label getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getStatements() {
      return this.statements;
    }
  
    @Override
    public boolean hasStatements() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(label), clone(statements));
    }
            
  }
  public boolean isReturn() {
    return false;
  }

  static public class Return extends Statement {
    // Production: sig("Return",[arg("org.rascalmpl.ast.Statement","statement")],breakable=true)
  
    
    private final org.rascalmpl.ast.Statement statement;
  
    public Return(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Statement statement) {
      super(src, node);
      
      this.statement = statement;
    }
  
    @Override
    public boolean isReturn() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementReturn(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = statement.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        statement.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Return)) {
        return false;
      }        
      Return tmp = (Return) o;
      return true && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 151 + 409 * statement.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Statement getStatement() {
      return this.statement;
    }
  
    @Override
    public boolean hasStatement() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(statement));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isSolve() {
    return false;
  }

  static public class Solve extends Statement {
    // Production: sig("Solve",[arg("java.util.List\<org.rascalmpl.ast.QualifiedName\>","variables"),arg("org.rascalmpl.ast.Bound","bound"),arg("org.rascalmpl.ast.Statement","body")],breakable=true)
  
    
    private final java.util.List<org.rascalmpl.ast.QualifiedName> variables;
    private final org.rascalmpl.ast.Bound bound;
    private final org.rascalmpl.ast.Statement body;
  
    public Solve(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.QualifiedName> variables,  org.rascalmpl.ast.Bound bound,  org.rascalmpl.ast.Statement body) {
      super(src, node);
      
      this.variables = variables;
      this.bound = bound;
      this.body = body;
    }
  
    @Override
    public boolean isSolve() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementSolve(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : variables) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = bound.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        bound.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = body.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        body.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Solve)) {
        return false;
      }        
      Solve tmp = (Solve) o;
      return true && tmp.variables.equals(this.variables) && tmp.bound.equals(this.bound) && tmp.body.equals(this.body) ; 
    }
   
    @Override
    public int hashCode() {
      return 691 + 691 * variables.hashCode() + 73 * bound.hashCode() + 457 * body.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.QualifiedName> getVariables() {
      return this.variables;
    }
  
    @Override
    public boolean hasVariables() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Bound getBound() {
      return this.bound;
    }
  
    @Override
    public boolean hasBound() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Statement getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(variables), clone(bound), clone(body));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isSwitch() {
    return false;
  }

  static public class Switch extends Statement {
    // Production: sig("Switch",[arg("org.rascalmpl.ast.Label","label"),arg("org.rascalmpl.ast.Expression","expression"),arg("java.util.List\<org.rascalmpl.ast.Case\>","cases")],breakable=true)
  
    
    private final org.rascalmpl.ast.Label label;
    private final org.rascalmpl.ast.Expression expression;
    private final java.util.List<org.rascalmpl.ast.Case> cases;
  
    public Switch(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Label label,  org.rascalmpl.ast.Expression expression,  java.util.List<org.rascalmpl.ast.Case> cases) {
      super(src, node);
      
      this.label = label;
      this.expression = expression;
      this.cases = cases;
    }
  
    @Override
    public boolean isSwitch() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementSwitch(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = label.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        label.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = expression.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        expression.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : cases) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Switch)) {
        return false;
      }        
      Switch tmp = (Switch) o;
      return true && tmp.label.equals(this.label) && tmp.expression.equals(this.expression) && tmp.cases.equals(this.cases) ; 
    }
   
    @Override
    public int hashCode() {
      return 719 + 461 * label.hashCode() + 929 * expression.hashCode() + 199 * cases.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Label getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Case> getCases() {
      return this.cases;
    }
  
    @Override
    public boolean hasCases() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(label), clone(expression), clone(cases));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isThrow() {
    return false;
  }

  static public class Throw extends Statement {
    // Production: sig("Throw",[arg("org.rascalmpl.ast.Statement","statement")],breakable=true)
  
    
    private final org.rascalmpl.ast.Statement statement;
  
    public Throw(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Statement statement) {
      super(src, node);
      
      this.statement = statement;
    }
  
    @Override
    public boolean isThrow() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementThrow(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = statement.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        statement.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Throw)) {
        return false;
      }        
      Throw tmp = (Throw) o;
      return true && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 569 + 149 * statement.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Statement getStatement() {
      return this.statement;
    }
  
    @Override
    public boolean hasStatement() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(statement));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isTry() {
    return false;
  }

  static public class Try extends Statement {
    // Production: sig("Try",[arg("org.rascalmpl.ast.Statement","body"),arg("java.util.List\<org.rascalmpl.ast.Catch\>","handlers")],breakable=true)
  
    
    private final org.rascalmpl.ast.Statement body;
    private final java.util.List<org.rascalmpl.ast.Catch> handlers;
  
    public Try(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Statement body,  java.util.List<org.rascalmpl.ast.Catch> handlers) {
      super(src, node);
      
      this.body = body;
      this.handlers = handlers;
    }
  
    @Override
    public boolean isTry() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementTry(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = body.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        body.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : handlers) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Try)) {
        return false;
      }        
      Try tmp = (Try) o;
      return true && tmp.body.equals(this.body) && tmp.handlers.equals(this.handlers) ; 
    }
   
    @Override
    public int hashCode() {
      return 569 + 617 * body.hashCode() + 389 * handlers.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Statement getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Catch> getHandlers() {
      return this.handlers;
    }
  
    @Override
    public boolean hasHandlers() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(body), clone(handlers));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isTryFinally() {
    return false;
  }

  static public class TryFinally extends Statement {
    // Production: sig("TryFinally",[arg("org.rascalmpl.ast.Statement","body"),arg("java.util.List\<org.rascalmpl.ast.Catch\>","handlers"),arg("org.rascalmpl.ast.Statement","finallyBody")],breakable=true)
  
    
    private final org.rascalmpl.ast.Statement body;
    private final java.util.List<org.rascalmpl.ast.Catch> handlers;
    private final org.rascalmpl.ast.Statement finallyBody;
  
    public TryFinally(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Statement body,  java.util.List<org.rascalmpl.ast.Catch> handlers,  org.rascalmpl.ast.Statement finallyBody) {
      super(src, node);
      
      this.body = body;
      this.handlers = handlers;
      this.finallyBody = finallyBody;
    }
  
    @Override
    public boolean isTryFinally() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementTryFinally(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = body.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        body.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : handlers) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = finallyBody.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        finallyBody.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof TryFinally)) {
        return false;
      }        
      TryFinally tmp = (TryFinally) o;
      return true && tmp.body.equals(this.body) && tmp.handlers.equals(this.handlers) && tmp.finallyBody.equals(this.finallyBody) ; 
    }
   
    @Override
    public int hashCode() {
      return 757 + 757 * body.hashCode() + 439 * handlers.hashCode() + 239 * finallyBody.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Statement getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Catch> getHandlers() {
      return this.handlers;
    }
  
    @Override
    public boolean hasHandlers() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Statement getFinallyBody() {
      return this.finallyBody;
    }
  
    @Override
    public boolean hasFinallyBody() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(body), clone(handlers), clone(finallyBody));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isVariableDeclaration() {
    return false;
  }

  static public class VariableDeclaration extends Statement {
    // Production: sig("VariableDeclaration",[arg("org.rascalmpl.ast.LocalVariableDeclaration","declaration")],breakable=true)
  
    
    private final org.rascalmpl.ast.LocalVariableDeclaration declaration;
  
    public VariableDeclaration(ISourceLocation src, IConstructor node , org.rascalmpl.ast.LocalVariableDeclaration declaration) {
      super(src, node);
      
      this.declaration = declaration;
    }
  
    @Override
    public boolean isVariableDeclaration() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementVariableDeclaration(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = declaration.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        declaration.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof VariableDeclaration)) {
        return false;
      }        
      VariableDeclaration tmp = (VariableDeclaration) o;
      return true && tmp.declaration.equals(this.declaration) ; 
    }
   
    @Override
    public int hashCode() {
      return 641 + 227 * declaration.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.LocalVariableDeclaration getDeclaration() {
      return this.declaration;
    }
  
    @Override
    public boolean hasDeclaration() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(declaration));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isVisit() {
    return false;
  }

  static public class Visit extends Statement {
    // Production: sig("Visit",[arg("org.rascalmpl.ast.Label","label"),arg("org.rascalmpl.ast.Visit","visit")],breakable=true)
  
    
    private final org.rascalmpl.ast.Label label;
    private final org.rascalmpl.ast.Visit visit;
  
    public Visit(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Label label,  org.rascalmpl.ast.Visit visit) {
      super(src, node);
      
      this.label = label;
      this.visit = visit;
    }
  
    @Override
    public boolean isVisit() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementVisit(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = label.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        label.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = visit.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        visit.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Visit)) {
        return false;
      }        
      Visit tmp = (Visit) o;
      return true && tmp.label.equals(this.label) && tmp.visit.equals(this.visit) ; 
    }
   
    @Override
    public int hashCode() {
      return 269 + 389 * label.hashCode() + 367 * visit.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Label getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Visit getVisit() {
      return this.visit;
    }
  
    @Override
    public boolean hasVisit() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(label), clone(visit));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
  public boolean isWhile() {
    return false;
  }

  static public class While extends Statement {
    // Production: sig("While",[arg("org.rascalmpl.ast.Label","label"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions"),arg("org.rascalmpl.ast.Statement","body")],breakable=true)
  
    
    private final org.rascalmpl.ast.Label label;
    private final java.util.List<org.rascalmpl.ast.Expression> conditions;
    private final org.rascalmpl.ast.Statement body;
  
    public While(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Expression> conditions,  org.rascalmpl.ast.Statement body) {
      super(src, node);
      
      this.label = label;
      this.conditions = conditions;
      this.body = body;
    }
  
    @Override
    public boolean isWhile() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStatementWhile(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = label.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        label.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : conditions) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = body.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        body.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof While)) {
        return false;
      }        
      While tmp = (While) o;
      return true && tmp.label.equals(this.label) && tmp.conditions.equals(this.conditions) && tmp.body.equals(this.body) ; 
    }
   
    @Override
    public int hashCode() {
      return 643 + 619 * label.hashCode() + 631 * conditions.hashCode() + 151 * body.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Label getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getConditions() {
      return this.conditions;
    }
  
    @Override
    public boolean hasConditions() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Statement getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(label), clone(conditions), clone(body));
    }
    @Override
    public boolean isBreakable() {
      return true;
    }        
  }
}