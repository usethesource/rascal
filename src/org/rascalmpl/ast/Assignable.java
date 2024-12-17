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
public abstract class Assignable extends AbstractAST {
  public Assignable(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasArguments() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Assignable> getArguments() {
    throw new UnsupportedOperationException();
  }
  public boolean hasElements() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Assignable> getElements() {
    throw new UnsupportedOperationException();
  }
  public boolean hasArg() {
    return false;
  }

  public org.rascalmpl.ast.Assignable getArg() {
    throw new UnsupportedOperationException();
  }
  public boolean hasReceiver() {
    return false;
  }

  public org.rascalmpl.ast.Assignable getReceiver() {
    throw new UnsupportedOperationException();
  }
  public boolean hasDefaultExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getDefaultExpression() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSecond() {
    return false;
  }

  public org.rascalmpl.ast.Expression getSecond() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSubscript() {
    return false;
  }

  public org.rascalmpl.ast.Expression getSubscript() {
    throw new UnsupportedOperationException();
  }
  public boolean hasAnnotation() {
    return false;
  }

  public org.rascalmpl.ast.Name getAnnotation() {
    throw new UnsupportedOperationException();
  }
  public boolean hasField() {
    return false;
  }

  public org.rascalmpl.ast.Name getField() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasOptFirst() {
    return false;
  }

  public org.rascalmpl.ast.OptionalExpression getOptFirst() {
    throw new UnsupportedOperationException();
  }
  public boolean hasOptLast() {
    return false;
  }

  public org.rascalmpl.ast.OptionalExpression getOptLast() {
    throw new UnsupportedOperationException();
  }
  public boolean hasQualifiedName() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getQualifiedName() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isAnnotation() {
    return false;
  }

  static public class Annotation extends Assignable {
    // Production: sig("Annotation",[arg("org.rascalmpl.ast.Assignable","receiver"),arg("org.rascalmpl.ast.Name","annotation")],breakable=false)
  
    
    private final org.rascalmpl.ast.Assignable receiver;
    private final org.rascalmpl.ast.Name annotation;
  
    public Annotation(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.Name annotation) {
      super(src, node);
      
      this.receiver = receiver;
      this.annotation = annotation;
    }
  
    @Override
    public boolean isAnnotation() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignableAnnotation(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = receiver.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        receiver.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = annotation.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        annotation.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Annotation)) {
        return false;
      }        
      Annotation tmp = (Annotation) o;
      return true && tmp.receiver.equals(this.receiver) && tmp.annotation.equals(this.annotation) ; 
    }
   
    @Override
    public int hashCode() {
      return 233 + 383 * receiver.hashCode() + 227 * annotation.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Assignable getReceiver() {
      return this.receiver;
    }
  
    @Override
    public boolean hasReceiver() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getAnnotation() {
      return this.annotation;
    }
  
    @Override
    public boolean hasAnnotation() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(receiver), clone(annotation));
    }
            
  }
  public boolean isBracket() {
    return false;
  }

  static public class Bracket extends Assignable {
    // Production: sig("Bracket",[arg("org.rascalmpl.ast.Assignable","arg")],breakable=false)
  
    
    private final org.rascalmpl.ast.Assignable arg;
  
    public Bracket(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Assignable arg) {
      super(src, node);
      
      this.arg = arg;
    }
  
    @Override
    public boolean isBracket() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignableBracket(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = arg.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        arg.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Bracket)) {
        return false;
      }        
      Bracket tmp = (Bracket) o;
      return true && tmp.arg.equals(this.arg) ; 
    }
   
    @Override
    public int hashCode() {
      return 977 + 797 * arg.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Assignable getArg() {
      return this.arg;
    }
  
    @Override
    public boolean hasArg() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(arg));
    }
            
  }
  public boolean isConstructor() {
    return false;
  }

  static public class Constructor extends Assignable {
    // Production: sig("Constructor",[arg("org.rascalmpl.ast.Name","name"),arg("java.util.List\<org.rascalmpl.ast.Assignable\>","arguments")],breakable=false)
  
    
    private final org.rascalmpl.ast.Name name;
    private final java.util.List<org.rascalmpl.ast.Assignable> arguments;
  
    public Constructor(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Name name,  java.util.List<org.rascalmpl.ast.Assignable> arguments) {
      super(src, node);
      
      this.name = name;
      this.arguments = arguments;
    }
  
    @Override
    public boolean isConstructor() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignableConstructor(this);
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
      
      for (AbstractAST $elem : arguments) {
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
      if (!(o instanceof Constructor)) {
        return false;
      }        
      Constructor tmp = (Constructor) o;
      return true && tmp.name.equals(this.name) && tmp.arguments.equals(this.arguments) ; 
    }
   
    @Override
    public int hashCode() {
      return 601 + 619 * name.hashCode() + 167 * arguments.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Name getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Assignable> getArguments() {
      return this.arguments;
    }
  
    @Override
    public boolean hasArguments() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(name), clone(arguments));
    }
            
  }
  public boolean isFieldAccess() {
    return false;
  }

  static public class FieldAccess extends Assignable {
    // Production: sig("FieldAccess",[arg("org.rascalmpl.ast.Assignable","receiver"),arg("org.rascalmpl.ast.Name","field")],breakable=false)
  
    
    private final org.rascalmpl.ast.Assignable receiver;
    private final org.rascalmpl.ast.Name field;
  
    public FieldAccess(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.Name field) {
      super(src, node);
      
      this.receiver = receiver;
      this.field = field;
    }
  
    @Override
    public boolean isFieldAccess() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignableFieldAccess(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = receiver.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        receiver.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = field.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        field.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof FieldAccess)) {
        return false;
      }        
      FieldAccess tmp = (FieldAccess) o;
      return true && tmp.receiver.equals(this.receiver) && tmp.field.equals(this.field) ; 
    }
   
    @Override
    public int hashCode() {
      return 263 + 557 * receiver.hashCode() + 167 * field.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Assignable getReceiver() {
      return this.receiver;
    }
  
    @Override
    public boolean hasReceiver() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getField() {
      return this.field;
    }
  
    @Override
    public boolean hasField() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(receiver), clone(field));
    }
            
  }
  public boolean isIfDefinedOrDefault() {
    return false;
  }

  static public class IfDefinedOrDefault extends Assignable {
    // Production: sig("IfDefinedOrDefault",[arg("org.rascalmpl.ast.Assignable","receiver"),arg("org.rascalmpl.ast.Expression","defaultExpression")],breakable=false)
  
    
    private final org.rascalmpl.ast.Assignable receiver;
    private final org.rascalmpl.ast.Expression defaultExpression;
  
    public IfDefinedOrDefault(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.Expression defaultExpression) {
      super(src, node);
      
      this.receiver = receiver;
      this.defaultExpression = defaultExpression;
    }
  
    @Override
    public boolean isIfDefinedOrDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignableIfDefinedOrDefault(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = receiver.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        receiver.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = defaultExpression.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        defaultExpression.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof IfDefinedOrDefault)) {
        return false;
      }        
      IfDefinedOrDefault tmp = (IfDefinedOrDefault) o;
      return true && tmp.receiver.equals(this.receiver) && tmp.defaultExpression.equals(this.defaultExpression) ; 
    }
   
    @Override
    public int hashCode() {
      return 937 + 439 * receiver.hashCode() + 439 * defaultExpression.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Assignable getReceiver() {
      return this.receiver;
    }
  
    @Override
    public boolean hasReceiver() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getDefaultExpression() {
      return this.defaultExpression;
    }
  
    @Override
    public boolean hasDefaultExpression() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(receiver), clone(defaultExpression));
    }
            
  }
  public boolean isSlice() {
    return false;
  }

  static public class Slice extends Assignable {
    // Production: sig("Slice",[arg("org.rascalmpl.ast.Assignable","receiver"),arg("org.rascalmpl.ast.OptionalExpression","optFirst"),arg("org.rascalmpl.ast.OptionalExpression","optLast")],breakable=false)
  
    
    private final org.rascalmpl.ast.Assignable receiver;
    private final org.rascalmpl.ast.OptionalExpression optFirst;
    private final org.rascalmpl.ast.OptionalExpression optLast;
  
    public Slice(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.OptionalExpression optFirst,  org.rascalmpl.ast.OptionalExpression optLast) {
      super(src, node);
      
      this.receiver = receiver;
      this.optFirst = optFirst;
      this.optLast = optLast;
    }
  
    @Override
    public boolean isSlice() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignableSlice(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = receiver.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        receiver.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = optFirst.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        optFirst.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = optLast.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        optLast.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Slice)) {
        return false;
      }        
      Slice tmp = (Slice) o;
      return true && tmp.receiver.equals(this.receiver) && tmp.optFirst.equals(this.optFirst) && tmp.optLast.equals(this.optLast) ; 
    }
   
    @Override
    public int hashCode() {
      return 487 + 419 * receiver.hashCode() + 2 * optFirst.hashCode() + 797 * optLast.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Assignable getReceiver() {
      return this.receiver;
    }
  
    @Override
    public boolean hasReceiver() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.OptionalExpression getOptFirst() {
      return this.optFirst;
    }
  
    @Override
    public boolean hasOptFirst() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.OptionalExpression getOptLast() {
      return this.optLast;
    }
  
    @Override
    public boolean hasOptLast() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(receiver), clone(optFirst), clone(optLast));
    }
            
  }
  public boolean isSliceStep() {
    return false;
  }

  static public class SliceStep extends Assignable {
    // Production: sig("SliceStep",[arg("org.rascalmpl.ast.Assignable","receiver"),arg("org.rascalmpl.ast.OptionalExpression","optFirst"),arg("org.rascalmpl.ast.Expression","second"),arg("org.rascalmpl.ast.OptionalExpression","optLast")],breakable=false)
  
    
    private final org.rascalmpl.ast.Assignable receiver;
    private final org.rascalmpl.ast.OptionalExpression optFirst;
    private final org.rascalmpl.ast.Expression second;
    private final org.rascalmpl.ast.OptionalExpression optLast;
  
    public SliceStep(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.OptionalExpression optFirst,  org.rascalmpl.ast.Expression second,  org.rascalmpl.ast.OptionalExpression optLast) {
      super(src, node);
      
      this.receiver = receiver;
      this.optFirst = optFirst;
      this.second = second;
      this.optLast = optLast;
    }
  
    @Override
    public boolean isSliceStep() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignableSliceStep(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = receiver.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        receiver.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = optFirst.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        optFirst.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = second.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        second.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = optLast.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        optLast.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof SliceStep)) {
        return false;
      }        
      SliceStep tmp = (SliceStep) o;
      return true && tmp.receiver.equals(this.receiver) && tmp.optFirst.equals(this.optFirst) && tmp.second.equals(this.second) && tmp.optLast.equals(this.optLast) ; 
    }
   
    @Override
    public int hashCode() {
      return 19 + 317 * receiver.hashCode() + 883 * optFirst.hashCode() + 719 * second.hashCode() + 233 * optLast.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Assignable getReceiver() {
      return this.receiver;
    }
  
    @Override
    public boolean hasReceiver() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.OptionalExpression getOptFirst() {
      return this.optFirst;
    }
  
    @Override
    public boolean hasOptFirst() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getSecond() {
      return this.second;
    }
  
    @Override
    public boolean hasSecond() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.OptionalExpression getOptLast() {
      return this.optLast;
    }
  
    @Override
    public boolean hasOptLast() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(receiver), clone(optFirst), clone(second), clone(optLast));
    }
            
  }
  public boolean isSubscript() {
    return false;
  }

  static public class Subscript extends Assignable {
    // Production: sig("Subscript",[arg("org.rascalmpl.ast.Assignable","receiver"),arg("org.rascalmpl.ast.Expression","subscript")],breakable=false)
  
    
    private final org.rascalmpl.ast.Assignable receiver;
    private final org.rascalmpl.ast.Expression subscript;
  
    public Subscript(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.Expression subscript) {
      super(src, node);
      
      this.receiver = receiver;
      this.subscript = subscript;
    }
  
    @Override
    public boolean isSubscript() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignableSubscript(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = receiver.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        receiver.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = subscript.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        subscript.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Subscript)) {
        return false;
      }        
      Subscript tmp = (Subscript) o;
      return true && tmp.receiver.equals(this.receiver) && tmp.subscript.equals(this.subscript) ; 
    }
   
    @Override
    public int hashCode() {
      return 29 + 151 * receiver.hashCode() + 29 * subscript.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Assignable getReceiver() {
      return this.receiver;
    }
  
    @Override
    public boolean hasReceiver() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getSubscript() {
      return this.subscript;
    }
  
    @Override
    public boolean hasSubscript() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(receiver), clone(subscript));
    }
            
  }
  public boolean isTuple() {
    return false;
  }

  static public class Tuple extends Assignable {
    // Production: sig("Tuple",[arg("java.util.List\<org.rascalmpl.ast.Assignable\>","elements")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Assignable> elements;
  
    public Tuple(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Assignable> elements) {
      super(src, node);
      
      this.elements = elements;
    }
  
    @Override
    public boolean isTuple() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignableTuple(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : elements) {
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
      if (!(o instanceof Tuple)) {
        return false;
      }        
      Tuple tmp = (Tuple) o;
      return true && tmp.elements.equals(this.elements) ; 
    }
   
    @Override
    public int hashCode() {
      return 353 + 31 * elements.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Assignable> getElements() {
      return this.elements;
    }
  
    @Override
    public boolean hasElements() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(elements));
    }
            
  }
  public boolean isVariable() {
    return false;
  }

  static public class Variable extends Assignable {
    // Production: sig("Variable",[arg("org.rascalmpl.ast.QualifiedName","qualifiedName")],breakable=false)
  
    
    private final org.rascalmpl.ast.QualifiedName qualifiedName;
  
    public Variable(ISourceLocation src, IConstructor node , org.rascalmpl.ast.QualifiedName qualifiedName) {
      super(src, node);
      
      this.qualifiedName = qualifiedName;
    }
  
    @Override
    public boolean isVariable() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignableVariable(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = qualifiedName.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        qualifiedName.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Variable)) {
        return false;
      }        
      Variable tmp = (Variable) o;
      return true && tmp.qualifiedName.equals(this.qualifiedName) ; 
    }
   
    @Override
    public int hashCode() {
      return 337 + 257 * qualifiedName.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getQualifiedName() {
      return this.qualifiedName;
    }
  
    @Override
    public boolean hasQualifiedName() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(qualifiedName));
    }
            
  }
}