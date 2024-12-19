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
public abstract class Range extends AbstractAST {
  public Range(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasCharacter() {
    return false;
  }

  public org.rascalmpl.ast.Char getCharacter() {
    throw new UnsupportedOperationException();
  }
  public boolean hasEnd() {
    return false;
  }

  public org.rascalmpl.ast.Char getEnd() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStart() {
    return false;
  }

  public org.rascalmpl.ast.Char getStart() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isCharacter() {
    return false;
  }

  static public class Character extends Range {
    // Production: sig("Character",[arg("org.rascalmpl.ast.Char","character")],breakable=false)
  
    
    private final org.rascalmpl.ast.Char character;
  
    public Character(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Char character) {
      super(src, node);
      
      this.character = character;
    }
  
    @Override
    public boolean isCharacter() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitRangeCharacter(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = character.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        character.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Character)) {
        return false;
      }        
      Character tmp = (Character) o;
      return true && tmp.character.equals(this.character) ; 
    }
   
    @Override
    public int hashCode() {
      return 499 + 919 * character.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Char getCharacter() {
      return this.character;
    }
  
    @Override
    public boolean hasCharacter() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(character));
    }
            
  }
  public boolean isFromTo() {
    return false;
  }

  static public class FromTo extends Range {
    // Production: sig("FromTo",[arg("org.rascalmpl.ast.Char","start"),arg("org.rascalmpl.ast.Char","end")],breakable=false)
  
    
    private final org.rascalmpl.ast.Char start;
    private final org.rascalmpl.ast.Char end;
  
    public FromTo(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Char start,  org.rascalmpl.ast.Char end) {
      super(src, node);
      
      this.start = start;
      this.end = end;
    }
  
    @Override
    public boolean isFromTo() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitRangeFromTo(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = start.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        start.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = end.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        end.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof FromTo)) {
        return false;
      }        
      FromTo tmp = (FromTo) o;
      return true && tmp.start.equals(this.start) && tmp.end.equals(this.end) ; 
    }
   
    @Override
    public int hashCode() {
      return 233 + 709 * start.hashCode() + 503 * end.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Char getStart() {
      return this.start;
    }
  
    @Override
    public boolean hasStart() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Char getEnd() {
      return this.end;
    }
  
    @Override
    public boolean hasEnd() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(start), clone(end));
    }
            
  }
}