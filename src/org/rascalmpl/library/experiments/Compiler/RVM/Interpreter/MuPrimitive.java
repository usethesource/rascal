package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public enum MuPrimitive {
	addition_num_num,
	and_bool_bool,
	assign_pair,
	assign_subscript_array_int,
	equals_num_num,
	equals_str_str,
	equals_type_type,
	get_name_and_children,
	get_tuple_elements,
	greater_equal_num_num,
	greater_num_num,
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
	less_equal_num_num,
	less_num_num,
	make_array,
	make_array_of_size,
	not_equals_num_num,
	size_array,
	sublist,
	subscript_array_int, 
	subtraction_num_num,
	subtype,
	typeOf
	;
	
	private static MuPrimitive[] values = MuPrimitive.values();

	public static MuPrimitive fromInteger(int prim){
		return values[prim];
	}

}
