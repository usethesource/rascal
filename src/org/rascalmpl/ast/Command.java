/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
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
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public abstract class Command extends AbstractAST {
  public Command(IConstructor node) {
    super();
  }

  
  public boolean hasDeclaration() {
    return false;
  }

  public org.rascalmpl.ast.Declaration getDeclaration() {
    throw new UnsupportedOperationException();
  }
  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }
  public boolean hasImported() {
    return false;
  }

  public org.rascalmpl.ast.Import getImported() {
    throw new UnsupportedOperationException();
  }
  public boolean hasCommand() {
    return false;
  }

  public org.rascalmpl.ast.ShellCommand getCommand() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStatement() {
    return false;
  }

  public org.rascalmpl.ast.Statement getStatement() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends Command {
    private final java.util.List<org.rascalmpl.ast.Command> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Command> alternatives) {
      super(node);
      this.node = node;
      this.alternatives = java.util.Collections.unmodifiableList(alternatives);
    }
    
    @Override
    public IConstructor getTree() {
      return node;
    }
  
    @Override
    public AbstractAST findNode(int offset) {
      return null;
    }
  
    @Override
    public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
      throw new Ambiguous(src);
    }
      
    @Override
    public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env) {
      throw new Ambiguous(src);
    }
    
    public java.util.List<org.rascalmpl.ast.Command> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitCommandAmbiguity(this);
    }
  }

  

  
  public boolean isDeclaration() {
    return false;
  }

  static public class Declaration extends Command {
    // Production: sig("Declaration",[arg("org.rascalmpl.ast.Declaration","declaration")])
  
    
    private final org.rascalmpl.ast.Declaration declaration;
  
    public Declaration(IConstructor node , org.rascalmpl.ast.Declaration declaration) {
      super(node);
      
      this.declaration = declaration;
    }
  
    @Override
    public boolean isDeclaration() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitCommandDeclaration(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Declaration getDeclaration() {
      return this.declaration;
    }
  
    @Override
    public boolean hasDeclaration() {
      return true;
    }	
  }
  public boolean isExpression() {
    return false;
  }

  static public class Expression extends Command {
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
      return visitor.visitCommandExpression(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }	
  }
  public boolean isImport() {
    return false;
  }

  static public class Import extends Command {
    // Production: sig("Import",[arg("org.rascalmpl.ast.Import","imported")])
  
    
    private final org.rascalmpl.ast.Import imported;
  
    public Import(IConstructor node , org.rascalmpl.ast.Import imported) {
      super(node);
      
      this.imported = imported;
    }
  
    @Override
    public boolean isImport() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitCommandImport(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Import getImported() {
      return this.imported;
    }
  
    @Override
    public boolean hasImported() {
      return true;
    }	
  }
  public boolean isShell() {
    return false;
  }

  static public class Shell extends Command {
    // Production: sig("Shell",[arg("org.rascalmpl.ast.ShellCommand","command")])
  
    
    private final org.rascalmpl.ast.ShellCommand command;
  
    public Shell(IConstructor node , org.rascalmpl.ast.ShellCommand command) {
      super(node);
      
      this.command = command;
    }
  
    @Override
    public boolean isShell() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitCommandShell(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.ShellCommand getCommand() {
      return this.command;
    }
  
    @Override
    public boolean hasCommand() {
      return true;
    }	
  }
  public boolean isStatement() {
    return false;
  }

  static public class Statement extends Command {
    // Production: sig("Statement",[arg("org.rascalmpl.ast.Statement","statement")])
  
    
    private final org.rascalmpl.ast.Statement statement;
  
    public Statement(IConstructor node , org.rascalmpl.ast.Statement statement) {
      super(node);
      
      this.statement = statement;
    }
  
    @Override
    public boolean isStatement() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitCommandStatement(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Statement getStatement() {
      return this.statement;
    }
  
    @Override
    public boolean hasStatement() {
      return true;
    }	
  }
}