package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.impl.AbstractValueFactoryAdapter;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.values.LocalSharingValueFactory;
import org.rascalmpl.values.uptr.Factory;

/**
 * Experimental wrapper class for serializable IValues.
 * Your mileage may vary due to use of the BinaryWriter.
 */
public class SerializableRascalValue<T extends IValue> implements Serializable {
	private static final long serialVersionUID = -5507315290306212326L;
	private static IValueFactory vf;
	private static TypeReifier tr;
	private T value;
	private static TypeStore store;
	
	public static void initSerialization(IValueFactory vfactory, TypeStore ts){
		vf = vfactory;
		store = ts; 
		store.lookupAbstractDataType("RuntimeException");
		tr = new TypeReifier(vf);
	}
	
	public SerializableRascalValue(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	public void write(OutputStream out) throws IOException {
		new ObjectOutputStream(out).writeObject(this);
	}
	
	@SuppressWarnings("unchecked")
	public static <U extends IValue> SerializableRascalValue<U> read(InputStream in) throws IOException {
		try {
			return (SerializableRascalValue<U>) new ObjectInputStream(in).readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} 
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		String factoryName = vf.getClass().getName();
		out.write("factory".getBytes());
		out.write(':');
		out.writeInt(factoryName.length());
		out.write(':');
		out.write(factoryName.getBytes("UTF8"));
		out.write(':');
		
		// Binary version:
		//new BinaryValueWriter().write(value, out);
		
		// Text version (generates an extra string!):
		StringWriter sw = new StringWriter();
		new StandardTextWriter().write(value, sw);
		//System.out.println("writeObject: " + value + " and " + sw.toString());
		out.writeObject(sw.toString());
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		try {
			in.read(new byte["factory".length()], 0, "factory".length());
			in.read(); // ':'
			int length = in.readInt();
			in.read(); // ':'
			byte[] factoryName = new byte[length];
			in.read(factoryName, 0, length);
			in.read(); // ':'
			Class<?> clazz = getClass().getClassLoader().loadClass(new String(factoryName, "UTF8"));
			this.vf = (IValueFactory) clazz.getMethod("getInstance").invoke(null, new Object[0]);
			
			// Binary version;	
			//this.value = (T) new BinaryValueReader().read(vf, in);
			
			// Text version  (generates an extra string!):
			String s = (String) in.readObject();
			try (StringReader sr = new StringReader(s)) {
				this.value = (T) new StandardTextReader().read(new RascalValuesValueFactory(), store, TypeFactory.getInstance().valueType(), sr);
				//System.out.println("readObject: " + this.value.getType() + ",  " + this.value.getClass() + ": " + this.value);
			} 
			catch (FactTypeUseException e) {
				throw new IOException(e.getMessage());
			} 
			catch (IOException e) {
				throw new IOException(e.getMessage());
			}
		}
		catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | ClassCastException e) {
			throw new IOException("Could not load IValueFactory", e);
		}
	}
	
	private class RascalValuesValueFactory extends AbstractValueFactoryAdapter {
		public RascalValuesValueFactory() {
			super(vf);
		}
		
//		@Override
//		public IConstructor constructor(Type constructor) {
//			return super.constructor(constructor);
//		}
//		
//		@Override
//		public IConstructor constructor(Type constructor, IValue... children) {
//			return super.constructor(constructor, children);
//		}
		
		@Override
		public IConstructor constructor(Type constructor, IValue[] children,
				Map<String, IValue> keyArgValues) {
			IConstructor res = specializeType(constructor.getName(), children, keyArgValues);
			return res != null ? res: super.constructor(constructor, children, keyArgValues);
		}
		
		public IConstructor specializeType(String name, IValue[] children,
				Map<String, IValue> keyArgValues) {
			//System.out.println(constructor.getName());
			int arity = children.length;
			switch(name){
			
			case "type":	
				//return vf.constructor(Factory.Type_Reified, children[0], children[1]);
				// public static final Type Type_Reified = tf.constructor(uptr, Type, "type", Symbol, "symbol", tf.mapType(Symbol , Production), "definitions");
				if(children.length == 2
					&& children[0].getType().isSubtypeOf(Factory.Type_Reified.getFieldType(0))
					&& children[1].getType().isSubtypeOf(Factory.Type_Reified.getFieldType(1))) {
					
					java.util.Map<Type,Type> bindings = new HashMap<Type,Type>();
					bindings.put(Factory.TypeParam, tr.symbolToType((IConstructor) children[0], (IMap) children[1]));
				
					return vf.constructor(Factory.Type_Reified.instantiate(bindings), children[0], children[1]);
				} else { 
					return null;
				}

			case "appl":	
				if(arity == 2) return vf.constructor(Factory.Tree_Appl, children[0], children[1]); else break;
				// public static final Type Tree_Appl = tf.constructor(uptr, Tree, "appl", Production, "prod", tf.listType(Tree), "args");
			
			case "cycle":	
				if(arity == 2) return vf.constructor(Factory.Tree_Cycle, children[0], children[1]);	else break;
				// public static final Type Tree_Cycle = tf.constructor(uptr, Tree, "cycle", Symbol, "symbol", tf.integerType(), "cycleLength");

			case "amb":
				if(arity == 1) return vf.constructor(Factory.Tree_Amb, children[0]); else break;
				// public static final Type Tree_Amb = tf.constructor(uptr, Tree, "amb", Alternatives, "alternatives");
			
			case "char":	
				if(arity == 1) return vf.constructor(Factory.Tree_Char, children[0]); else break;
				// public static final Type Tree_Char = tf.constructor(uptr, Tree, "char", tf.integerType(), "character");
			
			case "prod":	
				if(children.length == 3){
					return vf.constructor(Factory.Production_Default, children[0], children[1], children[2]);
					// public static final Type Production_Default = tf.constructor(uptr, Production, "prod", Symbol, "def", tf.listType(Symbol), "symbols",  tf.setType(Attr), "attributes");
				} else if(children.length == 4){
					return vf.constructor(Factory.Symbol_Prod, children[0], children[1], children[2], children[3]);
					//	public static final Type Symbol_Prod = tf.constructor(uptr, Symbol, "prod", Symbol, "def", str, "name", tf.listType(Symbol), "symbols",  tf.setType(Attr), "attributes");
				} else {
					return null;
				}
			
			case "regular":	return vf.constructor(Factory.Production_Regular, children[0]);
			// public static final Type Production_Regular = tf.constructor(uptr, Production, "regular", Symbol, "def");
			
			case "error":	return vf.constructor(Factory.Production_Error, children[0], children[1]);
			// public static final Type Production_Error = tf.constructor(uptr, Production, "error", Production, "prod", tf.integerType(), "dot");
			
			case "skipped":	return vf.constructor(Factory.Production_Skipped);
			// public static final Type Production_Skipped = tf.constructor(uptr, Production, "skipped");
			
			case "cons":	
					if(children.length >= 4){
						return vf.constructor(Factory.Production_Cons, children[0], children[1], children[2], children[3]);	
						// public static final Type Production_Cons = tf.constructor(uptr, Production, "cons", Symbol, "def", tf.listType(Symbol), "symbols", tf.listType(Symbol), "kwTypes", tf.setType(Attr), "attributes");
					} else if (children.length == 3){
						return vf.constructor(Factory.Symbol_Cons, children[0], children[1], children[2]);
						// public static final Type Symbol_Cons = tf.constructor(uptr, Symbol, "cons", Symbol, "adt", str, "name", tf.listType(Symbol), "parameters");
					} else {
						return null;
					}
			case "func":	
					if(children.length >= 4){
							return vf.constructor(Factory.Production_Func, children[0], children[1], children[2], children[3]);	
							// public static final Type Production_Func = tf.constructor(uptr, Production, "func", Symbol, "def", tf.listType(Symbol), "symbols", tf.listType(Symbol), "kwTypes", tf.setType(Attr), "attributes");
					} else if(children.length == 2){
						return vf.constructor(Factory.Symbol_Func, children[0], children[1]);
						//	public static final Type Symbol_Func = tf.constructor(uptr, Symbol, "func", Symbol, "ret", tf.listType(Symbol), "parameters")
					} else {
						return null;
					}
					
			case "choice":	return vf.constructor(Factory.Production_Choice, children[0], children[1]);	
			// public static final Type Production_Choice = tf.constructor(uptr, Production, "choice", Symbol, "def", tf.setType(Production), "alternatives");

			case "priority":	return vf.constructor(Factory.Production_Priority, children[0], children[1]);	
			// public static final Type Production_Priority = tf.constructor(uptr, Production, "priority", Symbol, "def", tf.listType(Production), "choices");

			case "associativity":	return vf.constructor(Factory.Production_Associativity, children[0], children[1], children[2]);	
			// public static final Type Production_Associativity = tf.constructor(uptr, Production, "associativity", Symbol, "def", Associativity, "assoc", tf.setType(Production), "alternatives");

			case "assoc":	
					if(children.length == 1){
						return vf.constructor(Factory.Attr_Assoc, children[0]);	
						//	public static final Type Attr_Assoc = tf.constructor(uptr, Attr, "assoc", Associativity, "assoc");
					} else if(children.length == 0){
						return vf.constructor(Factory.Associativity_Assoc);
						//	public static final Type Associativity_Assoc = tf.constructor(uptr, Associativity, "assoc");
					} else {
						return null;
					}
			
			case "tag":	return vf.constructor(Factory.Attr_Tag, children[0]);	
			//	public static final Type Attr_Tag = tf.constructor(uptr, Attr, "tag", tf.valueType(), "tag");
			
			case "bracket":	return vf.constructor(Factory.Attr_Bracket);
			//	public static final Type Attr_Bracket = tf.constructor(uptr, Attr, "bracket");

			case "left":	
				return vf.constructor(Factory.Associativity_Left);
			//	public static final Type Associativity_Left = tf.constructor(uptr, Associativity, "left");
			
			case "right":	return vf.constructor(Factory.Associativity_Right);
			//	public static final Type Associativity_Right = tf.constructor(uptr, Associativity, "right");
			
			//case "assoc":	handled above
			//	public static final Type Associativity_Assoc = tf.constructor(uptr, Associativity, "assoc");
			
			case "non-assoc":	return vf.constructor(Factory.Associativity_NonAssoc);
			//	public static final Type Associativity_NonAssoc = tf.constructor(uptr, Associativity, "non-assoc");
			
			case "follow":	return vf.constructor(Factory.Condition_Follow, children[0]);	
			//	public static final Type Condition_Follow = tf.constructor(uptr, Condition, "follow", Symbol, "symbol");
			
			case "not-follow":	return vf.constructor(Factory.Condition_NotFollow, children[0]);
			//	public static final Type Condition_NotFollow = tf.constructor(uptr, Condition, "not-follow", Symbol, "symbol");

			case "precede":	return vf.constructor(Factory.Condition_Precede, children[0]);
			//	public static final Type Condition_Precede = tf.constructor(uptr, Condition, "precede", Symbol, "symbol");
			
			case "not-precede":	return vf.constructor(Factory.Condition_NotPrecede, children[0]);
			//	public static final Type Condition_NotPrecede = tf.constructor(uptr, Condition, "not-precede", Symbol, "symbol");
			
			case "delete":	return vf.constructor(Factory.Condition_Delete, children[0]);
			//	public static final Type Condition_Delete = tf.constructor(uptr, Condition, "delete", Symbol, "symbol");
			
			case "end-of-line":	return vf.constructor(Factory.Condition_EndOfLine);
			//	public static final Type Condition_EndOfLine = tf.constructor(uptr, Condition, "end-of-line");
			
			case "begin-of-line":	return vf.constructor(Factory.Condition_StartOfLine);
			//	public static final Type Condition_StartOfLine = tf.constructor(uptr, Condition, "begin-of-line");
			
			case "column":	return vf.constructor(Factory.Condition_AtColumn, children[0]);
			//	public static final Type Condition_AtColumn = tf.constructor(uptr, Condition, "at-column", tf.integerType(), "column");
			
			case "except":	return vf.constructor(Factory.Condition_Except, children[0]);
			//	public static final Type Condition_Except = tf.constructor(uptr, Condition, "except", str, "label");
	
			case "label":	return vf.constructor(Factory.Symbol_Label, children[0], children[1]);
			//	public static final Type Symbol_Label = tf.constructor(uptr, Symbol, "label", str, "name", Symbol, "symbol");
			
			case "start":	return vf.constructor(Factory.Symbol_Start_Sort, children[0]);
			//	public static final Type Symbol_Start_Sort = tf.constructor(uptr, Symbol, "start", Symbol, "symbol");
			
			////	public static final Type Symbol_START = tf.constructor(uptr, Symbol, "START");

			case "lit":	return vf.constructor(Factory.Symbol_Lit, children[0]);
			//	public static final Type Symbol_Lit = tf.constructor(uptr, Symbol, "lit", str, "string");

			case "cilit":	return vf.constructor(Factory.Symbol_CiLit, children[0]);
			//	public static final Type Symbol_CiLit = tf.constructor(uptr, Symbol, "cilit", str, "string");

			case "empty":	return vf.constructor(Factory.Symbol_Empty);
			//	public static final Type Symbol_Empty = tf.constructor(uptr, Symbol, "empty");

			case "seq":	return vf.constructor(Factory.Symbol_Seq, children[0]);
			//	public static final Type Symbol_Seq = tf.constructor(uptr, Symbol, "seq", tf.listType(Symbol), "symbols");

			case "opt":	return vf.constructor(Factory.Symbol_Opt, children[0]);
			//	public static final Type Symbol_Opt = tf.constructor(uptr, Symbol, "opt", Symbol, "symbol");

			case "alt":	return vf.constructor(Factory.Symbol_Alt, children[0]);
			//	public static final Type Symbol_Alt = tf.constructor(uptr, Symbol, "alt", tf.setType(Symbol), "alternatives");
			
			case "sort":	return vf.constructor(Factory.Symbol_Sort, children[0]);
			//	public static final Type Symbol_Sort = tf.constructor(uptr, Symbol, "sort", str, "name");
			
			case "lex":	return vf.constructor(Factory.Symbol_Lex, children[0]);
			//	public static final Type Symbol_Lex = tf.constructor(uptr, Symbol, "lex", str, "name");
			
			case "keywords": return vf.constructor(Factory.Symbol_Keyword, children[0]);
			//	public static final Type Symbol_Keyword = tf.constructor(uptr, Symbol, "keywords", str, "name");
			
			case "meta": return vf.constructor(Factory.Symbol_Meta, children[0]);
			//	public static final Type Symbol_Meta = tf.constructor(uptr, Symbol, "meta", Symbol, "symbol");
			
			case "conditional": return vf.constructor(Factory.Symbol_Conditional, children[0], children[1]);
			//	public static final Type Symbol_Conditional = tf.constructor(uptr, Symbol, "conditional", Symbol, "symbol", tf.setType(Condition), "conditions");
			
			case "iter-seps": return vf.constructor(Factory.Symbol_IterSepX, children[0], children[1]);
			//	public static final Type Symbol_IterSepX = tf.constructor(uptr, Symbol, "iter-seps", Symbol, "symbol", tf.listType(Symbol), "separators");

			case "iter-star-seps": return vf.constructor(Factory.Symbol_IterStarSepX, children[0], children[1]);
			//	public static final Type Symbol_IterStarSepX = tf.constructor(uptr, Symbol, "iter-star-seps", Symbol, "symbol", tf.listType(Symbol), "separators");

			case "iter": return vf.constructor(Factory.Symbol_IterPlus, children[0]);
			//	public static final Type Symbol_IterPlus = tf.constructor(uptr, Symbol, "iter", Symbol, "symbol");

			case "iter-star": return vf.constructor(Factory.Symbol_IterStar, children[0]);
			//	public static final Type Symbol_IterStar = tf.constructor(uptr, Symbol, "iter-star", Symbol, "symbol");

			case "parameterized-sort": 
				return vf.constructor(Factory.Symbol_ParameterizedSort, children[0], children[1]);
			//	public static final Type Symbol_ParameterizedSort = tf.constructor(uptr, Symbol, "parameterized-sort", str, "name", tf.listType(Symbol), "parameters");

			case "parameterized-lex": return vf.constructor(Factory.Symbol_ParameterizedLex, children[0], children[1]);
			//	public static final Type Symbol_ParameterizedLex = tf.constructor(uptr, Symbol, "parameterized-lex", str, "name", tf.listType(Symbol), "parameters");

			case "parameter": 
				// TODO here is a problem, how to decide between the two options? Can we renamen the second to "bound-parameter"?
					if(true){
						return vf.constructor(Factory.Symbol_Parameter, children[0], children[1]);
						//	public static final Type Symbol_Parameter = tf.constructor(uptr, Symbol, "parameter", str, "name", Symbol, "bound");
					} else {
						return vf.constructor(Factory.Symbol_BoundParameter, children[0], children[1]);
						//	public static final Type Symbol_BoundParameter = tf.constructor(uptr, Symbol, "parameter", str , "name", Symbol, "bound");
					}
	
			case "layouts": return vf.constructor(Factory.Symbol_LayoutX, children[0]);
			//	public static final Type Symbol_LayoutX = tf.constructor(uptr, Symbol, "layouts", str, "name");

			case "char-class": return vf.constructor(Factory.Symbol_CharClass, children[0]);
			//	public static final Type Symbol_CharClass = tf.constructor(uptr, Symbol, "char-class", tf.listType(CharRange), "ranges");

			case "int": return vf.constructor(Factory.Symbol_Int);
			//	public static final Type Symbol_Int = tf.constructor(uptr, Symbol, "int");
			
			case "rat": return vf.constructor(Factory.Symbol_Rat);
			//	public static final Type Symbol_Rat = tf.constructor(uptr, Symbol, "rat");
			
			case "bool": return vf.constructor(Factory.Symbol_Bool);
			//	public static final Type Symbol_Bool = tf.constructor(uptr, Symbol, "bool");
			
			case "real": return vf.constructor(Factory.Symbol_Real);	
			//	public static final Type Symbol_Real = tf.constructor(uptr, Symbol, "real");
			
			case "str": return vf.constructor(Factory.Symbol_Str);
			//	public static final Type Symbol_Str = tf.constructor(uptr, Symbol,  "str");
			
			case "node": return vf.constructor(Factory.Symbol_Node);
			//	public static final Type Symbol_Node = tf.constructor(uptr, Symbol,  "node");
			
			case "num": return vf.constructor(Factory.Symbol_Num);
			//	public static final Type Symbol_Num = tf.constructor(uptr, Symbol,  "num");
			
			case "void": return vf.constructor(Factory.Symbol_Void);
			//	public static final Type Symbol_Void = tf.constructor(uptr, Symbol, "void");
			
			case "value": return vf.constructor(Factory.Symbol_Value);
			//	public static final Type Symbol_Value = tf.constructor(uptr, Symbol,  "value");
			
			case "loc": return vf.constructor(Factory.Symbol_Loc);
			//	public static final Type Symbol_Loc = tf.constructor(uptr, Symbol,  "loc");
			
			case "datetime": return vf.constructor(Factory.Symbol_Datetime);
			//	public static final Type Symbol_Datetime = tf.constructor(uptr, Symbol,  "datetime");
			
			case "set": return vf.constructor(Factory.Symbol_Set, children[0]);
			//	public static final Type Symbol_Set = tf.constructor(uptr, Symbol, "set", Symbol, "symbol");
			
			case "rel": return vf.constructor(Factory.Symbol_Rel, children[0]);			
			//	public static final Type Symbol_Rel = tf.constructor(uptr, Symbol, "rel", tf.listType(Symbol), "symbols");
			
			case "lrel": return vf.constructor(Factory.Symbol_ListRel, children[0]);				
			//	public static final Type Symbol_ListRel = tf.constructor(uptr, Symbol, "lrel", tf.listType(Symbol), "symbols");
			
			case "tuple": return vf.constructor(Factory.Symbol_Tuple, children[0]);
			//	public static final Type Symbol_Tuple = tf.constructor(uptr, Symbol, "tuple", tf.listType(Symbol), "symbols");

			case "list": return vf.constructor(Factory.Symbol_List, children[0]);
			//	public static final Type Symbol_List = tf.constructor(uptr, Symbol, "list", Symbol, "symbol");

			case "map": return vf.constructor(Factory.Symbol_Map, children[0], children[1]);
			//	public static final Type Symbol_Map = tf.constructor(uptr, Symbol, "map", Symbol, "from", Symbol, "to");

			case "bag": return vf.constructor(Factory.Symbol_Bag, children[0]);
			//	public static final Type Symbol_Bag = tf.constructor(uptr, Symbol, "bag", Symbol, "symbol");

			case "adt": 
				return vf.constructor(Factory.Symbol_Adt, children[0], children[1]);
			//	public static final Type Symbol_Adt = tf.constructor(uptr, Symbol, "adt", str, "name", tf.listType(Symbol), "parameters");

			case "reified": 
					return vf.constructor(Factory.Symbol_ReifiedType, children[0]);
				
			//	public static final Type Symbol_ReifiedType = tf.constructor(uptr, Symbol, "reified", Symbol, "symbol");
			
			//	case "func": handled above
			//	public static final Type Symbol_Func = tf.constructor(uptr, Symbol, "func", Symbol, "ret", tf.listType(Symbol), "parameters");

			case "alias": return vf.constructor(Factory.Symbol_Alias, children[0], children[1]);
			//	public static final Type Symbol_Alias = tf.constructor(uptr, Symbol, "alias", str, "name", tf.listType(Symbol), "parameters", Symbol, "aliased");

			//case "cons": handled above
			//	public static final Type Symbol_Cons = tf.constructor(uptr, Symbol, "cons", Symbol, "adt", str, "name", tf.listType(Symbol), "parameters");
			
			// case "parameter": handled above
			//	public static final Type Symbol_BoundParameter = tf.constructor(uptr, Symbol, "parameter", str , "name", Symbol, "bound");
			
			//	// Two constructors introduced by the type checker
			
			case "overloaded": return vf.constructor(Factory.Symbol_Overloaded, children[0], children[1]);
			//	public static final Type Symbol_Overloaded = tf.constructor(uptr, Symbol, "overloaded", tf.setType(Symbol), "alternatives", tf.setType(Symbol), "defaults");

			//case "prod": handled above
			//	public static final Type Symbol_Prod = tf.constructor(uptr, Symbol, "prod", Symbol, "def", str, "name", tf.listType(Symbol), "symbols",  tf.setType(Attr), "attributes");

			case "from": return vf.constructor(Factory.CharRange_Single, children[0]);
			//	public static final Type CharRange_Single = tf.constructor(uptr, CharRange, "from", tf.integerType()); // TODO: can go when older parser is gone
			
			case "range": return vf.constructor(Factory.CharRange_Range, children[0], children[1]);
			//	public static final Type CharRange_Range = tf.constructor(uptr, CharRange, "range", tf.integerType(), "begin", tf.integerType(), "end");

//	TODO: handle these
//	private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
//	public static final IValue Attribute_Assoc_Left = vf.constructor(Attr_Assoc, vf.constructor(Associativity_Left));
//	public static final IValue Attribute_Assoc_Right = vf.constructor(Attr_Assoc, vf.constructor(Associativity_Right));
//	public static final IValue Attribute_Assoc_Non_Assoc = vf.constructor(Attr_Assoc, vf.constructor(Associativity_NonAssoc));
//	public static final IValue Attribute_Assoc_Assoc = vf.constructor(Attr_Assoc, vf.constructor(Associativity_Assoc));
//	public static final IValue Attribute_Bracket = vf.constructor(Attr_Bracket);
			
			}
			return null; //super.constructor(constructor, children, keyArgValues);
		}
		
		@Override
		public IConstructor constructor(Type constructor, Map<String, IValue> annotations,
				IValue... children) {
			return super.constructor(constructor, annotations, children);
		}
		
		
//		@Override
//		public INode node(String name, IValue... children) {
//			IConstructor res = specializeType(name, children);
//			return res != null ? res: vf.node(name, children);
//		}

//		private IConstructor specializeType(String name, IValue... children) {
//			if("type".equals(name)){
//				System.out.println(name + ", " + children[0]);
//			}
//			if ("type".equals(name) 
//					&& children.length == 2
//					&& children[0].getType().isSubtypeOf(Factory.Type_Reified.getFieldType(0))
//					&& children[1].getType().isSubtypeOf(Factory.Type_Reified.getFieldType(1))) 
//				{
//				java.util.Map<Type,Type> bindings = new HashMap<Type,Type>();
//				bindings.put(Factory.TypeParam, tr.symbolToType((IConstructor) children[0], (IMap) children[1]));
//				
//				return vf.constructor(Factory.Type_Reified.instantiate(bindings), children[0], children[1]);
//			}
//			
//			if("overloaded".equals(name) && children.length == 2){
//				return vf.constructor(Factory.Symbol_Overloaded, children[0], children[1]);
//			}
//			
//			return null;
//		}
		
//		@Override
//		public INode node(String name, Map<String, IValue> annotations,
//				IValue... children) throws FactTypeUseException {
//			IConstructor res = specializeType(name, children);
//			return res != null ? res: vf.node(name, annotations, children);
//		}
		
		@Override
		public INode node(String name, IValue[] children,
				Map<String, IValue> keyArgValues) throws FactTypeUseException {
			IConstructor res = specializeType(name, children, keyArgValues);
			return res != null ? res: vf.node(name, children, keyArgValues);
		}
	}

}
