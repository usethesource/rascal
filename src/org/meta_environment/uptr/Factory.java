package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;

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
	public static TypeStore uptr = new TypeStore(
			org.meta_environment.errors.Factory.getStore(), 
			org.meta_environment.locations.Factory.getStore());
	private static TypeFactory tf = TypeFactory.getInstance();

	public static final Type ParseTree = tf.abstractDataType(uptr, "ParseTree");
	public static final Type Tree = tf.abstractDataType(uptr, "Tree");
	public static final Type Production = tf.abstractDataType(uptr, "Production");
	public static final Type Attributes = tf.abstractDataType(uptr, "Attributes");
	public static final Type Attr = tf.abstractDataType(uptr, "Attr");
	public static final Type Associativity = tf.abstractDataType(uptr, "Associativity");
	public static final Type Symbol = tf.abstractDataType(uptr, "Symbol");
	public static final Type CharRange = tf.abstractDataType(uptr, "CharRange");
	public static final Type Constructor = tf.abstractDataType(uptr, "Constructor");
	public static final Type Args = tf.listType(Tree);
	public static final Type Attrs = tf.listType(Attr);
	public static final Type Symbols = tf.listType(Symbol);
	public static final Type CharRanges = tf.listType(CharRange);
	public static final Type Alternatives = tf.setType(Tree);
	
	public static final Type ParseTree_Top = tf.constructor(uptr, ParseTree,"parsetree", Tree, "top", tf.integerType(), "amb_cnt");
	public static final Type ParseTree_Summary = tf.constructor(uptr, ParseTree, "summary", tf.stringType(), "producer", tf.stringType(), "id", tf.listType(org.meta_environment.errors.Factory.Error), "errors");
	
	public static final Type Constructor_Name = tf.constructor(uptr, Constructor, "cons", tf.stringType(), "name");
	public static final Type Constructor_Category = tf.constructor(uptr, Constructor, "category", tf.stringType(), "name");
	
	public static final Type Tree_Appl = tf.constructor(uptr, Tree, "appl", Production, "prod", tf.listType(Tree), "args");
	public static final Type Tree_Cycle = tf.constructor(uptr, Tree, "cycle", Symbol, "symbol", tf.integerType(), "cycleLength");
	public static final Type Tree_Amb = tf.constructor(uptr, Tree, "amb", Alternatives, "alternatives");
	public static final Type Tree_Char = tf.constructor(uptr, Tree, "char", tf.integerType(), "character");
	
	public static final Type Production_Default = tf.constructor(uptr, Production, "prod", tf.listType(Symbol), "lhs", Symbol, "rhs", Attributes, "attributes");
	public static final Type Production_List = tf.constructor(uptr, Production, "list", Symbol, "rhs");
	
	public static final Type Attributes_NoAttrs = tf.constructor(uptr, Attributes, "no-attrs");
	public static final Type Attributes_Attrs = tf.constructor(uptr, Attributes, "attrs", tf.listType(Attr), "attrs");
	
	public static final Type Attr_Assoc = tf.constructor(uptr, Attr, "assoc", Associativity, "assoc");
	public static final Type Attr_Term = tf.constructor(uptr, Attr, "term", tf.valueType(), "term");
	public static final Type Attr_Id = tf.constructor(uptr, Attr, "id", tf.stringType(), "moduleName");
	public static final Type Attr_Bracket = tf.constructor(uptr, Attr, "bracket");
	public static final Type Attr_Reject = tf.constructor(uptr, Attr, "reject");
	public static final Type Attr_Prefer = tf.constructor(uptr, Attr, "prefer");
	public static final Type Attr_Avoid = tf.constructor(uptr, Attr, "avoid");
	
	public static final Type Associativity_Left = tf.constructor(uptr, Associativity, "left");
	public static final Type Associativity_Right = tf.constructor(uptr, Associativity, "right");
	public static final Type Associativity_Assoc = tf.constructor(uptr, Associativity, "assoc");
	public static final Type Associativity_NonAssoc = tf.constructor(uptr, Associativity, "non-assoc");
	
	public static final Type Symbol_Lit = tf.constructor(uptr, Symbol, "lit", tf.stringType(), "string");
	public static final Type Symbol_CiLit = tf.constructor(uptr, Symbol, "cilit", tf.stringType(), "string");
	public static final Type Symbol_Cf = tf.constructor(uptr, Symbol, "cf", Symbol, "symbol");
	public static final Type Symbol_Lex = tf.constructor(uptr, Symbol, "lex", Symbol, "symbol");
	public static final Type Symbol_Empty = tf.constructor(uptr, Symbol, "empty");
	public static final Type Symbol_Seq = tf.constructor(uptr, Symbol, "seq", tf.listType(Symbol), "symbols");
	public static final Type Symbol_Opt = tf.constructor(uptr, Symbol, "opt", Symbol, "symbol");
	public static final Type Symbol_Alt = tf.constructor(uptr, Symbol, "alt", Symbol, "lhs", Symbol, "rhs");
	public static final Type Symbol_Tuple = tf.constructor(uptr, Symbol, "tuple", Symbol, "head", tf.listType(Symbol), "rest");
	public static final Type Symbol_Sort = tf.constructor(uptr, Symbol, "sort", tf.stringType(), "string");
	public static final Type Symbol_IterPlus = tf.constructor(uptr, Symbol, "iter", Symbol, "symbol");
	public static final Type Symbol_IterStar = tf.constructor(uptr, Symbol, "iter-star", Symbol, "symbol");
	public static final Type Symbol_IterPlusSep = tf.constructor(uptr, Symbol, "iter-sep", Symbol, "symbol", Symbol, "separator");
	public static final Type Symbol_IterStarSep = tf.constructor(uptr, Symbol, "iter-star-sep", Symbol, "symbol", Symbol, "separator");
	public static final Type Symbol_IterN = tf.constructor(uptr, Symbol, "iter-n", Symbol, "symbol", tf.integerType(), "number");
	public static final Type Symbol_IterSepN = tf.constructor(uptr, Symbol, "iter-sep-n", Symbol, "symbol", Symbol, "separator", tf.integerType(), "number");
	public static final Type Symbol_Func = tf.constructor(uptr, Symbol, "func", tf.listType(Symbol), "symbols", Symbol, "symbol");
	public static final Type Symbol_ParameterizedSort = tf.constructor(uptr, Symbol, "parameterized-sort", tf.stringType(), "sort", tf.listType(Symbol), "parameters");
	public static final Type Symbol_Strategy = tf.constructor(uptr, Symbol, "strategy", Symbol, "lhs", Symbol, "rhs");
	public static final Type Symbol_VarSym = tf.constructor(uptr, Symbol, "varsym", tf.stringType(), "string");
	public static final Type Symbol_Layout = tf.constructor(uptr, Symbol, "layout");
	public static final Type Symbol_CharClass = tf.constructor(uptr, Symbol, "char-class", tf.listType(CharRange), "ranges");
		
	public static final Type CharRange_Single = tf.constructor(uptr, CharRange, "single", tf.integerType(), "start");
	public static final Type CharRange_Range = tf.constructor(uptr, CharRange, "range", tf.integerType(), "start", tf.integerType(), "end");
	
	public static final String Location = "loc";
	public static final String Length = "len";
	
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
