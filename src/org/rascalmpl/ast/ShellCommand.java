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

  static public class Ambiguity extends ShellCommand {
    private final java.util.List<org.rascalmpl.ast.ShellCommand> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.ShellCommand> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.ShellCommand> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitShellCommandAmbiguity(this);
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
    public org.rascalmpl.ast.QualifiedName getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }	
  }
}