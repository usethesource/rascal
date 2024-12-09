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
public abstract class Command extends AbstractAST {
  public Command(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
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
    // Production: sig("Declaration",[arg("org.rascalmpl.ast.Declaration","declaration")],breakable=false)
  
    
    private final org.rascalmpl.ast.Declaration declaration;
  
    public Declaration(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Declaration declaration) {
      super(src, node);
      
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
      if (!(o instanceof Declaration)) {
        return false;
      }        
      Declaration tmp = (Declaration) o;
      return true && tmp.declaration.equals(this.declaration) ; 
    }
   
    @Override
    public int hashCode() {
      return 857 + 509 * declaration.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Declaration getDeclaration() {
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
            
  }
  public boolean isExpression() {
    return false;
  }

  static public class Expression extends Command {
    // Production: sig("Expression",[arg("org.rascalmpl.ast.Expression","expression")],breakable=false)
  
    
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
      return visitor.visitCommandExpression(this);
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
      return 7 + 173 * expression.hashCode() ; 
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
            
  }
  public boolean isImport() {
    return false;
  }

  static public class Import extends Command {
    // Production: sig("Import",[arg("org.rascalmpl.ast.Import","imported")],breakable=false)
  
    
    private final org.rascalmpl.ast.Import imported;
  
    public Import(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Import imported) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = imported.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        imported.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 827 + 941 * imported.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Import getImported() {
      return this.imported;
    }
  
    @Override
    public boolean hasImported() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(imported));
    }
            
  }
  public boolean isShell() {
    return false;
  }

  static public class Shell extends Command {
    // Production: sig("Shell",[arg("org.rascalmpl.ast.ShellCommand","command")],breakable=false)
  
    
    private final org.rascalmpl.ast.ShellCommand command;
  
    public Shell(ISourceLocation src, IConstructor node , org.rascalmpl.ast.ShellCommand command) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = command.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        command.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 199 + 661 * command.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.ShellCommand getCommand() {
      return this.command;
    }
  
    @Override
    public boolean hasCommand() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(command));
    }
            
  }
  public boolean isStatement() {
    return false;
  }

  static public class Statement extends Command {
    // Production: sig("Statement",[arg("org.rascalmpl.ast.Statement","statement")],breakable=false)
  
    
    private final org.rascalmpl.ast.Statement statement;
  
    public Statement(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Statement statement) {
      super(src, node);
      
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
      if (!(o instanceof Statement)) {
        return false;
      }        
      Statement tmp = (Statement) o;
      return true && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 487 + 59 * statement.hashCode() ; 
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
            
  }
}