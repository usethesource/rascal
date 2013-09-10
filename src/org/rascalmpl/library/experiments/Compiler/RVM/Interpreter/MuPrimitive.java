package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public enum MuPrimitive {
	addition_mint_mint,
	and_mbool_mbool,
	AND_U_U,
	assign_pair,
	assign_subscript_array_mint,
	check_arg_type,
	equal_mint_mint,
	equal,
	equivalent_mbool_mbool,
	EQUIVALENT_U_U,
	get_name_and_children,
	get_tuple_elements,
	greater_equal_mint_mint,
	greater_mint_mint,
	implies_mbool_mbool,
	IMPLIES_U_U,
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
	less_equal_mint_mint,
	less_mint_mint,
	make_array,
	make_array_of_size,
	mint,
	not_equal_mint_mint,
	not_mbool,
	NOT_U,
	OR_U_U,
	or_mbool_mbool,
	println,
	rbool,
	rint,
	set2list,
	size_array_list_map,
	sublist_list_mint_mint,
	subscript_array_or_list_mint, 
	subtraction_mint_mint,
	subtype,
	typeOf,
	product_mint_mint
	;
	
	private static MuPrimitive[] values = MuPrimitive.values();

	public static MuPrimitive fromInteger(int prim){
		return values[prim];
	}

}
