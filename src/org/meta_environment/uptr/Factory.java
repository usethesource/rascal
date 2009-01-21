package org.meta_environment.uptr;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

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
	private static TypeFactory tf = TypeFactory.getInstance();
	private static IValueFactory vf = ValueFactory.getInstance();

	public static final Type ParseTree = tf.abstractDataType("ParseTree");
	public static final Type Tree = tf.abstractDataType("Node");
	public static final Type Production = tf.abstractDataType("Production");
	public static final Type Attributes = tf.abstractDataType("Attributes");
	public static final Type Attr = tf.abstractDataType("Attr");
	public static final Type Associativity = tf.abstractDataType("Associativity");
	public static final Type Symbol = tf.abstractDataType("Symbol");
	public static final Type CharRange = tf.abstractDataType("CharRange");
	public static final Type Constructor = tf.abstractDataType("Constructor");
	public static final Type Args = tf.aliasType("Args", tf.listType(Tree));
	public static final Type Attrs = tf.aliasType("Attrs", tf.listType(Attr));
	public static final Type Symbols = tf.aliasType("Symbols", tf.listType(Symbol));
	public static final Type CharRanges = tf.aliasType("CharRanges", tf.listType(CharRange));
	public static final Type Alternatives = tf.aliasType("Alternatives", tf.setType(Tree));
	
	public static final Type ParseTree_Top = tf.constructor(ParseTree,"parsetree", Tree, "top", tf.integerType(), "amb-cnt");
	public static final Type ParseTree_Summary = tf.constructor(ParseTree, "summary", tf.stringType(), "producer", tf.stringType(), "id", tf.listType(org.meta_environment.rascal.errors.Factory.Error), "errors");
	
	public static final Type Constructor_Name = tf.constructor(Constructor, "cons", tf.stringType(), "name");
	public static final Type Constructor_Category = tf.constructor(Constructor, "category", tf.stringType(), "name");
	
	public static final Type Tree_Appl = tf.constructor(Tree, "appl", Production, "prod", Args, "args");
	public static final Type Tree_Cycle = tf.constructor(Tree, "cycle", Symbol, "symbol", tf.integerType(), "cycle-length");
	public static final Type Tree_Amb = tf.constructor(Tree, "amb", Alternatives, "alternatives");
	public static final Type Tree_Char = tf.define(Tree, tf.integerType(), "character");
	
	public static final Type Production_Default = tf.constructor(Production, "prod", Symbols, "lhs", Symbol, "rhs", Attributes, "attributes");
	public static final Type Production_List = tf.constructor(Production, "list", Symbol, "rhs");
	
	public static final Type Attributes_NoAttrs = tf.constructor(Attributes, "no-attrs");
	public static final Type Attributes_Attrs = tf.constructor(Attributes, "attrs", Attrs, "attrs");
	
	public static final Type Attr_Assoc = tf.constructor(Attr, "assoc", Associativity, "assoc");
	public static final Type Attr_Term = tf.constructor(Attr, "term", Constructor, "value");
	public static final Type Attr_Id = tf.constructor(Attr, "id", tf.stringType(), "module-name");
	public static final Type Attr_Bracket = tf.constructor(Attr, "bracket");
	public static final Type Attr_Reject = tf.constructor(Attr, "reject");
	public static final Type Attr_Prefer = tf.constructor(Attr, "prefer");
	public static final Type Attr_Avoid = tf.constructor(Attr, "avoid");
	
	public static final Type Associativity_Left = tf.constructor(Associativity, "left");
	public static final Type Associativity_Right = tf.constructor(Associativity, "right");
	public static final Type Associativity_Assoc = tf.constructor(Associativity, "assoc");
	public static final Type Associativity_NonAssoc = tf.constructor(Associativity, "non-assoc");
	
	public static final Type Symbol_Lit = tf.constructor(Symbol, "lit", tf.stringType(), "string");
	public static final Type Symbol_CiLit = tf.constructor(Symbol, "cilit", tf.stringType(), "string");
	public static final Type Symbol_Cf = tf.constructor(Symbol, "cf", Symbol, "symbol");
	public static final Type Symbol_Lex = tf.constructor(Symbol, "lex", Symbol, "symbol");
	public static final Type Symbol_Empty = tf.constructor(Symbol, "empty");
	public static final Type Symbol_Seq = tf.constructor(Symbol, "seq", Symbols, "symbols");
	public static final Type Symbol_Opt = tf.constructor(Symbol, "opt", Symbol, "symbol");
	public static final Type Symbol_Alt = tf.constructor(Symbol, "alt", Symbol, "lhs", Symbol, "rhs");
	public static final Type Symbol_Tuple = tf.constructor(Symbol, "tuple", Symbol, "head", Symbols, "rest");
	public static final Type Symbol_Sort = tf.constructor(Symbol, "sort", tf.stringType(), "string");
	public static final Type Symbol_IterPlus = tf.constructor(Symbol, "iter", Symbol, "symbol");
	public static final Type Symbol_IterStar = tf.constructor(Symbol, "iter-star", Symbol, "symbol");
	public static final Type Symbol_IterPlusSep = tf.constructor(Symbol, "iter-sep", Symbol, "symbol", Symbol, "separator");
	public static final Type Symbol_IterStarSep = tf.constructor(Symbol, "iter-star-sep", Symbol, "symbol", Symbol, "separator");
	public static final Type Symbol_IterN = tf.constructor(Symbol, "iter-n", Symbol, "symbol", tf.integerType(), "number");
	public static final Type Symbol_IterSepN = tf.constructor(Symbol, "iter-sep-n", Symbol, "symbol", Symbol, "separator", tf.integerType(), "number");
	public static final Type Symbol_Func = tf.constructor(Symbol, "func", Symbols, "symbols", Symbol, "symbol");
	public static final Type Symbol_ParameterizedSort = tf.constructor(Symbol, "parameterized-sort", tf.stringType(), "sort", Symbols, "parameters");
	public static final Type Symbol_Strategy = tf.constructor(Symbol, "strategy", Symbol, "lhs", Symbol, "rhs");
	public static final Type Symbol_VarSym = tf.constructor(Symbol, "lit", tf.stringType(), "string");
	public static final Type Symbol_Layout = tf.constructor(Symbol, "layout");
	public static final Type Symbol_CharClass = tf.constructor(Symbol, "char-class", CharRanges, "ranges");
		
	public static final Type CharRange_Character = tf.define(CharRange, tf.integerType(), "character");
	public static final Type CharRange_Range = tf.constructor(CharRange, "range", tf.integerType(), "start", tf.integerType(), "end");
	
	private static final class InstanceHolder {
		public final static Factory factory = new Factory();
	}
	  
	public static Factory getInstance() {
		return InstanceHolder.factory;
	}
	
	private Factory() {}
	
	public INode readParseTree(InputStream stream) throws FactTypeError, IOException {
		ATermReader reader = new ATermReader();
		return (INode) reader.read(vf, ParseTree, stream);
	}
}
