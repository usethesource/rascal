package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public enum MuPrimitive {
	addition_mint_mint,
	and_mbool_mbool,
	assign_pair,
	assign_subscript_array_mint,
	equals_rint_rint,
	equals_mint_mint,
	equals_str_str,
	equals_type_type,
	get_name_and_children,
	get_tuple_elements,
	greater_equal_mint_mint,
	greater_mint_mint,
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
	less_equal_mint_mint,
	less_mint_mint,
	make_array,
	make_array_of_size,
	not_equals_mint_mint,
	size_array,
	size_list,
	sublist_list_mint_mint,
	subscript_array_mint, 
	subscript_list_mint,
	subtraction_mint_mint,
	subtype,
	typeOf
	;
	
	private static MuPrimitive[] values = MuPrimitive.values();

	public static MuPrimitive fromInteger(int prim){
		return values[prim];
	}

}
