package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

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
	println,
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
	
	private static MuPrimitive[] values = MuPrimitive.values();

	public static MuPrimitive fromInteger(int prim){
		return values[prim];
	}

}
