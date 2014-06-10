package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;

public class CallMuPrim extends Instruction {

	MuPrimitive muprim;
	int arity;

	public CallMuPrim(CodeBlock ins, MuPrimitive muprim, int arity) {
		super(ins, Opcode.CALLMUPRIM);
		this.muprim = muprim;
		this.arity = arity;
	}

	public String toString() {
		return "CALLMUPRIM " + muprim + ", " + arity;
	}

	public void generate(BytecodeGenerator codeEmittor, boolean dcode) {
		codeEmittor.emitInlineCallMuPrime(muprim, arity, dcode);
		codeblock.addCode2(opcode.getOpcode(), muprim.ordinal(), arity);
	}
	
	// EXPIRIMENT
	public void generateV2(BytecodeGenerator codeEmittor, boolean dcode) {
		switch (muprim) {	
		case mint:
			codeEmittor.emitCall("exnsnMINT");
			break;
		case rint:
			codeEmittor.emitCall("exnsnRINT");
			break;
		case greater_mint_mint:
			codeEmittor.emitCall("exnsnGREATER_MINT_MINT");
			break;
		case equal:
			codeEmittor.emitCall("exnsnEQUAL");
			break;
		case equal_mint_mint:
			codeEmittor.emitCall("exnsnEQUAL_MINT_MINT");
			break;
		case equal_set_mset:
			codeEmittor.emitCall("exnsnEQUAL_SET_MSET");
			break;
		case assign_pair:
			codeEmittor.emitCall("exnsnASSIGN_PAIR");
			break;
		case make_array_of_size :
			codeEmittor.emitCall("exnsnMAKE_ARRAY_OF_SIZE");
			break ;
		case assign_subscript_array_mint :
			codeEmittor.emitCall("exnsnASSIGN_SUBSCRIPT_ARRAY_MINT") ;
			break ;
		case size_array :
			codeEmittor.emitCall("exnsnSIZE_ARRAY") ;
			break ;
		case size_list :
			codeEmittor.emitCall("exnsnSIZE_LIST") ;
			break ;
		case size_tuple :
			codeEmittor.emitCall("exnsnSIZE_TUPLE") ;
			break ;
		case set2list :
			codeEmittor.emitCall("exnsnSET2LIST") ;
			break ;
		case get_name :
			codeEmittor.emitCall("exnsnGET_NAME") ;
			break ;
		case less_equal_mint_mint :
			codeEmittor.emitCall("exnsnLESS_EQUAL_MINT_MINT") ;
			break ;
		case keys_map :
			codeEmittor.emitCall("exnsnKEYS_MAP") ;
			break ;
		case subscript_tuple_mint :
			codeEmittor.emitCall("exnsnSUBSCRIPT_TUPLE_MINT") ;
			break ;
		case is_bool :
			codeEmittor.emitCall("exnsnIS_BOOL") ;
			break ;
		case is_int :
			codeEmittor.emitCall("exnsnIS_INT") ;
			break ;
		case is_real :
			codeEmittor.emitCall("exnsnIS_REAL") ;
			break ;
		case is_list :
			codeEmittor.emitCall("exnsnIS_LIST") ;
			break ;
		case is_loc :
			codeEmittor.emitCall("exnsnIS_LOC") ;
			break ;
		case is_datetime :
			codeEmittor.emitCall("exnsnIS_DATETIME") ;
			break ;
		case is_constructor :
			codeEmittor.emitCall("exnsnIS_CONSTRUCTOR") ;
			break ;
		case is_defined :
			codeEmittor.emitCall("exnsnIS_DEFINED") ;
			break ;
		case is_element_mset :
			codeEmittor.emitCall("exnsnIS_ELEMENT_MSET") ;
			break ;
		case is_lrel :
			codeEmittor.emitCall("exnsnIS_LREL") ;
			break ;
		case is_map :
			codeEmittor.emitCall("exnsnIS_MAP") ;
			break ;
		case is_node :
			codeEmittor.emitCall("exnsnIS_NODE") ;
			break ;
		case is_num :
			codeEmittor.emitCall("exnsnIS_NUM") ;
			break ;
		case is_rat :
			codeEmittor.emitCall("exnsnIS_RAT") ;
			break ;
		case is_rel :
			codeEmittor.emitCall("exnsnIS_REL") ;
			break ;
		case is_set :
			codeEmittor.emitCall("exnsnIS_SET") ;
			break ;
		case is_str :
			codeEmittor.emitCall("exnsnIS_STR") ;
			break ;
		case is_tuple :
			codeEmittor.emitCall("exnsnIS_TUPLE") ;
			break ;
		case less_mint_mint :
			codeEmittor.emitCall("exnsnIS_LESS_MINT_MINT") ;
			break ;
		case not_equal_mint_mint :
			codeEmittor.emitCall("exnsnNOT_EQUAL_MINT_MINT") ;
			break ;
		case not_mbool :
			codeEmittor.emitCall("exnsnNOT_MBOOL") ;
			break ;
		case addition_mint_mint :
			codeEmittor.emitCall("exnsnADDITION_MINT_MINT") ;
			break ;
		case and_mbool_mbool :
			codeEmittor.emitCall("exnsnAND_MBOOL_MBOOL") ;
			break ;
		case get_children :
			codeEmittor.emitCall("exnsnGET_CHILDREN") ;
			break ;
		case get_children_and_keyword_params_as_map :
			codeEmittor.emitCall("exnsnGET_CHILDREN_AND_KEYWORD_PARAMS_AS_MAP") ;
			break ;
		case get_children_and_keyword_params_as_values :
			codeEmittor.emitCall("exnsnGET_CHILDREN_AND_KEYWORD_PARAMS_AS_VALUES") ;
			break ;
		case get_children_without_layout_or_separators :
			codeEmittor.emitCall("exnsnGET_CHILDREN_WITHOUT_LAYOUT_OR_SEPARATORS") ;
			break ;
		case get_name_and_children_and_keyword_params_as_map :
			codeEmittor.emitCall("exnsnGET_NAME_AND_CHILDREN_AND_KEYWORD_PARAMS_AS_MAP") ;
			break ;
		case get_tuple_elements :
			codeEmittor.emitCall("exnsnGET_TUPLE_ELEMENTS") ;
			break ;
		case greater_equal_mint_mint :
			codeEmittor.emitCall("exnsnGREATER_EQUAL_MINT_MINT") ;
			break ;
		case make_array :
			codeEmittor.emitCall("exnsnMAKE_ARRAY",arity) ;
			break ;
		case make_entry_type_ivalue :
			codeEmittor.emitCall("exnsnMAKE_ENTRY_TYPE_IVALUE") ;
			break ;
		case make_iarray :
			codeEmittor.emitCall("exnsnMAKE_IARRAY",arity) ;
			break ;
		case make_iarray_of_size :
			codeEmittor.emitCall("exnsnMAKE_IARRAY_OF_SIZE") ;
			break ;
		case make_map_str_entry :
			codeEmittor.emitCall("exnsnMAKE_MAP_STR_ENTRY") ;
			break ;
		case make_map_str_ivalue :
			codeEmittor.emitCall("exnsnMAKE_MAP_STR_IVALUE") ;
			break ;
		case make_mset :
			codeEmittor.emitCall("exnsnMAKE_MSET") ;
			break ;
		case map_contains_key :
			codeEmittor.emitCall("exnsnMAP_CONTAINS_KEY") ;
			break ;
		case map_str_entry_add_entry_type_ivalue :
			codeEmittor.emitCall("exnsnMAP_STR_ENTRY_ADD_ENTRY_TYPE_IVALUE") ;
			break ;
		case map_str_ivalue_add_ivalue :
			codeEmittor.emitCall("exnsnMAP_STR_IVALUE_ADD_IVALUE") ;
			break ;
		case max_mint_mint :
			codeEmittor.emitCall("exnsnMAX_MINT_MINT") ;
			break ;
		case min_mint_mint :
			codeEmittor.emitCall("exnsnMIN_MINT_MINT") ;
			break ;
		case modulo_mint_mint :
			codeEmittor.emitCall("exnsnMODULO_MINT_MINT") ;
			break ;
		case mset :
			codeEmittor.emitCall("exnsnMSET") ;
			break ;
		case mset2list :
			codeEmittor.emitCall("exnsnMSET2LIST") ;
			break ;
		case mset_empty :
			codeEmittor.emitCall("exnsnMSET_EMPTY") ;
			break ;
		case mset_destructive_add_elm :
			codeEmittor.emitCall("exnsnMSET_DESTRUCTIVE_ADD_ELM") ;
			break ;
		case mset_destructive_add_mset :
			codeEmittor.emitCall("exnsnMSET_DESTRUCTIVE_ADD_MSET") ;
			break ;
		case mset_destructive_subtract_elm :
			codeEmittor.emitCall("exnsnMSET_DESTRUCTIVE_SUBTRACT_ELM") ;
			break ;
		case mset_destructive_subtract_mset :
			codeEmittor.emitCall("exnsnMSET_DESTRUCTIVE_SUBTRACT_MSET") ;
			break ;
		case mset_destructive_subtract_set :
			codeEmittor.emitCall("exnsnMSET_DESTRUCTIVE_SUBTRACT_SET") ;
			break ;
		case mset_subtract_elm :
			codeEmittor.emitCall("exnsnMSET_SUBTRACT_ELM") ;
			break ;
		case mset_subtract_mset :
			codeEmittor.emitCall("exnsnMSET_SUBTRACT_MSET") ;
			break ;
		case mset_subtract_set :
			codeEmittor.emitCall("exnsnMSET_SUBTRACT_SET") ;
			break ;
		case multiplication_mint_mint :
			codeEmittor.emitCall("exnsnMULTIPLICATION_MINT_MINT") ;
			break ;
		case set :
			codeEmittor.emitCall("exnsnSET") ;
			break ;
		case set_is_subset_of_mset :
			codeEmittor.emitCall("exnsnSET_IS_SUBSET_OF_MSET") ;
			break ;
		case size :
			codeEmittor.emitCall("exnsnSIZE") ;
			break ;
		case size_map :
			codeEmittor.emitCall("exnsnSIZE_MAP") ;
			break ;
		case size_mset :
			codeEmittor.emitCall("exnsnSIZE_MSET") ;
			break ;
		case size_set :
			codeEmittor.emitCall("exnsnSIZE_SET") ;
			break ;
		case starts_with :
			codeEmittor.emitCall("exnsnSTARTS_WITH") ;
			break ;
		case sublist_list_mint_mint :
			codeEmittor.emitCall("exnsnSUBLIST_LIST_MINT_MINT") ;
			break ;
		case subscript_array_mint :
			codeEmittor.emitCall("exnsnSUBSCRIPT_ARRAY_MINT") ;
			break ;
		case subscript_list_mint :
			codeEmittor.emitCall("exnsnSUBSCRIPT_LIST_MINT") ;
			break ;
		case subtraction_mint_mint :
			codeEmittor.emitCall("exnsnSUBTRACTION_MINT_MINT") ;
			break ;
		case subtype :
			codeEmittor.emitCall("exnsnSUBTYPE") ;
			break ;
		case rbool :
			codeEmittor.emitCall("exnsnRBOOL") ;
			break ;
		case regexp_compile :
			codeEmittor.emitCall("exnsnREGEXP_COMPILE") ;
			break ;
		case regexp_find :
			codeEmittor.emitCall("exnsnREGEXP_FIND") ;
			break ;
		case regexp_group :
			codeEmittor.emitCall("exnsnREGEXP_GROUP") ;
			break ;
		case typeOf :
			codeEmittor.emitCall("exnsnTYPEOF") ;
			break ;
		case typeOf_constructor :
			codeEmittor.emitCall("exnsnTYPEOF_CONSTRUCTOR") ;
			break ;
		case typeOfMset :
			codeEmittor.emitCall("exnsnTYPEOFMSET") ;
			break ;
		case power_mint_mint :
			codeEmittor.emitCall("exnsnPOWER_MINT_MINT") ;
			break ;
		case product_mint_mint :
			codeEmittor.emitCall("exnsnPRODUCT_MINT_MINT") ;
			break ;
		case equivalent_mbool_mbool :
			codeEmittor.emitCall("exnsnEQUIVALENT_MBOOL_MBOOL") ;
			break ;
		case check_arg_type :
			codeEmittor.emitCall("exnsnCHECK_ARG_TYPE") ;
			break ;
		case division_mint_mint :
			codeEmittor.emitCall("exnsnDIVISION_MINT_MINT") ;
			break ;
		case has_label :
			codeEmittor.emitCall("exnsnHAS_LABEL") ;
			break ;
		case implies_mbool_mbool :
			codeEmittor.emitCall("exnsnIMPLIES_MBOOL_MBOOL") ;
			break ;
		case undefine :
			codeEmittor.emitCall("exnsnUNDEFINE") ;
			break ;
		case occurs_list_list_mint :
			codeEmittor.emitCall("exnsnOCCURS_LIST_LIST_MINT") ;
			break ;
		case or_mbool_mbool :
			codeEmittor.emitCall("exnsnOR_MBOOL_MBOOL") ;
			break ;
		case values_map :
			codeEmittor.emitCall("exnsnVALUES_MAP") ;
			break ;

		default:
			// codeEmittor.emitInlineCallMuPrime(muprim.ordinal(), arity) ;
			codeEmittor.emitCall("insnCALLMUPRIM", muprim.ordinal(), arity);
		}

		codeblock.addCode2(opcode.getOpcode(), muprim.ordinal(), arity);
	}
}
