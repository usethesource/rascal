package org.meta_environment.uptr;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.NamedType;
import org.eclipse.imp.pdb.facts.type.TreeNodeType;
import org.eclipse.imp.pdb.facts.type.TreeSortType;
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

	public static final TreeSortType ParseTree = tf.treeSortType("ParseTree");
	public static final TreeSortType Tree = tf.treeSortType("Tree");
	public static final TreeSortType Production = tf.treeSortType("Production");
	public static final TreeSortType Attributes = tf.treeSortType("Attributes");
	public static final TreeSortType Attr = tf.treeSortType("Attr");
	public static final TreeSortType Associativity = tf.treeSortType("Associativity");
	public static final TreeSortType Symbol = tf.treeSortType("Symbol");
	public static final TreeSortType CharRange = tf.treeSortType("CharRange");
	public static final TreeSortType Constructor = tf.treeSortType("Constructor");
	public static final NamedType    Args = tf.namedType("Args", tf.listType(Tree));
	public static final NamedType    Attrs = tf.namedType("Attrs", tf.listType(Attr));
	public static final NamedType    Symbols = tf.namedType("Symbols", tf.listType(Symbol));
	public static final NamedType    CharRanges = tf.namedType("CharRanges", tf.listType(CharRange));
	public static final NamedType    Alternatives = tf.namedType("Alternatives", tf.setType(Tree));
	
	public static final TreeNodeType ParseTree_Top = tf.treeNodeType(ParseTree,"parsetree", Tree, "top", tf.integerType(), "amb-cnt");
	
	public static final TreeNodeType Constructor_Name = tf.treeNodeType(Constructor, "cons", tf.stringType(), "name");
	public static final TreeNodeType Constructor_Category = tf.treeNodeType(Constructor, "category", tf.stringType(), "name");
	
	public static final TreeNodeType Tree_Appl = tf.treeNodeType(Tree, "appl", Production, "prod", Args, "args");
	public static final TreeNodeType Tree_Cycle = tf.treeNodeType(Tree, "cycle", Symbol, "symbol", tf.integerType(), "cycle-length");
	public static final TreeNodeType Tree_Amb = tf.treeNodeType(Tree, "amb", Alternatives, "alternatives");
	public static final TreeNodeType Tree_Char = tf.anonymousTreeType(Tree, "char", tf.integerType(), "character");
	
	public static final TreeNodeType Production_Default = tf.treeNodeType(Production, "prod", Symbols, "lhs", Symbol, "rhs", Attributes, "attributes");
	public static final TreeNodeType Production_List = tf.treeNodeType(Production, "list", Symbol, "rhs");
	
	public static final TreeNodeType Attributes_NoAttrs = tf.treeNodeType(Attributes, "no-attrs");
	public static final TreeNodeType Attributes_Attrs = tf.treeNodeType(Attributes, "attrs", Attrs, "attrs");
	
	public static final TreeNodeType Attr_Assoc = tf.treeNodeType(Attr, "assoc", Associativity, "assoc");
	public static final TreeNodeType Attr_Term = tf.treeNodeType(Attr, "term", Constructor, "value");
	public static final TreeNodeType Attr_Id = tf.treeNodeType(Attr, "id", tf.stringType(), "module-name");
	public static final TreeNodeType Attr_Bracket = tf.treeNodeType(Attr, "bracket");
	public static final TreeNodeType Attr_Reject = tf.treeNodeType(Attr, "reject");
	public static final TreeNodeType Attr_Prefer = tf.treeNodeType(Attr, "prefer");
	public static final TreeNodeType Attr_Avoid = tf.treeNodeType(Attr, "avoid");
	
	public static final TreeNodeType Associativity_Left = tf.treeNodeType(Associativity, "left");
	public static final TreeNodeType Associativity_Right = tf.treeNodeType(Associativity, "right");
	public static final TreeNodeType Associativity_Assoc = tf.treeNodeType(Associativity, "assoc");
	public static final TreeNodeType Associativity_NonAssoc = tf.treeNodeType(Associativity, "non-assoc");
	
	public static final TreeNodeType Symbol_Lit = tf.treeNodeType(Symbol, "lit", tf.stringType(), "string");
	public static final TreeNodeType Symbol_CiLit = tf.treeNodeType(Symbol, "cilit", tf.stringType(), "string");
	public static final TreeNodeType Symbol_Cf = tf.treeNodeType(Symbol, "cf", Symbol, "symbol");
	public static final TreeNodeType Symbol_Lex = tf.treeNodeType(Symbol, "lex", Symbol, "symbol");
	public static final TreeNodeType Symbol_Empty = tf.treeNodeType(Symbol, "empty");
	public static final TreeNodeType Symbol_Seq = tf.treeNodeType(Symbol, "seq", Symbols, "symbols");
	public static final TreeNodeType Symbol_Opt = tf.treeNodeType(Symbol, "opt", Symbol, "symbol");
	public static final TreeNodeType Symbol_Alt = tf.treeNodeType(Symbol, "alt", Symbol, "lhs", Symbol, "rhs");
	public static final TreeNodeType Symbol_Tuple = tf.treeNodeType(Symbol, "tuple", Symbol, "head", Symbols, "rest");
	public static final TreeNodeType Symbol_Sort = tf.treeNodeType(Symbol, "sort", tf.stringType(), "string");
	public static final TreeNodeType Symbol_IterPlus = tf.treeNodeType(Symbol, "iter", Symbol, "symbol");
	public static final TreeNodeType Symbol_IterStar = tf.treeNodeType(Symbol, "iter-star", Symbol, "symbol");
	public static final TreeNodeType Symbol_IterPlusSep = tf.treeNodeType(Symbol, "iter-sep", Symbol, "symbol", Symbol, "separator");
	public static final TreeNodeType Symbol_IterStarSep = tf.treeNodeType(Symbol, "iter-star-sep", Symbol, "symbol", Symbol, "separator");
	public static final TreeNodeType Symbol_IterN = tf.treeNodeType(Symbol, "iter-n", Symbol, "symbol", tf.integerType(), "number");
	public static final TreeNodeType Symbol_IterSepN = tf.treeNodeType(Symbol, "iter-sep-n", Symbol, "symbol", Symbol, "separator", tf.integerType(), "number");
	public static final TreeNodeType Symbol_Func = tf.treeNodeType(Symbol, "func", Symbols, "symbols", Symbol, "symbol");
	public static final TreeNodeType Symbol_ParameterizedSort = tf.treeNodeType(Symbol, "parameterized-sort", tf.stringType(), "sort", Symbols, "parameters");
	public static final TreeNodeType Symbol_Strategy = tf.treeNodeType(Symbol, "strategy", Symbol, "lhs", Symbol, "rhs");
	public static final TreeNodeType Symbol_VarSym = tf.treeNodeType(Symbol, "lit", tf.stringType(), "string");
	public static final TreeNodeType Symbol_Layout = tf.treeNodeType(Symbol, "layout");
	public static final TreeNodeType Symbol_CharClass = tf.treeNodeType(Symbol, "char-class", CharRanges, "ranges");
		
	public static final TreeNodeType CharRange_Character = tf.anonymousTreeType(CharRange, "character", tf.integerType(), "start");
	public static final TreeNodeType CharRange_Range = tf.treeNodeType(CharRange, "range", tf.integerType(), "start", tf.integerType(), "end");
	
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
