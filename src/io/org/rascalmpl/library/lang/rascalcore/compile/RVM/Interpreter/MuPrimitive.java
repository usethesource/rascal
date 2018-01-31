package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.rascalmpl.interpreter.types.RascalType;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.DescendantDescriptor;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.DescendantMatchIterator;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.impl.AnnotatedConstructorFacade;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.RascalValueFactory.AnnotatedAmbFacade;
import org.rascalmpl.values.uptr.TreeAdapter;

/**
 * MuPrimitive defines all primitives that are necessary for running muRascal programs.
 * This includes all operations with at least one operand that is not an IValue:
 * 		- mint	(Java Integer)
 * 		- mstr  (Java String)
 * 		- mset	(Java HashSet<IValue>)
 * 		- mmap	(Java HashMap of various types, e.g., HashMap<String, IValue> and HashMap<String, Entry<String, IValue>>)
 * 		- array	(Java Object[])
 * 
 * All operations with only IValues as arguments are defined in RascalPrimitive
 * 
 * Each MuPrimitive has an execute function with as arguments
 * - stack in the current stack frame
 * - stackpointer (sp) in that stack
 * - arity of the primitive
 * 
 * and returns a new values for the stackpointer.
 * 
 * TODO: Earlier we use Java booleans to represent booleans but we have switched to using IBool instead.
 * 		 Some primitives using booleans now work on IValues only and should be moved to RascalPrimitives.
 */

/**
 * @author paulklint
 *
 */
public enum MuPrimitive {
	
	/**
	 * mint3 = mint1 + mint2
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., mint3 ]
	 *
	 */
	addition_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return (int) arg_2 + ((int) arg_1);
		};
	},

	/**
	 * Assign to array element 
	 * [ ..., array, mint, Object ] => [ ..., Object ]
	 *
	 */
	assign_subscript_array_mint {
		@Override
		public int executeN(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			Object[] ar = (Object[]) stack[sp - 3];
			int index = ((int) stack[sp - 2]);
			ar[index] = stack[sp - 1];
			stack[sp - 3] = stack[sp - 1];
			return sp - 2;
		};
	},
	
	/**
	 * Check the type of an argument
	 * [ ..., IValue arg, Type type ] => [ ..., bool ]
	 *
	 */
	check_arg_type {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			Type argType = ((IValue) arg_2).getType();
			Type paramType = ((Type) arg_1);
			return vf.bool(argType.isSubtypeOf(paramType));
		};
	},
	
	/**
	 * mint3 = mint1 / mint2
	 * 
	 * [ ... mint1, mint2 ] => [ ..., mint3 ]
	 *
	 */
	division_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return ((int) arg_2) / ((int) arg_1);
		};
	},
	
	/**
	 * bool = (mint1 == mint2)
	 * [ ..., mint1, mint2 ] => [ ..., bool ]
	 *
	 */
	equal_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return vf.bool(((int) arg_2) == ((int) arg_1));
		};
	},
	
	/**
	 * Equality on IValues or Types: bool = (IValueOrType1 == IValueOrType2)
	 * 
	 * [ ..., IValueOrType1, IValueOrType2 ] => [ ..., bool ]
	 *
	 */
	equal {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			if (arg_2 instanceof IValue	&& (arg_1 instanceof IValue)) {
				return vf.bool(((IValue) arg_2).isEqual(((IValue) arg_1)));
			} else if (arg_2 instanceof Type && (arg_1 instanceof Type)) {
				return vf.bool(((Type) arg_2) == ((Type) arg_1));
			} else {
				throw new InternalCompilerError("MuPrimitive equal -- not defined on "
						+ arg_2.getClass() + " and "
						+ arg_1.getClass());
			}
		};
	},
	
	/**
     * Matching on IValue
     * 
     * [ ..., IValue, IValue ] => [ ..., bool ]
     *
     */
    match {
        @Override
        public Object execute2(final Object arg_2, final Object arg_1) {
            return vf.bool(((IValue) arg_2).match(((IValue) arg_1)));
        };
    },
    
	/**
	 * Equality on ISet and mset: bool = (ISet == mset)
	 * 
	 * [ ..., ISet, mset ] => [ ..., bool ]
	 *
	 */
	equal_set_mset {
		@Override
		@SuppressWarnings("unchecked")
		public Object execute2(final Object arg_2, final Object arg_1) {
			ISet set = (ISet) arg_2;
			HashSet<IValue> mset = (HashSet<IValue>) arg_1;
			if (set.size() != mset.size()) {
				return RascalPrimitive.Rascal_FALSE;
			}
			for (IValue v : set) {
				if (!mset.contains(v)) {
					return RascalPrimitive.Rascal_FALSE;
				}
			}
			return RascalPrimitive.Rascal_TRUE;
		};
	},

	/**
	 * Get the positional arguments of node or constructor (any keyword parameters are ignored):
	 * [ ..., iNode ] => [ ..., array ]
	 */
	get_children {
		@Override
		public Object execute1(final Object arg_1) {
			INode nd = (INode) arg_1;
			Object[] elems = new Object[nd.arity()];
			for (int i = 0; i < nd.arity(); i++) {
				elems[i] = nd.get(i);
			}
			return elems;
		};
	},
	
	/**
	 * Given a ParseTree of the form appl(Symbol, list[Tree]) get
	 * its children without layout as well as its keyword map.
	 * Also handles concrete lists with separators
	 * [ ... IConstructor tree ] => [ ..., array ]
	 *
	 */
	get_children_without_layout_or_separators_with_keyword_map {
		@Override
		public Object execute1(final Object arg_1) {
			IConstructor cons = (IConstructor) arg_1;
			
			// TODO use TreeAdapter.getNonLayoutArgs(cons);
			IConstructor prod = (IConstructor) cons.get(0);
			IList args = (IList) cons.get(1);
			IConstructor symbol = (IConstructor) prod.get(0);

			int step = RascalPrimitive.$getIterDelta(symbol);
			if(step < 0) step = 2;
			
			int non_lit_len = 0;

			for(int i = 0; i < args.length(); i += step){
				if(!$is_literal(args.get(i))){
					non_lit_len++;
				}
			}
			Object[] elems = new Object[non_lit_len + 1];
			
			int j = 0;
			for(int i = 0; i < args.length(); i += step){
				if(!$is_literal(args.get(i))){
					elems[j++] = args.get(i);
				}
			}
			elems[non_lit_len] = RVMCore.emptyKeywordMap;
			return elems;
		}
	},
	
	/**
	 * Given a ParseTree of the form appl(Symbol, list[Tree]) get
	 * its children without layout and without its keyword map.
	 * Also handles concrete lists with separators
	 * [ ... IConstructor tree ] => [ ..., array ]
	 *
	 */
	get_children_without_layout_or_separators_without_keyword_map {
		@Override
		public Object execute1(final Object arg_1) {
			IConstructor cons = (IConstructor) arg_1;
			
			// TODO use TreeAdapter.getNonLayoutArgs(cons);
			IConstructor prod = (IConstructor) cons.get(0);
			IList args = (IList) cons.get(1);
			IConstructor symbol = (IConstructor) prod.get(0);

			int step = RascalPrimitive.$getIterDelta(symbol);
			if(step < 0) step = 2;
			
			int non_lit_len = 0;

			for(int i = 0; i < args.length(); i += step){
				if(!$is_literal(args.get(i))){
					non_lit_len++;
				}
			}
			Object[] elems = new Object[non_lit_len];
			
			int j = 0;
			for(int i = 0; i < args.length(); i += step){
				if(!$is_literal(args.get(i))){
					elems[j++] = args.get(i);
				}
			}
			return elems;
		}
	},
	
	/**
	 * Get value associated with a key in mmap
	 * 
	 * [ ..., mmap, String key ] => Object
	 *
	 */
	get_mmap {
		@SuppressWarnings("unchecked")
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			Map<String,IValue> m = (Map<String,IValue>) arg_2;
			String key = (String) arg_1;
			return m.get(key);
		};
	},
	
	/** 
	 * Get the name of a node
	 * 
	 * [ ..., node nd ] => [ ..., nodeName]
	 */
	
	get_name {
		@Override
		public Object execute1(final Object arg_1) {
			INode nd = (INode) arg_1;
			return vf.string(nd.getName());
		};
	},
	
	/**
	 * Given a constructor or node get an array consisting of
	 * - node/constructor name 
	 * - positional arguments 
	 * 
	 * [ ..., node ] => [ ..., array ]
	 */
	get_name_and_children {
		@Override
		public Object execute1(final Object arg_1) {
			INode v = (INode) arg_1;
			int cons_arity = v.arity();
			Object[] elems = new Object[cons_arity + 1];
			elems[0] = vf.string(v.getName());
			for (int i = 0; i < cons_arity; i++) {
			  elems[i + 1] = v.get(i);
			}
			return elems;
		};
	},
	
	/**
	 * Given a mmap, return its keys as array
	 * 
	 * [ ..., mmap ] => [ ..., array ]
	 */
	get_keys_mmap {
		@Override
		public Object execute1(final Object arg_1) {
			@SuppressWarnings("unchecked")
			Map<String,IValue> mmap = (Map<String,IValue>) arg_1;
			int len = mmap.size();
			String[] keys = new String[len];
			int i = 0;
			for(String key : mmap.keySet()){
				keys[i++] = key;
			}
			return keys;
		};
	},

	/**
	 * Given 
	 * - an array of keywords (as string)
	 * - an array of IValues
	 * construct an mmap representing <keyword[i],value[i]> pairs
	 * 
	 * [ ..., array of keys, array of IValues] => [ ..., mmap ] 
	 *
	 */
	make_keyword_mmap {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			String[] keywords = (String[]) arg_2;
			Object[] values = (Object[]) arg_1;
			assert keywords.length == values.length;
			Map<String,IValue> mmap = new HashMap<String,IValue>(keywords.length);
			for(int i = 0; i< keywords.length; i++){
				mmap.put(keywords[i], (IValue) values[i]);
			}
			return mmap;
		};
	},
	
	/**
	 * Given a tuple, get an array consisting of its elements
	 * 
	 * [ ..., ITuple ] => [ ..., array ]
	 */
	get_tuple_elements {
		@Override
		public Object execute1(final Object arg_1) {
			ITuple tup = (ITuple) arg_1;
			int nelem = tup.arity();
			Object[] elems = new Object[nelem];
			for (int i = 0; i < nelem; i++) {
				elems[i] = tup.get(i);
			}
			return elems;
		};
	},
	
	/**
	 * bool = (mint1 >= mint2)
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., bool ]
	 *
	 */
	greater_equal_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return vf.bool(((int) arg_2) >= ((int) arg_1));
		};
	},
	
	/**
	 * bool = mint1 > mint2
	 * 
	 *  [ ..., mint1, mint2 ] => [ ..., bool ]
	 *
	 */
	greater_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return vf.bool(((int) arg_2) > ((int) arg_1));
		};
	},

	/**
	 * Has a concrete term a given label?
	 * 
	 * [ ..., IValue, IString label ] => [ ..., bool ]
	 */
	// TODO rename to more parse tree specific?
	has_label {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			IValue v = (IValue) arg_2;
			Type vt = v.getType();
			String label_name = ((IString) arg_1).getValue();
			
			if (isNonTerminalType(vt)) {
				ITree cons = (ITree) v;
				if (TreeAdapter.isAppl(cons)) {
					String treeLabel = TreeAdapter.getConstructorName(cons);
					return (treeLabel != null && label_name.equals(treeLabel)) ? RascalPrimitive.Rascal_TRUE : RascalPrimitive.Rascal_FALSE;
				}
			}
			return RascalPrimitive.Rascal_FALSE;
		}
	},
	
	/**
	 * bool3 = (bool1 ==> bool2)
	 * 
	 * [ ..., bool1, bool2 ] => [ ..., bool3 ]
	 *
	 */
	// TODO: move to RascalPrimitive?
	implies_mbool_mbool {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return ((IBool) arg_2).implies((IBool) arg_1);
		};
	},
	
	/**
	 * Check that a Reference refers to a non-null variable
	 * 
	 * [ ..., Reference ] => [ ..., bool ]
	 *
	 */
	is_defined {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((Reference) arg_1).isDefined());
		};
	},
	
	/**
	 * Check that IValue is element of mset
	 * 
	 * [ ..., IValue, mset ] => [ ..., bool ]
	 *
	 */
	is_element_mset {
		@Override
		@SuppressWarnings("unchecked")
		public Object execute2(final Object arg_2, final Object arg_1) {
			return vf.bool(((HashSet<IValue>) arg_1).contains((IValue) arg_2));
		};
	},
	
	/**
	 * Is IValue an IBool?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 * 
	 * Note: all is_* primitives are introduced by Implode and are only used in Library.mu
	 *
	 */
	is_bool {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isBool());
		};
	},
	
	/**
	 * Is IValue a constructor?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_constructor {
		@Override
		public Object execute1(final Object arg_1) {
			Type t = ((IValue) arg_1).getType();
			// TODO: review if is_constructor still needs true on parse trees
			return vf.bool(t.isAbstractData() || isNonTerminalType(t));
		}
	},
	
	/**
	 * Is IValue a IDateTime?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_datetime {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isDateTime());
		};
	},
	
	/**
	 * Is IValue an IInteger?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_int {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isInteger());
		};
	},
	
	/**
	 * Is IValue an IList?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_list {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isList());
		};
	},
	
	/**
	 * Is IValue an IListRelation?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_lrel {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isListRelation());
		};
	},
	
	/**
	 * Is IValue an ISourceLocation?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_loc {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isSourceLocation());
		};
	},
	
	/**
	 * Is IValue an IMap?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_map {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isMap());
		};
	},
	
	/**
	 * Is Object an mmap?
	 * 
	 * [ ..., Object ] => [ ..., bool ]
	 *
	 */
	is_mmap {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(arg_1 instanceof Map);
		};
	},
	
	/**
	 * Is IValue an INode?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_node {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isNode());
		};
	},
	
	/**
	 * Is IValue an INumber?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_num {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isNumber());
		};
	},
	
	/**
	 * Is IValue an IReal?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_real {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isReal());
		};
	},
	
	/**
	 * Is IValue an IRational?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_rat {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isRational());
		};
	},
	
	/**
	 * Is IValue an IRelation?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_rel {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isRelation());
		};
	},
	
	/**
	 * Is IValue an ISet?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_set {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isSet());
		};
	},
	
	/**
	 * Is IValue an IString?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_str {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isString());
		};
	},
	
	/**
	 * Is IValue an ITuple?
	 * 
	 * [ ..., IValue ] => [ ..., bool ]
	 *
	 */
	is_tuple {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((IValue) arg_1).getType().isTuple());
		};
	},
	
	/**
	 * Given an IMap return an array containing its keys
	 * 
	 * [ ..., IMap ] => [ ..., array ]
	 *
	 */
	keys_map {
		@Override
		public Object execute1(final Object arg_1) {
			IMap map = ((IMap) arg_1);
			IListWriter writer = vf.listWriter();
			for (IValue key : map) {
				writer.append(key);
			}
			return writer.done();
		};
	},
	
	/**
	 * Given an IMap return an array containing its values
	 * 
	 * [ ..., IMap ] => [ ..., array ]
	 *
	 */
	values_map {
		@Override
		public Object execute1(final Object arg_1) {
			IMap map = ((IMap) arg_1);
			IListWriter writer = vf.listWriter();
			for (IValue key : map) {
				writer.append(map.get(key));
			}
			return writer.done();
		};
	},
	
	/**
	 * bool= mint1 <= mint2
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., bool ]
	 *
	 */
	less_equal_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return vf.bool(((int) arg_2) <= ((int) arg_1));
		};
	},
	
	/**
	 * bool = mint1 < mint2
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., bool ]
	 *
	 */
	less_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return vf.bool(((int) arg_2) < ((int) arg_1));
		};
	},
	
	make_iarray {	// TODO replace by make_array?
		@Override
		public int executeN(final Object[] stack, final int sp, final int arity) {
			assert arity >= 0;

			IValue[] ar = new IValue[arity];
			for (int i = arity - 1; i >= 0; i--) {
				ar[i] = (IValue) stack[sp - arity + i];
			}
			int sp1 = sp - arity + 1;
			stack[sp1 - 1] = ar;
			return sp1;
		};
	},
	
	make_iarray_of_size {
		@Override
		public Object execute1(final Object arg_1) {
			int len = ((int) arg_1);
			return new IValue[len];
		};
	},
	
	/**
	 * Make an array containing Object_1, Object_2, ..., Object_n:
	 * 
	 * [ ..., Object_1, Object_2, ..., Object_n ] => [ ..., array ]
	 *
	 */
	make_array {
		@Override
		public int executeN(final Object[] stack, final int sp, final int arity) {
			assert arity >= 0;

			Object[] ar = new Object[arity];

			for (int i = arity - 1; i >= 0; i--) {
				ar[i] = stack[sp - arity + i];
			}
			int sp1 = sp - arity + 1;
			stack[sp1 - 1] = ar;
			return sp1;
		};
	},
	
	/**
	 * Make an array of given size
	 * 
	 * [ ..., mint ] => [ ..., array ]
	 *
	 */
	make_array_of_size {
		@Override
		public Object execute1(final Object arg_1) {
			int len = ((int) arg_1);
			return new Object[len];
		};
	},	
	
	/**
	 * Make a new mset
	 * 
	 * [ ... ] => [ ..., mset ]
	 *
	 */
	make_mset {
		@Override
		public Object execute0() {
			HashSet<IValue> mset = new HashSet<IValue>();
			return mset;
		};
	},
	
	/**
	 * Create a new mmap from keyword name (String) to an MapEntry <Type, IValue>
	 * 
	 * [ ... ] => [ ..., mmap ]
	 */
	make_mmap_str_entry {
		@Override
		public Object execute0() {
			return new HashMap<String, Map.Entry<Type, IValue>>();
		};
	}, 
	
	/**
	 * Create a MapEntry <type, default_value>
	 * 
	 * [ ..., Type, IValue ] => [ ..., MapEntry ]
	 *
	 */
	make_mentry_type_ivalue {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return new AbstractMap.SimpleEntry<Type, IValue>((Type) arg_2, (IValue) arg_1);
		};
	},
	
	/**
	 * Given IString_1, IValue_1, ..., IString_n, IValue_n, create a keyword map with <String_i, IValue_i> as entries
	 *
	 * [ ..., IString_1, IValue_1, ..., IString_n, IValue_n ] => [ ..., mmap ]
	 */
	make_mmap {
		@Override
		public int executeN(final Object[] stack, final int sp, final int arity) {
			assert arity >= 0;
			
			if(arity == 0){
				stack[sp] = new HashMap<String, IValue>(0);
				return sp + 1;
			}
			Map<String, IValue> writer = new HashMap<String, IValue>((arity+1)/2);
			for (int i = arity; i > 0; i -= 2) {
				writer.put(((IString) stack[sp - i]).getValue(), (IValue) stack[sp - i + 1]);
			}
			int sp1 = sp - arity + 1;
			stack[sp1 - 1] = writer;

			return sp1;
		}
	},
	
	/**
	 * Given defPos, IString_1, IValue_1, ..., IString_n, IValue_n, 
	 * copy the default map at stack[defPos] and insert the entries <String_i, IValue_i> in it
	 *
	 * [ ..., defPos, IString_1, IValue_1, ..., IString_n, IValue_n ] => [ ..., mmap ]
	 * 
	 * Note: this implements downward keyword parameter propagation that is not (yet) supported by the interpreter
	 */
	copy_and_update_keyword_mmap {
		@Override
		public int executeN(final Object[] stack, final int sp, final int arity) {
			assert arity >= 1;
			
			int defPos = ((IInteger) stack[sp - arity]).intValue();
			@SuppressWarnings("unchecked")
			HashMap<String, IValue> oldMap = (HashMap<String, IValue>) stack[defPos];
			
			if(arity == 1){
				stack[sp - 1] = oldMap;
				return sp;
			}
			
			@SuppressWarnings("unchecked")
			HashMap<String, IValue> writer = (HashMap<String, IValue>) oldMap.clone();
			
			for (int i = arity - 1; i > 0; i -= 2) {
				writer.put(((IString) stack[sp - i]).getValue(), (IValue) stack[sp - i + 1]);
			}
			int sp1 = sp - arity + 1;
			stack[sp1 - 1] = writer;

			return sp1;
		}
	},
	
	/**
	 * Does a keyword map with <String, IValue> entries contain a given key (as String)?
	 * 
	 * [ ..., mmap, String] => [ ..., bool ]
	 *
	 */
	mmap_contains_key {
		@SuppressWarnings("unchecked")
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			Map<String,IValue> m = (Map<String,IValue>) arg_2;
			String key = ((String) arg_1);
			return vf.bool(m.containsKey(key));
		};
	},
	
	make_iterator {
		@SuppressWarnings("unchecked")
		@Override
		public Object execute1(final Object arg_1) {
			Object iteratee = arg_1;
			if(iteratee instanceof Object[]){
				return new ArrayIterator<Object>((Object[]) iteratee);
			} else 
			if(iteratee instanceof AnnotatedAmbFacade){
					return ((AnnotatedAmbFacade) iteratee).getAlternatives().iterator();
			} else
			if(iteratee instanceof AnnotatedConstructorFacade){
				return ((AnnotatedConstructorFacade) iteratee).getChildren().iterator();
			} else {
				return ((Iterable<IValue>) iteratee).iterator();
			}
		};
	},
	
	make_descendant_iterator{
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			IValue iteratee = (IValue) arg_2;
			DescendantDescriptor descriptor = (DescendantDescriptor) arg_1;
			return new DescendantMatchIterator(iteratee, descriptor);
		};
	},
	
	iterator_hasNext {
		@SuppressWarnings("unchecked")
		@Override
		public Object execute1(final Object arg_1) {
			return vf.bool(((Iterator<IValue>) arg_1).hasNext());
		};
	},
	
	iterator_next {
		@SuppressWarnings("unchecked")
		@Override
		public Object execute1(final Object arg_1) {
			return ((Iterator<IValue>) arg_1).next();
		};
	},
	
	/**
	 * mint3 = min(mint1, mint2)
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., mint3 ]
	 *
	 */
	min_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			int x = ((int) arg_2);
			int y = ((int) arg_1);
			return x < y ? x : y;
		};
	},
	
	/**
	 * mint3 = max(mint1, mint2)
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., mint3 ]
	 *
	 */
	max_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			int x = ((int) arg_2);
			int y = ((int) arg_1);
			return x > y ? x : y;
		};
	},
	
	/**
	 * Convert an IValue to a mint
	 * 
	 * [ ..., IValue ] => [ ..., mint ]
	 *
	 */
	mint {
		@Override
		public Object execute1(final Object arg_1) {
			if(arg_1 instanceof IInteger){
				return ((IInteger) arg_1).intValue();
			}
		return arg_1;
		};
	},
	
	/**
	 * Convert an IString to mstr
	 * 
	 * [ ..., IString ] => [ ..., mstr ]
	 *
	 */
	mstr {
		@Override
		public Object execute1(final Object arg_1) {
			if(arg_1 instanceof IString){
				return ((IString) arg_1).getValue();
			}
			return arg_1;
		};
	},
	
	/**
	 * mint3 = mint1 % mint2
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., mint3 ]
	 *
	 */
	modulo_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return ((int) arg_2) % ((int) arg_1);
		};
	},
	
	/**
	 * Convert an ISet to mset
	 * 
	 * [ ..., ISet ] => [ ..., mset ]
	 *
	 */
	mset {
		@Override
		public Object execute1(final Object arg_1) {
			ISet set = ((ISet) arg_1);
			int n = set.size();
			HashSet<IValue> mset = n > 0 ? new HashSet<IValue>(n)
					                     : emptyMset;
			for (IValue v : set) {
				mset.add(v);
			}
			return mset;
		};
	},
	
	/**
	 * mset = ISet1 - ISet2
	 *
	 * [ [ ..., ISet1, ISet2 ] => [ ..., mset ]
	 */
	mset_set_subtract_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			ISet set1 = ((ISet) arg_2);
			int n = set1.size();
			ISet set2 = ((ISet) arg_1);
			HashSet<IValue> mset = n > 0 ? new HashSet<IValue>(n)
					                     : emptyMset;
			for (IValue v : set1) {
				if(!set2.contains(v)){
					mset.add(v);
				}
			}
			return mset;
		};
	},
	
	/**
	 * Make an empty mset
	 * 
	 * [ ... ] => [ ..., mset ]
	 *
	 */
	mset_empty() {
		@Override
		public Object execute0() {
			return emptyMset;
		};
	},
	
	/**
	 * Convert an mset to an IList
	 * 
	 * [ ..., mset ] => [ ..., IList ]
	 *
	 */
	mset2list {
		@Override
		@SuppressWarnings("unchecked")
		public Object execute1(final Object arg_1) {
			HashSet<IValue> mset = (HashSet<IValue>) arg_1;
			IListWriter writer = vf.listWriter();
			for (IValue elem : mset) {
				writer.append(elem);
			}
			return writer.done();
		};
	},
	
	/**
	 * Destructively add element to an mset
	 * 
	 * [ ..., mset, elm ] => [ ..., mset ]
	 *
	 */
	mset_destructive_add_elm {
		@Override
		@SuppressWarnings("unchecked")
		public Object execute2(final Object arg_2, final Object arg_1) {
			HashSet<IValue> mset = (HashSet<IValue>) arg_2;
			if(mset == emptyMset){
				mset = (HashSet<IValue>) emptyMset.clone();
			}
			IValue elm = ((IValue) arg_1);
			mset.add(elm);
			return mset;
		};
	},
	
	/**
	 * Destructively add mset2 to mset1
	 * 
	 * [ ..., mset1, mset2 ] => [ ..., mset1 ]
	 *
	 */
	mset_destructive_add_mset {
		@Override
		@SuppressWarnings("unchecked")
		public Object execute2(final Object arg_2, final Object arg_1) {
			HashSet<IValue> lhs = (HashSet<IValue>) arg_2;
			if(lhs == emptyMset){
				lhs = (HashSet<IValue>) emptyMset.clone();
			}
			// lhs = (HashSet<IValue>) lhs.clone();
			HashSet<IValue> rhs = (HashSet<IValue>) arg_1;
			lhs.addAll(rhs);
			return lhs;
		};
	},
	
	/**
	 *	Add a <Tye, IValue> entry to an mmap
	 *
	 * [ ..., mmap, IString, MapEntry<Type,IValue> ] => [ ..., mmap ]
	 */
	mmap_str_entry_add_entry_type_ivalue {
		@Override
		@SuppressWarnings("unchecked")
		public int executeN(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			/*stack[sp - 3] = */((Map<String, Map.Entry<Type, IValue>>) stack[sp - 3])
					.put(((IString) stack[sp - 2]).getValue(),
							(Map.Entry<Type, IValue>) stack[sp - 1]);
			return sp - 2;
		};
	},

	/**
	 * mint3 = mint1 * mint2
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., mint3 ]
	 *
	 */
	multiplication_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return ((int) arg_2) * ((int) arg_1);
		};
	},
	
	/**
	 * Destructively subtract mset from mset
	 * 
	 * [ ..., mset1, mset2 ] => [ ..., mset1 ]
	 *
	 */
	mset_destructive_subtract_mset {
		@Override
		@SuppressWarnings("unchecked")
		public Object execute2(final Object arg_2, final Object arg_1) {
			HashSet<IValue> lhs = (HashSet<IValue>) arg_2;
			// lhs = (HashSet<IValue>) lhs.clone();
			HashSet<IValue> rhs = (HashSet<IValue>) arg_1;

			lhs.removeAll(rhs);
			return lhs;
		};
	},
	
	/**
	 * Subtract mset from copied mset
	 * 
	 * [ ..., mset1, mset2 ] => [ ..., mset3 ]
	 *
	 */
	mset_subtract_mset {
		@Override
		@SuppressWarnings("unchecked")
		public Object execute2(final Object arg_2, final Object arg_1) {
			HashSet<IValue> lhs = (HashSet<IValue>) arg_2;
			lhs = (HashSet<IValue>) lhs.clone();
			HashSet<IValue> rhs = (HashSet<IValue>) arg_1;
			lhs.removeAll(rhs);
			return lhs;
		};
	},
	
	/**
	 * Destructively subtract ISet from an mset
	 * 
	 * [ ..., mset, ISet ] => [ ..., mset ]
	 *
	 */
	mset_destructive_subtract_set {
		@Override
		@SuppressWarnings("unchecked")
		public Object execute2(final Object arg_2, final Object arg_1) {
			HashSet<IValue> mset = (HashSet<IValue>) arg_2;
			mset = (HashSet<IValue>) mset.clone();
			ISet set = ((ISet) arg_1);
			for (IValue v : set) {
				mset.remove(v);
			}
			return mset;
		};
	},
	
	/**
	 * Subtract ISet from copied mset
	 * 
	 * [ ..., mset1, ISet ] => [ ..., mset2 ]
	 *
	 */
	mset_subtract_set {
		@Override
		@SuppressWarnings("unchecked")
		public Object execute2(final Object arg_2, final Object arg_1) {
			HashSet<IValue> mset = (HashSet<IValue>) arg_2;
			mset = (HashSet<IValue>) mset.clone();
			ISet set = ((ISet) arg_1);
			for (IValue v : set) {
				mset.remove(v);
			}
			return mset;
		};
	},
	
	/**
	 * Destructively subtract element from an mset
	 * 
	 * [ ..., mset, IValue ] => [ ..., mset ]
	 *
	 */
	mset_destructive_subtract_elm {
		@Override
		@SuppressWarnings("unchecked")
		public Object execute2(final Object arg_2, final Object arg_1) {
			HashSet<IValue> mset = (HashSet<IValue>) arg_2;
			IValue elm = ((IValue) arg_1);
			mset.remove(elm);
			return mset;
		};
	},
	
	/**
	 * Subtract element from copied mset
	 * 
	 * [ ..., mset1, IValue ] => [ ..., mset2 ]
	 *
	 */
	mset_subtract_elm {
		@Override
		@SuppressWarnings("unchecked")
		public Object execute2(final Object arg_2, final Object arg_1) {
			HashSet<IValue> mset = (HashSet<IValue>) arg_2;
			mset = (HashSet<IValue>) mset.clone();
			IValue elm = ((IValue) arg_1);
			mset.remove(elm);
			return mset;
		};
	},
	
	/**
	 * bool = (mint1 != mint2)
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., bool ]
	 *
	 */
	not_equal_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return vf.bool(((int) arg_2) != ((int) arg_1));
		};
	},
	
	/**
	 * bool2 = !bool1
	 * 
	 * [ ..., bool1 ] => [ ..., bool2 ]
	 *
	 */
	// TODO: move to RascalPrimitive?
	not_mbool {
		@Override
		public Object execute1(final Object arg_1) {
			return ((IBool) arg_1).not();
		};
	},
	
	/**
	 * bool = IList2 is a sublist of IList1 at start
	 * 
	 * [ ..., IList1, IList2, start ] => [ ..., bool ]
	 */
	occurs_list_list_mint {
		@Override
		public int executeN(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			IList sublist = ((IList) stack[sp - 3]);
			int nsub = sublist.length();
			IList list = ((IList) stack[sp - 2]);
			int start = (int) stack[sp - 1];
			int nlist = list.length();
			stack[sp - 3] = RascalPrimitive.Rascal_FALSE;
			int newsp = sp - 2;
			if (start + nsub <= nlist) {
				for (int i = 0; i < nsub; i++) {
					if (!sublist.get(i).isEqual(list.get(start + i)))
						return newsp;
				}
			} else {
				return newsp;
			}

			stack[sp - 3] = RascalPrimitive.Rascal_TRUE;
			return newsp;
		};
	},
	
	one_dot_zero {
		@Override
		public Object execute1(final Object arg_1) { // Actually zero args, but muRascal does not permit that
			return vf.real("1.0");
		};
	},
	
	/**
	 * bool3 = (bool1 || bool2)
	 * 
	 * [ ..., bool1, bool2 ] => [ ..., bool3 ]
	 *
	 */
	
	// TODO: move to RascalPrimitive?
	or_mbool_mbool {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return ((IBool) arg_2).or((IBool) arg_1);
		};
	},
	
	/**
	 * mint3 = mint1 ^ mint2
	 *
	 * [ ..., mint1, mint2 ] => [ ..., mint3 ]
	 *
	 */
	power_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			int n1 = ((int) arg_2);
			int n2 = ((int) arg_1);
			int pow = 1;
			for (int i = 0; i < n2; i++) {
				pow *= n1;
			}
			return pow;
		};
	},
	
	/**
	 * Convert mint to Rascal int (IInteger)
	 * 
	 * [ ..., mint ] => [ ..., IInteger ]
	 */
	rint {
		@Override
		public Object execute1(final Object arg_1) {
			return vf.integer((int) arg_1);
		};
	},
	
	/**
	 * Compile a RegExp Matcher given:
	 * - IString, the regexp
	 * - IValue, the subject string, either an IString or an arbitrary IValue (always a ParseTree).
	 * 
	 * [ ..., IString regexp, IValue subject ] => [ ..., Matcher ]
	 */
	regexp_compile {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			String RegExpAsString = ((IString) arg_2).getValue();
			IValue isubject = (IValue) arg_1;
			String subject;
			if(isubject instanceof IString){
				subject =  ((IString) isubject).getValue();
			} else {
				StringWriter w = new StringWriter();
				IConstructor c = (IConstructor) isubject;
				try {
					TreeAdapter.unparse(c, w);
				} catch (FactTypeUseException | IOException e) {
					throw new RuntimeException(e);
				}
				subject = w.toString();
			}
			//System.err.println("regexp_compile: \"" + RegExpAsString + "\" and \"" + subject + "\" len = " + subject.length());
			try {
				Pattern pat = Pattern.compile(RegExpAsString, Pattern.UNICODE_CHARACTER_CLASS);
				return pat.matcher(subject);
			} catch (PatternSyntaxException e) {
				throw RascalRuntimeException.RegExpSyntaxError(RegExpAsString, null);
			}
		};
	},
	
	/**
	 * Begin position of RegExp Match
	 * 
	 * [ ..., Matcher ] => [ ..., begin position ]
	 */
	regexp_begin {
		@Override
		public Object execute1(final Object arg_1) {
			Matcher matcher = (Matcher) arg_1;
			return matcher.start(); //vf.integer(matcher.start());
		};
	},
	
	/**
	 * End position of RegExp Match
	 * 
	 * [ ..., Matcher ] => [ ..., end position ]
	 */
	regexp_end {
		@Override
		public Object execute1(final Object arg_1) {
			Matcher matcher = (Matcher) arg_1;
			return matcher.end(); //vf.integer(matcher.end());
		};
	},
	
	/**
	 * Find next RegExp match
	 * 
	 * [ ..., Matcher ] => [ ..., bool ]
	 */
	regexp_find {
		@Override
		public Object execute1(final Object arg_1) {
			Matcher matcher = (Matcher) arg_1;
			return vf.bool(matcher.find());
		};
	},
	
	/**
	 * Set the region for a RegExp Matcher
	 * 
	 * [ ..., Matcher, mint1, mint2 ] => [ ..., Matcher ]
	 *
	 */
	regexp_set_region {
		@Override
		public int executeN(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			Matcher matcher = (Matcher) stack[sp - 3];
			int start = ((int) stack[sp - 2]);
			int end = ((int) stack[sp - 1]);
			stack[sp - 3] = matcher.region(start, end);
			return sp - 2;
		};
	},
	
	/**
	 * Get the match result for a specifc group mint in RegExp match
	 * 
	 * [ ..., Matcher, mint ] => [ ..., IString ]
	 */
	regexp_group {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			Matcher matcher = (Matcher) arg_2;
			int idx = (int) arg_1;
			return vf.string(matcher.group(idx));
		};
	},
	
	/**
	 * Convert mset to Rascal set (ISet)
	 * 
	 *	[ ..., mset ] => [ ..., ISet ] 
	 */
	set {
		@Override
		@SuppressWarnings("unchecked")
		public Object execute1(final Object arg_1) {
			HashSet<IValue> mset = (HashSet<IValue>) arg_1;
			ISetWriter w = vf.setWriter();
			for (IValue v : mset) {
				w.insert(v);
			}
			return w.done();
		};
	},
	
	/**
	 * Convert an ISet to an IList
	 * 
	 * [ ..., ISet ] => [ ..., IList ]
	 */
	set2list {
		@Override
		public Object execute1(final Object arg_1) {
			ISet set = (ISet) arg_1;
			IListWriter writer = vf.listWriter();
			for (IValue elem : set) {
				writer.append(elem);
			}
			return writer.done();
		};
	},
	
	/**
	 * bool = (ISet < mset)
	 *
	 * [ ..., ISet, mset ] => [ ..., bool ]
	 */
	set_is_subset_of_mset {
		@Override
		@SuppressWarnings("unchecked")
		public Object execute2(final Object arg_2, final Object arg_1) {
			ISet subset = ((ISet) arg_2);
			HashSet<IValue> mset = (HashSet<IValue>) arg_1;
			for (IValue v : subset) {
				if (!mset.contains(v)) {
					return RascalPrimitive.Rascal_FALSE;
				}
			}
			return RascalPrimitive.Rascal_TRUE;
		};
	},
	
	/**
	 * Size of array
	 * 
	 * [ ..., array ] => [ ..., mint ]
	 */
	size_array {
		@Override
		public Object execute1(final Object arg_1) {
			return ((Object[]) arg_1).length;
		};
	},
	
	/**
	 * Size of IList
	 * 
	 * [ ..., IList ] => [ ..., mint ]
	 */
	size_list {
		@Override
		public Object execute1(final Object arg_1) {
			return ((IList) arg_1).length();
		};
	},
	
	/**
	 * Size of ISet
	 * 
	 * [ ..., ISet ] => [ ..., mint ]
	 */
	size_set {
		@Override
		public Object execute1(final Object arg_1) {
			return ((ISet) arg_1).size();
		};
	},
	
	/**
	 * Size of mset
	 * 
	 * [ ..., mset ] => [ ..., mint ]
	 */
	size_mset {
		@Override
		public Object execute1(final Object arg_1) {
			return ((HashSet<?>) arg_1).size();
		};
	},
	
	/**
	 * Size of IMap
	 * 
	 * [ ..., IMap ] => [ ..., mint ]
	 */
	size_map {
		@Override
		public Object execute1(final Object arg_1) {
			return ((IMap) arg_1).size();
		};
	},
	
	/**
	 * Size of IString
	 * 
	 * [ ..., IString ] => [ ..., mint ]
	 */
	size_str {
		@Override
		public Object execute1(final Object arg_1) {
			return ((IString) arg_1).length();
		};
	},
	
	/**
	 * Size of ITuple
	 * 
	 * [ ..., ITuple ] => [ ..., mint ]
	 */
	size_tuple {
		@Override
		public Object execute1(final Object arg_1) {
			return ((ITuple) arg_1).arity();
		};
	},
	
	/**
	 * Generic size function
	 * 
	 * [ ..., IValue ] => [ ..., mint ]
	 */
	size {
		@Override
		public Object execute1(final Object arg_1) {
			if (arg_1 instanceof IString) {
				return ((IString) arg_1).length();
			} else if (arg_1 instanceof IConstructor) {
				return ((IConstructor) arg_1).arity();
			} else if (arg_1 instanceof INode) {
				return ((INode) arg_1).arity();
			} else if (arg_1 instanceof IList) {
				return ((IList) arg_1).length();
			} else if (arg_1 instanceof ISet) {
				return ((ISet) arg_1).size();
			} else if (arg_1 instanceof IMap) {
				return ((IMap) arg_1).size();
			} else if (arg_1 instanceof ITuple) {
				return ((ITuple) arg_1).arity();
			}
			return 0;			// TODO?
		};
	},
	
	/**
	 * 	IList2 occurs as subslist in IList1 at position start
	 * 
	 * [ ..., IList1, IList2, start] => [ ..., bool ]
	 *
	 */
	starts_with {
		@Override
		public int executeN(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			IList sublist = (IList) stack[sp - 3];
			IList list = (IList) stack[sp - 2];
			int start = (int) stack[sp - 1];
			IBool eq = RascalPrimitive.Rascal_TRUE;

			if (start + sublist.length() <= list.length()) {
				for (int i = 0; i < sublist.length() && eq.getValue(); i++) {
					if (!sublist.get(i).equals(list.get(start + i))) {
						eq = RascalPrimitive.Rascal_FALSE;
					}
				}
			}
			stack[sp - 3] = eq;
			return sp - 2;
		};
	},
	
	/**
	 * IList2 = sublist of IList given offset and length
	 * 
	 * [ ..., IList1, offset, length ] => [ ..., IList2 ]
	 */
	sublist_list_mint_mint {
		@Override
		public int executeN(final Object[] stack, int sp, final int arity) {
			assert arity == 3;
			int length = ((int) stack[--sp]);
			int offset = ((int) stack[--sp]);
			IList lst = (IList) stack[--sp];
			
			stack[sp++] = lst.sublist(offset, length);
			return sp;
		};
	},
	
	/**
	 * Object = array[mint]
	 * 
	 * [ ..., array, mint ] => [ ..., Object ]
	 *
	 */
	subscript_array_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return ((Object[]) arg_2)[((int) arg_1)];
		};
	},
	
	/**
	 * Object = array[int]
	 * 
	 * [ ..., array, int ] => [ ..., Object ]
	 *
	 */
	subscript_array_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return ((Object[]) arg_2)[((IInteger) arg_1).intValue()];
		};
	},
	
	/**
	 * IValue = IList[mint]
	 * 
	 * [ ..., IList, mint ] => [ ..., IValue ]
	 */
	subscript_list_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return ((IList) arg_2).get((int) arg_1);
		};
	},
	
	/**
	 * IValue = ITuple[mint]
	 * 
	 * [ ..., ITuple, mint ] => [ ..., IValue ]
	 */
	subscript_tuple_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return ((ITuple) arg_2).get((int) arg_1);
		};
	},
	
	/**
	 * Make a substring
	 * 
	 * IString2 = IString2.substring(start,end)
	 * 
	 * [ ..., IString1, start, end ] => [ ..., IString2 ]
	 */
	substring_str_mint_mint {
		@Override
		public int executeN(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			IString subject = ((IString)  stack[sp - 3]);
			int start = ((int)  stack[sp - 2]);
			int end  = ((int)  stack[sp - 1]);
			//System.err.println("substring: " + subject + ", " + start + ", " + end);
			stack[sp - 3] = subject.substring(start, end);
			return sp - 2;
		};
	},
	
	/**
	 * bool = IString2 is tail of IString1 at start
	 *	[ ..., IString1, IString2, start ] => [ ..., bool ]
	 */
	is_tail_str_str_mint {
		@Override
		public int executeN(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			IString subject = ((IString)  stack[sp - 3]);
			IString substr = ((IString)  stack[sp - 2]);
			int start = ((int)  stack[sp - 1]);
			if(start + substr.length() == subject.length()){
				stack[sp - 3] = vf.bool(subject.substring(start, start + substr.length()).compare(substr) == 0);
			} else {
				stack[sp - 3] = RascalPrimitive.Rascal_FALSE;
			}
			return sp - 2;
		};
	},
	
	/**
	 * mint3 = mint1 - mint2
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., mint3 ]
	 */
	subtraction_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return ((int) arg_2) - ((int) arg_1);
		};
	},
	
	/**
	 * bool = Type1 < Type2
	 * 
	 * [ ..., Type1, Type2 ] => [ ..., bool ]
	 */
	subtype {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return vf.bool(((Type) arg_2).isSubtypeOf((Type) arg_1));
		};
	},
	
	/**
	 * Get type of an IValue or mint
	 * 
	 * [ ..., IValueOrMint ] => [ ..., bool ]
	 */
	typeOf {
		@Override
		public Object execute1(final Object arg_1) {
			if (arg_1 instanceof Integer) {
				return TypeFactory.getInstance().integerType();
			} else {
				return ((IValue) arg_1).getType();
			}
		};
	},
	
	/**
	 * Get type of mset
	 * 
	 * [ ..., mset ] => [ ..., Type ]
	 */
	typeOfMset {
		@SuppressWarnings("unchecked")
		@Override
		public Object execute1(final Object arg_1) {
			if (arg_1 instanceof HashSet) {
				HashSet<IValue> mset =  (HashSet<IValue>) arg_1;
				Type elmType = TypeFactory.getInstance().voidType();
				for (IValue elm : mset) {
					elmType = elm.getType().lub(elmType);
				}
				return TypeFactory.getInstance().setType(elmType);
			}
			throw new InternalCompilerError("typeOfMset");
		};
	},
	
	/**
	 * Set value of Reference to undefined
	 * 
	 * [ ..., Reference ] => [ ..., Reference ]
	 *
	 */
	undefine {
		@Override
		public Object execute1(final Object arg_1) {
			Reference ref = (Reference) arg_1;
			Object val = ref.getValue();
			ref.undefine();
			return val;
		};
	},
	
	/**
	 * mint3 = mint1 * mint2
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., mint3 ]
	 *
	 */
	product_mint_mint {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			return ((int) arg_2) * ((int) arg_1);
		};
	},
	/**
	 * Make a new subject: [iValue, mint]
	 * 
	 * [ ..., iValue, mint ] => [ ..., [iValue, mint] ]
	 *
	 */
	make_subject {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1) {
			Object[] subject = new Object[2];
			subject[0] = arg_2;
			subject[1] = arg_1;
			return subject;
		};
	},
	/**
	 * Accept a list match when end of subject has been reached
	 * 
	 * [ ..., [iList, mint] ] => [ ..., ilist.length() == mint ]
	 *
	 */
	accept_list_match {
		@Override
		public Object execute1(final Object arg_1) {
			Object[] subject = (Object[]) arg_1;
			IList listSubject = (IList) subject[0];
			int cursor = (int) subject[1];
			int len = listSubject.length();
			if(cursor == len){
				return RascalPrimitive.Rascal_TRUE;
			}
			for(int i = cursor; i < len; i++){				// Check whether only nullables follow
				if(!$is_nullable(listSubject.get(i))){		// TODO maybe better to make a separate accept for the concrete case
					return RascalPrimitive.Rascal_FALSE;
				}
			}
			return RascalPrimitive.Rascal_TRUE;
		};
	}
	
	;

	private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();

	public static final MuPrimitive[] values = MuPrimitive.values();
	
	private static final HashSet<IValue> emptyMset = new HashSet<IValue>(0);
		
	public static MuPrimitive fromInteger(int muprim) {
		return values[muprim];
	}

	private static boolean isNonTerminalType(Type t) {
		return t.isExternalType() && ((RascalType) t).isNonterminal();
	}
	
	public Object execute0() {
		throw RascalRuntimeException.notImplemented("MuPrimitive.execute0 " + name(), null, null);
	}
	
	@SuppressWarnings("unused")
    public Object execute1(final Object arg_1) {
	  throw RascalRuntimeException.notImplemented("MuPrimitive.execute1 " + name(), null, null);
	}
	
	@SuppressWarnings("unused")
    public Object execute2(final Object arg_2, final Object arg_1) {
	  throw RascalRuntimeException.notImplemented("MuPrimitive.execute2 " + name(), null, null);
	}
	
	@SuppressWarnings("unused")
    public int executeN(final Object[] stack, final int sp, final int arity) {
	  throw RascalRuntimeException.notImplemented("MuPrimitive.executeN " + name(), null, null);
	}

	public static void exit(PrintWriter out) {
	}
	
	// Bootstrap method used for invokeDynamic on MuPrimitives, see BytecoeGenerator
	
	@SuppressWarnings("unused")
    public static CallSite bootstrapMuPrimitive(MethodHandles.Lookup caller, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MuPrimitive enumElement = MuPrimitive.valueOf(name);
        Class<?>[] parameters = type.parameterArray();
        int arity = parameters.length;
        String suffix = arity <= 2 ? String.valueOf(arity): "N";
        Method execute = enumElement.getClass().getMethod("execute" + suffix, parameters);

        MethodHandle foundMethod = lookup.unreflect(execute).bindTo(enumElement);
        return new ConstantCallSite(foundMethod.asType(type));
    }

	/*******************************************************************
	 *                 AUXILIARY FUNCTIONS                             *
	 ******************************************************************/   
	/**
	 * @param v
	 * @return true if v is a 'lit' or 'cilit'.
	 */
	private static boolean $is_literal(final IValue v){
		if(isNonTerminalType(v.getType())) {
			return TreeAdapter.isLiteral((ITree) v);
		}
		return false;
	}
	
	private static boolean $is_nullable(final IValue v){
		if (v instanceof ITree) {
			return TreeAdapter.getArgs((ITree) v).length() == 0;
		}
		return false;
	}
	
	/*
	 * Main program: handy to map a primitive index back to its name (e.g., in profiles!)
	 */
	public static void main(String[] args) {
		int n = 102;
		
		System.err.println("MuPrimitive: " + fromInteger(n) + " (" + n + ")");
	}
}

class ArrayIterator<T> implements Iterator<T> {
	  private T array[];
	  private int pos = 0;

	  public ArrayIterator(T anArray[]) {
	    array = anArray;
	  }

	  public boolean hasNext() {
	    return pos < array.length;
	  }

	  public T next() throws NoSuchElementException {
	    if (hasNext())
	      return array[pos++];
	    else
	      throw new NoSuchElementException();
	  }

	  public void remove() {
	    throw new UnsupportedOperationException();
	  }
	}
