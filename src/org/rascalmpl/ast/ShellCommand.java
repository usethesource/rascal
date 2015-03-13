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

public abstract class ShellCommand extends AbstractAST {
  public ShellCommand(IConstructor node) {
    super();
  }

  
  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getName() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isClear() {
    return false;
  }

  static public class Clear extends ShellCommand {
    // Production: sig("Clear",[])
  
    
  
    public Clear(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isClear() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandClear(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Clear)) {
        return false;
      }        
      Clear tmp = (Clear) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 67 ; 
    } 
  
    	
  }
  public boolean isEdit() {
    return false;
  }

  static public class Edit extends ShellCommand {
    // Production: sig("Edit",[arg("org.rascalmpl.ast.QualifiedName","name")])
  
    
    private final org.rascalmpl.ast.QualifiedName name;
  
    public Edit(IConstructor node , org.rascalmpl.ast.QualifiedName name) {
      super(node);
      
      this.name = name;
    }
  
    @Override
    public boolean isEdit() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandEdit(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Edit)) {
        return false;
      }        
      Edit tmp = (Edit) o;
      return true && tmp.name.equals(this.name) ; 
    }
   
    @Override
    public int hashCode() {
      return 647 + 499 * name.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }	
  }
  public boolean isHelp() {
    return false;
  }

  static public class Help extends ShellCommand {
    // Production: sig("Help",[])
  
    
  
    public Help(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isHelp() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandHelp(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Help)) {
        return false;
      }        
      Help tmp = (Help) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 619 ; 
    } 
  
    	
  }
  public boolean isHistory() {
    return false;
  }

  static public class History extends ShellCommand {
    // Production: sig("History",[])
  
    
  
    public History(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isHistory() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandHistory(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof History)) {
        return false;
      }        
      History tmp = (History) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 107 ; 
    } 
  
    	
  }
  public boolean isListDeclarations() {
    return false;
  }

  static public class ListDeclarations extends ShellCommand {
    // Production: sig("ListDeclarations",[])
  
    
  
    public ListDeclarations(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isListDeclarations() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandListDeclarations(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof ListDeclarations)) {
        return false;
      }        
      ListDeclarations tmp = (ListDeclarations) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 653 ; 
    } 
  
    	
  }
  public boolean isListModules() {
    return false;
  }

  static public class ListModules extends ShellCommand {
    // Production: sig("ListModules",[])
  
    
  
    public ListModules(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isListModules() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandListModules(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof ListModules)) {
        return false;
      }        
      ListModules tmp = (ListModules) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 631 ; 
    } 
  
    	
  }
  public boolean isQuit() {
    return false;
  }

  static public class Quit extends ShellCommand {
    // Production: sig("Quit",[])
  
    
  
    public Quit(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isQuit() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandQuit(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Quit)) {
        return false;
      }        
      Quit tmp = (Quit) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 191 ; 
    } 
  
    	
  }
  public boolean isSetOption() {
    return false;
  }

  static public class SetOption extends ShellCommand {
    // Production: sig("SetOption",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.Expression","expression")])
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.Expression expression;
  
    public SetOption(IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.Expression expression) {
      super(node);
      
      this.name = name;
      this.expression = expression;
    }
  
    @Override
    public boolean isSetOption() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandSetOption(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof SetOption)) {
        return false;
      }        
      SetOption tmp = (SetOption) o;
      return true && tmp.name.equals(this.name) && tmp.expression.equals(this.expression) ; 
    }
   
    @Override
    public int hashCode() {
      return 947 + 3 * name.hashCode() + 59 * expression.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
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
  }
  public boolean isTest() {
    return false;
  }

  static public class Test extends ShellCommand {
    // Production: sig("Test",[])
  
    
  
    public Test(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isTest() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandTest(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Test)) {
        return false;
      }        
      Test tmp = (Test) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 683 ; 
    } 
  
    	
  }
  public boolean isUndeclare() {
    return false;
  }

  static public class Undeclare extends ShellCommand {
    // Production: sig("Undeclare",[arg("org.rascalmpl.ast.QualifiedName","name")])
  
    
    private final org.rascalmpl.ast.QualifiedName name;
  
    public Undeclare(IConstructor node , org.rascalmpl.ast.QualifiedName name) {
      super(node);
      
      this.name = name;
    }
  
    @Override
    public boolean isUndeclare() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandUndeclare(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Undeclare)) {
        return false;
      }        
      Undeclare tmp = (Undeclare) o;
      return true && tmp.name.equals(this.name) ; 
    }
   
    @Override
    public int hashCode() {
      return 281 + 17 * name.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }	
  }
  public boolean isUnimport() {
    return false;
  }

  static public class Unimport extends ShellCommand {
    // Production: sig("Unimport",[arg("org.rascalmpl.ast.QualifiedName","name")])
  
    
    private final org.rascalmpl.ast.QualifiedName name;
  
    public Unimport(IConstructor node , org.rascalmpl.ast.QualifiedName name) {
      super(node);
      
      this.name = name;
    }
  
    @Override
    public boolean isUnimport() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandUnimport(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Unimport)) {
        return false;
      }        
      Unimport tmp = (Unimport) o;
      return true && tmp.name.equals(this.name) ; 
    }
   
    @Override
    public int hashCode() {
      return 149 + 593 * name.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }	
  }
}