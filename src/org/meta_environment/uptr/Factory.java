package org.meta_environment.uptr;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.io.ATermReader;

/**
 * UPTR stands for Universal Parse Tree Representation (formerly known as AsFix). It is
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

	public static final Type ParseTree = tf.namedTreeType("ParseTree");
	public static final Type Tree = tf.namedTreeType("Tree");
	public static final Type Production = tf.namedTreeType("Production");
	public static final Type Attributes = tf.namedTreeType("Attributes");
	public static final Type Attr = tf.namedTreeType("Attr");
	public static final Type Associativity = tf.namedTreeType("Associativity");
	public static final Type Symbol = tf.namedTreeType("Symbol");
	public static final Type CharRange = tf.namedTreeType("CharRange");
	public static final Type Constructor = tf.namedTreeType("Constructor");
	public static final Type Args = tf.namedType("Args", tf.listType(Tree));
	public static final Type Attrs = tf.namedType("Attrs", tf.listType(Attr));
	public static final Type Symbols = tf.namedType("Symbols", tf.listType(Symbol));
	public static final Type CharRanges = tf.namedType("CharRanges", tf.listType(CharRange));
	public static final Type Alternatives = tf.namedType("Alternatives", tf.setType(Tree));
	
	public static final Type ParseTree_Top = tf.treeNodeType(ParseTree,"parsetree", Tree, "top", tf.integerType(), "amb-cnt");
	public static final Type ParseTree_Summary = tf.treeNodeType(ParseTree, "summary", tf.stringType(), "producer", tf.stringType(), "id", tf.listType(org.meta_environment.rascal.errors.Factory.Error), "errors");
	
	public static final Type Constructor_Name = tf.treeNodeType(Constructor, "cons", tf.stringType(), "name");
	public static final Type Constructor_Category = tf.treeNodeType(Constructor, "category", tf.stringType(), "name");
	
	public static final Type Tree_Appl = tf.treeNodeType(Tree, "appl", Production, "prod", Args, "args");
	public static final Type Tree_Cycle = tf.treeNodeType(Tree, "cycle", Symbol, "symbol", tf.integerType(), "cycle-length");
	public static final Type Tree_Amb = tf.treeNodeType(Tree, "amb", Alternatives, "alternatives");
	public static final Type Tree_Char = tf.anonymousTreeType(Tree, "char", tf.integerType(), "character");
	
	public static final Type Production_Default = tf.treeNodeType(Production, "prod", Symbols, "lhs", Symbol, "rhs", Attributes, "attributes");
	public static final Type Production_List = tf.treeNodeType(Production, "list", Symbol, "rhs");
	
	public static final Type Attributes_NoAttrs = tf.treeNodeType(Attributes, "no-attrs");
	public static final Type Attributes_Attrs = tf.treeNodeType(Attributes, "attrs", Attrs, "attrs");
	
	public static final Type Attr_Assoc = tf.treeNodeType(Attr, "assoc", Associativity, "assoc");
	public static final Type Attr_Term = tf.treeNodeType(Attr, "term", Constructor, "value");
	public static final Type Attr_Id = tf.treeNodeType(Attr, "id", tf.stringType(), "module-name");
	public static final Type Attr_Bracket = tf.treeNodeType(Attr, "bracket");
	public static final Type Attr_Reject = tf.treeNodeType(Attr, "reject");
	public static final Type Attr_Prefer = tf.treeNodeType(Attr, "prefer");
	public static final Type Attr_Avoid = tf.treeNodeType(Attr, "avoid");
	
	public static final Type Associativity_Left = tf.treeNodeType(Associativity, "left");
	public static final Type Associativity_Right = tf.treeNodeType(Associativity, "right");
	public static final Type Associativity_Assoc = tf.treeNodeType(Associativity, "assoc");
	public static final Type Associativity_NonAssoc = tf.treeNodeType(Associativity, "non-assoc");
	
	public static final Type Symbol_Lit = tf.treeNodeType(Symbol, "lit", tf.stringType(), "string");
	public static final Type Symbol_CiLit = tf.treeNodeType(Symbol, "cilit", tf.stringType(), "string");
	public static final Type Symbol_Cf = tf.treeNodeType(Symbol, "cf", Symbol, "symbol");
	public static final Type Symbol_Lex = tf.treeNodeType(Symbol, "lex", Symbol, "symbol");
	public static final Type Symbol_Empty = tf.treeNodeType(Symbol, "empty");
	public static final Type Symbol_Seq = tf.treeNodeType(Symbol, "seq", Symbols, "symbols");
	public static final Type Symbol_Opt = tf.treeNodeType(Symbol, "opt", Symbol, "symbol");
	public static final Type Symbol_Alt = tf.treeNodeType(Symbol, "alt", Symbol, "lhs", Symbol, "rhs");
	public static final Type Symbol_Tuple = tf.treeNodeType(Symbol, "tuple", Symbol, "head", Symbols, "rest");
	public static final Type Symbol_Sort = tf.treeNodeType(Symbol, "sort", tf.stringType(), "string");
	public static final Type Symbol_IterPlus = tf.treeNodeType(Symbol, "iter", Symbol, "symbol");
	public static final Type Symbol_IterStar = tf.treeNodeType(Symbol, "iter-star", Symbol, "symbol");
	public static final Type Symbol_IterPlusSep = tf.treeNodeType(Symbol, "iter-sep", Symbol, "symbol", Symbol, "separator");
	public static final Type Symbol_IterStarSep = tf.treeNodeType(Symbol, "iter-star-sep", Symbol, "symbol", Symbol, "separator");
	public static final Type Symbol_IterN = tf.treeNodeType(Symbol, "iter-n", Symbol, "symbol", tf.integerType(), "number");
	public static final Type Symbol_IterSepN = tf.treeNodeType(Symbol, "iter-sep-n", Symbol, "symbol", Symbol, "separator", tf.integerType(), "number");
	public static final Type Symbol_Func = tf.treeNodeType(Symbol, "func", Symbols, "symbols", Symbol, "symbol");
	public static final Type Symbol_ParameterizedSort = tf.treeNodeType(Symbol, "parameterized-sort", tf.stringType(), "sort", Symbols, "parameters");
	public static final Type Symbol_Strategy = tf.treeNodeType(Symbol, "strategy", Symbol, "lhs", Symbol, "rhs");
	public static final Type Symbol_VarSym = tf.treeNodeType(Symbol, "lit", tf.stringType(), "string");
	public static final Type Symbol_Layout = tf.treeNodeType(Symbol, "layout");
	public static final Type Symbol_CharClass = tf.treeNodeType(Symbol, "char-class", CharRanges, "ranges");
		
	public static final Type CharRange_Character = tf.anonymousTreeType(CharRange, "character", tf.integerType(), "start");
	public static final Type CharRange_Range = tf.treeNodeType(CharRange, "range", tf.integerType(), "start", tf.integerType(), "end");
	
	private static final class InstanceHolder {
		public final static Factory factory = new Factory();
	}
	  
	public static Factory getInstance() {
		return InstanceHolder.factory;
	}
	
	private Factory() {}
	
	public ITree readParseTree(InputStream stream) throws FactTypeError, IOException {
		ATermReader reader = new ATermReader();
		return (ITree) reader.read(vf, ParseTree, stream);
	}
}
