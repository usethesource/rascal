package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.result.util.MemoizationCache;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.DescendantDescriptor;
import org.rascalmpl.uri.SourceLocationURICompare;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.IRascalValueFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListRelation;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetRelation;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.exceptions.InvalidDateTimeException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/*
 * The primitives that can be called via the CALLPRIM instruction are defined here.
 * Each primitive with name P (e.g., int_add_int) is defined by:
 * - an enumeration constant P (e.g., int_add_int)
 * - a method 
 *     int execute(Object[] stack, int sp, int arity, Frame currentFrame, RascalexecutionContext rex) 
 *   associated with that enumeration constant.
 * 
 * The arguments of 'execute' have the following meaning:
 * 
 * - the current stack
 * - the current stack pointer (an index in 'stack')
 * - its arity
 * - the current stack frame
 * - the current RascalExecutionContext
 * 
 * and returns a new stack pointer (an index in 'stack'). 
 * 
 * 'execute' is only allowed to make modifications to the stack and usually returns an IValue
 * on top of the stack.
 *
 * This enumeration is organized in the following sections:
 * - Creation of values and some utilities on them (~ line 90)
 * - Readers and writers (~ line 605)
 * - Operators (~ line 770)
 * - Type-related operators and functions (~ line 6290)
 * - String templates (~ line 6748)
 * - Fields and Field updates (~ line 6849)
 * - Various getters (~ line 7700)
 * - Slices ( ~ line 7854)
 * - Subscripting (~ line 8219)
 * - Annotations (~ line 8719)
 * - Type reachability for descendant match (~ line 8792)
 * - Miscellaneous  (~ line 8871)
 * - Initialization and auxiliary functions (> line 9000)
 * 
 * Some further clarifications:
 * - Completely generic primitives like 'add', 'subtract', and 'join' are needed
 *   in cases where they operate on a parameter type. For a function that uses 
 *   one or more type parameters, still a single translation is generated that 
 *   is shared across all type instantiations of the parameters. Under those circumstances,
 *   there is no static knowldege of the argument types of these primitives and they have to
 *   be determined at run-time.
 * 
 */

public enum RascalPrimitive {
	/************************************************************************************************/
	/*				Creation of values and some utilities on them									*/
	/************************************************************************************************/

	/**
	 * Build a constructor
	 * 
	 * [ ..., Type type,  IValue[] args, Map kwArgs ] => [ ..., constructor value ]
	 *
	 */
	constructor {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			Type type = (Type) stack[sp - 3]; 
			IValue[] args = (IValue[]) stack[sp - 2];
			@SuppressWarnings("unchecked")
			Map<String,IValue> kwargs = (Map<String,IValue>) stack[sp - 1];
			stack[sp - 3] = vf.constructor(type, args, kwargs);
			return sp - 2;
		}
	},

	/**
	 * Build a node, given args and kwArgs
	 * 
	 * [ ..., IString name,  IValue[] args, Map kwArgs ] => [ ..., node value]
	 *
	 */
	node {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			String name = ((IString) stack[sp - 3]).getValue(); 
			IValue[] args = (IValue[]) stack[sp - 2];
			@SuppressWarnings("unchecked")
			Map<String,IValue> kwargs = (Map<String,IValue>) stack[sp - 1];
			stack[sp - 3] = vf.node(name, args, kwargs);
			return sp - 2;
		}
	},
	
	/**
	 * Build a node, given args on stack
	 * 
	 * [ ..., IString name, IValue arg1, IValue arg2, ... ] => [ ..., name(arg1, arg2, ...) ]
	 */
	node_create {
		@SuppressWarnings("unchecked")
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity >= 1;

			String name = ((IString) stack[sp - arity]).getValue();
			IValue[] args = new IValue[arity - 2];
			for(int i = 0; i < arity - 2; i ++){
				args[i] = (IValue) stack[sp - arity + 1 + i];
			}
			stack[sp - arity] = vf.node(name, args, (Map<String, IValue>)stack[sp - 1]);
			return sp - arity + 1;
		}
	},

	/**
	 * Build a list, given list of elements
	 * 
	 * [ ..., IValue[] args ] => [ ..., list value]
	 *
	 */
	list {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue[] args = (IValue[]) arg_1;
			return (args.length == 0) ? emptyList : vf.list(args);
		}
	},
	
	/**
	 * Build a list, given elements on stack
	 * 
	 * [ ... IValue val1, IValue val2, ... ] => [ ..., [val1, val2, ...] ]
	 */
	list_create {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity >= 0;

			if(arity == 0){
				stack[sp] = emptyList;
				return sp + 1;
			}

			IListWriter writer = vf.listWriter();

			for (int i = arity - 1; i >= 0; i--) {
				writer.append((IValue) stack[sp - 1 - i]);
			}
			int sp1 = sp - arity + 1;
			stack[sp1 - 1] = writer.done();

			return sp1;
		}
	},
	
	/**
	 * size of list
	 * 
	 * [ ... IList val] => [ ...,IInteger size ]
	 */	
	list_size {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.integer(((IList) arg_1).length());
		}
	},
	
	/**
	 * sublist of list
	 * 
	 * [ ..., IList lst, IInteger offset, IInteger length ] => [ ..., lst[offset .. offset+length] ]
	 */
	sublist {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			IList lst = (IList) stack[sp - 3];
			int offset = ((IInteger) stack[sp - 2]).intValue();
			int length = ((IInteger) stack[sp - 1]).intValue();
			stack[sp - 3] = lst.sublist(offset, length);
			return sp - 2;
		}
	},

	/**
	 * Build a list relation, given list of elements
	 * 
	 * [ ..., IValue[] args ] => [ ..., list relation value]
	 *
	 */
	lrel {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue[] args = (IValue[]) arg_1;
			return (args.length == 0) ? emptyList : vf.list(args);
		}
	},

	/**
	 * Build a set, given list of elements
	 * 
	 * [ ..., IValue[] args ] => [ ..., set value]
	 *
	 */
	set {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue[] args = (IValue[]) arg_1;
			return (args.length == 0) ? emptySet : vf.set(args);
		}
	},
	
	/**
	 * Build a set, given elements on stack
	 * 
	 *  * [ ... IValue val1, IValue val2, ... ] => [ ..., {val1, val2, ...} ]
	 */
	set_create {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity >= 0;

			if(arity == 0){
				stack[sp] = emptySet;
				return sp + 1;
			}

			ISetWriter writer = vf.setWriter();

			for (int i = arity - 1; i >= 0; i--) {
				writer.insert((IValue) stack[sp - 1 - i]);
			}
			int sp1 = sp - arity + 1;
			stack[sp1 - 1] = writer.done();

			return sp1;
		}

	},
	
	/**
	 * Convert single-element set to element
	 * 
	 * [ ..., ISet set ] => [ ..., IValue elm ]
	 */
	set2elm {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet set = (ISet) arg_1;
			if(set.size() != 1)
				throw new InternalCompilerError("set2elm: set should have a single element", rex.getStdErr(), currentFrame);
			return set.iterator().next();
		}
	},

	/**
	 * size of set
	 * 
	 * [ ... ISet val] => [ ..., IInteger size ]
	 */	
	set_size {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet set = (ISet) arg_1;		
			return vf.integer(set.size());
		}
	},

	/**
	 * Build a relation, given list of elements
	 * 
	 * [ ..., IValue[] args ] => [ ..., relation value]
	 *
	 */
	rel {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue[] args = (IValue[]) arg_1;
			return (args.length == 0) ? emptySet : vf.set(args);
		}
	},

	/**
	 * Build a tuple, given list of elements
	 * 
	 * [ ..., IValue[] args ] => [ ..., tuple value]
	 *
	 */
	tuple {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue[] args = (IValue[]) arg_1;
			return vf.tuple(args);
		}
	},
	
	/**
	 * Build a tuple, given elements on stack
	 * 
	 *  * [ ... IValue val1, IValue val2, ... ] => [ ..., <val1, val2, ...> ]
	 */
	tuple_create {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity >= 0;
			IValue[] elems = new IValue[arity];

			for (int i = arity - 1; i >= 0; i--) {
				elems[i] = (IValue) stack[sp - arity + i];
			}
			int sp1 = sp - arity + 1;
			stack[sp1 - 1] = vf.tuple(elems);
			return sp1;
		}
	},

	/**
	 * Create a map, given key, value pairs on stack
	 * 
	 * [ ... IValue key1, IValue val1, IValue key2, IValue val2... ] => [ ..., (key1 : val1, key2 : val2, ...) ]
	 */
	map_create {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity >= 0;

			if(arity == 0){
				stack[sp] = emptyMap;
				return sp + 1;
			}

			IMapWriter writer = vf.mapWriter();

			for (int i = arity; i > 0; i -= 2) {
				writer.put((IValue) stack[sp - i], (IValue) stack[sp - i + 1]);
			}
			int sp1 = sp - arity + 1;
			stack[sp1 - 1] = writer.done();

			return sp1;
		}
	},

	/**
	 * Create a loc
	 * [ ..., IString uri ] => [ ..., ISourceLocation l ]
	 */
	loc_create {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IString uri = ((IString) arg_1);

			try {
				return URIUtil.createFromURI(uri.getValue());
			} 
			catch (URISyntaxException e) {
				// this is actually an unexpected run-time exception since Rascal prevents you from 
				// creating non-encoded 
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidURI(uri.getValue(), currentFrame));
			}
			catch (UnsupportedOperationException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidURI(uri.getValue() + ":" + e.getMessage(), currentFrame));
			}
		}
	},

	/**
	 * Create a loc with given offsets and length
	 * [ ..., IString uri, IInteger offset, IInteger length, IInteger beginLine, IInteger beginCol, IInteger endLine, IInteger endCol] => [ ..., ISourceLocation l ]
	 */
	loc_with_offset_create {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 5;
			ISourceLocation loc = (ISourceLocation) stack[sp - arity];
			int offset = ((IInteger) stack [sp - arity + 1]).intValue();
			int length = ((IInteger) stack [sp - arity + 2]).intValue();

			ITuple begin = (ITuple) stack [sp - arity + 3];
			int beginLine = ((IInteger) begin.get(0)).intValue();
			int beginCol = ((IInteger) begin.get(1)).intValue();

			ITuple end = (ITuple) stack [sp - arity + 4];
			int endLine = ((IInteger) end.get(0)).intValue();
			int endCol = ((IInteger)  end.get(1)).intValue();

			stack[sp - arity] = vf.sourceLocation(loc, offset, length, beginLine, endLine, beginCol, endCol);
			return sp - arity + 1;
		}
	},

	/**
	 * Create a nonterminal value
	 * 
	 * [ ..., IValue prod, IValue args ] => [ ..., appl(prod, args) ]
	 */
	appl_create {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue prod = (IValue) arg_2;
			IValue args = (IValue) arg_1;

			return vf.constructor(RascalValueFactory.Tree_Appl, prod, args);
		}
	},

	/**
	 * Create a reified type
	 * 
	 * [ ..., IConstructor type_cons, IMap definitions ] => [ ..., Type t ]
	 */
	reifiedType_create {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IConstructor type_cons = (IConstructor) arg_2;
			IMap idefinitions = (IMap) arg_1;

			return IRascalValueFactory.getInstance().reifiedType(type_cons, idefinitions);
		}
	},

	/*****************************************************************************************************/
	/*						Readers and writers															 */
	/*****************************************************************************************************/
	
	listwriter_open {
		@Override
		public Object execute0(final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.listWriter();
		}
	},

	/**
	 * Add values to a list writer
	 * 
	 * [ ..., IListWriter e, IValue arg1, IValue arg2, ... ] => [ ..., IListWriter e]
	 *
	 */
	listwriter_add {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity > 0;
			IListWriter writer = (IListWriter) stack[sp - arity];
			for(int i = arity - 1; i > 0; i--){
				writer.append((IValue) stack[sp - i]);
			}
			return sp - arity + 1;
		}
	},

	listwriter_close {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IListWriter writer = (IListWriter) arg_1;
			return writer.done();
			
		}
	},
	
	setwriter_open {
		@Override
		public Object execute0(final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.setWriter();
		}
	},

	/**
	 * Add values to a set writer
	 * 
	 * [ ..., ISetWriter e, IValue arg1, IValue arg2, ... ] => [ ..., ISetWriter e]
	 *
	 */
	setwriter_add {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity > 0;
			ISetWriter writer = (ISetWriter) stack[sp - arity];
			for(int i = arity - 1; i > 0; i--){
				writer.insert((IValue) stack[sp - i]);
			}
			return sp - arity + 1;
		}
	},

	setwriter_close {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISetWriter writer = (ISetWriter) arg_1;
			return writer.done();
		}
	},
	
	mapwriter_open {
		@Override
		public Object execute0(final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.mapWriter();
		}
	},

	/**
	 * Add values to a map writer
	 * 
	 * [ ..., IMapWriter e, IValue key1, IValue val1, IValue key2, IValue val2, ... ] => [ ..., IMapWriter e]
	 *
	 */
	mapwriter_add {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			IMapWriter writer = (IMapWriter) stack[sp - 3];
			writer.insert(vf.tuple((IValue) stack[sp - 2], (IValue) stack[sp - 1]));
			return sp - 2;
		}
	},

	mapwriter_close {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IMapWriter writer = (IMapWriter) arg_1;
			return writer.done();
		}
	},

	/**
	 * Create a string writer
	 * 
	 * [ ... ] => [ ..., new StringBuilder ]
	 */
	stringwriter_open {
		@Override
		public Object execute0(final Frame currentFrame, final RascalExecutionContext rex) {
			return new StringBuilder();
		}
	},

	/**
	 * Add a value to a stringwriter
	 * 
	 * [ ... StringBuilder sb, IValue val] => [ ..., sb with val appended ]
	 */
	stringwriter_add {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			StringBuilder b = (StringBuilder) arg_2;
			IValue v = ((IValue) arg_1);
			String s;
			if(v.getType().isString()){
				s = ((IString) v).getValue();
			} else {
				s = v.toString();
			}
			return b.append(s);
		}
	},

	/**
	 * Close a stringwriter
	 * 
	 * [ ... StringBuilder sb] => [ ..., IString string value of stringwriter ]
	 */
	stringwriter_close {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			StringBuilder b = (StringBuilder) arg_1;
			return vf.string(b.toString());
		}
	},

	/********************************************************************************************************/
	/*								Operators																*/
	/********************************************************************************************************/

	/**
	 * Definitions of all operators. The general strategy is to define a gegenric version of each operator (e.g. 'add') that
	 * works for all argument types as well as a collection of specialized versions (e.g., 'int_add_int') that only work for specific argument types
	 * 
	 * The following operators are defined here:
	 * addition
	 * compose
	 * division
	 * equal
	 * greater
	 * greaterequal
	 * in
	 * intersect
	 * join
	 * less
	 * lessequal
	 * mod
	 * negative
	 * notequal
	 * notin
	 * non_negative
	 * product
	 * remainder
	 * subtract
	 * transitive_closure
	 * transitive_reflexive_closure
	 */
	
	/**
	 * addition
	 * 
	 * [ ... IValue val1, IValue val2] => [ ..., val1 + val2]
	 *
	 * infix Addition "+"
	 * {  
	 *		&L <: num x &R <: num               -> LUB(&L, &R),

	 *		list[&L] x list[&R]                 -> list[LUB(&L,&R)],
	 *		list[&L] x &R              		  	-> list[LUB(&L,&R)] when &R is not a list,	  
	 *		&L x list[&R <: &L]                 -> list[LUB(&L,&R)] when &L is not a list,

	 *		set[&L] x set[&R]                   -> set[LUB(&L,&R)],
	 *		set[&L] x &R                        -> set[LUB(&L,&R)] when &R is not a list,
	 *		&L x set[&R]                        -> set[LUB(&L,&R)] when &L is not a list,

	 *		map[&K1,&V1] x map[&K2,&V2]         -> map[LUB(&K1,&K2), LUB(&V1,&V2)],

	 *		str x str                           -> str,
	 *		loc x str                           -> loc,
	 *		tuple[&L1,&L2] x tuple[&R1,&R2,&R3] -> tuple[&L1,&L2,&R1,&R2,&R3]
	 * }
	 */

	/**
	 * See general remark about generic primitives
	 */
	add {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue lhs = ((IValue) arg_2);
			IValue rhs = ((IValue) arg_1);
			ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
			ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
			switch (lhsType) {
			case INT:
				switch (rhsType) {
				case INT:
					return int_add_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return int_add_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return int_add_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return int_add_rat.execute2(lhs, rhs, currentFrame, rex);
				case LIST:
					return elm_add_list.execute2(lhs, rhs, currentFrame, rex);
				case SET:
					return elm_add_list.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive add: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case NUM:
				switch (rhsType) {
				case INT:
					return num_add_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return num_add_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return num_add_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return num_add_rat.execute2(lhs, rhs, currentFrame, rex);
				case LIST:
					return elm_add_list.execute2(lhs, rhs, currentFrame, rex);
				case SET:
					return elm_add_list.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive add: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case REAL:
				switch (rhsType) {
				case INT:
					return real_add_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return real_add_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return real_add_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return real_add_rat.execute2(lhs, rhs, currentFrame, rex);
				case LIST:
					return elm_add_list.execute2(lhs, rhs, currentFrame, rex);
				case SET:
					return elm_add_list.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive add: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case RAT:
				switch (rhsType) {
				case INT:
					return rat_add_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return rat_add_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return rat_add_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return rat_add_rat.execute2(lhs, rhs, currentFrame, rex);
				case LIST:
					return elm_add_list.execute2(lhs, rhs, currentFrame, rex);
				case SET:
					return elm_add_list.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive add: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case SET:
				return set_add_elm.execute2(lhs, rhs, currentFrame, rex);
				
			case LIST:
				return list_add_elm.execute2(lhs, rhs, currentFrame, rex);

			case LOC:
				switch (rhsType) {
				case STR:
					return loc_add_str.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive add: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case LREL:
				switch (rhsType) {
				case LIST:
					return lrel_add_list.execute2(lhs, rhs, currentFrame, rex);
				case LREL:
					return lrel_add_lrel.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive add: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case MAP:
				switch (rhsType) {
				case MAP:
					return map_add_map.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive add: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case REL:
				switch (rhsType) {
				case SET:
					return rel_add_set.execute2(lhs, rhs, currentFrame, rex);
				case REL:
					return rel_add_rel.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive add: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case STR:
				switch (rhsType) {
				case STR:
					return str_add_str.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive add: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case TUPLE:
				switch (rhsType) {
				case TUPLE:
					return tuple_add_tuple.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive add: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			default:
				switch (rhsType) {
				case SET:
					return elm_add_set.execute2(lhs, rhs, currentFrame, rex);
				case LIST:
					return elm_add_list.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive add: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			}
		}
	},

	/**
	 * addition on int and int
	 * 
	 * [ ... IInteger val1, IInteger val2] => [ ..., val1 + val2]
	 */
	int_add_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).add((IInteger) arg_1);
		}
	},

	/**
	 * addition on int and num
	 * 
	 * [ ... IInteger val1, INumber val2] => [ ..., val1 + val2]
	 */
	int_add_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).add((INumber) arg_1);
		}
	},

	/**
	 * addition on int and rational
	 * 
	 * [ ... IInteger val1, IRational val2] => [ ..., val1 + val2]
	 */
	int_add_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).add((IRational) arg_1);
		}
	},

	/**
	 * addition on int and real
	 * 
	 * [ ... IInteger val1, IReal val2] => [ ..., val1 + val2]
	 */
	int_add_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).add((IReal) arg_1);
		}
	},

	/**
	 * addition on num and int
	 * 
	 * [ ... INumber val1, IInteger val2] => [ ..., val1 + val2]
	 */
	num_add_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).add((IInteger) arg_1);
		}
	},
	/**
	 * addition on num and num
	 * 
	 * [ ... INumber val1, INumber val2] => [ ..., val1 + val2]
	 */
	num_add_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).add((INumber) arg_1);
		}
	},

	/**
	 * addition on num and rat
	 * 
	 * [ ... INumber val1, IRational val2] => [ ..., val1 + val2]
	 */
	num_add_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).add((IRational) arg_1);
		}
	},

	/**
	 * addition on num and real
	 * 
	 * [ ... INumber val1, IReal val2] => [ ..., val1 + val2]
	 */
	num_add_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).add((IReal) arg_1);
		}
	},

	/**
	 * addition on rat and int
	 * 
	 * [ ... IRational val1, IInteger val2] => [ ..., val1 + val2]
	 */
	rat_add_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).add((IInteger) arg_1);
		}
	},

	/**
	 * addition on rat and num
	 * 
	 * [ ... IRational val1, INumber val2] => [ ..., val1 + val2]
	 */
	rat_add_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).add((INumber) arg_1);
		}
	},
	/**
	 * addition on rat and rat
	 * 
	 * [ ... IRational val1, IRational val2] => [ ..., val1 + val2]
	 */
	rat_add_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).add((IRational) arg_1);
		}
	},

	/**
	 * addition on rat and real
	 * 
	 * [ ... IRational val1, Ireal val2] => [ ..., val1 + val2]
	 */
	rat_add_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).add((IReal) arg_1);
		}
	},

	/**
	 * addition on real and num
	 * 
	 * [ ... IReal val1, INumber val2] => [ ..., val1 + val2]
	 */
	real_add_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).add((INumber) arg_1);
		}
	},

	/**
	 * addition on real and int
	 * 
	 * [ ... IReal val1, IInteger val2] => [ ..., val1 + val2]
	 */
	real_add_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).add((IInteger) arg_1);
		}
	},
	/**
	 * addition on real and real
	 * 
	 * [ ... IReal val1, Ireal val2] => [ ..., val1 + val2]
	 */
	real_add_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).add((IReal) arg_1);
		}
	},

	/**
	 * addition on real and rat
	 * 
	 * [ ... IReal val1, IRational val2] => [ ..., val1 + val2]
	 */
	real_add_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).add((IRational) arg_1);
		}
	},

	// Add on non-numeric types

	/**
	 * addition on list and list
	 * 
	 * [ ... IList val1, IList val2] => [ ..., val1 + val2]
	 */

	list_add_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IList) arg_2).concat((IList) arg_1);
		}
	},

	/**
	 * addition on list and element
	 * 
	 * [ ... IList val1, IValue val2] => [ ..., val1 + val2]
	 */
	list_add_elm {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IList) arg_2).append((IValue) arg_1);
		}
	},

	/**
	 * addition on element and list
	 * 
	 * [ ... IValue val1, IList val2] => [ ..., val1 + val2]
	 */
	elm_add_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IList) arg_1).insert((IValue) arg_2);
		}
	},

	/**
	 * addition on list and list relation
	 * 
	 * [ ... IList val1, IListRelation val2] => [ ..., val1 + val2]
	 */
	list_add_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return list_add_list.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * addition on list relation and list relation
	 * 
	 * [ ... IListRelation val1, IListRelation val2] => [ ..., val1 + val2]
	 */
	lrel_add_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return list_add_list.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * addition on list relation and list
	 * 
	 * [ ... IListRelation val1, IList val2] => [ ..., val1 + val2]
	 */
	lrel_add_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return list_add_list.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * addition on list relation and element
	 * 
	 * [ ... IListRelation val1, IValue val2] => [ ..., val1 + val2]
	 */
	lrel_add_elm {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return list_add_elm.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * addition on element and list relation
	 * 
	 * [ ... IListRelation val1, IValue val2] => [ ..., val1 + val2]
	 */
	elm_add_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return elm_add_list.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * addition on loc and str
	 * 
	 * [ ... ISourceLocation val1, IString val2] => [ ..., val1 + val2]
	 */
	loc_add_str {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISourceLocation sloc = (ISourceLocation) arg_2;
			String s = ((IString) arg_1).getValue();

			String path = sloc.hasPath() ? sloc.getPath() : "";
			if(!path.endsWith("/")){
				path = path + "/";
			}
			path = path.concat(s);
			return $loc_field_update(sloc, "path", vf.string(path), currentFrame, rex);
		}
	},

	/**
	 * addition on map and map
	 * 
	 * [ ... IMap val1, IMap val2] => [ ..., val1 + val2]
	 */
	map_add_map {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IMap) arg_2).join((IMap) arg_1);
		}
	},

	/**
	 * addition on set and element
	 * 
	 * [ ... ISet val1, IValue val2] => [ ..., val1 + val2]
	 */
	set_add_elm {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((ISet) arg_2).insert((IValue) arg_1);
		}
	},

	/**
	 * addition on element and set
	 * 
	 * [ ... IValue val1, ISet val2] => [ ..., val1 + val2]
	 */
	elm_add_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((ISet) arg_1).insert((IValue) arg_2);
		}
	},

	/**
	 * addition on set and set
	 * 
	 * [ ... ISet val1, ISet val2] => [ ..., val1 + val2]
	 */
	set_add_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((ISet) arg_2).union((ISet) arg_1);
		}
	},

	/**
	 * addition on set and relation
	 * 
	 * [ ... ISet val1, IRelation val2] => [ ..., val1 + val2]
	 */
	set_add_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return set_add_set.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * addition on rel and rel
	 * 
	 * [ ... IRelation val1, IRelation val2] => [ ..., val1 + val2]
	 */
	rel_add_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((ISet) arg_2).union((ISet) arg_1);
		}
	},

	/**
	 * addition on rel and set
	 * 
	 * [ ... IRelation val1, ISet val2] => [ ..., val1 + val2]
	 */
	rel_add_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return set_add_set.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * addition on rel and element
	 * 
	 * [ ... IRelation val1, IValue val2] => [ ..., val1 + val2]
	 */
	rel_add_elm {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return set_add_elm.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * addition on element and rel
	 * 
	 * [ ... IValue val1, IRelation val2] => [ ..., val1 + val2]
	 */
	elm_add_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return elm_add_set.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * addition on str and str
	 * 
	 * [ ... IString val1, IString val2] => [ ..., val1 + val2]
	 */
	str_add_str {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity >= 2;
			if(arity == 2){
				stack[sp - 2] = ((IString) stack[sp - 2]).concat((IString) stack[sp - 1]);
				return sp - 1;
			} else {
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < arity; i++){
					sb.append(((IString)stack[sp - arity + i]).getValue());
				}
				stack[sp - arity] = vf.string(sb.toString());
				return sp - arity + 1;
			}
		}		
	},

	/**
	 * addition on tuple and tuple
	 * 
	 * [ ... ITuple val1, ITuple val2] => [ ..., val1 + val2]
	 */

	tuple_add_tuple {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ITuple t1 = (ITuple) arg_2;
			ITuple t2 = (ITuple) arg_1;
			int len1 = t1.arity();
			int len2 = t2.arity();
			IValue elems[] = new IValue[len1 + len2];
			for(int i = 0; i < len1; i++)
				elems[i] = t1.get(i);
			for(int i = 0; i < len2; i++)
				elems[len1 + i] = t2.get(i);
			return vf.tuple(elems);
		}
	},

	/**
	 * subtraction on arbitary values
	 * 
	 * [ ..., IValue val1, IValue val2 ] => [ ..., val1 - val2 ]
	 * 
	 * infix Difference "-" {
	 *		&L <: num x &R <: num                -> LUB(&L, &R),
	 * 		list[&L] x list[&R]                  -> list[LUB(&L,&R)],
	 *		set[&L] x set[&R]                    -> set[LUB(&L,&R)],
	 * 		map[&K1,&V1] x map[&K2,&V2]          -> map[LUB(&K1,&K2), LUB(&V1,&V2)]
	 * }
	 */
	
	/**
	 * See general remark about generic primitives
	 */
	subtract {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue lhs = ((IValue) arg_2);
			IValue rhs = ((IValue) arg_1);
			ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
			ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
			switch (lhsType) {
			case INT:
				switch (rhsType) {
				case INT:
					return int_subtract_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return int_subtract_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return int_subtract_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return int_subtract_rat.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType, currentFrame);
				}
			case NUM:
				switch (rhsType) {
				case INT:
					return num_subtract_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return num_subtract_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return num_subtract_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return num_subtract_rat.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType, currentFrame);
				}
			case REAL:
				switch (rhsType) {
				case INT:
					return real_subtract_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return real_subtract_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return real_subtract_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return real_subtract_rat.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType, currentFrame);
				}
			case RAT:
				switch (rhsType) {
				case INT:
					return rat_subtract_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return rat_subtract_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return rat_subtract_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return rat_subtract_rat.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType, currentFrame);
				}
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType, currentFrame);
			}
		}
	},

	/**
	 * subtraction on int and int
	 * 
	 * [ ..., IInteger val1, IInteger val2 ] => [ ..., val1 - val2 ]
	 */
	int_subtract_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).subtract((IInteger) arg_1);
		}
	},

	/**
	 * subtraction on int and num
	 * 
	 * [ ..., IInteger val1, INumber val2 ] => [ ..., val1 - val2 ]
	 */
	int_subtract_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).subtract((INumber) arg_1);
		}
	},

	/**
	 * subtraction on int and rat
	 * 
	 * [ ..., IInteger val1, IRational val2 ] => [ ..., val1 - val2 ]
	 */
	int_subtract_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).subtract((IRational) arg_1);
		}
	},

	/**
	 * subtraction on int and real
	 * 
	 * [ ..., IInteger val1, IReal val2 ] => [ ..., val1 - val2 ]
	 */
	int_subtract_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).subtract((IReal) arg_1);
		}	
	},

	/**
	 * subtraction on num and int
	 * 
	 * [ ..., INumber val1, IInteger val2 ] => [ ..., val1 - val2 ]
	 */
	num_subtract_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).subtract((IInteger) arg_1);
		}
	},

	/**
	 * subtraction on num and num
	 * 
	 * [ ..., INumber val1, INumber val2 ] => [ ..., val1 - val2 ]
	 */
	num_subtract_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).subtract((INumber) arg_1);
		}
	},

	/**
	 * subtraction on num and rat
	 * 
	 * [ ..., INumber val1, IRational val2 ] => [ ..., val1 - val2 ]
	 */
	num_subtract_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).subtract((IRational) arg_1);
		}
	},

	/**
	 * subtraction on num and inrealt
	 * 
	 * [ ..., INumber val1, IReal val2 ] => [ ..., val1 - val2 ]
	 */
	num_subtract_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).subtract((IReal) arg_1);
		}
	},

	/**
	 * subtraction on rat and int
	 * 
	 * [ ..., IRational val1, IInteger val2 ] => [ ..., val1 - val2 ]
	 */
	rat_subtract_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).subtract((IInteger) arg_1);
		}
	},

	/**
	 * subtraction on rat and inumnt
	 * 
	 * [ ..., IRational val1, INumber val2 ] => [ ..., val1 - val2 ]
	 */
	rat_subtract_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).subtract((INumber) arg_1);
		}
	},

	/**
	 * subtraction on rat and rat
	 * 
	 * [ ..., IRational val1, IRational val2 ] => [ ..., val1 - val2 ]
	 */
	rat_subtract_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).subtract((IRational) arg_1);
		}
	},

	/**
	 * subtraction on rat and real
	 * 
	 * [ ..., IRational val1, IReal val2 ] => [ ..., val1 - val2 ]
	 */
	rat_subtract_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).subtract((IReal) arg_1);
		}	
	},

	/**
	 * subtraction on real and number
	 * 
	 * [ ..., IReal val1, INumber val2 ] => [ ..., val1 - val2 ]
	 */
	real_subtract_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).subtract((INumber) arg_1);
		}
	},

	/**
	 * subtraction on real and int
	 * 
	 * [ ..., IReal val1, IInteger val2 ] => [ ..., val1 - val2 ]
	 */
	real_subtract_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).subtract((IInteger) arg_1);
		}
	},

	/**
	 * subtraction on real and real
	 * 
	 * [ ..., IReal val1, IReal val2 ] => [ ..., val1 - val2 ]
	 */
	real_subtract_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).subtract((IReal) arg_1);
		}
	},

	/**
	 * subtraction on real and rat
	 * 
	 * [ ..., IReal val1, IRational val2 ] => [ ..., val1 - val2 ]
	 */
	real_subtract_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).subtract((IRational) arg_1);
		}
	},

	/**
	 * subtraction on list and element
	 * 
	 * [ ..., IList val1, IValue val2 ] => [ ..., val1 - val2 ]
	 */
	list_subtract_elm {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IList) arg_2).delete((IValue) arg_1);
		}
	},

	/**
	 * subtraction on list and list
	 * 
	 * [ ..., IList val1, IList val2 ] => [ ..., val1 - val2 ]
	 */
	list_subtract_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IList) arg_2).subtract((IList) arg_1);
		}
	},

	/**
	 * subtraction on list and list relation
	 * 
	 * [ ..., IList val1, IListRelation val2 ] => [ ..., val1 - val2 ]
	 */
	list_subtract_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return list_subtract_list.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * subtraction on list relation and list relation
	 * 
	 * [ ..., IListRelation val1, IListRelation val2 ] => [ ..., val1 - val2 ]
	 */
	lrel_subtract_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return list_subtract_list.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * subtraction on list relation and list
	 * 
	 * [ ..., IList val1, IListRelation val2 ] => [ ..., val1 - val2 ]
	 */
	lrel_subtract_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return list_subtract_list.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * subtraction on list relation and element
	 * 
	 * [ ..., IListRelation val1, IValue val2 ] => [ ..., val1 - val2 ]
	 */
	lrel_subtract_elm {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return list_subtract_elm.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * subtraction on maps
	 * 
	 * [ ..., IMap val1, IMap val2 ] => [ ..., val1 - val2 ]
	 */
	map_subtract_map {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IMap) arg_2).remove((IMap) arg_1);
		}
	},

	/**
	 * subtraction on rel
	 * 
	 * [ ..., IRelation val1, IRelation val2 ] => [ ..., val1 - val2 ]
	 */
	rel_subtract_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return set_subtract_set.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * subtraction on rel and set
	 * 
	 * [ ..., IRelation val1, ISet val2 ] => [ ..., val1 - val2 ]
	 */
	rel_subtract_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return set_subtract_set.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * subtraction on rel and element
	 * 
	 * [ ..., IRelation val1, IValue val2 ] => [ ..., val1 - val2 ]
	 */
	rel_subtract_elm {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return set_subtract_elm.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * subtraction on set and element
	 * 
	 * [ ..., ISet val1, IValue val2 ] => [ ..., val1 - val2 ]
	 */
	set_subtract_elm {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((ISet) arg_2).delete((IValue) arg_1);
		}
	},

	/**
	 * subtraction on set
	 * 
	 * [ ..., ISet val1, ISet val2 ] => [ ..., val1 - val2 ]
	 */
	set_subtract_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((ISet) arg_2).subtract((ISet) arg_1);
		}
	},

	/**
	 * subtraction on set and rel
	 * 
	 * [ ..., ISet val1, IRelation val2 ] => [ ..., val1 - val2 ]
	 */
	set_subtract_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return set_subtract_set.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},	




	/**
	 * product of arbitrary values
	 * 
	 * [ ..., IValue val1, IValue val2 ] => [ ..., val1 * val2 ]
	 * 
	 * infix Product "*" {
	 *		&L <: num x &R <: num                -> LUB(&L, &R),
	 * 		list[&L] x list[&R]                  -> lrel[&L,&R],
	 *		set[&L] x set[&R]                    -> rel[&L,&R]
	 * }
	 */
	
	/**
	 * See general remark about generic primitives
	 */
	product {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue lhs = ((IValue) arg_2);
			IValue rhs = ((IValue) arg_1);
			ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
			ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
			switch (lhsType) {
			case INT:
				switch (rhsType) {
				case INT:
					return int_product_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return int_product_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return int_product_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return int_product_rat.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case NUM:
				switch (rhsType) {
				case INT:
					return num_product_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return num_product_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return num_product_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return num_product_rat.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case REAL:
				switch (rhsType) {
				case INT:
					return real_product_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return real_product_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return real_product_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return real_product_rat.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case RAT:
				switch (rhsType) {
				case INT:
					return rat_product_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return rat_product_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return rat_product_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return rat_product_rat.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			default:
				throw new InternalCompilerError("Illegal type combination: " + lhsType + " and " + rhsType, currentFrame);
			}
		}
	},

	/**
	 * product of int and int
	 * 
	 * [ ..., IInteger val1, IInteger val2 ] => [ ..., val1 * val2 ]
	 */
	int_product_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).multiply((IInteger) arg_1);
		}
	},

	/**
	 * product of int and num
	 * 
	 * [ ..., IInteger val1, INumber val2 ] => [ ..., val1 * val2 ]
	 */
	int_product_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).multiply((INumber) arg_1);
		}
	},

	/**
	 * product of int and rat
	 * 
	 * [ ..., IInteger val1, IRational val2 ] => [ ..., val1 * val2 ]
	 */
	int_product_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).multiply((IRational) arg_1);
		}
	},

	/**
	 * product of int and real
	 * 
	 * [ ..., IInteger val1, IReal val2 ] => [ ..., val1 * val2 ]
	 */
	int_product_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).multiply((IReal) arg_1);
		}
	},

	/**
	 * product of num and int
	 * 
	 * [ ..., INumber val1, IInteger val2 ] => [ ..., val1 * val2 ]
	 */
	num_product_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).multiply((IInteger) arg_1);
		}
	},

	/**
	 * product of num and num
	 * 
	 * [ ..., INumber val1, INumber val2 ] => [ ..., val1 * val2 ]
	 */
	num_product_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).multiply((INumber) arg_1);
		}
	},

	/**
	 * product of num and rat
	 * 
	 * [ ..., INumber val1, IRational val2 ] => [ ..., val1 * val2 ]
	 */
	num_product_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).multiply((IRational) arg_1);
		}
	},

	/**
	 * product of num and real
	 * 
	 * [ ..., INumber val1, IReal val2 ] => [ ..., val1 * val2 ]
	 */
	num_product_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).multiply((IReal) arg_1);
		}	
	},

	/**
	 * product of rat and int
	 * 
	 * [ ..., IRational val1, IInteger val2 ] => [ ..., val1 * val2 ]
	 */
	rat_product_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).multiply((IInteger) arg_1);
		}
	},

	/**
	 * product of rat and num
	 * 
	 * [ ..., IRational val1, INumber val2 ] => [ ..., val1 * val2 ]
	 */
	rat_product_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).multiply((INumber) arg_1);
		}
	},

	/**
	 * product of rat and rat
	 * 
	 * [ ..., IRational val1, IRational val2 ] => [ ..., val1 * val2 ]
	 */
	rat_product_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).multiply((IRational) arg_1);
		}
	},

	/**
	 * product of rat and real
	 * 
	 * [ ..., IRational val1, IReal val2 ] => [ ..., val1 * val2 ]
	 */
	rat_product_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).multiply((IReal) arg_1);
		}
	},

	/**
	 * product of real and num
	 * 
	 * [ ..., IReal val1, INumber val2 ] => [ ..., val1 * val2 ]
	 */
	real_product_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).multiply((INumber) arg_1);
		}
	},

	/**
	 * product of real and int
	 * 
	 * [ ..., IReal val1, IInteger val2 ] => [ ..., val1 * val2 ]
	 */
	real_product_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).multiply((IInteger) arg_1);
		}
	},

	/**
	 * product of real and real
	 * 
	 * [ ..., IReal val1, IReal val2 ] => [ ..., val1 * val2 ]
	 */
	real_product_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).multiply((IReal) arg_1);
		}
	},

	/**
	 * product of real and rat
	 * 
	 * [ ..., IReal val1, IRational val2 ] => [ ..., val1 * val2 ]
	 */
	real_product_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).multiply((IRational) arg_1);
		}
	},

	/**
	 * product of lists
	 * 
	 * [ ..., IList val1, IList val2 ] => [ ..., val1 * val2 ]
	 */
	list_product_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IList left = (IList) arg_2;
			IList right = (IList) arg_1;
			IListWriter w = vf.listWriter();
			for(IValue l : left){
				for(IValue r : right){
					w.append(vf.tuple(l,r));
				}
			}
			return w.done();
		}
	},

	/**
	 * product of list relations
	 * 
	 * [ ..., IListRelation val1, IListRelation val2 ] => [ ..., val1 * val2 ]
	 */
	lrel_product_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return list_product_list.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * product of sets
	 * 
	 * [ ..., ISet val1, ISet val2 ] => [ ..., val1 * val2 ]
	 */
	set_product_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet left = (ISet) arg_2;
			ISet right = (ISet) arg_1;
			ISetWriter w = vf.setWriter();
			for(IValue l : left){
				for(IValue r : right){
					w.insert(vf.tuple(l,r));
				}
			}
			return w.done();
		}
	},

	/**
	 * product of rel
	 * 
	 * [ ..., IRelation val1, IRelation val2 ] => [ ..., val1 * val2 ]
	 */
	rel_product_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return set_product_set.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * remainder on arbitrary values
	 * 
	 * [ ... IValue val1, IValue val2 ] => [ ..., val1 % val2 ]
	 */
	/**
	 * See general remark about generic primitives
	 */
	remainder {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue lhs = ((IValue) arg_2);
			IValue rhs = ((IValue) arg_1);
			if(lhs.getType().isInteger() && rhs.getType().isInteger()){
				return int_remainder_int.execute2(lhs, rhs, currentFrame, rex);
			}
			throw new InternalCompilerError("remainder: unexpected type combination" + lhs.getType() + " and " + rhs.getType(), currentFrame);
		}
	},

	/**
	 * remainder on int and int
	 * 
	 * [ ... IInteger val1, IINteger val2 ] => [ ..., val1 % val2 ]
	 */
	int_remainder_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).remainder((IInteger) arg_1);
		}
	},


	/**
	 *composition
	 * 
	 * [ ... IValue val1, IValue val2] => [ ..., val1 o val2]
	 *
	 * infix Composition "o" {
	 * 	lrel[&A,&B] x lrel[&B,&C] -> lrel[&A,&C],
	 * 	rel[&A,&B] x rel[&B,&C] -> rel[&A,&C],
	 * 	map[&A,&B] x map[&B,&C] -> map[&A,&C]
	 * }
	 */

	/**
	 * See general remark about generic primitives
	 */
	compose {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {

			IValue left = (IValue) arg_2;
			Type leftType = left.getType();
			IValue right = (IValue) arg_1;
			switch (ToplevelType.getToplevelType(leftType)) {
			case LREL:
				return lrel_compose_lrel.execute2(left, right, currentFrame, rex);
			case REL:
				return rel_compose_rel.execute2(left, right, currentFrame, rex);
			case MAP:
				return map_compose_map.execute2(left, right, currentFrame, rex);
			default:
				throw new InternalCompilerError("RascalPrimtive compose: unexpected type " + leftType, rex.getStdErr(), currentFrame);
			}
		}
	},

	/**
	 * compose lrel and lrel
	 * 
	 * [ ... IListRelation val1, IListRelation val2] => [ ..., val1 o val2]
	 */
	lrel_compose_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {

			IListRelation<IList> left = ((IList) arg_2).asRelation();
			IListRelation<IList> right = ((IList) arg_1).asRelation();
			return left.compose(right);
		}
	},


	/**
	 * compose rel and rel
	 * 
	 * [ ... IRelation val1, IRelation val2] => [ ..., val1 o val2]
	 */
	rel_compose_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISetRelation<ISet> left = ((ISet) arg_2).asRelation();
			ISetRelation<ISet> right = ((ISet) arg_1).asRelation();
			return left.compose(right);
		}
	},

	/**
	 * compose map and map
	 * 
	 * [ ... IMap val1, IMap val2] => [ ..., val1 o val2]
	 */
	map_compose_map {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IMap) arg_2).compose((IMap) arg_1);
		}
	},

	/**
	 * modulo
	 * 
	 * [ ... IValue val1, IValue val2] => [ ..., val1 mod val2]
	 */
	
	/**
	 * See general remark about generic primitives
	 */
	mod {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue lhs = ((IValue) arg_2);
			IValue rhs = ((IValue) arg_1);
			if(lhs.getType().isInteger() && rhs.getType().isInteger()){
				return int_mod_int.execute2(lhs, rhs, currentFrame, rex);
			}
			throw new InternalCompilerError("RascalPrimitive mod: unexpected type combination" + lhs.getType() + " and " + rhs.getType(), rex.getStdErr(), currentFrame);
		}
	},

	/**
	 * modulo on int and int
	 * 
	 * [ ... IValue val1, IValue val2] => [ ..., val1 mod val2]
	 */
	int_mod_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).mod((IInteger) arg_1);
		}
	},

	/**
	 * division
	 * 
	 *  * [ ... IValue val1, IValue val2] => [ ..., val1 / val2]
	 * 
	 * infix Division "/" { &L <: num x &R <: num        -> LUB(&L, &R) }
	 */
	
	/**
	 * See general remark about generic primitives
	 */

	divide {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue lhs = ((IValue) arg_2);
			IValue rhs = ((IValue) arg_1);
			ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
			ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
			switch (lhsType) {
			case INT:
				switch (rhsType) {
				case INT:
					return int_divide_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return int_divide_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return int_divide_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return int_divide_rat.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive divide: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case NUM:
				switch (rhsType) {
				case INT:
					return num_divide_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return num_divide_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return num_divide_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return num_divide_rat.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive divide: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case REAL:
				switch (rhsType) {
				case INT:
					return real_divide_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return real_divide_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return real_divide_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return real_divide_rat.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive divide: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			case RAT:
				switch (rhsType) {
				case INT:
					return rat_divide_int.execute2(lhs, rhs, currentFrame, rex);
				case NUM:
					return rat_divide_num.execute2(lhs, rhs, currentFrame, rex);
				case REAL:
					return rat_divide_real.execute2(lhs, rhs, currentFrame, rex);
				case RAT:
					return rat_divide_rat.execute2(lhs, rhs, currentFrame, rex);
				default:
					throw new InternalCompilerError("RascalPrimitive divide: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
				}
			default:
				throw new InternalCompilerError("RascalPrimitive divide: Illegal type combination: " + lhsType + " and " + rhsType, rex.getStdErr(), currentFrame);
			}
		}
	},

	/**
	 * divide on int and int
	 * 
	 * [ ... IInteger val1, IInteger val2] => [ ..., val1 / val2]
	 */
	int_divide_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((IInteger) arg_2).divide((IInteger) arg_1);
			} catch(ArithmeticException e) {
			  return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on int and num
	 * 
	 * [ ... IInteger val1, INumber val2] => [ ..., val1 / val2]
	 */
	int_divide_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((IInteger) arg_2).divide((INumber) arg_1, vf.getPrecision());
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on int and rat
	 * 
	 * [ ... IInteger val1, IRational val2] => [ ..., val1 / val2]
	 */
	int_divide_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((IInteger) arg_2).divide((IRational) arg_1);
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on int and real
	 * 
	 * [ ... IInteger val1, IReal val2] => [ ..., val1 / val2]
	 */
	int_divide_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((IInteger) arg_2).divide((IReal) arg_1, vf.getPrecision());
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on num and int
	 * 
	 * [ ... INumber val1, IInteger val2] => [ ..., val1 / val2]
	 */
	num_divide_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((INumber) arg_2).divide((IInteger) arg_1, vf.getPrecision());
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on num and num
	 * 
	 * [ ... INumber val1, INumber val2] => [ ..., val1 / val2]
	 */
	num_divide_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((INumber) arg_2).divide((INumber) arg_1, vf.getPrecision());
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on num and rat
	 * 
	 * [ ... INumber val1, IRational val2] => [ ..., val1 / val2]
	 */
	num_divide_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((INumber) arg_2).divide((IRational) arg_1, vf.getPrecision());
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on num and real
	 * 
	 * [ ... INumber val1, IReal val2] => [ ..., val1 / val2]
	 */
	num_divide_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((INumber) arg_2).divide((IReal) arg_1, vf.getPrecision());
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on rat and int
	 * 
	 * [ ... IRational val1, IInteger val2] => [ ..., val1 / val2]
	 */
	rat_divide_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((IRational) arg_2).divide((IInteger) arg_1);
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on rat and num
	 * 
	 * [ ... IRational val1, INumber val2] => [ ..., val1 / val2]
	 */
	rat_divide_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((IRational) arg_2).divide((INumber) arg_1, vf.getPrecision());
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on rat and rat
	 * 
	 * [ ... IRational val1, IRational val2] => [ ..., val1 / val2]
	 */
	rat_divide_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((IRational) arg_2).divide((IRational) arg_1);
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on rat and real
	 * 
	 * [ ... IRational val1, IReal val2] => [ ..., val1 / val2]
	 */
	rat_divide_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((IRational) arg_2).divide((IReal) arg_1, vf.getPrecision());
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on real and num
	 * 
	 * [ ... IReal val1, INumber val2] => [ ..., val1 / val2]
	 */
	real_divide_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((IReal) arg_2).divide((INumber) arg_1, vf.getPrecision());
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on real and int
	 * 
	 * [ ... IReal val1, IInteger val2] => [ ..., val1 / val2]
	 */
	real_divide_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((IReal) arg_2).divide((IInteger) arg_1, vf.getPrecision());
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on real and real
	 * 
	 * [ ... IReal val1, IReal val2] => [ ..., val1 / val2]
	 */
	real_divide_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((IReal) arg_2).divide((IReal) arg_1, vf.getPrecision());
			} catch(ArithmeticException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/**
	 * divide on real and rat
	 * 
	 * [ ... IReal val1, IRational val2] => [ ..., val1 / val2]
	 */
	real_divide_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			try {
				return ((IReal) arg_2).divide((IRational) arg_1, vf.getPrecision());
			} catch(ArithmeticException e) {
			  return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.arithmeticException("divide by zero", currentFrame));
			}
		}
	},

	/*
	 * equal
	 */

	/**
	 * equal on int and int
	 * 
	 * [ ... IInteger val1, IInteger val2] => [ ..., val1 == val2]
	 */

	int_equal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).equal((IInteger) arg_1);
		}
	},

	/**
	 * equal on int and num
	 * 
	 * [ ... IInteger val1, INumber val2] => [ ..., val1 == val2]
	 */
	int_equal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).equal((INumber) arg_1);
		}
	},

	/**
	 * equal on int and rat
	 * 
	 * [ ... IInteger val1, IRational val2] => [ ..., val1 == val2]
	 */
	int_equal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).equal((IRational) arg_1);
		}
	},

	/**
	 * equal on int and real
	 * 
	 * [ ... IInteger val1, IReal val2] => [ ..., val1 == val2]
	 */
	int_equal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).equal((IReal) arg_1);
		}
	},

	/**
	 * equal on num and int
	 * 
	 * [ ... INumber val1, IInteger val2] => [ ..., val1 == val2]
	 */
	num_equal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).equal((IInteger) arg_1);
		}
	},

	/**
	 * equal on num and num
	 * 
	 * [ ... INumber val1, INumber val2] => [ ..., val1 == val2]
	 */
	num_equal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).equal((INumber) arg_1);
		}
	},

	/**
	 * equal on num and rat
	 * 
	 * [ ... INumber val1, IRational val2] => [ ..., val1 == val2]
	 */
	num_equal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).equal((IRational) arg_1);
		}
	},

	/**
	 * equal on num and real
	 * 
	 * [ ... INumber val1, IReal val2] => [ ..., val1 == val2]
	 */
	num_equal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).equal((IReal) arg_1);
		}
	},

	/**
	 * equal on real and int
	 * 
	 * [ ... IReal val1, IInteger val2] => [ ..., val1 == val2]
	 */
	real_equal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).equal((IInteger) arg_1);
		}
	},


	/**
	 * equal on real and num
	 * 
	 * [ ... IReal val1, INumber val2] => [ ..., val1 == val2]
	 */
	real_equal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).equal((INumber) arg_1);
		}
	},


	/**
	 * equal on real and rat
	 * 
	 * [ ... IReal val1, IRational val2] => [ ..., val1 == val2]
	 */
	real_equal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).equal((IRational) arg_1);
		}
	},

	/**
	 * equal on real and real
	 * 
	 * [ ... IReal val1, IReal val2] => [ ..., val1 == val2]
	 */
	real_equal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).equal((IReal) arg_1);
		}
	},

	/**
	 * equal on rat and int
	 * 
	 * [ ... IRational val1, IInteger val2] => [ ..., val1 == val2]
	 */
	rat_equal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).equal((IInteger) arg_1);
		}
	},

	/**
	 * equal on rat and num
	 * 
	 * [ ... IRational val1, INumber val2] => [ ..., val1 == val2]
	 */
	rat_equal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).equal((INumber) arg_1);
		}
	},

	/**
	 * equal on rat and rat
	 * 
	 * [ ... IRational val1, IRational val2] => [ ..., val1 == val2]
	 */
	rat_equal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).equal((IRational) arg_1);
		}
	},

	/**
	 * equal on rat and real
	 * 
	 * [ ... IRational val1, IReal val2] => [ ..., val1 == val2]
	 */
	rat_equal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).equal((IReal) arg_1);
		}
	},

	/**
	 * equal on node and node
	 * 
	 * [ ... INode val1, INode val2] => [ ..., val1 == val2]
	 */

	node_equal_node {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			INode leftNode = (INode) arg_2;
			INode rightNode = (INode) arg_1;

			return leftNode.isEqual(rightNode) ? Rascal_TRUE : Rascal_FALSE;
		}
	},

	// equal on other types


	/**
	 * equal on arbitrary types
	 * 
	 * [ ... IValue val1, IValue val2] => [ ..., val1 == val2]
	 */
	
	/**
	 * See general remark about generic primitives
	 */
	equal {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue left = (IValue)arg_2;
			IValue right = (IValue)arg_1;
			if(left.getType().isNumber() && right.getType().isNumber()){
				return num_equal_num.execute2(left,right, currentFrame, rex);
			} else if(left.getType().isNode() && right.getType().isNode()){
				return node_equal_node.execute2(left, right, currentFrame, rex);
			} else {
				return vf.bool(left.isEqual(right));
			}
		}
	},

	/**
	 * equal on types
	 * 
	 * [ ... Type type1, Type type2] => [ ..., type1 == type2]
	 */
	type_equal_type {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((Type) arg_2) == ((Type) arg_1));
		}		
	},


	// ==== greater

	/**
	 * greater-than on int and int
	 * 
	 * [ ... IInteger val1, IInteger val2] => [ ..., val1 > val2]
	 */
	int_greater_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).greater((IInteger) arg_1);
		}
	},

	/**
	 * greater-than on int and num
	 * 
	 * [ ... IInteger val1, INumber val2] => [ ..., val1 > val2]
	 */
	int_greater_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).greater((INumber) arg_1);
		}
	},

	/**
	 * greater-than on int and rat
	 * 
	 * [ ... IInteger val1, IRational val2] => [ ..., val1 > val2]
	 */
	int_greater_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).greater((IRational) arg_1);
		}
	},

	/**
	 * greater-than on int and real
	 * 
	 * [ ... IInteger val1, IReal val2] => [ ..., val1 > val2]
	 */
	int_greater_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).greater((IReal) arg_1);
		}
	},

	/**
	 * greater-than on num and int
	 * 
	 * [ ... INumber val1, IInteger val2] => [ ..., val1 > val2]
	 */
	num_greater_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).greater((IInteger) arg_1);
		}
	},


	/**
	 * greater-than on num and num
	 * 
	 * [ ... INumber val1, INumber val2] => [ ..., val1 > val2]
	 */
	num_greater_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).greater((INumber) arg_1);
		}
	},

	/**
	 * greater-than on num and rat
	 * 
	 * [ ... INumber val1, IRational val2] => [ ..., val1 > val2]
	 */
	num_greater_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).greater((IRational) arg_1);
		}
	},


	/**
	 * greater-than on num and real
	 * 
	 * [ ... INumber val1, IReal val2] => [ ..., val1 > val2]
	 */
	num_greater_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).greater((IReal) arg_1);
		}
	},	

	/**
	 * greater-than on rat and int
	 * 
	 * [ ... IRational val1, IInteger val2] => [ ..., val1 > val2]
	 */
	rat_greater_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).greater((IInteger) arg_1);
		}
	},

	/**
	 * greater-than on rat and num
	 * 
	 * [ ... IRational val1, INumber val2] => [ ..., val1 > val2]
	 */
	rat_greater_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).greater((INumber) arg_1);
		}
	},

	/**
	 * greater-than on rat and rat
	 * 
	 * [ ... IRational val1, IRational val2] => [ ..., val1 > val2]
	 */
	rat_greater_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).greater((IRational) arg_1);
		}
	},

	/**
	 * greater-than on rat and real
	 * 
	 * [ ... IRational val1, IReal val2] => [ ..., val1 > val2]
	 */
	rat_greater_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).greater((IReal) arg_1);
		}
	},

	/**
	 * greater-than on real and num
	 * 
	 * [ ... IReal val1, INumber val2] => [ ..., val1 > val2]
	 */
	real_greater_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).greater((INumber) arg_1);
		}
	},

	/**
	 * greater-than on real and int
	 * 
	 * [ ... IReal val1, IInteger val2] => [ ..., val1 > val2]
	 */
	real_greater_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).greater((IInteger) arg_1);
		}
	},

	/**
	 * greater-than on real and real
	 * 
	 * [ ... IReal val1, IReal val2] => [ ..., val1 > val2]
	 */
	real_greater_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).greater((IReal) arg_1);
		}
	},

	/**
	 * greater-than on real and rat
	 * 
	 * [ ... IReal val1, IRational val2] => [ ..., val1 > val2]
	 */
	real_greater_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).greater((IRational) arg_1);
		}
	},

	/**
	 * greater-than on arbitrary values
	 * 
	 * [ ... IValue val1, IValue val2] => [ ..., val1 > val2]
	 */

	greater {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IBool) lessequal.execute2(arg_2, arg_1, currentFrame, rex)).not();
		}
	},

	/**
	 * greater-than on adts
	 * 
	 * [ ... IConstructor val1, IConstructor val2] => [ ..., val1 > val2]
	 */
	adt_greater_adt {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return node_greater_node.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * greater-than on bool
	 * 
	 * [ ... IBool val1, IBool val2] => [ ..., val1 > val2]
	 */

	bool_greater_bool {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IBool) arg_2).and(((IBool) arg_1).not());
		}
	},

	/**
	 * greater-than on datetime
	 * 
	 * [ ... IDateTime val1, IDateTime val2] => [ ..., val1 > val2]
	 */
	datetime_greater_datetime {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IDateTime) arg_2).compareTo((IDateTime) arg_1) == 1);
		}
	},

	/**
	 * greater-than on list
	 * 
	 * [ ... IList val1, IList val2] => [ ..., val1 > val2]
	 */
	list_greater_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IBool) list_lessequal_list.execute2(arg_2, arg_1, currentFrame, rex)).not();
		}
	},

	/**
	 * greater-than on list relation
	 * 
	 * [ ... IListRelation val1, IListRelation val2] => [ ..., val1 > val2]
	 */
	lrel_greater_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IBool) list_lessequal_list.execute2(arg_2, arg_1, currentFrame, rex)).not();
		}
	},

	/**
	 * greater-than on loc
	 * 
	 * [ ... ISourceLocation val1, ISourceLocation val2] => [ ..., val1 > val2]
	 */
	loc_greater_loc {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IBool) loc_lessequal_loc.execute2(arg_2, arg_1, currentFrame, rex)).not();
		}
	},

	/**
	 * greater-than on map
	 * 
	 * [ ... IMap val1, IMap val2] => [ ..., val1 > val2]
	 */
	map_greater_map {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IMap left = (IMap) arg_2;
			IMap right = (IMap) arg_1;

			return vf.bool(right.isSubMap(left) && !left.isSubMap(right));
		}
	},

	/**
	 * greater-than on node
	 * 
	 * [ ... INode val1, INode val2] => [ ..., val1 > val2]
	 */
	node_greater_node {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IBool) node_lessequal_node.execute2(arg_2, arg_1, currentFrame, rex)).not();
		}
	},

	/**
	 * greater-than on set
	 * 
	 * [ ... ISet val1, ISet val2] => [ ..., val1 > val2]
	 */
	set_greater_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((ISet) arg_1).isSubsetOf((ISet) arg_2));
		}
	},

	/**
	 * greater-than on rel
	 * 
	 * [ ... IRelation val1, IRelation val2] => [ ..., val1 > val2]
	 */
	rel_greater_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((ISet) arg_1).isSubsetOf((ISet) arg_2));
		}
	},

	/**
	 * greater-than on str
	 * 
	 * [ ... IString val1, IString val2] => [ ..., val1 > val2]
	 */
	str_greater_str {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IString) arg_2).compare((IString) arg_1) == 1);
		}
	},

	/**
	 * greater-than on tuple
	 * 
	 * [ ... ITuple val1, ITuple val2] => [ ..., val1 > val2]
	 */
	tuple_greater_tuple {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IBool) tuple_lessequal_tuple.execute2(arg_2, arg_1, currentFrame, rex)).not();
		}
	},
	
	// ==== greaterequal

	/**
	 * greater-than-or-equal on int and int
	 * 
	 * [ ... IInteger val1, IInteger val2] => [ ..., val1 >= val2]
	 */
	int_greaterequal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).greaterEqual((IInteger) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on int and num
	 * 
	 * [ ... IInteger val1, INumber val2] => [ ..., val1 >= val2]
	 */
	int_greaterequal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).greaterEqual((INumber) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on int and rat
	 * 
	 * [ ... IInteger val1, IRational val2] => [ ..., val1 >= val2]
	 */
	int_greaterequal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).greaterEqual((IRational) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on int and real
	 * 
	 * [ ... IInteger val1, IReal val2] => [ ..., val1 >= val2]
	 */
	int_greaterequal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).greaterEqual((IReal) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on num and int
	 * 
	 * [ ... INumber val1, IInteger val2] => [ ..., val1 >= val2]
	 */
	num_greaterequal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).greaterEqual((IInteger) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on num and num
	 * 
	 * [ ... INumber val1, INumber val2] => [ ..., val1 >= val2]
	 */
	num_greaterequal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).greaterEqual((INumber) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on num and rat
	 * 
	 * [ ... INumber val1, IRational val2] => [ ..., val1 >= val2]
	 */
	num_greaterequal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).greaterEqual((IRational) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on num and real
	 * 
	 * [ ... INumber val1, IReal val2] => [ ..., val1 >= val2]
	 */
	num_greaterequal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).greaterEqual((IReal) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on rat and int
	 * 
	 * [ ... IRational val1, IInteger val2] => [ ..., val1 >= val2]
	 */
	rat_greaterequal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).greaterEqual((IInteger) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on rat and num
	 * 
	 * [ ... IRational val1, INumber val2] => [ ..., val1 >= val2]
	 */
	rat_greaterequal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).greaterEqual((INumber) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on rat and rat
	 * 
	 * [ ... IRational val1, IRational val2] => [ ..., val1 >= val2]
	 */
	rat_greaterequal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).greaterEqual((IRational) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on rat and real
	 * 
	 * [ ... IRational val1, IReal val2] => [ ..., val1 >= val2]
	 */
	rat_greaterequal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).greaterEqual((IReal) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on real and num
	 * 
	 * [ ... IReal val1, INumber val2] => [ ..., val1 >= val2]
	 */
	real_greaterequal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).greaterEqual((INumber) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on real and int
	 * 
	 * [ ... IReal val1, IInteger val2] => [ ..., val1 >= val2]
	 */
	real_greaterequal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).greaterEqual((IInteger) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on real and real
	 * 
	 * [ ... IReal val1, IReal val2] => [ ..., val1 >= val2]
	 */
	real_greaterequal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).greaterEqual((IReal) arg_1);
		}
	},
	/**
	 * greater-than-or-equal on real and rat
	 * 
	 * [ ... IReal val1, IRational val2] => [ ..., val1 >= val2]
	 */
	real_greaterequal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).greaterEqual((IRational) arg_1);
		}
	},

	/**
	 * greater-than-or-equal on arbitrary values
	 * 
	 * [ ... IValue val1, IValue val2] => [ ..., val1 >= val2]
	 */
	greaterequal {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IBool) less.execute2(arg_2, arg_1, currentFrame, rex)).not();
		}
	},

	/**
	 * greater-than-or-equal on adts
	 * 
	 * [ ... IConstructor val1, IConstructor val2] => [ ..., val1 >= val2]
	 */
	adt_greaterequal_adt {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return node_greaterequal_node.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * greater-than-or-equal on bool
	 * 
	 * [ ... IBool val1, IBool val2] => [ ..., val1 >= val2]
	 */
	bool_greaterequal_bool {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			boolean left = ((IBool) arg_2).getValue();
			boolean right = ((IBool) arg_1).getValue();
			return vf.bool((left && !right) || (left == right));
		}
	},

	/**
	 * greater-than-or-equal on datetime
	 * 
	 * [ ... IDateTime val1, IDateTime val2] => [ ..., val1 >= val2]
	 */
	datetime_greaterequal_datetime {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IDateTime) arg_2).compareTo((IDateTime) arg_1) == 1);
		}
	},

	/**
	 * greater-than-or-equal on lists
	 * 
	 * [ ... IList val1, IList val2] => [ ..., val1 >= val2]
	 */
	list_greaterequal_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IBool) list_less_list.execute2(arg_2, arg_1, currentFrame, rex)).not();
		}
	},

	/**
	 * greater-than-or-equal on list relations
	 * 
	 * [ ... IListRelation val1, IListRelation val2] => [ ..., val1 >= val2]
	 */
	lrel_greaterequal_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IBool) list_less_list.execute2(arg_2, arg_1, currentFrame, rex)).not();
		}
	},

	/**
	 * greater-than-or-equal on locs
	 * 
	 * [ ... ISourceLocation val1, ISourceLocation val2] => [ ..., val1 >= val2]
	 */
	loc_greaterequal_loc {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IBool) loc_less_loc.execute2(arg_2, arg_1, currentFrame, rex)).not();
		}
	},

	/**
	 * greater-than-or-equal on nodes
	 * 
	 * [ ... INode val1, INode val2] => [ ..., val1 >= val2]
	 */
	node_greaterequal_node {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IBool) node_less_node.execute2(arg_2, arg_1, currentFrame, rex)).not();
		}
	},

	/**
	 * greater-than-or-equal on maps
	 * 
	 * [ ... IMap val1, IMap val2] => [ ..., val1 >= val2]
	 */
	map_greaterequal_map {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IMap left = (IMap) arg_2;
			IMap right = (IMap) arg_1;
			return vf.bool(right.isSubMap(left));
		}
	},

	/**
	 * greater-than-or-equal on sets
	 * 
	 * [ ... ISet val1, ISet val2] => [ ..., val1 >= val2]
	 */
	set_greaterequal_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet left = (ISet) arg_2;
			ISet right = (ISet) arg_1;
			return vf.bool(left.isEqual(right) || right.isSubsetOf(left));
		}
	},

	/**
	 * greater-than-or-equal on rels
	 * 
	 * [ ... IRelation val1, IRelation val2] => [ ..., val1 >= val2]
	 */
	rel_greaterequal_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet left = (ISet) arg_2;
			ISet right = (ISet) arg_1;
			return vf.bool(left.isEqual(right) || right.isSubsetOf(left));
		}
	},

	/**
	 * greater-than-or-equal on str
	 * 
	 * [ ... IString val1, IString val2] => [ ..., val1 >= val2]
	 */
	str_greaterequal_str {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			int c = ((IString) arg_2).compare((IString) arg_1);
			return vf.bool(c == 0 || c == 1);
		}
	},

	/**
	 * greater-than-or-equal on tuples
	 * 
	 * [ ... ITuple val1, ITuple val2] => [ ..., val1 >= val2]
	 */
	tuple_greaterequal_tuple {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IBool) tuple_less_tuple.execute2(arg_2, arg_1, currentFrame, rex)).not();
		}
	},

	// ==== intersect
	
	/**
	 * intersect of two values
	 * 
	 * [ ... IValue val1, IValue val2 ] => [ ..., val1 & val2 ]
	 * 
	 * infix intersect "&" {
	 *		list[&L] x list[&R]                  -> list[LUB(&L,&R)],
	 *		set[&L] x set[&R]                    -> set[LUB(&L,&R)],
	 * 		map[&K1,&V1] x map[&K2,&V2]          -> map[LUB(&K1,&K2), LUB(&V1,&V2)]
	 * } 
	 */
	
	/**
	 * See general remark about generic primitives
	 */

	intersect {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {

			IValue left = (IValue) arg_2;
			Type leftType = left.getType();
			IValue right = (IValue) arg_1;
			Type rightType = right.getType();

			switch (ToplevelType.getToplevelType(leftType)) {
			case LIST:
				switch (ToplevelType.getToplevelType(rightType)) {
				case LIST:
					return list_intersect_list.execute2(left, right, currentFrame, rex);
				case LREL:
					return list_intersect_lrel.execute2(left, right, currentFrame, rex);
				default:
					throw new InternalCompilerError("intersect: illegal combination " + leftType + " and " + rightType, rex.getStdErr(), currentFrame);
				}
			case SET:
				switch (ToplevelType.getToplevelType(rightType)) {
				case SET:
					return set_intersect_set.execute2(left, right, currentFrame, rex);
				case REL:
					return set_intersect_rel.execute2(left, right, currentFrame, rex);
				default:
					throw new InternalCompilerError("intersect: illegal combination " + leftType + " and " + rightType, rex.getStdErr(), currentFrame);
				}
			case MAP:
				return map_intersect_map.execute2(left, right, currentFrame, rex);

			default:
				throw new InternalCompilerError("intersect: illegal combination " + leftType + " and " + rightType, rex.getStdErr(), currentFrame);
			}
		}
	},

	/**
	 * intersect on lists
	 * 
	 * [ ... IList val1, IList val2 ] => [ ..., val1 & val2 ]
	 */
	list_intersect_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IList) arg_2).intersect((IList) arg_1);
		}
	},

	/**
	 * intersect on list and list relation
	 * 
	 * [ ... IList val1, IListRelation val2 ] => [ ..., val1 & val2 ]
	 */
	list_intersect_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return list_intersect_list.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * intersect on list relations
	 * 
	 * [ ... IListRelation val1, IListRelation val2 ] => [ ..., val1 & val2 ]
	 */
	lrel_intersect_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return list_intersect_list.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * intersect on list relation and list
	 * 
	 * [ ... IListRelation val1, IList val2 ] => [ ..., val1 & val2 ]
	 */
	lrel_intersect_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return list_intersect_list.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * intersect on maps
	 * 
	 * [ ... IMap val1, IMap val2 ] => [ ..., val1 & val2 ]
	 */
	map_intersect_map {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IMap) arg_2).common((IMap) arg_1);
		}
	},

	/**
	 * intersect on rels
	 * 
	 * [ ... IRelation val1, IRelation val2 ] => [ ..., val1 & val2 ]
	 */
	rel_intersect_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return set_intersect_set.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * intersect on rel and set
	 * 
	 * [ ... IRelation val1, ISet val2 ] => [ ..., val1 & val2 ]
	 */
	rel_intersect_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return set_intersect_set.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * intersect on sets
	 * 
	 * [ ... ISet val1, ISet val2 ] => [ ..., val1 & val2 ]
	 */
	set_intersect_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((ISet) arg_2).intersect((ISet) arg_1);
		}
	},

	/**
	 * intersect on set and rel
	 * 
	 * [ ... ISet val1, IRelation val2 ] => [ ..., val1 & val2 ]
	 */
	set_intersect_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return set_intersect_set.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	// ==== in
	
	/**
	 * in (is-element-of) on arbitrary values
	 * 
	 * [ ... IValue val1, IValue val2 ] => [ ..., val1 in val2 ]
	 */
	
	/**
	 * See general remark about generic primitives
	 */
	in {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {

			IValue left = (IValue) arg_2;
			Type leftType = left.getType();
			IValue right = (IValue) arg_1;
			Type rightType = right.getType();

			switch (ToplevelType.getToplevelType(leftType)) {
			case LIST:
				return elm_in_list.execute2(left, right, currentFrame, rex);
			case LREL:
				return elm_in_lrel.execute2(left, right, currentFrame, rex);
			case SET:
				return elm_in_set.execute2(left, right, currentFrame, rex);
			case REL:
				return elm_in_rel.execute2(left, right, currentFrame, rex);
			case MAP:
				return elm_in_map.execute2(left, right, currentFrame, rex);
			default:
				throw new InternalCompilerError("in: illegal combination " + leftType + " and " + rightType, rex.getStdErr(), currentFrame);
			}
		}
	},

	/**
	 * in (is-element-of) on element and list
	 * 
	 * [ ... IValue val1, IList val2 ] => [ ..., val1 in val2 ]
	 */
	elm_in_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IList) arg_1).contains((IValue) arg_2));
		}
	},

	/**
	 * in (is-element-of) on element and list relation
	 * 
	 * [ ... IValue val1, IListRelation val2 ] => [ ..., val1 in val2 ]
	 */
	elm_in_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return elm_in_list.execute2(arg_2, arg_1, currentFrame, rex);
		}

	},

	/**
	 * in (is-element-of) on element and set
	 * 
	 * [ ... IValue val1, ISet val2 ] => [ ..., val1 in val2 ]
	 */
	elm_in_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((ISet) arg_1).contains((IValue) arg_2));
		}

	},

	/**
	 * in (is-element-of) on element and relation
	 * 
	 * [ ... IValue val1, IRelation val2 ] => [ ..., val1 in val2 ]
	 */
	elm_in_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return elm_in_set.execute2(arg_2, arg_1, currentFrame, rex);
		}

	},

	/**
	 * in (is-element-of) on element and map
	 * 
	 * [ ... IValue val1, IMap val2 ] => [ ..., val1 in val2 ]
	 */
	elm_in_map {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IMap) arg_1).containsKey((IValue) arg_2));
		}
	},

	// ==== notin

	/**
	 * notin (not-element-of) on arbitrary values
	 * 
	 * [ ... IValue val1, IValue val2 ] => [ ..., val1 notin val2 ]
	 */	
	/**
	 * See general remark about generic primitives
	 */
	notin {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {

			IValue left = (IValue) arg_2;
			Type leftType = left.getType();
			IValue right = (IValue) arg_1;
			Type rightType = right.getType();

			switch (ToplevelType.getToplevelType(leftType)) {
			case LIST:
				return elm_notin_list.execute2(left, right, currentFrame, rex);
			case LREL:
				return elm_notin_lrel.execute2(left, right, currentFrame, rex);
			case SET:
				return elm_notin_set.execute2(left, right, currentFrame, rex);
			case REL:
				return elm_notin_rel.execute2(left, right, currentFrame, rex);
			case MAP:
				return elm_notin_map.execute2(left, right, currentFrame, rex);
			default:
				throw new InternalCompilerError("notin: illegal combination " + leftType + " and " + rightType, rex.getStdErr(), currentFrame);
			}
		}
	},

	/**
	 * notin (not-element-of) on element and list
	 * 
	 * [ ... IValue val1, IList val2 ] => [ ..., val1 notin val2 ]
	 */	
	elm_notin_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(!((IList) arg_1).contains((IValue) arg_2));
		}
	},

	/**
	 * notin (not-element-of) on element and list relation
	 * 
	 * [ ... IValue val1, IListRelation val2 ] => [ ..., val1 notin val2 ]
	 */	
	elm_notin_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return elm_notin_list.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * notin (not-element-of) on element and set
	 * 
	 * [ ... IValue val1, ISet val2 ] => [ ..., val1 notin val2 ]
	 */	
	elm_notin_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(!((ISet) arg_1).contains((IValue) arg_2));
		}
	},

	/**
	 * notin (not-element-of) on element and relation
	 * 
	 * [ ... IValue val1, IRelation val2 ] => [ ..., val1 notin val2 ]
	 */	
	elm_notin_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return elm_notin_set.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * notin (not-element-of) on element and map
	 * 
	 * [ ... IValue val1, IMap val2 ] => [ ..., val1 notin val2 ]
	 */	
	elm_notin_map {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(!((IMap) arg_1).containsKey((IValue) arg_2));
		}
	},
	
	// ==== non_negative

	/**
	 * Non_negative
	 * 
	 * [ ..., IInteger val ] => [ ..., val >= 0 ]
	 */
	non_negative {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			if(((IInteger)arg_1).intValue() < 0){
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.indexOutOfBounds(((IInteger)arg_1), currentFrame));
			}
			return Rascal_TRUE;
		}
	},

	// ==== join
	
	/**
	 * See general remark about generic primitives
	 */
	
	join {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
	
			IValue left = (IValue) arg_2;
			Type leftType = left.getType();
			IValue right = (IValue) arg_1;
			Type rightType = right.getType();

			switch (ToplevelType.getToplevelType(leftType)) {
			case LIST:
				switch (ToplevelType.getToplevelType(rightType)) {
				case LIST:
					return list_join_list.execute2(left, right, currentFrame, rex);
				case LREL:
					return list_join_lrel.execute2(left, right, currentFrame, rex);
				default:
					throw new InternalCompilerError("join: illegal combination " + leftType + " and " + rightType, rex.getStdErr(), currentFrame);
				}
			case LREL:
				switch (ToplevelType.getToplevelType(rightType)) {
				case LIST:
					return lrel_join_list.execute2(left, right, currentFrame, rex);
				case LREL:
					return lrel_join_lrel.execute2(left, right, currentFrame, rex);
				default:
					throw new InternalCompilerError("join: illegal combination " + leftType + " and " + rightType, rex.getStdErr(), currentFrame);
				}
			case SET:
				switch (ToplevelType.getToplevelType(rightType)) {
				case SET:
					return set_join_set.execute2(left, right, currentFrame, rex);
				case REL:
					return set_join_rel.execute2(left, right, currentFrame, rex);
				default:
					throw new InternalCompilerError("join: illegal combination " + leftType + " and " + rightType, rex.getStdErr(), currentFrame);
				}

			case REL:
				switch (ToplevelType.getToplevelType(rightType)) {
				case SET:
					return rel_join_set.execute2(left, right, currentFrame, rex);
				case REL:
					return rel_join_rel.execute2(left, right, currentFrame, rex);
				default:
					throw new InternalCompilerError("join: illegal combination " + leftType + " and " + rightType, rex.getStdErr(), currentFrame);
				}

			default:
				throw new InternalCompilerError("join: illegal combination " + leftType + " and " + rightType, rex.getStdErr(), currentFrame);
			}
		}
	},

	/** join on lists
	 * 
	 * [ ..., IList val1, IList val2 ] => [ ..., val1 join val2 ]
	 */
	list_join_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return list_product_list.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/** join on list and list relation
	 * 
	 * [ ..., IList val1, IListRelation val2 ] => [ ..., val1 join val2 ]
	 */
	list_join_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IList left = (IList) arg_2;
			IList right = (IList) arg_1;
			if(left.length() == 0){
				return left;
			}
			if(right.length() == 0){
				return right;
			}
			Type rightType = right.get(0).getType();
			assert rightType.isTuple();

			int rarity = rightType.getArity();
			IValue fieldValues[] = new IValue[1 + rarity];
			IListWriter w =vf.listWriter();

			for (IValue lval : left){
				fieldValues[0] = lval;
				for (IValue rtuple: right) {
					for (int i = 0; i < rarity; i++) {
						fieldValues[i + 1] = ((ITuple)rtuple).get(i);
					}
					w.append(vf.tuple(fieldValues));
				}
			}
			return w.done();
		}
	},

	/** join on list relations
	 * 
	 * [ ..., IListRelation val1, IListRelation val2 ] => [ ..., val1 join val2 ]
	 */
	lrel_join_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IList left = (IList) arg_2;
			IList right = (IList) arg_1;
			if(left.length() == 0){
				return left;
			}
			if(right.length() == 0){
				return right;
			}
			Type leftType = left.get(0).getType();
			Type rightType = right.get(0).getType();
			assert leftType.isTuple();
			assert rightType.isTuple();

			int larity = leftType.getArity();
			int rarity = rightType.getArity();
			IValue fieldValues[] = new IValue[larity + rarity];
			IListWriter w =vf.listWriter();

			for (IValue ltuple : left){
				for (IValue rtuple: right) {
					for (int i = 0; i < larity; i++) {
						fieldValues[i] = ((ITuple)ltuple).get(i);
					}
					for (int i = larity; i < larity + rarity; i++) {
						fieldValues[i] = ((ITuple)rtuple).get(i - larity);
					}
					w.append(vf.tuple(fieldValues));
				}
			}
			return w.done();
		}
	},

	/** join on list relation and list
	 * 
	 * [ ..., IListRelation val1, IList val2 ] => [ ..., val1 join val2 ]
	 */
	lrel_join_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IList left = (IList) arg_2;
			IList right = (IList) arg_1;
			if(left.length() == 0){
				return left;
			}
			if(right.length() == 0){
				return right;
			}
			Type leftType = left.get(0).getType();
			assert leftType.isTuple();

			int larity = leftType.getArity();
			IValue fieldValues[] = new IValue[larity + 1];
			IListWriter w =vf.listWriter();

			for (IValue ltuple : left){
				for (IValue rval: right) {
					for (int i = 0; i < larity; i++) {
						fieldValues[i] = ((ITuple)ltuple).get(i);
					}
					fieldValues[larity] = rval;
					w.append(vf.tuple(fieldValues));
				}
			}
			return w.done();
		}
	},

	/** join on sets
	 * 
	 * [ ..., ISet val1, ISet val2 ] => [ ..., val1 join val2 ]
	 */
	set_join_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return set_product_set.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/** join on set and relation
	 * 
	 * [ ..., ISet val1, IRelation val2 ] => [ ..., val1 join val2 ]
	 */
	set_join_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet left = (ISet) arg_2;
			ISet right = (ISet) arg_1;
			if(left.size() == 0){
				return left;
			}
			if(right.size() == 0){
				return right;
			}
			Type rightType = right.getElementType();
			assert rightType.isTuple();

			int rarity = rightType.getArity();
			IValue fieldValues[] = new IValue[1 + rarity];
			ISetWriter w =vf.setWriter();

			for (IValue lval : left){
				for (IValue rtuple: right) {
					fieldValues[0] = lval;
					for (int i = 0; i <  rarity; i++) {
						fieldValues[i + 1] = ((ITuple)rtuple).get(i);
					}
					w.insert(vf.tuple(fieldValues));
				}
			}
			return w.done();
		}
	},

	/** join on rels
	 * 
	 * [ ..., IRelation val1, IRelation val2 ] => [ ..., val1 join val2 ]
	 */
	rel_join_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet left = (ISet) arg_2;
			ISet right = (ISet) arg_1;
			if(left.size() == 0){
				return left;
			}
			if(right.size() == 0){
				return right;
			}
			Type leftType = left.getElementType();
			Type rightType = right.getElementType();
			assert leftType.isTuple();
			assert rightType.isTuple();

			int larity = leftType.getArity();
			int rarity = rightType.getArity();
			IValue fieldValues[] = new IValue[larity + rarity];
			ISetWriter w =vf.setWriter();

			for (IValue ltuple : left){
				for (IValue rtuple: right) {
					for (int i = 0; i < larity; i++) {
						fieldValues[i] = ((ITuple)ltuple).get(i);
					}
					for (int i = larity; i < larity + rarity; i++) {
						fieldValues[i] = ((ITuple)rtuple).get(i - larity);
					}
					w.insert(vf.tuple(fieldValues));
				}
			}
			return w.done();
		}
	},

	/** join on relation and set
	 * 
	 * [ ..., IRelation val1, ISet val2 ] => [ ..., val1 join val2 ]
	 */
	rel_join_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet left = (ISet) arg_2;
			ISet right = (ISet) arg_1;
			if(left.size() == 0){
				return left;
			}
			if(right.size() == 0){
				return right;
			}
			Type leftType = left.getElementType();
			assert leftType.isTuple();

			int larity = leftType.getArity();
			IValue fieldValues[] = new IValue[larity + 1];
			ISetWriter w =vf.setWriter();

			for (IValue ltuple : left){
				for (IValue rval: right) {
					for (int i = 0; i < larity; i++) {
						fieldValues[i] = ((ITuple)ltuple).get(i);
					}
					fieldValues[larity] = rval;
					w.insert(vf.tuple(fieldValues));
				}
			}
			return w.done();
		}
	},

	// ==== less

	/**
	 * less-than on arbitrary values
	 * 
	 * [ ..., IValue val1, IValue val2 ] => [ ..., val1 < val2 ]
	 */
	
	/**
	 * See general remark about generic primitives
	 */
	less {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {

			IValue left = (IValue) arg_2;
			IValue right = (IValue) arg_1;
			Type leftType = left.getType();
			Type rightType = right.getType();

			if (leftType.isSubtypeOf(tf.numberType()) && rightType.isSubtypeOf(tf.numberType())) {
				return num_less_num.execute2(left, right, currentFrame, rex);
			}

			if(!leftType.comparable(rightType)){
				return Rascal_FALSE;
			}

			switch (ToplevelType.getToplevelType(leftType)) {
			// TODO: is this really faster than a TypeVisitor?? No because getTopLevelType includes a TypeVisitor itself.
			case BOOL:
				return bool_less_bool.execute2(left, right, currentFrame, rex);
			case STR:
				return str_less_str.execute2(left, right, currentFrame, rex);
			case DATETIME:
				return datetime_less_datetime.execute2(left, right, currentFrame, rex);
			case LOC:
				return loc_less_loc.execute2(left, right, currentFrame, rex);
			case LIST:
			case LREL:
				return list_less_list.execute2(left, right, currentFrame, rex);
			case SET:
			case REL:
				return set_less_set.execute2(left, right, currentFrame, rex);
			case MAP:
				return map_less_map.execute2(left, right, currentFrame, rex);
			case CONSTRUCTOR:
			case NODE:
				return node_less_node.execute2(left, right, currentFrame, rex);
			case ADT:
				return adt_less_adt.execute2(left, right, currentFrame, rex);
			case TUPLE:
				return tuple_less_tuple.execute2(left, right, currentFrame, rex);
			default:
				throw new InternalCompilerError("less: unexpected type " + leftType, rex.getStdErr(), currentFrame);
			}
		}
	},

	/**
	 * less-than on int and int
	 * 
	 * [ ..., IInteger val1, IInteger val2 ] => [ ..., val1 < val2 ]
	 */
	int_less_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).less((IInteger) arg_1);
		}
	},

	/**
	 * less-than on int and num
	 * 
	 * [ ..., IInteger val1, INumber val2 ] => [ ..., val1 < val2 ]
	 */
	int_less_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).less((INumber) arg_1);
		}
	},

	/**
	 * less-than on int and rat
	 * 
	 * [ ..., IInteger val1, IRational val2 ] => [ ..., val1 < val2 ]
	 */
	int_less_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).less((IRational) arg_1);
		}
	},

	/**
	 * less-than on int and real
	 * 
	 * [ ..., IInteger val1, IReal val2 ] => [ ..., val1 < val2 ]
	 */
	int_less_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).less((IReal) arg_1);
		}
	},

	/**
	 * less-than on num and int
	 * 
	 * [ ..., INumber val1, IInteger val2 ] => [ ..., val1 < val2 ]
	 */
	num_less_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).less((IInteger) arg_1);
		}
	},

	/**
	 * less-than on num and num
	 * 
	 * [ ..., INumber val1, INumber val2 ] => [ ..., val1 < val2 ]
	 */
	num_less_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).less((INumber) arg_1);
		}
	},

	/**
	 * less-than on num and rat
	 * 
	 * [ ..., INumber val1, IRational val2 ] => [ ..., val1 < val2 ]
	 */
	num_less_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).less((IRational) arg_1);
		}
	},

	/**
	 * less-than on num and real
	 * 
	 * [ ..., INumber val1, IReal val2 ] => [ ..., val1 < val2 ]
	 */
	num_less_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).less((IReal) arg_1);
		}
	},

	/**
	 * less-than on rat and int
	 * 
	 * [ ..., IRational val1, IInteger val2 ] => [ ..., val1 < val2 ]
	 */
	rat_less_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).less((IInteger) arg_1);
		}
	},

	/**
	 * less-than on rat and num
	 * 
	 * [ ..., IRational val1, INumber val2 ] => [ ..., val1 < val2 ]
	 */
	rat_less_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).less((INumber) arg_1);
		}
	},

	/**
	 * less-than on rat and rat
	 * 
	 * [ ..., IRational val1, IRational val2 ] => [ ..., val1 < val2 ]
	 */
	rat_less_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).less((IRational) arg_1);
		}
	},

	/**
	 * less-than on rat and real
	 * 
	 * [ ..., IRational val1, IReal val2 ] => [ ..., val1 < val2 ]
	 */
	rat_less_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).less((IReal) arg_1);
		}
	},

	/**
	 * less-than on real and num
	 * 
	 * [ ..., IReal val1, INumber val2 ] => [ ..., val1 < val2 ]
	 */
	real_less_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).less((INumber) arg_1);
		}
	},

	/**
	 * less-than on real and int
	 * 
	 * [ ..., IReal val1, IInteger val2 ] => [ ..., val1 < val2 ]
	 */
	real_less_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).less((IInteger) arg_1);
		}
	},

	/**
	 * less-than on real and numreal
	 * 
	 * [ ..., IReal val1, IReal val2 ] => [ ..., val1 < val2 ]
	 */
	real_less_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).less((IReal) arg_1);
		}
	},

	/**
	 * less-than on real and num
	 * 
	 * [ ..., IReal val1, IRational val2 ] => [ ..., val1 < val2 ]
	 */
	real_less_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).less((IRational) arg_1);
		}
	},

	/**
	 * less-than on adts
	 * 
	 * [ ..., IConstructor val1, IConstructor val2 ] => [ ..., val1 < val2 ]
	 */
	adt_less_adt {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return node_less_node.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * less-than on bool
	 * 
	 * [ ..., IBool val1, IBool val2 ] => [ ..., val1 < val2 ]
	 */
	bool_less_bool {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			boolean left = ((IBool) arg_2).getValue();
			boolean right = ((IBool) arg_1).getValue();

			return vf.bool(!left && right);
		}
	},
	//		bool_or_bool {
	//			@Override
	//			public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
	//				assert arity == 2;
	//				boolean left = ((IBool) arg_2).getValue();
	//				boolean right = ((IBool) arg_1).getValue();
	//
	//				stack[sp - 2] = vf.bool(left || right);
	//				return sp - 1;
	//			}
	//		},

	/**
	 * less-than on datetime
	 * 
	 * [ ..., IDateTime val1, IDateTime val2 ] => [ ..., val1 < val2 ]
	 */
	datetime_less_datetime {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IDateTime) arg_2).compareTo((IDateTime) arg_1) == -1);
		}
	},

	/**
	 * less-than on list
	 * 
	 * [ ..., IList val1, IList val2 ] => [ ..., val1 < val2 ]
	 */
	list_less_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IList left = (IList) arg_2;
			IList right = (IList) arg_1;
			return $list_less_list(left, right);
		}
	},

	/**
	 * less-than on list relation
	 * 
	 * [ ..., IListRelation val1, IListRelation val2 ] => [ ..., val1 < val2 ]
	 */
	lrel_less_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IList left = (IList) arg_2;
			IList right = (IList) arg_1;
			return $list_less_list(left, right);
		}
	},

	/**
	 * less-than on loc
	 * 
	 * [ ..., ISourceLocation val1, ISourceLocation val2 ] => [ ..., val1 < val2 ]
	 */
	loc_less_loc {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISourceLocation left = (ISourceLocation) arg_2;
			ISourceLocation right = (ISourceLocation) arg_1;

			int compare = SourceLocationURICompare.compare(left, right);
			if (compare < 0) {
				return Rascal_TRUE;
			}
			else if (compare > 0) {
				return Rascal_FALSE;
			}

			// but the uri's are the same
			// note that line/column information is superfluous and does not matter for ordering

			if (left.hasOffsetLength()) {
				if (!right.hasOffsetLength()) {
					return Rascal_FALSE;
				}

				int roffset = right.getOffset();
				int rlen = right.getLength();
				int loffset = left.getOffset();
				int llen = left.getLength();

				if (loffset == roffset) {
					return vf.bool(llen < rlen);
				}
				return vf.bool(roffset < loffset && roffset + rlen >= loffset + llen);
			}
			else if (compare == 0) {
				return Rascal_FALSE;
			}

			if (!right.hasOffsetLength()) {
				throw new InternalCompilerError("offset length missing", rex.getStdErr(), currentFrame);
			}
			return Rascal_FALSE;
		}
	},

	/**
	 * less-than on map
	 * 
	 * [ ..., IMap val1, IMap val2 ] => [ ..., val1 < val2 ]
	 */
	map_less_map {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IMap left = ((IMap) arg_2);
			IMap right = ((IMap) arg_1);
			return vf.bool(left.isSubMap(right) && !right.isSubMap(left));
		}
	},

	/**
	 * less-than on node
	 * 
	 * [ ..., INode val1, INode val2 ] => [ ..., val1 < val2 ]
	 */
	node_less_node {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			INode left = (INode) arg_2;
			INode right = (INode) arg_1;

			int compare = left.getName().compareTo(right.getName());

			if (compare <= -1) {
				return Rascal_TRUE;
			}

			if (compare >= 1){
				return Rascal_FALSE;
			}

			// if the names are not ordered, then we order lexicographically on the arguments:

			int leftArity = left.arity();
			int rightArity = right.arity();

			Object result =  Rascal_FALSE;
			for (int i = 0; i < Math.min(leftArity, rightArity); i++) {
				
				if(leftArity < rightArity || i < leftArity - 1)
					result = lessequal.execute2(left.get(i), right.get(i), currentFrame, rex);
				else
					result = less.execute2(left.get(i), right.get(i), currentFrame, rex);

				if(!((IBool)result).getValue()){
					return Rascal_FALSE;
				}
			}

			if (!left.mayHaveKeywordParameters() && !right.mayHaveKeywordParameters()) {
				if (left.asAnnotatable().hasAnnotations() || right.asAnnotatable().hasAnnotations()) {
					// bail out 
					return Rascal_FALSE;
				}
			}

			if (!left.asWithKeywordParameters().hasParameters() && right.asWithKeywordParameters().hasParameters()) {
				return Rascal_TRUE;
			}

			if (left.asWithKeywordParameters().hasParameters() && !right.asWithKeywordParameters().hasParameters()) {
				return Rascal_FALSE;
			}

			if (left.asWithKeywordParameters().hasParameters() && right.asWithKeywordParameters().hasParameters()) {
				Map<String, IValue> paramsLeft = left.asWithKeywordParameters().getParameters();
				Map<String, IValue> paramsRight = right.asWithKeywordParameters().getParameters();
				if (paramsLeft.size() < paramsRight.size()) {
					return Rascal_TRUE;
				}
				if (paramsLeft.size() > paramsRight.size()) {
					return Rascal_FALSE;
				}
				if (paramsRight.keySet().containsAll(paramsLeft.keySet()) && !paramsRight.keySet().equals(paramsLeft.keySet())) {
					return Rascal_TRUE;
				}
				if (paramsLeft.keySet().containsAll(paramsLeft.keySet()) && !paramsRight.keySet().equals(paramsLeft.keySet())) {
					return Rascal_FALSE;
				}
				//assert paramsLeft.keySet().equals(paramsRight.keySet());
				for (String k: paramsLeft.keySet()) {
					result = less.execute2(paramsLeft.get(k), paramsRight.get(k), currentFrame, rex);

					if(!((IBool)result).getValue()){
						return Rascal_FALSE;
					}
				}
			}

			return vf.bool((leftArity < rightArity) || ((IBool)result).getValue());
		}
	},

	/**
	 * less-than on set
	 * 
	 * [ ..., ISet val1, ISet val2 ] => [ ..., val1 < val2 ]
	 */
	set_less_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet lhs = (ISet) arg_2;
			ISet rhs = (ISet) arg_1;
			return vf.bool(!lhs.isEqual(rhs) && lhs.isSubsetOf(rhs));
		}
	},

	/**
	 * less-than on rel
	 * 
	 * [ ..., IRelation val1, IRelation val2 ] => [ ..., val1 < val2 ]
	 */
	rel_less_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet lhs = (ISet) arg_2;
			ISet rhs = (ISet) arg_1;
			return vf.bool(!lhs.isEqual(rhs) && lhs.isSubsetOf(rhs));
		}
	},

	/**
	 * less-than on str
	 * 
	 * [ ..., IString val1, IString val2 ] => [ ..., val1 < val2 ]
	 */
	str_less_str {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			int c = ((IString) arg_2).compare((IString) arg_1);
			return vf.bool(c == -1);
		}
	},

	/**
	 * less-than on tuple
	 * 
	 * [ ..., ITuple val1, ITuple val2 ] => [ ..., val1 < val2 ]
	 */
	tuple_less_tuple {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ITuple left = (ITuple)arg_2;
			int leftArity = left.arity();
			ITuple right = (ITuple)arg_1;
			int rightArity = right.arity();

			for (int i = 0; i < Math.min(leftArity, rightArity); i++) {
				Object result;
				if(leftArity < rightArity || i < leftArity - 1)
					result = equal.execute2(left.get(i), right.get(i), currentFrame, rex);
				else
					result =less.execute2(left.get(i), right.get(i), currentFrame, rex);

				if(!((IBool)result).getValue()){
					return Rascal_FALSE;
				}
			}

			return vf.bool(leftArity <= rightArity);
		}
	},

	// ==== lessequal

	/**
	 * less-than-or-equal on arbitrary values
	 * 
	 * [ ... IValue val1, IValue val2 ] => [ ..., val1 <= val2 ]
	 */
	
	/**
	 * See general remark about generic primitives
	 */
	lessequal {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {

			IValue left = (IValue) arg_2;
			IValue right = (IValue) arg_1;
			Type leftType = ((IValue) arg_2).getType();
			Type rightType = ((IValue) arg_1).getType();

			if (leftType.isSubtypeOf(tf.numberType()) && rightType.isSubtypeOf(tf.numberType())) {
				return num_lessequal_num.execute2(left, right, currentFrame, rex);
			}

			if(!leftType.comparable(rightType)){
				return Rascal_FALSE;
			}

			switch (ToplevelType.getToplevelType(leftType)) {

			case BOOL:
				return bool_lessequal_bool.execute2(left, right, currentFrame, rex);

			case STR:
				return str_lessequal_str.execute2(left, right, currentFrame, rex);

			case DATETIME:
				return datetime_lessequal_datetime.execute2(left, right, currentFrame, rex);

			case LOC:
				return loc_lessequal_loc.execute2(left, right, currentFrame, rex);

			case LIST:
			case LREL:
				return list_lessequal_list.execute2(left, right, currentFrame, rex);
			case SET:
			case REL:
				return set_lessequal_set.execute2(left, right, currentFrame, rex);
			case MAP:
				return map_lessequal_map.execute2(left, right, currentFrame, rex);
			case CONSTRUCTOR:
			case NODE:
				return node_lessequal_node.execute2(left, right, currentFrame, rex);
			case ADT:
				return adt_lessequal_adt.execute2(left, right, currentFrame, rex);
			case TUPLE:
				return tuple_lessequal_tuple.execute2(left, right, currentFrame, rex);
			default:
				throw new InternalCompilerError("lessequal: unexpected type " + leftType, rex.getStdErr(), currentFrame);
			}
		}
	},

	/**
	 * less-than-or-equal on int and int
	 * 
	 * [ ... IInteger val1, IInteger val2 ] => [ ..., val1 <= val2 ]
	 */
	int_lessequal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).lessEqual((IInteger) arg_1);
		}
	},

	/**
	 * less-than-or-equal on int and num
	 * 
	 * [ ... IInteger val1, INumber val2 ] => [ ..., val1 <= val2 ]
	 */
	int_lessequal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).lessEqual((INumber) arg_1);
		}
	},

	/**
	 * less-than-or-equal on int and rat
	 * 
	 * [ ... IInteger val1, IRational val2 ] => [ ..., val1 <= val2 ]
	 */
	int_lessequal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).lessEqual((IRational) arg_1);
		}
	},

	/**
	 * less-than-or-equal on int and real
	 * 
	 * [ ... IInteger val1, IReal val2 ] => [ ..., val1 <= val2 ]
	 */
	int_lessequal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).lessEqual((IReal) arg_1);
		}
	},

	/**
	 * less-than-or-equal on num and int
	 * 
	 * [ ... INumber val1, IInteger val2 ] => [ ..., val1 <= val2 ]
	 */
	num_lessequal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).lessEqual((IInteger) arg_1);
		}
	},

	/**
	 * less-than-or-equal on num and num
	 * 
	 * [ ... INumber val1, INumber val2 ] => [ ..., val1 <= val2 ]
	 */
	num_lessequal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).lessEqual((INumber) arg_1);
		}
	},

	/**
	 * less-than-or-equal on num and rat
	 * 
	 * [ ... INumber val1, IRational val2 ] => [ ..., val1 <= val2 ]
	 */
	num_lessequal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).lessEqual((IRational) arg_1);
		}
	},

	/**
	 * less-than-or-equal on num and real
	 * 
	 * [ ... INumber val1, IReal val2 ] => [ ..., val1 <= val2 ]
	 */
	num_lessequal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).lessEqual((IReal) arg_1);
		}
	},

	/**
	 * less-than-or-equal on rat and int
	 * 
	 * [ ... IRational val1, IInteger val2 ] => [ ..., val1 <= val2 ]
	 */
	rat_lessequal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).lessEqual((IInteger) arg_1);
		}
	},

	/**
	 * less-than-or-equal on rat and num
	 * 
	 * [ ... IRational val1, INumber val2 ] => [ ..., val1 <= val2 ]
	 */
	rat_lessequal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).lessEqual((INumber) arg_1);
		}
	},

	/**
	 * less-than-or-equal on rat and rat
	 * 
	 * [ ... IRational val1, IRational val2 ] => [ ..., val1 <= val2 ]
	 */
	rat_lessequal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).lessEqual((IRational) arg_1);
		}
	},

	/**
	 * less-than-or-equal on rat and real
	 * 
	 * [ ... IRational val1, IReal val2 ] => [ ..., val1 <= val2 ]
	 */
	rat_lessequal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).lessEqual((IReal) arg_1);
		}
	},

	/**
	 * less-than-or-equal on real and num
	 * 
	 * [ ... IReal val1, INumber val2 ] => [ ..., val1 <= val2 ]
	 */
	real_lessequal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).lessEqual((INumber) arg_1);
		}
	},


	/**
	 * less-than-or-equal on real and int
	 * 
	 * [ ... IReal val1, IInteger val2 ] => [ ..., val1 <= val2 ]
	 */
	real_lessequal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).lessEqual((IInteger) arg_1);
		}
	},

	/**
	 * less-than-or-equal on real and real
	 * 
	 * [ ... IReal val1, IReal val2 ] => [ ..., val1 <= val2 ]
	 */
	real_lessequal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).lessEqual((IReal) arg_1);
		}
	},

	/**
	 * less-than-or-equal on real and rat
	 * 
	 * [ ... IReal val1, IRational val2 ] => [ ..., val1 <= val2 ]
	 */
	real_lessequal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).lessEqual((IRational) arg_1);
		}
	},

	/**
	 * less-than-or-equal on adt
	 * 
	 * [ ... IConstructor val1, IConstructor val2 ] => [ ..., val1 <= val2 ]
	 */
	adt_lessequal_adt {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return node_lessequal_node.execute2(arg_2, arg_1, currentFrame, rex);
		}
	},

	/**
	 * less-than-or-equal on bool
	 * 
	 * [ ... IBool val1, IBool val2 ] => [ ..., val1 <= val2 ]
	 */
	bool_lessequal_bool {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			boolean left = ((IBool) arg_2).getValue();
			boolean right = ((IBool) arg_1).getValue();

			return vf.bool((!left && right) || (left == right));
		}
	},

	/**
	 * less-than-or-equal on datetime
	 * 
	 * [ ... IDateTime val1, IDateTime val2 ] => [ ..., val1 <= val2 ]
	 */
	datetime_lessequal_datetime {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			int c = ((IDateTime) arg_2).compareTo((IDateTime) arg_1);
			return  vf.bool(c == -1 || c == 0);
		}
	},

	/**
	 * less-than-or-equal on list
	 * 
	 * [ ... IList val1, IList val2 ] => [ ..., val1 <= val2 ]
	 */
	list_lessequal_list {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IList left = (IList) arg_2;
			IList right = (IList) arg_1;

			return $list_lessequal_list(left, right);
		}
	},

	/**
	 * less-than-or-equal on list relation
	 * 
	 * [ ... IListRelation val1, IListRelation val2 ] => [ ..., val1 <= val2 ]
	 */
	lrel_lessequal_lrel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IList left = (IList) arg_2;
			IList right = (IList) arg_1;

			return $list_lessequal_list(left, right);
		}

	},

	/**
	 * less-than-or-equal on loc
	 * 
	 * [ ... ISourceLocation val1, ISourceLocation val2 ] => [ ..., val1 <= val2 ]
	 */
	loc_lessequal_loc {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISourceLocation left = (ISourceLocation) arg_2;
			ISourceLocation right = (ISourceLocation) arg_1;

			int compare = SourceLocationURICompare.compare(left, right);
			if (compare < 0) {
				return Rascal_TRUE;
			}
			else if (compare > 0) {
				return Rascal_FALSE;
			}

			// but the uri's are the same
			// note that line/column information is superfluous and does not matter for ordering

			if (left.hasOffsetLength()) {
				if (!right.hasOffsetLength()) {
					return Rascal_FALSE;
				}

				int roffset = right.getOffset();
				int rlen = right.getLength();
				int loffset = left.getOffset();
				int llen = left.getLength();

				if (loffset == roffset) {
					return vf.bool(llen <= rlen);
				}
				return vf.bool(roffset < loffset && roffset + rlen >= loffset + llen);
			}
			else if (compare == 0) {
				return Rascal_TRUE;
			}

			if (!right.hasOffsetLength()) {
				throw new InternalCompilerError("missing offset length", rex.getStdErr(), currentFrame);
			}
			return Rascal_FALSE;
		}
	},

	/**
	 * less-than-or-equal on map
	 * 
	 * [ ... IMap val1, IMap val2 ] => [ ..., val1 <= val2 ]
	 */
	map_lessequal_map {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IMap left = (IMap) arg_2;
			IMap right = (IMap) arg_1;
			return vf.bool(left.isSubMap(right));
		}
	},

	/**
	 * less-than-or-equal on node
	 * 
	 * [ ... INode val1, INode val2 ] => [ ..., val1 <= val2 ]
	 */
	node_lessequal_node {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			INode left = (INode) arg_2;
			INode right = (INode) arg_1;

			int compare = left.getName().compareTo(right.getName());

			if (compare <= -1) {
				return Rascal_TRUE;
			}

			if (compare >= 1){
				return Rascal_FALSE;
			}

			// if the names are not ordered, then we order lexicographically on the arguments:

			int leftArity = left.arity();
			int rightArity = right.arity();

			for (int i = 0; i < Math.min(leftArity, rightArity); i++) {
				if(!$lessequal(left.get(i), right.get(i), currentFrame, rex).getValue()){
					return Rascal_FALSE;
				}
			}
			return vf.bool(leftArity <= rightArity);
		}
	},

	/**
	 * less-than-or-equal on losetc
	 * 
	 * [ ... ISet val1, ISet val2 ] => [ ..., val1 <= val2 ]
	 */
	set_lessequal_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet left = (ISet) arg_2;
			ISet right = (ISet) arg_1;
			return vf.bool(left.size() == 0 || left.isEqual(right) || left.isSubsetOf(right));
		}	
	},

	/**
	 * less-than-or-equal on rel
	 * 
	 * [ ... IRelation val1, IRelation val2 ] => [ ..., val1 <= val2 ]
	 */
	rel_lessequal_rel {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet left = (ISet) arg_2;
			ISet right = (ISet) arg_1;
			return vf.bool(left.isEqual(right) || left.isSubsetOf(right));
		}	
	},

	/**
	 * less-than-or-equal on str
	 * 
	 * [ ... IString val1, IString val2 ] => [ ..., val1 <= val2 ]
	 */
	str_lessequal_str {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			int c = ((IString) arg_2).compare((IString) arg_1);
			return vf.bool(c == -1 || c == 0);
		}
	},

	/**
	 * less-than-or-equal on tuple
	 * 
	 * [ ... ITuple val1, ITuple val2 ] => [ ..., val1 <= val2 ]
	 */
	tuple_lessequal_tuple {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ITuple left = (ITuple)arg_2;
			int leftArity = left.arity();
			ITuple right = (ITuple)arg_1;
			int rightArity = right.arity();

			for (int i = 0; i < Math.min(leftArity, rightArity); i++) {			
				if(!$lessequal(left.get(i), right.get(i), currentFrame, rex).getValue()){
					return Rascal_FALSE;
				}
			}

			return vf.bool(leftArity <= rightArity);
		}
	},

	// ==== transitiveClosure

	/**
	 * transitiveClosure on arbitrary values
	 * 
	 * [ ..., IValue val ] => [ ..., val* ]
	 * 
	 * postfix Closure "+", "*" { 
	 *  	lrel[&L,&L]			-> lrel[&L,&L],
	 * 		rel[&L,&L]  		-> rel[&L,&L]
	 * }
	 */
	transitive_closure {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue lhs = (IValue) arg_1;
			Type lhsType = lhs.getType();
			if(lhsType.isListRelation()){
				return lrel_transitive_closure.execute1(arg_1, currentFrame, rex);
			}
			if(lhsType.isRelation()){
				return rel_transitive_closure.execute1(arg_1, currentFrame, rex);
			}
			throw new InternalCompilerError("transitive_closure: unexpected type " + lhsType, currentFrame);
		}

	},

	/**
	 * transitiveClosure on lrel
	 * 
	 * [ ..., IListRelation val ] => [ ..., val* ]
	 */
	lrel_transitive_closure {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IListRelation<IList> left = ((IList) arg_1).asRelation();
			return left.closure();
		}
	},

	/**
	 * transitiveClosure on rel
	 * 
	 * [ ..., IRelation val ] => [ ..., val* ]
	 */
	rel_transitive_closure {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISetRelation<ISet> left = ((ISet) arg_1).asRelation();
			return left.closure();
		}
	},

	/**
	 * transitiveReflexiveClosure on arbitrary values
	 * 
	 *  [ ..., IValue val ] => [ ..., val+ ]
	 * 
	 * postfix Closure "+", "*" { 
	 *  	lrel[&L,&L]			-> lrel[&L,&L],
	 * 		rel[&L,&L]  		-> rel[&L,&L]
	 */
	transitive_reflexive_closure {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue lhs = (IValue) arg_1;
			Type lhsType = lhs.getType();
			if(lhsType.isListRelation()){
				return lrel_transitive_reflexive_closure.execute1(arg_1, currentFrame, rex);
			}
			if(lhsType.isRelation()){
				return rel_transitive_reflexive_closure.execute1(arg_1, currentFrame, rex);
			}
			throw new InternalCompilerError("transitive_reflexive_closure: unexpected type " + lhsType, currentFrame);
		}
	},

	/**
	 * transitiveReflexiveClosure on lrel
	 * 
	 *  [ ..., IListRelation val ] => [ ..., val+ ]
	 */
	lrel_transitive_reflexive_closure {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IListRelation<IList> left = ((IList) arg_1).asRelation();
			return left.closureStar();
		}

	},

	/**
	 * transitiveReflexiveClosure on rel
	 * 
	 *  [ ..., IRelation val ] => [ ..., val+ ]
	 */
	rel_transitive_reflexive_closure {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISetRelation<ISet> left = ((ISet) arg_1).asRelation();
			return left.closureStar();
		}
	},

	/**
	 * notequal on arbitrary values
	 * 
	 * [ ..., IValue val1, IValue val2 ] => [ ..., val1 != val2 ]
	 */
	
	/**
	 * See general remark about generic primitives
	 */
	notequal {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(!((IValue) arg_2).isEqual((IValue) arg_1));
		}
	},

	/**
	 * notequal on int and int
	 * 
	 * [ ..., IInteger val1, IInteger val2 ] => [ ..., val1 != val2 ]
	 */
	int_notequal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).equal((IInteger) arg_1).not();
		}
	},

	/**
	 * notequal on int and num
	 * 
	 * [ ..., IInteger val1, INumber val2 ] => [ ..., val1 != val2 ]
	 */
	int_notequal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).equal((INumber) arg_1).not();
		}
	},

	/**
	 * notequal on int and rat
	 * 
	 * [ ..., IInteger val1, IRational val2 ] => [ ..., val1 != val2 ]
	 */
	int_notequal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).equal((IRational) arg_1).not();
		}
	},

	/**
	 * notequal on int and real
	 * 
	 * [ ..., IInteger val1, IReal val2 ] => [ ..., val1 != val2 ]
	 */
	int_notequal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_2).equal((IReal) arg_1).not();
		}
	},

	/**
	 * notequal on num and int
	 * 
	 * [ ..., INumber val1, IInteger val2 ] => [ ..., val1 != val2 ]
	 */
	num_notequal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).equal((IInteger) arg_1).not();
		}
	},

	/**
	 * notequal on num and int
	 * 
	 * [ ..., INumber val1, INumber val2 ] => [ ..., val1 != val2 ]
	 */
	num_notequal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).equal((INumber) arg_1).not();
		}
	},

	/**
	 * notequal on num and rat
	 * 
	 * [ ..., INumber val1, IRational val2 ] => [ ..., val1 != val2 ]
	 */
	num_notequal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).equal((IRational) arg_1).not();
		}
	},

	/**
	 * notequal on num and real
	 * 
	 * [ ..., INumber val1, IReal val2 ] => [ ..., val1 != val2 ]
	 */
	num_notequal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_2).equal((IReal) arg_1).not();
		}
	},

	/**
	 * notequal on real and int
	 * 
	 * [ ..., IReal val1, IInteger val2 ] => [ ..., val1 != val2 ]
	 */
	real_notequal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).equal((IInteger) arg_1).not();
		}
	},

	/**
	 * notequal on real and num
	 * 
	 * [ ..., IReal val1, INumber val2 ] => [ ..., val1 != val2 ]
	 */
	real_notequal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).equal((INumber) arg_1).not();
		}
	},

	/**
	 * notequal on real and rat
	 * 
	 * [ ..., IReal val1, IRational val2 ] => [ ..., val1 != val2 ]
	 */
	real_notequal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).equal((IRational) arg_1).not();
		}
	},

	/**
	 * notequal on real and irealnt
	 * 
	 * [ ..., IReal val1, IReal val2 ] => [ ..., val1 != val2 ]
	 */
	real_notequal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_2).equal((IReal) arg_1).not();
		}
	},

	/**
	 * notequal on rat and int
	 * 
	 * [ ..., IRational val1, IInteger val2 ] => [ ..., val1 != val2 ]
	 */
	rat_notequal_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).equal((IInteger) arg_1).not();
		}
	},

	/**
	 * notequal on rat and num
	 * 
	 * [ ..., IRational val1, INUmber val2 ] => [ ..., val1 != val2 ]
	 */
	rat_notequal_num {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).equal((INumber) arg_1).not();
		}
	},

	/**
	 * notequal on rat and rat
	 * 
	 * [ ..., IRational val1, IRational val2 ] => [ ..., val1 != val2 ]
	 */
	rat_notequal_rat {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).equal((IRational) arg_1).not();
		}
	},

	/**
	 * notequal on rat and real
	 * 
	 * [ ..., IRational val1, IReal val2 ] => [ ..., val1 != val2 ]
	 */
	rat_notequal_real {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_2).equal((IReal) arg_1).not();
		}
	},

	/**
	 * negative on arbitrary value
	 * 
	 * [ ..., IValue val ] => [ ..., -val ]
	 * 
	 * prefix UnaryMinus "-" { &L <: num -> &L }
	 */
	
	/**
	 * See general remark about generic primitives
	 */
	negative {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {

			IValue left = (IValue) arg_1;
			Type leftType = left.getType();

			switch (ToplevelType.getToplevelType(leftType)) {
			case INT: return negative_int.execute1(left, currentFrame, rex);
			case NUM: return negative_num.execute1(left, currentFrame, rex);
			case REAL: return negative_real.execute1(left, currentFrame, rex);
			case RAT: return negative_rat.execute1(left, currentFrame, rex);
			default:
				throw new InternalCompilerError("negative: unexpected type " + leftType, currentFrame);
			}
		}
	},

	/**
	 * negative on int
	 * 
	 * [ ..., IInteger val ] => [ ..., -val ]
	 */
	negative_int {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IInteger) arg_1).negate();
		}
	},

	/**
	 * negative on real
	 * 
	 * [ ..., IReal val ] => [ ..., -val ]
	 */
	negative_real {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IReal) arg_1).negate();
		}
	},

	/**
	 * negative on rat
	 * 
	 * [ ..., IRational val ] => [ ..., -val ]
	 */
	negative_rat {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((IRational) arg_1).negate();
		}
	},

	/**
	 * negative on num
	 * 
	 * [ ..., INumber val ] => [ ..., -val ]
	 */
	negative_num {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_1).negate();
		}
	},

	/********************************************************************************************************/
	/* 								Type-related operators and functions									*/
	/********************************************************************************************************/

	/**
	 * is (is-type) on arbitrary value
	 * 
	 * [ ... IValue val1, IString typeName] => [ ..., val1 is typeName ]
	 */
	is {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue val  = (IValue) arg_2;
			Type tp = val.getType();
			String name = ((IString) arg_1).getValue();
			if(tp.isAbstractData()){
				if(tp.getName().equals("Tree")){
					IConstructor cons = (IConstructor) val;
					if(cons.getName().equals("appl")){
						IConstructor prod = (IConstructor) cons.get(0);
						IConstructor def = (IConstructor) prod.get(0);
						if(def.getName().equals("label")){
							return vf.bool(((IString) def.get(0)).getValue().equals(name));
						}
					}
				} else {
					String consName = ((IConstructor)val).getConstructorType().getName();
					if(consName.startsWith("\\")){
						consName = consName.substring(1);
					}
					return vf.bool(consName.equals(name));
				}
			} else if(tp.isNode()){
				String nodeName = ((INode) val).getName();
				if(nodeName.startsWith("\\")){
					nodeName = nodeName.substring(1);
				}
				return vf.bool(nodeName.equals(name));
			} 
			return Rascal_FALSE;
		}
	},

	/**
	 * is (is-type) for bool type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is bool ]
	 */
	is_bool {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isBool());
		}

	},

	/**
	 * is (is-type) for datetime type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is datetime ]
	 */
	is_datetime {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isDateTime());
		}
	},

	/**
	 * is (is-type) for int type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is int ]
	 */
	is_int {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isInteger());
		}
	},

	/**
	 * is (is-type) for list type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is list ]
	 */
	is_list {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isList());
		}
	},

	/**
	 * is (is-type) for loc type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is loc ]
	 */
	is_loc {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isSourceLocation());
		}
	},

	/**
	 * is (is-type) for list relation type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is lrel ]
	 */
	is_lrel {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isListRelation());
		}

	},

	/**
	 * is (is-type) for map type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is map ]
	 */
	is_map {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isMap());
		}

	},

	/**
	 * is (is-type) for node type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is node ]
	 */
	is_node {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isNode());
		}

	},

	/**
	 * is (is-type) for num type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is num ]
	 */
	is_num {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isNumber());
		}
	},

	/**
	 * is (is-type) for rat type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is rat ]
	 */
	is_rat {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isRational());
		}
	},

	/**
	 * is (is-type) for real type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is real ]
	 */
	is_real {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isReal());
		}
	},

	/**
	 * is (is-type) for rel type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is rel ]
	 */
	is_rel {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isRelation());
		}
	},

	/**
	 * is (is-type) for set type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is set ]
	 */
	is_set {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isSet());
		}
	},

	/**
	 * is (is-type) for str type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is str ]
	 */
	is_str {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isString());
		}
	},

	/**
	 * is (is-type) for tuple type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is tuple ]
	 */
	is_tuple {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_1).getType().isTuple());
		}	
	},

	/**
	 * is (is-type) for nonterminal type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is appl ]
	 */
	is_appl {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(arg_1 instanceof ITree && ((ITree) arg_1).isAppl());
		}	
	},

	/**
	 * is (is-type) for amb type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is amb ]
	 */
	is_amb {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			Object treeSubject = arg_1;
			return vf.bool(treeSubject instanceof ITree && ((ITree) arg_1).isAmb());
		}	
	},

	/**
	 * is (is-type) for datetimelayout type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is layout ]
	 */
	is_layout {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue treeSubject = (IValue) arg_1;
			Type subjectType = treeSubject.getType();
			return vf.bool(subjectType.isAbstractData() && TreeAdapter.isLayout((ITree)treeSubject));
		}	
	},

	/**
	 * is (is-type) for concrete list type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is concretelist ]
	 */
	is_concretelist {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue treeSubject = (IValue) arg_1;
			Type subjectType = treeSubject.getType();
			return vf.bool(subjectType.isAbstractData() && (TreeAdapter.isList((ITree)treeSubject) || TreeAdapter.isOpt((ITree)treeSubject)));
		}	
	},

	/**
	 * is (is-type) for lexical type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is lexical ]
	 */
	is_lexical {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue treeSubject = (IValue) arg_1;
			Type subjectType = treeSubject.getType();
			return vf.bool(subjectType.isAbstractData() && TreeAdapter.isLexical((ITree)treeSubject));
		}	
	},

	/**
	 * is (is-type) for char type
	 * 
	 * [ ... IValue val1] => [ ..., val1 is char ]
	 */
	is_char {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue treeSubject = (IValue) arg_1;
			Type subjectType = treeSubject.getType();
			return vf.bool(subjectType.isAbstractData() && TreeAdapter.isChar((ITree)treeSubject));
		}	
	},

	/**
	 * subtype-of
	 * 
	 * [ ..., Type t1, Type t1 ] => [ ..., t1 <: t2 ]
	 */
	subtype {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((Type) arg_2).isSubtypeOf((Type) arg_1));
			//return vf.bool(rex.isSubtypeOf((Type) arg_2, (Type) arg_1));
		}
	},

	/**
	 * subtype-of-value
	 * 
	 * [ ..., IValue v, Type t ] => [ ..., typeOf(v) <: t ]
	 */
	subtype_value_type {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_2).getType().isSubtypeOf((Type) arg_1));
			//return vf.bool(rex.isSubtypeOf(((IValue) arg_2).getType(), (Type) arg_1));
		}
	},
	
	/**
	 * subtype-of-value-value
	 * 
	 * [ ..., IValue v1, Value v2 ] => [ ..., typeOf(v1) <: typeOf(v2) ]
	 */
	subtype_value_value {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.bool(((IValue) arg_2).getType().isSubtypeOf(((IValue) arg_1).getType()));
			//return vf.bool(rex.isSubtypeOf(((IValue) arg_2).getType(), (Type) arg_1));
		}
	},


	/**
	 * typeOf a value
	 * 
	 * [ ..., IValue v ] => ..., typeOf(v) ]
	 */
	@SuppressWarnings("unchecked")
	typeOf {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			if(arg_1 instanceof HashSet<?>){	// For the benefit of set matching
				// Move to muPrimitives?
				HashSet<IValue> mset = (HashSet<IValue>) arg_1;
				if(mset.isEmpty()){
					return tf.setType(tf.voidType());
				} else {
					IValue v = mset.iterator().next();		// TODO: this is incorrect for set[value]!
					return tf.setType(v.getType());
				}

			} else {
				return ((IValue) arg_1).getType();
			}
		}
	},

	/**
	 * Convert from type to Symbol
	 * 
	 * [ ..., Type t ] => [ ... Symbol s ]
	 * 
	 * TODO redundant arg here
	 */

	type2symbol {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			Type type = (Type) arg_2;
			return rex.typeToSymbol(type);
			//return $type2symbol(type);
		}
	},

	/**
	 * Get the element type of a composite type.
	 * 
	 * [ ..., Type t ] => [ ..., elementTypeOf(t) ]
	 */
	elementTypeOf {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			Type tp = (Type) arg_1;
			return tp.getElementType();
		}
	},

	/*******************************************************************************************************/
	/*				String templates																	  */
	/******************************************************************************************************/

	/**
	 * Create a string template
	 * 
	 * [ ... ] => [ ... ]
	 * or
	 * [ ..., IString initial ] => [ ..., initial]
	 * 
	 * Note: The string builder is maintained implicitly on the templateBuilderStack.
	 * This may cause problems (i.e. leaving spurious builders on that stack)  when an exception
	 * occurs during template construction. 
	 */
	template_open {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity <= 1;
			String pre = "";
			if(arity == 1){
				pre = ((IString) stack[sp - 1]).getValue();
				stack[sp - 1] = vf.string("");
			} else {
				stack[sp] = vf.string("");
			}
			$pushIndent("", rex);
			rex.getTemplateBuilderStack().push(rex.getTemplateBuilder());
			StringBuilder templateBuilder = new StringBuilder();
			templateBuilder.append($unescape(pre));
			rex.setTemplateBuilder(templateBuilder);
			return arity == 1 ? sp : sp + 1;
		}
	},

	/**
	 * Increase indentation in string template
	 * 
	 * [ ..., IString indent ] => [ ..., ""]
	 */
	template_indent {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			String ind = ((IString) arg_1).getValue();
			$indent(ind, rex);
			return vf.string("");
		}
	},

	/**
	 * Decrease indentation in string template
	 * 
	 * [ ..., IString indent ] => [ ..., ""]
	 */
	template_unindent {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			String ind = ((IString) arg_1).getValue();
			$unindent(ind, rex);
			return vf.string("");
		}
	},

	/**
	 * Add string to string template
	 * 
	 * [ ..., IString val ] => [ ..., ""]
	 */
	template_add {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IString iarg_s = vf.string($value_to_string(arg_1, currentFrame, rex));
			String arg_s = $removeMargins(iarg_s, rex).getValue();

			rex.getTemplateBuilder().append(arg_s);
			return vf.string("");
		}
	},

	/**
	 * Close string template
	 * 
	 * [ ... ] => [ ..., IString value_of_template]
	 */
	template_close {
		@Override
		public Object execute0(final Frame currentFrame, final RascalExecutionContext rex) {
			$popIndent(rex);
			IString result = vf.string(rex.getTemplateBuilder().toString());
			StringBuilder templateBuilder = rex.getTemplateBuilderStack().pop();
			rex.setTemplateBuilder(templateBuilder);
			return result;
		}
	},

	/******************************************************************************************/
	/*			Fields and Field updates							 						  */
	/******************************************************************************************/

	/**
	 * Runtime check whether given constructor has a named field (positional or keyword).
	 * hasMap represents required type information and has entries in the format:
	 * [consName, fieldNames] => [kwNames]
	 * 
	 * [ ..., IConstructor cons, IString fieldName, IMap hasMap ] => [ ..., IBool true if cons does have fieldName ]
	 */
	adt_has_field {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			IConstructor cons = (IConstructor) stack[sp - 3];
			IString field = ((IString) stack[sp - 2]);
			String fieldName = field.getValue();
			Type consType = cons.getConstructorType();
			
			// Does fieldName exist as positional field?
			if(consType.hasField(fieldName)){
			    stack[sp - 3] = Rascal_TRUE;
			    return sp - 2;
			}
			
			// Check for keyword parameter
			String[] fieldNames = consType.getFieldNames();
			if(fieldNames == null){
			    fieldNames = new String[0];
			}
			IMap consFieldMap = (IMap) stack[sp - 1];
			IListWriter w = vf.listWriter();
			w.append(vf.string(cons.getName()));
			for(String fname : fieldNames){
			    w.append(vf.string(fname));
			}

			IList kwNames = (IList) consFieldMap.get(w.done());
			if(kwNames != null && kwNames.contains(field)){
			    stack[sp - 3] = Rascal_TRUE;
			    return sp - 2;
			}
			
			if(TreeAdapter.isTree(cons) && TreeAdapter.isAppl((ITree) cons)) {
			    IConstructor prod = ((ITree) cons).getProduction();

			    for(IValue elem : ProductionAdapter.getSymbols(prod)) {
			        IConstructor arg = (IConstructor) elem;
			        if (SymbolAdapter.isLabel(arg) && SymbolAdapter.getLabel(arg).equals(fieldName)) {
			            stack[sp - 3] = Rascal_TRUE;
			            return sp - 2;
			        }
			    }
			}
			if(cons.isAnnotatable()){
			    stack[sp - 3] = cons.asAnnotatable().getAnnotation(fieldName) == null ? Rascal_FALSE : Rascal_TRUE;
			} else {
			    stack[sp - 3] = Rascal_FALSE;
			}
			return sp - 2;
		}
	},
	
	/**
	 * Runtime check whether a node has a named field
	 * 
	 * [ ..., INode nd, IString fieldName ] => [ ..., IBool true if cons does have fieldName ]
	 */
	node_has_field {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			INode nd = (INode) arg_2;
			IString field = ((IString) arg_1);
			String fieldName = field.getValue();
			if ((nd.mayHaveKeywordParameters() && nd.asWithKeywordParameters().getParameter(fieldName) != null)){
				return Rascal_TRUE;
			} else {
				if(nd.isAnnotatable()){
					return nd.asAnnotatable().getAnnotation(fieldName) == null ? Rascal_FALSE : Rascal_TRUE;
				} else {
				   return Rascal_FALSE;
				}
			}
		}
	},
	
	/**
     * Retrieve value of named field of node
     * 
     * [ ..., INode nd, IString fieldName ] => [ ..., IValue value of field fieldName ]
     */
	node_field_access {
	    @Override
	    public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
	        INode nd = (INode) arg_2;
	        IString field = (IString) arg_1;
	        String fieldName = field.getValue();
	        // A default field that was set?

	        IValue v = null;
	        if(nd.mayHaveKeywordParameters()){
	            v = nd.asWithKeywordParameters().getParameter(fieldName);
	        }
	        if(v != null){
	            return v;
	        }

	        return rex.getFrameObserver().exception(currentFrame,  RascalRuntimeException.noSuchField(fieldName, currentFrame));
	    }
	},

	/**
	 * Retrieve value of named field of constructor
	 * 
	 * [ ..., IConstructor cons, IString fieldName ] => [ ..., IValue value of field fieldName ]
	 */
	adt_field_access {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			IConstructor cons = (IConstructor) stack[sp - 3];
			IString field = ((IString) stack[sp - 2]);
			String fieldName = field.getValue();
			Type tp = cons.getConstructorType();
			
			try {
				
				// A positional field?
				
				if(tp.hasField(fieldName)){	
					stack[sp - 3] = cons.get(fieldName);
					return sp - 2;
				} 
				
				// A default field that was set?
				
				IValue v = null;
				if(cons.mayHaveKeywordParameters()){
					v = cons.asWithKeywordParameters().getParameter(fieldName);
				}
				if(v != null){				
					stack[sp - 3] = v;
					return sp - 2;
				}
				
				// A default field that was not set but has a constant value?
				
				String consName = cons.getName();
				IMap constructorConstantDefaultExpressions = (IMap) stack[sp - 1];
				IMap constantFields = (IMap) constructorConstantDefaultExpressions.get(vf.string(consName));
				
				if(constantFields != null){
					IValue constantValue = constantFields.get(vf.string(fieldName));
					if(constantValue != null){
						stack[sp - 3] = constantValue;
						return sp - 2;
					}
				}
				
				// TODO jurgen rewrite to ITree API
				if(TreeAdapter.isTree(cons)){
					ITree tree = (ITree) cons;
					if(TreeAdapter.isAppl(tree)){
						IConstructor prod = tree.getProduction();
						IList prod_symbols = (IList) prod.get("symbols");
						int n = prod_symbols.length();
						IList appl_args = (IList) tree.get("args"); // TODO getArgs() gives UnsupportedOperation
						for(int i = 0; i < n; i++){
							IConstructor arg = (IConstructor) prod_symbols.get(i);
							if(arg.getConstructorType() == RascalValueFactory.Symbol_Label){
								if(((IString) arg.get(0)).equals(field)){
									stack[sp - 3] = appl_args.get(i);
									return sp - 2;
								}
							}
						}
					}
				}
				
				// Next resort: an unset default field with a computed value?
				
				Function getDefaults = rex.getCompanionDefaultsFunction(consName, tp);
				
				if(getDefaults != RVMCore.noCompanionFunction){
					IValue[] posArgs = new IValue[cons.arity()];
					for(int i = 0; i < cons.arity(); i++){
						posArgs[i] = cons.get(i);
					}

					Map<String, IValue> kwArgs = cons.asWithKeywordParameters().getParameters();

					@SuppressWarnings("unchecked")
					Map<String, Map.Entry<Type, IValue>> defaults = (Map<String, Map.Entry<Type, IValue>>) rex.getRVM().executeRVMFunction(getDefaults, posArgs, kwArgs);
					Entry<Type, IValue> def = defaults.get(fieldName);
					if(def != null){
						stack[sp - 3] = def.getValue();
						return sp - 2;
					}
				}
				
				// Final resort: an unset common data field with a computed value?
				
				Function getFieldDefault = rex.getCompanionFieldDefaultFunction(tp.getAbstractDataType(), fieldName);
				
				if(getFieldDefault !=  RVMCore.noCompanionFunction){
					IValue[] posArgs = new IValue[0];

					Map<String, IValue> kwArgs = cons.asWithKeywordParameters().getParameters();

					IValue defaultValue = (IValue) rex.getRVM().executeRVMFunction(getFieldDefault, posArgs, kwArgs);
				
					stack[sp - 3] = defaultValue;
					return sp - 2;
				}
				
				rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.noSuchField(fieldName, currentFrame));
			} catch(FactTypeUseException e) {
			    rex.getFrameObserver().exception(currentFrame,  RascalRuntimeException.noSuchField(fieldName, currentFrame));
			}
			return sp - 2;
		}
	},
	
	/**
	 * Is a named field of a constructor defined? Returns false when:
	 * - constructor does not have the field
	 * - the field is a default field with unset value.
	 * 
	 * [ ..., IConstructor cons, IString fieldName ] => [ ..., bool ]
	 */
	is_defined_adt_field_access_get {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IConstructor cons = (IConstructor) arg_2;
			IString field = ((IString) arg_1);
			String fieldName = field.getValue();
			Type tp = cons.getConstructorType();
			try {
				
				// A positional field?
				
				if(tp.hasField(fieldName)){	
					return new Object[] { Rascal_TRUE, cons.get(tp.getFieldIndex(fieldName)) };
				} 
				
				// A default field that was set?
				
				IValue v = null;
				if(cons.mayHaveKeywordParameters()){
					v = cons.asWithKeywordParameters().getParameter(fieldName);
				}
				if(v != null){
					return new Object[] { Rascal_TRUE, v };
				}
				
				// TODO jurgen rewrite to ITree API
				if(TreeAdapter.isTree(cons)){
					ITree tree = (ITree) cons;
					if(TreeAdapter.isAppl(tree)){
						IConstructor prod = tree.getProduction();
						IList prod_symbols = (IList) prod.get("symbols");
						int n = prod_symbols.length();
						IList appl_args = (IList) tree.get("args"); // TODO getArgs() gives UnsupportedOperation
						for(int i = 0; i < n; i++){
							IConstructor arg = (IConstructor) prod_symbols.get(i);
							if(arg.getConstructorType() == RascalValueFactory.Symbol_Label){
								if(((IString) arg.get(0)).equals(field)){
									return new Object[] {  Rascal_TRUE, appl_args.get(i) };
								}
							}
						}
					}
				}
				
			// Final resort: an unset default field: fall through and return false
				
			} catch(FactTypeUseException e) {
				
			}
			return new Object[] { Rascal_FALSE, null };
		}
	},
	
	/**
	 * Is a named field of a node defined? Returns true when:
	 * - the field is a default field with set value.
	 * 
	 * [ ..., INode nd, IString fieldName ] => [ ..., bool ]
	 */
	is_defined_node_field_access_get {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			INode nd = (INode) arg_2;
			IString field = ((IString) arg_1);
			String fieldName = field.getValue();
			
			try {
				
				// A default field that was set?
				
				IValue v = null;
				if(nd.mayHaveKeywordParameters()){
					v = nd.asWithKeywordParameters().getParameter(fieldName);
				}
				if(v != null){
					return new Object[] { Rascal_TRUE, v };
				}
				
			// Final resort: an unset default field: fall through and return false
				
			} catch(FactTypeUseException e) {
				
			}
			return new Object[] { Rascal_FALSE, null };
		}
	},
	
	/**
	 * Is a named field of a location defined?
	 * 
	 * [ ..., ISourceLocation nd, IString fieldName ] => [ ..., bool ]
	 */
//	is_defined_loc_field_access_get {
//		@Override
//		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
//
//			try {
//				temp_array_of_2[0] = Rascal_TRUE;
//				temp_array_of_2[1] = loc_field_access.execute2(arg_2, arg_1, currentFrame, rex);;
//				return temp_array_of_2;
//
//			} catch(Exception e) {
//				temp_array_of_2[0] = Rascal_FALSE;
//			}
//			return temp_array_of_2;
//		}
//	},
	
	/**
	 * Retrieve value of named field of datetime value
	 * 
	 * [ ..., IDateTime dt, IString fieldName ] => [ ..., IValue value of field fieldName ]
	 */
	datetime_field_access {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IDateTime dt = ((IDateTime) arg_2);
			String field = ((IString) arg_1).getValue();
			IValue v;
			try {
				switch (field) {
				case "isDate":
					v = vf.bool(dt.isDate());
					break;
				case "isTime":
					v = vf.bool(dt.isTime());
					break;
				case "isDateTime":
					v = vf.bool(dt.isDateTime());
					break;
				case "century":
					if (!dt.isTime()) {
						v = vf.integer(dt.getCentury());
						break;
					}
					return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.unavailableInformation("Can not retrieve the century on a time value", currentFrame));
				case "year":
					if (!dt.isTime()) {
						v = vf.integer(dt.getYear());
						break;
					}
					return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.unavailableInformation("Can not retrieve the year on a time value", currentFrame));

				case "month":
					if (!dt.isTime()) {
						v = vf.integer(dt.getMonthOfYear());
						break;
					}
					return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.unavailableInformation("Can not retrieve the month on a time value", currentFrame));
				case "day":
					if (!dt.isTime()) {
						v = vf.integer(dt.getDayOfMonth());
						break;
					}
					return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.unavailableInformation("Can not retrieve the day on a time value", currentFrame));
				case "hour":
					if (!dt.isDate()) {
						v = vf.integer(dt.getHourOfDay());
						break;
					}
					return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.unavailableInformation("Can not retrieve the hour on a date value", currentFrame));
				case "minute":
					if (!dt.isDate()) {
						v = vf.integer(dt.getMinuteOfHour());
						break;
					}
					return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.unavailableInformation("Can not retrieve the minute on a date value", currentFrame));
				case "second":
					if (!dt.isDate()) {
						v = vf.integer(dt.getSecondOfMinute());
						break;
					}
					return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.unavailableInformation("Can not retrieve the second on a date value", currentFrame));
				case "millisecond":
					if (!dt.isDate()) {
						v = vf.integer(dt.getMillisecondsOfSecond());
						break;
					}
					return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.unavailableInformation("Can not retrieve the millisecond on a date value", currentFrame));
				case "timezoneOffsetHours":
					if (!dt.isDate()) {
						v = vf.integer(dt.getTimezoneOffsetHours());
						break;
					}
					return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.unavailableInformation("Can not retrieve the timezone offset hours on a date value", currentFrame));
				case "timezoneOffsetMinutes":
					if (!dt.isDate()) {
						v = vf.integer(dt.getTimezoneOffsetMinutes());
						break;
					}
					return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.unavailableInformation("Can not retrieve the timezone offset minutes on a date value", currentFrame));

				case "justDate":
					if (!dt.isTime()) {
						v = vf.date(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
						break;
					}
					return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.unavailableInformation("Can not retrieve the date component of a time value", currentFrame));
				case "justTime":
					if (!dt.isDate()) {
						v = vf.time(dt.getHourOfDay(), dt.getMinuteOfHour(), dt.getSecondOfMinute(), 
								dt.getMillisecondsOfSecond(), dt.getTimezoneOffsetHours(),
								dt.getTimezoneOffsetMinutes());
						break;
					}
					return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.unavailableInformation("Can not retrieve the time component of a date value", currentFrame));
				default:
				  return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.noSuchField(field, currentFrame));
				}
				return v;

			} catch (InvalidDateTimeException e) {
			  return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidArgument(dt, currentFrame, e.getMessage()));
			}
		}
	},

	/**
	 * Update value of named field of datetime value
	 * 
	 * [ ..., IDateTime dt, IString fieldName, IValue repl ] => [ ...,  new IDateTime value with updated value for field fieldName ]
	 */
	datetime_field_update {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			IDateTime dt = ((IDateTime) stack[sp - 3]);
			String field = ((IString) stack[sp - 2]).getValue();
			IValue repl = (IValue) stack[sp - 1];

			// Individual fields
			int year = dt.getYear();
			int month = dt.getMonthOfYear();
			int day = dt.getDayOfMonth();
			int hour = dt.getHourOfDay();
			int minute = dt.getMinuteOfHour();
			int second = dt.getSecondOfMinute();
			int milli = dt.getMillisecondsOfSecond();
			int tzOffsetHour = dt.getTimezoneOffsetHours();
			int tzOffsetMin = dt.getTimezoneOffsetMinutes();

			try {
				switch (field) {

				case "year":
					if (dt.isTime()) {
					  rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidUseOfTime("Can not update the year on a time value", currentFrame));
					}
					year = ((IInteger)repl).intValue();
					break;

				case "month":
					if (dt.isTime()) {
					  rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidUseOfTime("Can not update the month on a time value", currentFrame));
					}
					month = ((IInteger)repl).intValue();
					break;

				case "day":
					if (dt.isTime()) {
					  rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidUseOfTime("Can not update the day on a time value", currentFrame));
					}	
					day = ((IInteger)repl).intValue();
					break;

				case "hour":
					if (dt.isDate()) {
					  rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidUseOfDate("Can not update the hour on a date value", currentFrame));
					}	
					hour = ((IInteger)repl).intValue();
					break;

				case "minute":
					if (dt.isDate()) {
					  rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidUseOfDate("Can not update the minute on a date value", currentFrame));
					}
					minute = ((IInteger)repl).intValue();
					break;

				case "second":
					if (dt.isDate()) {
					  rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidUseOfDate("Can not update the second on a date value", currentFrame));
					}
					second = ((IInteger)repl).intValue();
					break;

				case "millisecond":
					if (dt.isDate()) {
					  rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidUseOfDate("Can not update the millisecond on a date value", currentFrame));
					}
					milli = ((IInteger)repl).intValue();
					break;

				case "timezoneOffsetHours":
					if (dt.isDate()) {
					  rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidUseOfDate("Can not update the timezone offset hours on a date value", currentFrame));
					}
					tzOffsetHour = ((IInteger)repl).intValue();
					break;

				case "timezoneOffsetMinutes":
					if (dt.isDate()) {
					  rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidUseOfDate("Can not update the timezone offset minutes on a date value", currentFrame));
					}
					tzOffsetMin = ((IInteger)repl).intValue();
					break;			

				default:
				  rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.noSuchField(field, currentFrame));
				}
				IDateTime newdt = null;
				if (dt.isDate()) {
					newdt = vf.date(year, month, day);
				} else if (dt.isTime()) {
					newdt = vf.time(hour, minute, second, milli, tzOffsetHour, tzOffsetMin);
				} else {
					newdt = vf.datetime(year, month, day, hour, minute, second, milli, tzOffsetHour, tzOffsetMin);
				}

				stack[sp - 3] = newdt;
				return sp - 2;
			}
			catch (IllegalArgumentException e) {
			    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidArgument(repl, currentFrame, "Cannot update field " + field + ", this would generate an invalid datetime value"));
			}
			catch (InvalidDateTimeException e) {
			  rex.getFrameObserver().exception(currentFrame,  RascalRuntimeException.invalidArgument(dt, currentFrame, e.getMessage()));
			}
			return sp - 2;
		}
	},

	/**
	 * Retrieve value of named field of loc value
	 * 
	 * [ ..., ISourceLocation sloc, IString fieldName ] => [ ...,  IValue value of field fieldName ]
	 */
	loc_field_access {
		@SuppressWarnings("deprecation")
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISourceLocation sloc = ((ISourceLocation) arg_2);
			String field = ((IString) arg_1).getValue();
			IValue v;
			switch (field) {

			case "scheme":
				String s = sloc.getScheme();
				v = vf.string(s == null ? "" : s);
				break;

			case "authority":
				v = vf.string(sloc.hasAuthority() ? sloc.getAuthority() : "");
				break;

			case "host":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				  return RascalRuntimeException.noSuchField("The scheme " + sloc.getScheme() + " does not support the host field, use authority instead.", currentFrame);
				}
				s = sloc.getURI().getHost();
				v = vf.string(s == null ? "" : s);
				break;

			case "path":
				v = vf.string(sloc.hasPath() ? sloc.getPath() : "/");
				break;

			case "parent":
				String path = sloc.getPath();
				if (path.equals("") || path.equals("/")) {
				  return RascalRuntimeException.noParent(sloc, currentFrame);
				}
				int i = path.lastIndexOf("/");

				if (i != -1) {
					path = path.substring(0, i);
					if (sloc.getScheme().equalsIgnoreCase("file")) {
						// there is a special case for file references to windows paths.
						// the root path should end with a / (c:/ not c:)
						if (path.lastIndexOf((int)'/') == 0 && path.endsWith(":")) {
							path += "/";
						}
					}
					v = $loc_field_update(sloc, "path", vf.string(path), currentFrame, rex);
				} else {
				  return RascalRuntimeException.noParent(sloc, currentFrame);
				}
				break;	

			case "file": 
				path = sloc.hasPath() ? sloc.getPath() : "";

				i = path.lastIndexOf((int)'/');

				if (i != -1) {
					path = path.substring(i+1);
				}
				v = vf.string(path);	
				break;

			case "ls":
			    ISourceLocation resolved = sloc;
			    if(URIResolverRegistry.getInstance().exists(resolved) && URIResolverRegistry.getInstance().isDirectory(resolved)){
					IListWriter w = vf.listWriter();

					try {
                        for (ISourceLocation elem : URIResolverRegistry.getInstance().list(resolved)) {
                        	w.append(elem);
                        }
                    }
                    catch (FactTypeUseException | IOException e) {
                        return RascalRuntimeException.io(vf.string(e.getMessage()), currentFrame);
                    }

					v = w.done();
					break;
				} else {
				  return RascalRuntimeException.io(vf.string("You can only access ls on a directory, or a container."), currentFrame);
				}

			case "extension":
				path = sloc.hasPath() ? sloc.getPath() : "";
				i = path.lastIndexOf('.');
				if (i != -1) {
					v = vf.string(path.substring(i + 1));
				} else {
					v = vf.string("");
				}
				break;

			case "fragment":
				v = vf.string(sloc.hasFragment() ? sloc.getFragment() : "");
				break;

			case "query":
				v = vf.string(sloc.hasQuery() ? sloc.getQuery() : "");
				break;

			case "params":
				String query = sloc.hasQuery() ? sloc.getQuery() : "";
				IMapWriter res = vf.mapWriter(tf.stringType(), tf.stringType());

				if (query.length() > 0) {
					String[] params = query.split("&");
					for (String param : params) {
						String[] keyValue = param.split("=");
						res.put(vf.string(keyValue[0]), vf.string(keyValue[1]));
					}
				}
				v = res.done();
				break;

			case "user":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				  return RascalRuntimeException.noSuchField("The scheme " + sloc.getScheme() + " does not support the user field, use authority instead.", currentFrame);
				}
				s = sloc.getURI().getUserInfo();
				v = vf.string(s == null ? "" : s);
				break;

			case "port":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				  return RascalRuntimeException.noSuchField("The scheme " + sloc.getScheme() + " does not support the port field, use authority instead.", currentFrame);
				}
				int n = sloc.getURI().getPort();
				v = vf.integer(n);
				break;	

			case "length":
				if(sloc.hasOffsetLength()){
					v = vf.integer(sloc.getLength());
					break;
				} else {
				  return RascalRuntimeException.unavailableInformation("length", currentFrame);
				}

			case "offset":
				if(sloc.hasOffsetLength()){
					v = vf.integer(sloc.getOffset());
					break;
				} else {
				  return RascalRuntimeException.unavailableInformation("offset", currentFrame);
				}

			case "begin":
				if(sloc.hasLineColumn()){
					v = vf.tuple(vf.integer(sloc.getBeginLine()), vf.integer(sloc.getBeginColumn()));
					break;
				} else {
				  return RascalRuntimeException.unavailableInformation("begin", currentFrame);
				}
			case "end":
				if(sloc.hasLineColumn()){
					v = vf.tuple(vf.integer(sloc.getEndLine()), vf.integer(sloc.getEndColumn()));
					break;
				} else {
				  return RascalRuntimeException.unavailableInformation("end", currentFrame);
				}

			case "uri":
				v = vf.string(sloc.getURI().toString());
				break;

			case "top":
				v = sloc.top();
				break;

			default:
			    return RascalRuntimeException.noSuchField(field, currentFrame);
			}

			return v;
		}
	},
	/**
	 * Is a named field of a location defined? Returns
	 * - <true, field value> when the field exists and its value is defined
	 * - false, _> otherwise
	 * 
	 * [ ..., INode nd, IString fieldName ] => [ ..., [bool, value] ]
	 */
	
	is_defined_loc_field_access_get {
	    @Override
	    public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
	        Object result = loc_field_access.execute2(arg_2, arg_1, currentFrame, rex);
	        return (result instanceof Thrown) ? new Object[] { Rascal_FALSE, null }
	                                          : new Object[] { Rascal_TRUE, result };
	    }
	},

	/**
	 * Update value of named field of loc value
	 * 
	 * [ ..., ISourceLocation sloc, IString fieldName, IValue repl ] => [ ...,  new ISourceLocation value with updated value for field fieldName ]
	 */
	loc_field_update {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			ISourceLocation sloc = ((ISourceLocation) stack[sp - 3]);
			String field = ((IString) stack[sp - 2]).getValue();
			IValue repl = (IValue) stack[sp - 1];
			stack[sp - 3] = $loc_field_update(sloc, field, repl, currentFrame, rex);
			return sp - 2;
		}
	},

	/**
	 * retrieve value of named field of lrel value
	 * 
	 * [ ..., IListRelation sloc, IString fieldName ] => [ ...,  IValue value for field fieldName ]
	 */
	lrel_field_access {
		@SuppressWarnings("deprecation")
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IListRelation<IList> left = ((IList) arg_2).asRelation();
			return left.projectByFieldNames(((IString) arg_1).getValue());
		}
	},

	/**
	 * retrieve value of named field of rel value
	 * 
	 * [ ..., IRelation sloc, IString fieldName ] => [ ...,  IValue value for field fieldName ]
	 */
	rel_field_access {
		@SuppressWarnings("deprecation")
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISetRelation<ISet> left = ((ISet) arg_2).asRelation();
			return left.projectByFieldNames(((IString) arg_1).getValue());
		}
	},

	// TODO document this

	reified_field_access {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IConstructor reified = (IConstructor) arg_2;
			String field = ((IString) arg_1).getValue();
			return reified.get(field);
		}
	},

	/**
	 * retrieve value of named field of nonterminal value
	 * 
	 * [ ..., IConstructor appl, IString fieldName ] => [ ...,  IValue value for field fieldName ]
	 */
	nonterminal_field_access {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {

			ITree appl = (ITree) arg_2;
			IString field = ((IString) arg_1);
			IList appl_args = (IList) appl.get("args");	// TODO getArgs() gives UnsupportedOperation

			// Note: the "args" fields is used to access the arguments of concrete lists
			// So far, there has been no need to support other fields but more fields might
			// be added here.
			if(field.getValue().equals("args")){
				return appl_args;
			}
			IConstructor prod = appl.getProduction();
			IList prod_symbols = (IList) prod.get("symbols");
			int n = prod_symbols.length();

			for(int i = 0; i < n; i++){
				IConstructor arg = (IConstructor) prod_symbols.get(i);
				if(arg.getConstructorType() == RascalValueFactory.Symbol_Conditional){
					arg = (IConstructor) arg.get(0);
				}

				if(arg.getConstructorType() == RascalValueFactory.Symbol_Label){
					if(((IString) arg.get(0)).equals(field)){
						return appl_args.get(i);
					}
				}
			}

			return  RascalRuntimeException.noSuchField(field.getValue(), currentFrame);
		}
	},

	/**
	 * Update value of named field of nonterminal value
	 * 
	 * [ ..., IConstructor appl, IString fieldName, IValue repl ] => [ ...,  new IConstructor value with updated value for field fieldName  ]
	 */
	nonterminal_field_update {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;

			ITree appl = (ITree) stack[sp - 3];
			IString field = ((IString) stack[sp - 2]);
			ITree repl = (ITree) stack[sp - 1];
			IList appl_args = (IList) appl.get("args");	// TODO getArgs() gives UnsupportedOperation

			//			// Note: the "args" fields is used to access the arguments of concrete lists
			//			// So far, there has been no need to support other fields but more fields might
			//			// be added here.
			//			if(field.getValue().equals("args")){
			//				return appl_args;
			//				return sp - 1;
			//			}
			IConstructor prod = appl.getProduction();
			IList prod_symbols = (IList) prod.get("symbols");
			int n = prod_symbols.length();

			for(int i = 0; i < n; i++){
				IConstructor arg = (IConstructor) prod_symbols.get(i);
				if(arg.getConstructorType() == RascalValueFactory.Symbol_Conditional){
					arg = (IConstructor) arg.get(0);
				}

				if(arg.getConstructorType() == RascalValueFactory.Symbol_Label){
					if(((IString) arg.get(0)).equals(field)){
						appl_args = appl_args.put(i, repl);
						stack[sp - 3] = vf.constructor(RascalValueFactory.Tree_Appl, prod, appl_args);
						return sp - 2;
					}
				}
			}

			rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.noSuchField(field.getValue(), currentFrame));
			return sp - 2;
		}
	},

	/**
	 * Run-time check that nonterminal value has a given named field.
	 * 
	 * [ ..., IConstructor appl, IString fieldName ] => [ ...,  IBool true if named field is present ]
	 */
	nonterminal_has_field {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ITree appl = (ITree) arg_2;
			IString field = ((IString) arg_1);
			IConstructor prod = appl.getProduction();
			IList prod_symbols = (IList) prod.get("symbols");
			int n = prod_symbols.length();

			for(int i = 0; i < n; i++){
				IConstructor arg = (IConstructor) prod_symbols.get(i);
				if(arg.getConstructorType() == RascalValueFactory.Symbol_Conditional){
					arg = (IConstructor) arg.get(0);
				}
				if(arg.getConstructorType() == RascalValueFactory.Symbol_Label){
					if(((IString) arg.get(0)).equals(field)){
						return Rascal_TRUE;
					}
				}
			}
			return Rascal_FALSE;
		}
	},
	
	/**
	 * Get value of a named field of a tuple
	 * 
	 * [ ..., ITuple tup, IString fieldName ] => [ ...,  IValue value of field fieldName  ]
	 */
	tuple_field_access {
		@SuppressWarnings("deprecation")
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((ITuple) arg_2).get(((IString) arg_1).getValue());
		}
	},

	/**
	 * Set value of a named field of a tuple
	 * 
	 * [ ..., ITuple tup, IString fieldName, IValue repl ] => [ ...,  new ITuple with field fieldName set to repl ]
	 */

	tuple_field_update {
		@SuppressWarnings("deprecation")
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			stack[sp - 3] = ((ITuple) stack[sp - 3]).set(((IString) stack[sp - 2]).getValue(), (IValue) stack[sp - 1]);
			return sp - 2;
		}
	},

	/**
	 * Get projection of tuple elements by field name or index
	 * 
	 * [ ..., ITuple tup, IValue nameOrIndex1, IValue  nameOrIndex2, ... ] => [ ...,  new ITuple containing the projected elements ]
	 */
	tuple_field_project {
		@SuppressWarnings("deprecation")
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity >= 2;
			ITuple tup = (ITuple) stack[sp - arity];
			IValue [] newFields = new IValue[arity - 1];
			for(int i = 0; i < arity - 1; i++){
				IValue field = (IValue) stack[sp - arity + 1 + i];
				newFields[i] = field.getType().isInteger() ? tup.get(((IInteger) field).intValue())
						: tup.get(((IString) field).getValue());
			}
			stack[sp - arity] = (arity - 1 > 1) ? vf.tuple(newFields) : newFields[0];
			return sp - arity + 1;
		}
	},
	
	 /**
     * Set named field of node value
     * 
     * [ ..., INode nd, IString fieldName, IValue repl... ] => [ ...,  new INode with named field set to repl ]
     */
    node_field_update {
        @Override
        public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
            assert arity == 3;
            INode nd = (INode) stack[sp - 3];
            IString field = ((IString) stack[sp - 2]);
            String fieldName = field.getValue();
            IValue repl = (IValue) stack[sp - 1];
            if(nd.mayHaveKeywordParameters()){
                stack[sp - 3] = nd.asWithKeywordParameters().setParameter(fieldName, repl);
                return sp - 2;
            } else {
                rex.getFrameObserver().exception(currentFrame, 
                    RascalRuntimeException.noSuchField(fieldName, currentFrame));
            }
            return sp - 2;
        }
	},

	/**
	 * Set named field of constructor value
	 * 
	 * [ ..., IConstructor cons, IString fieldName, IValue repl... ] => [ ...,  new IConstructor with named field set to repl ]
	 */
	adt_field_update {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			IConstructor cons = (IConstructor) stack[sp - 3];
			IString field = ((IString) stack[sp - 2]);
			String fieldName = field.getValue();
			IValue repl = (IValue) stack[sp - 1];
			
			Type tp = cons.getConstructorType();
			try {
				//stack[sp - 3] = ((IConstructor) stack[sp - 3]).set(((IString) stack[sp - 2]).getValue(), (IValue) stack[sp -1]);
				if(tp.hasField(fieldName)){	// A positional field
					int fld_index = tp.getFieldIndex(fieldName);
					stack[sp - 3] = cons.set(fld_index, repl);
					return sp - 2;
				} 

				if(cons.mayHaveKeywordParameters()){
					stack[sp - 3] = cons.asWithKeywordParameters().setParameter(fieldName, repl);
					return sp - 2;
				}
				
				rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.notImplemented("Assignment to parse tree field not yet implemented", currentFrame.src, currentFrame));
				
			} catch(FactTypeUseException e) {
			    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.noSuchField(((IString) stack[sp - 2]).getValue(), currentFrame));
			}
			return sp - 2;
		}
	},

	/**
	 * Get projection of a relation consisting of tuple elements projected by field name or index
	 * 
	 * [ ..., IRelation rel, IValue nameOrIndex1, IValue  nameOrIndex2, ... ] => [ ...,  new IRelation containing the projected elements ]
	 */
	rel_field_project {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity >= 2;
			int[] fields = new int[arity - 1];
			for(int i = 1; i < arity; i++){
				fields[i - 1] = ((IInteger)stack[sp - arity + i]).intValue();
			}
			stack[sp - arity] = ((ISet) stack[sp - arity]).asRelation().project(fields);
			return sp - arity + 1;
		}
	},

	/**
	 * Get projection of a list relation consisting of tuple elements projected by field name or index
	 * 
	 * [ ..., IListRelation lrel, IValue nameOrIndex1, IValue  nameOrIndex2, ... ] => [ ...,  new IListRelation containing the projected elements ]
	 */
	lrel_field_project {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity >= 2;
			IList lrel = (IList) stack[sp - arity];
			int indexArity = arity - 1;
			int[] fields = new int[arity - 1];
			for(int i = 1; i < arity; i++){
				fields[i - 1] = ((IInteger)stack[sp - arity + i]).intValue();
			}
			IListWriter w = vf.listWriter();
			IValue[] elems = new IValue[arity - 1];
			for(IValue vtup : lrel){
				ITuple tup = (ITuple) vtup;
				for(int j = 0; j < fields.length; j++){
					elems[j] = tup.get(fields[j]);
				}
				w.append((indexArity > 1) ? vf.tuple(elems) : elems[0]);
			}
			stack[sp - arity] = w.done();
			return sp - arity + 1;
		}
	},

	/**
	 * Get projection of a map with elements projected by field name or index
	 * 
	 * [ ..., IMap map, IValue nameOrIndex1, IValue  nameOrIndex2, ... ] => [ ...,  new IMap containing the projected elements ]
	 */
	map_field_project {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity >= 2;
			IMap map = (IMap) stack[sp - arity];
			int indexArity = arity - 1;
			int[] fields = new int[indexArity];
			for(int i = 1; i < arity; i++){
				fields[i - 1] = ((IInteger)stack[sp - arity + i]).intValue();
			}
			ISetWriter w = vf.setWriter();
			IValue[] elems = new IValue[indexArity];
			Iterator<Entry<IValue,IValue>> iter = map.entryIterator();
			while (iter.hasNext()) {
				Entry<IValue,IValue> entry = iter.next();
				for(int j = 0; j < fields.length; j++){
					elems[j] = fields[j] == 0 ? entry.getKey() : entry.getValue();
				}
				w.insert((indexArity > 1) ? vf.tuple(elems) : elems[0]);
			}
			stack[sp - arity] = w.done();
			return sp - arity + 1;
		}	
	},


	/************************************************************************************************/
	/*								Various getters													*/
	/************************************************************************************************/
	
	/**
	 * Get the non-layout arguments of a nonterminal value
	 * 
	 * [ ... ITree val1] => [ ..., IList args ]
	 */

	get_nonlayout_args {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return TreeAdapter.getNonLayoutArgs((ITree)arg_1);
		}	
	},

	/**
	 * Get the arguments of a nonterminal value
	 * 
	 * [ ... ITree val1] => [ ..., IList args ]
	 */
	get_appl_args {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return TreeAdapter.getArgs((ITree)arg_1);
		}	
	},

	/**
	 * Get the alternmatives of an amb value
	 * 
	 * [ ... ITree val1] => [ ..., IList alternatives ]
	 */
	get_amb_alternatives {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return TreeAdapter.getAlternatives((ITree)arg_1);
		}	
	},

	/**
	 * Get the non-layout elements of a concrete list
	 * 
	 * [ ... ITree val1] => [ ..., IList elements ]
	 */
	get_concrete_list_elements {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ITree treeSubject = (ITree) arg_1;
			if (!(TreeAdapter.isList(treeSubject) || TreeAdapter.isOpt(treeSubject))) {			// Fixes TreeAdapter.getListASTArgs for the case of lexical list in concrete context
				throw new ImplementationError(
						"This is not a context-free list production: " + treeSubject);
			}
			IList children = TreeAdapter.getArgs(treeSubject);
			IListWriter writer = ValueFactoryFactory.getValueFactory().listWriter();

			IConstructor symbol = TreeAdapter.getType(treeSubject);
			boolean layoutPresent = false;
			if(children.length() > 1){
				ITree child1 = (ITree)children.get(1);
				if(TreeAdapter.isLayout(child1)){
					layoutPresent = true;
				}
			}
			int delta = layoutPresent ? 2 : 1;

			if(SymbolAdapter.isIterPlusSeps(symbol) || SymbolAdapter.isIterStarSeps(symbol)){
				IList separators = SymbolAdapter.getSeparators(symbol);
				boolean nonLayoutSeparator = false;
				for(IValue sep : separators){
					if(!((IConstructor) sep).getName().equals("layouts")){
						nonLayoutSeparator = true;
						break;
					}
				}
				delta = nonLayoutSeparator && layoutPresent ? 4 : 2;
			}

			for (int i = 0; i < children.length();) {
				IValue kid = children.get(i);
				writer.append(kid);
				// skip layout and/or separators
				i += delta;
			}
			return writer.done();
		}	
	},

	/**
	 * Strip a conditional from a lexical node
	 * 
	 * [ ... IConstructor val1] => [ ..., IConstructor val2 ]
	 */
	strip_lexical {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IConstructor lexSubject = (IConstructor) arg_1;
			if(lexSubject.getName().equals("conditional")){
				lexSubject = (IConstructor) lexSubject.get("symbol");
			}

			return lexSubject;
		}	
	},

	/**
	 * Get the type of a nonterminal value as Symbol
	 * 
	 * [ ... ITree val1] => [ ..., IConstructor type ]
	 */
	get_tree_type_as_symbol {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return TreeAdapter.getType((ITree)arg_1);
		}	
	},

	/**
	 * Get the type of a nonterminal value as Type
	 * 
	 * [ ... ITree val1] => [ ..., IConstructor type ]
	 */
	get_tree_type_as_type {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IConstructor symbol = TreeAdapter.getType((ITree)arg_1);
			String typeName = ((IString)symbol.get(0)).getValue();
			return vf.constructor(RascalValueFactory.Symbol_Sort, vf.string(typeName));
		}	
	},

	/************************************************************************************************/
	/*					Slices																		*/
	/************************************************************************************************/

	/**
	 * Replace a list slice
	 * [ ... IList lst, IInteger from, IInteger by, IInteger to, IList repl ] => [ ..., new IList lst with slice elements replaced by elements of repl ]
	 */
	list_slice_replace {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			return $list_slice_operator(stack, sp, arity, SliceOperator.replace(), currentFrame, rex);
		}
	},

	/**
	 * Add value to a list slice
	 * [ ... IList lst, IInteger from, IInteger by, IInteger to, IValue val ] => [ ..., new IList lst with val added to slice elements ]
	 */
	list_slice_add {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			return $list_slice_operator(stack, sp, arity, SliceOperator.add(), currentFrame, rex);
		}
	},

	/**
	 * Subtract value from a list slice
	 * [ ... IList lst, IInteger from, IInteger by, IInteger to, IValue val ] => [ ..., new IList lst with val subtracted from slice elements ]
	 */
	list_slice_subtract {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			return $list_slice_operator(stack, sp, arity, SliceOperator.subtract(), currentFrame, rex);
		}
	},

	/**
	 * Multiply elements of a list slice by a value
	 * [ ... IList lst, IInteger from, IInteger by, IInteger to, IValue val ] => [ ..., new IList lst with slice elements multiplied by val ]
	 */
	list_slice_product {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			return $list_slice_operator(stack, sp, arity, SliceOperator.product(), currentFrame, rex);
		}
	},

	/**
	 * Divide elements of a list slice by a value
	 * [ ... IList lst, IInteger from, IInteger by, IInteger to, IValue val ] => [ ..., new IList lst with slice elements divided by val ]
	 */
	list_slice_divide {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			return $list_slice_operator(stack, sp, arity, SliceOperator.divide(), currentFrame, rex);
		}
	},

	/**
	 * Intersect elements of a list slice
	 * [ ... IList lst, IInteger from, IInteger by, IInteger to, IValue val ] => [ ..., new IList lst with slice elements intersected with val ]
	 */
	list_slice_intersect {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			return $list_slice_operator(stack, sp, arity, SliceOperator.intersect(), currentFrame, rex);
		}
	},

	/**
	 * Replace string slice by a value
	 * [ ... IString s, IInteger from, IInteger by, IInteger to, IString val ] => [ ..., new IString s with slice elements replaced by val ]
	 */
	str_slice_replace {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 5;
			IString str = (IString) stack[sp - 5];
			SliceDescriptor sd = $makeSliceDescriptor($getInt((IValue) stack[sp - 4]), $getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), str.length());
			IString repl = (IString) stack[sp - 1];
			stack[sp - 5] = str.replace(sd.first, sd.second, sd.end, repl);
			return sp - 4;
		}
	},

	/**
	 * Replace elements in node slice
	 * [ ... IString s, IInteger from, IInteger by, IInteger to, IList val ] => [ ..., new INode s with slice elements replaced by elements in val ]
	 */
	node_slice_replace {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 5;
			INode node = (INode) stack[sp - 5];
			int nd_arity = node.arity();
			SliceDescriptor sd = $makeSliceDescriptor($getInt((IValue) stack[sp - 4]), $getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), nd_arity);
			IList repl = (IList) stack[sp - 1];
			stack[sp - 5] = node.replace(sd.first, sd.second, sd.end, repl);
			return sp - 4;
		}
	},

	/**
	 * Create list slice
	 * [ ... IList lst, IInteger from, IInteger by, IInteger to] => [ ..., new IList with slice elements ]
	 */
	list_slice {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 4;

			IList lst = (IList) stack[sp - 4];
			stack[sp - 4] = $makeSlice(lst, $makeSliceDescriptor($getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), $getInt((IValue) stack[sp - 1]), lst.length()));
			return sp - 3;
		}
	},

	/**
	 * Create str slice
	 * [ ... IString s, IInteger from, IInteger by, IInteger to] => [ ..., new IString with slice elements ]
	 */
	str_slice {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 4;

			IString str = (IString) stack[sp - 4];
			stack[sp - 4] = $makeSlice(str, $makeSliceDescriptor($getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), $getInt((IValue) stack[sp - 1]), str.length()));
			return sp - 3;
		}
	},

	/**
	 * Create node slice
	 * [ ... INode node, IInteger from, IInteger by, IInteger to] => [ ..., new INode with slice elements as args ]
	 */
	node_slice {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 4;

			INode node = (INode) stack[sp - 4];
			int nd_arity = node.arity();
			stack[sp - 4] = $makeSlice(node, $makeSliceDescriptor($getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), $getInt((IValue) stack[sp - 1]), nd_arity));
			return sp - 3;
		}
	},
	
	/**
	 * Create concrete list slice
	 * [ ... ITree tree, IInteger from, IInteger by, IInteger to] => [ ..., new INode with slice elements as args ]
	 */
	concrete_list_slice {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 5;

			ITree tree = (ITree) stack[sp - 5];
			IList elems = TreeAdapter.getArgs(tree);
			int len = elems.length();
			int sep_count = TreeAdapter.getSeparatorCount(tree);
			int list_length = (len == 0) ? 0 : ((sep_count == 0 ? len : 1 + len/(1 + sep_count)));
			int min_length =  $getInt((IValue) stack[sp - 1]);
			stack[sp - 5] = $makeSlice(tree, sep_count, min_length, $makeSliceDescriptor($getInt((IValue) stack[sp - 4]), $getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), list_length));
			if(stack[sp - 5] == null){
			    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidArgument(tree, currentFrame, "sliced value should have length of at least " + min_length));
			}
			return sp - 4;
		}
	},
	
	/**
	 * Create nonterminal slice
	 * [ ... ITree tree, IInteger from, IInteger by, IInteger to] => [ ..., new INode with slice elements as args ]
	 */
	nonterminal_slice {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 4;

			ITree tree = (ITree) stack[sp - 4];
			int tree_arity = TreeAdapter.getArgs(tree).length();
			stack[sp - 4] = $makeSlice(tree, $makeSliceDescriptor($getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), $getInt((IValue) stack[sp - 1]), tree_arity));
			return sp - 3;
		}
	},
	
	/**
	 * Create lexical slice
	 * [ ... ITree tree, IInteger from, IInteger by, IInteger to] => [ ..., new INode with slice elements as args ]
	 */
	lex_slice {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 4;

			ITree tree = (ITree) stack[sp - 4];
			int tree_arity = TreeAdapter.getArgs(tree).length();
			stack[sp - 4] = $makeSlice(tree, $makeSliceDescriptor($getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), $getInt((IValue) stack[sp - 1]), tree_arity));
			return sp - 3;
		}
	},

	/**
	 * Splice elements in a list writer
	 * 
	 * [ ..., IListWriter w, IListOrISet val ] => [ ..., w with val's elements spliced in ]
	 */
	listwriter_splice {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IListWriter writer = (IListWriter)arg_2;
			if(arg_1 instanceof IList){
				IList lst = (IList) arg_1;
				for(IValue v : lst){
					writer.append(v);
				}
			} else if(arg_1 instanceof ISet){
				ISet set = (ISet) arg_1;
				for(IValue v : set){
					writer.append(v);
				}
			} else {
				writer.append((IValue) arg_1);
			}
			return writer;
		}
	},

	/**
	 * Splice elements in a set writer
	 * 
	 * [ ..., ISetWriter w, IListOrISet val ] => [ ..., w with val's elements spliced in ]
	 */
	setwriter_splice {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISetWriter writer = (ISetWriter)arg_2;
			if(arg_1 instanceof IList){
				IList lst = (IList) arg_1;
				for(IValue v : lst){
					writer.insert(v);
				}
			} else if(arg_1 instanceof ISet){
				ISet set = (ISet) arg_1;
				for(IValue v : set){
					writer.insert(v);
				}
			} else {
				writer.insert((IValue) arg_1);
			}
			return writer;
		}
	},

	/**
	 * Splice elements of a concrete list in a list writer
	 * 
	 * [ ..., IListWriter w, ConcreteList val ] => [ ..., w with val's elements spliced in ]
	 */
	listwriter_splice_concrete_list_var {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IListWriter writer = (IListWriter)arg_2;
			IConstructor nonterm = (IConstructor) arg_1;
			//			stdout.println("nonterm = " + nonterm);

			IList nonterm_args = (IList) nonterm.get("args");
			//			stdout.println("nonterm_args = " + nonterm_args);

			if($getIterDelta((IConstructor) ((IConstructor) nonterm.get("prod")).get(0)) >= 0){
				for(IValue v : nonterm_args) {
					//					stdout.println("append: " + v);
					writer.append(v);
				}
			} else {
				IConstructor iter = (IConstructor) nonterm_args.get(0);
				//				stdout.println("iter = " + iter);

				IList iter_args = (IList) iter.get("args");
				//				stdout.println("iter_args = " + iter_args);

				for(IValue v : iter_args) {
					//					stdout.println("append: " + v);
					writer.append(v);
				}
			}

			return writer;
		}
	},

	/************************************************************************************************/
	/*                               Subscripting													*/
	/************************************************************************************************/

	/**
	 * Get subscripted element from adt
	 * 
	 * [ ..., IConstructor cons, IInteger idx ] => [ ..., IValue argument idx from cons ]
	 */
	adt_subscript_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IConstructor cons =  (IConstructor) arg_2;
			int idx = ((IInteger) arg_1).intValue();
			try {
				return cons.get((idx >= 0) ? idx : (cons.arity() + idx));
			} catch(IndexOutOfBoundsException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.indexOutOfBounds((IInteger) arg_1, currentFrame));
			}
		}
	},

	/**
	 * Has adt constructor an argument with given index?
	 * 
	 * [ ..., IConstructor cons, IInteger idx ] => [ ..., IBool true if idx is legal index]
	 */
	is_defined_adt_subscript_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IConstructor cons =  (IConstructor) arg_2;
			int idx = ((IInteger) arg_1).intValue();
			try {
				return new Object[] { Rascal_TRUE, cons.get((idx >= 0) ? idx : (cons.arity() + idx)) };
			} catch(IndexOutOfBoundsException e) {
				return new Object[] { Rascal_FALSE, null };
			}
		}
	},

	/**
	 * Get subscripted element from node
	 * 
	 * [ ..., INode nd, IInteger idx ] => [ ..., IValue argument idx from nd ]
	 */
	node_subscript_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			INode node =  (INode) arg_2;
			int idx = ((IInteger) arg_1).intValue();
			try {
				if(idx < 0){
					idx =  node.arity() + idx;
				}
				return node.get(idx);  
			} catch(IndexOutOfBoundsException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.indexOutOfBounds((IInteger) arg_1, currentFrame));
			}
		}
	},

	/**
	 * Has node an argument with given index?
	 * 
	 * [ ..., INode nd, IInteger idx ] => [ ..., IBool true if idx is legal index]
	 */
	is_defined_node_subscript_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			INode node =  (INode) arg_2;
			int idx = ((IInteger) arg_1).intValue();
			try {
				if(idx < 0){
					idx =  node.arity() + idx;
				}
				return new Object[] { Rascal_TRUE, node.get(idx) };
			} catch(IndexOutOfBoundsException e) {
				return new Object[] { Rascal_FALSE, null };
			}
		}
	},

	/**
	 * Get element with given index from list
	 * 
	 * [ ..., IList lst, IInteger idx ] => [ ..., IValue list element with index idx ]
	 */
	list_subscript_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IList lst = ((IList) arg_2);
			int idx = ((IInteger) arg_1).intValue();
			try {
				return lst.get((idx >= 0) ? idx : (lst.length() + idx));
			} catch(IndexOutOfBoundsException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.indexOutOfBounds((IInteger) arg_1, currentFrame));
			}
		}
	},

	/**
	 * Has list an element with given index?
	 * 
	 * [ ..., IList lst, IInteger idx ] => [ ...,  IBool true if idx is legal index ]
	 */
	is_defined_list_subscript_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IList lst = ((IList) arg_2);
			int idx = ((IInteger) arg_1).intValue();
			try {
				return new Object[] { Rascal_TRUE, lst.get((idx >= 0) ? idx : (lst.length() + idx)) };
			} catch(IndexOutOfBoundsException e) {
				return new Object[] { Rascal_FALSE, null };
			}
		}
	},

	/**
	 * Get element with given key from map
	 * 
	 * [ ..., IMap mp, IValue key ] => [ ..., mp[key] ]
	 */
	map_subscript {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue result = ((IMap) arg_2).get((IValue) arg_1);
			if(result == null) {
			  return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.noSuchKey((IValue) arg_1, currentFrame));
			}
			return result;
		}
	},

	/**
	 * Has map an element with given key?
	 * 
	 * [ ..., IMap mp, IValue key ] => [ ...,  IBool true if key is legal key ]
	 */
	is_defined_map_subscript {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			Object v = ((IMap) arg_2).get((IValue) arg_1);
			return new Object[] { (v == null) ? Rascal_FALSE : Rascal_TRUE, v };
		}
	},

	/**
	 * Get element with given index from str
	 * 
	 * [ ..., IString s, IInteger idx ] => [ ..., IString s[idx] ]
	 */
	str_subscript_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IString str = ((IString) arg_2);
			int idx = ((IInteger) arg_1).intValue();
			try {
				return (idx >= 0) ? str.substring(idx, idx+1)
						          : str.substring(str.length() + idx, str.length() + idx + 1);
			} catch(IndexOutOfBoundsException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.indexOutOfBounds((IInteger) arg_1, currentFrame));
			}
		}
	},

	/**
	 * Has str an element with given index? str
	 * 
	 * [ ..., IString s, IInteger idx ] => [ ..., IBool true if idx is legal index in s ]
	 */
	is_defined_str_subscript_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IString str = ((IString) arg_2);
			int idx = ((IInteger) arg_1).intValue();
			try {
				return new Object[] { Rascal_TRUE,  (idx >= 0) ? str.substring(idx, idx+1)
                                                               : str.substring(str.length() + idx, str.length() + idx + 1) };
			} catch(IndexOutOfBoundsException e) {
				return new Object[] { Rascal_FALSE, null };
			}
		}
	},

	/**
	 * Get element with given index from tuple
	 * 
	 * [ ..., ITuple tup, IInteger idx ] => [ ..., IValue tup[idx] ]
	 */
	tuple_subscript_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ITuple tup = (ITuple) arg_2;
			int idx = ((IInteger) arg_1).intValue();
			try {
				return tup.get((idx >= 0) ? idx : tup.arity() + idx);
			} catch(IndexOutOfBoundsException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.indexOutOfBounds((IInteger) arg_1, currentFrame));
			}
		}
	},

	/**
	 * Has tuple an element with given index?
	 * 
	 * [ ..., ITuple tup, IInteger idx ] => [ ..., IBool true if idx is legal index in tup ]
	 */
	is_defined_tuple_subscript_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ITuple tup = (ITuple) arg_2;
			int idx = ((IInteger) arg_1).intValue();
			try {
				return new Object[] { Rascal_TRUE, tup.get((idx >= 0) ? idx : tup.arity() + idx) };
			} catch(IndexOutOfBoundsException e) {
				return new Object[] { Rascal_FALSE, null };
			}
		}
	},
	
	/**
	 * Subscript of a binary rel with a single subscript (no set and unequal to _)
	 * 
	 * [ ..., IRelation r, IValue idx1] => r[idx1] ]
	 */
	rel2_subscript1_noset {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet rel = (ISet) arg_2;
			if(rel.isEmpty()){
				return rel;
			}
			return rel.asRelation().index((IValue) arg_1);
		}
	},

	
	/**
	 * Subscript of a binary rel with a single subscript (a set but unequal to _)
	 * 
	 * [ ..., IRelation r, IValue idx1] => r[idx1] ]
	 */
	rel2_subscript1_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet rel = (ISet) arg_2;
			if(rel.isEmpty()){
				return rel;
			}
			IValue index = (IValue) arg_1;
			ISetWriter wset = vf.setWriter();

			for (IValue v : rel) {
				ITuple tup = (ITuple)v;

				if((((ISet) index).contains(tup.get(0)))){
					wset.insert(tup.get(1));
				} 
			}
			return wset.done();
		}
	},

	/**
	 * Subscript of an n-ary (n > 2) rel with a single subscript (not a set and unequal to _)
	 * 
	 * [ ..., IRelation r, IValue idx1] => r[idx1] ]
	 */
	rel_subscript1_noset {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet rel = (ISet) arg_2;
			if(rel.isEmpty()){
				return rel;
			}
			return rel.asRelation().index((IValue) arg_1);
		}
	},
	
	/**
	 * Subscript of an n-ary (n > 2) rel with a single subscript (a set and unequal to _)
	 * 
	 * [ ..., IRelation r, IValue idx1] => r[idx1] ]
	 */
	rel_subscript1_set {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ISet rel = (ISet) arg_2;
			if(rel.isEmpty()){
				return rel;
			}
			int relArity = rel.getElementType().getArity();
			IValue index = (IValue) arg_1;			
			
			ISetWriter wset = vf.setWriter();
			IValue args[] = new IValue[relArity - 1];
			
			for (IValue v : rel) {
				ITuple tup = (ITuple)v;

				if((((ISet) index).contains(tup.get(0)))){
					for (int i = 1; i < relArity; i++) {
						args[i - 1] = tup.get(i);
					}
					wset.insert(vf.tuple(args));
				} 
			}
			return wset.done();
		}
	},
	
	/**
	 * Subscript of rel, general case
	 * subsDesc is a subscript descriptor: a list with integers: 0: noset, 1: set, 2: wildcard
	 * 
	 * [ ..., IRelation r, IList subsDesc, IValue idx1, IValue idx2, ...] => rel[idx1, idx2, ...] ]
	 */
	rel_subscript {
		@Override
		public int executeN(final Object[] stack, final int sp, final int arity, final Frame currentFrame, final RascalExecutionContext rex) {
			//assert arity >= 4;
			
			ISet rel = ((ISet) stack[sp - arity]);
			if(rel.isEmpty()){
				stack[sp - arity] = rel;
				return sp - arity + 1;
			}
			IList subsDesc = ((IList) stack[sp - arity + 1]);
			int indexArity = arity - 2;
			int relArity = rel.getElementType().getArity();
			
			ISetWriter wset = vf.setWriter();
			int indexBase = sp - arity + 2 ;

			if(relArity - indexArity == 1){	// Return a set
				allValues:
					for (IValue v : rel) {
						ITuple tup = (ITuple)v;
						for(int k = 0; k < indexArity; k++){
							switch(((IInteger)subsDesc.get(k)).intValue()){
							case 0: 
									if(!tup.get(k).isEqual((IValue)stack[indexBase + k])) continue allValues; 
									continue;
							case 1: 
									if(!((ISet)stack[indexBase + k]).contains(tup.get(k))) continue allValues;
							}
						}
						wset.insert(tup.get(indexArity));
					}
			} else {						// Return a relation
				IValue args[] = new IValue[relArity - indexArity];
				allValues:
					for (IValue v : rel) {
						ITuple tup = (ITuple)v;
						for(int k = 0; k < indexArity; k++){
							switch(((IInteger)subsDesc.get(k)).intValue()){
							case 0: 
									if(!tup.get(k).isEqual((IValue)stack[indexBase + k])) continue allValues; 
									continue;
							case 1: 
									if(!((ISet)stack[indexBase + k]).contains(tup.get(k))) continue allValues;
							}
						}

						for (int i = indexArity; i < relArity; i++) {
							args[i - indexArity] = tup.get(i);
						}
						wset.insert(vf.tuple(args));
					}
			}

			stack[sp - arity] = wset.done();
			return sp - arity + 1;
		}
	},
	
	is_defined_rel_subscript {
			@Override
			public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
				ISet rel = (ISet) arg_2;
				int arity = rel.getElementType().getArity();
				IValue idx = ((IValue) arg_1);
				if(idx.getType().isString()){
					String sidx = ((IString) idx).getValue();
					if(sidx.equals("_")){
					    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.notImplemented("Wild card _ not implemented", currentFrame.src, currentFrame));
					}
				}
				try {
					return new Object[] { Rascal_TRUE,
		                                  idx.getType().isSet() ? (arity == 2 ? RascalPrimitive.rel2_subscript1_set.execute2(arg_2, arg_1, currentFrame, rex)
		                                                                      : RascalPrimitive.rel_subscript1_set.execute2(arg_2, arg_1, currentFrame, rex))
		                                                        : (arity == 2 ? RascalPrimitive.rel2_subscript1_noset.execute2(arg_2, arg_1, currentFrame, rex)
		                                                                      : RascalPrimitive.rel_subscript1_noset.execute2(arg_2, arg_1, currentFrame, rex)) };
				} catch(Exception e) {
				    return new Object[] { Rascal_FALSE, null };
					
				}
			}
	},

	/**
	 * Subscript of a lrel
	 * 
	 * [ ..., IListRelation r, IValue idx1, IValue idx2, ...] => r[idx1, idx2, ...] ]
	 */
	lrel_subscript {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity >= 2;
			IList lrel = ((IList) stack[sp - arity]);
			if(lrel.isEmpty()){
				stack[sp - arity] = lrel;
				return sp - arity + 1;
			}
			int indexArity = arity - 1;
			int lrelArity = lrel.getElementType().getArity();
			assert indexArity < lrelArity ;
			
			IValue[] indices = new IValue[indexArity];
			Type subscriptType[] = new Type[indexArity];
			boolean subscriptIsSet[] = new boolean[indexArity];
			
			boolean yieldList = (lrelArity - indexArity) == 1;
			Type resFieldType[] = new Type[lrelArity - indexArity];
			
			for(int i = 0; i < indexArity; i++ ){
				indices[i] = (IValue) stack[sp - arity + i + 1];
				if(indices[i].getType().isString()){
					String s = ((IString) indices[i]).getValue();
					if(s.equals("_"))
						indices[i] = null;
				}
				subscriptType[i] = indices[i] == null ? valueType : indices[i].getType();
			}
			
			for (int i = 0; i < lrelArity; i++) {
				Type relFieldType = lrel.getType().getFieldType(i);
				if (i < indexArity) {
					if (subscriptType[i].isSet() && 
							relFieldType.comparable(subscriptType[i].getElementType())){
						subscriptIsSet[i] = true;
					} 
					else if (indices[i] == null || relFieldType.comparable(subscriptType[i])){
						subscriptIsSet[i] = false;
					} 
				} else {
					resFieldType[i - indexArity] = relFieldType;
				}
			}
			
			IListWriter wlist = vf.listWriter();
			
			for (IValue v : lrel) {
				ITuple tup = (ITuple)v;
				boolean allEqual = true;
				for(int k = 0; k < indexArity; k++){
					if(subscriptIsSet[k] && ((indices[k] == null) ||
							                 ((ISet) indices[k]).contains(tup.get(k)))){
						/* ok */
					} else if (indices[k] == null || tup.get(k).isEqual(indices[k])){
						/* ok */
					} else {
						allEqual = false;
					}
				}
				
				if (allEqual) {
					IValue args[] = new IValue[lrelArity - indexArity];
					for (int i = indexArity; i < lrelArity; i++) {
						args[i - indexArity] = tup.get(i);
					}
					if(yieldList){
						wlist.append(args[0]);
					} else {
						wlist.append(vf.tuple(args));
					}
				}
			}
			
			stack[sp - arity] = wlist.done();
			return sp - arity + 1;
		}
	},
	/**
     * Subscript of a lrel with single int index
     * 
     * [ ..., IListRelation r, IValue idx1] => r[idx1, idx2, ...] ]
     */
    lrel_subscript_int {
        @Override
        public Object execute2(final Object arg_2, final Object arg_1, Frame currentFrame, RascalExecutionContext rex) {
            return list_subscript_int.execute2(arg_2, arg_1, currentFrame, rex);
        }
	},

	/**
	 * Get argument with given index from nonterminal
	 * 
	 * [ ..., ITree tree, IInteger idx ] => [ ..., tree[idx] ]
	 */
	nonterminal_subscript_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {			
			ITree tree =  (ITree) arg_2;
			if(!TreeAdapter.isList(tree) && TreeAdapter.isInjectionOrSingleton(tree)){
				tree = (ITree) TreeAdapter.getArgs(tree).get(0);
			}
			if(!TreeAdapter.isList(tree)){
			  throw new InternalCompilerError("subscript not supported on " + TreeAdapter.getProduction(tree), currentFrame);
			}

			IList elems = TreeAdapter.getArgs(tree);
			int phys_length = elems.length();
			int sep_count = TreeAdapter.getSeparatorCount(tree);
			int log_length = (phys_length == 0) ? 0 : ((sep_count == 0 ? phys_length : 1 + phys_length/(1 + sep_count)));

			int index = ((IInteger) arg_1).intValue();
			if(index < 0){
				index = log_length + index;
			}
			if(index < 0 || index >= log_length){
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.indexOutOfBounds((IInteger) arg_1, currentFrame));
			}
			IValue result = elems.get(index * (sep_count + 1));
			if(result == null){
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.indexOutOfBounds((IInteger) arg_1, currentFrame));
			}

			return result;
		}
	},
	
	/**
	 * Get argument with given index from lexical
	 * 
	 * [ ..., ITree tree, IInteger idx ] => [ ..., tree[idx] ]
	 */
	lex_subscript_int {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ITree tree =  (ITree) arg_2;
			if(!TreeAdapter.isList(tree) && TreeAdapter.isInjectionOrSingleton(tree)){
				tree = (ITree) TreeAdapter.getArgs(tree).get(0);
			}
			if(!TreeAdapter.isList(tree)){
			    throw new InternalCompilerError("subscript not supported on " + TreeAdapter.getProduction(tree), currentFrame);
			}
			int index = ((IInteger) arg_1).intValue();
			int delta = 1;
			if(TreeAdapter.isSeparatedList(tree)){
				delta = TreeAdapter.getSeparatorCount(tree) + 1;
			}
			return TreeAdapter.getArgs(tree).get(index * delta);
		}
	},

	/*************************************************************************************************/
	/*         			Update parts of a structured value 											 */
	/*************************************************************************************************/

	/**
	 * Update argument of adt constructor by its field name
	 * 
	 * [ ..., IConstructor cons, IString fieldName, IValue repl ] => [ ..., new IConmstructor with cons.fieldName == repl ]
	 */
	adt_update {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			IConstructor cons = (IConstructor) stack[sp - 3];
			String field = ((IString) stack[sp - 2]).getValue();
			stack[sp - 3] = cons.set(field, (IValue) stack[sp - 1]);
			return sp - 2;
		}
	},

	/**
	 * Update list element
	 * 
	 * [ ..., IList cons, IInteger idx, IValue repl ] => [ ..., new IList with lst[idx] == repl ]
	 */
	list_update {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			IList lst = (IList) stack[sp - 3];
			int n = ((IInteger) stack[sp - 2]).intValue();
			if(n < 0){
				n = lst.length() + n;
			}
			try {
				stack[sp - 3] = lst.put(n, (IValue) stack[sp - 1]);
				return sp - 2;
			} catch (IndexOutOfBoundsException e){
			    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.indexOutOfBounds(vf.integer(n), currentFrame));
			    return sp - 2;
			}
		}
	},
	
	/**
     * Update list element
     * 
     * [ ..., IList cons, IInteger idx, IValue repl ] => [ ..., new IList with lst[idx] == repl ]
     */
    lrel_update {
        @Override
        public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
            return list_update.executeN(stack, sp, arity, currentFrame, rex);
        }
	},

	/**
	 * Update map element
	 * 
	 * [ ..., IMap mp, IValue key, IValue repl ] => [ ..., new IMap with mp[key] == repl ]
	 */
	map_update {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			IMap map = (IMap) stack[sp - 3];
			IValue key = (IValue) stack[sp - 2];
			stack[sp - 3] = map.put(key, (IValue) stack[sp - 1]);
			return sp - 2;
		}
	},

	/**
	 * Update tuple element
	 * 
	 * [ ..., ITuple tup, IInteger idx, IValue repl ] => [ ..., new ITuple with tup[idx] == repl ]
	 */
	tuple_update {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			ITuple tup = (ITuple) stack[sp - 3];
			int n = ((IInteger) stack[sp - 2]).intValue();
			try {
				stack[sp - 3] = tup.set(n, (IValue) stack[sp - 1]);
				return sp - 2;
			} catch (IndexOutOfBoundsException e){
			  rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.indexOutOfBounds(vf.integer(n), currentFrame));
			  return sp - 2;
			}
		}
	},
	
	/************************************************************************************************/
	/*				Annotations																		*/
	/************************************************************************************************/

	/**
	 * Get value of an annotation
	 * 
	 * [ ..., IConstructor val, IString label ] => [ ...,  IValue value of annotation label  ]
	 */
	annotation_get {
		@SuppressWarnings("deprecation")
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue val = (IValue) arg_2;
			String label = ((IString) arg_1).getValue();
			try {
				IValue result = val.asAnnotatable().getAnnotation(label);

				if(result == null) {
				  return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.noSuchAnnotation(label, currentFrame));
				}
				return result;
			} catch (FactTypeUseException e) {
			    return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.noSuchAnnotation(label, currentFrame));
			}
		}
	},

	/**
	 * Get value of an annotation, if it is defined
	 * 
	 * [ ..., IConstructor val, IString label ] => [ ...,  [ IBool present, IValue value of annotation label]  ]
	 */
	is_defined_annotation_get {
		@SuppressWarnings("deprecation")
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue val = (IValue) arg_2;
			String label = ((IString) arg_1).getValue();
			try {
				IValue v = val.asAnnotatable().getAnnotation(label);
				return new Object[] { (v == null) ? Rascal_FALSE : Rascal_TRUE, v };
			} catch (FactTypeUseException e) {
				return new Object[] { Rascal_FALSE, null };
			}
		}
	},

	/**
	 * Set value of an annotation
	 * 
	 * [ ..., IConstructor val, IString label, IValue repl ] => [ ...,  IConstructor val with annotation label set to repl  ]
	 */
	annotation_set {
		@SuppressWarnings("deprecation")
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			IValue val = (IValue) stack[sp - 3];
			String label = ((IString) stack[sp - 2]).getValue();
			IValue repl = (IValue) stack[sp - 1];
			stack[sp - 3] = val.asAnnotatable().setAnnotation(label, repl);
			return sp - 2;
		}
	},

	/**********************************************************************************************/
	/*			Type reachability for descendant match       									  */
	/**********************************************************************************************/

	/**
	 * Given a subject value and a descriptor, should we descent in it as abstract value?
	 * 
	 * [ ..., subject value, descriptor] => true/false
	 */
	should_descent_in_abstract {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue subject = (IValue) arg_2;
			DescendantDescriptor descriptor = (DescendantDescriptor) arg_1;
			return descriptor.shouldDescentInAbstractValue(subject);
		}
	},

	/**
	 * Given a subject value and a descriptor, should we descent in it as concrete value?
	 * 
	 * [ ..., subject value, descriptor] => true/false
	 */
	should_descent_in_concrete {
		@Override
		public Object execute2(final Object arg_2, final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			ITree subject = (ITree) arg_2;
			DescendantDescriptor descriptor = (DescendantDescriptor) arg_1;
			return descriptor.shouldDescentInConcreteValue(subject);
		}
	},
	
	/**
	 * Create a descendant descriptor given
	 * - a unique id
	 * - symbolset, set of symbols
	 * - prodset, set of productions
	 * - concreteMatch, indicates a concrete or abstract match
	 * - definitions needed for type reifier
	 * 
	 * [ IString id, ISet symbolset, ISET prodset, IBool concreteMatch, IMap definitions] => DescendantDescriptor
	 */
	make_descendant_descriptor {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 5;
			IString id = (IString) stack[sp - 5];

			stack[sp - 5] = rex.getDescendantDescriptorCache()
					.get(id, k -> {
						ISet symbolset = (ISet) stack[sp - 4];
						ISet prodset = (ISet) stack[sp - 3];
						IBool concreteMatch = (IBool) stack[sp - 2];
						IMap definitions = (IMap) stack[sp - 1];
						return new DescendantDescriptor(symbolset, prodset, definitions, concreteMatch, rex);
					});
			return sp - 4;
		};
	},

	/************************************************************************************************/
	/*				Miscellaneous																	*/
	/************************************************************************************************/

	/**
	 * Report a failing assertion
	 * 
	 * [ ..., IValue msg ] => raise assertionFailedException
	 *
	 */
	assert_fails {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IString message = (IString) arg_1;
			return rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.assertionFailed(message, currentFrame.src,  currentFrame));
		}
	},

	/**
	 * str_escape_for_regexp: escape the regexp meta-characters in a string
	 * 
	 * [ ... IValue val] => [ ..., IString escaped_val]
	 */

	str_escape_for_regexp {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			IValue v = ((IValue) arg_1);
			String s;
			if(v.getType().isString()){
				s = ((IString) v).getValue();
			} else {
				s = v.toString();
			}
			StringBuilder b = new StringBuilder();

			for (int i = 0; i < s.length(); i++) {
				char ch = s.charAt(i);
				if ("^.|?*+()[\\".indexOf(ch) != -1) {
					b.append('\\');
				}
				b.append(ch);
			}
			return vf.string(b.toString());
		}
	},

	/**
	 * Convert a value to string
	 * 
	 * [ ..., IValue val] => [ ..., IString converted_value]
	 */
	value_to_string {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return vf.string($value_to_string(arg_1, currentFrame, rex));
		}
	},

	/**
	 * Convert num to real
	 * 
	 * [ ..., INumber nval ] => [ ,,,., IReal rval ]
	 */
	num_to_real {
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			return ((INumber) arg_1).toReal(vf.getPrecision());
		}
	},

	/**
	 * parse a string according to type defined in moduleName
	 * 
	 * [ ..., IString moduleName, IConstructor type, IStringOrSourceLocation src ] => [ ..., ITree parseTree ]
	 */
	parse {
		@Override
		public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
			assert arity == 3;
			IString module_name = (IString) stack[sp - 3];
			IConstructor type = (IConstructor) stack[sp - 2];
			IValue source = (IValue) stack[sp - 1]; 
			if(source.getType().isString()){
				IString s = (IString) source;
				stack[sp - 3] = rex.getParsingTools().parse(module_name, type, s, currentFrame.src, true, currentFrame, rex);
			} else {
				ISourceLocation s = (ISourceLocation) source;
				stack[sp - 3] = rex.getParsingTools().parse(module_name, type, s, true, currentFrame, rex);
			}
			return sp - 2;
		}
	},
	
	/**
	 * memoize result of executing a function for given parameters
	 * 
	 * [ ..., IValue result ] => [ ..., IValue result ]
	 */
	memoize {
		@SuppressWarnings("unchecked")
		@Override
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			
			IValue result = (IValue) arg_1;
			Function fun = currentFrame.function;
			int nformals = fun.nformals;
			IValue[] args = new IValue[nformals - 1];
			for(int i = 0; i < nformals - 1; i++){
				args[i] = (IValue) currentFrame.stack[i];
			}
			MemoizationCache<IValue> cache = fun.memoization == null ? null : fun.memoization.get();
			if(cache == null){
				cache = new MemoizationCache<>();
	            fun.memoization = new SoftReference<>(cache);
			}
			cache.storeResult(args, (Map<String,IValue>)currentFrame.stack[nformals - 1], result);
			return result;
		}
	},
	
	/************************************************************************************************/
	/*				Keyword-related (former) MuPrimitives that need access to rex					*/
	/* NOTE: these primitives violate the convention that all RascalPrimitives return an IValue     */
	/*       Reason: they need access to keyword parameter handling									*/
	/************************************************************************************************/
	
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
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			INode v = (INode) arg_1;
			int cons_arity = v.arity();
			Object[] elems = new Object[cons_arity + 2];
			elems[0] = vf.string(v.getName());
			for (int i = 0; i < cons_arity; i++) {
				elems[i + 1] = v.get(i);
			}
			elems[cons_arity + 1] = $getAllKeywordParameters(v, rex);
			return elems;
		}	
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
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			INode v = (INode) arg_1;
			int cons_arity = v.arity();
			Object[] elems = new Object[cons_arity + 1];
			for (int i = 0; i < cons_arity; i++) {
			  elems[i] = v.get(i);
			}
			elems[cons_arity] = $getAllKeywordParameters(v, rex);
			return elems;
		}
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
		public Object execute1(final Object arg_1, final Frame currentFrame, final RascalExecutionContext rex) {
			INode v = (INode) arg_1;
			int cons_arity = v.arity();
			Map<String, IValue> m = $getAllKeywordParameters(v, rex);

			int kw_arity = m.size();
			Object[] elems = new Object[cons_arity + kw_arity];
			for (int i = 0; i < cons_arity; i++) {
			  elems[i] = v.get(i);
			}
			int j = cons_arity;
			for(IValue val : m.values()){
				elems[j++] = val;
			}
			return elems;
		}
	}
	;
	
	/********************************************************************************************************/
	/*								End of enumeration														*/
	/********************************************************************************************************/

	/**
	 * Abstract declaration for the execute0 method in a RascalPrimitive:
	 * @param currentFrame	the stackFram of the function that calls this primitive
	 * @param rex			the current RascalExecutionContext
	 * @return				value of of this execute method
	 */

	public Object execute0(Frame currentFrame, RascalExecutionContext rex) {
	  throw RascalRuntimeException.notImplemented("RascalPrimitive.execute0 " + name(), currentFrame.src, currentFrame);
	}
	
	/**
     * Abstract declaration for the execute1 method in a RascalPrimitive:
     * @param Object arg_1  argument of execute1
     * @param currentFrame  the stackFrame of the function that calls this primitive
     * @param rex           the current RascalExecutionContext
     * @return              value of of this execute method
     */
	@SuppressWarnings("unused")
    public Object execute1(Object arg_1, Frame currentFrame, RascalExecutionContext rex) {
	  throw RascalRuntimeException.notImplemented("RascalPrimitive.execute1 " + name(), currentFrame.src, currentFrame);
	}

	/**
     * Abstract declaration for the execute2 method in a RascalPrimitive:
     * @param Object arg_2, Object arg_1  arguments of execute2
     * @param currentFrame  the stackFrame of the function that calls this primitive
     * @param rex           the current RascalExecutionContext
     * @return              value of of this execute method
     */
	@SuppressWarnings("unused")
    public Object execute2(Object arg_2, Object arg_1, Frame currentFrame, RascalExecutionContext rex) {
	  throw RascalRuntimeException.notImplemented("RascalPrimitive.execute2 " + name(), currentFrame.src, currentFrame);
	}
	
	/**
     * Abstract declaration for the execute3 method in a RascalPrimitive:
     * @param Object arg_3, Object arg_2, Object arg_1  arguments of execute3
     * @param currentFrame  the stackFrame of the function that calls this primitive
     * @param rex           the current RascalExecutionContext
     * @return              value of of this execute method
     */
    @SuppressWarnings("unused")
    public Object execute3(Object arg_3, Object arg_2, Object arg_1, Frame currentFrame, RascalExecutionContext rex) {
      throw RascalRuntimeException.notImplemented("RascalPrimitive.execute3 " + name(), currentFrame.src, currentFrame);
    }
    
    /**
     * Abstract declaration for the execute4 method in a RascalPrimitive:
     * @param Object arg_4, Object arg_3, Object arg_2, Object arg_1  arguments of execute4
     * @param currentFrame  the stackFrame of the function that calls this primitive
     * @param rex           the current RascalExecutionContext
     * @return              value of of this execute method
     */
    @SuppressWarnings("unused")
    public Object execute4(Object arg_4, Object arg_3, Object arg_2, Object arg_1, Frame currentFrame, RascalExecutionContext rex) {
      throw RascalRuntimeException.notImplemented("RascalPrimitive.execute4 " + name(), currentFrame.src, currentFrame);
    }
    
    /**
     * Abstract declaration for the execute5 method in a RascalPrimitive:
     * @param Object arg_5, Object arg_4, Object arg_3, Object arg_2, Object arg_1  arguments of execute5
     * @param currentFrame  the stackFrame of the function that calls this primitive
     * @param rex           the current RascalExecutionContext
     * @return              value of of this execute method
     */
    @SuppressWarnings("unused")
    public Object execute5(Object arg_5, Object arg_4, Object arg_3, Object arg_2, Object arg_1, Frame currentFrame, RascalExecutionContext rex) {
      throw RascalRuntimeException.notImplemented("RascalPrimitive.execute5 " + name(), currentFrame.src, currentFrame);
    }

	/**
	 * Abstract declaration for the (n-ary) executeN method in a RascalPrimitive:
	 * @param stack    Current stack
	 * @param sp       stack pointer
	 * @param arity    numbers of arguments (found on the stack)
	 * @param currentFrame the stackFrame of the function that calls this primitive
	 * @param rex      the current RascalExecutionContext
	 * @return         new value for stack pointer (sp)
	 */
	public int executeN(Object[] stack, int sp, int arity, Frame currentFrame, RascalExecutionContext rex) {
	  throw RascalRuntimeException.notImplemented("RascalPrimitive.executeN " + name(), currentFrame.src, currentFrame);
	}

	/**
	 * Array of all RascalPrimitives
	 */
	static final RascalPrimitive[] values = RascalPrimitive.values();

	/**
	 * Return a RascalPrimtive based on its index
	 */
	public static RascalPrimitive fromInteger(int prim){
		return values[prim];
	}
	
	// Some global variables and constants
	
	public static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory tf = TypeFactory.getInstance();

	private static final Type lineColumnType = TypeFactory.getInstance().tupleType(new Type[] {TypeFactory.getInstance().integerType(), TypeFactory.getInstance().integerType()},
			new String[] {"line", "column"});
	
	public static final Type nodeType = tf.nodeType();
	public static final Type valueType = tf.valueType();
	private static final IMap emptyMap = vf.mapWriter().done();
	private static final IList emptyList = vf.listWriter().done();
	private static final ISet emptySet = vf.setWriter().done();

	public static final IBool Rascal_TRUE =  ValueFactoryFactory.getValueFactory().bool(true);
	public static final IBool Rascal_FALSE =  ValueFactoryFactory.getValueFactory().bool(false);
	
	/**
	 * Generic exit function that allows some post execution actions (like printing a profile)
	 * @param rex
	 */
	public static void exit(RascalExecutionContext rex){
	}

	
	// Bootstrap method used for invokeDynamic on RascalPrimitives, see BytecoeGenerator
	
	@SuppressWarnings("unused")
    public static CallSite bootstrapRascalPrimitive(MethodHandles.Lookup caller, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
	    MethodHandles.Lookup lookup = MethodHandles.lookup();
	    RascalPrimitive enumElement = RascalPrimitive.valueOf(name);
        Class<?>[] parameters = type.parameterArray();
        int arity = parameters.length - 2;  // do not count common arguments cf and rex
        String suffix = arity <= 2 ? String.valueOf(arity): "N";
	    Method execute = enumElement.getClass().getMethod("execute" + suffix, parameters);

	    MethodHandle foundMethod = lookup.unreflect(execute).bindTo(enumElement);
	    return new ConstantCallSite(foundMethod.asType(type));
	}

	/************************************************************************************
	 * 					AUXILIARY FUNCTIONS	 (prefixed with $) used in RascalPrimitives	*	
	 ************************************************************************************/

	/*
	 * String templates
	 */

	void $pushIndent(final String s, final RascalExecutionContext rex){
		//stdout.println("$indent: " + indentStack.size() + ", \"" + s + "\"");
		rex.getIndentStack().push(s);
	}

	void $popIndent(final RascalExecutionContext rex){
		rex.getIndentStack().pop();
	}

	public String $getCurrentIndent(final RascalExecutionContext rex) {
		return rex.getIndentStack().isEmpty() ? "" : rex.getIndentStack().peek();
	}

	public String $indent(final String s, final RascalExecutionContext rex) {
		String ind = rex.getIndentStack().pop();		// TODO: check empty?
		rex.getIndentStack().push(ind + s);
		return s;
	}

	public String $unindent(final String s, final RascalExecutionContext rex) {
		String ind = rex.getIndentStack().pop();		// TODO: check empty?
		int indLen = ind.length();
		int sLen = s.length();
		int endIndex = Math.max(indLen - sLen,  0);
		rex.getIndentStack().push(ind.substring(0, endIndex));
		return s;
	}

	IString $removeMargins(final IString s, final RascalExecutionContext rex) {
		// NB: ignored margin indents can only start *after* a new line.
		// So atBeginning is initially false.
		boolean atBeginning = false;
		StringBuffer buf = new StringBuffer();
		String indent = $getCurrentIndent(rex);

		StringBuilder sb = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++) {
			int ch = s.charAt(i);
			if (atBeginning && (ch == ' ' || ch == '\t')) {
				buf.appendCodePoint(ch);
				continue;
			}
			if (atBeginning && ch == '\'') {
				// we've only seen ' ' and/or '\t' so we're about
				// to reach real content, don't add to buf.
				buf = new StringBuffer();
				atBeginning = false;
				continue;
			}
			if (ch == '\n') { // atBeginning &&
				sb.append(buf);
				buf = new StringBuffer(indent);
				atBeginning = true;
				sb.appendCodePoint(ch);
				continue;
			}
			if (atBeginning) {
				// we were in the margin, but found something other
				// than ' ', '\t' and '\'', so anything in buf
				// is actual content; add it.
				sb.append(buf);
				buf = new StringBuffer();
				sb.appendCodePoint(ch);
				atBeginning = false;
				continue;
			}
			sb.appendCodePoint(ch);
		}

		// Add trailing whitespace (fixes #543)
		sb.append(buf.toString());
		String jstr = sb.toString();
		// TODO: inline this to avoid another pass over the string.
		return vf.string(jstr);
		//return vf.string(org.rascalmpl.interpreter.utils.StringUtils.unescapeSingleQuoteAndBackslash(jstr));
	}

	private static ISourceLocation $loc_field_update(final ISourceLocation sloc, final String field, final IValue repl, final Frame currentFrame, RascalExecutionContext rex) {		
		Type replType = repl.getType();

		int iLength = sloc.hasOffsetLength() ? sloc.getLength() : -1;
		int iOffset = sloc.hasOffsetLength() ? sloc.getOffset() : -1;
		int iBeginLine = sloc.hasLineColumn() ? sloc.getBeginLine() : -1;
		int iBeginColumn = sloc.hasLineColumn() ? sloc.getBeginColumn() : -1;
		int iEndLine = sloc.hasLineColumn() ? sloc.getEndLine() : -1;
		int iEndColumn = sloc.hasLineColumn() ? sloc.getEndColumn() : -1;
		URI uri;
		boolean uriPartChanged = false;
		String scheme = sloc.getScheme();
		String authority = sloc.hasAuthority() ? sloc.getAuthority() : "";
		String path = sloc.hasPath() ? sloc.getPath() : null;
		String query = sloc.hasQuery() ? sloc.getQuery() : null;
		String fragment = sloc.hasFragment() ? sloc.getFragment() : null;

		try {
			String newStringValue = null;
			if(replType.isString()){
				newStringValue = ((IString)repl).getValue();
			}

			switch (field) {

			case "uri":
				uri = URIUtil.createFromEncoded(newStringValue);
				// now destruct it again
				scheme = uri.getScheme();
				authority = uri.getAuthority();
				path = uri.getPath();
				query = uri.getQuery();
				fragment = uri.getFragment();
				uriPartChanged = true;
				break;

			case "scheme":
				scheme = newStringValue;
				uriPartChanged = true;
				break;

			case "authority":
				authority = newStringValue;
				uriPartChanged = true;
				break;

			case "host":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				  rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.noSuchField("The scheme " + sloc.getScheme() + " does not support the host field, use authority instead.", currentFrame));
				}
				uri = URIUtil.changeHost(sloc.getURI(), newStringValue);
				authority = uri.getAuthority();
				uriPartChanged = true;
				break;

			case "path":
				path = newStringValue;
				uriPartChanged = true;
				break;

			case "file": 
				int i = path.lastIndexOf("/");

				if (i != -1) {
					path = path.substring(0, i) + "/" + newStringValue;
				}
				else {
					path = path + "/" + newStringValue;	
				}	
				uriPartChanged = true;
				break;

			case "parent":
				i = path.lastIndexOf("/");
				String parent = newStringValue;
				if (i != -1) {
					path = parent + path.substring(i);
				}
				else {
					path = parent;	
				}
				uriPartChanged = true;
				break;	

			case "ls":
			    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.noSuchField("Cannot update the children of a location", currentFrame));
			    break;

			case "extension":
				String ext = newStringValue;

				if (path.length() > 1) {
					int index = path.lastIndexOf('.');

					if (index == -1 && !ext.isEmpty()) {
						path = path + (!ext.startsWith(".") ? "." : "") + ext;
					}
					else if (!ext.isEmpty()) {
						path = path.substring(0, index) + (!ext.startsWith(".") ? "." : "") + ext;
					}
					else {
						path = path.substring(0, index);
					}
				}
				uriPartChanged = true;
				break;

			case "top":
				if (replType.isString()) {
					uri = URIUtil.assumeCorrect(newStringValue);
					scheme = uri.getScheme();
					authority = uri.getAuthority();
					path = uri.getPath();
					query = uri.getQuery();
					fragment = uri.getFragment();
				}
				else if (replType.isSourceLocation()) {
					ISourceLocation rep = (ISourceLocation) repl;
					scheme = rep.getScheme();
					authority = rep.hasAuthority() ? rep.getAuthority() : null;
					path = rep.hasPath() ? rep.getPath() : null;
					query = rep.hasQuery() ? rep.getQuery() : null;
					fragment = rep.hasFragment() ? rep.getFragment() : null;
				}
				uriPartChanged = true;
				break;

			case "fragment":
				fragment = newStringValue;
				uriPartChanged = true;
				break;

			case "query":
				query = newStringValue;
				uriPartChanged = true;
				break;

			case "user":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.noSuchField("The scheme " + sloc.getScheme() + " does not support the user field, use authority instead.", currentFrame));
				}
				uri = sloc.getURI();
				if (uri.getHost() != null) {
					uri = URIUtil.changeUserInformation(uri, newStringValue);
				}

				authority = uri.getAuthority();
				uriPartChanged = true;
				break;

			case "port":
				if (!URIResolverRegistry.getInstance().supportsHost(sloc)) {
				    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.noSuchField("The scheme " + sloc.getScheme() + " does not support the port field, use authority instead.", currentFrame));
				}
				if (sloc.getURI().getHost() != null) {
					int port = Integer.parseInt(((IInteger) repl).getStringRepresentation());
					uri = URIUtil.changePort(sloc.getURI(), port);
				}
				authority = sloc.getURI().getAuthority();
				uriPartChanged = true;
				break;	

			case "length":
				iLength = ((IInteger) repl).intValue();
				if (iLength < 0) {
				    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidArgument(repl, currentFrame));
				}
				break;

			case "offset":
				iOffset = ((IInteger) repl).intValue();
				if (iOffset < 0) {
				    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidArgument(repl, currentFrame));
				}
				break;

			case "begin":
				iBeginLine = ((IInteger) ((ITuple) repl).get(0)).intValue();
				iBeginColumn = ((IInteger) ((ITuple) repl).get(1)).intValue();

				if (iBeginColumn < 0 || iBeginLine < 0) {
				    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidArgument(repl, currentFrame));
				}
				break;
			case "end":
				iEndLine = ((IInteger) ((ITuple) repl).get(0)).intValue();
				iEndColumn = ((IInteger) ((ITuple) repl).get(1)).intValue();

				if (iEndColumn < 0 || iEndLine < 0) {
				    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidArgument(repl, currentFrame));
				}
				break;			

			default:
			    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.noSuchField("Modification of field " + field + " in location not allowed", currentFrame));
			}

			ISourceLocation newLoc = sloc;
			if (uriPartChanged) {
				newLoc = vf.sourceLocation(scheme, authority, path, query, fragment);
			}

			if (sloc.hasLineColumn()) {
				// was a complete loc, and thus will be now
				return vf.sourceLocation(newLoc, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
			}

			if (sloc.hasOffsetLength()) {
				// was a partial loc

				if (iBeginLine != -1 || iBeginColumn != -1) {
					//will be complete now.
					iEndLine = iBeginLine;
					iEndColumn = iBeginColumn;
					return vf.sourceLocation(newLoc, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
				}
				else if (iEndLine != -1 || iEndColumn != -1) {
					// will be complete now.
					iBeginLine = iEndLine;
					iBeginColumn = iEndColumn;
					return vf.sourceLocation(newLoc, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
				}
				else {
					// remains a partial loc
					return vf.sourceLocation(newLoc, iOffset, iLength);
				}
			}

			// used to have no offset/length or line/column info, if we are here

			if (iBeginColumn != -1 || iEndColumn != -1 || iBeginLine != -1 || iBeginColumn != -1) {
				// trying to add line/column info to a uri that has no offset length
			    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidUseOfLocation("Can not add line/column information without offset/length", currentFrame));
			}

			// trying to set offset that was not there before, adding length automatically
			if (iOffset != -1 ) {
				if (iLength == -1) {
					iLength = 0;
				}
			}

			// trying to set length that was not there before, adding offset automatically
			if (iLength != -1) {
				if (iOffset == -1) {
					iOffset = 0;
				}
			}

			if (iOffset != -1 || iLength != -1) {
				// used not to no offset/length, but do now
				return vf.sourceLocation(newLoc, iOffset, iLength);
			}

			// no updates to offset/length or line/column, and did not used to have any either:
			return newLoc;

		} catch (IllegalArgumentException e) {
		    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidArgument(currentFrame));
		} catch (URISyntaxException e) {
		    rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidURI(e.getMessage(), currentFrame));
		}
		return null;
	}

	private static IBool $list_less_list(IList left, IList right) {
		if(left.length() > right.length()){
			return Rascal_FALSE;
		}
		OUTER:for (int l = 0, r = 0; l < left.length(); l++) {
			for (r = Math.max(l, r) ; r < right.length(); r++) {
				if (left.get(l).isEqual(right.get(r))) {
					r++;
					continue OUTER;
				}
			}
			return Rascal_FALSE;
		}
		return vf.bool(left.length() != right.length());
	}

	static IValue $add(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex){
		return (IValue) add.execute2(left, right, currentFrame, rex);
	}

	static IValue $subtract(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex){
		return (IValue) subtract.execute2(left, right, currentFrame, rex);
	}

	static IValue $product(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex){
		return (IValue) product.execute2(left, right, currentFrame, rex);
	}

	static IValue $divide(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex){
		return (IValue) divide.execute2(left, right, currentFrame, rex);
	}

	static IValue $intersect(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex){
		return (IValue) intersect.execute2(left, right, currentFrame, rex);
	}

	private static IBool $equal(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex){
		return (IBool) equal.execute2(left, right, currentFrame, rex);
	
	}

	private static IBool $lessequal(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex){
		return (IBool) lessequal.execute2(left, right, currentFrame, rex);
	}

	private static IBool $list_lessequal_list(IList left, IList right) {
		if (left.length() == 0) {
			return Rascal_TRUE;
		}
		else if (left.length() > right.length()) {
			return Rascal_FALSE;
		}

		OUTER:for (int l = 0, r = 0; l < left.length(); l++) {
			for (r = Math.max(l, r) ; r < right.length(); r++) {
				if (left.get(l).isEqual(right.get(r))) {
					continue OUTER;
				}
			}
			return Rascal_FALSE;
		}

		return vf.bool(left.length() <= right.length());
	}

	private static Integer $getInt(IValue v){
		return v instanceof IInteger ? ((IInteger) v).intValue() : null;
	}

	public SliceDescriptor $makeSliceDescriptor(Integer first, Integer second, Integer end, int len) {

		int firstIndex = 0;
		int secondIndex = 1;
		int endIndex = len;

		if(first != null){
			firstIndex = first;
			if(firstIndex < 0)
				firstIndex += len;
		}
		if(end != null){
			endIndex = end;
			if(endIndex < 0){
				endIndex += len;
			}
		}

		if(second == null){
			secondIndex = firstIndex + ((firstIndex <= endIndex) ? 1 : -1);
		} else {
			secondIndex = second;
			if(secondIndex < 0)
				secondIndex += len;
			if(!(first == null && end == null)){
				if(first == null && secondIndex > endIndex)
					firstIndex = len - 1;
				if(end == null && secondIndex < firstIndex)
					endIndex = -1;
			}
		}

		if(len == 0 || firstIndex >= len){
			firstIndex = secondIndex = endIndex = 0;
		} else if(endIndex > len){
			endIndex = len;
		} 
//		else if(endIndex == -1){
//			endIndex = 0;
//		}

		return new SliceDescriptor(firstIndex, secondIndex, endIndex);
	}

	// Slices on list

	public IList $makeSlice(IList lst, SliceDescriptor sd){
		IListWriter w = vf.listWriter();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					w.append(lst.get(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < lst.length(); j += increment){
					w.append(lst.get(j));
				}
			}
		return w.done();
	}

	public int $list_slice_operator(final Object[] stack, final int sp, final int arity, final SliceOperator op, final Frame currentFrame, final RascalExecutionContext rex) {
		assert arity == 5;
		IList lst = (IList) stack[sp - 5];
		SliceDescriptor sd = $makeSliceDescriptor($getInt((IValue) stack[sp - 4]), $getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), lst.length());
		IList repl = (IList) stack[sp - 1];
		stack[sp - 5] = $updateListSlice(lst, sd, op, repl, currentFrame, rex);
		return sp - 4;
	}

	public IList $updateListSlice(final IList lst, final SliceDescriptor sd, final SliceOperator op, final IList repl, final Frame currentFrame, final RascalExecutionContext rex){
		IListWriter w = vf.listWriter();
		int increment = sd.second - sd.first;
		int replIndex = 0;
		int rlen = repl.length();
		boolean wrapped = false;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				assert increment > 0;
				int listIndex = 0;
				while(listIndex < sd.first){
					w.append(lst.get(listIndex++));
				}
				while(listIndex >= 0 && listIndex < sd.end){
					w.append(op.execute(lst.get(listIndex), repl.get(replIndex++), currentFrame, rex));
					if(replIndex == rlen){
						replIndex = 0;
						wrapped = true;
					}
					for(int q = 1; q < increment && listIndex + q < sd.end; q++){
						w.append(lst.get(listIndex + q));
					}
					listIndex += increment;
				}
				listIndex = sd.end;
				if(!wrapped){
					while(replIndex < rlen){
						w.append(repl.get(replIndex++));
					}
				}
				while(listIndex < lst.length()){
					w.append(lst.get(listIndex++));
				}
			} else {
				assert increment < 0;
				int j = lst.length() - 1;
				while(j > sd.first){
					w.insert(lst.get(j--));
				}
				while(j >= 0 && j > sd.end && j < lst.length()){
					w.insert(op.execute(lst.get(j), repl.get(replIndex++), currentFrame, rex));
					if(replIndex == rlen){
						replIndex = 0;
						wrapped = true;
					}
					for(int q = -1; q > increment && j + q > sd.end; q--){
						w.insert(lst.get(j + q));
					}
					j += increment;
				}
				j = sd.end;
				if(!wrapped){
					while(replIndex < rlen){
						w.insert(repl.get(replIndex++));
					}
				}

				while(j >= 0){
					w.insert(lst.get(j--));
				}

			}
		return w.done();
	}

	public IString $makeSlice(final IString str, final SliceDescriptor sd){
		StringBuilder buffer = new StringBuilder();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					buffer.appendCodePoint(str.charAt(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < str.length(); j += increment){
					buffer.appendCodePoint(str.charAt(j));
				}
			}
		return vf.string(buffer.toString());
	}

	public IList $makeSlice(final INode node, final SliceDescriptor sd){
		IListWriter w = vf.listWriter();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					w.append(node.get(i));
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < node.arity(); j += increment){
					w.append(node.get(j));
				}
			}

		return w.done();
	}
	
	public IConstructor $makeSlice(final ITree tree, final int separatorCount, final int minLength, final SliceDescriptor sd){
		IListWriter w = vf.listWriter();

		IList elems = TreeAdapter.getArgs(tree);
		
		int increment = sd.second - sd.first;
		if(sd.first == sd.end){
			// nothing to be done
		} else
			if(sd.first <= sd.end){
				for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
					int base_i = i * (separatorCount + 1);
					w.append(elems.get(base_i));
					if(i < sd.end - increment){
						for(int k = 1; k <= separatorCount; k++){
							w.append(elems.get(base_i + k));
						}
					}
				}
			} else {
				for(int j = sd.first; j >= 0 && j > sd.end && j < elems.length(); j += increment){
					int base_j = j * (separatorCount + 1);
					w.append(elems.get(base_j));
					if(j > sd.end && j > 0){
						for(int k = 1; k <= separatorCount; k++){
							w.append(elems.get(base_j - k));
						}
					}
				}
			}
		
		IConstructor prod = TreeAdapter.getProduction(tree);
		IList new_elems = w.done();
		if(new_elems.length() < minLength){
			return null;
		}
		IConstructor result = vf.constructor(RascalValueFactory.Tree_Appl, prod, new_elems);
		return result;
	}

	private static boolean $isTree(final IValue v){
		return v.getType().isSubtypeOf(RascalValueFactory.Tree); 
	}

	static int $getIterDelta(final IConstructor cons){		
		Type tp = cons.getConstructorType();
		if(tp == RascalValueFactory.Symbol_IterPlus || tp == RascalValueFactory.Symbol_IterStar){
			return 2;
		}
		
		if(tp == RascalValueFactory.Symbol_IterSepX || tp == RascalValueFactory.Symbol_IterStarSepX){
			return 4;
		}
		return -1;
	}

	public String $unescape(final String s) {
		StringBuilder b = new StringBuilder(s.length() * 2); 

		int sLength = s.length();
		for (int c = 0; c < sLength; c++) {
			String schr = s.substring(c, c+1);
			String sub = schr;

			switch(schr){
			case "\\\"":	sub = "\""; break;
			case "\\\'":	sub = "\'"; break;
			case "\\\\":	sub = "\\"; break;
			case "\\<":		sub = "<"; break;
			case "\\>":		sub = ">"; break;
			}
			b.append(sub);
		}
		return b.toString();
	}

	// TODO: merge the following two functions

	private static String $value_to_string(final Object given, final Frame currentFrame, RascalExecutionContext rex) {
	    String res = null;
	    if(given != null){
	        if(given instanceof IValue){
	            IValue val = (IValue) given;
	            Type tp = val.getType();
	            if(tp.isList()){
	                Type elemType = tp.getElementType();
	                if(!elemType.equals(tf.voidType()) && elemType.isNode() && elemType.isSubtypeOf(RascalValueFactory.Tree)){
	                    IList lst = (IList) val;
	                    StringWriter w = new StringWriter();
	                    for(int i = 0; i < lst.length(); i++){
	                        w.write($value2string(lst.get(i)));
	                    }
	                    res = w.toString();
	                } else {
	                    res = $value2string(val);
	                }
	            } else {
	                res = $value2string(val);
	            }
	        } else if(given instanceof Integer){
	            res = ((Integer) given).toString();
	        } else {
	            rex.getFrameObserver().exception(currentFrame, RascalRuntimeException.invalidArgument(vf.string(given.toString()), currentFrame));
	        }
	    } else {
	        return "";
	    }
	    return res;
	}

	public static String $value2string(final IValue val){
		if(val.getType().isString()){
			return ((IString) val).getValue();
		}
		if($isTree(val)){
			StringWriter w = new StringWriter();
			try {
				IConstructor c = (IConstructor) val;
				TreeAdapter.unparse(c, w);
				return w.toString();
			} catch (FactTypeUseException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		return val.toString();
	}

	/**
	 * @param t the given type
	 * @return t converted to a symbol
	 */
	static IConstructor $type2symbol(final Type t){
	    return t.asSymbol(vf, /*new TypeStore()*/ new TypeStore(RascalValueFactory.getStore()), vf.setWriter(), new HashSet<>());
	}
	
	private static Map<String,IValue> $getAllKeywordParameters(IValue v, RascalExecutionContext rex){
		Type tp = v.getType();
		
		if(tp.isAbstractData()){
			IConstructor cons = (IConstructor) v;
			if(cons.mayHaveKeywordParameters()){
				Map<String, IValue> setKwArgs =  cons.asWithKeywordParameters().getParameters();
				String consName = cons.getName();
				Function getDefaults = rex.getCompanionDefaultsFunction(consName, tp);
				if(getDefaults != RVMCore.noCompanionFunction){
					IValue[] posArgs = new IValue[cons.arity()];
					for(int i = 0; i < cons.arity(); i++){
						posArgs[i] = cons.get(i);
					}
					
					@SuppressWarnings("unchecked")
					Map<String, Map.Entry<Type, IValue>> defaults = (Map<String, Map.Entry<Type, IValue>>) rex.getRVM().executeRVMFunction(getDefaults, posArgs, setKwArgs);

					HashMap<String, IValue> allKwArgs = new HashMap<>(defaults.size());
					for(String key : defaults.keySet()){
						IValue val = setKwArgs.get(key);
						if(val != null){
							allKwArgs.put(key,  val);
						} else {
							allKwArgs.put(key, defaults.get(key).getValue());
						}
					}
					//System.err.println(", returns " + allKwArgs);
					
					return allKwArgs;
					
				}
			} else {
				return RVMCore.emptyKeywordMap;
			}
		}
		
		if(tp.isNode()){
			INode nd = (INode) v;
			if(nd.mayHaveKeywordParameters()){
				return nd.asWithKeywordParameters().getParameters();
			} else {
				return RVMCore.emptyKeywordMap;
			}
		}
		
		throw new InternalCompilerError("getAllKeywordParameters");
	}
	
	/*
	 * Main program: handy to map a primitive index back to its name (e.g., in profiles!)
	 */
	public static void main(String[] args) {
		int n = 427;
		
		System.err.println("RascalPrimitive: " + fromInteger(n) + " (" + n + ")");
	}
}

/*
 * Internal class to describe slices
 */

class SliceDescriptor{

	final int first;
	final int second;
	final int end;

	SliceDescriptor(int first, int second, int end){
		this.first = first;
		this.second = second;
		this.end = end;
	}
}

enum SliceOperator {
	replace(0) {
		@Override
		public IValue execute(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex) {
			return right;
		}
	},
	add(1) {
		@Override
		public IValue execute(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex) {
			return RascalPrimitive.$add(left, right, currentFrame, rex);
		}
	},
	subtract(2){
		@Override
		public IValue execute(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex) {
			return RascalPrimitive.$subtract(left, right, currentFrame, rex);
		}
	}, 
	product(3){
		@Override
		public IValue execute(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex) {
			return RascalPrimitive.$product(left, right, currentFrame, rex);
		}
	}, 

	divide(4){
		@Override
		public IValue execute(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex) {
			return RascalPrimitive.$divide(left, right, currentFrame, rex);
		}
	}, 

	intersect(5){
		@Override
		public IValue execute(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex) {
			return RascalPrimitive.$intersect(left, right, currentFrame, rex);
		}
	};

	final int operator;

	public static final SliceOperator[] values = SliceOperator.values();

	public static SliceOperator fromInteger(int n) {
		return values[n];
	}

	public abstract IValue execute(final IValue left, final IValue right, final Frame currentFrame, final RascalExecutionContext rex);
	
	public static SliceOperator replace() {
		return values[0];
	}

	public static SliceOperator add() {
		return values[1];
	}

	public static SliceOperator subtract() {
		return values[2];
	}

	public static SliceOperator product() {
		return values[3];
	}

	public static SliceOperator divide() {
		return values[4];
	}

	public static SliceOperator intersect() {
		return values[5];
	}

	SliceOperator(int op) {
		this.operator = op;
	}

}