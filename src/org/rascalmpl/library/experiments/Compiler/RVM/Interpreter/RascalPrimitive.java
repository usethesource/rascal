package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.regex.Pattern;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListRelation;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetRelation;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.exceptions.InvalidDateTimeException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperation;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.cobra.TypeParameterVisitor;
import org.rascalmpl.library.experiments.Compiler.Rascal2muRascal.RandomValueTypeVisitor;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.uptr.TreeAdapter;

/*
 * The primitives that can be called via the CALLPRIM instruction.
 * Each primitive with name P (e.g., int_add_int) is defined by:
 * - an enumeration constant P (e.g., int_add_int)
 * - a method int execute(Object[] stack, int sp) associated with that enumeration constant
 * 
 * Each primitive implementation gets the current stack and stack pointer as argument
 * and returns a new stack pointer. It may make modifications to the stack.
 */

public enum RascalPrimitive {

	assertreport {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 3;
			boolean succeeded = (stack[sp - 3] instanceof Boolean) ? (Boolean) stack[sp - 3] : ((IBool) stack[sp - 3]).getValue();
			IString message = (IString) stack[sp - 2];
			ISourceLocation src = ((ISourceLocation) stack[sp - 1]);
			if(!succeeded){
				stdout.println("Assertion failed" + message + " at " + src);
				throw RuntimeExceptions.assertionFailed(message, src,  new ArrayList<Frame>());
			}
			return sp - 2;
		}
	},

	/*
	 * Value factory operations
	 */
	
	constructor {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			Type type = (Type) stack[sp - 2]; 
			IValue[] args = (IValue[]) stack[sp - 1];
			stack[sp - 2] = vf.constructor(type, args);
			return sp - 1;
		}
	},
	node {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			String name = ((IString) stack[sp - 2]).getValue(); 
			IValue[] args = (IValue[]) stack[sp - 1];
			stack[sp - 2] = vf.node(name, args);
			return sp - 1;
		}
	},
	list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			IValue[] args = (IValue[]) stack[sp - 1];
			stack[sp - 1] = args.length == 0 ? emptyList : vf.list(args);
			return sp;
		}
	},
	set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			IValue[] args = (IValue[]) stack[sp - 1];
			stack[sp - 1] = args.length == 0 ? emptySet : vf.set(args);
			return sp;
		}
	},
	tuple {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			IValue[] args = (IValue[]) stack[sp - 1];
			stack[sp - 1] = vf.tuple(args);
			return sp;
		}
	},
	
	/*
	 * ...writer_add
	 */
	
	listwriter_add {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity > 0;
			IListWriter writer = (IListWriter) stack[sp - arity];
			for(int i = arity - 1; i > 0; i--){
				writer.append((IValue) stack[sp - i]);
			}
			return sp - arity + 1;
		}
	},
	setwriter_add {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity > 0;
			ISetWriter writer = (ISetWriter) stack[sp - arity];
			for(int i = arity - 1; i > 0; i--){
				writer.insert((IValue) stack[sp - i]);
			}
			return sp - arity + 1;
		}
	},
	mapwriter_add {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 3;
			IMapWriter writer = (IMapWriter) stack[sp - 3];
			writer.insert(vf.tuple((IValue) stack[sp - 2], (IValue) stack[sp - 1]));
			return sp - 2;
		}
	},
	
	/*
	 * addition
	 *
	 * infix Addition "+"
	 * {  
	 *		&L <: num x &R <: num               -> LUB(&L, &R),

	 *		list[&L] x list[&R]                 -> list[LUB(&L,&R)],
	 *		list[&L] x &R              		  -> list[LUB(&L,&R)] when &R is not a list,	  
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
	
	add {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IValue lhs = ((IValue) stack[sp - 2]);
			IValue rhs = ((IValue) stack[sp - 1]);
			ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
			ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
			switch (lhsType) {
			case INT:
				switch (rhsType) {
				case INT:
					return int_add_int.execute(stack, sp, arity);
				case NUM:
					return int_add_num.execute(stack, sp, arity);
				case REAL:
					return int_add_real.execute(stack, sp, arity);
				case RAT:
					return int_add_rat.execute(stack, sp, arity);
				case LIST:
					return elm_add_list.execute(stack, sp, arity);
				case SET:
					return elm_add_list.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case NUM:
				switch (rhsType) {
				case INT:
					return num_add_int.execute(stack, sp, arity);
				case NUM:
					return num_add_num.execute(stack, sp, arity);
				case REAL:
					return num_add_real.execute(stack, sp, arity);
				case RAT:
					return num_add_rat.execute(stack, sp, arity);
				case LIST:
					return elm_add_list.execute(stack, sp, arity);
				case SET:
					return elm_add_list.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case REAL:
				switch (rhsType) {
				case INT:
					return real_add_int.execute(stack, sp, arity);
				case NUM:
					return real_add_num.execute(stack, sp, arity);
				case REAL:
					return real_add_real.execute(stack, sp, arity);
				case RAT:
					return real_add_rat.execute(stack, sp, arity);
				case LIST:
					return elm_add_list.execute(stack, sp, arity);
				case SET:
					return elm_add_list.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case RAT:
				switch (rhsType) {
				case INT:
					return rat_add_int.execute(stack, sp, arity);
				case NUM:
					return rat_add_num.execute(stack, sp, arity);
				case REAL:
					return rat_add_real.execute(stack, sp, arity);
				case RAT:
					return rat_add_rat.execute(stack, sp, arity);
				case LIST:
					return elm_add_list.execute(stack, sp, arity);
				case SET:
					return elm_add_list.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case SET:
				//			switch (rhsType) {
				//			case SET:
				//				return set_add_set(stack, sp, arity);
				//			case REL:
				//				return set_add_rel(stack, sp, arity);
				//			default:
				return set_add_elm.execute(stack, sp, arity);
				//			}
			case LIST:
				//			switch (rhsType) {
				//			case LIST:
				//				return list_add_list(stack, sp, arity);
				//			case LREL:
				//				return list_add_lrel(stack, sp, arity);
				//			default:
				return list_add_elm.execute(stack, sp, arity);
				//			}
			case LOC:
				switch (rhsType) {
				case STR:
					return loc_add_str.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case LREL:
				switch (rhsType) {
				case LIST:
					return lrel_add_list.execute(stack, sp, arity);
				case LREL:
					return lrel_add_lrel.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case MAP:
				switch (rhsType) {
				case MAP:
					return map_add_map.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case REL:
				switch (rhsType) {
				case SET:
					return rel_add_set.execute(stack, sp, arity);
				case REL:
					return rel_add_rel.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case STR:
				switch (rhsType) {
				case STR:
					return str_add_str.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case TUPLE:
				switch (rhsType) {
				case TUPLE:
					return tuple_add_tuple.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			default:
				switch (rhsType) {
				case SET:
					return elm_add_set.execute(stack, sp, arity);
				case LIST:
					return elm_add_list.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			}
		}
	},
	
	// add on int
	
	int_add_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).add((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	int_add_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).add((INumber) stack[sp - 1]);
			return sp - 1;
		}
	},
	int_add_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).add((IRational) stack[sp - 1]);
			return sp - 1;
		}
	},
	int_add_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).add((IReal) stack[sp - 1]);
			return sp - 1;
		}
	},
	
	// add on num
	
	num_add_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).add((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	num_add_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).add((INumber) stack[sp - 1]);
			return sp - 1;
		}
	},
	num_add_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).add((IRational) stack[sp - 1]);
			return sp - 1;
		}
	},
	num_add_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).add((IReal) stack[sp - 1]);
			return sp - 1;
		}
	},
	
	// add on rat
	
	rat_add_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).add((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	rat_add_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).add((INumber) stack[sp - 1]);
			return sp - 1;
		}
	},
	rat_add_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).add((IRational) stack[sp - 1]);
			return sp - 1;
		}
	},
	rat_add_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).add((IReal) stack[sp - 1]);
			return sp - 1;
		}
	},
	
	// add on real
	
	real_add_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).add((INumber) stack[sp - 1]);
			return sp - 1;
		}
	},
	real_add_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).add((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	real_add_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).add((IReal) stack[sp - 1]);
			return sp - 1;
		}
	},
	real_add_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).add((IRational) stack[sp - 1]);
			return sp - 1;
		}
	},
	
	// Add on non-numeric types
	
	list_add_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IList) stack[sp - 2]).concat((IList) stack[sp - 1]);
			return sp - 1;
		}
	},
	list_add_elm {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IList) stack[sp - 2]).append((IValue) stack[sp - 1]);
			return sp - 1;
		}
	},
	elm_add_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IList) stack[sp - 1]).insert((IValue) stack[sp - 2]);
			return sp - 1;
		}
	},
	list_add_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return list_add_list.execute(stack, sp, arity);
		}
	},
	lrel_add_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return list_add_list.execute(stack, sp, arity);
		}
	},
	lrel_add_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return list_add_list.execute(stack, sp, arity);
		}
	},
	lrel_add_elm {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return list_add_elm.execute(stack, sp, arity);
		}
	},
	loc_add_str {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISourceLocation sloc = (ISourceLocation) stack[sp - 2];
			String s = ((IString) stack[sp - 1]).getValue();

			String path = sloc.hasPath() ? sloc.getPath() : "";
			if(!path.endsWith("/")){
				path = path + "/";
			}
			path = path.concat(s);
			stack[sp - 2 ] = $loc_field_update(sloc, "path", vf.string(path));
			return sp - 1;
		}
	},
	map_add_map {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IMap) stack[sp - 2]).join((IMap) stack[sp - 1]);
			return sp - 1;
		}
	},
	set_add_elm {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((ISet) stack[sp - 2]).insert((IValue) stack[sp - 1]);
			return sp - 1;
		}
	},
	elm_add_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((ISet) stack[sp - 1]).insert((IValue) stack[sp - 2]);
			return sp - 1;
		}
	},
	set_add_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((ISet) stack[sp - 2]).union((ISet) stack[sp - 1]);
			return sp - 1;
		}
	},
	set_add_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return set_add_set.execute(stack, sp, arity);
		}
	},
	rel_add_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((ISet) stack[sp - 2]).union((ISet) stack[sp - 1]);
			return sp - 1;
		}
	},
	rel_add_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return set_add_set.execute(stack, sp, arity);
		}
	},
	str_add_str {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity >= 2;
			if(arity == 2){
				stack[sp - 2] = ((IString) stack[sp - 2]).concat((IString) stack[sp - 1]);
				return sp - 1;
			} else {
				StringWriter w = new StringWriter();
				for(int i = 0; i < arity; i++){
					w.append(((IString)stack[sp - arity + i]).getValue());
				}
				stack[sp - arity] = vf.string(w.toString());
				return sp - arity + 1;
			}
		}		
	},
	
	/*
	 * str_escape_for_regexp
	 */
	
	str_escape_for_regexp {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			IValue v = ((IValue) stack[sp - 1]);
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
			stack[sp - 1] = vf.string(b.toString());
			return sp;
		}
	},
	
	/*
	 * Templates
	 */
	
	template_open {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			String ind = ((IString) stack[sp - 2]).getValue();
			String pre = ((IString) stack[sp - 1]).getValue();
			$indent(ind);
			stack[sp - 2] = vf.string(pre);
			return sp - 1;
		}
	},
	template_addunindented {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity <= 2;
			if(arity == 1){
				stack[sp - 1] = (((IString) stack[sp - 1]));
				return sp;
			}
			stack[sp - 2] = ((IString) stack[sp - 2]).concat(((IString) stack[sp - 1]));
			return sp - 1;
		}
	},
	template_add {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity >= 2;
			IString template = (IString) stack[sp - arity];
			String indent = $getCurrentIndent();
			for(int i = 1; i < arity; i++){
				IString arg_s = (IString) stack[sp - arity + i];
				String [] lines = $removeMargins(arg_s.getValue()).split("\n");
				if(lines.length <= 1){
					template = template.concat(arg_s);
				} else {
					StringBuilder sb = new StringBuilder();
					sb.append(lines[0]);
					for(int j = 1; j < lines.length; j++){
						sb.append("\n").append(indent).append(lines[j]);
					}
					String res = sb.toString();
					template = template.concat(vf.string(res));
				}
			}
			stack[sp - arity] = template;
			return sp - arity + 1;
		}
	},
	template_close {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			$unindent();
			return sp;
		}
	},
	tuple_add_tuple {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ITuple t1 = (ITuple) stack[sp - 2];
			ITuple t2 = (ITuple) stack[sp - 1];
			int len1 = t1.arity();
			int len2 = t2.arity();
			IValue elems[] = new IValue[len1 + len2];
			for(int i = 0; i < len1; i++)
				elems[i] = t1.get(i);
			for(int i = 0; i < len2; i++)
				elems[len1 + i] = t2.get(i);
			stack[sp - 2] = vf.tuple(elems);
			return sp - 1;
		}
	},
	
	/*
	 * composition
	 * infix Composition "o" {
	 * 	lrel[&A,&B] x lrel[&B,&C] -> lrel[&A,&C],
 	 * 	rel[&A,&B] x rel[&B,&C] -> rel[&A,&C],
 	 * 	map[&A,&B] x map[&B,&C] -> map[&A,&C]
	 * }
	 */
	
	compose {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;

			IValue left = (IValue) stack[sp - 2];
			Type leftType = left.getType();
			switch (ToplevelType.getToplevelType(leftType)) {
			case LREL:
				return lrel_compose_lrel.execute(stack, sp, arity);
			case REL:
				return rel_compose_rel.execute(stack, sp, arity);
			case MAP:
				return map_compose_map.execute(stack, sp, arity);
			default:
				throw new RuntimeException("compose: unexpected type " + leftType);
			}
		}
	},
	lrel_compose_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;

			IListRelation<IList> left = ((IList) stack[sp - 2]).asRelation();
			IListRelation<IList> right = ((IList) stack[sp - 1]).asRelation();
			stack[sp - 2] = left.compose(right);
			return sp - 1;
		}
	},
	rel_compose_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISetRelation<ISet> left = ((ISet) stack[sp - 2]).asRelation();
			ISetRelation<ISet> right = ((ISet) stack[sp - 1]).asRelation();
			stack[sp - 2] = left.compose(right);
			return sp - 1;
		}
	},
	map_compose_map {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IMap) stack[sp - 2]).compose((IMap) stack[sp - 1]);
			return sp - 1;
		}
	},
	
	/*
	 * mod
	 */
	
	mod {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			IValue lhs = ((IValue) stack[sp - 2]);
			IValue rhs = ((IValue) stack[sp - 1]);
			if(lhs.getType().isInteger() && rhs.getType().isInteger()){
				return int_mod_int.execute(stack, sp, arity);
			}
			throw new RuntimeException("mod: unexpected type combination" + lhs.getType() + " and " + rhs.getType());
		}
	},
	int_mod_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).mod((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	
	/*
	 * division
	 * 
	 * infix Division "/" { &L <: num x &R <: num        -> LUB(&L, &R) }
	 */
	
	// Generic divide
	
	divide {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IValue lhs = ((IValue) stack[sp - 2]);
			IValue rhs = ((IValue) stack[sp - 1]);
			ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
			ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
			switch (lhsType) {
			case INT:
				switch (rhsType) {
				case INT:
					return int_divide_int.execute(stack, sp, arity);
				case NUM:
					return int_divide_num.execute(stack, sp, arity);
				case REAL:
					return int_divide_real.execute(stack, sp, arity);
				case RAT:
					return int_divide_rat.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case NUM:
				switch (rhsType) {
				case INT:
					return num_divide_int.execute(stack, sp, arity);
				case NUM:
					return num_divide_num.execute(stack, sp, arity);
				case REAL:
					return num_divide_real.execute(stack, sp, arity);
				case RAT:
					return num_divide_rat.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case REAL:
				switch (rhsType) {
				case INT:
					return real_divide_int.execute(stack, sp, arity);
				case NUM:
					return real_divide_num.execute(stack, sp, arity);
				case REAL:
					return real_divide_real.execute(stack, sp, arity);
				case RAT:
					return real_divide_rat.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case RAT:
				switch (rhsType) {
				case INT:
					return rat_divide_int.execute(stack, sp, arity);
				case NUM:
					return rat_divide_num.execute(stack, sp, arity);
				case REAL:
					return rat_divide_real.execute(stack, sp, arity);
				case RAT:
					return rat_divide_rat.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			default:
				throw new RuntimeException("Illegal type combination: "
						+ lhsType + " and " + rhsType);
			}
		}
	},
	
	// divide on int
	
	int_divide_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((IInteger) stack[sp - 2]).divide((IInteger) stack[sp - 1]);
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	int_divide_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((IInteger) stack[sp - 2]).divide((INumber) stack[sp - 1], vf.getPrecision());
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	int_divide_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((IInteger) stack[sp - 2]).divide((IRational) stack[sp - 1]);
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	int_divide_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((IInteger) stack[sp - 2]).divide((IReal) stack[sp - 1], vf.getPrecision());
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	
	// divide on num
	
	num_divide_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((INumber) stack[sp - 2]).divide((IInteger) stack[sp - 1], vf.getPrecision());
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	num_divide_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((INumber) stack[sp - 2]).divide((INumber) stack[sp - 1], vf.getPrecision());
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	num_divide_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((INumber) stack[sp - 2]).divide((IRational) stack[sp - 1], vf.getPrecision());
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	num_divide_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((INumber) stack[sp - 2]).divide((IReal) stack[sp - 1], vf.getPrecision());
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	
	// divide on rat
	
	rat_divide_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((IRational) stack[sp - 2]).divide((IInteger) stack[sp - 1]);
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	rat_divide_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((IRational) stack[sp - 2]).divide((INumber) stack[sp - 1], vf.getPrecision());
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	rat_divide_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((IRational) stack[sp - 2]).divide((IRational) stack[sp - 1]);
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	rat_divide_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((IRational) stack[sp - 2]).divide((IReal) stack[sp - 1], vf.getPrecision());
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	
	// divide on real
	
	real_divide_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((IReal) stack[sp - 2]).divide((INumber) stack[sp - 1], vf.getPrecision());
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	real_divide_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((IReal) stack[sp - 2]).divide((IInteger) stack[sp - 1], vf.getPrecision());
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	real_divide_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((IReal) stack[sp - 2]).divide((IReal) stack[sp - 1], vf.getPrecision());
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	real_divide_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			try {
				stack[sp - 2] = ((IReal) stack[sp - 2]).divide((IRational) stack[sp - 1], vf.getPrecision());
				return sp - 1;
			} catch(ArithmeticException e) {
				throw RuntimeExceptions.arithmeticException("/ by zero", null, new ArrayList<Frame>());
			}
		}
	},
	
	/*
	 * ...writer_close
	 */

	listwriter_close {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			IListWriter writer = (IListWriter) stack[sp - 1];
			stack[sp - 1] = writer.done();
			return sp;
		}

	},
	setwriter_close {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			ISetWriter writer = (ISetWriter) stack[sp - 1];
			stack[sp - 1] = writer.done();
			return sp;
		}

	},
	mapwriter_close {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			IMapWriter writer = (IMapWriter) stack[sp - 1];
			stack[sp - 1] = writer.done();
			return sp;
		}
	},
	
	/*
	 * equal
	 */
	
	// equal on int
	
	int_equal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).equal((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_equal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).equal((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_equal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).equal((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_equal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).equal((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// equal on num
	
	num_equal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).equal((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_equal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).equal((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_equal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).equal((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_equal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).equal((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// equal on real
	
	real_equal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).equal((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_equal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).equal((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_equal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).equal((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_equal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).equal((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// equal on rat
	
	rat_equal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).equal((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_equal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).equal((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_equal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).equal((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_equal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).equal((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// equal on other types
	
	equal {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IValue left = (stack[sp - 2] instanceof Boolean) ? vf.bool((Boolean)stack[sp - 2]) : (IValue)stack[sp - 2];
			IValue right = (stack[sp - 1] instanceof Boolean) ? vf.bool((Boolean)stack[sp - 1]) : (IValue)stack[sp - 1];
			if(left.getType().isNumber() && right.getType().isNumber()){
				return num_equal_num.execute(stack, sp, arity);
			} else {
				stack[sp - 2] = left.isEqual(right);
				return sp - 1;
			}
		}
	},
	type_equal_type {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = vf.bool(((Type) stack[sp - 2]) == ((Type) stack[sp - 1]));
			return sp - 1;
		}		
	},
	
	/*
	 * ..._field_access
	 */
	
	adt_field_access {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IConstructor cons = (IConstructor) stack[sp - 2];
			String fieldName = ((IString) stack[sp - 1]).getValue();
			Type tp = cons.getConstructorType();
			int fld_index = tp.getFieldIndex(fieldName);
			stack[sp - 2] = cons.get(fld_index);
			return sp - 1;
		}
	},
	datetime_field_access {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IDateTime dt = ((IDateTime) stack[sp - 2]);
			String field = ((IString) stack[sp - 1]).getValue();
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
					throw new UnsupportedOperation("Can not retrieve the century on a time value", null);
				case "year":
					if (!dt.isTime()) {
						v = vf.integer(dt.getYear());
						break;
					}
					throw new UnsupportedOperation("Can not retrieve the year on a time value", null);

				case "month":
					if (!dt.isTime()) {
						v = vf.integer(dt.getMonthOfYear());
						break;
					}
					throw new UnsupportedOperation("Can not retrieve the month on a time value", null);
				case "day":
					if (!dt.isTime()) {
						v = vf.integer(dt.getDayOfMonth());
						break;
					}
					throw new UnsupportedOperation("Can not retrieve the day on a time value", null);
				case "hour":
					if (!dt.isDate()) {
						v = vf.integer(dt.getHourOfDay());
						break;
					}
					throw new UnsupportedOperation("Can not retrieve the hour on a date value", null);
				case "minute":
					if (!dt.isDate()) {
						v = vf.integer(dt.getMinuteOfHour());
						break;
					}
					throw new UnsupportedOperation("Can not retrieve the minute on a date value", null);
				case "second":
					if (!dt.isDate()) {
						v = vf.integer(dt.getSecondOfMinute());
						break;
					}
					throw new UnsupportedOperation("Can not retrieve the second on a date value", null);
				case "millisecond":
					if (!dt.isDate()) {
						v = vf.integer(dt.getMillisecondsOfSecond());
						break;
					}
					throw new UnsupportedOperation("Can not retrieve the millisecond on a date value", null);
				case "timezoneOffsetHours":
					if (!dt.isDate()) {
						v = vf.integer(dt.getTimezoneOffsetHours());
						break;
					}
					throw new UnsupportedOperation("Can not retrieve the timezone offset hours on a date value", null);
				case "timezoneOffsetMinutes":
					if (!dt.isDate()) {
						v = vf.integer(dt.getTimezoneOffsetMinutes());
						break;
					}
					throw new UnsupportedOperation("Can not retrieve the timezone offset minutes on a date value", null);

				case "justDate":
					if (!dt.isTime()) {
						v = vf.date(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
						break;
					}
					throw new UnsupportedOperation("Can not retrieve the date component of a time value", null);
				case "justTime":
					if (!dt.isDate()) {
						v = vf.time(dt.getHourOfDay(), dt.getMinuteOfHour(), dt.getSecondOfMinute(), 
								dt.getMillisecondsOfSecond(), dt.getTimezoneOffsetHours(),
								dt.getTimezoneOffsetMinutes());
						break;
					}
					throw new UnsupportedOperation("Can not retrieve the time component of a date value", null);
				default:
					throw RuntimeExceptions.noSuchField(field, null,  new ArrayList<Frame>());
				}
				stack[sp - 2] = v;
				return sp - 1;

			} catch (InvalidDateTimeException e) {
				throw RuntimeExceptions.illegalArgument(dt, null, new ArrayList<Frame>(), e.getMessage());
			}
		}

		/* TODO: In both implementations UnsupportedOperation is thrown. 
		 * I guess this has to be replaced by something better, e.g., UnavailableInformation
		 */
	},
	datetime_field_update {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
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
						throw new UnsupportedOperation("Can not update the year on a time value", null);
					}
					year = ((IInteger)repl).intValue();
					break;

				case "month":
					if (dt.isTime()) {
						throw new UnsupportedOperation("Can not update the month on a time value", null);
					}
					month = ((IInteger)repl).intValue();
					break;

				case "day":
					if (dt.isTime()) {
						throw new UnsupportedOperation("Can not update the day on a time value", null);
					}	
					day = ((IInteger)repl).intValue();
					break;

				case "hour":
					if (dt.isDate()) {
						throw new UnsupportedOperation("Can not update the hour on a date value", null);
					}	
					hour = ((IInteger)repl).intValue();
					break;

				case "minute":
					if (dt.isDate()) {
						throw new UnsupportedOperation("Can not update the minute on a date value", null);
					}
					minute = ((IInteger)repl).intValue();
					break;

				case "second":
					if (dt.isDate()) {
						throw new UnsupportedOperation("Can not update the second on a date value", null);
					}
					second = ((IInteger)repl).intValue();
					break;

				case "millisecond":
					if (dt.isDate()) {
						throw new UnsupportedOperation("Can not update the millisecond on a date value", null);
					}
					milli = ((IInteger)repl).intValue();
					break;

				case "timezoneOffsetHours":
					if (dt.isDate()) {
						throw new UnsupportedOperation("Can not update the timezone offset hours on a date value", null);
					}
					tzOffsetHour = ((IInteger)repl).intValue();
					break;

				case "timezoneOffsetMinutes":
					if (dt.isDate()) {
						throw new UnsupportedOperation("Can not update the timezone offset minutes on a date value", null);
					}
					tzOffsetMin = ((IInteger)repl).intValue();
					break;			

				default:
					throw RuntimeExceptions.noSuchField(field, null,  new ArrayList<Frame>());
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
				throw RuntimeExceptions.illegalArgument(repl, null, null, "Cannot update field " + field + ", this would generate an invalid datetime value");
			}
			catch (InvalidDateTimeException e) {
				throw RuntimeExceptions.illegalArgument(dt, null, new ArrayList<Frame>(), e.getMessage());
			}
		}
	},
	loc_field_access {
		@SuppressWarnings("deprecation")
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISourceLocation sloc = ((ISourceLocation) stack[sp - 2]);
			String field = ((IString) stack[sp - 1]).getValue();
			URI uri;
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
				uri = sloc.getURI();
				if (!rvm.ctx.getResolverRegistry().supportsHost(uri)) {
					throw RuntimeExceptions.noSuchField("The scheme " + uri.getScheme() + " does not support the host field, use authority instead.", null,  new ArrayList<Frame>());
				}
				s = uri.getHost();
				v = vf.string(s == null ? "" : s);
				break;

			case "path":
				v = vf.string(sloc.hasPath() ? sloc.getPath() : "");
				break;

			case "parent":
				String path = sloc.getPath();
				if (path.equals("")) {
					throw RuntimeExceptions.noParent(sloc, null, null);
				}
				int i = path.lastIndexOf("/");

				if (i != -1) {
					path = path.substring(0, i);
					v = $loc_field_update(sloc, "path", vf.string(path));
				} else {
					throw RuntimeExceptions.noParent(sloc, null, null);
				}
				break;	

			case "file": 
				path = sloc.hasPath() ? sloc.getPath() : "";

				if (path.equals("")) {
					throw RuntimeExceptions.noParent(sloc,null,null);
				}
				i = path.lastIndexOf("/");

				if (i != -1) {
					path = path.substring(i+1);
				}
				v = vf.string(path);	
				break;

			case "ls":
				try {
					ISourceLocation resolved = rvm.ctx.getHeap().resolveSourceLocation(sloc);
					IListWriter w = vf.listWriter();

					Object[] fakeStack = new Object[2];
					for (String elem : rvm.ctx.getResolverRegistry().listEntries(resolved.getURI())) {
						fakeStack[0] = resolved;	// TODO
						fakeStack[1] = vf.string(elem);
						loc_add_str.execute(fakeStack, 2, 2);
						w.append((ISourceLocation)fakeStack[0]);
					}

					v = w.done();
					break;
				} catch (IOException e) {
					throw RuntimeExceptions.io(vf.string(e.getMessage()), null,null);
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
				uri = sloc.getURI();
				if (!rvm.ctx.getResolverRegistry().supportsHost(uri)) {
					throw RuntimeExceptions.noSuchField("The scheme " + uri.getScheme() + " does not support the user field, use authority instead.", null,  new ArrayList<Frame>());
				}
				s = uri.getUserInfo();
				v = vf.string(s == null ? "" : s);
				break;

			case "port":
				uri = sloc.getURI();
				if (!rvm.ctx.getResolverRegistry().supportsHost(uri)) {
					throw RuntimeExceptions.noSuchField("The scheme " + uri.getScheme() + " does not support the port field, use authority instead.", null,  new ArrayList<Frame>());
				}
				int n = uri.getPort();
				v = vf.integer(n);
				break;	

			case "length":
				if(sloc.hasOffsetLength()){
					v = vf.integer(sloc.getLength());
					break;
				} else {
					throw RuntimeExceptions.unavailableInformation(sloc, new ArrayList<Frame>());
				}

			case "offset":
				if(sloc.hasOffsetLength()){
					v = vf.integer(sloc.getOffset());
					break;
				} else {
					throw RuntimeExceptions.unavailableInformation(sloc, new ArrayList<Frame>());
				}

			case "begin":
				if(sloc.hasLineColumn()){
					v = vf.tuple(lineColumnType, vf.integer(sloc.getBeginLine()), vf.integer(sloc.getBeginColumn()));
					break;
				} else {
					throw RuntimeExceptions.unavailableInformation(sloc, new ArrayList<Frame>());
				}
			case "end":
				if(sloc.hasLineColumn()){
					v = vf.tuple(lineColumnType, vf.integer(sloc.getEndLine()), vf.integer(sloc.getEndColumn()));
					break;
				} else {
					throw RuntimeExceptions.unavailableInformation(sloc, new ArrayList<Frame>());
				}

			case "uri":
				v = vf.string(sloc.getURI().toString());
				break;

			case "top":
				v = vf.sourceLocation(sloc.getURI());
				break;

			default:
				throw new RuntimeException("Access to non-existing field " + field + " in location");
			}

			stack[sp - 2] = v;
			return sp - 1;
		}
	},
	loc_field_update {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 3;
			ISourceLocation sloc = ((ISourceLocation) stack[sp - 3]);
			String field = ((IString) stack[sp - 2]).getValue();
			IValue repl = (IValue) stack[sp - 1];
			stack[sp - 3] = $loc_field_update(sloc, field, repl);
			return sp - 2;
		}
	},
	lrel_field_access {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IListRelation<IList> left = ((IList) stack[sp - 2]).asRelation();
			stack[sp - 2] = left.projectByFieldNames(((IString) stack[sp - 1]).getValue());
			return sp - 1;
		}
	},
	rel_field_access {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISetRelation<ISet> left = ((ISet) stack[sp - 2]).asRelation();
			stack[sp - 2] = left.projectByFieldNames(((IString) stack[sp - 1]).getValue());
			return sp - 1;
		}
	},
	reified_field_access {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IConstructor reified = (IConstructor) stack[sp - 2];
			String field = ((IString) stack[sp - 1]).getValue();
			stack[sp - 2] = reified.get(field);
			return sp - 1;
		}
	},
	nonterminal_field_access {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IConstructor appl = (IConstructor) stack[sp - 2];
			IList appl_args = (IList) appl.get("args");
			IConstructor prod = (IConstructor) appl.get("prod");
			IList prod_symbols = (IList) prod.get("symbols");
			IString field = ((IString) stack[sp - 1]);

			for(int i = 0; i < prod_symbols.length(); i++){
				IConstructor arg = (IConstructor) prod_symbols.get(i);
				if(arg.getName().equals("label")){
					if(((IString) arg.get(0)).equals(field)){
						stack[sp - 2] = appl_args.get(i);
						return sp - 1;
					}
				}
			}
			throw RuntimeExceptions.noSuchField(field.getValue(), null, new ArrayList<Frame>());
		}
	},
	
	/*
	 * Annotations
	 */

	annotation_get {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IValue val = (IValue) stack[sp - 2];
			String label = ((IString) stack[sp - 1]).getValue();
			try {
				stack[sp - 2] = val.asAnnotatable().getAnnotation(label);
				if(stack[sp - 2] == null) {
					throw RuntimeExceptions.noSuchAnnotation(label, null, new ArrayList<Frame>());
				}
				return sp - 1;
			} catch (FactTypeUseException e) {
				throw RuntimeExceptions.noSuchAnnotation(label, null, new ArrayList<Frame>());
			}
		}
	},
	annotation_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 3;
			IValue val = (IValue) stack[sp - 3];
			String label = ((IString) stack[sp - 2]).getValue();
			IValue repl = (IValue) stack[sp - 1];
			stack[sp - 3] = val.asAnnotatable().setAnnotation(label, repl);
			return sp - 2;
		}
	},
	tuple_field_access {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((ITuple) stack[sp - 2]).get(((IString) stack[sp - 1]).getValue());
			return sp - 1;
		}
	},
	
	/*
	 * ..._field_update
	 */
	
	tuple_field_update {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 3;
			stack[sp - 3] = ((ITuple) stack[sp - 3]).set(((IString) stack[sp - 2]).getValue(), (IValue) stack[sp - 1]);
			return sp - 2;
		}
	},
	tuple_field_project {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
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
	adt_field_update {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 3;
			stack[sp - 3] = ((IConstructor) stack[sp - 3]).set(((IString) stack[sp - 2]).getValue(),
					(IValue) stack[sp -1]);
			return sp - 2;
		}
	},
	
	/*
	 * ..._field_project
	 */
	
	rel_field_project {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity >= 2;
			ISet rel = (ISet) stack[sp - arity];
			int indexArity = arity - 1;
			int[] fields = new int[arity - 1];
			for(int i = 1; i < arity; i++){
				fields[i - 1] = ((IInteger)stack[sp - arity + i]).intValue();
			}
			ISetWriter w = vf.setWriter();
			IValue[] elems = new IValue[arity - 1];
			for(IValue vtup : rel){
				ITuple tup = (ITuple) vtup;
				for(int j = 0; j < fields.length; j++){
					elems[j] = tup.get(fields[j]);
				}
				w.insert((indexArity > 1) ? vf.tuple(elems) : elems[0]);
			}
			stack[sp - arity] = w.done();
			return sp - arity + 1;
		}
	},
	lrel_field_project {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
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
	map_field_project {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
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
	
	// greater on int
	
	int_greater_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_greater_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_greater_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_greater_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// greater on num
	
	num_greater_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).greater((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_greater_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).greater((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_greater_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).greater((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_greater_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).greater((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// greater on rat
	
	rat_greater_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).greater((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_greater_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).greater((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_greater_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).greater((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_greater_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).greater((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// greater on real
	
	real_greater_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).greater((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_greater_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).greater((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_greater_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).greater((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_greater_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).greater((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// greater on other types
	
	greater {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			int spnew = lessequal.execute(stack, sp, arity);
			stack[sp - 2] = ! (Boolean) stack[sp - 2];
			return spnew;
		}
	},
	adt_greater_adt {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			return node_greater_node.execute(stack, sp, arity);
		}
	},
	bool_greater_bool {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			boolean left = (stack[sp - 2] instanceof Boolean) ? (Boolean) stack[sp - 2] : ((IBool) stack[sp - 2]).getValue();
			boolean right = (stack[sp - 1] instanceof Boolean) ? (Boolean) stack[sp - 1] : ((IBool) stack[sp - 1]).getValue();

			stack[sp - 2] = left && !right;
			return sp - 1;
		}
	},
	datetime_greater_datetime {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IDateTime) stack[sp - 2]).compareTo((IDateTime) stack[sp - 1]) == 1;
			return sp - 1;
		}
	},
	list_greater_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			int spnew = list_lessequal_list.execute(stack, sp, arity);
			stack[sp - 2] = ! (Boolean) stack[sp - 2];
			return spnew;
		}
	},
	lrel_greater_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			int spnew = list_lessequal_list.execute(stack, sp, arity);
			stack[sp - 2] = ! (Boolean) stack[sp - 2];
			return spnew;
		}
	},
	loc_greater_loc {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			int spnew = loc_lessequal_loc.execute(stack, sp, arity);
			stack[sp - 2] = ! (Boolean) stack[sp - 2];
			return spnew;
		}
	},
	map_greater_map {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IMap left = (IMap) stack[sp - 2];
			IMap right = (IMap) stack[sp - 1];

			stack[sp - 2] = right.isSubMap(left) && !left.isSubMap(right);
			return sp - 1;
		}
	},
	node_greater_node {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			int newsp = node_lessequal_node.execute(stack, sp, arity);
			stack[newsp - 1] = !(Boolean)stack[newsp - 1];
			return newsp;
		}
	},
	set_greater_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((ISet) stack[sp - 1]).isSubsetOf((ISet) stack[sp - 2]);
			return sp - 1;
		}
	},
	rel_greater_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((ISet) stack[sp - 1]).isSubsetOf((ISet) stack[sp - 2]);
			return sp - 1;
		}
	},
	str_greater_str {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IString) stack[sp - 2]).compare((IString) stack[sp - 1]) == 1;
			return sp - 1;
		}
	},
	tuple_greater_tuple {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			int spnew = tuple_lessequal_tuple.execute(stack, sp, arity);
			stack[sp - 2] = ! (Boolean) stack[sp - 2];
			return spnew;
		}
	},
	
	// greaterequal on int
	
	int_greaterequal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).greaterEqual((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_greaterequal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).greaterEqual((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_greaterequal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).greaterEqual((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_greaterequal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).greaterEqual((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// greaterequal on num
	
	num_greaterequal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).greaterEqual((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_greaterequal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).greaterEqual((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_greaterequal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).greaterEqual((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_greaterequal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).greaterEqual((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// greaterequal on rat
	
	rat_greaterequal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).greaterEqual((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_greaterequal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).greaterEqual((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_greaterequal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).greaterEqual((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_greaterequal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).greaterEqual((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// greaterequal on real
	
	real_greaterequal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).greaterEqual((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_greaterequal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).greaterEqual((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_greaterequal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).greaterEqual((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_greaterequal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).greaterEqual((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// greaterequal on other types
	
	greaterequal {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			int spnew = less.execute(stack, sp, arity);
			stack[sp - 2] = ! (Boolean) stack[sp - 2];
			return spnew;
		}
	},
	adt_greaterequal_adt {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			return node_greaterequal_node.execute(stack, sp, arity);
		}
	},
	bool_greaterequal_bool {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			boolean left = (stack[sp - 2] instanceof Boolean) ? (Boolean) stack[sp - 2] : ((IBool) stack[sp - 2]).getValue();
			boolean right = (stack[sp - 1] instanceof Boolean) ? (Boolean) stack[sp - 1] : ((IBool) stack[sp - 1]).getValue();
			stack[sp - 2] = (left && !right) || (left == right);
			return sp - 1;
		}
	},
	datetime_greaterequal_datetime {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IDateTime) stack[sp - 2]).compareTo((IDateTime) stack[sp - 1]) == 1;
			return sp - 1;
		}
	},
	list_greaterequal_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			int spnew = list_less_list.execute(stack, sp, arity);
			stack[sp - 2] = !(Boolean)stack[sp - 2];
			return spnew;
		}
	},
	lrel_greaterequal_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			int spnew = list_less_list.execute(stack, sp, arity);
			stack[sp - 2] = !(Boolean)stack[sp - 2];
			return spnew;
		}
	},
	loc_greaterequal_loc {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			int spnew = loc_less_loc.execute(stack, sp, arity);
			stack[sp - 2] = !(Boolean)stack[sp - 2];
			return spnew;
		}
	},
	node_greaterequal_node {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			int newsp = node_less_node.execute(stack, sp, arity);
			stack[newsp - 1] = !(Boolean)stack[newsp - 1];
			return newsp;
		}
	},
	map_greaterequal_map {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IMap left = (IMap) stack[sp - 2];
			IMap right = (IMap) stack[sp - 1];
			stack[sp - 2] = right.isSubMap(left);
			return sp - 1;
		}
	},
	set_greaterequal_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISet left = (ISet) stack[sp - 2];
			ISet right = (ISet) stack[sp - 1];
			stack[sp - 2] = left.isEqual(right) || right.isSubsetOf(left);
			return sp - 1;
		}
	},
	rel_greaterequal_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISet left = (ISet) stack[sp - 2];
			ISet right = (ISet) stack[sp - 1];
			stack[sp - 2] = left.isEqual(right) || right.isSubsetOf(left);
			return sp - 1;
		}
	},
	str_greaterequal_str {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			int c = ((IString) stack[sp - 2]).compare((IString) stack[sp - 1]);
			stack[sp - 2] = c == 0 || c == 1;
			return sp - 1;
		}
	},
	tuple_greaterequal_tuple {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			int spnew = tuple_less_tuple.execute(stack, sp, arity);
			stack[sp - 2] = !(Boolean)stack[sp - 2];
			return spnew;
		}
	},
	
	/*
	 * intersect
	 * 
	 * infix intersect "&" {
	 *		list[&L] x list[&R]                  -> list[LUB(&L,&R)],
	 *		set[&L] x set[&R]                    -> set[LUB(&L,&R)],
	 * 		map[&K1,&V1] x map[&K2,&V2]          -> map[LUB(&K1,&K2), LUB(&V1,&V2)]
	 * } 
	 */
	
	intersect {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;

			IValue left = (IValue) stack[sp - 2];
			Type leftType = left.getType();
			IValue right = (IValue) stack[sp - 2];
			Type rightType = right.getType();

			switch (ToplevelType.getToplevelType(leftType)) {
			case LIST:
				switch (ToplevelType.getToplevelType(rightType)) {
				case LIST:
					return list_intersect_list.execute(stack, sp, arity);
				case LREL:
					return list_intersect_lrel.execute(stack, sp, arity);
				default:
					throw new RuntimeException("intersect: illegal combination "
							+ leftType + " and " + rightType);
				}
			case SET:
				switch (ToplevelType.getToplevelType(rightType)) {
				case SET:
					return set_intersect_set.execute(stack, sp, arity);
				case REL:
					return set_intersect_rel.execute(stack, sp, arity);
				default:
					throw new RuntimeException("intersect: illegal combination "
							+ leftType + " and " + rightType);
				}
			case MAP:
				return map_intersect_map.execute(stack, sp, arity);

			default:
				throw new RuntimeException("intersect: illegal combination "
						+ leftType + " and " + rightType);
			}
		}
	},
	list_intersect_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IList) stack[sp - 2]).intersect((IList) stack[sp - 1]);
			return sp - 1;
		}
	},
	list_intersect_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return list_intersect_list.execute(stack, sp, arity);
		}
	},
	lrel_intersect_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return list_intersect_list.execute(stack, sp, arity);
		}
	},
	lrel_intersect_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return list_intersect_list.execute(stack, sp, arity);
		}
	},
	map_intersect_map {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IMap) stack[sp - 2]).common((IMap) stack[sp - 1]);
			return sp - 1;
		}
	},
	rel_intersect_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return set_intersect_set.execute(stack, sp, arity);
		}
	},
	rel_intersect_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return set_intersect_set.execute(stack, sp, arity);
		}
	},
	set_intersect_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((ISet) stack[sp - 2]).intersect((ISet) stack[sp - 1]);
			return sp - 1;
		}
	},
	set_intersect_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return set_intersect_set.execute(stack, sp, arity);
		}
	},
	
	/*
	 * in
	 */
	
	// Generic in
	
	in {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;

			IValue left = (IValue) stack[sp - 2];
			Type leftType = left.getType();
			IValue right = (IValue) stack[sp - 2];
			Type rightType = right.getType();

			switch (ToplevelType.getToplevelType(leftType)) {
			case LIST:
				return elm_in_list.execute(stack, sp, arity);
			case LREL:
				return elm_in_lrel.execute(stack, sp, arity);
			case SET:
				return elm_in_set.execute(stack, sp, arity);
			case REL:
				return elm_in_rel.execute(stack, sp, arity);
			case MAP:
				return elm_in_map.execute(stack, sp, arity);
			default:
				throw new RuntimeException("in: illegal combination " + leftType
						+ " and " + rightType);
			}
		}
	},
	
	// elm_in_...: in for specific types
	
	elm_in_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IList) stack[sp - 1]).contains((IValue) stack[sp - 2]);
			return sp - 1;
		}

	},
	elm_in_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return elm_in_list.execute(stack, sp, arity);
		}

	},
	elm_in_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((ISet) stack[sp - 1]).contains((IValue) stack[sp - 2]);
			return sp - 1;
		}

	},
	elm_in_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return elm_in_set.execute(stack, sp, arity);
		}

	},
	elm_in_map {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IMap) stack[sp - 1]).containsKey((IValue) stack[sp - 2]);
			return sp - 1;
		}
	},
	
	/*
	 * is
	 * 
	 */
	
	// Generic is
	
	is {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IValue val  = (IValue) stack[sp - 2];
			Type tp = val.getType();
			String name = ((IString) stack[sp - 1]).getValue();
			stack[sp - 2] = false;
			if(tp.isAbstractData()){
				if(tp.getName().equals("Tree")){
					IConstructor cons = (IConstructor) val;
					if(cons.getName().equals("appl")){
						IConstructor prod = (IConstructor) cons.get(0);
						IConstructor def = (IConstructor) prod.get(0);
						if(def.getName().equals("label")){
							stack[sp - 2] = ((IString) def.get(0)).getValue().equals(name);
						}
					}
				} else {
					stack[sp - 2] = ((IConstructor)val).getConstructorType().getName().equals(name);
				}
			} else if(tp.isNode()){
				stack[sp - 2] = ((INode) val).getName().equals(name);
			} 
			return sp - 1;
		}
	},
	
	/*
	 * is_...: check the type of an IValue
	 */
	
	is_bool {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isBool());
			return sp;
		}

	},
	is_datetime {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isDateTime());
			return sp;
		}
	},
	is_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isInteger());
			return sp;
		}

	},
	is_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isList());
			return sp;
		}

	},
	is_loc {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isSourceLocation());
			return sp;
		}

	},
	is_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isListRelation());
			return sp;
		}

	},
	is_map {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isMap());
			return sp;
		}

	},
	is_node {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isNode());
			return sp;
		}

	},
	is_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isNumber());
			return sp;
		}

	},
	is_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isRational());
			return sp;
		}

	},
	is_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isReal());
			return sp;
		}

	},
	is_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isRelation());
			return sp;
		}

	},
	is_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isSet());
			return sp;
		}

	},
	is_str {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isString());
			return sp;
		}

	},
	is_tuple {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isTuple());
			return sp;
		}	
	},
	
	/*
	 * join
	 */
	
	// Generic join
	
	join {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;

			IValue left = (IValue) stack[sp - 2];
			Type leftType = left.getType();
			IValue right = (IValue) stack[sp - 2];
			Type rightType = right.getType();

			switch (ToplevelType.getToplevelType(leftType)) {
			case LIST:
				switch (ToplevelType.getToplevelType(rightType)) {
				case LIST:
					return list_join_list.execute(stack, sp, arity);
				case LREL:
					return list_join_lrel.execute(stack, sp, arity);
				default:
					throw new RuntimeException("join: illegal combination "
							+ leftType + " and " + rightType);
				}
			case LREL:
				switch (ToplevelType.getToplevelType(rightType)) {
				case LIST:
					return lrel_join_list.execute(stack, sp, arity);
				case LREL:
					return lrel_join_lrel.execute(stack, sp, arity);
				default:
					throw new RuntimeException("join: illegal combination "
							+ leftType + " and " + rightType);
				}
			case SET:
				switch (ToplevelType.getToplevelType(rightType)) {
				case SET:
					return set_join_set.execute(stack, sp, arity);
				case REL:
					return set_join_rel.execute(stack, sp, arity);
				default:
					throw new RuntimeException("join: illegal combination "
							+ leftType + " and " + rightType);
				}

			case REL:
				switch (ToplevelType.getToplevelType(rightType)) {
				case SET:
					return rel_join_set.execute(stack, sp, arity);
				case REL:
					return rel_join_rel.execute(stack, sp, arity);
				default:
					throw new RuntimeException("join: illegal combination "
							+ leftType + " and " + rightType);
				}

			default:
				throw new RuntimeException("join: illegal combination "
						+ leftType + " and " + rightType);
			}
		}
	},
	
	// join for specific types
	
	list_join_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return list_product_list.execute(stack, sp, arity);
		}
	},
	list_join_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IList left = (IList) stack[sp - 2];
			IList right = (IList) stack[sp - 1];
			if(left.length() == 0){
				stack[sp - 2] = left;
				return sp -1;
			}
			if(right.length() == 0){
				stack[sp - 2] = right;
				return sp -1;
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
			stack[sp - 2] = w.done();
			return sp - 1;
		}
	},
	lrel_join_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IList left = (IList) stack[sp - 2];
			IList right = (IList) stack[sp - 1];
			if(left.length() == 0){
				stack[sp - 2] = left;
				return sp -1;
			}
			if(right.length() == 0){
				stack[sp - 2] = right;
				return sp -1;
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
			stack[sp - 2] = w.done();
			return sp - 1;
		}
	},
	lrel_join_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IList left = (IList) stack[sp - 2];
			IList right = (IList) stack[sp - 1];
			if(left.length() == 0){
				stack[sp - 2] = left;
				return sp -1;
			}
			if(right.length() == 0){
				stack[sp - 2] = right;
				return sp -1;
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
			stack[sp - 2] = w.done();
			return sp - 1;
		}
	},
	set_join_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return set_product_set.execute(stack, sp, arity);
		}
	},
	set_join_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISet left = (ISet) stack[sp - 2];
			ISet right = (ISet) stack[sp - 1];
			if(left.size() == 0){
				stack[sp - 2] = left;
				return sp -1;
			}
			if(right.size() == 0){
				stack[sp - 2] = right;
				return sp -1;
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
			stack[sp - 2] = w.done();
			return sp - 1;
		}
	},
	rel_join_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISet left = (ISet) stack[sp - 2];
			ISet right = (ISet) stack[sp - 1];
			if(left.size() == 0){
				stack[sp - 2] = left;
				return sp -1;
			}
			if(right.size() == 0){
				stack[sp - 2] = right;
				return sp -1;
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
			stack[sp - 2] = w.done();
			return sp - 1;
		}
	},
	rel_join_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISet left = (ISet) stack[sp - 2];
			ISet right = (ISet) stack[sp - 1];
			if(left.size() == 0){
				stack[sp - 2] = left;
				return sp -1;
			}
			if(right.size() == 0){
				stack[sp - 2] = right;
				return sp -1;
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
			stack[sp - 2] = w.done();
			return sp - 1;
		}
	},
	
	/*
	 * less
	 */

	// Generic less
	
	less {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;

			Type leftType = ((IValue) stack[sp - 2]).getType();
			Type rightType = ((IValue) stack[sp - 1]).getType();

			if (leftType.isSubtypeOf(tf.numberType()) && rightType.isSubtypeOf(tf.numberType())) {
				return num_less_num.execute(stack, sp, arity);
			}

			if(!leftType.comparable(rightType)){
				stack[sp - 2] = false;
				return sp - 1;
			}


			switch (ToplevelType.getToplevelType(leftType)) {

			case BOOL:
				return bool_less_bool.execute(stack, sp, arity);
			case STR:
				return str_less_str.execute(stack, sp, arity);
			case DATETIME:
				return datetime_less_datetime.execute(stack, sp, arity);
			case LOC:
				return loc_less_loc.execute(stack, sp, arity);
			case LIST:
			case LREL:
				return list_less_list.execute(stack, sp, arity);
			case SET:
			case REL:
				return set_less_set.execute(stack, sp, arity);
			case MAP:
				return map_less_map.execute(stack, sp, arity);
			case CONSTRUCTOR:
			case NODE:
				return node_less_node.execute(stack, sp, arity);
			case ADT:
				return adt_less_adt.execute(stack, sp, 2);
			case TUPLE:
				return tuple_less_tuple.execute(stack, sp, arity);
			default:
				throw new RuntimeException("less: unexpected type " + leftType);
			}
		}
	},
	
	// less on int

	int_less_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).less((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_less_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).less((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_less_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).less((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_less_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).less((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// less on num
	
	num_less_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).less((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_less_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).less((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_less_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).less((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_less_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).less((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// less on rat
	
	rat_less_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).less((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_less_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).less((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_less_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).less((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_less_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).less((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// less on real
	
	real_less_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).less((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_less_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).less((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_less_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).less((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_less_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).less((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// less on other types
	
	adt_less_adt {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			return node_less_node.execute(stack, sp, arity);
		}
	},
	bool_less_bool {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			boolean left = (stack[sp - 2] instanceof Boolean) ? (Boolean) stack[sp - 2] : ((IBool) stack[sp - 2]).getValue();
			boolean right = (stack[sp - 1] instanceof Boolean) ? (Boolean) stack[sp - 1] : ((IBool) stack[sp - 1]).getValue();

			stack[sp - 2] = !left && right;
			return sp - 1;
		}
	},
	datetime_less_datetime {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IDateTime) stack[sp - 2]).compareTo((IDateTime) stack[sp - 1]) == -1;
			return sp - 1;
		}
	},
	list_less_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IList left = (IList) stack[sp - 2];
			IList right = (IList) stack[sp - 1];
			stack[sp - 2] = $list_less_list(left, right);
			return sp - 1;
		}
	},
	lrel_less_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IList left = (IList) stack[sp - 2];
			IList right = (IList) stack[sp - 1];
			stack[sp - 2] = $list_less_list(left, right);
			return sp - 1;
		}
	},
	loc_less_loc {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISourceLocation left = (ISourceLocation) stack[sp - 2];
			ISourceLocation right = (ISourceLocation) stack[sp - 1];

			int compare = left.getURI().toString().compareTo(right.getURI().toString());
			if (compare < 0) {
				stack[sp - 2] = true;
				return sp - 1;
			}
			else if (compare > 0) {
				stack[sp - 2] = false;
				return sp - 1;
			}

			// but the uri's are the same
			// note that line/column information is superfluous and does not matter for ordering

			if (left.hasOffsetLength()) {
				if (!right.hasOffsetLength()) {
					stack[sp - 2] = false;
					return sp - 1;
				}

				int roffset = right.getOffset();
				int rlen = right.getLength();
				int loffset = left.getOffset();
				int llen = left.getLength();

				if (loffset == roffset) {
					stack[sp - 2] = (llen < rlen);
					return sp - 1;
				}
				stack[sp - 2] = false;
				return sp - 1;
			}
			else if (compare == 0) {
				stack[sp - 2] = false;
				return sp - 1;
			}

			if (!right.hasOffsetLength()) {
				throw new ImplementationError("assertion failed");
			}
			stack[sp - 2] = false;
			return sp - 1;
		}
	},
	map_less_map {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IMap left = ((IMap) stack[sp - 2]);
			IMap right = ((IMap) stack[sp - 1]);

			stack[sp - 2] = left.isSubMap(right) && !right.isSubMap(left);
			return sp - 1;
		}
	},
	node_less_node {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			INode left = (INode) stack[sp - 2];
			INode right = (INode) stack[sp - 1];

			int compare = left.getName().compareTo(right.getName());

			if (compare <= -1) {
				stack[sp - 2] = true;
				return sp - 1;
			}

			if (compare >= 1){
				stack[sp - 2] = false;
				return sp - 1;
			}

			// if the names are not ordered, then we order lexicographically on the arguments:

			int leftArity = left.arity();
			int rightArity = right.arity();

			Object[] fakeStack = new Object[2];
			fakeStack[0] = false;
			for (int i = 0; i < Math.min(leftArity, rightArity); i++) {

				fakeStack[0] = left.get(i);
				fakeStack[1] = right.get(i);
				if(leftArity < rightArity || i < leftArity - 1)
					lessequal.execute(fakeStack, 2, 2);
				else
					less.execute(fakeStack, 2, 2);

				if(!((Boolean)fakeStack[0])){
					stack[sp - 2] = false;
					return sp - 1;
				}
			}
			stack[sp - 2] = (leftArity < rightArity) || (Boolean)fakeStack[0];
			return sp - 1;
		}
	},
	set_less_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISet lhs = (ISet) stack[sp - 2];
			ISet rhs = (ISet) stack[sp - 1];
			stack[sp - 2] = !lhs.isEqual(rhs) && lhs.isSubsetOf(rhs);
			return sp - 1;
		}
	},
	rel_less_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISet lhs = (ISet) stack[sp - 2];
			ISet rhs = (ISet) stack[sp - 1];
			stack[sp - 2] = !lhs.isEqual(rhs) && lhs.isSubsetOf(rhs);
			return sp - 1;
		}
	},
	str_less_str {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			int c = ((IString) stack[sp - 2]).compare((IString) stack[sp - 1]);
			stack[sp - 2] = c == -1;
			return sp - 1;
		}
	},
	tuple_less_tuple {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			ITuple left = (ITuple)stack[sp - 2];
			int leftArity = left.arity();
			ITuple right = (ITuple)stack[sp - 1];
			int rightArity = right.arity();

			Object[] fakeStack = new Object[2];
			for (int i = 0; i < Math.min(leftArity, rightArity); i++) {
				fakeStack[0] = left.get(i);
				fakeStack[1] = right.get(i);
				if(leftArity < rightArity || i < leftArity - 1)
					equal.execute(fakeStack, 2, 2);
				else
					less.execute(fakeStack, 2, 2);

				if(!((Boolean)fakeStack[0])){
					stack[sp - 2] = false;
					return sp - 1;
				}
			}

			stack[sp - 2] = leftArity <= rightArity;
			return sp - 1;
		}
	},

	/*
	 * lessequal
	 */

	// Generic lessequal

	lessequal {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;

			Type leftType = ((IValue) stack[sp - 2]).getType();
			Type rightType = ((IValue) stack[sp - 1]).getType();

			if (leftType.isSubtypeOf(tf.numberType()) && rightType.isSubtypeOf(tf.numberType())) {
				return num_lessequal_num.execute(stack, sp, arity);
			}

			if(!leftType.comparable(rightType)){
				stack[sp - 2] = false;
				return sp - 1;
			}

			switch (ToplevelType.getToplevelType(leftType)) {

			case BOOL:
				return bool_lessequal_bool.execute(stack, sp, arity);

			case STR:
				return str_lessequal_str.execute(stack, sp, arity);

			case DATETIME:
				return datetime_lessequal_datetime.execute(stack, sp, arity);

			case LOC:
				return loc_lessequal_loc.execute(stack, sp, arity);

			case LIST:
			case LREL:
				return list_lessequal_list.execute(stack, sp, arity);
			case SET:
			case REL:
				return set_lessequal_set.execute(stack, sp, arity);
			case MAP:
				return map_lessequal_map.execute(stack, sp, arity);
			case CONSTRUCTOR:
			case NODE:
				return node_lessequal_node.execute(stack, sp, arity);
			case ADT:
				return adt_lessequal_adt.execute(stack, sp, 2);
			case TUPLE:
				return tuple_lessequal_tuple.execute(stack, sp, arity);
			default:
				throw new RuntimeException("lessequal: unexpected type " + leftType);
			}
		}
	},
	
	// lessequal on int
	
	int_lessequal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).lessEqual((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_lessequal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).lessEqual((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_lessequal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).lessEqual((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	int_lessequal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).lessEqual((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// lessequal on num
	
	num_lessequal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).lessEqual((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_lessequal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).lessEqual((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_lessequal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).lessEqual((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	num_lessequal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).lessEqual((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// lessequal on rat
	
	rat_lessequal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).lessEqual((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_lessequal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).lessEqual((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_lessequal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).lessEqual((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	rat_lessequal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).lessEqual((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// lessequal on real
	
	real_lessequal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).lessEqual((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_lessequal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).lessEqual((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_lessequal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).lessEqual((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	real_lessequal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).lessEqual((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// lessequal on other types
	
	adt_lessequal_adt {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			return node_lessequal_node.execute(stack, sp, arity);
		}

	},
	bool_lessequal_bool {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			boolean left = (stack[sp - 2] instanceof Boolean) ? (Boolean) stack[sp - 2] : ((IBool) stack[sp - 2]).getValue();
			boolean right = (stack[sp - 1] instanceof Boolean) ? (Boolean) stack[sp - 1] : ((IBool) stack[sp - 1]).getValue();

			stack[sp - 2] = (!left && right) || (left == right);
			return sp - 1;

		}

	},
	datetime_lessequal_datetime {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			int c = ((IDateTime) stack[sp - 2]).compareTo((IDateTime) stack[sp - 1]);
			stack[sp - 2] =  c == -1 || c == 0;
			return sp - 1;
		}



	},
	list_lessequal_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IList left = (IList) stack[sp - 2];
			IList right = (IList) stack[sp - 1];

			stack[sp - 2] = $list_lessequal_list(left, right);
			return sp - 1;
		}

	},
	lrel_lessequal_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IList left = (IList) stack[sp - 2];
			IList right = (IList) stack[sp - 1];

			stack[sp - 2] = $list_lessequal_list(left, right);
			return sp - 1;
		}

	},
	loc_lessequal_loc {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISourceLocation left = (ISourceLocation) stack[sp - 2];
			ISourceLocation right = (ISourceLocation) stack[sp - 1];

			int compare = left.getURI().toString().compareTo(right.getURI().toString());
			if (compare < 0) {
				stack[sp - 2] = true;
				return sp - 1;
			}
			else if (compare > 0) {
				stack[sp - 2] = false;
				return sp - 1;
			}

			// but the uri's are the same
			// note that line/column information is superfluous and does not matter for ordering

			if (left.hasOffsetLength()) {
				if (!right.hasOffsetLength()) {
					stack[sp - 2] = false;
					return sp - 1;
				}

				int roffset = right.getOffset();
				int rlen = right.getLength();
				int loffset = left.getOffset();
				int llen = left.getLength();

				if (loffset == roffset) {
					stack[sp - 2] = (llen <= rlen);
					return sp - 1;
				}
				stack[sp - 2] = roffset < loffset && roffset + rlen >= loffset + llen;
				return sp - 1;
			}
			else if (compare == 0) {
				stack[sp - 2] = true;
				return sp - 1;
			}

			if (!right.hasOffsetLength()) {
				throw new ImplementationError("assertion failed");
			}
			stack[sp - 2] = false;
			return sp - 1;

		}

	},
	map_lessequal_map {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IMap left = (IMap) stack[sp - 2];
			IMap right = (IMap) stack[sp - 1];
			stack[sp - 2] = left.isSubMap(right);
			return sp - 1;
		}

	},
	node_lessequal_node {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			INode left = (INode) stack[sp - 2];
			INode right = (INode) stack[sp - 1];

			int compare = left.getName().compareTo(right.getName());

			if (compare <= -1) {
				stack[sp - 2] = true;
				return sp - 1;
			}

			if (compare >= 1){
				stack[sp - 2] = false;
				return sp - 1;
			}

			// if the names are not ordered, then we order lexicographically on the arguments:

			int leftArity = left.arity();
			int rightArity = right.arity();

			for (int i = 0; i < Math.min(leftArity, rightArity); i++) {
				if(!$lessequal(left.get(i), right.get(i))){
					stack[sp - 2] = false;
					return sp - 1;
				}
			}
			stack[sp - 2] = leftArity <= rightArity;
			return sp - 1;
		}

	},
	set_lessequal_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISet left = (ISet) stack[sp - 2];
			ISet right = (ISet) stack[sp - 1];
			stack[sp - 2] = left.isEqual(right) || left.isSubsetOf(right);
			return sp - 1;
		}	

	},
	rel_lessequal_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISet left = (ISet) stack[sp - 2];
			ISet right = (ISet) stack[sp - 1];
			stack[sp - 2] = left.isEqual(right) || left.isSubsetOf(right);
			return sp - 1;
		}	

	},
	str_lessequal_str {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			int c = ((IString) stack[sp - 2]).compare((IString) stack[sp - 1]);
			stack[sp - 2] = c == -1 || c == 0;
			return sp - 1;
		}

	},
	tuple_lessequal_tuple {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			ITuple left = (ITuple)stack[sp - 2];
			int leftArity = left.arity();
			ITuple right = (ITuple)stack[sp - 1];
			int rightArity = right.arity();

			for (int i = 0; i < Math.min(leftArity, rightArity); i++) {			
				if(!$lessequal(left.get(i), right.get(i))){
					stack[sp - 2] = false;
					return sp - 1;
				}
			}

			stack[sp - 2] = leftArity <= rightArity;
			return sp - 1;
		}
	},
	list_create {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity >= 0;
			
			if(arity == 0){
				stack[sp] = emptyList;
				return sp + 1;
			}
			
			IListWriter writer = vf.listWriter();

			for (int i = arity - 1; i >= 0; i--) {
				writer.append((IValue) stack[sp - 1 - i]);
			}
			sp = sp - arity + 1;
			stack[sp - 1] = writer.done();

			return sp;
		}
	},
	
	/*
	 * ...writer_open
	 */
	
	listwriter_open {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 0;	// For now, later type can be added
			IListWriter writer = vf.listWriter();
			stack[sp] = writer;
			return sp + 1;
		}

	},
	setwriter_open {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 0;	// For now, later type can be added
			ISetWriter writer = vf.setWriter();
			stack[sp] = writer;
			return sp + 1;
		}

	},
	mapwriter_open {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 0;	// For now, later type can be added
			IMapWriter writer = vf.mapWriter();
			stack[sp] = writer;
			return sp + 1;
		}
	},
	
	// ..._create: create values of various types
	
	map_create {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity >= 0;
			
			if(arity == 0){
				stack[sp] = emptyMap;
				return sp + 1;
			}
			
			IMapWriter writer = vf.mapWriter();

			for (int i = arity; i > 0; i -= 2) {
				writer.put((IValue) stack[sp - i], (IValue) stack[sp - i + 1]);
			}
			sp = sp - arity + 1;
			stack[sp - 1] = writer.done();

			return sp;
		}
	},
	set_create {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity >= 0;
			
			if(arity == 0){
				stack[sp] = emptySet;
				return sp + 1;
			}
			
			ISetWriter writer = vf.setWriter();

			for (int i = arity - 1; i >= 0; i--) {
				writer.insert((IValue) stack[sp - 1 - i]);
			}
			sp = sp - arity + 1;
			stack[sp - 1] = writer.done();

			return sp;
		}

	},
	set2elm {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			ISet set = (ISet) stack[sp - 1];
			if(set.size() != 1)
				throw new RuntimeException("set2elm: set should have a single element");
			IValue elm = set.iterator().next();
			stack[sp - 1] = elm;
			return sp;
		}

	},
	set_size {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			ISet set = (ISet) stack[sp - 1];		
			stack[sp - 1] = vf.integer(set.size());
			return sp;
		}

	},
	tuple_create {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity >= 0;
			IValue[] elems = new IValue[arity];

			for (int i = arity - 1; i >= 0; i--) {
				elems[i] = (IValue) stack[sp - arity + i];
			}
			sp = sp - arity + 1;
			stack[sp - 1] = vf.tuple(elems);
			return sp;
		}
	},
	
	/*
	 * notin
	 *
	 */
	
	// Generic notin
	
	notin {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;

			IValue left = (IValue) stack[sp - 2];
			Type leftType = left.getType();
			IValue right = (IValue) stack[sp - 2];
			Type rightType = right.getType();

			switch (ToplevelType.getToplevelType(leftType)) {
			case LIST:
				return elm_notin_list.execute(stack, sp, arity);
			case LREL:
				return elm_notin_lrel.execute(stack, sp, arity);
			case SET:
				return elm_notin_set.execute(stack, sp, arity);
			case REL:
				return elm_notin_rel.execute(stack, sp, arity);
			case MAP:
				return elm_notin_map.execute(stack, sp, arity);
			default:
				throw new RuntimeException("notin: illegal combination " + leftType
						+ " and " + rightType);
			}
		}
	},
	
	// elm_notin_...: notin for specific types
	
	elm_notin_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IList) stack[sp - 1]).contains((IValue) stack[sp - 2]);
			return sp - 1;
		}

	},
	elm_notin_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return elm_notin_list.execute(stack, sp, arity);
		}

	},
	elm_notin_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((ISet) stack[sp - 1]).contains((IValue) stack[sp - 2]);
			return sp - 1;
		}

	},
	elm_notin_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return elm_notin_set.execute(stack, sp, arity);
		}

	},
	elm_notin_map {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IMap) stack[sp - 1]).containsKey((IValue) stack[sp - 2]);
			return sp - 1;
		}
	},
	list_size {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 1] = vf.integer(((IList) stack[sp - 1]).length());
			return sp;
		}

	},
	list_replace {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 5;
			IList lst = (IList) stack[sp - 5];
			SliceDescriptor sd = $makeSliceDescriptor($getInt((IValue) stack[sp - 4]), $getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), lst.length());
			IList repl = (IList) stack[sp - 1];
			stack[sp - 5] = lst.replace(sd.first, sd.second, sd.end, repl);
			return sp - 4;
		}

	},
	str_replace {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 5;
			IString str = (IString) stack[sp - 5];
			SliceDescriptor sd = $makeSliceDescriptor($getInt((IValue) stack[sp - 4]), $getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), str.length());
			IString repl = (IString) stack[sp - 1];
			stack[sp - 5] = str.replace(sd.first, sd.second, sd.end, repl);
			return sp - 4;
		}

	},
	node_replace {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 5;
			INode node = (INode) stack[sp - 5];
			int nd_arity = node.arity();
			if(node.get(nd_arity - 1).getType().isMap()){ // Take keyword map into consideration, when present
				nd_arity--;
			}
			SliceDescriptor sd = $makeSliceDescriptor($getInt((IValue) stack[sp - 4]), $getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), nd_arity);
			IList repl = (IList) stack[sp - 1];
			stack[sp - 5] = node.replace(sd.first, sd.second, sd.end, repl);
			return sp - 4;
		}
	},
	list_slice {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 4;

			IList lst = (IList) stack[sp - 4];
			stack[sp - 4] = $makeSlice(lst, $makeSliceDescriptor($getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), $getInt((IValue) stack[sp - 1]), lst.length()));
			return sp - 3;
		}

	},
	str_slice {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 4;

			IString str = (IString) stack[sp - 4];
			stack[sp - 4] = $makeSlice(str, $makeSliceDescriptor($getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), $getInt((IValue) stack[sp - 1]), str.length()));
			return sp - 3;
		}
	},
	node_create {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity >= 1;

			String name = ((IString) stack[sp - arity]).getValue();
			IValue[] args = new IValue[arity - 1];
			for(int i = 0; i < arity - 1; i ++){
				args[i] = (IValue) stack[sp - arity + 1 + i];
			}
			stack[sp - arity] = vf.node(name, args);
			return sp - arity + 1;
		}

	},
	appl_create {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 3;
			Type applConstrType = (Type) stack[sp - 3];
			IValue prod = (IValue) stack[sp - 2];
			IValue args = (IValue) stack[sp -1];

			stack[sp - 3] = vf.constructor(applConstrType, prod, args);
			return sp - 2;
		}

	},
	node_slice {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 4;

			INode node = (INode) stack[sp - 4];
			int nd_arity = node.arity();
			if(node.get(nd_arity - 1).getType().isMap()){ // Take keyword map into consideration, when present
				nd_arity--;
			}
			stack[sp - 4] = $makeSlice(node, $makeSliceDescriptor($getInt((IValue) stack[sp - 3]), $getInt((IValue) stack[sp - 2]), $getInt((IValue) stack[sp - 1]), nd_arity));
			return sp - 3;
		}
	},
	
	/*
	 * stringwriter_*
	 */
	
	stringwriter_open {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 0;
			stack[sp] = new StringBuilder();
			return sp + 1;
		}

	},
	stringwriter_add {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			StringBuilder b = (StringBuilder) stack[sp - 2];
			IValue v = ((IValue) stack[sp - 1]);
			String s;
			if(v.getType().isString()){
				s = ((IString) v).getValue();
			} else {
				s = v.toString();
			}
			stack[sp - 2] = b.append(s);
			return sp - 1;
		}

	},
	stringwriter_close {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			StringBuilder b = (StringBuilder) stack[sp - 1];
			stack[sp - 1] = vf.string(b.toString());
			return sp;
		}
	},
	
	/*
	 * ...writer_splice
	 */
	
	listwriter_splice {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IListWriter writer = (IListWriter)stack[sp - 2];
			if(stack[sp - 1] instanceof IList){
				IList lst = (IList) stack[sp - 1];
				for(IValue v : lst){
					writer.append(v);
				}
			} else if(stack[sp - 1] instanceof ISet){
				ISet set = (ISet) stack[sp - 1];
				for(IValue v : set){
					writer.append(v);
				}
			} else {
				writer.append((IValue) stack[sp - 1]);
			}
			stack[sp - 2] = writer;
			return sp - 1;
		}

	},
	setwriter_splice {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISetWriter writer = (ISetWriter)stack[sp - 2];
			if(stack[sp - 1] instanceof IList){
				IList lst = (IList) stack[sp - 1];
				for(IValue v : lst){
					writer.insert(v);
				}
			} else if(stack[sp - 1] instanceof ISet){
				ISet set = (ISet) stack[sp - 1];
				for(IValue v : set){
					writer.insert(v);
				}
			} else {
				writer.insert((IValue) stack[sp - 1]);
			}
			stack[sp - 2] = writer;
			return sp - 1;
		}
	},
	sublist {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 3;
			IList lst = (IList) stack[sp - 3];
			int offset = ((IInteger) stack[sp - 2]).intValue();
			int length = ((IInteger) stack[sp - 1]).intValue();
			stack[sp - 3] = lst.sublist(offset, length);
			return sp - 2;
		}
	},
	
	/*
	 * product
	 * 
	 * infix Product "*" {
	 *		&L <: num x &R <: num                -> LUB(&L, &R),
	 * 		list[&L] x list[&R]                  -> lrel[&L,&R],
	 *		set[&L] x set[&R]                    -> rel[&L,&R]
	 * }
	 */
	
	product {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IValue lhs = ((IValue) stack[sp - 2]);
			IValue rhs = ((IValue) stack[sp - 1]);
			ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
			ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
			switch (lhsType) {
			case INT:
				switch (rhsType) {
				case INT:
					return int_product_int.execute(stack, sp, arity);
				case NUM:
					return int_product_num.execute(stack, sp, arity);
				case REAL:
					return int_product_real.execute(stack, sp, arity);
				case RAT:
					return int_product_rat.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case NUM:
				switch (rhsType) {
				case INT:
					return num_product_int.execute(stack, sp, arity);
				case NUM:
					return num_product_num.execute(stack, sp, arity);
				case REAL:
					return num_product_real.execute(stack, sp, arity);
				case RAT:
					return num_product_rat.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case REAL:
				switch (rhsType) {
				case INT:
					return real_product_int.execute(stack, sp, arity);
				case NUM:
					return real_product_num.execute(stack, sp, arity);
				case REAL:
					return real_product_real.execute(stack, sp, arity);
				case RAT:
					return real_product_rat.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case RAT:
				switch (rhsType) {
				case INT:
					return rat_product_int.execute(stack, sp, arity);
				case NUM:
					return rat_product_num.execute(stack, sp, arity);
				case REAL:
					return rat_product_real.execute(stack, sp, arity);
				case RAT:
					return rat_product_rat.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			default:
				throw new RuntimeException("Illegal type combination: "
						+ lhsType + " and " + rhsType);
			}
		}
	},
	
	// product on int
	
	int_product_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	int_product_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((INumber) stack[sp - 1]);
			return sp - 1;
		}
	},
	int_product_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((IRational) stack[sp - 1]);
			return sp - 1;
		}
	},
	int_product_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((IReal) stack[sp - 1]);
			return sp - 1;
		}
	},
	
	// product on num
	
	num_product_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	num_product_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).multiply((INumber) stack[sp - 1]);
			return sp - 1;
		}
	},
	num_product_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).multiply((IRational) stack[sp - 1]);
			return sp - 1;
		}
	},
	num_product_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).multiply((IReal) stack[sp - 1]);
			return sp - 1;
		}	
	},
	
	// product on rat
	
	rat_product_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	rat_product_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).multiply((INumber) stack[sp - 1]);
			return sp - 1;
		}
	},
	rat_product_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).multiply((IRational) stack[sp - 1]);
			return sp - 1;
		}
	},
	rat_product_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).multiply((IReal) stack[sp - 1]);
			return sp - 1;
		}
	},
	
	// product on real
	
	real_product_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).multiply((INumber) stack[sp - 1]);
			return sp - 1;
		}
	},
	real_product_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	real_product_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).multiply((IReal) stack[sp - 1]);
			return sp - 1;
		}
	},
	real_product_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).multiply((IRational) stack[sp - 1]);
			return sp - 1;
		}
	},
	
	// product on non-numeric types
	
	list_product_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IList left = (IList) stack[sp - 2];
			IList right = (IList) stack[sp - 1];
			IListWriter w = vf.listWriter();
			for(IValue l : left){
				for(IValue r : right){
					w.append(vf.tuple(l,r));
				}
			}
			stack[sp - 2] = w.done();
			return sp - 1;
		}

	},
	lrel_product_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return list_product_list.execute(stack, sp, arity);
		}

	},
	set_product_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ISet left = (ISet) stack[sp - 2];
			ISet right = (ISet) stack[sp - 1];
			ISetWriter w = vf.setWriter();
			for(IValue l : left){
				for(IValue r : right){
					w.insert(vf.tuple(l,r));
				}
			}
			stack[sp - 2] = w.done();
			return sp - 1;
		}

	},
	rel_product_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return set_product_set.execute(stack, sp, arity);
		}
	},
	
	/*
	 * remainder
	 */
	
	remainder {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			IValue lhs = ((IValue) stack[sp - 2]);
			IValue rhs = ((IValue) stack[sp - 1]);
			if(lhs.getType().isInteger() && rhs.getType().isInteger()){
				return int_remainder_int.execute(stack, sp, arity);
			}
			throw new RuntimeException("remainder: unexpected type combination" + lhs.getType() + " and " + rhs.getType());
		}

	},
	int_remainder_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).remainder((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	testreport_open {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 0;
			test_results = vf.listWriter();
			typeReifier = new TypeReifier(vf);
			stack[sp] = null;
			return sp + 1;
		}

	},
	testreport_close {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 0;
			stack[sp] = test_results.done();
			return sp + 1;
		}

	},
	testreport_add {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 5; 

			String fun = ((IString) stack[sp - 5]).getValue();
			boolean ignore =  ((IBool) stack[sp - 4]).getValue();
			String expected =  ((IString) stack[sp - 3]).getValue();
			ISourceLocation src = ((ISourceLocation) stack[sp - 2]);
			//stdout.println("testreport_add: " + fun);
			//Type argType = (Type) stack[sp - 1];

			if(ignore){
				test_results.append(vf.tuple(src,  vf.integer(2), vf.string("")));
				return sp - 4;
			}
			IConstructor type_cons = ((IConstructor) stack[sp - 1]);
			Type argType = typeReifier.valueToType(type_cons);
			IMap definitions = (IMap) type_cons.get("definitions");

			TypeStore store = new TypeStore();
			typeReifier.declareAbstractDataTypes(definitions, store);

			int nargs = argType.getArity();
			IValue[] args = new IValue[nargs + 1]; // '+1' kwargs

			TypeParameterVisitor tpvisit = new TypeParameterVisitor();
			Type requestedType = tf.tupleType(argType);
			HashMap<Type, Type> tpbindings = tpvisit.bindTypeParameters(requestedType);
			RandomValueTypeVisitor randomValue = new RandomValueTypeVisitor(vf, MAXDEPTH, tpbindings, store);

			int tries = nargs == 0 ? 1 : TRIES;
			boolean passed = true;
			String message = "";
			for(int i = 0; i < tries; i++){
				if(nargs > 0){
					message = " with arguments: ";
					ITuple tup = (ITuple) randomValue.generate(argType);
					for(int j = 0; j < nargs; j++){
						args[j] = tup.get(j);
						message = message + args[j].toString() + " ";
					}
				}
				try {
					args[nargs] = vf.mapWriter().done(); // kwargs
					IValue res = rvm.executeFunction(fun, args); 
					passed = ((IBool) res).getValue();
					if(!passed){
						break;
					}
				} catch (Thrown e){
					IConstructor cons = (IConstructor) e.value;
					if(!cons.getName().equals(expected)){
						message = e.toString() + message;
						passed = false;
						break;
					}
				}
				catch (Exception e){
					message = e.getMessage() + message;
					passed = false;
					break;
				}
			}
			if(passed)
				message = "";
			test_results.append(vf.tuple(src,  vf.integer(passed ? 1 : 0), vf.string(message)));
			return sp - 4;
		}
	},
	
	/*
	 * notequal
	 */
	
	// notequal int
	
	int_notequal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IInteger) stack[sp - 2]).equal((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}

	},
	int_notequal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IInteger) stack[sp - 2]).equal((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}

	},
	int_notequal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IInteger) stack[sp - 2]).equal((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}

	},
	int_notequal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IInteger) stack[sp - 2]).equal((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// notequal on num
	
	num_notequal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((INumber) stack[sp - 2]).equal((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}

	},
	num_notequal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((INumber) stack[sp - 2]).equal((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}

	},
	num_notequal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((INumber) stack[sp - 2]).equal((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}

	},
	num_notequal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((INumber) stack[sp - 2]).equal((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// notequal on real
	
	real_notequal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IReal) stack[sp - 2]).equal((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}

	},
	real_notequal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IReal) stack[sp - 2]).equal((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}

	},
	real_notequal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IReal) stack[sp - 2]).equal((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}

	},
	real_notequal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IReal) stack[sp - 2]).equal((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// notequal on rat
	
	rat_notequal_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IRational) stack[sp - 2]).equal((IInteger) stack[sp - 1]).getValue();
			return sp - 1;
		}

	},
	rat_notequal_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IRational) stack[sp - 2]).equal((INumber) stack[sp - 1]).getValue();
			return sp - 1;
		}

	},
	rat_notequal_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IRational) stack[sp - 2]).equal((IRational) stack[sp - 1]).getValue();
			return sp - 1;
		}

	},
	rat_notequal_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IRational) stack[sp - 2]).equal((IReal) stack[sp - 1]).getValue();
			return sp - 1;
		}
	},
	
	// Notequal on other types
	
	notequal {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = !((IValue) stack[sp - 2]).isEqual((IValue) stack[sp - 1]);
			return sp - 1;
		}
	},
	
	/*
	 * negative
	 * 
	 * prefix UnaryMinus "-" { &L <: num -> &L }
	 */
	
	negative {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;

			IValue left = (IValue) stack[sp - 1];
			Type leftType = left.getType();

			switch (ToplevelType.getToplevelType(leftType)) {
			case INT: return negative_int.execute(stack, sp, arity);
			case NUM: return negative_num.execute(stack, sp, arity);
			case REAL: return negative_real.execute(stack, sp, arity);
			case RAT: return negative_rat.execute(stack, sp, arity);
			default:
				throw new RuntimeException("negative: unexpected type " + leftType);

			}
		}
	},
	negative_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 1] = ((IInteger) stack[sp - 1]).negate();
			return sp;
		}
	},
	negative_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 1] = ((IReal) stack[sp - 1]).negate();
			return sp;
		}

	},
	negative_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 1] = ((IRational) stack[sp - 1]).negate();
			return sp;
		}

	},
	negative_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 1] = ((INumber) stack[sp - 1]).negate();
			return sp;
		}
	},
	num_to_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			stack[sp - 1] = ((INumber) stack[sp - 1]).toReal();
			return sp;
		}
	},
	parse {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 3;
			IString module_name = (IString) stack[sp - 3];
			IConstructor type = (IConstructor) stack[sp - 2];
			IString s = ((IString) stack[sp - 1]);

			stack[sp - 3] = parsingTools.parse(module_name, type, s);
			return sp - 2;
		}

	},
	parse_fragment {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 5;
			IString module_name = (IString) stack[sp - 5];
			IValue start = (IValue) stack[sp - 4];
			IConstructor ctree = (IConstructor) stack[sp - 3];
			ISourceLocation loc = ((ISourceLocation) stack[sp - 2]);
			IMap grammar = (IMap) stack[sp - 1];

			IValue tree = parsingTools.parseFragment(module_name, start, ctree, loc.getURI(), grammar);
			stack[sp - 5] = tree;
			return sp - 4;
		}
	},
	println {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			stdout.println(">>>>> " + stack[sp - 1]);
			return sp;
		}
	},
	
	/*
	 * subscript
	 */
	
	adt_subscript_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IConstructor cons =  (IConstructor) stack[sp - 2];
			int idx = ((IInteger) stack[sp - 1]).intValue();
			try {
				stack[sp - 2] = cons.get((idx >= 0) ? idx : (cons.arity() + idx));
			} catch(IndexOutOfBoundsException e) {
				throw RuntimeExceptions.indexOutOfBounds((IInteger) stack[sp - 1], null, new ArrayList<Frame>());
			}
			return sp - 1;
		}

	},
	node_subscript_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			INode node =  (INode) stack[sp - 2];
			int idx = ((IInteger) stack[sp - 1]).intValue();
			try {
				if(idx < 0){
					int nd_arity = node.arity();
					/* take keyword map into consideration, when present */
					idx =  (nd_arity + idx) + (node.get(nd_arity - 1).getType().isMap() ? - 1 : 0);
				}
				stack[sp - 2] = node.get(idx);  
			} catch(IndexOutOfBoundsException e) {
				throw RuntimeExceptions.indexOutOfBounds((IInteger) stack[sp - 1], null, new ArrayList<Frame>());
			}
			return sp - 1;
		}

	},
	list_subscript_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IList lst = ((IList) stack[sp - 2]);
			int idx = ((IInteger) stack[sp - 1]).intValue();
			try {
				stack[sp - 2] = lst.get((idx >= 0) ? idx : (lst.length() + idx));
			} catch(IndexOutOfBoundsException e) {
				throw RuntimeExceptions.indexOutOfBounds((IInteger) stack[sp - 1], null, new ArrayList<Frame>());
			}
			return sp - 1;
		}
	},
	map_subscript {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IMap) stack[sp - 2]).get((IValue) stack[sp - 1]);
			if(stack[sp - 2] == null) {
				throw RuntimeExceptions.noSuchKey((IValue) stack[sp - 1], null, new ArrayList<Frame>());
			}
			return sp - 1;
		}
	},
	str_subscript_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IString str = ((IString) stack[sp - 2]);
			int idx = ((IInteger) stack[sp - 1]).intValue();
			try {
				stack[sp - 2] = (idx >= 0) ? str.substring(idx, idx+1)
						: str.substring(str.length() + idx, str.length() + idx + 1);
			} catch(IndexOutOfBoundsException e) {
				throw RuntimeExceptions.indexOutOfBounds((IInteger) stack[sp - 1], null, new ArrayList<Frame>());
			}
			return sp - 1;
		}

	},
	tuple_subscript_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			ITuple tup = (ITuple) stack[sp - 2];
			int idx = ((IInteger) stack[sp - 1]).intValue();
			try {
				stack[sp - 2] = tup.get((idx >= 0) ? idx : tup.arity() + idx);
			} catch(IndexOutOfBoundsException e) {
				throw RuntimeExceptions.indexOutOfBounds((IInteger) stack[sp - 1], null, new ArrayList<Frame>());
			}
			return sp - 1;
		}
	},
	rel_subscript {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity >= 2;
			ISet rel = ((ISet) stack[sp - arity]);
			if(rel.isEmpty()){
				stack[sp - arity] = rel;
				return sp - arity + 1;
			}
			int indexArity = arity - 1;
			int relArity = rel.getElementType().getArity();
			assert indexArity < relArity ;
			int resArity = relArity - indexArity;
			IValue[] indices = new IValue[indexArity];
			for(int i = 0; i < indexArity; i++ ){
				indices[i] = (IValue) stack[sp - arity + i + 1];
				if(indices[i].getType().isString()){
					String s = ((IString) indices[i]).getValue();
					if(s.equals("_"))
						indices[i] = null;
				}
			}
			IValue[] elems = new  IValue[resArity];
			ISetWriter w = vf.setWriter();
			NextTuple:
				for(IValue vtup : rel){
					ITuple tup = (ITuple) vtup;
					for(int i = 0; i < indexArity; i++){
						if(indices[i] != null){
							IValue v = tup.get(i);
							if(indices[i].getType().isSet()){
								ISet s = (ISet) indices[i];
								if(!s.contains(v)){
									continue NextTuple;
								}
							} else
								if(!v.isEqual(indices[i])){
									continue NextTuple;
								}
						}
					}
					for(int i = 0; i < resArity; i++){
						elems[i] = tup.get(indexArity + i);
					}
					w.insert(resArity > 1 ? vf.tuple(elems) : elems[0]);
				}
			stack[sp - arity] = w.done();
			return sp - arity + 1;
		}
	},
	lrel_subscript {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity >= 2;
			IList lrel = ((IList) stack[sp - arity]);
			if(lrel.isEmpty()){
				stack[sp - arity] = lrel;
				return sp - arity + 1;
			}
			int indexArity = arity - 1;
			int lrelArity = lrel.getElementType().getArity();
			assert indexArity < lrelArity;
			int resArity = lrelArity - indexArity;
			IValue[] indices = new IValue[indexArity];
			for(int i = 0; i < indexArity; i++ ){
				indices[i] = (IValue) stack[sp - arity + i + 1];
				if(indices[i].getType().isString()){
					String s = ((IString) indices[i]).getValue();
					if(s.equals("_"))
						indices[i] = null;
				}
			}
			IValue[] elems = new  IValue[resArity];
			IListWriter w = vf.listWriter();
			NextTuple:
				for(IValue vtup : lrel){
					ITuple tup = (ITuple) vtup;
					for(int i = 0; i < indexArity; i++){
						if(indices[i] != null){
							IValue v = tup.get(i);
							if(indices[i].getType().isSet()){
								ISet s = (ISet) indices[i];
								if(!s.contains(v)){
									continue NextTuple;
								}
							} else
								if(!v.isEqual(indices[i])){
									continue NextTuple;
								}
						}
					}
					for(int i = 0; i < resArity; i++){
						elems[i] = tup.get(indexArity + i);
					}
					w.append(resArity > 1 ? vf.tuple(elems) : elems[0]);
				}
			stack[sp - arity] = w.done();
			return sp - arity + 1;
		}
	},
	
	nonterminal_subscript_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IConstructor appl = (IConstructor) stack[sp - 2];
			IList appl_args = (IList) appl.get("args");
			IConstructor prod = (IConstructor) appl.get("prod");
			IConstructor symbol = $removeLabel((IConstructor) prod.get("def"));
			int delta = $getIter(symbol);
			if(delta < 0){
				if(appl_args.length() == 1){
					IConstructor child = (IConstructor) appl_args.get(0);
					prod = (IConstructor) child.get("prod");
					symbol = $removeLabel((IConstructor) prod.get("def"));
					appl_args = (IList) child.get(1);
					delta = $getIter(symbol);
					if(delta < 0){
					  throw new RuntimeException("subscript not supported on " + symbol);
					}
				}
			}
			int index = ((IInteger) stack[sp - 1]).intValue();
			stack[sp - 2] = appl_args.get(index * delta);
			return sp - 1;
		}
	},
	
	/*
	 * subtraction
	 * 
	 * infix Difference "-" {
	 *		&L <: num x &R <: num                -> LUB(&L, &R),
	 * 		list[&L] x list[&R]                  -> list[LUB(&L,&R)],
	 *		set[&L] x set[&R]                    -> set[LUB(&L,&R)],
	 * 		map[&K1,&V1] x map[&K2,&V2]          -> map[LUB(&K1,&K2), LUB(&V1,&V2)]
	 * }
	 */
	
	subtract {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			IValue lhs = ((IValue) stack[sp - 2]);
			IValue rhs = ((IValue) stack[sp - 1]);
			ToplevelType lhsType = ToplevelType.getToplevelType(lhs.getType());
			ToplevelType rhsType = ToplevelType.getToplevelType(rhs.getType());
			switch (lhsType) {
			case INT:
				switch (rhsType) {
				case INT:
					return int_subtract_int.execute(stack, sp, arity);
				case NUM:
					return int_subtract_num.execute(stack, sp, arity);
				case REAL:
					return int_subtract_real.execute(stack, sp, arity);
				case RAT:
					return int_subtract_rat.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case NUM:
				switch (rhsType) {
				case INT:
					return num_subtract_int.execute(stack, sp, arity);
				case NUM:
					return num_subtract_num.execute(stack, sp, arity);
				case REAL:
					return num_subtract_real.execute(stack, sp, arity);
				case RAT:
					return num_subtract_rat.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case REAL:
				switch (rhsType) {
				case INT:
					return real_subtract_int.execute(stack, sp, arity);
				case NUM:
					return real_subtract_num.execute(stack, sp, arity);
				case REAL:
					return real_subtract_real.execute(stack, sp, arity);
				case RAT:
					return real_subtract_rat.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			case RAT:
				switch (rhsType) {
				case INT:
					return rat_subtract_int.execute(stack, sp, arity);
				case NUM:
					return rat_subtract_num.execute(stack, sp, arity);
				case REAL:
					return rat_subtract_real.execute(stack, sp, arity);
				case RAT:
					return rat_subtract_rat.execute(stack, sp, arity);
				default:
					throw new RuntimeException("Illegal type combination: "
							+ lhsType + " and " + rhsType);
				}
			default:
				throw new RuntimeException("Illegal type combination: "
						+ lhsType + " and " + rhsType);
			}
		}
	},
	
	// subtract on int
	
	int_subtract_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	int_subtract_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((INumber) stack[sp - 1]);
			return sp - 1;
		}
	},
	int_subtract_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((IRational) stack[sp - 1]);
			return sp - 1;
		}
	},
	int_subtract_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((IReal) stack[sp - 1]);
			return sp - 1;
		}	
	},
	
	// subtract on num
	
	num_subtract_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	num_subtract_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).subtract((INumber) stack[sp - 1]);
			return sp - 1;
		}
	},
	num_subtract_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).subtract((IRational) stack[sp - 1]);
			return sp - 1;
		}
	},
	num_subtract_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((INumber) stack[sp - 2]).subtract((IReal) stack[sp - 1]);
			return sp - 1;
		}
	},
	
	// subtract on rat
	
	rat_subtract_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	rat_subtract_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).subtract((INumber) stack[sp - 1]);
			return sp - 1;
		}
	},
	rat_subtract_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).subtract((IRational) stack[sp - 1]);
			return sp - 1;
		}
	},
	rat_subtract_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IRational) stack[sp - 2]).subtract((IReal) stack[sp - 1]);
			return sp - 1;
		}	
	},
	
	// subtract on real
	
	real_subtract_num {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).subtract((INumber) stack[sp - 1]);
			return sp - 1;
		}
	},
	real_subtract_int {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
			return sp - 1;
		}
	},
	real_subtract_real {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).subtract((IReal) stack[sp - 1]);
			return sp - 1;
		}
	},
	real_subtract_rat {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IReal) stack[sp - 2]).subtract((IRational) stack[sp - 1]);
			return sp - 1;
		}
	},
	
	// subtract on non-numeric types
	
	list_subtract_elm {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IList) stack[sp - 2]).delete((IValue) stack[sp - 1]);
			return sp - 1;
		}

	},
	list_subtract_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IList) stack[sp - 2]).subtract((IList) stack[sp - 1]);
			return sp - 1;
		}

	},
	list_subtract_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return list_subtract_list.execute(stack, sp, arity);
		}

	},
	lrel_subtract_lrel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return list_subtract_list.execute(stack, sp, arity);
		}

	},
	lrel_subtract_list {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return list_subtract_list.execute(stack, sp, arity);
		}

	},
	lrel_subtract_elm {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return list_subtract_elm.execute(stack, sp, arity);
		}

	},
	map_subtract_map {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((IMap) stack[sp - 2]).remove((IMap) stack[sp - 1]);
			return sp - 1;
		}

	},
	rel_subtract_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return set_subtract_set.execute(stack, sp, arity);
		}

	},
	rel_subtract_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return set_subtract_set.execute(stack, sp, arity);
		}

	},
	rel_subtract_elm {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return set_subtract_elm.execute(stack, sp, arity);
		}

	},
	set_subtract_elm {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((ISet) stack[sp - 2]).delete((IValue) stack[sp - 1]);
			return sp - 1;
		}

	},
	set_subtract_set {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = ((ISet) stack[sp - 2]).subtract((ISet) stack[sp - 1]);
			return sp - 1;
		}

	},
	set_subtract_rel {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			return set_subtract_set.execute(stack, sp, arity);
		}
	},
	subtype {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 2;
			stack[sp - 2] = vf.bool(((Type) stack[sp - 2]).isSubtypeOf((Type) stack[sp - 1]));
			return sp - 1;
		}
	},
	
	/*
	 * transitiveClosure
	 * 
	 * postfix Closure "+", "*" { 
	 *  	lrel[&L,&L]			-> lrel[&L,&L],
	 * 		rel[&L,&L]  		-> rel[&L,&L]
	 * }
	 */
	
	transitive_closure {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			IValue lhs = (IValue) stack[sp - 1];
			Type lhsType = lhs.getType();
			if(lhsType.isListRelation()){
				return lrel_transitive_closure.execute(stack, sp, arity);
			}
			if(lhsType.isRelation()){
				return rel_transitive_closure.execute(stack, sp, arity);
			}
			throw new RuntimeException("transitive_closure: unexpectetype " + lhsType);
		}

	},
	lrel_transitive_closure {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			IListRelation<IList> left = ((IList) stack[sp - 1]).asRelation();
			stack[sp - 1] = left.closure();
			return sp;
		}

	},
	rel_transitive_closure {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			ISetRelation<ISet> left = ((ISet) stack[sp - 1]).asRelation();
			stack[sp - 1] = left.closure();
			return sp;
		}
	},
	
	/*
	 * transitiveReflexiveClosure
	 */
	
	transitive_reflexive_closure {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			IValue lhs = (IValue) stack[sp - 1];
			Type lhsType = lhs.getType();
			if(lhsType.isListRelation()){
				return lrel_transitive_reflexive_closure.execute(stack, sp, arity);
			}
			if(lhsType.isRelation()){
				return rel_transitive_reflexive_closure.execute(stack, sp, arity);
			}
			throw new RuntimeException("transitive_closure: unexpectetype " + lhsType);
		}
	},
	lrel_transitive_reflexive_closure {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			IListRelation<IList> left = ((IList) stack[sp - 1]).asRelation();
			stack[sp - 1] = left.closureStar();
			return sp;
		}

	},
	rel_transitive_reflexive_closure {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			ISetRelation<ISet> left = ((ISet) stack[sp - 1]).asRelation();
			stack[sp - 1] = left.closureStar();
			return sp;
		}
	},

	@SuppressWarnings("unchecked")
	typeOf {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			if(stack[sp - 1] instanceof HashSet<?>){	// For the benefit of set matching
				// Move to muPrimitives?
				HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
				if(mset.isEmpty()){
					stack[sp - 1] = tf.setType(tf.voidType());
				} else {
					IValue v = mset.iterator().next();		// TODO: this is incorrect for set[value]!
					stack[sp - 1] =tf.setType(v.getType());
				}

			} else {
				stack[sp - 1] = ((IValue) stack[sp - 1]).getType();
			}
			return sp;
		}
	},

	elementTypeOf {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			Type tp = (Type) stack[sp - 1];
			stack[sp - 1] = tp.getElementType();
			return sp;
		}
	},
	
	/*
	 * update_...
	 */
	adt_update {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 3;
			IConstructor cons = (IConstructor) stack[sp - 3];
			String field = ((IString) stack[sp - 2]).getValue();
			stack[sp - 3] = cons.set(field, (IValue) stack[sp - 1]);
			return sp - 2;
		}

	},
	list_update {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 3;
			IList lst = (IList) stack[sp - 3];
			int n = ((IInteger) stack[sp - 2]).intValue();
			try {
				stack[sp - 3] = lst.put(n, (IValue) stack[sp - 1]);
				return sp - 2;
			} catch (IndexOutOfBoundsException e){
				throw RuntimeExceptions.indexOutOfBounds(vf.integer(n), null, new ArrayList<Frame>());
			}
		}

	},
	map_update {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 3;
			IMap map = (IMap) stack[sp - 3];
			IValue key = (IValue) stack[sp - 2];
			stack[sp - 3] = map.put(key, (IValue) stack[sp - 1]);
			return sp - 2;
		}

	},
	tuple_update {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 3;
			ITuple tup = (ITuple) stack[sp - 3];
			int n = ((IInteger) stack[sp - 2]).intValue();
			try {
				stack[sp - 3] = tup.set(n, (IValue) stack[sp - 1]);
				return sp - 2;
			} catch (IndexOutOfBoundsException e){
				throw RuntimeExceptions.indexOutOfBounds(vf.integer(n), null, new ArrayList<Frame>());
			}
		}

		/*
		 * Miscellaneous
		 */

	},
	loc_create {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			IString uri = ((IString) stack[sp - 1]);

			try {
				stack[sp - 1] =vf.sourceLocation(new URI(uri.getValue()));
				return sp;
			} catch (URISyntaxException e) {
				throw RuntimeExceptions.illegalArgument(uri, null, new ArrayList<Frame>());
			}

		}
	},
	loc_with_offset_create {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
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
	value_to_string {
		@Override
		public int execute(Object[] stack, int sp, int arity) {
			assert arity == 1;
			if(stack[sp - 1] instanceof IValue){
				IValue val = (IValue) stack[sp -1];
				Type tp = val.getType();
				if(tp.isList() && tp.getElementType().isAbstractData() && tp.getElementType().getName().equals("Tree")){
					IList lst = (IList) val;
					StringWriter w = new StringWriter();
					for(int i = 0; i < lst.length(); i++){
						w.write($value2string(lst.get(i)));
					}
					stack[sp - 1] = vf.string(w.toString());

				} else {
					stack[sp - 1] = vf.string($value2string(val));
				}
			} else if(stack[sp - 1] instanceof Boolean){
				stack[sp - 1] = vf.string(((Boolean) stack[sp - 1]).toString());
			} else if(stack[sp - 1] instanceof Integer){
				stack[sp - 1] = vf.string(((Integer) stack[sp - 1]).toString());
			} else {
				throw RuntimeExceptions.illegalArgument(vf.string(stack[sp -1].toString()), null, new ArrayList<Frame>());
			}
			return sp;
		}
	};

	static RascalPrimitive[] values = RascalPrimitive.values();

	public static RascalPrimitive fromInteger(int prim){
		return values[prim];
	}

	private static IValueFactory vf;
	private static TypeFactory tf;
	private static Type lineColumnType;
	private static IMap emptyMap;
	private static IList emptyList;
	private static ISet emptySet;

	private static PrintWriter stdout;
	private static RVM rvm;
	private static ParsingTools parsingTools;

	public static ParsingTools getParsingTools() { return parsingTools; }

	/**
	 * Initialize the primitive methods.
	 * @param fact value factory to be used
	 * @param profiling TODO
	 * @param stdout 
	 */
	public static void init(IValueFactory fact, RVM usedRVM, boolean doProfile) {
		vf = fact;
		if(usedRVM != null){
			stdout = usedRVM.stdout;
			rvm = usedRVM;
			parsingTools = new ParsingTools(fact);
			parsingTools.setContext(rvm.ctx);
		} else {
			System.err.println("No RVM found");
		}
		tf = TypeFactory.getInstance();
		lineColumnType = tf.tupleType(new Type[] {tf.integerType(), tf.integerType()},
				new String[] {"line", "column"});
		emptyMap = vf.mapWriter().done();
		emptyList = vf.listWriter().done();
		emptySet = vf.setWriter().done();
		indentStack = new Stack<String>();
	}

	public int execute(Object[] stack, int sp, int arity) {
		System.err.println("Not implemented mufunction");
		return 0 ;
	}

	public static void exit(){
//		if(profiling)
//			printProfile();
	}

//	private static void printProfile(){
//		stdout.println("\nRascalPrimitive execution times (ms)");
//		long total = 0;
//		TreeMap<Long,String> data = new TreeMap<Long,String>();
//		for(int i = 0; i < values.length; i++){
//			if(timeSpent[i] > 0 ){
//				data.put(timeSpent[i], values[i].name());
//				total += timeSpent[i];
//			}
//		}
//		for(long t : data.descendingKeySet()){
//			stdout.printf("%30s: %3d%% (%d ms)\n", data.get(t), t * 100 / total, t);
//		}
//	}

	/************************************************************************************
	 * 					AUXILIARY VARIABLES USED BY AUXILIARY FUNCTIONS					*	
	 ************************************************************************************/
	
	/* 
	 * testreport_...
	 */
	static TypeReifier typeReifier;
	static final int MAXDEPTH = 5;
	static final int TRIES = 3;
	static IListWriter test_results;
	
	/*
	 * String templates
	 */

	private static final Pattern MARGIN = Pattern.compile("^[ \t]*'", Pattern.MULTILINE);
	private static Stack<String> indentStack = new Stack<String>();
	
	/************************************************************************************
	 * 					AUXILIARY FUNCTIONS	 (prefixed with $)							*	
	 ************************************************************************************/

	/*
	 * String templates
	 */

	private static void $indent(String s){
		//stdout.println("$indent: " + indentStack.size() + ", \"" + s + "\"");
		indentStack.push(s);
	}

	public static String $getCurrentIndent() {
		return indentStack.isEmpty() ? "" : indentStack.peek();
	}

	private static void $unindent(){
		indentStack.pop();
	}

	private static String $removeMargins(String arg) {
		arg = MARGIN.matcher(arg).replaceAll("");
		return org.rascalmpl.interpreter.utils.StringUtils.unescapeSingleQuoteAndBackslash(arg);
	}

	private static ISourceLocation $loc_field_update(ISourceLocation sloc, String field, IValue repl) {		
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
		String authority = sloc.hasAuthority() ? sloc.getAuthority() : null;
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
				uri = sloc.getURI();
				if (!rvm.ctx.getResolverRegistry().supportsHost(uri)) {
					throw RuntimeExceptions.noSuchField("The scheme " + uri.getScheme() + " does not support the host field, use authority instead.", null,  new ArrayList<Frame>());
				}
				uri = URIUtil.changeHost(uri, newStringValue);
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
				throw RuntimeExceptions.noSuchField("Cannot update the children of a location", null, new ArrayList<Frame>());

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
				uri = sloc.getURI();
				if (!rvm.ctx.getResolverRegistry().supportsHost(uri)) {
					throw RuntimeExceptions.noSuchField("The scheme " + uri.getScheme() + " does not support the user field, use authority instead.", null,  new ArrayList<Frame>());
				}
				if (uri.getHost() != null) {
					uri = URIUtil.changeUserInformation(uri, newStringValue);
				}
				uriPartChanged = true;
				break;

			case "port":
				uri = sloc.getURI();
				if (!rvm.ctx.getResolverRegistry().supportsHost(uri)) {
					throw RuntimeExceptions.noSuchField("The scheme " + uri.getScheme() + " does not support the port field, use authority instead.", null,  new ArrayList<Frame>());
				}
				if (uri.getHost() != null) {
					int port = Integer.parseInt(((IInteger) repl).getStringRepresentation());
					uri = URIUtil.changePort(uri, port);
				}
				authority = uri.getAuthority();
				uriPartChanged = true;
				break;	

			case "length":
				iLength = ((IInteger) repl).intValue();
				if (iLength < 0) {
					throw RuntimeExceptions.illegalArgument(repl, null, new ArrayList<Frame>());
				}
				break;

			case "offset":
				iOffset = ((IInteger) repl).intValue();
				if (iOffset < 0) {
					throw RuntimeExceptions.illegalArgument(repl, null, new ArrayList<Frame>());
				}
				break;

			case "begin":
				iBeginLine = ((IInteger) ((ITuple) repl).get(0)).intValue();
				iBeginColumn = ((IInteger) ((ITuple) repl).get(1)).intValue();

				if (iBeginColumn < 0 || iBeginLine < 0) {
					throw RuntimeExceptions.illegalArgument(repl, null,  new ArrayList<Frame>());
				}
				break;
			case "end":
				iEndLine = ((IInteger) ((ITuple) repl).get(0)).intValue();
				iEndColumn = ((IInteger) ((ITuple) repl).get(1)).intValue();

				if (iEndColumn < 0 || iEndLine < 0) {
					throw RuntimeExceptions.illegalArgument(repl, null,  new ArrayList<Frame>());
				}
				break;			

			default:
				throw RuntimeExceptions.noSuchField("Modification of field " + field + " in location not allowed", null, new ArrayList<Frame>());
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
				throw RuntimeExceptions.invalidUseOfLocation("Can not add line/column information without offset/length", null,  new ArrayList<Frame>());
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
			throw RuntimeExceptions.illegalArgument(null, null);
		} catch (URISyntaxException e) {
			throw RuntimeExceptions.parseError(null, null, null);
		}
	}

	private static boolean $list_less_list(IList left, IList right) {
		boolean res = false;

		if(left.length() > right.length()){
			return res;
		}
		OUTER:for (int l = 0, r = 0; l < left.length(); l++) {
			for (r = Math.max(l, r) ; r < right.length(); r++) {
				if (left.get(l).isEqual(right.get(r))) {
					r++;
					continue OUTER;
				}
			}
			return res;
		}
		return left.length() != right.length();
	}

	private static boolean $lessequal(IValue left, IValue right){
		Object[] fakeStack = new Object[2];
		fakeStack[0] = left;
		fakeStack[1] = right;
		lessequal.execute(fakeStack, 2, 2);
		return (Boolean)fakeStack[0];
	}

	private static boolean $list_lessequal_list(IList left, IList right) {
		boolean res = false;
		if (left.length() == 0) {
			return true;
		}
		else if (left.length() > right.length()) {
			return false;
		}

		OUTER:for (int l = 0, r = 0; l < left.length(); l++) {
			for (r = Math.max(l, r) ; r < right.length(); r++) {
				if (left.get(l).isEqual(right.get(r))) {
					continue OUTER;
				}
			}
			return res;
		}

		return left.length() <= right.length();
	}

	private static Integer $getInt(IValue v){
		return v instanceof IInteger ? ((IInteger) v).intValue() : null;
	}

	public static SliceDescriptor $makeSliceDescriptor(Integer first, Integer second, Integer end, int len) {

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

		if (len == 0) {
			throw RuntimeExceptionFactory.emptyList(null, null);
		}
		if (firstIndex >= len) {
			throw RuntimeExceptions.indexOutOfBounds(vf.integer(firstIndex), null, new ArrayList<Frame>());
		}
		if (endIndex > len ) {
			throw RuntimeExceptions.indexOutOfBounds(vf.integer(endIndex), null, new ArrayList<Frame>());
		}

		return new SliceDescriptor(firstIndex, secondIndex, endIndex);
	}

	public static IList $makeSlice(IList lst, SliceDescriptor sd){
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

	public static IString $makeSlice(IString str, SliceDescriptor sd){
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

	public static IList $makeSlice(INode node, SliceDescriptor sd){
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

	private static boolean $isTree(IValue v){
		return v.getType().isAbstractData() && v.getType().getName().equals("Tree");
	}
	
	private static int $getIter(IConstructor cons){
		switch(cons.getName()){
		case "iter": case "iter-star":
			return 2;
		case "iter-seps": case "iter-star-seps":
			return 4;
		}
		return -1;
	}
	
	private static IConstructor $removeLabel(IConstructor cons){
		if(cons.getName().equals("label"))
			return (IConstructor) cons.get(1);
		return cons;
	}

	private static String $value2string(IValue val){
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return val.toString();
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
