/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.values.uptr;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * UPTR stands for Universal Parse Node Representation (formerly known as AsFix). It is
 * an abstract syntax for SDF productions, completed with constructors for parse forests.
 * <p>
 * UPTR is produced by the SGLR parser, by the ASF+SDF interpreter and by compiled ASF+SDF
 * programs. UPTR is consumed by tools that manipulate parse trees in general (such as
 * automatic syntax high-lighters) or tools that manipulate specific parse trees (such
 * as the Rascal interpreter).
 * 
 */
public class Factory {
	public final static TypeStore uptr = new TypeStore(
			org.rascalmpl.values.errors.Factory.getStore(), 
			org.rascalmpl.values.locations.Factory.getStore());
	private final static TypeFactory tf = TypeFactory.getInstance();
	private static final Type str = tf.stringType();
	
	public static final Type TypeParam = tf.parameterType("T");
	public static final Type Type = new ReifiedType(TypeParam);
	
	static {
		uptr.declareAbstractDataType(Type);
	}

	public static final Type Tree = tf.abstractDataType(uptr, "Tree");
	public static final Type Production = tf.abstractDataType(uptr, "Production");
	public static final Type Attributes = tf.abstractDataType(uptr, "Attributes");
	public static final Type Attr = tf.abstractDataType(uptr, "Attr");
	public static final Type Associativity = tf.abstractDataType(uptr, "Associativity");
	public static final Type Symbol = tf.abstractDataType(uptr, "Symbol");
	public static final Type CharRange = tf.abstractDataType(uptr, "CharRange");
	public static final Type Args = tf.listType(Tree);
	public static final Type Attrs = tf.setType(Attr);
	public static final Type Symbols = tf.listType(Symbol);
	public static final Type CharRanges = tf.listType(CharRange);
	public static final Type Alternatives = tf.setType(Tree);

	public static final Type Type_Reified = tf.constructor(uptr, Type, "type", Symbol, "symbol", tf.mapType(Symbol , Production), "definitions");
					
	public static final Type Tree_Appl = tf.constructor(uptr, Tree, "appl", Production, "prod", tf.listType(Tree), "args");
	public static final Type Tree_Cycle = tf.constructor(uptr, Tree, "cycle", Symbol, "symbol", tf.integerType(), "cycleLength");
	public static final Type Tree_Amb = tf.constructor(uptr, Tree, "amb", Alternatives, "alternatives");
	public static final Type Tree_Char = tf.constructor(uptr, Tree, "char", tf.integerType(), "character");
	
	public static final Type Production_Default = tf.constructor(uptr, Production, "prod", Symbol, "def", tf.listType(Symbol), "symbols",  tf.setType(Attr), "attributes");
	public static final Type Production_Regular = tf.constructor(uptr, Production, "regular", Symbol, "def");
	public static final Type Production_Error = tf.constructor(uptr, Production, "error", Production, "prod", tf.integerType(), "dot");
	public static final Type Production_Skipped = tf.constructor(uptr, Production, "skipped");
	public static final Type Production_Cons = tf.constructor(uptr, Production, "cons", Symbol, "def", tf.listType(Symbol), "symbols",  tf.setType(Attr), "attributes");
	public static final Type Production_Func = tf.constructor(uptr, Production, "func", Symbol, "def", tf.listType(Symbol), "symbols",  tf.setType(Attr), "attributes");
	public static final Type Production_Choice = tf.constructor(uptr, Production, "choice", Symbol, "def", tf.setType(Production), "alternatives");
	public static final Type Production_Priority = tf.constructor(uptr, Production, "priority", Symbol, "def", tf.listType(Production), "choices");
	public static final Type Production_Associativity = tf.constructor(uptr, Production, "associativity", Symbol, "def", Associativity, "assoc", tf.setType(Production), "alternatives");
	

	public static final Type Attr_Assoc = tf.constructor(uptr, Attr, "assoc", Associativity, "assoc");
	public static final Type Attr_Tag = tf.constructor(uptr, Attr, "tag", tf.valueType(), "tag");
	public static final Type Attr_Bracket = tf.constructor(uptr, Attr, "bracket");
	
	public static final Type Associativity_Left = tf.constructor(uptr, Associativity, "left");
	public static final Type Associativity_Right = tf.constructor(uptr, Associativity, "right");
	public static final Type Associativity_Assoc = tf.constructor(uptr, Associativity, "assoc");
	public static final Type Associativity_NonAssoc = tf.constructor(uptr, Associativity, "non-assoc");
	
	public static final Type Condition = tf.abstractDataType(uptr, "Condition");
	public static final Type Condition_Follow = tf.constructor(uptr, Condition, "follow", Symbol, "symbol");
	public static final Type Condition_NotFollow = tf.constructor(uptr, Condition, "not-follow", Symbol, "symbol");
	public static final Type Condition_Precede = tf.constructor(uptr, Condition, "precede", Symbol, "symbol");
	public static final Type Condition_NotPrecede = tf.constructor(uptr, Condition, "not-precede", Symbol, "symbol");
	public static final Type Condition_Delete = tf.constructor(uptr, Condition, "delete", Symbol, "symbol");
	public static final Type Condition_EndOfLine = tf.constructor(uptr, Condition, "end-of-line");
	public static final Type Condition_StartOfLine = tf.constructor(uptr, Condition, "begin-of-line");
	public static final Type Condition_AtColumn = tf.constructor(uptr, Condition, "at-column", tf.integerType(), "column");
	public static final Type Condition_Except = tf.constructor(uptr, Condition, "except", tf.stringType(), "label");
	
	public static final Type Symbol_Label = tf.constructor(uptr, Symbol, "label", str, "name", Symbol, "symbol");
	public static final Type Symbol_Start_Sort = tf.constructor(uptr, Symbol, "start", Symbol, "start");
	public static final Type Symbol_START = tf.constructor(uptr, Symbol, "START");
	public static final Type Symbol_Lit = tf.constructor(uptr, Symbol, "lit", str, "string");
	public static final Type Symbol_CiLit = tf.constructor(uptr, Symbol, "cilit", str, "string");
	public static final Type Symbol_Empty = tf.constructor(uptr, Symbol, "empty");
	public static final Type Symbol_Seq = tf.constructor(uptr, Symbol, "seq", tf.listType(Symbol), "symbols");
	public static final Type Symbol_Opt = tf.constructor(uptr, Symbol, "opt", Symbol, "symbol");
	public static final Type Symbol_Alt = tf.constructor(uptr, Symbol, "alt", tf.setType(Symbol), "alternatives");
	public static final Type Symbol_Sort = tf.constructor(uptr, Symbol, "sort", str, "name");
	public static final Type Symbol_Lex = tf.constructor(uptr, Symbol, "lex", str, "name");
	public static final Type Symbol_Keyword = tf.constructor(uptr, Symbol, "keywords", str, "name");
	public static final Type Symbol_Meta = tf.constructor(uptr, Symbol, "meta", Symbol, "symbol");
	public static final Type Symbol_Conditional = tf.constructor(uptr, Symbol, "conditional", Symbol, "symbol", tf.setType(Condition), "conditions");
	public static final Type Symbol_IterSepX = tf.constructor(uptr, Symbol, "iter-seps", Symbol, "symbol", tf.listType(Symbol), "separators");
	public static final Type Symbol_IterStarSepX = tf.constructor(uptr, Symbol, "iter-star-seps", Symbol, "symbol", tf.listType(Symbol), "separators");
	public static final Type Symbol_IterPlus = tf.constructor(uptr, Symbol, "iter", Symbol, "symbol");
	public static final Type Symbol_IterStar = tf.constructor(uptr, Symbol, "iter-star", Symbol, "symbol");
	public static final Type Symbol_ParameterizedSort = tf.constructor(uptr, Symbol, "parameterized-sort", str, "name", tf.listType(Symbol), "parameters");
	public static final Type Symbol_Parameter = tf.constructor(uptr, Symbol, "parameter", str, "name");
	public static final Type Symbol_LayoutX = tf.constructor(uptr, Symbol, "layouts", str, "name");
	
	public static final Type Symbol_CharClass = tf.constructor(uptr, Symbol, "char-class", tf.listType(CharRange), "ranges");
	
	public static final Type Symbol_Int = tf.constructor(uptr, Symbol, "int");
	public static final Type Symbol_Rat = tf.constructor(uptr, Symbol, "rat");
	public static final Type Symbol_Bool = tf.constructor(uptr, Symbol, "bool");
	public static final Type Symbol_Real = tf.constructor(uptr, Symbol, "real");
	public static final Type Symbol_Str = tf.constructor(uptr, Symbol,  "str");
	public static final Type Symbol_Node = tf.constructor(uptr, Symbol,  "node");
	public static final Type Symbol_Num = tf.constructor(uptr, Symbol,  "num");
	public static final Type Symbol_Void = tf.constructor(uptr, Symbol, "void");
	public static final Type Symbol_Value = tf.constructor(uptr, Symbol,  "value");
	public static final Type Symbol_Loc = tf.constructor(uptr, Symbol,  "loc");
	public static final Type Symbol_Datetime = tf.constructor(uptr, Symbol,  "datetime");
	public static final Type Symbol_Set = tf.constructor(uptr, Symbol, "set", Symbol, "symbol");
	public static final Type Symbol_Rel = tf.constructor(uptr, Symbol, "rel", tf.listType(Symbol), "symbols");
	public static final Type Symbol_Tuple = tf.constructor(uptr, Symbol, "tuple", tf.listType(Symbol), "symbols");
	public static final Type Symbol_List = tf.constructor(uptr, Symbol, "list", Symbol, "symbol");
	public static final Type Symbol_Map = tf.constructor(uptr, Symbol, "map", Symbol, "from", Symbol, "to");
	public static final Type Symbol_Bag = tf.constructor(uptr, Symbol, "bag", Symbol, "symbol");
	public static final Type Symbol_Adt = tf.constructor(uptr, Symbol, "adt", str, "name", tf.listType(Symbol), "parameters");
	public static final Type Symbol_ReifiedType = tf.constructor(uptr, Symbol, "reified", Symbol, "symbol");
	public static final Type Symbol_Func = tf.constructor(uptr, Symbol, "func", Symbol, "ret", tf.listType(Symbol), "parameters");
	public static final Type Symbol_Alias = tf.constructor(uptr, Symbol, "alias", str, "name", tf.listType(Symbol), "parameters", Symbol, "aliased");
	public static final Type Symbol_Cons = tf.constructor(uptr, Symbol, "cons", Symbol, "adt", str, "name", tf.listType(Symbol), "parameters");
	public static final Type Symbol_BoundParameter = tf.constructor(uptr, Symbol, "parameter", str , "name", Symbol, "bound");

	public static final Type CharRange_Single = tf.constructor(uptr, CharRange, "single", tf.integerType(), "begin");
	public static final Type CharRange_Range = tf.constructor(uptr, CharRange, "range", tf.integerType(), "begin", tf.integerType(), "end");
	
	public static final String Location = "loc";
	public static final String Length = "len";

	private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
	public static final IValue Attribute_Assoc_Left = Attr_Assoc.make(vf, Associativity_Left.make(vf));
	public static final IValue Attribute_Assoc_Right = Attr_Assoc.make(vf, Associativity_Right.make(vf));
	public static final IValue Attribute_Assoc_Non_Assoc = Attr_Assoc.make(vf, Associativity_NonAssoc.make(vf));
	public static final IValue Attribute_Assoc_Assoc =  Attr_Assoc.make(vf, Associativity_Assoc.make(vf));
	public static final IValue Attribute_Bracket = Attr_Bracket.make(vf);
	
	
	private static final class InstanceHolder {
		public final static Factory factory = new Factory();
	}
	  
	public static Factory getInstance() {
		return InstanceHolder.factory;
	}
	
	private Factory() {
		uptr.declareAnnotation(Tree, Location, tf.sourceLocationType());
		uptr.declareAnnotation(Tree, Length, tf.integerType());
	}
	
	public static TypeStore getStore() {
		return uptr;
	}
}
