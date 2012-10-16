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

public abstract class BasicType extends AbstractAST {
  public BasicType(IConstructor node) {
    super();
  }

  

  static public class Ambiguity extends BasicType {
    private final java.util.List<org.rascalmpl.ast.BasicType> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.BasicType> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.BasicType> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitBasicTypeAmbiguity(this);
    }
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
  
    	
  }
}