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
public abstract class ShellCommand extends AbstractAST {
  public ShellCommand(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasOptName() {
    return false;
  }

  public org.rascalmpl.ast.@org.checkerframework.checker.nullness.qual.MonotonicNonNull QualifiedName getOptName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSign() {
    return false;
  }

  public org.rascalmpl.ast.OptionalEqualSign getSign() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTerminator() {
    return false;
  }

  public org.rascalmpl.ast.OptionalTerminator getTerminator() {
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
    // Production: sig("Clear",[arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public Clear(ISourceLocation src, IConstructor node , org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.terminator = terminator;
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Clear)) {
        return false;
      }        
      Clear tmp = (Clear) o;
      return true && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 283 + 727 * terminator.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(terminator));
    }
            
  }
  public boolean isEdit() {
    return false;
  }

  static public class Edit extends ShellCommand {
    // Production: sig("Edit",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public Edit(ISourceLocation src, IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.name = name;
      this.terminator = terminator;
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = name.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        name.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Edit)) {
        return false;
      }        
      Edit tmp = (Edit) o;
      return true && tmp.name.equals(this.name) && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 449 + 983 * name.hashCode() + 523 * terminator.hashCode() ; 
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
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(name), clone(terminator));
    }
            
  }
  public boolean isHelp() {
    return false;
  }

  static public class Help extends ShellCommand {
    // Production: sig("Help",[arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public Help(ISourceLocation src, IConstructor node , org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.terminator = terminator;
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Help)) {
        return false;
      }        
      Help tmp = (Help) o;
      return true && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 271 + 907 * terminator.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(terminator));
    }
            
  }
  public boolean isHistory() {
    return false;
  }

  static public class History extends ShellCommand {
    // Production: sig("History",[arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public History(ISourceLocation src, IConstructor node , org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.terminator = terminator;
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof History)) {
        return false;
      }        
      History tmp = (History) o;
      return true && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 479 + 383 * terminator.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(terminator));
    }
            
  }
  public boolean isListDeclarations() {
    return false;
  }

  static public class ListDeclarations extends ShellCommand {
    // Production: sig("ListDeclarations",[arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public ListDeclarations(ISourceLocation src, IConstructor node , org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.terminator = terminator;
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof ListDeclarations)) {
        return false;
      }        
      ListDeclarations tmp = (ListDeclarations) o;
      return true && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 397 + 929 * terminator.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(terminator));
    }
            
  }
  public boolean isListModules() {
    return false;
  }

  static public class ListModules extends ShellCommand {
    // Production: sig("ListModules",[arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public ListModules(ISourceLocation src, IConstructor node , org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.terminator = terminator;
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof ListModules)) {
        return false;
      }        
      ListModules tmp = (ListModules) o;
      return true && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 997 + 857 * terminator.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(terminator));
    }
            
  }
  public boolean isQuit() {
    return false;
  }

  static public class Quit extends ShellCommand {
    // Production: sig("Quit",[arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public Quit(ISourceLocation src, IConstructor node , org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.terminator = terminator;
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Quit)) {
        return false;
      }        
      Quit tmp = (Quit) o;
      return true && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 127 + 607 * terminator.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(terminator));
    }
            
  }
  public boolean isSetOption() {
    return false;
  }

  static public class SetOption extends ShellCommand {
    // Production: sig("SetOption",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.OptionalEqualSign","sign"),arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.OptionalEqualSign sign;
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public SetOption(ISourceLocation src, IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.OptionalEqualSign sign,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.name = name;
      this.sign = sign;
      this.expression = expression;
      this.terminator = terminator;
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = name.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        name.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = sign.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        sign.addForLineNumber($line, $result);
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
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof SetOption)) {
        return false;
      }        
      SetOption tmp = (SetOption) o;
      return true && tmp.name.equals(this.name) && tmp.sign.equals(this.sign) && tmp.expression.equals(this.expression) && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 631 + 449 * name.hashCode() + 113 * sign.hashCode() + 659 * expression.hashCode() + 113 * terminator.hashCode() ; 
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
    public org.rascalmpl.ast.OptionalEqualSign getSign() {
      return this.sign;
    }
  
    @Override
    public boolean hasSign() {
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
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(name), clone(sign), clone(expression), clone(terminator));
    }
            
  }
  public boolean isSetOptionTrue() {
    return false;
  }

  static public class SetOptionTrue extends ShellCommand {
    // Production: sig("SetOptionTrue",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public SetOptionTrue(ISourceLocation src, IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.name = name;
      this.terminator = terminator;
    }
  
    @Override
    public boolean isSetOptionTrue() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandSetOptionTrue(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = name.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        name.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof SetOptionTrue)) {
        return false;
      }        
      SetOptionTrue tmp = (SetOptionTrue) o;
      return true && tmp.name.equals(this.name) && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 163 + 491 * name.hashCode() + 887 * terminator.hashCode() ; 
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
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(name), clone(terminator));
    }
            
  }
  public boolean isTest() {
    return false;
  }

  static public class Test extends ShellCommand {
    // Production: sig("Test",[arg("org.rascalmpl.ast.@org.checkerframework.checker.nullness.qual.Nullable QualifiedName","optName",isOptional=true),arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.@org.checkerframework.checker.nullness.qual.Nullable QualifiedName optName;
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public Test(ISourceLocation src, IConstructor node , org.rascalmpl.ast.@org.checkerframework.checker.nullness.qual.Nullable QualifiedName optName,  org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.optName = optName;
      this.terminator = terminator;
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l =  (optName == null) ? null : optName.getLocation();
      if ($l != null && $l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        optName.addForLineNumber($line, $result);
      }
      if ($l != null && $l.getBeginLine() > $line) {
        return;
      }
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Test)) {
        return false;
      }        
      Test tmp = (Test) o;
      return true && java.util.Objects.equals(tmp.optName, this.optName) && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 547 + 293 * java.util.Objects.hashCode(optName) + 997 * terminator.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.@org.checkerframework.checker.nullness.qual.MonotonicNonNull QualifiedName getOptName() {
      return this.optName;
    }
  
    @Override @org.checkerframework.checker.nullness.qual.EnsuresNonNullIf(expression="getOptName()", result=true) 
    public boolean hasOptName() {
      return this.optName != null;
    }
    @Override
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(optName), clone(terminator));
    }
            
  }
  public boolean isUndeclare() {
    return false;
  }

  static public class Undeclare extends ShellCommand {
    // Production: sig("Undeclare",[arg("org.rascalmpl.ast.@org.checkerframework.checker.nullness.qual.Nullable QualifiedName","optName",isOptional=true),arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.@org.checkerframework.checker.nullness.qual.Nullable QualifiedName optName;
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public Undeclare(ISourceLocation src, IConstructor node , org.rascalmpl.ast.@org.checkerframework.checker.nullness.qual.Nullable QualifiedName optName,  org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.optName = optName;
      this.terminator = terminator;
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l =  (optName == null) ? null : optName.getLocation();
      if ($l != null && $l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        optName.addForLineNumber($line, $result);
      }
      if ($l != null && $l.getBeginLine() > $line) {
        return;
      }
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Undeclare)) {
        return false;
      }        
      Undeclare tmp = (Undeclare) o;
      return true && java.util.Objects.equals(tmp.optName, this.optName) && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 881 + 313 * java.util.Objects.hashCode(optName) + 127 * terminator.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.@org.checkerframework.checker.nullness.qual.MonotonicNonNull QualifiedName getOptName() {
      return this.optName;
    }
  
    @Override @org.checkerframework.checker.nullness.qual.EnsuresNonNullIf(expression="getOptName()", result=true) 
    public boolean hasOptName() {
      return this.optName != null;
    }
    @Override
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(optName), clone(terminator));
    }
            
  }
  public boolean isUnextend() {
    return false;
  }

  static public class Unextend extends ShellCommand {
    // Production: sig("Unextend",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public Unextend(ISourceLocation src, IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.name = name;
      this.terminator = terminator;
    }
  
    @Override
    public boolean isUnextend() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandUnextend(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = name.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        name.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Unextend)) {
        return false;
      }        
      Unextend tmp = (Unextend) o;
      return true && tmp.name.equals(this.name) && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 823 + 757 * name.hashCode() + 89 * terminator.hashCode() ; 
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
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(name), clone(terminator));
    }
            
  }
  public boolean isUnimport() {
    return false;
  }

  static public class Unimport extends ShellCommand {
    // Production: sig("Unimport",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public Unimport(ISourceLocation src, IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.name = name;
      this.terminator = terminator;
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = name.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        name.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Unimport)) {
        return false;
      }        
      Unimport tmp = (Unimport) o;
      return true && tmp.name.equals(this.name) && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 619 + 401 * name.hashCode() + 463 * terminator.hashCode() ; 
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
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(name), clone(terminator));
    }
            
  }
  public boolean isUnsetOption() {
    return false;
  }

  static public class UnsetOption extends ShellCommand {
    // Production: sig("UnsetOption",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.OptionalTerminator","terminator")],breakable=false)
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.OptionalTerminator terminator;
  
    public UnsetOption(ISourceLocation src, IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.OptionalTerminator terminator) {
      super(src, node);
      
      this.name = name;
      this.terminator = terminator;
    }
  
    @Override
    public boolean isUnsetOption() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitShellCommandUnsetOption(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = name.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        name.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = terminator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        terminator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof UnsetOption)) {
        return false;
      }        
      UnsetOption tmp = (UnsetOption) o;
      return true && tmp.name.equals(this.name) && tmp.terminator.equals(this.terminator) ; 
    }
   
    @Override
    public int hashCode() {
      return 317 + 857 * name.hashCode() + 353 * terminator.hashCode() ; 
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
    public org.rascalmpl.ast.OptionalTerminator getTerminator() {
      return this.terminator;
    }
  
    @Override
    public boolean hasTerminator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(name), clone(terminator));
    }
            
  }
}