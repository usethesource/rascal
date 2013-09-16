package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public enum MuPrimitive {
	addition_mint_mint,
	and_mbool_mbool,
	assign_pair,
	assign_subscript_array_mint,
	check_arg_type,
	division_mint_mint,
	equal_mint_mint,
	equal,
	equivalent_mbool_mbool,
	get_name_and_children,
	get_tuple_elements,
	greater_equal_mint_mint,
	greater_mint_mint,
	implies_mbool_mbool,
	is_defined,
	is_element,
	is_bool,
	is_datetime,
	is_int,
	is_list,
	is_lrel,
	is_loc,
	is_map,
	is_node,
	is_num,
	is_real,
	is_rat,
	is_rel,
	is_set,
	is_str,
	is_tuple,
	keys_map,
	values_map,
	less_equal_mint_mint,
	less_mint_mint,
	make_array,
	make_array_of_size,
	mint,
	modulo_mint_mint,
	not_equal_mint_mint,
	not_mbool,
	or_mbool_mbool,
	power_mint_mint,
	rbool,
	rint,
	set2list,
	size_array_or_list_or_set_or_map_or_tuple,
	starts_with,
	sublist_list_mint_mint,
	subscript_array_or_list_or_tuple_mint, 
	subtraction_mint_mint,
	subtype,
	typeOf,
	product_mint_mint
	;
	
	private static IValueFactory vf;
	static Method [] methods;
	
	private static MuPrimitive[] values = MuPrimitive.values();

	public static MuPrimitive fromInteger(int muprim){
		return values[muprim];
	}
	
	/**
	 * Initialize the primitive methods.
	 * @param fact value factory to be used
	 * @param stdout 
	 */
	public static void init(IValueFactory fact) {
		vf = fact;
	
		Method [] methods1 = MuPrimitive.class.getDeclaredMethods();
		HashSet<String> implemented = new HashSet<String>();
		methods = new Method[methods1.length];
		for(int i = 0; i < methods1.length; i++){
			Method m = methods1[i];
			String name = m.getName();
			if(!name.startsWith("$")){ // ignore all auxiliary functions that start with $.
				switch(name){
				case "init":
				case "invoke":
				case "fromInteger":
				case "values":
				case "valueOf":
				case "main":
					/* ignore all utility functions that do not implement some primitive */
					break;
				default:
					implemented.add(name);
					methods[valueOf(name).ordinal()] = m;
				}
			}
		}
		for(int i = 0; i < values.length; i++){
			if(!implemented.contains(values[i].toString())){
				throw new RuntimeException("PANIC: unimplemented primitive " + values[i] + " [add implementation to MuPrimitive.java]");
			}
		}
	}
	
	/**
	 * Invoke the implementation of a muRascal primitive from the RVM main interpreter loop.
	 * @param stack	stack in the current execution frame
	 * @param sp	stack pointer
	 * @param arity number of arguments on the stack
	 * @return		new stack pointer and (implicitly) modified stack contents
	 */
	int invoke(Object[] stack, int sp, int arity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return (int) methods[ordinal()].invoke(null, stack,  sp, arity);
	}
	
	/***************************************************************
	 * 				IMPLEMENTATION OF muRascal PRIMITIVES          *
	 ***************************************************************/
	
	public static int addition_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) + ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int subtraction_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) - ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int and_mbool_mbool(Object[] stack, int sp, int arity) {
		assert arity == 2;
		boolean b1 =  (stack[sp - 2] instanceof Boolean) ? ((Boolean) stack[sp - 2]) : ((IBool) stack[sp - 2]).getValue();
		boolean b2 =  (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
		stack[sp - 2] = b1 && b2;
		return sp - 1;
	}
		
	public static int greater_equal_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) >= ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int greater_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) > ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int less_equal_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) <= ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int less_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) < ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int assign_pair(Object[] stack, int sp, int arity) {
		assert arity == 3;
		int v1 = ((Integer) stack[sp - 3]);
		int v2 = ((Integer) stack[sp - 2]);
		Object[] pair = (Object[]) stack[sp - 1];
		stack[v1] = pair[0];
		stack[v2] = pair[1];
		stack[sp - 3] = pair;
		return sp - 2;  // TODO:???
	}
		
	public static int assign_subscript_array_mint(Object[] stack, int sp, int arity) {
		assert arity == 3;
		Object[] ar = (Object[]) stack[sp - 3];
		Integer index = ((Integer) stack[sp - 2]);
		ar[index] = stack[sp - 1];
		stack[sp - 3] = stack[sp - 1];
		return sp - 2;
	}
		
	public static int check_arg_type(Object[] stack, int sp, int arity) {
		assert arity == 2;
		Type argType =  ((IValue) stack[sp - 2]).getType();
		Type paramType = ((Type) stack[sp - 1]);
		stack[sp - 2] = argType.isSubtypeOf(paramType);
		return sp - 1;
	}
		
	public static int division_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) / ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int equal_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) == ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int equal(Object[] stack, int sp, int arity) {
		assert arity == 2;
		if(stack[sp - 2] instanceof IValue && (stack[sp - 2] instanceof IValue)){
			stack[sp - 2] = ((IValue) stack[sp - 2]).isEqual(((IValue) stack[sp - 1]));
		} else if(stack[sp - 2] instanceof Type && (stack[sp - 2] instanceof Type)){
			stack[sp - 2] = ((Type) stack[sp - 2]) == ((Type) stack[sp - 1]);
		} else 
			throw new RuntimeException("equal -- not defined on " + stack[sp - 2].getClass() + " and " + stack[sp - 2].getClass());
		return sp - 1;
	}
			
	public static int equivalent_mbool_mbool(Object[] stack, int sp, int arity) {
		assert arity == 2;
		boolean b1 = (stack[sp - 2] instanceof Boolean) ? ((Boolean) stack[sp - 2]) : ((IBool) stack[sp - 2]).getValue();
		boolean b2 = (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
		stack[sp - 2] = (b1 == b2);
		return sp - 1;
	}
		
	public static int get_name_and_children(Object[] stack, int sp, int arity) {
		assert arity == 1;
		INode nd = (INode) stack[sp - 1];
		String name = nd.getName();
		Object[] elems = new Object[nd.arity() + 1];
		elems[0] = vf.string(name);
		for(int i = 0; i < nd.arity(); i++){
			elems[i + 1] = nd.get(i);
		}
		stack[sp - 1] =  elems;
		return sp;
	}
		
	public static int get_tuple_elements(Object[] stack, int sp, int arity) {
		assert arity == 1;
		ITuple tup = (ITuple) stack[sp - 1];
		int nelem = tup.arity();
		Object[] elems = new Object[nelem];
		for(int i = 0; i < nelem; i++){
			elems[i] = tup.get(i);
		}
		stack[sp - 1] =  elems;
		return sp;
	}
		
	public static int implies_mbool_mbool(Object[] stack, int sp, int arity) {
		assert arity == 2;
		boolean b1 = (stack[sp - 2] instanceof Boolean) ? ((Boolean) stack[sp - 2]) : ((IBool) stack[sp - 2]).getValue();
		boolean b2 = (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
		stack[sp - 2] = b1 ? b2 : true;
		return sp - 1;
	}
		
	public static int is_defined(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = stack[sp - 1] != null;
		return sp;
	}
		
	public static int is_element(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ISet) stack[sp - 1]).contains((ISet) stack[sp - 2]);
		return sp - 1;
	}
		
	public static int is_bool(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isBool();
		return sp;
	}
		
	public static int is_datetime(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isDateTime();
		return sp;
	}
		
	public static int is_int(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isInteger();
		return sp;
	}
		
	public static int is_list(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isList();
		return sp;
	}
		
	public static int is_loc(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isSourceLocation();
		return sp;
	}
		
	public static int is_lrel(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isListRelation();
		return sp;
	}
		
	public static int is_map(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isMap();
		return sp;
	}
		
	public static int is_node(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isNode();
		return sp;
	}
		
	public static int is_num(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isNumber();
		return sp;
	}
		
	public static int is_rat(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isRational();
		return sp;
	}
		
	public static int is_real(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isReal();
		return sp;
	}
		
	public static int is_rel(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isRelation();
		return sp;
	}
		
	public static int is_set(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isSet();
		return sp - 1;}
		
	public static int is_str(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isString();
		return sp;
	}
		
	public static int is_tuple(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isTuple();
		return sp;
	}
		
	public static int keys_map(Object[] stack, int sp, int arity) {
		assert arity == 1;
		IMap map = ((IMap) stack[sp - 1]);
		IListWriter writer = vf.listWriter();
		for(IValue key : map){
			writer.append(key);
		}
		stack[sp - 1] = writer.done();
		return sp;
	}
		
	public static int make_array(Object[] stack, int sp, int arity) {
		assert arity >= 0;
		
		Object[] ar = new Object[arity];

		for (int i = arity - 1; i >= 0; i--) {
			ar[i] = stack[sp - arity + i];
		}
		sp = sp - arity + 1;
		stack[sp - 1] = ar;
		return sp;
	}
		
	public static int make_array_of_size(Object[] stack, int sp, int arity) {
		assert arity == 1;
		int len = ((Integer)stack[sp - 1]);
		stack[sp - 1] = new Object[len];
		return sp;
	}
		
	public static int mint(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IInteger) stack[sp - 1]).intValue();
		return sp;
	}
		
	public static int modulo_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) % ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int not_equal_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) != ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int not_mbool(Object[] stack, int sp, int arity) {
		assert arity == 1;
		boolean b1 = (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
		stack[sp - 1] = !b1;
		return sp;
	}
		
	public static int or_mbool_mbool(Object[] stack, int sp, int arity) {
		assert arity == 2;
		boolean b1 = (stack[sp - 2] instanceof Boolean) ? ((Boolean) stack[sp - 2]) : ((IBool) stack[sp - 2]).getValue();
		boolean b2 = (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
		stack[sp - 2] = b1 || b2;
		return sp - 1;
	}
		
	public static int power_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		int n1 = ((Integer) stack[sp - 2]);
		int n2 = ((Integer) stack[sp - 1]);
		int pow = 1;
		for(int i = 0; i < n2; i++){
			pow *= n1;
		}
		stack[sp - 2] = pow;
		return sp - 1;
	}
		
	public static int rbool(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp -1] = (stack[sp -1] instanceof Boolean) ? vf.bool((Boolean) stack[sp - 2]) : (IBool) stack[sp - 1];
		return sp;
	}
		
	public static int rint(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = vf.integer((Integer) stack[sp -1]);
		return sp;
	}
	
	public static int set2list(Object[] stack, int sp, int arity) {
		assert arity == 1;
		ISet set = (ISet) stack[sp - 1];
		IListWriter writer = vf.listWriter();
		for(IValue elem : set){
			writer.append(elem);
		}
		stack[sp - 1] = writer.done();
		return sp;
	}
		
	public static int size_array_or_list_or_set_or_map_or_tuple(Object[] stack, int sp, int arity) {
		assert arity == 1;
		if(stack[sp - 1] instanceof Object[]){
			stack[sp - 1] = ((Object[]) stack[sp - 1]).length;
		} else if(stack[sp - 1] instanceof IList){
			stack[sp - 1] = ((IList) stack[sp - 1]).length();
		} else if(stack[sp - 1] instanceof ISet){
			stack[sp - 1] = ((ISet) stack[sp - 1]).size();
		} else if(stack[sp - 1] instanceof IMap){
			stack[sp - 1] = ((IMap) stack[sp - 1]).size();
		} else if(stack[sp - 1] instanceof ITuple){
			stack[sp - 1] = ((ITuple) stack[sp - 1]).arity();
		} else
			throw new RuntimeException("size_array_or_list_mint -- not defined on " + stack[sp - 1].getClass());
		return sp;
	}
		
	public static int starts_with(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IList sublist = (IList) stack[sp - 3];
		IList list = (IList) stack[sp - 2];
		int start = (Integer) stack[sp - 1];
		boolean eq = true;
		
		if(start + sublist.length() <= list.length()){
			for(int i = 0; i < sublist.length() && eq; i++){
				if(!sublist.get(i).equals(list.get(start + i))){
					eq = false;
				}
			}
		}
		stack[sp - 3] = eq;
		return sp - 2;
	}
		
	public static int sublist_list_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IList lst = (IList) stack[sp - 3];
		int offset = ((Integer) stack[sp - 2]);
		int length = ((Integer) stack[sp - 1]);
		stack[sp - 3] = lst.sublist(offset, length);
		return sp - 2;
	}
		
	public static int subscript_array_or_list_or_tuple_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		if(stack[sp - 2] instanceof Object[]){
			stack[sp - 2] = ((Object[]) stack[sp - 2])[((Integer) stack[sp - 1])];
		} else if(stack[sp - 2] instanceof IList){
			stack[sp - 2] = ((IList) stack[sp - 2]).get((Integer) stack[sp - 1]);
		} else if(stack[sp - 2] instanceof ITuple){
			stack[sp - 2] = ((ITuple) stack[sp - 2]).get((Integer) stack[sp - 1]);
		} else
			throw new RuntimeException("subscript_array_or_list_or_tuplemint -- Object[], IList or ITuple expected");
		return sp - 1;
	}
		
	public static int subtype(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Type) stack[sp - 2]).isSubtypeOf((Type) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int typeOf(Object[] stack, int sp, int arity) {
		assert arity == 1;
		if(stack[sp - 1] instanceof Integer) {
			stack[sp - 1] = TypeFactory.getInstance().integerType();
		} else {
			stack[sp - 1] = ((IValue) stack[sp - 1]).getType();
		}
		return sp;
	}
	
	public static int product_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) * ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int values_map(Object[] stack, int sp, int arity) {
		assert arity == 1;
		IMap map = ((IMap) stack[sp - 1]);
		IListWriter writer = vf.listWriter();
		for(IValue key : map){
			writer.append(map.get(key));
		}
		stack[sp - 1] = writer.done();
		return sp;
	}
	
	/*
	 * Run this class as a Java program to compare the list of enumeration constants with the implemented methods in this class.
	 */

	public static void main(String[] args) {
		init(ValueFactoryFactory.getValueFactory());
		System.err.println("MuPrimitives have been validated!");
	}
}
