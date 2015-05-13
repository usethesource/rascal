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


import org.eclipse.imp.pdb.facts.IConstructor;

public abstract class Statement extends AbstractAST {
  public Statement(IConstructor node) {
    super();
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
    // Production: sig("Append",[arg("org.rascalmpl.ast.DataTarget","dataTarget"),arg("org.rascalmpl.ast.Statement","statement")])
  
    
    private final org.rascalmpl.ast.DataTarget dataTarget;
    private final org.rascalmpl.ast.Statement statement;
  
    public Append(IConstructor node , org.rascalmpl.ast.DataTarget dataTarget,  org.rascalmpl.ast.Statement statement) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Append)) {
        return false;
      }        
      Append tmp = (Append) o;
      return true && tmp.dataTarget.equals(this.dataTarget) && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 163 + 19 * dataTarget.hashCode() + 229 * statement.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(dataTarget), clone(statement));
    }
  }
  public boolean isAssert() {
    return false;
  }

  static public class Assert extends Statement {
    // Production: sig("Assert",[arg("org.rascalmpl.ast.Expression","expression")])
  
    
    private final org.rascalmpl.ast.Expression expression;
  
    public Assert(IConstructor node , org.rascalmpl.ast.Expression expression) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Assert)) {
        return false;
      }        
      Assert tmp = (Assert) o;
      return true && tmp.expression.equals(this.expression) ; 
    }
   
    @Override
    public int hashCode() {
      return 101 + 761 * expression.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(expression));
    }
  }
  public boolean isAssertWithMessage() {
    return false;
  }

  static public class AssertWithMessage extends Statement {
    // Production: sig("AssertWithMessage",[arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.Expression","message")])
  
    
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.Expression message;
  
    public AssertWithMessage(IConstructor node , org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.Expression message) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof AssertWithMessage)) {
        return false;
      }        
      AssertWithMessage tmp = (AssertWithMessage) o;
      return true && tmp.expression.equals(this.expression) && tmp.message.equals(this.message) ; 
    }
   
    @Override
    public int hashCode() {
      return 181 + 439 * expression.hashCode() + 43 * message.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(expression), clone(message));
    }
  }
  public boolean isAssignment() {
    return false;
  }

  static public class Assignment extends Statement {
    // Production: sig("Assignment",[arg("org.rascalmpl.ast.Assignable","assignable"),arg("org.rascalmpl.ast.Assignment","operator"),arg("org.rascalmpl.ast.Statement","statement")])
  
    
    private final org.rascalmpl.ast.Assignable assignable;
    private final org.rascalmpl.ast.Assignment operator;
    private final org.rascalmpl.ast.Statement statement;
  
    public Assignment(IConstructor node , org.rascalmpl.ast.Assignable assignable,  org.rascalmpl.ast.Assignment operator,  org.rascalmpl.ast.Statement statement) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Assignment)) {
        return false;
      }        
      Assignment tmp = (Assignment) o;
      return true && tmp.assignable.equals(this.assignable) && tmp.operator.equals(this.operator) && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 769 + 613 * assignable.hashCode() + 967 * operator.hashCode() + 257 * statement.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(assignable), clone(operator), clone(statement));
    }
  }
  public boolean isBreak() {
    return false;
  }

  static public class Break extends Statement {
    // Production: sig("Break",[arg("org.rascalmpl.ast.Target","target")])
  
    
    private final org.rascalmpl.ast.Target target;
  
    public Break(IConstructor node , org.rascalmpl.ast.Target target) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Break)) {
        return false;
      }        
      Break tmp = (Break) o;
      return true && tmp.target.equals(this.target) ; 
    }
   
    @Override
    public int hashCode() {
      return 661 + 809 * target.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(target));
    }
  }
  public boolean isContinue() {
    return false;
  }

  static public class Continue extends Statement {
    // Production: sig("Continue",[arg("org.rascalmpl.ast.Target","target")])
  
    
    private final org.rascalmpl.ast.Target target;
  
    public Continue(IConstructor node , org.rascalmpl.ast.Target target) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Continue)) {
        return false;
      }        
      Continue tmp = (Continue) o;
      return true && tmp.target.equals(this.target) ; 
    }
   
    @Override
    public int hashCode() {
      return 503 + 167 * target.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(target));
    }
  }
  public boolean isDoWhile() {
    return false;
  }

  static public class DoWhile extends Statement {
    // Production: sig("DoWhile",[arg("org.rascalmpl.ast.Label","label"),arg("org.rascalmpl.ast.Statement","body"),arg("org.rascalmpl.ast.Expression","condition")])
  
    
    private final org.rascalmpl.ast.Label label;
    private final org.rascalmpl.ast.Statement body;
    private final org.rascalmpl.ast.Expression condition;
  
    public DoWhile(IConstructor node , org.rascalmpl.ast.Label label,  org.rascalmpl.ast.Statement body,  org.rascalmpl.ast.Expression condition) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof DoWhile)) {
        return false;
      }        
      DoWhile tmp = (DoWhile) o;
      return true && tmp.label.equals(this.label) && tmp.body.equals(this.body) && tmp.condition.equals(this.condition) ; 
    }
   
    @Override
    public int hashCode() {
      return 617 + 83 * label.hashCode() + 727 * body.hashCode() + 239 * condition.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(label), clone(body), clone(condition));
    }
  }
  public boolean isEmptyStatement() {
    return false;
  }

  static public class EmptyStatement extends Statement {
    // Production: sig("EmptyStatement",[])
  
    
  
    public EmptyStatement(IConstructor node ) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof EmptyStatement)) {
        return false;
      }        
      EmptyStatement tmp = (EmptyStatement) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 719 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), (IConstructor) null );
    }
  }
  public boolean isExpression() {
    return false;
  }

  static public class Expression extends Statement {
    // Production: sig("Expression",[arg("org.rascalmpl.ast.Expression","expression")])
  
    
    private final org.rascalmpl.ast.Expression expression;
  
    public Expression(IConstructor node , org.rascalmpl.ast.Expression expression) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Expression)) {
        return false;
      }        
      Expression tmp = (Expression) o;
      return true && tmp.expression.equals(this.expression) ; 
    }
   
    @Override
    public int hashCode() {
      return 653 + 653 * expression.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(expression));
    }
  }
  public boolean isFail() {
    return false;
  }

  static public class Fail extends Statement {
    // Production: sig("Fail",[arg("org.rascalmpl.ast.Target","target")])
  
    
    private final org.rascalmpl.ast.Target target;
  
    public Fail(IConstructor node , org.rascalmpl.ast.Target target) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Fail)) {
        return false;
      }        
      Fail tmp = (Fail) o;
      return true && tmp.target.equals(this.target) ; 
    }
   
    @Override
    public int hashCode() {
      return 397 + 251 * target.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(target));
    }
  }
  public boolean isFilter() {
    return false;
  }

  static public class Filter extends Statement {
    // Production: sig("Filter",[])
  
    
  
    public Filter(IConstructor node ) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Filter)) {
        return false;
      }        
      Filter tmp = (Filter) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 61 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), (IConstructor) null );
    }
  }
  public boolean isFor() {
    return false;
  }

  static public class For extends Statement {
    // Production: sig("For",[arg("org.rascalmpl.ast.Label","label"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","generators"),arg("org.rascalmpl.ast.Statement","body")])
  
    
    private final org.rascalmpl.ast.Label label;
    private final java.util.List<org.rascalmpl.ast.Expression> generators;
    private final org.rascalmpl.ast.Statement body;
  
    public For(IConstructor node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Expression> generators,  org.rascalmpl.ast.Statement body) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof For)) {
        return false;
      }        
      For tmp = (For) o;
      return true && tmp.label.equals(this.label) && tmp.generators.equals(this.generators) && tmp.body.equals(this.body) ; 
    }
   
    @Override
    public int hashCode() {
      return 127 + 577 * label.hashCode() + 41 * generators.hashCode() + 137 * body.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(label), clone(generators), clone(body));
    }
  }
  public boolean isFunctionDeclaration() {
    return false;
  }

  static public class FunctionDeclaration extends Statement {
    // Production: sig("FunctionDeclaration",[arg("org.rascalmpl.ast.FunctionDeclaration","functionDeclaration")])
  
    
    private final org.rascalmpl.ast.FunctionDeclaration functionDeclaration;
  
    public FunctionDeclaration(IConstructor node , org.rascalmpl.ast.FunctionDeclaration functionDeclaration) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof FunctionDeclaration)) {
        return false;
      }        
      FunctionDeclaration tmp = (FunctionDeclaration) o;
      return true && tmp.functionDeclaration.equals(this.functionDeclaration) ; 
    }
   
    @Override
    public int hashCode() {
      return 499 + 7 * functionDeclaration.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(functionDeclaration));
    }
  }
  public boolean isGlobalDirective() {
    return false;
  }

  static public class GlobalDirective extends Statement {
    // Production: sig("GlobalDirective",[arg("org.rascalmpl.ast.Type","type"),arg("java.util.List\<org.rascalmpl.ast.QualifiedName\>","names")])
  
    
    private final org.rascalmpl.ast.Type type;
    private final java.util.List<org.rascalmpl.ast.QualifiedName> names;
  
    public GlobalDirective(IConstructor node , org.rascalmpl.ast.Type type,  java.util.List<org.rascalmpl.ast.QualifiedName> names) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof GlobalDirective)) {
        return false;
      }        
      GlobalDirective tmp = (GlobalDirective) o;
      return true && tmp.type.equals(this.type) && tmp.names.equals(this.names) ; 
    }
   
    @Override
    public int hashCode() {
      return 919 + 97 * type.hashCode() + 491 * names.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(type), clone(names));
    }
  }
  public boolean isIfThen() {
    return false;
  }

  static public class IfThen extends Statement {
    // Production: sig("IfThen",[arg("org.rascalmpl.ast.Label","label"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions"),arg("org.rascalmpl.ast.Statement","thenStatement")])
  
    
    private final org.rascalmpl.ast.Label label;
    private final java.util.List<org.rascalmpl.ast.Expression> conditions;
    private final org.rascalmpl.ast.Statement thenStatement;
  
    public IfThen(IConstructor node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Expression> conditions,  org.rascalmpl.ast.Statement thenStatement) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof IfThen)) {
        return false;
      }        
      IfThen tmp = (IfThen) o;
      return true && tmp.label.equals(this.label) && tmp.conditions.equals(this.conditions) && tmp.thenStatement.equals(this.thenStatement) ; 
    }
   
    @Override
    public int hashCode() {
      return 883 + 743 * label.hashCode() + 619 * conditions.hashCode() + 659 * thenStatement.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(label), clone(conditions), clone(thenStatement));
    }
  }
  public boolean isIfThenElse() {
    return false;
  }

  static public class IfThenElse extends Statement {
    // Production: sig("IfThenElse",[arg("org.rascalmpl.ast.Label","label"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions"),arg("org.rascalmpl.ast.Statement","thenStatement"),arg("org.rascalmpl.ast.Statement","elseStatement")])
  
    
    private final org.rascalmpl.ast.Label label;
    private final java.util.List<org.rascalmpl.ast.Expression> conditions;
    private final org.rascalmpl.ast.Statement thenStatement;
    private final org.rascalmpl.ast.Statement elseStatement;
  
    public IfThenElse(IConstructor node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Expression> conditions,  org.rascalmpl.ast.Statement thenStatement,  org.rascalmpl.ast.Statement elseStatement) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof IfThenElse)) {
        return false;
      }        
      IfThenElse tmp = (IfThenElse) o;
      return true && tmp.label.equals(this.label) && tmp.conditions.equals(this.conditions) && tmp.thenStatement.equals(this.thenStatement) && tmp.elseStatement.equals(this.elseStatement) ; 
    }
   
    @Override
    public int hashCode() {
      return 839 + 167 * label.hashCode() + 577 * conditions.hashCode() + 929 * thenStatement.hashCode() + 809 * elseStatement.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(label), clone(conditions), clone(thenStatement), clone(elseStatement));
    }
  }
  public boolean isInsert() {
    return false;
  }

  static public class Insert extends Statement {
    // Production: sig("Insert",[arg("org.rascalmpl.ast.DataTarget","dataTarget"),arg("org.rascalmpl.ast.Statement","statement")])
  
    
    private final org.rascalmpl.ast.DataTarget dataTarget;
    private final org.rascalmpl.ast.Statement statement;
  
    public Insert(IConstructor node , org.rascalmpl.ast.DataTarget dataTarget,  org.rascalmpl.ast.Statement statement) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Insert)) {
        return false;
      }        
      Insert tmp = (Insert) o;
      return true && tmp.dataTarget.equals(this.dataTarget) && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 181 + 971 * dataTarget.hashCode() + 647 * statement.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(dataTarget), clone(statement));
    }
  }
  public boolean isNonEmptyBlock() {
    return false;
  }

  static public class NonEmptyBlock extends Statement {
    // Production: sig("NonEmptyBlock",[arg("org.rascalmpl.ast.Label","label"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","statements")])
  
    
    private final org.rascalmpl.ast.Label label;
    private final java.util.List<org.rascalmpl.ast.Statement> statements;
  
    public NonEmptyBlock(IConstructor node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Statement> statements) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof NonEmptyBlock)) {
        return false;
      }        
      NonEmptyBlock tmp = (NonEmptyBlock) o;
      return true && tmp.label.equals(this.label) && tmp.statements.equals(this.statements) ; 
    }
   
    @Override
    public int hashCode() {
      return 977 + 659 * label.hashCode() + 229 * statements.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(label), clone(statements));
    }
  }
  public boolean isReturn() {
    return false;
  }

  static public class Return extends Statement {
    // Production: sig("Return",[arg("org.rascalmpl.ast.Statement","statement")])
  
    
    private final org.rascalmpl.ast.Statement statement;
  
    public Return(IConstructor node , org.rascalmpl.ast.Statement statement) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Return)) {
        return false;
      }        
      Return tmp = (Return) o;
      return true && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 337 + 2 * statement.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(statement));
    }
  }
  public boolean isSolve() {
    return false;
  }

  static public class Solve extends Statement {
    // Production: sig("Solve",[arg("java.util.List\<org.rascalmpl.ast.QualifiedName\>","variables"),arg("org.rascalmpl.ast.Bound","bound"),arg("org.rascalmpl.ast.Statement","body")])
  
    
    private final java.util.List<org.rascalmpl.ast.QualifiedName> variables;
    private final org.rascalmpl.ast.Bound bound;
    private final org.rascalmpl.ast.Statement body;
  
    public Solve(IConstructor node , java.util.List<org.rascalmpl.ast.QualifiedName> variables,  org.rascalmpl.ast.Bound bound,  org.rascalmpl.ast.Statement body) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Solve)) {
        return false;
      }        
      Solve tmp = (Solve) o;
      return true && tmp.variables.equals(this.variables) && tmp.bound.equals(this.bound) && tmp.body.equals(this.body) ; 
    }
   
    @Override
    public int hashCode() {
      return 503 + 953 * variables.hashCode() + 283 * bound.hashCode() + 673 * body.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(variables), clone(bound), clone(body));
    }
  }
  public boolean isSwitch() {
    return false;
  }

  static public class Switch extends Statement {
    // Production: sig("Switch",[arg("org.rascalmpl.ast.Label","label"),arg("org.rascalmpl.ast.Expression","expression"),arg("java.util.List\<org.rascalmpl.ast.Case\>","cases")])
  
    
    private final org.rascalmpl.ast.Label label;
    private final org.rascalmpl.ast.Expression expression;
    private final java.util.List<org.rascalmpl.ast.Case> cases;
  
    public Switch(IConstructor node , org.rascalmpl.ast.Label label,  org.rascalmpl.ast.Expression expression,  java.util.List<org.rascalmpl.ast.Case> cases) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Switch)) {
        return false;
      }        
      Switch tmp = (Switch) o;
      return true && tmp.label.equals(this.label) && tmp.expression.equals(this.expression) && tmp.cases.equals(this.cases) ; 
    }
   
    @Override
    public int hashCode() {
      return 433 + 967 * label.hashCode() + 103 * expression.hashCode() + 701 * cases.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(label), clone(expression), clone(cases));
    }
  }
  public boolean isThrow() {
    return false;
  }

  static public class Throw extends Statement {
    // Production: sig("Throw",[arg("org.rascalmpl.ast.Statement","statement")])
  
    
    private final org.rascalmpl.ast.Statement statement;
  
    public Throw(IConstructor node , org.rascalmpl.ast.Statement statement) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Throw)) {
        return false;
      }        
      Throw tmp = (Throw) o;
      return true && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 823 + 461 * statement.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(statement));
    }
  }
  public boolean isTry() {
    return false;
  }

  static public class Try extends Statement {
    // Production: sig("Try",[arg("org.rascalmpl.ast.Statement","body"),arg("java.util.List\<org.rascalmpl.ast.Catch\>","handlers")])
  
    
    private final org.rascalmpl.ast.Statement body;
    private final java.util.List<org.rascalmpl.ast.Catch> handlers;
  
    public Try(IConstructor node , org.rascalmpl.ast.Statement body,  java.util.List<org.rascalmpl.ast.Catch> handlers) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Try)) {
        return false;
      }        
      Try tmp = (Try) o;
      return true && tmp.body.equals(this.body) && tmp.handlers.equals(this.handlers) ; 
    }
   
    @Override
    public int hashCode() {
      return 463 + 181 * body.hashCode() + 733 * handlers.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(body), clone(handlers));
    }
  }
  public boolean isTryFinally() {
    return false;
  }

  static public class TryFinally extends Statement {
    // Production: sig("TryFinally",[arg("org.rascalmpl.ast.Statement","body"),arg("java.util.List\<org.rascalmpl.ast.Catch\>","handlers"),arg("org.rascalmpl.ast.Statement","finallyBody")])
  
    
    private final org.rascalmpl.ast.Statement body;
    private final java.util.List<org.rascalmpl.ast.Catch> handlers;
    private final org.rascalmpl.ast.Statement finallyBody;
  
    public TryFinally(IConstructor node , org.rascalmpl.ast.Statement body,  java.util.List<org.rascalmpl.ast.Catch> handlers,  org.rascalmpl.ast.Statement finallyBody) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof TryFinally)) {
        return false;
      }        
      TryFinally tmp = (TryFinally) o;
      return true && tmp.body.equals(this.body) && tmp.handlers.equals(this.handlers) && tmp.finallyBody.equals(this.finallyBody) ; 
    }
   
    @Override
    public int hashCode() {
      return 397 + 547 * body.hashCode() + 599 * handlers.hashCode() + 373 * finallyBody.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(body), clone(handlers), clone(finallyBody));
    }
  }
  public boolean isVariableDeclaration() {
    return false;
  }

  static public class VariableDeclaration extends Statement {
    // Production: sig("VariableDeclaration",[arg("org.rascalmpl.ast.LocalVariableDeclaration","declaration")])
  
    
    private final org.rascalmpl.ast.LocalVariableDeclaration declaration;
  
    public VariableDeclaration(IConstructor node , org.rascalmpl.ast.LocalVariableDeclaration declaration) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof VariableDeclaration)) {
        return false;
      }        
      VariableDeclaration tmp = (VariableDeclaration) o;
      return true && tmp.declaration.equals(this.declaration) ; 
    }
   
    @Override
    public int hashCode() {
      return 137 + 199 * declaration.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(declaration));
    }
  }
  public boolean isVisit() {
    return false;
  }

  static public class Visit extends Statement {
    // Production: sig("Visit",[arg("org.rascalmpl.ast.Label","label"),arg("org.rascalmpl.ast.Visit","visit")])
  
    
    private final org.rascalmpl.ast.Label label;
    private final org.rascalmpl.ast.Visit visit;
  
    public Visit(IConstructor node , org.rascalmpl.ast.Label label,  org.rascalmpl.ast.Visit visit) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Visit)) {
        return false;
      }        
      Visit tmp = (Visit) o;
      return true && tmp.label.equals(this.label) && tmp.visit.equals(this.visit) ; 
    }
   
    @Override
    public int hashCode() {
      return 823 + 421 * label.hashCode() + 739 * visit.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(label), clone(visit));
    }
  }
  public boolean isWhile() {
    return false;
  }

  static public class While extends Statement {
    // Production: sig("While",[arg("org.rascalmpl.ast.Label","label"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions"),arg("org.rascalmpl.ast.Statement","body")])
  
    
    private final org.rascalmpl.ast.Label label;
    private final java.util.List<org.rascalmpl.ast.Expression> conditions;
    private final org.rascalmpl.ast.Statement body;
  
    public While(IConstructor node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Expression> conditions,  org.rascalmpl.ast.Statement body) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof While)) {
        return false;
      }        
      While tmp = (While) o;
      return true && tmp.label.equals(this.label) && tmp.conditions.equals(this.conditions) && tmp.body.equals(this.body) ; 
    }
   
    @Override
    public int hashCode() {
      return 509 + 769 * label.hashCode() + 179 * conditions.hashCode() + 233 * body.hashCode() ; 
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
      return newInstance(getClass(), (IConstructor) null , clone(label), clone(conditions), clone(body));
    }
  }
}