package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.impl.AnnotatedConstructorFacade;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.types.RascalType;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.RascalValueFactory.AnnotatedAmbFacade;
import org.rascalmpl.values.uptr.TreeAdapter;

/**
 * MuPrimitive defines all primitives that are necessary for running muRascal programs.
 * This includes all operations with at least one operand that is not an IValue:
 * 		- mbool	(PDB IBool)
 * 		- mint	(Java Integer)
 * 		- mstr  (Java String)
 * 		- mset	(Java HashSet<IValue>)
 * 		- mmap	(Java HashMap of various types, e.g., HashMap<String, IValue> and HashMap<String, Entry<String, IValue>>)
 * 		- array	(Java Object[])
 * 
 * All operations with only IValues as arguments are defined in RascalPrimitive
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = ((Integer) stack[sp - 2]) + ((Integer) stack[sp - 1]);
			return sp - 1;
		};
	},

	/**
	 * Assign to array element 
	 * [ ..., array, mint, Object ] => [ ..., Object ]
	 *
	 */
	assign_subscript_array_mint {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			Object[] ar = (Object[]) stack[sp - 3];
			Integer index = ((Integer) stack[sp - 2]);
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			Type argType = ((IValue) stack[sp - 2]).getType();
			Type paramType = ((Type) stack[sp - 1]);
			stack[sp - 2] = vf.bool(argType.isSubtypeOf(paramType));
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = ((Integer) stack[sp - 2]) / ((Integer) stack[sp - 1]);
			return sp - 1;
		};
	},
	
	/**
	 * mbool = (mint1 == mint2)
	 * [ ..., mint1, mint2 ] => [ ..., mbool ]
	 *
	 */
	equal_mint_mint {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = vf.bool(((Integer) stack[sp - 2]) == ((Integer) stack[sp - 1]));
			return sp - 1;
		};
	},
	
	/**
	 * Equality on IValues or Types: mbool = (IValueOrType1 == IValueOrType2)
	 * 
	 * [ ..., IValueOrType1, IValueOrType2 ] => [ ..., mbool ]
	 *
	 */
	equal {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			if (stack[sp - 2] instanceof IValue	&& (stack[sp - 1] instanceof IValue)) {
				stack[sp - 2] = vf.bool(((IValue) stack[sp - 2]).isEqual(((IValue) stack[sp - 1])));
			} else if (stack[sp - 2] instanceof Type && (stack[sp - 1] instanceof Type)) {
				stack[sp - 2] = vf.bool(((Type) stack[sp - 2]) == ((Type) stack[sp - 1]));
			} else
				throw new CompilerError("MuPrimitive equal -- not defined on "
						+ stack[sp - 2].getClass() + " and "
						+ stack[sp - 1].getClass());
			return sp - 1;
		};
	},
	
	/**
	 * Equality on ISet and mset: mbool = (ISet == mset)
	 * 
	 * [ ..., ISet, mset ] => [ ..., mbool ]
	 *
	 */
	equal_set_mset {
		@Override
		@SuppressWarnings("unchecked")
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			ISet set = (ISet) stack[sp - 2];
			HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
			stack[sp - 2] = Rascal_FALSE;
			if (set.size() != mset.size()) {
				return sp - 1;
			}
			for (IValue v : set) {
				if (!mset.contains(v)) {
					return sp - 1;
				}
			}
			stack[sp - 2] = Rascal_TRUE;
			return sp - 1;
		};
	},

	/**
	 * Get the positional arguments of node or constructor (any keyword parameters are ignored):
	 * [ ..., iNode ] => [ ..., array ]
	 */
	get_children {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			INode nd = (INode) stack[sp - 1];
			Object[] elems = new Object[nd.arity()];
			for (int i = 0; i < nd.arity(); i++) {
				elems[i] = nd.get(i);
			}
			stack[sp - 1] = elems;
			return sp;
		};
	},
	
	/**
	 * Given a ParseTree of the form appl(Symbol, list[Tree]) get
	 * its children without layout. Also handles concrete lists with separators
	 * [ ... IConstructor tree ] => [ ..., array ]
	 *
	 */
	get_children_without_layout_or_separators {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			IConstructor cons = (IConstructor) stack[sp - 1];
			
			// TODO use TreeAdapter.getNonLayoutArgs(cons);
			IConstructor prod = (IConstructor) cons.get(0);
			IList args = (IList) cons.get(1);
			IConstructor symbol = (IConstructor) prod.get(0);

			int step;

			switch(symbol.getName()){

			case "iter":
			case "iter-star":
				step = 2; break;
			case "iter-seps":
			case "iter-seps-star":
				step = 4; break;
			default:
				step = 2;
			}
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
			elems[non_lit_len] = emptyKeywordMap;
			stack[sp - 1] = elems;
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			Map<String,IValue> m = (Map<String,IValue>) stack[sp - 2];
			String key = (String) stack[sp - 1];
			stack[sp - 2] = m.get(key);
			return sp - 1;
		};
	},
	
	/** 
	 * Get the name of a node
	 * 
	 * [ ..., node nd ] => [ ..., nodeName]
	 */
	get_name {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			INode nd = (INode) stack[sp - 1];
			stack[sp - 1] = vf.string(nd.getName());
			return sp;
		};
	},
	
	/**
	 * Given a constructor or node get an array consisting of
	 * - node/constructor name 
	 * - positional arguments 
	 * - keyword parameters collected in a mmap	
	 * 
	 * [ ..., node ] => [ ..., array ]
	 */
	get_name_and_children_and_keyword_mmap {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			INode v = (INode) stack[sp - 1];
			int cons_arity = v.arity();
			Object[] elems = new Object[cons_arity + 2];
			elems[0] = vf.string(v.getName());
			for (int i = 0; i < cons_arity; i++) {
			  elems[i + 1] = v.get(i);
			}
			if(v.mayHaveKeywordParameters()){
				elems[cons_arity + 1] = v.asWithKeywordParameters().getParameters();
			} else {
				elems[cons_arity + 1] = emptyKeywordMap;
			}
			stack[sp - 1] = elems;
			return sp;
		};
	},
	
	/**
	 * Given a constructor or node get an array consisting of
	 * - positional arguments 
	 * - keyword parameters collected in a mmap	
	 * 
	 * [ ..., node ] => [ ..., array ]
	 */
	get_children_and_keyword_mmap {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			INode v = (INode) stack[sp - 1];
			int cons_arity = v.arity();
			Object[] elems = new Object[cons_arity + 1];
			for (int i = 0; i < cons_arity; i++) {
			  elems[i] = v.get(i);
			}
			if(v.mayHaveKeywordParameters()){
				elems[cons_arity] = v.asWithKeywordParameters().getParameters();
			} else {
				elems[cons_arity] = emptyKeywordMap;
			}
			stack[sp - 1] = elems;
			return sp;
		};
	},
	
	/**
	 * Given a constructor or node get an array consisting of
	 * - keyword parameters collected in a mmap
	 * 
	 * [ ..., node ] => [ ..., mmap ]
	 */
	get_keyword_mmap {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			INode v = (INode) stack[sp - 1];
			if(v.mayHaveKeywordParameters()){
				stack[sp - 1] = v.asWithKeywordParameters().getParameters();
			} else {
				stack[sp - 1] = emptyKeywordMap;
			}
			return sp;
		};
	},
	
	/**
	 * Given a mmap, return its keys as array
	 * 
	 * [ ..., mmap ] => [ ..., array ]
	 */
	get_keys_mmap {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			@SuppressWarnings("unchecked")
			Map<String,IValue> mmap = (Map<String,IValue>) stack[sp - 1];
			int len = mmap.size();
			String[] keys = new String[len];
			int i = 0;
			for(String key : mmap.keySet()){
				keys[i++] = key;
			}
			stack[sp - 1] = keys;
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			String[] keywords = (String[]) stack[sp - 2];
			Object[] values = (Object[]) stack[sp - 1];
			assert keywords.length == values.length;
			Map<String,IValue> mmap = new HashMap<String,IValue>();
			for(int i = 0; i< keywords.length; i++){
				mmap.put(keywords[i], (IValue) values[i]);
			}
			stack[sp - 2] = mmap;
			return sp - 1;
		};
	},
	
	/**
	 * Given a constructor or node get an array consisting of
	 * - its positional arguments 
	 * - the values of its keyword arguments
	 * 
	 * [ ... node ] => [ ..., array ]
	 */
	get_children_and_keyword_values {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			INode v = (INode) stack[sp - 1];
			int cons_arity = v.arity();
			Map<String, IValue> m ;
			if(v.mayHaveKeywordParameters()){
				m = v.asWithKeywordParameters().getParameters();
			} else {
				m = emptyKeywordMap;
			}
			int kw_arity = m.size();
			Object[] elems = new Object[cons_arity + kw_arity];
			for (int i = 0; i < cons_arity; i++) {
			  elems[i] = v.get(i);
			}
			int j = cons_arity;
			for(IValue val : m.values()){
				elems[j++] = val;
			}
			stack[sp - 1] = elems;
			return sp;
		};
	},
	
	/**
	 * Given a tuple, get an array consisting of its elements
	 * 
	 * [ ..., ITuple ] => [ ..., array ]
	 */
	get_tuple_elements {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			ITuple tup = (ITuple) stack[sp - 1];
			int nelem = tup.arity();
			Object[] elems = new Object[nelem];
			for (int i = 0; i < nelem; i++) {
				elems[i] = tup.get(i);
			}
			stack[sp - 1] = elems;
			return sp;
		};
	},
	
	/**
	 * mbool = (mint1 >= mint2)
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., mbool ]
	 *
	 */
	greater_equal_mint_mint {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = vf.bool(((Integer) stack[sp - 2]) >= ((Integer) stack[sp - 1]));
			return sp - 1;
		};
	},
	
	/**
	 * mbool = mint1 > mint2
	 * 
	 *  [ ..., mint1, mint2 ] => [ ..., mbool ]
	 *
	 */
	greater_mint_mint {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = vf.bool(((Integer) stack[sp - 2]) > ((Integer) stack[sp - 1]));
			return sp - 1;
		};
	},

	/**
	 * Has a concrete term a given label?
	 * 
	 * [ ..., IValue, IString label ] => [ ..., mbool ]
	 */
	// TODO rename to more parse tree specific?
	has_label {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			IValue v = (IValue) stack[sp - 2];
			Type vt = v.getType();
			String label_name = ((IString) stack[sp - 1]).getValue();
			
			if (isNonTerminalType(vt)) {
				ITree cons = (ITree) v;
				if (TreeAdapter.isAppl(cons)) {
					String treeLabel = TreeAdapter.getConstructorName(cons);
					stack[sp - 2] = (treeLabel != null && label_name.equals(treeLabel)) ? Rascal_TRUE : Rascal_FALSE;
					return sp - 1;
				}
			}
			stack[sp - 2] = Rascal_FALSE;
			return sp - 1;
		}
	},
	
	/**
	 * mbool 3 = (mbool1 ==> mbool2)
	 * 
	 * [ ..., mbool1, mbool2 ] => [ ..., mbool3 ]
	 *
	 */
	implies_mbool_mbool {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IBool) stack[sp - 2]).implies((IBool) stack[sp - 1]);
			return sp - 1;
		};
	},
	
	/**
	 * Check that a Reference refers to a non-null variable
	 * 
	 * [ ..., Reference ] => [ ..., mbool ]
	 *
	 */
	is_defined {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			Reference ref = (Reference) stack[sp - 1];
			stack[sp - 1] = vf.bool(ref.isDefined());
			return sp;
		};
	},
	
	/**
	 * Check that IValue is element of mset
	 * 
	 * [ ..., IValue, mset ] => [ ..., mbool ]
	 *
	 */
	is_element_mset {
		@Override
		@SuppressWarnings("unchecked")
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = vf.bool(((HashSet<IValue>) stack[sp - 1]).contains((IValue) stack[sp - 2]));
			return sp - 1;
		};
	},
	
	/**
	 * Is IValue an IBool?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_bool {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isBool());
			return sp;
		};
	},
	
	/**
	 * Is IValue a constructor?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_constructor {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			Type t = ((IValue) stack[sp - 1]).getType();
			// TODO: review if is_constructor still needs true on parse trees
			stack[sp - 1] = vf.bool(t.isAbstractData() || isNonTerminalType(t));
			return sp;
		}

		
	},
	
	/**
	 * Is IValue a IDateTime?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_datetime {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isDateTime());
			return sp;
		};
	},
	
	/**
	 * Is IValue an IInteger?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_int {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isInteger());
			return sp;
		};
	},
	
	/**
	 * Is IValue an IList?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_list {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isList());
			return sp;
		};
	},
	
	/**
	 * Is IValue an IListRelation?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_lrel {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isListRelation());
			return sp;
		};
	},
	
	/**
	 * Is IValue an ISourceLocation?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_loc {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isSourceLocation());
			return sp;
		};
	},
	
	/**
	 * Is IValue an IMap?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_map {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isMap());
			return sp;
		};
	},
	
	/**
	 * Is Object an mmap?
	 * 
	 * [ ..., Object ] => [ ..., mbool ]
	 *
	 */
	is_mmap {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(stack[sp - 1] instanceof Map);
			return sp;
		};
	},
	
	/**
	 * Is IValue an INode?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_node {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isNode());
			return sp;
		};
	},
	
	/**
	 * Is IValue an INumber?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_num {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isNumber());
			return sp;
		};
	},
	
	/**
	 * Is IValue an IReal?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_real {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isReal());
			return sp;
		};
	},
	
	/**
	 * Is IValue an IRational?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_rat {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isRational());
			return sp;
		};
	},
	
	/**
	 * Is IValue an IRelation?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_rel {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isRelation());
			return sp;
		};
	},
	
	/**
	 * Is IValue an ISet?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_set {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isSet());
			return sp;
		};
	},
	
	/**
	 * Is IValue an IString?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_str {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isString());
			return sp;
		};
	},
	
	/**
	 * Is IValue an ITuple?
	 * 
	 * [ ..., IValue ] => [ ..., mbool ]
	 *
	 */
	is_tuple {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.bool(((IValue) stack[sp - 1]).getType().isTuple());
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			IMap map = ((IMap) stack[sp - 1]);
			IListWriter writer = vf.listWriter();
			for (IValue key : map) {
				writer.append(key);
			}
			stack[sp - 1] = writer.done();
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			IMap map = ((IMap) stack[sp - 1]);
			IListWriter writer = vf.listWriter();
			for (IValue key : map) {
				writer.append(map.get(key));
			}
			stack[sp - 1] = writer.done();
			return sp;
		};
	},
	
	/**
	 * mbool= mint1 <= mint2
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., mbool ]
	 *
	 */
	less_equal_mint_mint {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = vf.bool(((Integer) stack[sp - 2]) <= ((Integer) stack[sp - 1]));
			return sp - 1;
		};
	},
	
	/**
	 * mbool = mint1 < mint2
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., mbool ]
	 *
	 */
	less_mint_mint {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = vf.bool(((Integer) stack[sp - 2]) < ((Integer) stack[sp - 1]));
			return sp - 1;
		};
	},
	
	make_iarray {	// TODO replace by make_array?
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			int len = ((Integer) stack[sp - 1]);
			stack[sp - 1] = new IValue[len];
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			int len = ((Integer) stack[sp - 1]);
			stack[sp - 1] = new Object[len];
			return sp;
		};
	},	
	
	/**
	 * Create a descendant descriptor given
	 * - a unique id
	 * - symbolset (converted from ISet of values to HashSet of Types, symbols and Productions)
	 * - concreteMatch, indicates a concrete or abstract match
	 * - definitions needed for type reifier
	 * 
	 * [ ISet symbolset, IBool concreteMatch, IMap definitions] => DescendantDescriptor
	 */
	make_descendant_descriptor {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 4;
			IString id = (IString) stack[sp - 4];
			DescendantDescriptor desc = descendantDescriptorMap.get(id);
			if(desc == null){
				ISet symbolset = (ISet) stack[sp - 3];
				IBool concreteMatch = (IBool) stack[sp - 2];
				IMap definitions = (IMap) stack[sp - 1];
				desc = new DescendantDescriptor(vf, symbolset, definitions, concreteMatch);
				descendantDescriptorMap.put(id,  desc);
			}
			stack[sp - 4] = desc;
			return sp - 3;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 0;
			HashSet<IValue> mset = new HashSet<IValue>();
			stack[sp] = mset;
			return sp + 1;
		};
	},
	
	/**
	 * Create a new mmap from keyword name (String) to an MapEntry <Type, IValue>
	 * 
	 * [ ... ] => [ ..., mmap ]
	 */
	make_mmap_str_entry {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 0;
			stack[sp] = new HashMap<String, Map.Entry<Type, IValue>>();
			return sp + 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = new AbstractMap.SimpleEntry<Type, IValue>((Type) stack[sp - 2], (IValue) stack[sp - 1]);
			return sp - 1;
		};
	},
	
	/**
	 * Given IString_1, IValue_1, ..., IString_n, IValue_n, create a keword map with <String_i, IValue_i> as entries
	 *
	 * [ ..., IString_1, IValue_1, ..., IString_n, IValue_n ] => [ ..., mmap ]
	 */
	make_mmap {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity >= 0;
			
			if(arity == 0){
				stack[sp] = new HashMap<String, IValue>();
				return sp + 1;
			}
			Map<String, IValue> writer = new HashMap<String, IValue>();
			for (int i = arity; i > 0; i -= 2) {
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
	 * [ ..., mmap, String] => [ ..., mbool ]
	 *
	 */
	mmap_contains_key {
		@SuppressWarnings("unchecked")
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			Map<String,IValue> m = (Map<String,IValue>) stack[sp - 2];
			String key = ((String) stack[sp - 1]);
			stack[sp - 2] = vf.bool(m.containsKey(key));
			return sp - 1;
		};
	},
	
	make_iterator {
		@SuppressWarnings("unchecked")
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			Object iteratee = stack[sp - 1];
			if(iteratee instanceof Object[]){
				stack[sp - 1] = new ArrayIterator<Object>((Object[]) iteratee);
			} else 
			if(iteratee instanceof AnnotatedAmbFacade){
					stack[sp - 1] = ((AnnotatedAmbFacade) iteratee).getAlternatives().iterator();
			} else
			if(iteratee instanceof AnnotatedConstructorFacade){
				stack[sp - 1] = ((AnnotatedConstructorFacade) iteratee).getChildren().iterator();
			} else {
				stack[sp - 1] = ((Iterable<IValue>) iteratee).iterator();
			}
			
			return sp;
		};
	},
	iterator_hasNext {
		@SuppressWarnings("unchecked")
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			Iterator<IValue> iter = (Iterator<IValue>) stack[sp - 1];
			stack[sp - 1] = vf.bool(iter.hasNext());
			return sp;
		};
	},
	iterator_next {
		@SuppressWarnings("unchecked")
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			Iterator<IValue> iter = (Iterator<IValue>) stack[sp - 1];
			stack[sp - 1] = iter.next();
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			Integer x = ((Integer) stack[sp - 2]);
			Integer y = ((Integer) stack[sp - 1]);
			stack[sp - 2] = x < y ? x : y;
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			Integer x = ((Integer) stack[sp - 2]);
			Integer y = ((Integer) stack[sp - 1]);
			stack[sp - 2] = x > y ? x : y;
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			if(stack[sp - 1] instanceof IInteger){
				stack[sp - 1] = ((IInteger) stack[sp - 1]).intValue();
			}
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			if(stack[sp - 1] instanceof IString){
				stack[sp - 1] = ((IString) stack[sp - 1]).getValue();
			}
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = ((Integer) stack[sp - 2])
					% ((Integer) stack[sp - 1]);
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			ISet set = ((ISet) stack[sp - 1]);
			int n = set.size();
			HashSet<IValue> mset = n > 0 ? new HashSet<IValue>(n)
					: emptyMset;
			for (IValue v : set) {
				mset.add(v);
			}
			stack[sp - 1] = mset;
			return sp;
		};
	},
	
	/**
	 * mset = ISet1 - ISet2
	 *
	 * [ [ ..., ISet1, ISet2 ] => [ ..., mset ]
	 */
	mset_set_subtract_set {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			ISet set1 = ((ISet) stack[sp - 2]);
			int n = set1.size();
			ISet set2 = ((ISet) stack[sp - 1]);
			HashSet<IValue> mset = n > 0 ? new HashSet<IValue>(n)
					: emptyMset;
			for (IValue v : set1) {
				if(!set2.contains(v)){
					mset.add(v);
				}
			}
			stack[sp - 2] = mset;
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 0;
			stack[sp] = emptyMset;
			return sp + 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
			IListWriter writer = vf.listWriter();
			for (IValue elem : mset) {
				writer.append(elem);
			}
			stack[sp - 1] = writer.done();
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 2];
			if(mset == emptyMset){
				mset = (HashSet<IValue>) emptyMset.clone();
			}
			IValue elm = ((IValue) stack[sp - 1]);
			mset.add(elm);
			stack[sp - 2] = mset;
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			HashSet<IValue> lhs = (HashSet<IValue>) stack[sp - 2];
			if(lhs == emptyMset){
				lhs = (HashSet<IValue>) emptyMset.clone();
			}
			// lhs = (HashSet<IValue>) lhs.clone();
			HashSet<IValue> rhs = (HashSet<IValue>) stack[sp - 1];
			lhs.addAll(rhs);
			stack[sp - 2] = lhs;
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = ((Integer) stack[sp - 2])
					* ((Integer) stack[sp - 1]);
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			HashSet<IValue> lhs = (HashSet<IValue>) stack[sp - 2];
			// lhs = (HashSet<IValue>) lhs.clone();
			HashSet<IValue> rhs = (HashSet<IValue>) stack[sp - 1];

			lhs.removeAll(rhs);
			stack[sp - 2] = lhs;
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			HashSet<IValue> lhs = (HashSet<IValue>) stack[sp - 2];
			lhs = (HashSet<IValue>) lhs.clone();
			HashSet<IValue> rhs = (HashSet<IValue>) stack[sp - 1];
			lhs.removeAll(rhs);
			stack[sp - 2] = lhs;
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 2];
			mset = (HashSet<IValue>) mset.clone();
			ISet set = ((ISet) stack[sp - 1]);
			for (IValue v : set) {
				mset.remove(v);
			}
			stack[sp - 2] = mset;
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 2];
			mset = (HashSet<IValue>) mset.clone();
			ISet set = ((ISet) stack[sp - 1]);
			for (IValue v : set) {
				mset.remove(v);
			}
			stack[sp - 2] = mset;
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 2];
			IValue elm = ((IValue) stack[sp - 1]);
			mset.remove(elm);
			stack[sp - 2] = mset;
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 2];
			mset = (HashSet<IValue>) mset.clone();
			IValue elm = ((IValue) stack[sp - 1]);
			mset.remove(elm);
			stack[sp - 2] = mset;
			return sp - 1;
		};
	},
	
	/**
	 * mbool = (mint1 != mint2)
	 * 
	 * [ ..., mint1, mint2 ] => [ ..., mbool ]
	 *
	 */
	not_equal_mint_mint {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = vf.bool(((Integer) stack[sp - 2]) != ((Integer) stack[sp - 1]));
			return sp - 1;
		};
	},
	
	/**
	 * mbool2 = !mbool1
	 * 
	 * [ ..., mbool1 ] => [ ..., mbool2 ]
	 *
	 */
	not_mbool {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = ((IBool) stack[sp - 1]).not();
			return sp;
		};
	},
	
	/**
	 * mbool = IList2 is a sublist of IList1 at start
	 * 
	 * [ ..., IList1, IList2, start ] => [ ..., mbool ]
	 */
	occurs_list_list_mint {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			IList sublist = ((IList) stack[sp - 3]);
			int nsub = sublist.length();
			IList list = ((IList) stack[sp - 2]);
			Integer start = (Integer) stack[sp - 1];
			int nlist = list.length();
			stack[sp - 3] = Rascal_FALSE;
			int newsp = sp - 2;
			if (start + nsub <= nlist) {
				for (int i = 0; i < nsub; i++) {
					if (!sublist.get(i).isEqual(list.get(start + i)))
						return newsp;
				}
			} else {
				return newsp;
			}

			stack[sp - 3] = Rascal_TRUE;
			return newsp;
		};
	},
	
	one_dot_zero {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 0;
			stack[sp] = vf.real("1.0");
			return sp + 1;
		};
	},
	
	/**
	 * mbool3 = (mbool1 || mbool2)
	 * 
	 * [ ..., mbool1, mbool2 ] => [ ..., mbool3 ]
	 *
	 */
	or_mbool_mbool {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IBool) stack[sp - 2]).or((IBool) stack[sp - 1]);
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			int n1 = ((Integer) stack[sp - 2]);
			int n2 = ((Integer) stack[sp - 1]);
			int pow = 1;
			for (int i = 0; i < n2; i++) {
				pow *= n1;
			}
			stack[sp - 2] = pow;
			return sp - 1;
		};
	},
	
	/**
	 * Convert mint to Rascal int (IInteger)
	 * 
	 * [ ..., mint ] => [ ..., IInteger ]
	 */
	rint {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.integer((Integer) stack[sp - 1]);
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			String RegExpAsString = ((IString) stack[sp - 2]).getValue();
			IValue isubject = (IValue) stack[sp - 1];
			String subject;
			if(isubject instanceof IString){
				subject =  ((IString) isubject).getValue();
			} else {
				StringWriter w = new StringWriter();
				IConstructor c = (IConstructor) isubject;
				try {
					TreeAdapter.unparse(c, w);
				} catch (FactTypeUseException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				subject = w.toString();
			}
			//System.err.println("regexp_compile: \"" + RegExpAsString + "\" and \"" + subject + "\" len = " + subject.length());
			try {
				Pattern pat = Pattern.compile(RegExpAsString, Pattern.UNICODE_CHARACTER_CLASS);
				stack[sp - 2] = pat.matcher(subject);
				return sp - 1;
			} catch (PatternSyntaxException e) {
				//throw new CompilerError("Syntax error in regular expression: " + RegExpAsString);
				//TODO: change to something like:
				throw RascalRuntimeException.RegExpSyntaxError(RegExpAsString, null);
			}
		};
	},
	
	/**
	 * Start RegExp Matcher
	 * 
	 * [ ..., Matcher ] => [ ..., Matcher ]
	 */
	regexp_begin {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			Matcher matcher = (Matcher) stack[sp - 1];
			stack[sp - 1] = vf.integer(matcher.start());
			return sp;
		};
	},
	
	/**
	 * Stop RegExp Matcher
	 * 
	 * [ ..., Matcher ] => [ ..., Matcher ]
	 */
	regexp_end {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			Matcher matcher = (Matcher) stack[sp - 1];
			stack[sp - 1] = vf.integer(matcher.end());
			return sp;
		};
	},
	
	/**
	 * Find next RegExp match
	 * 
	 * [ ..., Matcher ] => [ ..., mbool ]
	 */
	regexp_find {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			Matcher matcher = (Matcher) stack[sp - 1];
			stack[sp - 1] = vf.bool(matcher.find());
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			Matcher matcher = (Matcher) stack[sp - 3];
			int start = ((Integer) stack[sp - 2]);
			int end = ((Integer) stack[sp - 1]);
			stack[sp - 1] = matcher.region(start, end);
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			Matcher matcher = (Matcher) stack[sp - 2];
			int idx = (Integer) stack[sp - 1];
			stack[sp - 2] = vf.string(matcher.group(idx));
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
			ISetWriter w = vf.setWriter();
			for (IValue v : mset) {
				w.insert(v);
			}
			stack[sp - 1] = w.done();
			return sp;
		};
	},
	
	/**
	 * Convert an ISet to an IList
	 * 
	 * [ ..., ISet ] => [ ..., IList ]
	 */
	set2list {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			ISet set = (ISet) stack[sp - 1];
			IListWriter writer = vf.listWriter();
			for (IValue elem : set) {
				writer.append(elem);
			}
			stack[sp - 1] = writer.done();
			return sp;
		};
	},
	
	/**
	 * mbool = (ISet < mset)
	 *
	 * [ ..., ISet, mset ] => [ ..., mbool ]
	 */
	set_is_subset_of_mset {
		@Override
		@SuppressWarnings("unchecked")
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			ISet subset = ((ISet) stack[sp - 2]);
			HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
			for (IValue v : subset) {
				if (!mset.contains(v)) {
					stack[sp - 2] = Rascal_FALSE;
					return sp - 1;
				}
			}
			stack[sp - 2] = Rascal_TRUE;
			return sp - 1;
		};
	},
	
	/**
	 * Size of array
	 * 
	 * [ ..., array ] => [ ..., mint ]
	 */
	size_array {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = ((Object[]) stack[sp - 1]).length;
			return sp;
		};
	},
	
	/**
	 * Size of IList
	 * 
	 * [ ..., IList ] => [ ..., mint ]
	 */
	size_list {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = ((IList) stack[sp - 1]).length();
			return sp;
		};
	},
	
	/**
	 * Size of ISet
	 * 
	 * [ ..., ISet ] => [ ..., mint ]
	 */
	size_set {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = ((ISet) stack[sp - 1]).size();
			return sp;
		};
	},
	
	/**
	 * Size of mset
	 * 
	 * [ ..., mset ] => [ ..., mint ]
	 */
	size_mset {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = ((HashSet<?>) stack[sp - 1]).size();
			return sp;
		};
	},
	
	/**
	 * Size of IMap
	 * 
	 * [ ..., IMap ] => [ ..., mint ]
	 */
	size_map {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = ((IMap) stack[sp - 1]).size();
			return sp;
		};
	},
	
	/**
	 * Size of IString
	 * 
	 * [ ..., IString ] => [ ..., mint ]
	 */
	size_str {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = ((IString) stack[sp - 1]).length();
			return sp;
		};
	},
	
	/**
	 * Size of ITuple
	 * 
	 * [ ..., ITuple ] => [ ..., mint ]
	 */
	size_tuple {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			stack[sp - 1] = ((ITuple) stack[sp - 1]).arity();
			return sp;
		};
	},
	
	/**
	 * Generic size function
	 * 
	 * [ ..., IValue ] => [ ..., mint ]
	 */
	size {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			if (stack[sp - 1] instanceof IString) {
				stack[sp - 1] = ((IString) stack[sp - 1]).length();
			} else if (stack[sp - 1] instanceof IConstructor) {
				stack[sp - 1] = ((IConstructor) stack[sp - 1]).arity();
			} else if (stack[sp - 1] instanceof INode) {
				stack[sp - 1] = ((INode) stack[sp - 1]).arity();
			} else if (stack[sp - 1] instanceof IList) {
				stack[sp - 1] = ((IList) stack[sp - 1]).length();
			} else if (stack[sp - 1] instanceof ISet) {
				stack[sp - 1] = ((ISet) stack[sp - 1]).size();
			} else if (stack[sp - 1] instanceof IMap) {
				stack[sp - 1] = ((IMap) stack[sp - 1]).size();
			} else if (stack[sp - 1] instanceof ITuple) {
				stack[sp - 1] = ((ITuple) stack[sp - 1]).arity();
			}
			return sp;
		};
	},
	
	/**
	 * 	IList2 occurs as subslist in IList1 at position start
	 * 
	 * [ ..., IList1, IList2, start] => [ ..., mbool ]
	 *
	 */
	starts_with {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			IList sublist = (IList) stack[sp - 3];
			IList list = (IList) stack[sp - 2];
			int start = (Integer) stack[sp - 1];
			IBool eq = Rascal_TRUE;

			if (start + sublist.length() <= list.length()) {
				for (int i = 0; i < sublist.length() && eq.getValue(); i++) {
					if (!sublist.get(i).equals(list.get(start + i))) {
						eq = Rascal_FALSE;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			IList lst = (IList) stack[sp - 3];
			int offset = ((Integer) stack[sp - 2]);
			int length = ((Integer) stack[sp - 1]);
			stack[sp - 3] = lst.sublist(offset, length);
			return sp - 2;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = ((Object[]) stack[sp - 2])[((Integer) stack[sp - 1])];
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = ((Object[]) stack[sp - 2])[((IInteger) stack[sp - 1]).intValue()];
			return sp - 1;
		};
	},
	
	/**
	 * IValue = IList[mint]
	 * 
	 * [ ..., IList, mint ] => [ ..., IValue ]
	 */
	subscript_list_mint {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IList) stack[sp - 2]).get((Integer) stack[sp - 1]);
			return sp - 1;
		};
	},
	
	/**
	 * IValue = ITuple[mint]
	 * 
	 * [ ..., ITuple, mint ] => [ ..., IValue ]
	 */
	subscript_tuple_mint {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = ((ITuple) stack[sp - 2]).get((Integer) stack[sp - 1]);
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			IString subject = ((IString)  stack[sp - 3]);
			Integer start = ((Integer)  stack[sp - 2]);
			Integer end  = ((Integer)  stack[sp - 1]);
			//System.err.println("substring: " + subject + ", " + start + ", " + end);
			stack[sp - 3] = subject.substring(start, end);
			return sp - 2;
		};
	},
	
	/**
	 * mbool = IString2 is tail of IString1 at start
	 *	[ ..., IString1, IString2, start ] => [ ..., mbool ]
	 */
	is_tail_str_str_mint {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 3;
			IString subject = ((IString)  stack[sp - 3]);
			IString substr = ((IString)  stack[sp - 2]);
			Integer start = ((Integer)  stack[sp - 1]);
			if(start + substr.length() == subject.length()){
				stack[sp - 3] = vf.bool(subject.substring(start, start + substr.length()).compare(substr) == 0);
			} else {
				stack[sp - 3] = Rascal_FALSE;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = ((Integer) stack[sp - 2]) - ((Integer) stack[sp - 1]);
			return sp - 1;
		};
	},
	
	/**
	 * mbool = Type1 < Type2
	 * 
	 * [ ..., Type1, Type2 ] => [ ..., mbool ]
	 */
	subtype {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = vf.bool(((Type) stack[sp - 2]).isSubtypeOf((Type) stack[sp - 1]));
			return sp - 1;
		};
	},
	
	/**
	 * Get type of an IValue or mint
	 * 
	 * [ ..., IValueOrMint ] => [ ..., mbool ]
	 */
	typeOf {
		@Override
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			if (stack[sp - 1] instanceof Integer) {
				stack[sp - 1] = TypeFactory.getInstance().integerType();
			} else {
				stack[sp - 1] = ((IValue) stack[sp - 1]).getType();
			}
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			if (stack[sp - 1] instanceof HashSet) {
				HashSet<IValue> mset =  (HashSet<IValue>) stack[sp - 1];
				Type elmType = TypeFactory.getInstance().voidType();
				for (IValue elm : mset) {
					elmType = elm.getType().lub(elmType);
				}
				stack[sp - 1] = TypeFactory.getInstance().setType(elmType);
			}
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			Reference ref = (Reference) stack[sp - 1];
			stack[sp - 1] = ref.getValue();
			ref.undefine();
			return sp;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			stack[sp - 2] = ((Integer) stack[sp - 2]) * ((Integer) stack[sp - 1]);
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 2;
			Object[] subject = new Object[2];
			subject[0] = stack[sp - 2];
			subject[1] = stack[sp - 1];
			stack[sp - 2] = subject;
			return sp - 1;
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
		public int execute(final Object[] stack, final int sp, final int arity) {
			assert arity == 1;
			Object[] subject = (Object[]) stack[sp - 1];
			IList listSubject = (IList) subject[0];
			Integer cursor = (Integer) subject[1];
			int len = listSubject.length();
			if(cursor == len){
				stack[sp - 1] = Rascal_TRUE;
				return sp;
			}
			for(int i = cursor; i < len; i++){				// Check wether only nullables follow
				if(!$is_nullable(listSubject.get(i))){		// TODO maybe better to make a separate accept for the concrete case
					stack[sp - 1] = Rascal_FALSE;
					return sp;
				}
			}
			stack[sp - 1] = Rascal_TRUE;
			return sp;
		};
	}
	
	;

	private static IValueFactory vf;

	// Changed values to public (Ferry)
	public static final MuPrimitive[] values = MuPrimitive.values();
	private static final HashSet<IValue> emptyMset = new HashSet<IValue>(0);
	
	private static IBool Rascal_TRUE;
	private static IBool Rascal_FALSE;
	
	private static final Map<String, IValue> emptyKeywordMap = new  HashMap<String, IValue>();
	
	private static final HashMap<IString,DescendantDescriptor> descendantDescriptorMap= new HashMap<IString,DescendantDescriptor>();

	public static MuPrimitive fromInteger(int muprim) {
		return values[muprim];
	}

	private static boolean isNonTerminalType(Type t) {
		return t.isExternalType() && ((RascalType) t).isNonterminal();
	}
	
	public int execute(final Object[] stack, final int sp, final int arity) {
		System.err.println("Not implemented mufunction");
		return 0;
	}
	/**
	 * Initialize the primitive methods.
	 * 
	 * @param fact the value factory to be used
	 */
	public static void init(IValueFactory fact) {
		vf = fact;
		Rascal_TRUE = vf.bool(true);
		Rascal_FALSE = vf.bool(false);
	}

	public static void exit() {
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
	
//	private static boolean $is_layout(final IValue v){
//		if (isNonTerminalType(v.getType())) {
//			return TreeAdapter.isLayout((ITree) v);
//		}
//		return false;
//	}
	
	private static boolean $is_nullable(final IValue v){
		if (v instanceof ITree) {
			return TreeAdapter.getArgs((ITree) v).length() == 0;
		}
		return false;
	}
	
//	/**
//	 * @param descendantDescriptor
//	 * @return its 'id' element
//	 */
//	protected static IString $descendant_get_id(final Object[] descendantDescriptor){
//		return (IString) descendantDescriptor[0];
//	}
//	
//	/**
//	 * @param descendantDescriptor
//	 * @return its symbolset element
//	 */
//	@SuppressWarnings("unchecked")
//	protected static HashSet<Object> $descendant_get_symbolset(final Object[] descendantDescriptor){
//		return (HashSet<Object>) descendantDescriptor[1];
//	}
//	
//	/**
//	 * @param descendantDescriptor
//	 * @return its concreteMatch element
//	 */
//	protected static IBool $descendant_is_concrete_match(Object[] descendantDescriptor){
//		return (IBool) descendantDescriptor[2];
//	}
	 
	
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
