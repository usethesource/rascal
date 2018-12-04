module lang::rascalcore::compile::muRascal2Java::Primitives

import lang::rascalcore::check::AType;
import lang::rascalcore::compile::muRascal2Java::RValue;
import lang::rascalcore::compile::muRascal2Java::Env;

import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::muRascal2Java::CodeGen;
import List;

//AType getType(rvalue(v)) = getType(v);
//
//AType getType(int n) = aint();
//AType getType(list[&T] lst) = isEmpty(lst) ? alist(avoid()) : alist(getType(typeof(lst[0]))); // TODO

// Rascal primitives

default list[str] transPrimArgs(str prim, list[MuExp] exps, JGenie jg)
    = [trans(exp, jg) | exp <- exps];

// Integer
JCode transPrim("aint_add_aint", [str x, str y], JGenie jg)                     = "<x>.add(<y>)";
JCode transPrim("aint_subtract_aint", [str x, str y], JGenie jg)                = "<x>.subtract(<y>)";
JCode transPrim("aint_product_aint", [str x, str y], JGenie jg)                 = "<x>.multiply(<y>)";
JCode transPrim("aint_greater_aint", [str x, str y], JGenie jg)                 = "<x>.greater(<y>)";
JCode transPrim("aint_greaterequal_aint", [str x, str y], JGenie jg)            = "<x>.greateEqual(<y>)";
JCode transPrim("aint_less_aint", [str x, str y], JGenie jg)                    = "<x>.less(<y>)";
JCode transPrim("aint_lessequal_aint", [str x, str y], JGenie jg)               = "<x>.lessEqual(<y>)";



JCode transPrim("equal", [str x, str y], JGenie jg)                             = "<x>.equal(<y>)";
JCode transPrim("notequal", [str x, str y], JGenie jg)                          = "!(<x>.equal(<y>))";

JCode transPrim("not_abool", [str x], JGenie jg)                                = "<x>.not()";

// String
JCode transPrim("astr_add_astr", [str x, str y], JGenie jg)                     = "<x>.concat(<y>)";
 
// List   
JCode transPrim("list_create", list[str] args, JGenie jg)                       = "$VF.list(<intercalate(", ", args)>)";

list[str] transPrimArgs("alist_subscript_aint", [MuExp x, MuExp y], JGenie jg)  = [ trans(x,jg), trans2NativeInt(y,jg) ];
JCode transPrim("alist_subscript_aint", [str x, str y], JGenie jg)              = "<x>.get(<y>)";


// Set
JCode transPrim("set_create", list[str] args, JGenie jg)                        = "$VF.set(<intercalate(", ", args)>)";
JCode transPrim("aset_subtract_aset", [str x, str y], JGenie jg)                = "<x>.subtract(<y>)";
JCode transPrim("aset_subtract_elm", [str x, str y], JGenie jg)                 = "<x>.delete(<y>)";
JCode transPrim("aset_subset_aset", [str x, str y], JGenie jg)                  = "<x>.subset(<y>)";
JCode transPrim("aset_subsets", [str x], JGenie jg)                             = "new SubSetGenerator(<x>)";
JCode transPrim("elm_in_aset", [str x, str y], JGenie jg)                       = "<y>.contains(<x>)";



// Tuple
JCode transPrim("tuple_create", list[str] args, JGenie jg)                  = "$VF.tuple(<intercalate(", ", args)>)";
    
// Node 
JCode transPrim("anode_create", [str name, *str args, str kwpMap], JGenie jg)
    = "$VF.node(<name>.getValue(), new IValue[] { <intercalate(", ", args)> }, <kwpMap>)";
