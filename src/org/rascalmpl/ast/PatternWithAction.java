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
public abstract class PatternWithAction extends AbstractAST {
  public PatternWithAction(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasPattern() {
    return false;
  }

  public org.rascalmpl.ast.Expression getPattern() {
    throw new UnsupportedOperationException();
  }
  public boolean hasReplacement() {
    return false;
  }

  public org.rascalmpl.ast.Replacement getReplacement() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStatement() {
    return false;
  }

  public org.rascalmpl.ast.Statement getStatement() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isArbitrary() {
    return false;
  }

  static public class Arbitrary extends PatternWithAction {
    // Production: sig("Arbitrary",[arg("org.rascalmpl.ast.Expression","pattern"),arg("org.rascalmpl.ast.Statement","statement")],breakable=false)
  
    
    private final org.rascalmpl.ast.Expression pattern;
    private final org.rascalmpl.ast.Statement statement;
  
    public Arbitrary(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Statement statement) {
      super(src, node);
      
      this.pattern = pattern;
      this.statement = statement;
    }
  
    @Override
    public boolean isArbitrary() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitPatternWithActionArbitrary(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = pattern.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        pattern.addForLineNumber($line, $result);
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
      if (!(o instanceof Arbitrary)) {
        return false;
      }        
      Arbitrary tmp = (Arbitrary) o;
      return true && tmp.pattern.equals(this.pattern) && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 257 + 941 * pattern.hashCode() + 601 * statement.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getPattern() {
      return this.pattern;
    }
  
    @Override
    public boolean hasPattern() {
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
      return newInstance(getClass(), src, (IConstructor) null , clone(pattern), clone(statement));
    }
            
  }
  public boolean isReplacing() {
    return false;
  }

  static public class Replacing extends PatternWithAction {
    // Production: sig("Replacing",[arg("org.rascalmpl.ast.Expression","pattern"),arg("org.rascalmpl.ast.Replacement","replacement")],breakable=false)
  
    
    private final org.rascalmpl.ast.Expression pattern;
    private final org.rascalmpl.ast.Replacement replacement;
  
    public Replacing(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Replacement replacement) {
      super(src, node);
      
      this.pattern = pattern;
      this.replacement = replacement;
    }
  
    @Override
    public boolean isReplacing() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitPatternWithActionReplacing(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = pattern.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        pattern.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = replacement.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        replacement.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Replacing)) {
        return false;
      }        
      Replacing tmp = (Replacing) o;
      return true && tmp.pattern.equals(this.pattern) && tmp.replacement.equals(this.replacement) ; 
    }
   
    @Override
    public int hashCode() {
      return 239 + 241 * pattern.hashCode() + 673 * replacement.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getPattern() {
      return this.pattern;
    }
  
    @Override
    public boolean hasPattern() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Replacement getReplacement() {
      return this.replacement;
    }
  
    @Override
    public boolean hasReplacement() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(pattern), clone(replacement));
    }
            
  }
}