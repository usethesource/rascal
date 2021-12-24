/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl 
*******************************************************************************/
package org.rascalmpl.values;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;
import org.rascalmpl.values.parsetrees.visitors.TreeVisitor;

import io.usethesource.capsule.util.collection.AbstractSpecialisedImmutableMap;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.IRelation;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.exceptions.UndeclaredFieldException;
import io.usethesource.vallang.impl.fields.AbstractDefaultWithKeywordParameters;
import io.usethesource.vallang.impl.fields.AbstractValueFactoryAdapter;
import io.usethesource.vallang.impl.fields.ConstructorWithKeywordParametersFacade;
import io.usethesource.vallang.impl.persistent.ValueFactory;
import io.usethesource.vallang.io.StandardTextReader;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.io.binary.message.IValueReader;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;
import io.usethesource.vallang.visitors.IValueVisitor;

/**
 * The RascalValueFactory extends a given IValueFactory with the Rascal-specific builtin
 * data types it needs to bootstrap and to provide syntax features. This includes
 *    * UPTR - universal parse tree format
 *    * Type - reified types for PDB and Rascal types
 *    * Functions/Closures - TBD; not represented here yet
 *    * Modules - TBD; not represented here yet
 *    * Grammars - Partially here in the definition of Production
 *        
 * UPTR stands for Universal Parse Node Representation (formerly known as AsFix). It is
 * an abstract syntax for Rascal production rules, completed with constructors for parse forests.
 * UPTR is produced by parser implementations (as generated from Rascal grammars for example).
 * UPTR is consumed by tools that manipulate parse trees in general (such as
 * automatic syntax high-lighters) or tools that manipulate specific parse trees (such
 * as the Rascal interpreter).
 * 
 * UPTR parse trees (Tree) are special ADT trees in the sense that:
 *    * their implementation classes are specialized (compressed) implementations of IConstructor
 *      for the sake of speed
 *    * their instances have NonTerminalType instead of AbstractDataType
 *
 * Reified type values (Type) are special in the sense that they satisfy a special contract
 * which binds a reified type representation to the run-time type of the reified type:
 * Namely, for any instance of `Type[&T] = type(Symbol s, map[Symbol,Production] definitions)` 
 * it holds that:
 *    * &T is bound to the {@link Type} represented by Symbol s
 *    * At least all definitions necessary for constructing any value of type &T are
 *      listed in the `definitions` map.
 * Note that a reified type is in fact also a reified type environment.
 * Use {@link TypeReifier} for mapping back and forth between Types and reified types.
 * 
 * For (de)serialization using (for example) {@link StandardTextReader} and {@link StandardTextWriter}
 * the RascalValueFactory is needed as a parameter as well as its {@method getStore()} method to
 * provide deserialization with the right definitions of data-types.
 * 
 * For inspecting manipulating values for the data-types which are defined here please see this API:
 *    * {@link TreeAdapter}
 *    * {@link SymbolAdapter}
 *    * {@link ProductionAdapter}
 *    
 * All definitions included here are echoed in ParseTree.rsc and Type.rsc. The clone is necessary
 * for bootstrapping reasons. This class is where it all starts.
 */
public class RascalValueFactory extends AbstractValueFactoryAdapter implements IRascalValueFactory {
	public final static TypeStore uptr = new TypeStore();
	private final static TypeFactory tf = TypeFactory.getInstance();

	// This is where we bind the persistent implementation of IValueFactory:
	private static final IValueFactory bootFactory = ValueFactory.getInstance();
	private final TypeReifier tr = new TypeReifier(bootFactory);
	
	private static final Type str = tf.stringType();
	
	/* General abstract sort declarations */ 
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
	
	/* Content is a wrapper for strings that represent svg, html, png, etc, for
	 * use in interative visual REPL sessions with Rascal
	 */
	public static final Type Content = tf.abstractDataType(uptr, "Content");

	/* The next three declarations and the static block are order dependend */
	public static final Type TypeParam = tf.parameterType("T");
	public static final Type Type = RascalTypeFactory.getInstance().reifiedType(TypeParam);
	static { uptr.declareAbstractDataType(Type); }
	public static final Type Type_Reified = tf.constructor(uptr, Type, "type", Symbol, "symbol", tf.mapType(Symbol , Production), "definitions");
	public static final Type ADTforType = tf.abstractDataType(uptr, "type", TypeParam); // temporary for de-serializing Type
	
	/* end order dependence */
	
	/* Constructors for Tree */
	public static final Type Tree_Appl = tf.constructor(uptr, Tree, "appl", Production, "prod", tf.listType(Tree), "args");
	public static final Type Tree_Cycle = tf.constructor(uptr, Tree, "cycle", Symbol, "symbol", tf.integerType(), "cycleLength");
	public static final Type Tree_Amb = tf.constructor(uptr, Tree, "amb", Alternatives, "alternatives");
	public static final Type Tree_Char = tf.constructor(uptr, Tree, "char", tf.integerType(), "character");
	
	/* Constructors for Production */
	public static final Type Production_Default = tf.constructor(uptr, Production, "prod", Symbol, "def", tf.listType(Symbol), "symbols",  tf.setType(Attr), "attributes");
	public static final Type Production_Prod = Production_Default;
	public static final Type Production_Regular = tf.constructor(uptr, Production, "regular", Symbol, "def");
	public static final Type Production_Cons = tf.constructor(uptr, Production, "cons", Symbol, "def", tf.listType(Symbol), "symbols", tf.listType(Symbol), "kwTypes", tf.setType(Attr), "attributes");
	public static final Type Production_Func = tf.constructor(uptr, Production, "func", Symbol, "def", tf.listType(Symbol), "symbols", tf.listType(Symbol), "kwTypes", tf.setType(Attr), "attributes");
	public static final Type Production_Choice = tf.constructor(uptr, Production, "choice", Symbol, "def", tf.setType(Production), "alternatives");
	public static final Type Production_Priority = tf.constructor(uptr, Production, "priority", Symbol, "def", tf.listType(Production), "choices");
	public static final Type Production_Composition = tf.constructor(uptr,  Production,  "composition", Production, "lhs", Production, "rhs");
	public static final Type Production_Associativity = tf.constructor(uptr, Production, "associativity", Symbol, "def", Associativity, "assoc", tf.setType(Production), "alternatives");
	
	/* Constructors for Attr */
	public static final Type Attr_Assoc = tf.constructor(uptr, Attr, "assoc", Associativity, "assoc");
	public static final Type Attr_Tag = tf.constructor(uptr, Attr, "tag", tf.valueType(), "tag");
	public static final Type Attr_Bracket = tf.constructor(uptr, Attr, "bracket");
	
	/* Constructors for Associativity */
	public static final Type Associativity_Left = tf.constructor(uptr, Associativity, "left");
	public static final Type Associativity_Right = tf.constructor(uptr, Associativity, "right");
	public static final Type Associativity_Assoc = tf.constructor(uptr, Associativity, "assoc");
	public static final Type Associativity_NonAssoc = tf.constructor(uptr, Associativity, "non-assoc");
	
	/* Constructors for Condition */
	public static final Type Condition = tf.abstractDataType(uptr, "Condition");
	public static final Type Condition_Follow = tf.constructor(uptr, Condition, "follow", Symbol, "symbol");
	public static final Type Condition_NotFollow = tf.constructor(uptr, Condition, "not-follow", Symbol, "symbol");
	public static final Type Condition_Precede = tf.constructor(uptr, Condition, "precede", Symbol, "symbol");
	public static final Type Condition_NotPrecede = tf.constructor(uptr, Condition, "not-precede", Symbol, "symbol");
	public static final Type Condition_Delete = tf.constructor(uptr, Condition, "delete", Symbol, "symbol");
	public static final Type Condition_EndOfLine = tf.constructor(uptr, Condition, "end-of-line");
	public static final Type Condition_StartOfLine = tf.constructor(uptr, Condition, "begin-of-line");
	public static final Type Condition_AtColumn = tf.constructor(uptr, Condition, "at-column", tf.integerType(), "column");
	public static final Type Condition_Except = tf.constructor(uptr, Condition, "except", str, "label");
	
	/* Constructors for Symbol */
	public static final Type Symbol_Label = tf.constructor(uptr, Symbol, "label", str, "name", Symbol, "symbol");
	public static final Type Symbol_Start = tf.constructor(uptr, Symbol, "start", Symbol, "symbol");
	public static final Type Symbol_Lit = tf.constructor(uptr, Symbol, "lit", str, "string");
	public static final Type Symbol_Cilit = tf.constructor(uptr, Symbol, "cilit", str, "string");
	public static final Type Symbol_Empty = tf.constructor(uptr, Symbol, "empty");
	public static final Type Symbol_Seq = tf.constructor(uptr, Symbol, "seq", tf.listType(Symbol), "symbols");
	public static final Type Symbol_Opt = tf.constructor(uptr, Symbol, "opt", Symbol, "symbol");
	public static final Type Symbol_Alt = tf.constructor(uptr, Symbol, "alt", tf.setType(Symbol), "alternatives");
	public static final Type Symbol_Sort = tf.constructor(uptr, Symbol, "sort", str, "name");
	public static final Type Symbol_Lex = tf.constructor(uptr, Symbol, "lex", str, "name");
	public static final Type Symbol_Keywords = tf.constructor(uptr, Symbol, "keywords", str, "name");
	public static final Type Symbol_Meta = tf.constructor(uptr, Symbol, "meta", Symbol, "symbol");
	public static final Type Symbol_Conditional = tf.constructor(uptr, Symbol, "conditional", Symbol, "symbol", tf.setType(Condition), "conditions");
	public static final Type Symbol_IterSeps = tf.constructor(uptr, Symbol, "iter-seps", Symbol, "symbol", tf.listType(Symbol), "separators");
	public static final Type Symbol_IterStarSeps = tf.constructor(uptr, Symbol, "iter-star-seps", Symbol, "symbol", tf.listType(Symbol), "separators");
	public static final Type Symbol_Iter = tf.constructor(uptr, Symbol, "iter", Symbol, "symbol");
	public static final Type Symbol_IterStar = tf.constructor(uptr, Symbol, "iter-star", Symbol, "symbol");
	public static final Type Symbol_ParameterizedSort = tf.constructor(uptr, Symbol, "parameterized-sort", str, "name", tf.listType(Symbol), "parameters");
	public static final Type Symbol_ParameterizedLex = tf.constructor(uptr, Symbol, "parameterized-lex", str, "name", tf.listType(Symbol), "parameters");
	public static final Type Symbol_Parameter = tf.constructor(uptr, Symbol, "parameter", str, "name", Symbol, "bound");
	public static final Type Symbol_Layouts = tf.constructor(uptr, Symbol, "layouts", str, "name");
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
	public static final Type Symbol_Lrel = tf.constructor(uptr, Symbol, "lrel", tf.listType(Symbol), "symbols");
	public static final Type Symbol_Tuple = tf.constructor(uptr, Symbol, "tuple", tf.listType(Symbol), "symbols");
	public static final Type Symbol_List = tf.constructor(uptr, Symbol, "list", Symbol, "symbol");
	public static final Type Symbol_Map = tf.constructor(uptr, Symbol, "map", Symbol, "from", Symbol, "to");
	public static final Type Symbol_Bag = tf.constructor(uptr, Symbol, "bag", Symbol, "symbol");
	public static final Type Symbol_Adt = tf.constructor(uptr, Symbol, "adt", str, "name", tf.listType(Symbol), "parameters");
	public static final Type Symbol_Reified = tf.constructor(uptr, Symbol, "reified", Symbol, "symbol");
	public static final Type Symbol_Func = tf.constructor(uptr, Symbol, "func", Symbol, "ret", tf.listType(Symbol), "parameters", tf.listType(Symbol), "kwTypes");
	public static final Type Symbol_Alias = tf.constructor(uptr, Symbol, "alias", str, "name", tf.listType(Symbol), "parameters", Symbol, "aliased");
	public static final Type Symbol_Cons = tf.constructor(uptr, Symbol, "cons", Symbol, "adt", str, "name", tf.listType(Symbol), "parameters");
	
	/* Internally (type checker) used constructors for Symbol: */
	public static final Type Symbol_Overloaded = tf.constructor(uptr, Symbol, "overloaded", tf.setType(Symbol), "overloads", tf.setType(Symbol), "defaults");
	public static final Type Symbol_Prod = tf.constructor(uptr, Symbol, "prod", Symbol, "sort", str, "name", tf.listType(Symbol), "parameters",  tf.setType(Attr), "attributes");

	/* Constructors for CharRange */
	public static final Type CharRange_Single = tf.constructor(uptr, CharRange, "single", tf.integerType()); // TODO: can go when older parser is gone, still occurs in regression tests
	public static final Type CharRange_Range = tf.constructor(uptr, CharRange, "range", tf.integerType(), "begin", tf.integerType(), "end");

	/* Constructors for Attribute */
	public static final IValue Attribute_Assoc_Left = bootFactory.constructor(Attr_Assoc, bootFactory.constructor(Associativity_Left));
	public static final IValue Attribute_Assoc_Right = bootFactory.constructor(Attr_Assoc, bootFactory.constructor(Associativity_Right));
	public static final IValue Attribute_Assoc_Non_Assoc = bootFactory.constructor(Attr_Assoc, bootFactory.constructor(Associativity_NonAssoc));
	public static final IValue Attribute_Assoc_Assoc = bootFactory.constructor(Attr_Assoc, bootFactory.constructor(Associativity_Assoc));
	public static final IValue Attribute_Bracket = bootFactory.constructor(Attr_Bracket);
	
	/* Constructors for Function instances to be used in encodeAsConstructor */
	public static final Type Function = tf.abstractDataType(uptr, "Function");
	public static final Type Function_Choice = tf.constructor(uptr, Function, "choice", tf.listType(Function), "alternatives", tf.listType(Function), "otherwise");
	public static final Type Function_Function = tf.constructor(uptr, Function, "function", tf.sourceLocationType(), "id");
	public static final Type Function_Closure = tf.constructor(uptr, Function, "closure", Function, "definition", tf.mapType(str, tf.valueType()), "environment");
	public static final Type Function_Composition = tf.constructor(uptr, Function, "composition", Function, "lhs", Function, "rhs");
	
	/* grammars */
	public static final Type Grammar = tf.abstractDataType(uptr,  "Grammar");
	public static final Type Grammar_Default = tf.constructor(uptr,  Grammar, "grammar", tf.setType(Symbol), "starts", tf.mapType(Symbol, "sort", Production, "def"), "rules");
    
	public static final String Location = "src";
	public static final String LegacyLocation = "loc";
	
	static {
		uptr.declareKeywordParameter(Tree, Location, tf.sourceLocationType());
	}

	/** nested class for thread safe singleton allocation */
	static private class InstanceHolder {
		static final RascalValueFactory sInstance = new RascalValueFactory();
	}
	
	/*package*/ static IRascalValueFactory getInstance() {
		return InstanceHolder.sInstance;
	}
	
	/** caches ASCII characters for sharing */
	private final static ITree byteChars[];
	private final static Type byteCharTypes[];
	static {
		byteChars = new ITree[Byte.MAX_VALUE];
		byteCharTypes = new Type[Byte.MAX_VALUE];
		for (byte i = 0; i < Byte.MAX_VALUE; i++) {
			byteChars[i] = new CharByte(i);
			byteCharTypes[i] = RascalTypeFactory.getInstance().nonTerminalType(bootFactory.constructor(Symbol_CharClass, bootFactory.list(bootFactory.constructor(CharRange_Range, bootFactory.integer(i), bootFactory.integer(i)))));
		}
	}
	
	/**
	 * Use getInstance()
	 */
	public RascalValueFactory() {
		super(bootFactory);
	}
	
	public static TypeStore getStore() {
		return uptr;
	}

	public static boolean isLegacySourceLocationAnnotation(Type receiver, String label) {
        return receiver.isSubtypeOf(Tree) && label.equals(LegacyLocation); 
    }
	
	/**
   * Allocates new {@link TypeStore} environments that are used within {@link IValueReader}.
   *
   * Type stores are used as encapsulated namespaces for types. The supplier creates a fresh type
   * store environment, to avoid name clashes when nesting types / values.
   */
  public static final Supplier<TypeStore> TYPE_STORE_SUPPLIER = () -> {
    TypeStore typeStore = new TypeStore();
    typeStore.declareAbstractDataType(RascalValueFactory.Type);
    typeStore.declareConstructor(RascalValueFactory.Type_Reified);
    typeStore.declareAbstractDataType(RascalValueFactory.ADTforType);
    return typeStore;
  };

	@Override
	public INode node(String name, IValue... children) {
		IConstructor res = specializeNode(name, children);
		return res != null ? res: super.node(name, children);
	}

	private IConstructor specializeNode(String name, IValue... children) {
		if (Type_Reified.getName().equals(name) 
				&& children.length == 2
				&& children[0].getType().isSubtypeOf(Type_Reified.getFieldType(0))
				&& children[1].getType().isSubtypeOf(Type_Reified.getFieldType(1))) {
			return reifiedType((IConstructor) children[0], (IMap) children[1]);
		}
		
		return null;
	}
	
	@Override
	public INode node(String name, IValue[] children,
			Map<String, IValue> kws) throws FactTypeUseException {
		IConstructor result = specializeNode(name, children);
		return result != null 
				? (kws != null  && !kws.isEmpty() ? result.asWithKeywordParameters().setParameters(kws) : result) 
				: super.node(name, children, kws);
	}
	
	@Override
	public IConstructor constructor(Type constructor, IValue[] children, Map<String, IValue> kwParams) throws FactTypeUseException {
	    if (constructor == null) {
            throw new NullPointerException();
        }
        Arrays.stream(children).forEach(t -> { if (t == null) throw new NullPointerException(); });
        if (kwParams != null) { 
            kwParams.values().stream().forEach(t -> { if (t == null) throw new NullPointerException(); }); 
        }
        
		IConstructor result = specializeConstructor(constructor, children);
		return result != null 
				? (kwParams != null && !kwParams.isEmpty() && result.mayHaveKeywordParameters() ? result.asWithKeywordParameters().setParameters(kwParams) : result) 
				: super.constructor(constructor, children, kwParams);
	}
	
	@Override
	public IConstructor constructor(Type constructor, IValue... children) throws FactTypeUseException {
	    if (constructor == null) { throw new NullPointerException(); }
	    Arrays.stream(children).forEach(t -> { if (t == null) throw new NullPointerException(); });
	    
		IConstructor result = specializeConstructor(constructor, children);
		return result != null ? result : super.constructor(constructor, children);
	}
	
	/**
	 * This is where the core functionality of this class is implemented, specializing IConstructor values
	 */
	private IConstructor specializeConstructor(Type constructor, IValue... children) {
		if (constructor.getAbstractDataType() == Tree) {
			if (constructor == Tree_Appl) {
				return appl((IConstructor) children[0], (IList) children[1]);
			}
			else if (constructor == Tree_Amb) {
				return amb((ISet) children[0]);
			}
			else if (constructor == Tree_Char) {
				return character(((IInteger) children[0]).intValue());
			}
			else if (constructor == Tree_Cycle) {
				return cycle((IConstructor) children[0], ((IInteger) children[1]).intValue());
			}
		}
		else if (constructor == Type_Reified || constructor.getAbstractDataType() == ADTforType) {
			return reifiedType((IConstructor) children[0], (IMap) children[1]);
		}
		
		return null;
	}
	
	@Override
	public IConstructor reifiedType(IConstructor symbol, IMap definitions) {
		java.util.Map<Type,Type> bindings = 
		        Collections.singletonMap(RascalValueFactory.TypeParam, tr.symbolToType(symbol, definitions));
		return super.constructor(RascalValueFactory.Type_Reified.instantiate(bindings), symbol, definitions);
	}
	
	@Override
	public ITree character(int ch) {
		if (ch >= 0 && ch < Byte.MAX_VALUE) {
			return character((byte) ch);
		}
		
		return new CharInt(ch);
	}
	
	@Override
	public ITree character(byte ch) {
		return byteChars[ch];
	}

	@Override
	public IConstructor grammar(IMap rules) {
	    return constructor(Grammar_Default, setWriter().done(), rules);
	}
	 
	@Override
	public ITree appl(Map<String,IValue> annos, IConstructor prod, IList args) {
		return (ITree) appl(prod, args).asWithKeywordParameters().setParameters(annos);
	}

	/**
	 * For use with the UPTRNodeFactory otherwise will be replaced
	 */
	@Deprecated
	@Override
	public ITree appl(IConstructor prod, ArrayList<ITree> args) {
		switch (args.size()) {
		case 0: return new Appl0(prod);
		case 1: return new Appl1(prod, args.get(0));
		case 2: return new Appl2(prod, args.get(0), args.get(1));
		case 3: return new Appl3(prod, args.get(0), args.get(1), args.get(2));
		case 4: return new Appl4(prod, args.get(0), args.get(1), args.get(2), args.get(3));
		case 5: return new Appl5(prod, args.get(0), args.get(1), args.get(2), args.get(3), args.get(4));
		case 6: return new Appl6(prod, args.get(0), args.get(1), args.get(2), args.get(3), args.get(4), args.get(5));
		case 7: return new Appl7(prod, args.get(0), args.get(1), args.get(2), args.get(3), args.get(4), args.get(5), args.get(6));
		default:
			return new ApplN(prod, new ArrayListArgumentList(args));
		}
	}
	
	/**
	 * Construct a specialized IConstructor representing a Tree, applying a prod to a list of arguments
	 */
	@Override
	public ITree appl(IConstructor prod, IList args) {
		switch (args.length()) {
		case 0: return new Appl0(prod);
		case 1: return new Appl1(prod, args.get(0));
		case 2: return new Appl2(prod, args.get(0), args.get(1));
		case 3: return new Appl3(prod, args.get(0), args.get(1), args.get(2));
		case 4: return new Appl4(prod, args.get(0), args.get(1), args.get(2), args.get(3));
		case 5: return new Appl5(prod, args.get(0), args.get(1), args.get(2), args.get(3), args.get(4));
		case 6: return new Appl6(prod, args.get(0), args.get(1), args.get(2), args.get(3), args.get(4), args.get(5));
		case 7: return new Appl7(prod, args.get(0), args.get(1), args.get(2), args.get(3), args.get(4), args.get(5), args.get(6));
		default: return new ApplN(prod, args);
		}
	}
	
	/**
	 * Watch out, the array is not cloned! Must not modify hereafter.
	 */
	@Override
	public ITree appl(IConstructor prod, IValue... args) {
		switch (args.length) {
		case 0: return new Appl0(prod);
		case 1: return new Appl1(prod, args[0]);
		case 2: return new Appl2(prod, args[0], args[1]);
		case 3: return new Appl3(prod, args[0], args[1], args[2]);
		case 4: return new Appl4(prod, args[0], args[1], args[2], args[3]);
		case 5: return new Appl5(prod, args[0], args[1], args[2], args[3], args[4]);
		case 6: return new Appl6(prod, args[0], args[1], args[2], args[3], args[4], args[5]);
		case 7: return new Appl7(prod, args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
		default: return new ApplN(prod, new ArrayArgumentList(args));
		}
	}
	
	@Override
	public ITree cycle(IConstructor symbol, int cycleLength) {
		return new Cycle(symbol, cycleLength);
	}
	
	@Override
	public ITree amb(ISet alternatives) {
		if (alternatives.size() == 1) {
			// fast builtin canonicalization of singleton ambiguity clusters is
			// necessary when client code filters ambiguity clusters. This may
			// happen during visits and during parse tree construction using the
			// `filter` statement or by constructing a singleton set accidentally.
			//
			// &T <: Tree amb({&T <: Tree t}) = t;
			//
			// Note that without this canonicalization pattern matches are bound
			// to fail because of the extra indirection of the amb cluster.
			return (ITree) alternatives.iterator().next();
		}
		
		return new Amb(alternatives);
	}
	
	/*
	 * Below here are specialized static classes for {@link IConstructor}
	 * creating slim instances for Tree appl and for lazy {@link IList} 
	 * implementations for avoiding copying arrays/lists at parse tree creation time.
	 * 
	 * Some classes are specialized by arity to avoid memory overhead. Specialization
	 * is also necessary to correctly implement getType() and to optimize hashCode().
	 * 
	 * To avoid code duplication between specialized classes we factor out common elements in the {@link AbstractAppl} and
	 * and {@link AbstractArgumentList} abstract classes.
	 */
	
	static class CharInt implements ITree, IExternalValue {
		final int ch;
		
		@Override
		public boolean isChar() {
			return true;
		}
		
		@Override
		public INode setChildren(IValue[] childArray) {
		    return set(0, childArray[0]);
		}
		
		@Override
		public <E extends Throwable> ITree accept(TreeVisitor<E> v) throws E {
			return (ITree) v.visitTreeChar(this);
		}
		
		public CharInt(int ch) {
			this.ch = ch;
		}

		@Override
		public IConstructor encodeAsConstructor() {
			return this;
		}
		
		@Override
		public IValue get(int i) throws IndexOutOfBoundsException {
			switch (i) {
			case 0: return getInstance().integer(ch);
			default: throw new IndexOutOfBoundsException();
			}
		}

		@Override
		public int arity() {
			return 1;
		}

		@Override
		public String getName() {
			return Tree_Char.getName();
		}

		@Override
		public Iterable<IValue> getChildren() {
			return this;
		}
		
		@Override
		public int hashCode() {
			return ch;
		}
		
		@Override
		public Iterator<IValue> iterator() {
			return new Iterator<IValue>() {
				boolean done = false;
				
				@Override
				public boolean hasNext() {
					return !done;
				}

				@Override
				public IValue next() {
					done = true;
					return getInstance().integer(ch); 
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}

		@Override
		public INode replace(int first, int second, int end, IList repl)
				throws FactTypeUseException, IndexOutOfBoundsException {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T, E extends Throwable> T accept(IValueVisitor<T, E> v)
				throws E {
			return v.visitConstructor(this);
		}

		@Override
		public boolean equals(Object other) {
		    if (other == null) {
		        return false;
		    }
		    
			if (other instanceof CharInt) {
				CharInt o = (CharInt) other;
				return o.ch == ch;
			}
			
			return false;
		}
		
		@Override
		public boolean match(IValue other) {
		    return equals(other);
		}
		
		@Override
		public boolean mayHaveKeywordParameters() {
			return true;
		}

		@Override
		public Type getType() {
			return RascalTypeFactory.getInstance().nonTerminalType(SymbolAdapter.charClass(ch));
		}

		@Override
		public Type getConstructorType() {
			return Tree_Char;
		}

		@Override
		public Type getUninstantiatedConstructorType() {
			return Tree_Char;
		}

		@Override
		public IValue get(String label) {
			return get(Tree_Char.getFieldIndex(label));
		}

		@Override
		public IConstructor set(String label, IValue newChild)
				throws FactTypeUseException {
			return set(Tree_Char.getFieldIndex(label), newChild);
		}

		@Override
		public boolean has(String label) {
			return Tree_Char.hasField(label);
		}

		@Override
		public IConstructor set(int index, IValue newChild)
				throws FactTypeUseException {
			switch (index) {
			case 0: return getInstance().character(((IInteger) newChild).intValue());
			default: throw new IndexOutOfBoundsException();
			}
		}

		@Override
		public Type getChildrenTypes() {
			return tf.tupleType(tf.integerType());
		}

		@Override
        public IWithKeywordParameters<ITree> asWithKeywordParameters() {
             return new AbstractDefaultWithKeywordParameters<ITree>(this, AbstractSpecialisedImmutableMap.<String,IValue>mapOf()) {
                    @Override
                    protected ITree wrap(ITree content, io.usethesource.capsule.Map.Immutable<String, IValue> parameters) {
                      return new CharWithKeywordParametersFacade(content, parameters);
                    }
             }; 
        }
	}
	
	private static class CharByte implements ITree, IExternalValue {
		final byte ch;
		
		public CharByte(byte ch) {
			this.ch = ch;
		}
		
		@Override
		public boolean isChar() {
			return true;
		}
		
		@Override
		public INode setChildren(IValue[] childArray) {
		    return set(0, childArray[0]);
		}
		
		@Override
		public <E extends Throwable> ITree accept(TreeVisitor<E> v) throws E {
			return (ITree) v.visitTreeChar(this);
		}
		
		@Override
		public IConstructor encodeAsConstructor() {
			return this;
		}

		@Override
		public IValue get(int i) throws IndexOutOfBoundsException {
			switch (i) {
			case 0: return getInstance().integer(ch);
			default: throw new IndexOutOfBoundsException();
			}
		}

		@Override
		public int arity() {
			return 1;
		}

		@Override
		public String getName() {
			return Tree_Char.getName();
		}

		@Override
		public Iterable<IValue> getChildren() {
			return this;
		}
		
		@Override
		public int hashCode() {
			return ch;
		}

		@Override
		public Iterator<IValue> iterator() {
			return new Iterator<IValue>() {
				boolean done = false;
				
				@Override
				public boolean hasNext() {
					return !done;
				}

				@Override
				public IValue next() {
					done = true;
					return getInstance().integer(ch); 
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}
		
		@Override
        public String toString() {
            return StandardTextWriter.valueToString(this);
        }

		@Override
		public INode replace(int first, int second, int end, IList repl)
				throws FactTypeUseException, IndexOutOfBoundsException {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T, E extends Throwable> T accept(IValueVisitor<T, E> v)
				throws E {
			return v.visitConstructor(this);
		}

		@Override
		public boolean equals(Object other) {
		    if (other == null) {
		        return false;
		    }
		    
			if (other instanceof CharByte) {
				CharByte o = (CharByte) other;
				return o.ch == ch;
			}
			
			return false;
		}
		
		@Override
        public boolean match(IValue other) {
            return equals(other);
        }
		
		@Override
		public boolean mayHaveKeywordParameters() {
			return true;
		}

		@Override
		public Type getType() {
			return byteCharTypes[ch];
		}

		@Override
		public Type getConstructorType() {
			return Tree_Char;
		}

		@Override
		public Type getUninstantiatedConstructorType() {
			return Tree_Char;
		}

		@Override
		public IValue get(String label) {
			return get(Tree_Char.getFieldIndex(label));
		}

		@Override
		public IConstructor set(String label, IValue newChild)
				throws FactTypeUseException {
			return set(Tree_Char.getFieldIndex(label), newChild);
		}

		@Override
		public boolean has(String label) {
			return Tree_Char.hasField(label);
		}

		@Override
		public IConstructor set(int index, IValue newChild)
				throws FactTypeUseException {
			switch (index) {
			case 0: return getInstance().character(((IInteger) newChild).intValue());
			default: throw new IndexOutOfBoundsException();
			}
		}

		@Override
		public Type getChildrenTypes() {
			return tf.tupleType(tf.integerType());
		}

		@Override
        public IWithKeywordParameters<ITree> asWithKeywordParameters() {
             return new AbstractDefaultWithKeywordParameters<ITree>(this, AbstractSpecialisedImmutableMap.<String,IValue>mapOf()) {
                    @Override
                    protected ITree wrap(ITree content, io.usethesource.capsule.Map.Immutable<String, IValue> parameters) {
                      return new CharWithKeywordParametersFacade(content, parameters);
                    }
             }; 
        }
	}
	
	private static class Cycle implements ITree, IExternalValue {
		protected final IConstructor symbol;
		protected final int cycleLength;
		
		public Cycle(IConstructor symbol, int cycleLength) {
			this.symbol = symbol;
			this.cycleLength = cycleLength;
		}
		
		@Override
		public boolean isCycle() {
			return true;
		}
		
		@Override
		public <E extends Throwable> ITree accept(TreeVisitor<E> v) throws E {
			return (ITree) v.visitTreeCycle(this);
		}
		
		@Override
		public IConstructor encodeAsConstructor() {
			return this;
		}
		
		@Override
		public String getName() {
			return Tree_Cycle.getName();
		}
		
		@Override
		public Iterable<IValue> getChildren() {
			return this;
		}
		
		@Override
		public int hashCode() {
			return 17 + 19 * symbol.hashCode() + 29 * cycleLength; 
		}
		
		@Override
		public boolean equals(Object other) {
		    if (other == null) {
		        return false;
		    }
		    
			if (other instanceof IConstructor) {
				IConstructor cons = (IConstructor) other;
				
				return cons.getConstructorType() == getConstructorType()
						&& cons.get(0).equals(get(0))
						&& ((IInteger) cons.get(1)).intValue() == cycleLength;
			}
			
			return false;
		}
		
		@Override
        public boolean match(IValue other) {
            return equals(other);
        }
		
		@Override
		public Iterator<IValue> iterator() {
			return new Iterator<IValue>() {
				private int count = 0;
				
				@Override
				public boolean hasNext() {
					return count < 2;
				}

				@Override
				public IValue next() {
					count++;
					switch(count) {
					case 1: return symbol;
					case 2: return getInstance().integer(cycleLength);
					default: return null;
					}
				}
			};
		}

		@Override
		public int arity() {
			return 2;
		}
		
		@Override
		public String toString() {
			return StandardTextWriter.valueToString(this);
		}
		
		@Override
		public INode replace(int first, int second, int end, IList repl)
				throws FactTypeUseException, IndexOutOfBoundsException {
			throw new UnsupportedOperationException("Replace not supported on constructor.");
		}

		@Override
		public <T, E extends Throwable> T accept(IValueVisitor<T, E> v)
				throws E {
			return v.visitConstructor(this);
		}
		
		@Override
		public boolean mayHaveKeywordParameters() {
			return false;
		}

		@Override
		public Type getType() {
			return RascalTypeFactory.getInstance().nonTerminalType(symbol);
		}

		@Override
		public Type getConstructorType() {
			return Tree_Cycle;
		}

		@Override
		public Type getUninstantiatedConstructorType() {
			return Tree_Cycle;
		}

		@Override
		public IValue get(String label) {
			switch (label) {
			case "symbol": return symbol;
			case "cycleLength": return getInstance().integer(cycleLength);
			default: throw new UndeclaredFieldException(Tree_Amb, label);
			}
		}

		@Override
		public IConstructor set(String label, IValue newChild)
				throws FactTypeUseException {
			switch (label) {
			case "symbol": return getInstance().cycle((IConstructor) newChild, cycleLength);
			case "cycleLength" : return getInstance().cycle(symbol, ((IInteger) newChild).intValue());
			default: throw new UndeclaredFieldException(Tree_Appl, label);
			}
		}

		@Override
		public boolean has(String label) {
			return Tree_Cycle.hasField(label);
		}

		@Override
		public IConstructor set(int index, IValue newChild)
				throws FactTypeUseException {
			switch (index) {
			case 0: return getInstance().cycle((IConstructor) newChild, cycleLength);
			case 1: return getInstance().cycle(symbol, ((IInteger) newChild).intValue());
			default: throw new IndexOutOfBoundsException();
			}
		}

		@Override
		public Type getChildrenTypes() {
			return tf.tupleType(Symbol, tf.integerType());
		}

		@Override
		public IWithKeywordParameters<IConstructor> asWithKeywordParameters() {
			 return new AbstractDefaultWithKeywordParameters<IConstructor>(this, AbstractSpecialisedImmutableMap.<String,IValue>mapOf()) {
				    @Override
				    protected IConstructor wrap(IConstructor content, io.usethesource.capsule.Map.Immutable<String, IValue> parameters) {
				      	return new CycleWithKeywordParametersFacade(content, parameters);
				    }
			 }; 
		}
		
		@Override
		public IValue get(int i) throws IndexOutOfBoundsException {
			switch (i) {
			case 0: return symbol;
			case 1: return getInstance().integer(cycleLength);
			default: throw new IndexOutOfBoundsException();
			}
		}
	}
	
	private static class Amb implements ITree, IExternalValue {
		protected final ISet alternatives;
		
		public Amb(ISet alts) {
			this.alternatives = alts;
		}
		
		@Override
		public boolean isAmb() {
			return true;
		}
		
		@Override
		public <E extends Throwable> ITree accept(TreeVisitor<E> v) throws E {
			return (ITree) v.visitTreeAmb(this);
		}
		
		@Override
		public IConstructor encodeAsConstructor() {
			return this;
		}
		
		@Override
		public String getName() {
			return Tree_Amb.getName();
		}
		
		@Override
		public Iterable<IValue> getChildren() {
			return this;
		}
		
		@Override
		public int hashCode() {
			return 43 + 751 * alternatives.hashCode(); 
		}
		
		@Override
		public boolean equals(Object other) {
		    if (other == null) {
		        return false;
		    }
		    
			if (other instanceof Amb) {
				ITree cons = (ITree) other;
				return cons.getAlternatives().equals(alternatives);
			}
			
			return false;
		}
		
		@Override
        public boolean match(IValue other) {
		    if (other instanceof Amb) {
                ITree cons = (ITree) other;
                return cons.getAlternatives().match(alternatives);
            }
            
            return false;
        }
		
		@Override
		public ISet getAlternatives() {
			return alternatives;
		}

		@Override
		public Iterator<IValue> iterator() {
			return new Iterator<IValue>() {
				private int count = 0;
				
				@Override
				public boolean hasNext() {
					return count < 1;
				}

				@Override
				public IValue next() {
					count++;
					switch(count) {
					case 1: return getAlternatives();
					default: return null;
					}
				}
			};
		}

		@Override
		public int arity() {
			return 1;
		}
		
		@Override
		public String toString() {
			return StandardTextWriter.valueToString(this);
		}
		
		@Override
		public INode replace(int first, int second, int end, IList repl)
				throws FactTypeUseException, IndexOutOfBoundsException {
			throw new UnsupportedOperationException("Replace not supported on constructor.");
		}

		@Override
		public <T, E extends Throwable> T accept(IValueVisitor<T, E> v)
				throws E {
			return v.visitConstructor(this);
		}
		
		@Override
		public boolean mayHaveKeywordParameters() {
			return true;
		}
		
		

		@Override
		public Type getType() {
			if (!alternatives.isEmpty()) {
				return RascalTypeFactory.getInstance().nonTerminalType((IConstructor) alternatives.iterator().next());
			}
			else {
				return RascalTypeFactory.getInstance().nonTerminalType(IRascalValueFactory.getInstance().constructor(RascalValueFactory.Symbol_Empty));
			}
		}

		@Override
		public Type getConstructorType() {
			return Tree_Amb;
		}

		@Override
		public Type getUninstantiatedConstructorType() {
			return Tree_Amb;
		}

		@Override
		public IValue get(String label) {
			switch (label) {
			case "alternatives": return getAlternatives();
			default: throw new UndeclaredFieldException(Tree_Amb, label);
			}
		}

		@Override
		public ITree set(String label, IValue newChild)
				throws FactTypeUseException {
			switch (label) {
			case "alternatives": return getInstance().amb((ISet) newChild);
			default: throw new UndeclaredFieldException(Tree_Appl, label);
			}
		}

		@Override
		public boolean has(String label) {
			return Tree_Amb.hasField(label);
		}

		@Override
		public ITree set(int index, IValue newChild)
				throws FactTypeUseException {
			switch (index) {
			case 0: return getInstance().amb((ISet) newChild);
			default: throw new IndexOutOfBoundsException();
			}
		}

		@Override
		public Type getChildrenTypes() {
			return tf.tupleType(Alternatives);
		}

		@Override
		public IWithKeywordParameters<ITree> asWithKeywordParameters() {
			 return new AbstractDefaultWithKeywordParameters<ITree>(this, AbstractSpecialisedImmutableMap.<String,IValue>mapOf()) {
				    @Override
				    protected ITree wrap(ITree content, io.usethesource.capsule.Map.Immutable<String, IValue> parameters) {
				      return new AmbWithKeywordParametersFacade(content, parameters);
				    }
			 }; 
		}
		
		@Override
		public IValue get(int i) throws IndexOutOfBoundsException {
			switch (i) {
			case 0: return alternatives;
			default: throw new IndexOutOfBoundsException();
			}
		}
	}
	
	static public class ApplWithKeywordParametersFacade extends ConstructorWithKeywordParametersFacade implements ITree {
        public ApplWithKeywordParametersFacade(IConstructor content, io.usethesource.capsule.Map.Immutable<String, IValue> parameters) {
            super(content, parameters);
        }

        @Override
        public <E extends Throwable> ITree accept(TreeVisitor<E> v) throws E {
            return v.visitTreeAppl(this);
        }
        
        @Override
        public ITree set(String label, IValue newChild)
                throws FactTypeUseException {
            IConstructor newContent = content.set(label, newChild);
            return new ApplWithKeywordParametersFacade(newContent, parameters);                
        }
        
        @Override
        public ITree set(int index, IValue newChild)
                throws FactTypeUseException {
            IConstructor newContent = content.set(index, newChild);
            return new ApplWithKeywordParametersFacade(newContent, parameters);            
        }

        @Override
        public IWithKeywordParameters<? extends IConstructor> asWithKeywordParameters() {
            return new AbstractDefaultWithKeywordParameters<IConstructor>(content, parameters) {

                @Override
                protected IConstructor wrap(IConstructor content, io.usethesource.capsule.Map.Immutable<String, IValue> parameters) {
                    return parameters.isEmpty() ? content : new ApplWithKeywordParametersFacade(content, parameters);
                }
            };
        }
        
        @Override
        public boolean isAppl() {
            return true;
        }
        
        @Override
        public IConstructor getProduction() {
            return ((ITree) content).getProduction();
        }
        
        @Override
        public IList getArgs() {
            return ((ITree) content).getArgs();
        }
    }
	
	static public class AmbWithKeywordParametersFacade extends ConstructorWithKeywordParametersFacade implements ITree {
        public AmbWithKeywordParametersFacade(IConstructor content, io.usethesource.capsule.Map.Immutable<String, IValue> parameters) {
            super(content, parameters);
        }

        @Override
        public <E extends Throwable> ITree accept(TreeVisitor<E> v) throws E {
            return v.visitTreeAmb(this);
        }
        
        @Override
        public ITree set(String label, IValue newChild)
                throws FactTypeUseException {
            IConstructor newContent = content.set(label, newChild);
            return new AmbWithKeywordParametersFacade(newContent, parameters);                
        }
        
        @Override
        public ITree set(int index, IValue newChild)
                throws FactTypeUseException {
            IConstructor newContent = content.set(index, newChild);
            return new AmbWithKeywordParametersFacade(newContent, parameters);            
        }

        @Override
        public IWithKeywordParameters<? extends IConstructor> asWithKeywordParameters() {
            return new AbstractDefaultWithKeywordParameters<IConstructor>(content, parameters) {

                @Override
                protected IConstructor wrap(IConstructor content, io.usethesource.capsule.Map.Immutable<String, IValue> parameters) {
                    return parameters.isEmpty() ? content : new AmbWithKeywordParametersFacade(content, parameters);
                }
            };
        }
        
        @Override
        public boolean isAmb() {
            return true;
        }

        @Override
        public ISet getAlternatives() {
            return ((ITree) content).getAlternatives();
        }
    }
	
	static public class CycleWithKeywordParametersFacade extends ConstructorWithKeywordParametersFacade implements ITree {
        public CycleWithKeywordParametersFacade(IConstructor content, io.usethesource.capsule.Map.Immutable<String, IValue> parameters) {
            super(content, parameters);
        }

        @Override
        public <E extends Throwable> ITree accept(TreeVisitor<E> v) throws E {
            return v.visitTreeCycle(this);
        }
        
        @Override
        public ITree set(String label, IValue newChild)
                throws FactTypeUseException {
            IConstructor newContent = content.set(label, newChild);
            return new CycleWithKeywordParametersFacade(newContent, parameters);                
        }
        
        @Override
        public ITree set(int index, IValue newChild)
                throws FactTypeUseException {
            IConstructor newContent = content.set(index, newChild);
            return new CycleWithKeywordParametersFacade(newContent, parameters);            
        }

        @Override
        public IWithKeywordParameters<? extends IConstructor> asWithKeywordParameters() {
            return new AbstractDefaultWithKeywordParameters<IConstructor>(content, parameters) {

                @Override
                protected IConstructor wrap(IConstructor content, io.usethesource.capsule.Map.Immutable<String, IValue> parameters) {
                    return parameters.isEmpty() ? content : new CycleWithKeywordParametersFacade(content, parameters);
                }
            };
        }
        
        @Override
        public boolean isCycle() {
            return true;
        }
    }
	
	
	static public class CharWithKeywordParametersFacade extends ConstructorWithKeywordParametersFacade implements ITree {
        public CharWithKeywordParametersFacade(IConstructor content, io.usethesource.capsule.Map.Immutable<String, IValue> parameters) {
            super(content, parameters);
        }

        @Override
        public <E extends Throwable> ITree accept(TreeVisitor<E> v) throws E {
            return v.visitTreeChar(this);
        }
        
        @Override
        public ITree set(String label, IValue newChild)
                throws FactTypeUseException {
            IConstructor newContent = content.set(label, newChild);
            return new CharWithKeywordParametersFacade(newContent, parameters);                
        }
        
        @Override
        public ITree set(int index, IValue newChild)
                throws FactTypeUseException {
            IConstructor newContent = content.set(index, newChild);
            return new CharWithKeywordParametersFacade(newContent, parameters);            
        }

        @Override
        public IWithKeywordParameters<? extends IConstructor> asWithKeywordParameters() {
            return new AbstractDefaultWithKeywordParameters<IConstructor>(content, parameters) {

                @Override
                protected IConstructor wrap(IConstructor content, io.usethesource.capsule.Map.Immutable<String, IValue> parameters) {
                    return parameters.isEmpty() ? content : new CharWithKeywordParametersFacade(content, parameters);
                }
            };
        }
        
        @Override
        public boolean isChar() {
            return true;
        }

        @Override
        public IInteger getCharacter() {
            return ((ITree) content).getCharacter();
        }
    }
	
	private static abstract class AbstractAppl implements ITree, IExternalValue {
		protected final IConstructor production;
		protected final boolean isMatchIgnorable;
		protected Type type = null;
        

		@Override
		public <E extends Throwable> ITree accept(TreeVisitor<E> v) throws E {
		    return v.visitTreeAppl(this);
		}
		
		protected AbstractAppl(IConstructor production) {
			this.production = production;
			this.isMatchIgnorable
	          = ProductionAdapter.isLayout(production)
	            || ProductionAdapter.isCILiteral(production)
	            || ProductionAdapter.isLiteral(production);
		}
		
		@Override
		public IConstructor getProduction() {
			return production;
		}

		@Override
		public boolean isAppl() {
			return true;
		}
		
		@Override
		public IConstructor encodeAsConstructor() {
			return this;
		}
		
		@Override
		public String getName() {
			return Tree_Appl.getName();
		}
		
		@Override
		public Iterable<IValue> getChildren() {
			return this;
		}
		
		@Override
		public int hashCode() {
			return 41 
				  + 1331 * production.hashCode() 
				  + 13331 * getArgs().hashCode(); 
		}
		
		@Override
		public boolean equals(Object other) {
		    if (other == null) {
		        return false;
		    }
		    
			if (other instanceof IConstructor) {
				IConstructor cons = (IConstructor) other;
				
				return cons.getConstructorType() == getConstructorType()
						&& cons.get(0).equals(get(0))
						&& cons.get(1).equals(get(1));
			}
			
			return false;
		}
		
		@Override
        public boolean match(IValue other) {
		    if (isMatchIgnorable) {
		        return true;
		    }
		    
		    
            if (other instanceof IConstructor) {
                IConstructor cons = (IConstructor) other;
                
                return cons.getConstructorType() == getConstructorType()
                        && cons.get(0).equals(get(0))
                        && cons.get(1).match(get(1));
            }
            
            return false;
        }
		
		@Override
		abstract public IList getArgs();

		@Override
		public Iterator<IValue> iterator() {
			return new Iterator<IValue>() {
				private int count = 0;
				
				@Override
				public boolean hasNext() {
					return count < 2;
				}

				@Override
				public IValue next() {
					count++;
					switch(count) {
					case 1: return production;
					case 2: return getArgs();
					default: return null;
					}
				}
			};
		}

		@Override
		public int arity() {
			return 2;
		}
		
		@Override
		public String toString() {
			return StandardTextWriter.valueToString(this);
		}
		
		@Override
		public INode replace(int first, int second, int end, IList repl)
				throws FactTypeUseException, IndexOutOfBoundsException {
			throw new UnsupportedOperationException("Replace not supported on constructor.");
		}

		@Override
		public <T, E extends Throwable> T accept(IValueVisitor<T, E> v)
				throws E {
			return v.visitConstructor(this);
		}
		
		@Override
		public boolean mayHaveKeywordParameters() {
			return true;
		}

		@Override
		public Type getType() {
			// it is important to give the whole tree here, to be able to check for non-empty lists which have a more concrete type
			// than possibly empty lists!
		    if (type == null) {
		        type = RascalTypeFactory.getInstance().nonTerminalType(this);
		    }
		    
		    return type;
		}

		@Override
		public Type getConstructorType() {
			return Tree_Appl;
		}

		@Override
		public Type getUninstantiatedConstructorType() {
			return Tree_Appl;
		}

		@Override
		public IValue get(String label) {
			switch (label) {
			case "prod": return production;
			case "args": return getArgs();
			default: throw new UndeclaredFieldException(Tree_Appl, label);
			}
		}

		@Override
		public ITree set(String label, IValue newChild) throws FactTypeUseException {
			switch (label) {
			case "prod": return getInstance().appl((IConstructor) newChild, getArgs());
			case "args": return getInstance().appl(production, (IList) newChild);
			default: throw new UndeclaredFieldException(Tree_Appl, label);
			}
		}

		@Override
		public boolean has(String label) {
			return Tree_Appl.hasField(label);
		}

		@Override
		public ITree set(int index, IValue newChild)
				throws FactTypeUseException {
			switch (index) {
			case 0: return getInstance().appl((IConstructor) newChild, getArgs());
			case 1: return getInstance().appl(production, newChild);
			default: throw new IndexOutOfBoundsException();
			}
		}

		@Override
		public Type getChildrenTypes() {
			return tf.tupleType(production.getType(), Args);
		}

		@Override
		public IWithKeywordParameters<ITree> asWithKeywordParameters() {
			 return new AbstractDefaultWithKeywordParameters<ITree>(this, AbstractSpecialisedImmutableMap.<String,IValue>mapOf()) {
				    @Override
				    protected ITree wrap(ITree content, io.usethesource.capsule.Map.Immutable<String, IValue> parameters) {
				      return new ApplWithKeywordParametersFacade(content, parameters);
				    }
			 }; 
		}
		
		@Override
		public IValue get(int i) throws IndexOutOfBoundsException {
			switch (i) {
			case 0: return production;
			case 1: return getArgs();
			default: throw new IndexOutOfBoundsException();
			}
		}
	}
	
	private static abstract class AbstractArgumentList implements IList {
		// caching the hash on the list level (and nowhere else)
		// to balance memory usage and making sure computing hash codes
		// for any parse tree node is in (amortized) constant time
		protected int hash = 0;
		
		protected abstract IList asNormal();
		
		@Override
		public Type getType() {
			return tf.listType(getElementType());
		}
		
		@Override
		public boolean contains(IValue e) {
			for (int i = 0; i < length(); i++) {
				if (get(i).equals(e)) {
					return true;
				}
			}
			
			return false;
		}
		
		@Override
		public String toString() {
			return defaultToString();
		}
		
		@Override
		public boolean equals(Object other) {
		    return defaultEquals(other);
		}
		
		@Override
        public boolean match(IValue other) {
            if (other instanceof IList) {
                IList o = (IList) other;
                if (o.length() == length()) {
                    for (int i = 0; i < length(); i++) {
                        if (!o.get(i).match(get(i))) {
                            return false;
                        }
                    }
                    
                    return true;
                }
            }
            
            return false;
        }

		@Override
		public int hashCode(){
			if (hash != 0) {
				return hash;
			}

			Iterator<IValue> iterator = iterator();
			while(iterator.hasNext()){
				IValue element = iterator.next();
				hash = (hash << 1) ^ element.hashCode();
			}

			return hash;
		}
		
		@Override
		public Iterator<IValue> iterator() {
			return new Iterator<IValue>() {
				private int count = 0;
				@Override
				public boolean hasNext() {
					return count < length();
				}

				@Override
				public IValue next() {
					count++;
					return get(count - 1);
				}
			};
		}
		
		@Override
		public Type getElementType() {
			Type lub = tf.voidType();
			for (IValue elem : this) {
				if (lub == Tree) {
					// it's not going to be wider anyway
					return Tree;
				}
				lub = lub.lub(elem.getType());
			}
			return lub;
		}

		@Override
		public IList reverse() {
			return asNormal().reverse();
		}

		@Override
		public IList append(IValue e) {
			return asNormal().append(e);
		}

		@Override
		public IList insert(IValue e) {
			return asNormal().insert(e);
		}

		@Override
		public IList concat(IList o) {
			return asNormal().concat(o);
		}
		
		@Override
		public IList shuffle(Random rand) {
			return asNormal().shuffle(rand);
		}

		@Override
		public IList put(int i, IValue e) throws FactTypeUseException,
				IndexOutOfBoundsException {
			return asNormal().put(i,e);
		}

		@Override
		public IList replace(int first, int second, int end, IList repl)
				throws FactTypeUseException, IndexOutOfBoundsException {
			return asNormal().replace(first, second, end, repl);
		}


		@Override
		public IList sublist(int offset, int length) {
			return asNormal().sublist(offset,length);
		}

		public boolean isEmpty() {
			return false;
		}

		@Override
		public IList delete(IValue e) {
			return asNormal().delete(e);
		}

		@Override
		public IList delete(int i) {
			return asNormal().delete(i);
		}

		@Override
		public IList product(IList l) {
			return asNormal().product(l);
		}

		@Override
		public IList intersect(IList l) {
			return asNormal().intersect(l);
		}

		@Override
		public IList subtract(IList l) {
			return asNormal().subtract(l);
		}

		@Override
		public boolean isSubListOf(IList l) {
			return asNormal().isSubListOf(l);
		}

		@Override
		public boolean isRelation() {
			return false;
		}

		@Override
		public IRelation<IList> asRelation() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public IListWriter writer() {
		    return IRascalValueFactory.getInstance().listWriter();
		}
	}
	
	private static class ArrayArgumentList extends AbstractArgumentList {
		private final IValue[] array;

		public ArrayArgumentList(IValue[] array) {
			this.array = array;
		}
		
		@Override
		public int length() {
			return array.length;
		}

		@Override
		public IValue get(int i) throws IndexOutOfBoundsException {
			return array[i];
		}

		@Override
		protected IList asNormal() {
			return getInstance().list(array);
		}
	}
	
	@Deprecated
	private static class ArrayListArgumentList extends AbstractArgumentList {
		private final ArrayList<ITree> list;

		public ArrayListArgumentList(ArrayList<ITree> list) {
			this.list = list;
		}
		
		@Override
		public int length() {
			return list.size();
		}

		@Override
		public IValue get(int i) throws IndexOutOfBoundsException {
			return list.get(i);
		}

		@Override
		protected IList asNormal() {
			IListWriter w = getInstance().listWriter();
			for (int i = 0; i < list.size(); i++) {
				w.append(list.get(i));
			}
			return w.done();
		}
		
	}
	
	private static class Appl0 extends AbstractAppl {
		private static final IList EMPTY_LIST = getInstance().listWriter().done();
		public Appl0(IConstructor production) {
			super(production);
		}

		@Override
		public IList getArgs() {
			return EMPTY_LIST;
		}
		
		@Override
		public int hashCode() {
			return 17 
					+ 13 * production.hashCode();
		}
	}
	
	private static class ApplN extends AbstractAppl {
		private final IList args;

		public ApplN(IConstructor production, IList args) {
			super(production);
			this.args = args;
		}

		@Override
		public IList getArgs() {
			return args;
		}
		
		@Override
		public int hashCode() {
			return 3 
					+ 17 * production.hashCode() 
					+ 91 * args.hashCode()
					;
		}
	}
	
	private static class Appl1 extends AbstractAppl {
		private final IValue arg0;

		public Appl1(IConstructor production, IValue arg) {
			super(production);
			this.arg0 = arg;
		}

		@Override
		public IList getArgs() {
			return new AbstractArgumentList() {
				@Override
				public int length() {
					return 1;
				}
				
				@Override
				public IValue get(int i) throws IndexOutOfBoundsException {
					switch(i) {
					case 0: return arg0;
					default: throw new IndexOutOfBoundsException();
					}
				}
				
				@Override
				protected IList asNormal() {
					return getInstance().list(arg0);
				}
			};
		}
		
		@Override
		public int hashCode() {
			return 17 
					+ 13 * production.hashCode() 
					+ 17 * arg0.hashCode()
					;
		}
	}
	
	private static class Appl2 extends AbstractAppl {
		private final IValue arg0;
		private final IValue arg1;

		public Appl2(IConstructor production, IValue arg0, IValue arg1) {
			super(production);
			this.arg0 = arg0;
			this.arg1 = arg1;
		}

		@Override
		public IList getArgs() {
			return new AbstractArgumentList() {
				@Override
				public int length() {
					return 2;
				}
				
				@Override
				public IValue get(int i) throws IndexOutOfBoundsException {
					switch(i) {
					case 0: return arg0;
					case 1: return arg1;
					default: throw new IndexOutOfBoundsException();
					}
				}
				
				@Override
				protected IList asNormal() {
					return getInstance().list(arg0, arg1);
				}
			};
		}
		
		@Override
		public int hashCode() {
			return 23 
					+ 13 * production.hashCode() 
					+ 17 * arg0.hashCode()
					+ 23 * arg1.hashCode()
					;
		}
	}
	
	private static class Appl3 extends AbstractAppl {
		private final IValue arg0;
		private final IValue arg1;
		private final IValue arg2;

		public Appl3(IConstructor production, IValue arg0, IValue arg1, IValue arg2) {
			super(production);
			this.arg0 = arg0;
			this.arg1 = arg1;
			this.arg2 = arg2;
		}

		@Override
		public IList getArgs() {
			return new AbstractArgumentList() {
				@Override
				public int length() {
					return 3;
				}
				
				@Override
				public IValue get(int i) throws IndexOutOfBoundsException {
					switch(i) {
					case 0: return arg0;
					case 1: return arg1;
					case 2: return arg2;
					default: throw new IndexOutOfBoundsException();
					}
				}
				
				@Override
				protected IList asNormal() {
					return getInstance().list(arg0, arg1, arg2);
				}
			};
		}
		
		@Override
		public int hashCode() {
			return 29 
					+ 13 * production.hashCode() 
					+ 17 * arg0.hashCode()
					+ 23 * arg1.hashCode()
					+ 29 * arg2.hashCode()
					;
		}
	}
	
	private static class Appl4 extends AbstractAppl {
		private final IValue arg0;
		private final IValue arg1;
		private final IValue arg2;
		private final IValue arg3;

		public Appl4(IConstructor production, IValue arg0, IValue arg1, IValue arg2, IValue arg3) {
			super(production);
			this.arg0 = arg0;
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
		}

		@Override
		public IList getArgs() {
			return new AbstractArgumentList() {
				@Override
				public int length() {
					return 4;
				}
				
				@Override
				public IValue get(int i) throws IndexOutOfBoundsException {
					switch(i) {
					case 0: return arg0;
					case 1: return arg1;
					case 2: return arg2;
					case 3: return arg3;
					default: throw new IndexOutOfBoundsException();
					}
				}
				
				@Override
				protected IList asNormal() {
					return getInstance().list(arg0, arg1, arg2, arg3);
				}
			};
		}
		
		@Override
		public int hashCode() {
			return 31 
					+ 13 * production.hashCode() 
					+ 17 * arg0.hashCode()
					+ 23 * arg1.hashCode()
					+ 29 * arg2.hashCode()
					+ 31 * arg3.hashCode()
					;
		}
	}
	
	private static class Appl5 extends AbstractAppl {
		private final IValue arg0;
		private final IValue arg1;
		private final IValue arg2;
		private final IValue arg3;
		private final IValue arg4;

		public Appl5(IConstructor production, IValue arg0, IValue arg1, IValue arg2, IValue arg3, IValue arg4) {
			super(production);
			this.arg0 = arg0;
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
			this.arg4 = arg4;
		}

		@Override
		public IList getArgs() {
			return new AbstractArgumentList() {
				@Override
				public int length() {
					return 5;
				}
				
				@Override
				public IValue get(int i) throws IndexOutOfBoundsException {
					switch(i) {
					case 0: return arg0;
					case 1: return arg1;
					case 2: return arg2;
					case 3: return arg3;
					case 4: return arg4;
					default: throw new IndexOutOfBoundsException();
					}
				}
				
				@Override
				protected IList asNormal() {
					return getInstance().list(arg0, arg1, arg2, arg3, arg4);
				}
			};
		}
		
		@Override
		public int hashCode() {
			return 31 
					+ 13 * production.hashCode() 
					+ 17 * arg0.hashCode()
					+ 23 * arg1.hashCode()
					+ 29 * arg2.hashCode()
					+ 31 * arg3.hashCode()
					+ 37 * arg4.hashCode()
					;
		}
	}
	
	private static class Appl6 extends AbstractAppl {
		private final IValue arg0;
		private final IValue arg1;
		private final IValue arg2;
		private final IValue arg3;
		private final IValue arg4;
		private final IValue arg5;

		public Appl6(IConstructor production, IValue arg0, IValue arg1, IValue arg2, IValue arg3, IValue arg4, IValue arg5) {
			super(production);
			this.arg0 = arg0;
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
			this.arg4 = arg4;
			this.arg5 = arg5;
		}

		@Override
		public IList getArgs() {
			return new AbstractArgumentList() {
				@Override
				public int length() {
					return 6;
				}
				
				@Override
				public IValue get(int i) throws IndexOutOfBoundsException {
					switch(i) {
					case 0: return arg0;
					case 1: return arg1;
					case 2: return arg2;
					case 3: return arg3;
					case 4: return arg4;
					case 5: return arg5;
					default: throw new IndexOutOfBoundsException();
					}
				}
				
				@Override
				protected IList asNormal() {
					return getInstance().list(arg0, arg1, arg2, arg3, arg4, arg5);
				}
			};
		}
		
		@Override
		public int hashCode() {
			return 31 
					+ 13 * production.hashCode() 
					+ 17 * arg0.hashCode()
					+ 23 * arg1.hashCode()
					+ 29 * arg2.hashCode()
					+ 31 * arg3.hashCode()
					+ 37 * arg4.hashCode()
					+ 41 * arg5.hashCode()
					;
		}
	}

	private static class Appl7 extends AbstractAppl {
		private final IValue arg0;
		private final IValue arg1;
		private final IValue arg2;
		private final IValue arg3;
		private final IValue arg4;
		private final IValue arg5;
		private final IValue arg6;

		public Appl7(IConstructor production, IValue arg0, IValue arg1, IValue arg2, IValue arg3, IValue arg4, IValue arg5, IValue arg6) {
			super(production);
			this.arg0 = arg0;
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
			this.arg4 = arg4;
			this.arg5 = arg5;
			this.arg6 = arg6;
		}

		@Override
		public IList getArgs() {
			return new AbstractArgumentList() {
				@Override
				public int length() {
					return 7;
				}
				
				@Override
				public IValue get(int i) throws IndexOutOfBoundsException {
					switch(i) {
					case 0: return arg0;
					case 1: return arg1;
					case 2: return arg2;
					case 3: return arg3;
					case 4: return arg4;
					case 5: return arg5;
					case 6: return arg6;
					default: throw new IndexOutOfBoundsException();
					}
				}
				
				@Override
				protected IList asNormal() {
					return getInstance().list(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
				}
			};
		}
		
		@Override
		public int hashCode() {
			return 31 
					+ 13 * production.hashCode() 
					+ 17 * arg0.hashCode()
					+ 23 * arg1.hashCode()
					+ 29 * arg2.hashCode()
					+ 31 * arg3.hashCode()
					+ 37 * arg4.hashCode()
					+ 41 * arg5.hashCode()
					+ 43 * arg6.hashCode()
					;
		}
	}

	// please put additional methods above the nested classes
}
