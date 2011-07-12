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
	
	public static final Type Tree = tf.abstractDataType(uptr, "Tree");
	public static final Type Production = tf.abstractDataType(uptr, "Production");
	public static final Type Attributes = tf.abstractDataType(uptr, "Attributes");
	public static final Type Attr = tf.abstractDataType(uptr, "Attr");
	public static final Type Associativity = tf.abstractDataType(uptr, "Associativity");
	public static final Type Symbol = tf.abstractDataType(uptr, "Symbol");
	public static final Type CharRange = tf.abstractDataType(uptr, "CharRange");
	public static final Type Args = tf.listType(Tree);
	public static final Type Attrs = tf.listType(Attr);
	public static final Type Symbols = tf.listType(Symbol);
	public static final Type CharRanges = tf.listType(CharRange);
	public static final Type Alternatives = tf.setType(Tree);
	
	public static final Type Tree_Appl = tf.constructor(uptr, Tree, "appl", Production, "prod", tf.listType(Tree), "args");
	public static final Type Tree_Cycle = tf.constructor(uptr, Tree, "cycle", Symbol, "symbol", tf.integerType(), "cycleLength");
	public static final Type Tree_Amb = tf.constructor(uptr, Tree, "amb", Alternatives, "alternatives");
	public static final Type Tree_Char = tf.constructor(uptr, Tree, "char", tf.integerType(), "character");
	
	public static final Type Tree_Expected = tf.constructor(uptr, Tree, "expected", Symbol, "symbol", tf.listType(Tree), "rest");
	public static final Type Tree_Error = tf.constructor(uptr, Tree, "error", Production, "prod", tf.listType(Tree), "args", tf.listType(Tree), "rest");
	public static final Type Tree_Error_Amb = tf.constructor(uptr, Tree, "erroramb", Alternatives, "alternatives");
	public static final Type Tree_Error_Cycle = tf.constructor(uptr, Tree, "errorcycle", Symbol, "symbol", tf.integerType(), "cycleLength");
	
	public static final Type Production_Default = tf.constructor(uptr, Production, "prod", Symbol, "def", tf.listType(Symbol), "symbols",  tf.setType(Attr), "attributes");
	public static final Type Production_Regular = tf.constructor(uptr, Production, "regular", Symbol, "def");
	
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
	
	public static final Type Symbol_Label = tf.constructor(uptr, Symbol, "label", tf.stringType(), "name", Symbol, "symbol");
	public static final Type Symbol_Start_Sort = tf.constructor(uptr, Symbol, "start", Symbol, "start");
	public static final Type Symbol_START = tf.constructor(uptr, Symbol, "START");
	public static final Type Symbol_Lit = tf.constructor(uptr, Symbol, "lit", tf.stringType(), "string");
	public static final Type Symbol_CiLit = tf.constructor(uptr, Symbol, "cilit", tf.stringType(), "string");
	public static final Type Symbol_Empty = tf.constructor(uptr, Symbol, "empty");
	public static final Type Symbol_Seq = tf.constructor(uptr, Symbol, "seq", tf.listType(Symbol), "symbols");
	public static final Type Symbol_Opt = tf.constructor(uptr, Symbol, "opt", Symbol, "symbol");
	public static final Type Symbol_Alt = tf.constructor(uptr, Symbol, "alt", tf.setType(Symbol), "alternatives");
	public static final Type Symbol_Tuple = tf.constructor(uptr, Symbol, "tuple", Symbol, "head", tf.listType(Symbol), "rest");
	public static final Type Symbol_Sort = tf.constructor(uptr, Symbol, "sort", tf.stringType(), "string");
	public static final Type Symbol_Lex = tf.constructor(uptr, Symbol, "lex", tf.stringType(), "string");
	public static final Type Symbol_Keyword = tf.constructor(uptr, Symbol, "keywords", tf.stringType(), "string");
	public static final Type Symbol_Meta = tf.constructor(uptr, Symbol, "meta", Symbol, "symbol");
	public static final Type Symbol_Conditional = tf.constructor(uptr, Symbol, "conditional", Symbol, "symbol", tf.setType(Condition), "conditions");
	public static final Type Symbol_IterSepX = tf.constructor(uptr, Symbol, "iter-seps", Symbol, "symbol", tf.listType(Symbol), "separators");
	public static final Type Symbol_IterStarSepX = tf.constructor(uptr, Symbol, "iter-star-seps", Symbol, "symbol", tf.listType(Symbol), "separators");
	public static final Type Symbol_IterPlus = tf.constructor(uptr, Symbol, "iter", Symbol, "symbol");
	public static final Type Symbol_IterStar = tf.constructor(uptr, Symbol, "iter-star", Symbol, "symbol");
	public static final Type Symbol_ParameterizedSort = tf.constructor(uptr, Symbol, "parameterized-sort", tf.stringType(), "sort", tf.listType(Symbol), "parameters");
	public static final Type Symbol_Parameter = tf.constructor(uptr, Symbol, "parameter", tf.stringType(), "name");
	public static final Type Symbol_LayoutX = tf.constructor(uptr, Symbol, "layouts", tf.stringType(), "name");
	
	public static final Type Symbol_CharClass = tf.constructor(uptr, Symbol, "char-class", tf.listType(CharRange), "ranges");
		
	public static final Type CharRange_Single = tf.constructor(uptr, CharRange, "single", tf.integerType(), "start");
	public static final Type CharRange_Range = tf.constructor(uptr, CharRange, "range", tf.integerType(), "start", tf.integerType(), "end");
	
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
