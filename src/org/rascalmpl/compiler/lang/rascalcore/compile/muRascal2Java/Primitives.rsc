module lang::rascalcore::compile::muRascal2Java::Primitives

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;

import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::muRascal2Java::CodeGen;
import lang::rascalcore::compile::muRascal2Java::Conversions;
import List;
import Node;
import String;

// Rascal primitives

default list[str] transPrimArgs(str prim, AType result, list[AType] details, list[MuExp] exps, JGenie jg)
    = [trans(exp, jg) | exp <- exps];

set[str] arithTypes     = {"aint", "areal", "arat", "anum"};
set[str] setTypes       = {"aset", "arel"};
set[str] listTypes      = {"alist", "alrel"};
set[str] setOrListTypes = setTypes + listTypes;

//bool isArithType(AType t) = getName(t) in arithTypes;

bool isSetOnlyType(AType t) = getName(t) == "aset";
bool isRelOnlyType(AType t) = getName(t) == "arel";
bool isSetLikeType(AType t) = getName(t) in setTypes;

bool isListOnlyType(AType t) = getName(t) == "alist";
bool isListRelOnlyType(AType t) = getName(t) == "alrel";
bool isListLikeType(AType t) = getName(t) in listTypes;

bool isSetOrListLikeType(AType t) = getName(t) in setOrListTypes;

bool isTupleType(AType t) = getName(t) == "atuple";
bool isMapType(AType t) = getName(t) == "amap";
bool isNodeType(AType t) = getName(t) == "anode";
bool isADTType(AType t) = getName(t) == "acons";


// ---- add -------------------------------------------------------------------

JCode transPrim("add", AType r, [AType a, AType b], [str x, str y], JGenie jg)             = cast(r, "<getOuter(a)>_add_<getOuter(b)>(<castArg(a,x)>,<castArg(b,y)>)")  
                                                                                             when isArithType(a) && isArithType(b) || 
                                                                                                  isStrType(a) && isStrType(b) ||
                                                                                                  isLocType(a) && isStrType(b) ||
                                                                                                  isTupleType(a) && isTupleType(b) ||
                                                                                                  isListLikeType(a) && isListLikeType(b) ||
                                                                                                  isSetLikeType(a) && isSetLikeType(b) ||
                                                                                                  isMapType(a) && isMapType(b);
                                                                                                  
JCode transPrim("add", AType r, [AType a, AType b, AType c], [str x, str y, str z], JGenie jg)          
                                                                                         = "astr_add_astr(<castArg(a,x)>,astr_add_astr(<castArg(b,y)>,<castArg(c,z)>))"  when isStrType(a), isStrType(b), isStrType(c);
JCode transPrim("add", AType r, [AType a, AType b], [str x, str y], JGenie jg)           = "alist_add_elm(<castArg(a,x)>,<castArg(b,y)>)"      when isListLikeType(a), !isListLikeType(b);
JCode transPrim("add", AType r, [AType a, AType b], [str x, str y], JGenie jg)           = "elm_add_alist(<castArg(a,x)>,<castArg(b,y)>)"      when !isListLikeType(a), isListLikeType(b);

JCode transPrim("add", AType r, [AType a, AType b], [str x, str y], JGenie jg)           = "aset_add_elm(<castArg(a,x)>,<castArg(b,y)>)"       when isSetLikeType(a), !isSetLikeType(b);
JCode transPrim("add", AType r, [AType a, AType b], [str x, str y], JGenie jg)           = "elm_add_aset(<castArg(a,x)>,<castArg(b,y)>)"      when !isSetLikeType(a), isSetLikeType(b);

// ---- add_..._writer -------------------------------------------------------

JCode transPrim("add_list_writer", AType r, list[AType] atypes, [str w, str v], JGenie jg)        = "<w>.append(<v>);\n"; 
JCode transPrim("add_set_writer", AType r, list[AType] atypes, [str w, str v], JGenie jg)         = "<w>.insert(<v>);\n"; 
JCode transPrim("add_map_writer", AType r, list[AType] atypes, [str w, str k, str v], JGenie jg)  = "<w>.insert(<k>, <v>);\n"; 
JCode transPrim("add_string_writer", AType r, list[AType] atypes, [str w, str s], JGenie jg)      = "<w>.append(<s>);\n";

// ---- assert_fails ----------------------------------------------------------

JCode transPrim("assert_fails", AType r, [astr()], [str x], JGenie jg)                   = "assert_fails(<x>)";

// ---- close_..._writer ------------------------------------------------------

JCode transPrim("close_list_writer", AType r, [], [str w], JGenie jg)                    = "<w>.done()";
JCode transPrim("close_set_writer", AType r, [], [str w], JGenie jg)                     = "<w>.done()";   
JCode transPrim("close_map_writer", AType r, [], [str w], JGenie jg)                     = "<w>.done()"; 
JCode transPrim("close_string_writer", AType r, [], [str w], JGenie jg)                  = "<w>.toString()";  

// ---- compose ---------------------------------------------------------------

JCode transPrim("compose", AType r, [AType a, AType b], [str x, str y], JGenie jg)       = "((ISet)<x>).asRelation().compose(((ISet)<y>).asRelation())"
                                                                                           when isSetOrListLikeType(a), isSetOrListLikeType(b);
JCode transPrim("compose", AType r, [AType a, AType b], [str x, str y], JGenie jg)       = "((IMap)<x>).compose((IMap)<y>)"
                                                                                           when isMapType(a), isMapType(b);

// ---- create_... ------------------------------------------------------------

// TODO reconsider arg [AType a]
JCode transPrim("create_list", AType r, [AType a], list[str] args, JGenie jg)            = "$VF.list(<intercalate(", ", args)>)";
JCode transPrim("create_set", AType r, [AType a], list[str] args, JGenie jg)             = "$VF.set(<intercalate(", ", args)>)";
JCode transPrim("create_map", AType r, [AType , AType b], list[str] args, JGenie jg)     = "buildMap(<intercalate(", ", args)>)";
JCode transPrim("create_loc", aloc(), [aloc()], [str uri], JGenie jg)                    = "create_aloc(<uri>)";
JCode transPrim("create_loc_with_offset", aloc(), [aloc()], [str l, str off, str len, str bgn, str end], JGenie jg)
                                                                                          = "create_aloc_with_offset(<intercalate(", ", [l, castArg(aint(), off), castArg(aint(), len), bgn, end])>)";
JCode transPrim("create_tuple", AType r, list[AType] argTypes, list[str] args, JGenie jg)= "$VF.tuple(<intercalate(", ", args)>)";
JCode transPrim("create_node", AType r, list[AType] argTypes, [str name, *str args, str kwpMap], JGenie jg)
                                                                                         = "$VF.node(<name>.getValue(), new IValue[] { <intercalate(", ", args)> }, <kwpMap>)";
// ---- divide ----------------------------------------------------------------
 
JCode transPrim("divide", AType r, [AType l, aparameter(_, AType bnd)], [str x, str y], JGenie jg)  
                                                                                         = transPrim("divide", r, [l, bnd], [x, y], jg);
JCode transPrim("divide", AType r, [aparameter(_, AType bnd), AType rgt], [str x, str y], JGenie jg)  
                                                                                         = transPrim("divide", r, [bnd,rgt], [x, y], jg);

JCode transPrim("divide", AType r, [AType a, AType b], [str x, str y], JGenie jg)         = "<getOuter(a)>_divide_<getOuter(b)>(<castArg(a,x)>,<castArg(b,y)>)"     when isArithType(a), isArithType(b);


// ---- equal -----------------------------------------------------------------

JCode transPrim("equal", abool(), [AType a, AType b], [str x, str y], JGenie jg)         = "(<x>).isEqual(<y>)"   when !(isNodeType(a) || isNodeType(b)); // was .equal
JCode transPrim("equal", abool(), [AType a, AType b], [str x, str y], JGenie jg)         = "(<x>).isEqual(<y>)"   when isNodeType(a) || isNodeType(b);    //WHY?

// ---- field_project ---------------------------------------------------------

JCode transPrim("field_project", AType r, [AType a], [str x, *str args], JGenie jg)      = "((<atype2javatype(r)>) atuple_field_project(<x>, <intercalate(", ", args)>))"
                                                                                           when isTupleType(a);
JCode transPrim("field_project", AType r, [AType a], [str x, *str args], JGenie jg)      = "((<atype2javatype(r)>) amap_field_project(<x>, <intercalate(", ", args)>))"
                                                                                           when isMapType(a);
JCode transPrim("field_project", AType r, [AType a], [str x, *str args], JGenie jg)      = "((<atype2javatype(r)>) arel_field_project(<x>, <intercalate(", ", args)>))"
                                                                                           when isRelOnlyType(a);
JCode transPrim("field_project", AType r, [AType a], [str x, *str args], JGenie jg)      = "((<atype2javatype(r)>) alrel_field_project(<x>, <intercalate(", ", args)>))"
                                                                                           when isListRelOnlyType(a);

// ---- guarded_field_project -------------------------------------------------

JCode transPrim("guarded_field_project", AType r, [AType a], [str x, *str args], JGenie jg)      = "guarded_atuple_field_project(<x>, <intercalate(", ", args)>)"
                                                                                                   when isTupleType(a);
JCode transPrim("guarded_field_project", AType r, [AType a], [str x, *str args], JGenie jg)      = "guarded_amap_field_project(<x>, <intercalate(", ", args)>)"
                                                                                                    when isMapType(a);
JCode transPrim("guarded_field_project", AType r, [AType a], [str x, *str args], JGenie jg)      = "guarded_arel_field_project(<x>, <intercalate(", ", args)>)"
                                                                                                    when isRelOnlyType(a);
JCode transPrim("guarded_field_project", AType r, [AType a], [str x, *str args], JGenie jg)      = "guarded_alrel_field_project(<x>, <intercalate(", ", args)>)"
                                                                                                    when isListRelOnlyType(a);

// ---- greater ---------------------------------------------------------------

JCode transPrim("greater", abool(), [abool(), abool()], [str x, str y], JGenie jg)      = "abool_lessequal_abool(<x>,<y>).not()"; 
JCode transPrim("greater", AType r, [AType a, AType b], [str x, str y], JGenie jg)      = "<getOuter(a)>_lessequal_<getOuter(b)>(<castArg(a,x)>,<castArg(b,y)>).not()"     when isArithType(a), isArithType(b);
JCode transPrim("greater", abool(), [astr(), astr()], [str x, str y], JGenie jg)        = "astr_lessequal_astr(<x>,<y>.not()"; 
JCode transPrim("greater", abool(), [adatetime(), adatetime()], [str x, str y], JGenie jg)         
                                                                                        = "datetime_lessequal_adatetime(<x>,<y>.not()"; 
JCode transPrim("greater", abool(), [aloc(), aloc()], [str x, str y], JGenie jg)        = "aloc_lessequal_aloc(<x>,<y>).not()"; 
JCode transPrim("greater", abool(), [AType a, AType b], [str x, str y], JGenie jg)      = "atuple_lessequal_atuple(<x>,<y>).not()"      when isTupleType(a), isTupleType(b);
JCode transPrim("greater", abool(), [AType a, AType b], [str x, str y], JGenie jg)      = "anode_lessequal_anode(<x>,<y>).not()"        when isNodeType(a), isNodeType(b);
JCode transPrim("greater", abool(), [AType a, AType b], [str x, str y], JGenie jg)      = "alist_lessequal_alist(<x>,<y>).not()"        when isListLikeType(a), isListLikeType(b);
JCode transPrim("greater", abool(), [AType a, AType b], [str x, str y], JGenie jg)      = "aset_lessequal_aset(<x>,<y>).not()"          when isSetLikeType(a), isSetLikeType(b);
JCode transPrim("greater", abool(), [AType a, AType b], [str x, str y], JGenie jg)      = "amap_lessequal_amap(<x>,<y>).not()"          when isMapType(a), isMapType(b);
JCode transPrim("greater", abool(), [AType a, AType b], [str x, str y], JGenie jg)      = "lessequal(<x>,<y>).not()"                    when isValueType(a), isValueType(b);


// ---- greaterequal ----------------------------------------------------------

JCode transPrim("greaterequal", abool(), [abool(), abool()], [str x, str y], JGenie jg)  = "abool_less_abool(<x>,<y>).not()"; 
JCode transPrim("greaterequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)  = "alist_less_alist(<x>,<y>).not()"            when isListLikeType(a), isListLikeType(b); 
JCode transPrim("greaterequal", AType r, [AType a, AType b], [str x, str y], JGenie jg)  = "<getOuter(a)>_less_<getOuter(b)>(<castArg(a,x)>,<castArg(b,y)>).not()"     when isArithType(a), isArithType(b);
JCode transPrim("greaterequal", abool(), [astr(), astr()], [str x, str y], JGenie jg)    = "astr_less_astr(<x>,<y>.not()"; 
JCode transPrim("greaterequal", abool(), [adatetime(), adatetime()], [str x, str y], JGenie jg)         
                                                                                         = "adatetime_less_adatetime(<x>,<y>).not()"; 
JCode transPrim("greaterequal", abool(), [aloc(), aloc()], [str x, str y], JGenie jg)    = "aloc_less_aloc(<x>,<y>).not()"; 
JCode transPrim("greaterequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)  = "atuple_less_atuple(<x>,<y>).not()"          when isTupleType(a), isTupleType(b); 
JCode transPrim("greaterequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)  = "anode_less_anode(<x>,<y>).not()"            when isNodeType(a), isNodeType(b); 
JCode transPrim("greaterequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)  = "alist_less_alist(<x>,<y>).not()"            when isListLikeType(a), isListLikeType(b); 
JCode transPrim("greaterequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)  = "aset_less_aset(<x>,<y>).not()"              when isSetLikeType(a), isSetLikeType(b); 
JCode transPrim("greaterequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)  = "amap_less_amap(<x>,<y>).not()"              when isMapType(a), isMapType(b);  
JCode transPrim("greaterequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)  = "less(<x>,<y>).not()"                        when isValueType(a), isValueType(b);  

// has
// has_field
// ---- in --------------------------------------------------------------------

JCode transPrim("in", abool(), [AType a, AType b],  [str x, str y], JGenie jg)           = "$VF.bool(<y>.contains(<x>))"                when isSetOrListLikeType(b);
JCode transPrim("in", abool(), [AType a, AType b],  [str x, str y], JGenie jg)           = "$VF.bool(<y>.containsKey(<x>))"             when isMapType(b);

// ---- intersect -------------------------------------------------------------

JCode transPrim("intersect", AType r, [AType a, AType b], [ str x, str y], JGenie jg)    = "<x>.intersect(<y>)"                         when isListLikeType(a), isListLikeType(b);
JCode transPrim("intersect", AType r, [AType a, AType b], [ str x, str y], JGenie jg)    = "<x>.intersect(<y>)"                         when isSetLikeType(a), isSetLikeType(b);
JCode transPrim("intersect", AType r, [AType a, AType b], [ str x, str y], JGenie jg)    = "<x>.common(<y>)"                            when isMapType(a), isMapType(b);

// ---- is --------------------------------------------------------------------

JCode transPrim("is", abool(), [AType a], [str x, str y], JGenie jg)                     = "is(<x>,<y>)";

// ---- guarded_subscript -----------------------------------------------------

list[str] transPrimArgs("guarded_subscript", AType r, [AType a, aint()], [MuExp x, MuExp y], JGenie jg)  
                                                                                                = [ trans(x,jg), trans2NativeInt(y,jg) ] 
                                                                                                  when isListOnlyType(a) || (a == astr()) || isTupleType(a) || 
                                                                                                       isNodeType(a) || isADTType(a); 
                                                                                       
JCode transPrim("guarded_subscript", AType r, [astr(), aint()], [str x, str y], JGenie jg)      = "guarded_astr_subscript_int(<x>,<y>)";
JCode transPrim("guarded_subscript", AType r, [AType a, aint()], [str x, str y], JGenie jg)     = "guarded_atuple_subscript_int(<x>,<y>)" when isTupleType(a);
JCode transPrim("guarded_subscript", AType r, [AType a, aint()], [str x, str y], JGenie jg)     = "guarded_anode_subscript_int(<x>,<y>)" when isNodeType(a);
JCode transPrim("guarded_subscript", AType r, [AType a, aint()], [str x, str y], JGenie jg)     = "guarded_aadt_subscript_int(<x>,<y>)" when isADTType(a);
JCode transPrim("guarded_subscript", AType r, [AType a, AType b], [ str x, str y], JGenie jg)   
                                                                                                = "guarded_list_subscript(<x>,<y>)" when isListOnlyType(a);
JCode transPrim("guarded_subscript", AType r, [AType a, AType b], [str x, str y], JGenie jg)    = "guarded_map_subscript(<x>,<y>)" when isMapType(a);

JCode transPrim("guarded_subscript", AType r, [AType a, *AType types], [str x, *str args], JGenie jg) {
    if(arel(atypeList(list[AType] elemTypes)) := a){
        n = size(elemTypes);
        if(n == 2 && !jg.isWildCard(args[0])){
            return isSetLikeType(types[0])  ? "guarded_arel2_subscript1_aset(<x>,<args[0]>))"
                                        : "guarded_arel_subscript1_noset(<x>,<args[0]>)" ;
        } else if(size(args) == 1 && !jg.isWildCard(args[0]) && !isSetLikeType(types[0])){
            return "guarded_arel_subscript1_noset(<x>,<args[0]>)";
        }
        return "guarded_arel_subscript(<x>,<makeIndex(args)>,<makeIndexDescr(types, args, jg)>)";    
    } else if(alrel(atypeList(list[AType] elemTypes)) := a){
        n = size(elemTypes);
        if(n == 2 && !jg.isWildCard(args[0])){
            return isSetLikeType(types[0])  ? "guarded_alrel2_subscript1_aset(<x>,<args[0]>))"
                                        : "guarded_alrel_subscript1_noset(<x>,<args[0]>)" ;
        } else if(size(args) == 1 && !jg.isWildCard(args[0]) && !isSetLikeType(types[0])){
            return "guarded_alrel_subscript1_noset(<x>,<args[0]>)";
        }
        return "guarded_alrel_subscript(<x>,<makeIndex(args)>,<makeIndexDescr(types, args, jg)>)";    
    } else
        fail;
}   
                                                                                       
// ---- join ------------------------------------------------------------------

JCode transPrim("join", AType r, [AType a, AType b], [str x, str y], JGenie jg)          = "alist_product_alist(<castArg(a,x)>,<castArg(b,y)>)" when isListOnlyType(a), isListOnlyType(b);
JCode transPrim("join", AType r, [AType a, AType b], [str x, str y], JGenie jg)          = "alist_join_alrel(<castArg(a,x)>,<castArg(b,y)>)" when isListOnlyType(a), isListRelOnlyType(b);
JCode transPrim("join", AType r, [AType a, AType b], [str x, str y], JGenie jg)          = "alrel_join_alrel(<castArg(a,x)>,<castArg(b,y)>)" when isListRelOnlyType(a), isListRelOnlyType(b);
JCode transPrim("join", AType r, [AType a, AType b], [str x, str y], JGenie jg)          = "alrel_join_alist(<castArg(a,x)>,<castArg(b,y)>)" when isListRelOnlyType(a), isListOnlyType(b);

JCode transPrim("join", AType r, [AType a, AType b], [str x, str y], JGenie jg)          = "aset_product_aset(<castArg(a,x)>,<castArg(b,y)>)" when isSetOnlyType(a), isSetOnlyType(b);
JCode transPrim("join", AType r, [AType a, AType b], [str x, str y], JGenie jg)          = "aset_join_arel(<castArg(a,x)>,<castArg(b,y)>)" when isSetOnlyType(a), isRelOnlyType(b);
JCode transPrim("join", AType r, [AType a, AType b], [str x, str y], JGenie jg)          = "arel_join_arel(<castArg(a,x)>,<castArg(b,y)>)" when isRelOnlyType(a), isRelOnlyType(b);
JCode transPrim("join", AType r, [AType a, AType b], [str x, str y], JGenie jg)          = "arel_join_aset(<castArg(a,x)>,<castArg(b,y)>)" when isRelOnlyType(a), isSetOnlyType(b);

// ----less -------------------------------------------------------------------

JCode transPrim("less", abool(), [abool(), abool()], [str x, str y], JGenie jg)          = "abool_less_abool(<x>,<y>)"; 
JCode transPrim("less", AType r, [AType a, AType b], [str x, str y], JGenie jg)          = "<getOuter(a)>_less_<getOuter(b)>(<castArg(a,x)>,<castArg(b,y)>)"     when isArithType(a), isArithType(b);
JCode transPrim("less", abool(), [astr(), astr()], [str x, str y], JGenie jg)            = "astr_less_astr(<x>,<y>)";  
JCode transPrim("less", abool(), [adatetime(), adatetime()], [str x, str y], JGenie jg)  = "adatetime_less_adatetime(<x>,<y>)"; 
JCode transPrim("less", abool(), [aloc(), aloc()], [str x, str y], JGenie jg)            = "aloc_less_aloc(<x>,<y>)"; 
JCode transPrim("less", abool(), [AType a, AType b], [str x, str y], JGenie jg)          = "atuple_less_atuple(<x>,<y>)"  when isTupleType(a), isTupleType(b); 
JCode transPrim("less", abool(), [AType a, AType b], [str x, str y], JGenie jg)          = "anode_less_anode(<x>,<y>)" when isNodeType(a), isNodeType(b); 
JCode transPrim("less", abool(), [AType a, AType b], [str x, str y], JGenie jg)          = "alist_less_alist(<x>,<y>)"  when isListLikeType(a), isListLikeType(b);    
JCode transPrim("less", abool(), [AType a, AType b], [str x, str y], JGenie jg)          = "aset_less_aset(<x>,<y>)" when isSetLikeType(a), isSetLikeType(b);  
JCode transPrim("less", abool(), [AType a, AType b], [str x, str y], JGenie jg)          = "amap_less_amap(<x>,<y>)" when isMapType(a), isMapType(b); 
JCode transPrim("less", abool(), [AType a, AType b], [str x, str y], JGenie jg)          = "less(<x>,<y>)" when isValueType(a), isValueType(b); 

// adt

// ---- lessequal -------------------------------------------------------------

JCode transPrim("lessequal", abool(), [abool(), abool()], [str x, str y], JGenie jg)     = "abool_lessequal_abool(<x>,<y>)"; 
JCode transPrim("lessequal", AType r, [AType a, AType b], [str x, str y], JGenie jg)     = "<getOuter(a)>_lessequal_<getOuter(b)>(<castArg(a,x)>,<castArg(b,y)>)"     when isArithType(a), isArithType(b);
JCode transPrim("lessequal", abool(), [astr(), astr()], [str x, str y], JGenie jg)       = "astr_lessequal_astr(<x>,<y>)";  
JCode transPrim("lessequal", abool(), [adatetime(), adatetime()], [str x, str y], JGenie jg)  
                                                                                         = "adatetime_lessequal_adatetime(<x>,<y>)"; 
JCode transPrim("lessequal", abool(), [aloc(), aloc()], [str x, str y], JGenie jg)       = "aloc_lessequal_aloc(<x>,<y>)"; 
JCode transPrim("lessequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)     = "atuple_lessequal_atuple(<x>,<y>)" when isTupleType(a), isTupleType(b); 
JCode transPrim("lessequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)     = "anode_lessequal_anode(<x>,<y>)"  when isNodeType(a), isNodeType(b); 
JCode transPrim("lessequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)     = "alist_lessequal_alist(<x>,<y>)"  when isListLikeType(a), isListLikeType(b);     
JCode transPrim("lessequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)     = "aset_lessequal_aset(<x>,<y>)" when isSetLikeType(a), isSetLikeType(b); 
JCode transPrim("lessequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)     = "amap_lessequal_amap(<x>,<y>)"  when isMapType(a), isMapType(b);   
JCode transPrim("lessequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)     = "lessequal(<x>,<y>)" when isValueType(a), isValueType(b);

// ---- modulo ----------------------------------------------------------------
JCode transPrim("mod", aint(), [aint(), aint()], [str x, str y], JGenie jg)              = "<x>.mod(<y>)";

// ---- negative --------------------------------------------------------------

JCode transPrim("negative", AType r, [AType a], [str x], JGenie jg)                      = "<x>.negate()"            when isArithType(r);

//non_negative

// ---- not -------------------------------------------------------------------

list[str] transPrimArgs("not", abool(), [abool()], [MuExp x], JGenie jg)                 = [producesNativeBool(x) ? "$VF.bool(<trans(x, jg)>)" : trans(x, jg) ];
           
JCode transPrim("not", abool(), [abool()], [str x], JGenie jg)                           = "<x>.not()";

// ---- notequal --------------------------------------------------------------

JCode transPrim("notequal", abool(), [AType a, AType b], [str x, str y], JGenie jg)      = "!(<x>.isEqual(<y>))";

// ---- notin -----------------------------------------------------------------

JCode transPrim("notin", abool(), [AType a, AType b],  [str x, str y], JGenie jg)        = "$VF.bool(!<y>.contains(<x>))"       when isSetOrListLikeType(b);
JCode transPrim("notin", abool(), [AType a, AType b],  [str x, str y], JGenie jg)        = "$VF.bool(!<y>.containsKey(<x>))"    when isMapType(b);
                                                                                                            
// ---- open_..._writer -------------------------------------------------------

JCode transPrim("open_list_writer", AType r, [], [], JGenie jg)                          = "$VF.listWriter()";
JCode transPrim("open_set_writer", AType r, [], [], JGenie jg)                           = "$VF.setWriter();\n";   
JCode transPrim("open_map_writer", AType r, [], [], JGenie jg)                           = "$VF.mapWriter()";  
JCode transPrim("open_string_writer", AType r, [], [], JGenie jg)                        = "new StringWriter()";

// ---- product ---------------------------------------------------------------

JCode transPrim("product", AType r, [AType a, AType b], [str x, str y], JGenie jg)       = "<getOuter(a)>_product_<getOuter(b)>(<castArg(a,x)>,<castArg(b,y)>)"     when isArithType(a), isArithType(b);
JCode transPrim("product", AType r, [AType a, AType b], [str x, str y], JGenie jg)       = "alist_product_alist(<castArg(a,x)>,<castArg(b,y)>)"   when isListLikeType(a), isListLikeType(b);
JCode transPrim("product", AType r, [AType a, AType b], [str x, str y], JGenie jg)       = "aset_product_aset(<castArg(a,x)>,<castArg(b,y)>)"     when isSetLikeType(a), isSetLikeType(b);

// project
// ---- remainder -------------------------------------------------------------

JCode transPrim("remainder", AType r, [aint(), aint()], [str x, str y], JGenie jg)       = "<x>.remainder(<y>)";

// ---- slice -----------------------------------------------------------------

list[str] transSliceArgs(MuExp first, MuExp second, MuExp end, JGenie jg) =  [ first == muNoValue() ? "null" : trans2NativeInt(first,jg),
                                                                               second == muNoValue() ? "null" : trans2NativeInt(second,jg), 
                                                                               end == muNoValue() ? "null" : trans2NativeInt(end,jg) ];

list[str] transPrimArgs("slice", AType r, [AType a], [MuExp x, MuExp first, MuExp second, MuExp end], JGenie jg) = [ trans(x,jg), *transSliceArgs(first, second, end, jg)];
                                                                                   

JCode transPrim("slice", AType r, [astr()], [str x, str first, str second, str end], JGenie jg)   = "astr_slice(<x>, <first>, <second>, <end>)";
JCode transPrim("slice", AType r, [AType a], [str x, str first, str second, str end], JGenie jg)  = "alist_slice(<x>, <first>, <second>, <end>)" when isListLikeType(a);
JCode transPrim("slice", AType r, [AType a], [str x, str first, str second, str end], JGenie jg)  = "anode_slice(<x>, <first>, <second>, <end>)" when isNodeType(a);
// TODO: concrete cases

// ---- list slice operations -------------------------------------------------

// str_slice_replace
list[str] transPrimArgs("astr_slice_replace", AType r, [AType a], [MuExp x, MuExp first, MuExp second, MuExp end, MuExp repl], JGenie jg)
                                                                                                 = [ trans(x,jg), *transSliceArgs(first, second, end, jg), trans(repl, jg)];
 
JCode transPrim("astr_slice_replace", AType r, [AType a], [str x, str first, str second, str end, str repl], JGenie jg) = "astr_slice_replace(<x>, <first>, <second>, <end>, <repl>)";

// anode_slice_replace
list[str] transPrimArgs("anode_slice_replace", AType r, [AType a], [MuExp x, MuExp first, MuExp second, MuExp end, MuExp repl], JGenie jg)
                                                                                                 = [ trans(x,jg), *transSliceArgs(first, second, end, jg), trans(repl, jg)];
 
JCode transPrim("anode_slice_replace", AType r, [AType a], [str x, str first, str second, str end, str repl], JGenie jg) = "anode_slice_replace(<x>, <first>, <second>, <end>, <repl>)";


// list_slice_replace
list[str] transPrimArgs("alist_slice_replace", AType r, [AType a], [MuExp x, MuExp first, MuExp second, MuExp end, MuExp repl], JGenie jg)
                                                                                                 = [ trans(x,jg), *transSliceArgs(first, second, end, jg), trans(repl, jg)];
 
JCode transPrim("alist_slice_replace", AType r, [AType a], [str x, str first, str second, str end, str repl], JGenie jg) = "alist_slice_replace(<x>, <first>, <second>, <end>, <repl>)";


// list_slice_replace
list[str] transPrimArgs("alist_slice_replace", AType r, [AType a], [MuExp x, MuExp first, MuExp second, MuExp end, MuExp repl], JGenie jg)
                                                                                                 = [ trans(x,jg), *transSliceArgs(first, second, end, jg), trans(repl, jg)];
 
JCode transPrim("alist_slice_replace", AType r, [AType a], [str x, str first, str second, str end, str repl], JGenie jg) = "alist_slice_replace(<x>, <first>, <second>, <end>, <repl>)";

// list_slice_add
list[str] transPrimArgs("alist_slice_add", AType r, [AType a], [MuExp x, MuExp first, MuExp second, MuExp end, MuExp repl], JGenie jg)
                                                                                                 = [ trans(x,jg), *transSliceArgs(first, second, end, jg), trans(repl, jg)];
 
JCode transPrim("alist_slice_add", AType r, [AType a], [str x, str first, str second, str end, str repl], JGenie jg) = "alist_slice_add(<x>, <first>, <second>, <end>, <repl>)";

// list_slice_subtract
list[str] transPrimArgs("alist_slice_subtract", AType r, [AType a], [MuExp x, MuExp first, MuExp second, MuExp end, MuExp repl], JGenie jg)
                                                                                                 = [ trans(x,jg), *transSliceArgs(first, second, end, jg), trans(repl, jg)];
 
JCode transPrim("alist_slice_subtract", AType r, [AType a], [str x, str first, str second, str end, str repl], JGenie jg) = "alist_slice_subtract(<x>, <first>, <second>, <end>, <repl>)";

// list_slice_product
list[str] transPrimArgs("alist_slice_product", AType r, [AType a], [MuExp x, MuExp first, MuExp second, MuExp end, MuExp repl], JGenie jg)
                                                                                                 = [ trans(x,jg), *transSliceArgs(first, second, end, jg), trans(repl, jg)];
 
JCode transPrim("alist_slice_product", AType r, [AType a], [str x, str first, str second, str end, str repl], JGenie jg) = "alist_slice_product(<x>, <first>, <second>, <end>, <repl>)";

// list_slice_divide
list[str] transPrimArgs("alist_slice_divide", AType r, [AType a], [MuExp x, MuExp first, MuExp second, MuExp end, MuExp repl], JGenie jg)
                                                                                                 = [ trans(x,jg), *transSliceArgs(first, second, end, jg), trans(repl, jg)];
 
JCode transPrim("alist_slice_divide", AType r, [AType a], [str x, str first, str second, str end, str repl], JGenie jg) = "alist_slice_divide(<x>, <first>, <second>, <end>, <repl>)";

// list_slice_intersect
list[str] transPrimArgs("alist_slice_intersect", AType r, [AType a], [MuExp x, MuExp first, MuExp second, MuExp end, MuExp repl], JGenie jg)
                                                                                                 = [ trans(x,jg), *transSliceArgs(first, second, end, jg), trans(repl, jg)];
 
JCode transPrim("alist_slice_intersect", AType r, [AType a], [str x, str first, str second, str end, str repl], JGenie jg) = "alist_slice_intersect(<x>, <first>, <second>, <end>, <repl>)";

// ---- splice ----------------------------------------------------------------

JCode transPrim("splice_list", AType r, [AType a, AType b],  [str w, str v], JGenie jg)    = "listwriter_splice(<w>,<v>);\n";
JCode transPrim("splice_set", AType r, [AType a, AType b],  [str w, str v], JGenie jg)     = "setwriter_splice(<w>,<v>);\n";
// TODO: concrete cases
    
// ---- subscript -------------------------------------------------------------
    
list[str] transPrimArgs("subscript", AType r, [AType a, aint()], [MuExp x, MuExp y], JGenie jg)  
                                                                                = [ trans(x,jg), trans2NativeInt(y,jg) ] 
                                                                                  when isListOnlyType(a) || isStrType(a) || isTupleType(a) || 
                                                                                       isNodeType(a) || isADTType(a);   
JCode transPrim("subscript", AType r, [astr(), aint()], [str x, str y], JGenie jg)       = "astr_subscript_int(<x>,<y>)";
JCode transPrim("subscript", AType r, [AType a, aint()], [str x, str y], JGenie jg)      = "((<atype2javatype(r)>)atuple_subscript_int(<x>,<y>))" when isTupleType(a);
JCode transPrim("subscript", AType r, [AType a, aint()], [str x, str y], JGenie jg)      = "anode_subscript_int(<x>,<y>)" when isNodeType(a);
JCode transPrim("subscript", AType r, [AType a, aint()], [str x, str y], JGenie jg)      = "((<atype2javatype(r)>)aadt_subscript_int(<x>,<y>))" when isADTType(a);

JCode transPrim("subscript", AType r, [AType a, AType b], [str x, str y], JGenie jg)     = "((<atype2javatype(r)>)<x>.get(<y>))" when isListOnlyType(a);
JCode transPrim("subscript", AType r, [AType a, AType b], [str x, str y], JGenie jg)     = "((<atype2javatype(r)>)<x>.get(<y>))" when isMapType(a);

default JCode transPrim("subscript", AType r, [AType a, *AType types], [str x, *str args], JGenie jg) {
    if(arel(atypeList(list[AType] elemTypes)) := a){
        n = size(elemTypes);
        if(n == 2 && !jg.isWildCard(args[0])){
            return isSetLikeType(types[0])  ? "((<atype2javatype(r)>)arel2_subscript1_aset(<x>,<args[0]>))"
                                            : "((<atype2javatype(r)>)arel_subscript1_noset(<x>,<args[0]>))" ;
        } else if(size(args) == 1 && !jg.isWildCard(args[0]) && !isSetLikeType(types[0])){
            return "((<atype2javatype(r)>)arel_subscript1_noset(<x>,<args[0]>))";
        }
        return "((<atype2javatype(r)>)arel_subscript(<x>,<makeIndex(args)>,<makeIndexDescr(types, args, jg)>))";    
    } else if(alrel(atypeList(list[AType] elemTypes)) := a){
        n = size(elemTypes);
        if(n == 2 && !jg.isWildCard(args[0])){
            return isSetLikeType(types[0])  ? "alrel2_subscript1_aset(<x>,<args[0]>)"
                                            : "alrel_subscript1_noset(<x>,<args[0]>)" ;
        } else if(size(args) == 1 && !jg.isWildCard(args[0]) && !isSetLikeType(types[0])){
            return "alrel_subscript1_noset(<x>,<args[0]>)";
        }
        return "alrel_subscript(<x>,<makeIndex(args)>,<makeIndexDescr(types, args, jg)>))";    
    } else
        fail;
}      

JCode makeIndex(list[str] idx)
    = "new IValue[]{<intercalate(", ", idx)>}";

//  return a subscript descriptor: an array with integers for each index: 0: noset, 1: set, 2: wildcard
JCode makeIndexDescr(list[AType] types, list[str] idx, JGenie jg)
    = "new int[]{<intercalate(", ", [jg.isWildCard(idx[i]) ? 2 : isSetLikeType(types[i]) ? 1 : 0 | i <- index(idx)])>}";
    
// TODO: concrete cases
 
// ---- subtract --------------------------------------------------------------

JCode transPrim("subtract", AType r, [AType a, AType b], [str x, str y], JGenie jg)      = "<x>.subtract((<atype2javatype(b)>)<y>)"      when isArithType(a), isArithType(b);
JCode transPrim("subtract", AType r, [AType a, AType b], [str x, str y], JGenie jg)      = "<x>.subtract((<atype2javatype(b)>)<y>)"      when isSetOrListLikeType(a), isSetOrListLikeType(b);
JCode transPrim("subtract", AType r, [AType a, AType b], [str x, str y], JGenie jg)      = "<x>.delete((<atype2javatype(b)>)<y>)"        when isSetOrListLikeType(a), !isSetOrListLikeType(b);
JCode transPrim("subtract", AType r, [AType a, AType b], [str x, str y], JGenie jg)      = "<x>.delete((<atype2javatype(b)>)<y>)"        when isSetOrListLikeType(a), !isSetOrListLikeType(b);
JCode transPrim("subtract", AType r, [AType a, AType b], [str x, str y], JGenie jg)      = "<x>.remove((<atype2javatype(b)>)<y>)"        when isMapType(a), isMapType(b);

// ---- subset ----------------------------------------------------------------

JCode transPrim("subset", AType r, [AType a, AType b], [str x, str y], JGenie jg)        = "<x>.isSubsetOf(<y>)"        when isSetOrListLikeType(a), isSetOrListLikeType(b);

// ---- subsets ---------------------------------------------------------------

JCode transPrim("subsets", AType r, [AType a], [str x], JGenie jg)                       = "new SubSetGenerator(<x>)" when isSetLikeType(a);

// ---- transitive_closure ----------------------------------------------------

JCode transPrim("transitive_closure", AType r, [AType a], [str x], JGenie jg)            = "<x>.asRelation().closure()";

// ---- transitive_reflexive_closure ------------------------------------------

JCode transPrim("transitive_reflexive_closure", AType r, [AType a], [str x], JGenie jg)  = "<x>.asRelation().closureStar()";

JCode transPrim("typeOf", AType r, [AType a], [str x], JGenie jg)                       = "typeOf(<x>)";

// ---- update ----------------------------------------------------------------

list[str] transPrimArgs("update", AType r, [AType a], [MuExp x, MuExp y, MuExp z], JGenie jg)  
                                                                                         = [ trans(x,jg), trans2NativeInt(y,jg), trans(z, jg) ] 
                                                                                           when isListLikeType(a) || (a == astr()) || isTupleType(a) || isNodeType(a);
list[str] transPrimArgs("update", AType r, [AType a], [MuExp x, MuExp y, MuExp z], JGenie jg)  
                                                                                         = [ trans(x,jg), trans2NativeStr(y,jg), trans(z, jg) ]
                                                                                           when isADTType(a);
                                                                                
JCode transPrim("update", AType r, [AType a], [str x, str y, str z], JGenie jg)         = "alist_update(<x>,<y>,<z>)"   
                                                                                          when isListLikeType(a);
JCode transPrim("update", AType r, [AType a], [str x, str y, str z], JGenie jg)         = "amap_update(<x>,<y>,<z>)"    
                                                                                          when isMapType(a);
JCode transPrim("update", AType r, [AType a], [str x, str y, str z], JGenie jg)         = "atuple_update(<x>,<y>,<z>)" 
                                                                                          when isTupleType(a);