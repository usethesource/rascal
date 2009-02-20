package org.meta_environment.uptr;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.ValueFactoryFactory;

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
	private static TypeStore uptr = new TypeStore(
			org.meta_environment.rascal.errors.Factory.getStore(), 
			org.meta_environment.rascal.locations.Factory.getStore());
	private static TypeFactory tf = TypeFactory.getInstance();
	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();

	public static final Type ParseTree = uptr.abstractDataType("ParseTree");
	public static final Type Tree = uptr.abstractDataType("Tree");
	public static final Type Production = uptr.abstractDataType("Production");
	public static final Type Attributes = uptr.abstractDataType("Attributes");
	public static final Type Attr = uptr.abstractDataType("Attr");
	public static final Type Associativity = uptr.abstractDataType("Associativity");
	public static final Type Symbol = uptr.abstractDataType("Symbol");
	public static final Type CharRange = uptr.abstractDataType("CharRange");
	public static final Type Constructor = uptr.abstractDataType("Constructor");
	public static final Type Args = uptr.aliasType("Args", tf.listType(Tree));
	public static final Type Attrs = uptr.aliasType("Attrs", tf.listType(Attr));
	public static final Type Symbols = uptr.aliasType("Symbols", tf.listType(Symbol));
	public static final Type CharRanges = uptr.aliasType("CharRanges", tf.listType(CharRange));
	public static final Type Alternatives = uptr.aliasType("Alternatives", tf.setType(Tree));
	
	public static final Type ParseTree_Top = uptr.constructor(ParseTree,"parsetree", Tree, "top", tf.integerType(), "amb_cnt");
	public static final Type ParseTree_Summary = uptr.constructor(ParseTree, "summary", tf.stringType(), "producer", tf.stringType(), "id", tf.listType(org.meta_environment.rascal.errors.Factory.Error), "errors");
	
	public static final Type Constructor_Name = uptr.constructor(Constructor, "cons", tf.stringType(), "name");
	public static final Type Constructor_Category = uptr.constructor(Constructor, "category", tf.stringType(), "name");
	
	public static final Type Tree_Appl = uptr.constructor(Tree, "appl", Production, "prod", tf.listType(Tree), "args");
	public static final Type Tree_Cycle = uptr.constructor(Tree, "cycle", Symbol, "symbol", tf.integerType(), "cycleLength");
	public static final Type Tree_Amb = uptr.constructor(Tree, "amb", Alternatives, "alternatives");
	public static final Type Tree_Char = uptr.constructor(Tree, "char", tf.integerType(), "character");
	
	public static final Type Production_Default = uptr.constructor(Production, "prod", tf.listType(Symbol), "lhs", Symbol, "rhs", Attributes, "attributes");
	public static final Type Production_List = uptr.constructor(Production, "list", Symbol, "rhs");
	
	public static final Type Attributes_NoAttrs = uptr.constructor(Attributes, "no-attrs");
	public static final Type Attributes_Attrs = uptr.constructor(Attributes, "attrs", tf.listType(Attr), "attrs");
	
	public static final Type Attr_Assoc = uptr.constructor(Attr, "assoc", Associativity, "assoc");
	public static final Type Attr_Term = uptr.constructor(Attr, "term", tf.valueType(), "term");
	public static final Type Attr_Id = uptr.constructor(Attr, "id", tf.stringType(), "moduleName");
	public static final Type Attr_Bracket = uptr.constructor(Attr, "bracket");
	public static final Type Attr_Reject = uptr.constructor(Attr, "reject");
	public static final Type Attr_Prefer = uptr.constructor(Attr, "prefer");
	public static final Type Attr_Avoid = uptr.constructor(Attr, "avoid");
	
	public static final Type Associativity_Left = uptr.constructor(Associativity, "left");
	public static final Type Associativity_Right = uptr.constructor(Associativity, "right");
	public static final Type Associativity_Assoc = uptr.constructor(Associativity, "assoc");
	public static final Type Associativity_NonAssoc = uptr.constructor(Associativity, "non-assoc");
	
	public static final Type Symbol_Lit = uptr.constructor(Symbol, "lit", tf.stringType(), "string");
	public static final Type Symbol_CiLit = uptr.constructor(Symbol, "cilit", tf.stringType(), "string");
	public static final Type Symbol_Cf = uptr.constructor(Symbol, "cf", Symbol, "symbol");
	public static final Type Symbol_Lex = uptr.constructor(Symbol, "lex", Symbol, "symbol");
	public static final Type Symbol_Empty = uptr.constructor(Symbol, "empty");
	public static final Type Symbol_Seq = uptr.constructor(Symbol, "seq", tf.listType(Symbol), "symbols");
	public static final Type Symbol_Opt = uptr.constructor(Symbol, "opt", Symbol, "symbol");
	public static final Type Symbol_Alt = uptr.constructor(Symbol, "alt", Symbol, "lhs", Symbol, "rhs");
	public static final Type Symbol_Tuple = uptr.constructor(Symbol, "tuple", Symbol, "head", tf.listType(Symbol), "rest");
	public static final Type Symbol_Sort = uptr.constructor(Symbol, "sort", tf.stringType(), "string");
	public static final Type Symbol_IterPlus = uptr.constructor(Symbol, "iter", Symbol, "symbol");
	public static final Type Symbol_IterStar = uptr.constructor(Symbol, "iter-star", Symbol, "symbol");
	public static final Type Symbol_IterPlusSep = uptr.constructor(Symbol, "iter-sep", Symbol, "symbol", Symbol, "separator");
	public static final Type Symbol_IterStarSep = uptr.constructor(Symbol, "iter-star-sep", Symbol, "symbol", Symbol, "separator");
	public static final Type Symbol_IterN = uptr.constructor(Symbol, "iter-n", Symbol, "symbol", tf.integerType(), "number");
	public static final Type Symbol_IterSepN = uptr.constructor(Symbol, "iter-sep-n", Symbol, "symbol", Symbol, "separator", tf.integerType(), "number");
	public static final Type Symbol_Func = uptr.constructor(Symbol, "func", tf.listType(Symbol), "symbols", Symbol, "symbol");
	public static final Type Symbol_ParameterizedSort = uptr.constructor(Symbol, "parameterized-sort", tf.stringType(), "sort", tf.listType(Symbol), "parameters");
	public static final Type Symbol_Strategy = uptr.constructor(Symbol, "strategy", Symbol, "lhs", Symbol, "rhs");
	public static final Type Symbol_VarSym = uptr.constructor(Symbol, "lit", tf.stringType(), "string");
	public static final Type Symbol_Layout = uptr.constructor(Symbol, "layout");
	public static final Type Symbol_CharClass = uptr.constructor(Symbol, "char-class", tf.listType(CharRange), "ranges");
		
	public static final Type CharRange_Single = uptr.constructor(CharRange, "single", tf.integerType(), "start");
	public static final Type CharRange_Range = uptr.constructor(CharRange, "range", tf.integerType(), "start", tf.integerType(), "end");
	
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
	
	public INode readParseTree(InputStream stream) throws FactTypeUseException, IOException {
		ATermReader reader = new ATermReader();
		return (INode) reader.read(vf, ParseTree, stream);
	}
}
