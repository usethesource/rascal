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
    public boolean equals(Object o) {
      if (!(o instanceof Declaration)) {
        return false;
      }        
      Declaration tmp = (Declaration) o;
      return true && tmp.declaration.equals(this.declaration) ; 
    }
   
    @Override
    public int hashCode() {
      return 359 + 23 * declaration.hashCode() ; 
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
    public boolean equals(Object o) {
      if (!(o instanceof Expression)) {
        return false;
      }        
      Expression tmp = (Expression) o;
      return true && tmp.expression.equals(this.expression) ; 
    }
   
    @Override
    public int hashCode() {
      return 503 + 751 * expression.hashCode() ; 
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
    public boolean equals(Object o) {
      if (!(o instanceof Import)) {
        return false;
      }        
      Import tmp = (Import) o;
      return true && tmp.imported.equals(this.imported) ; 
    }
   
    @Override
    public int hashCode() {
      return 181 + 643 * imported.hashCode() ; 
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
    public boolean equals(Object o) {
      if (!(o instanceof Shell)) {
        return false;
      }        
      Shell tmp = (Shell) o;
      return true && tmp.command.equals(this.command) ; 
    }
   
    @Override
    public int hashCode() {
      return 647 + 107 * command.hashCode() ; 
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
    public boolean equals(Object o) {
      if (!(o instanceof Statement)) {
        return false;
      }        
      Statement tmp = (Statement) o;
      return true && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 439 + 937 * statement.hashCode() ; 
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