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

public abstract class Assignable extends AbstractAST {
  public Assignable(IConstructor node) {
    super();
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
    // Production: sig("Annotation",[arg("org.rascalmpl.ast.Assignable","receiver"),arg("org.rascalmpl.ast.Name","annotation")])
  
    
    private final org.rascalmpl.ast.Assignable receiver;
    private final org.rascalmpl.ast.Name annotation;
  
    public Annotation(IConstructor node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.Name annotation) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Annotation)) {
        return false;
      }        
      Annotation tmp = (Annotation) o;
      return true && tmp.receiver.equals(this.receiver) && tmp.annotation.equals(this.annotation) ; 
    }
   
    @Override
    public int hashCode() {
      return 223 + 173 * receiver.hashCode() + 557 * annotation.hashCode() ; 
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
  }
  public boolean isBracket() {
    return false;
  }

  static public class Bracket extends Assignable {
    // Production: sig("Bracket",[arg("org.rascalmpl.ast.Assignable","arg")])
  
    
    private final org.rascalmpl.ast.Assignable arg;
  
    public Bracket(IConstructor node , org.rascalmpl.ast.Assignable arg) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Bracket)) {
        return false;
      }        
      Bracket tmp = (Bracket) o;
      return true && tmp.arg.equals(this.arg) ; 
    }
   
    @Override
    public int hashCode() {
      return 569 + 947 * arg.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Assignable getArg() {
      return this.arg;
    }
  
    @Override
    public boolean hasArg() {
      return true;
    }	
  }
  public boolean isConstructor() {
    return false;
  }

  static public class Constructor extends Assignable {
    // Production: sig("Constructor",[arg("org.rascalmpl.ast.Name","name"),arg("java.util.List\<org.rascalmpl.ast.Assignable\>","arguments")])
  
    
    private final org.rascalmpl.ast.Name name;
    private final java.util.List<org.rascalmpl.ast.Assignable> arguments;
  
    public Constructor(IConstructor node , org.rascalmpl.ast.Name name,  java.util.List<org.rascalmpl.ast.Assignable> arguments) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Constructor)) {
        return false;
      }        
      Constructor tmp = (Constructor) o;
      return true && tmp.name.equals(this.name) && tmp.arguments.equals(this.arguments) ; 
    }
   
    @Override
    public int hashCode() {
      return 17 + 829 * name.hashCode() + 883 * arguments.hashCode() ; 
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
  }
  public boolean isFieldAccess() {
    return false;
  }

  static public class FieldAccess extends Assignable {
    // Production: sig("FieldAccess",[arg("org.rascalmpl.ast.Assignable","receiver"),arg("org.rascalmpl.ast.Name","field")])
  
    
    private final org.rascalmpl.ast.Assignable receiver;
    private final org.rascalmpl.ast.Name field;
  
    public FieldAccess(IConstructor node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.Name field) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof FieldAccess)) {
        return false;
      }        
      FieldAccess tmp = (FieldAccess) o;
      return true && tmp.receiver.equals(this.receiver) && tmp.field.equals(this.field) ; 
    }
   
    @Override
    public int hashCode() {
      return 797 + 97 * receiver.hashCode() + 727 * field.hashCode() ; 
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
  }
  public boolean isIfDefinedOrDefault() {
    return false;
  }

  static public class IfDefinedOrDefault extends Assignable {
    // Production: sig("IfDefinedOrDefault",[arg("org.rascalmpl.ast.Assignable","receiver"),arg("org.rascalmpl.ast.Expression","defaultExpression")])
  
    
    private final org.rascalmpl.ast.Assignable receiver;
    private final org.rascalmpl.ast.Expression defaultExpression;
  
    public IfDefinedOrDefault(IConstructor node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.Expression defaultExpression) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof IfDefinedOrDefault)) {
        return false;
      }        
      IfDefinedOrDefault tmp = (IfDefinedOrDefault) o;
      return true && tmp.receiver.equals(this.receiver) && tmp.defaultExpression.equals(this.defaultExpression) ; 
    }
   
    @Override
    public int hashCode() {
      return 541 + 331 * receiver.hashCode() + 367 * defaultExpression.hashCode() ; 
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
  }
  public boolean isSlice() {
    return false;
  }

  static public class Slice extends Assignable {
    // Production: sig("Slice",[arg("org.rascalmpl.ast.Assignable","receiver"),arg("org.rascalmpl.ast.OptionalExpression","optFirst"),arg("org.rascalmpl.ast.OptionalExpression","optLast")])
  
    
    private final org.rascalmpl.ast.Assignable receiver;
    private final org.rascalmpl.ast.OptionalExpression optFirst;
    private final org.rascalmpl.ast.OptionalExpression optLast;
  
    public Slice(IConstructor node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.OptionalExpression optFirst,  org.rascalmpl.ast.OptionalExpression optLast) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Slice)) {
        return false;
      }        
      Slice tmp = (Slice) o;
      return true && tmp.receiver.equals(this.receiver) && tmp.optFirst.equals(this.optFirst) && tmp.optLast.equals(this.optLast) ; 
    }
   
    @Override
    public int hashCode() {
      return 571 + 941 * receiver.hashCode() + 863 * optFirst.hashCode() + 401 * optLast.hashCode() ; 
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
  }
  public boolean isSliceStep() {
    return false;
  }

  static public class SliceStep extends Assignable {
    // Production: sig("SliceStep",[arg("org.rascalmpl.ast.Assignable","receiver"),arg("org.rascalmpl.ast.OptionalExpression","optFirst"),arg("org.rascalmpl.ast.Expression","second"),arg("org.rascalmpl.ast.OptionalExpression","optLast")])
  
    
    private final org.rascalmpl.ast.Assignable receiver;
    private final org.rascalmpl.ast.OptionalExpression optFirst;
    private final org.rascalmpl.ast.Expression second;
    private final org.rascalmpl.ast.OptionalExpression optLast;
  
    public SliceStep(IConstructor node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.OptionalExpression optFirst,  org.rascalmpl.ast.Expression second,  org.rascalmpl.ast.OptionalExpression optLast) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof SliceStep)) {
        return false;
      }        
      SliceStep tmp = (SliceStep) o;
      return true && tmp.receiver.equals(this.receiver) && tmp.optFirst.equals(this.optFirst) && tmp.second.equals(this.second) && tmp.optLast.equals(this.optLast) ; 
    }
   
    @Override
    public int hashCode() {
      return 349 + 673 * receiver.hashCode() + 827 * optFirst.hashCode() + 439 * second.hashCode() + 911 * optLast.hashCode() ; 
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
  }
  public boolean isSubscript() {
    return false;
  }

  static public class Subscript extends Assignable {
    // Production: sig("Subscript",[arg("org.rascalmpl.ast.Assignable","receiver"),arg("org.rascalmpl.ast.Expression","subscript")])
  
    
    private final org.rascalmpl.ast.Assignable receiver;
    private final org.rascalmpl.ast.Expression subscript;
  
    public Subscript(IConstructor node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.Expression subscript) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Subscript)) {
        return false;
      }        
      Subscript tmp = (Subscript) o;
      return true && tmp.receiver.equals(this.receiver) && tmp.subscript.equals(this.subscript) ; 
    }
   
    @Override
    public int hashCode() {
      return 797 + 941 * receiver.hashCode() + 307 * subscript.hashCode() ; 
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
  }
  public boolean isTuple() {
    return false;
  }

  static public class Tuple extends Assignable {
    // Production: sig("Tuple",[arg("java.util.List\<org.rascalmpl.ast.Assignable\>","elements")])
  
    
    private final java.util.List<org.rascalmpl.ast.Assignable> elements;
  
    public Tuple(IConstructor node , java.util.List<org.rascalmpl.ast.Assignable> elements) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Tuple)) {
        return false;
      }        
      Tuple tmp = (Tuple) o;
      return true && tmp.elements.equals(this.elements) ; 
    }
   
    @Override
    public int hashCode() {
      return 859 + 997 * elements.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Assignable> getElements() {
      return this.elements;
    }
  
    @Override
    public boolean hasElements() {
      return true;
    }	
  }
  public boolean isVariable() {
    return false;
  }

  static public class Variable extends Assignable {
    // Production: sig("Variable",[arg("org.rascalmpl.ast.QualifiedName","qualifiedName")])
  
    
    private final org.rascalmpl.ast.QualifiedName qualifiedName;
  
    public Variable(IConstructor node , org.rascalmpl.ast.QualifiedName qualifiedName) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Variable)) {
        return false;
      }        
      Variable tmp = (Variable) o;
      return true && tmp.qualifiedName.equals(this.qualifiedName) ; 
    }
   
    @Override
    public int hashCode() {
      return 997 + 479 * qualifiedName.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getQualifiedName() {
      return this.qualifiedName;
    }
  
    @Override
    public boolean hasQualifiedName() {
      return true;
    }	
  }
}