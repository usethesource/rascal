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

public abstract class BasicType extends AbstractAST {
  public BasicType(IConstructor node) {
    super();
  }

  

  

  
  public boolean isBag() {
    return false;
  }

  static public class Bag extends BasicType {
    // Production: sig("Bag",[])
  
    
  
    public Bag(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isBag() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeBag(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Bag)) {
        return false;
      }        
      Bag tmp = (Bag) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 227 ; 
    } 
  
    	
  }
  public boolean isBool() {
    return false;
  }

  static public class Bool extends BasicType {
    // Production: sig("Bool",[])
  
    
  
    public Bool(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isBool() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeBool(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Bool)) {
        return false;
      }        
      Bool tmp = (Bool) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 71 ; 
    } 
  
    	
  }
  public boolean isDateTime() {
    return false;
  }

  static public class DateTime extends BasicType {
    // Production: sig("DateTime",[])
  
    
  
    public DateTime(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isDateTime() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeDateTime(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DateTime)) {
        return false;
      }        
      DateTime tmp = (DateTime) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 937 ; 
    } 
  
    	
  }
  public boolean isInt() {
    return false;
  }

  static public class Int extends BasicType {
    // Production: sig("Int",[])
  
    
  
    public Int(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isInt() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeInt(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Int)) {
        return false;
      }        
      Int tmp = (Int) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 709 ; 
    } 
  
    	
  }
  public boolean isList() {
    return false;
  }

  static public class List extends BasicType {
    // Production: sig("List",[])
  
    
  
    public List(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isList() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeList(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof List)) {
        return false;
      }        
      List tmp = (List) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 911 ; 
    } 
  
    	
  }
  public boolean isListRelation() {
    return false;
  }

  static public class ListRelation extends BasicType {
    // Production: sig("ListRelation",[])
  
    
  
    public ListRelation(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isListRelation() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeListRelation(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof ListRelation)) {
        return false;
      }        
      ListRelation tmp = (ListRelation) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 821 ; 
    } 
  
    	
  }
  public boolean isLoc() {
    return false;
  }

  static public class Loc extends BasicType {
    // Production: sig("Loc",[])
  
    
  
    public Loc(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isLoc() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeLoc(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Loc)) {
        return false;
      }        
      Loc tmp = (Loc) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 373 ; 
    } 
  
    	
  }
  public boolean isMap() {
    return false;
  }

  static public class Map extends BasicType {
    // Production: sig("Map",[])
  
    
  
    public Map(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isMap() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeMap(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Map)) {
        return false;
      }        
      Map tmp = (Map) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 89 ; 
    } 
  
    	
  }
  public boolean isNode() {
    return false;
  }

  static public class Node extends BasicType {
    // Production: sig("Node",[])
  
    
  
    public Node(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isNode() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeNode(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Node)) {
        return false;
      }        
      Node tmp = (Node) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 463 ; 
    } 
  
    	
  }
  public boolean isNum() {
    return false;
  }

  static public class Num extends BasicType {
    // Production: sig("Num",[])
  
    
  
    public Num(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isNum() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeNum(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Num)) {
        return false;
      }        
      Num tmp = (Num) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 691 ; 
    } 
  
    	
  }
  public boolean isRational() {
    return false;
  }

  static public class Rational extends BasicType {
    // Production: sig("Rational",[])
  
    
  
    public Rational(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isRational() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeRational(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Rational)) {
        return false;
      }        
      Rational tmp = (Rational) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 457 ; 
    } 
  
    	
  }
  public boolean isReal() {
    return false;
  }

  static public class Real extends BasicType {
    // Production: sig("Real",[])
  
    
  
    public Real(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isReal() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeReal(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Real)) {
        return false;
      }        
      Real tmp = (Real) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 887 ; 
    } 
  
    	
  }
  public boolean isRelation() {
    return false;
  }

  static public class Relation extends BasicType {
    // Production: sig("Relation",[])
  
    
  
    public Relation(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isRelation() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeRelation(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Relation)) {
        return false;
      }        
      Relation tmp = (Relation) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 863 ; 
    } 
  
    	
  }
  public boolean isSet() {
    return false;
  }

  static public class Set extends BasicType {
    // Production: sig("Set",[])
  
    
  
    public Set(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isSet() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeSet(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Set)) {
        return false;
      }        
      Set tmp = (Set) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 2 ; 
    } 
  
    	
  }
  public boolean isString() {
    return false;
  }

  static public class String extends BasicType {
    // Production: sig("String",[])
  
    
  
    public String(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isString() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeString(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof String)) {
        return false;
      }        
      String tmp = (String) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 61 ; 
    } 
  
    	
  }
  public boolean isTuple() {
    return false;
  }

  static public class Tuple extends BasicType {
    // Production: sig("Tuple",[])
  
    
  
    public Tuple(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isTuple() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeTuple(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Tuple)) {
        return false;
      }        
      Tuple tmp = (Tuple) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 953 ; 
    } 
  
    	
  }
  public boolean isType() {
    return false;
  }

  static public class Type extends BasicType {
    // Production: sig("Type",[])
  
    
  
    public Type(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isType() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeType(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Type)) {
        return false;
      }        
      Type tmp = (Type) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 311 ; 
    } 
  
    	
  }
  public boolean isValue() {
    return false;
  }

  static public class Value extends BasicType {
    // Production: sig("Value",[])
  
    
  
    public Value(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isValue() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeValue(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Value)) {
        return false;
      }        
      Value tmp = (Value) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 853 ; 
    } 
  
    	
  }
  public boolean isVoid() {
    return false;
  }

  static public class Void extends BasicType {
    // Production: sig("Void",[])
  
    
  
    public Void(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isVoid() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBasicTypeVoid(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Void)) {
        return false;
      }        
      Void tmp = (Void) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 311 ; 
    } 
  
    	
  }
}