module lang::rascalcore::compile::muRascal2Java::Constructors

import List; 
import lang::rascalcore::check::AType;
import lang::rascalcore::compile::muRascal2Java::RValue;
import lang::rascalcore::compile::muRascal2Java::Env;
import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal::AST;

// Constructors

data Constructor
    = constructor(AType ctype, list[value] fields, map[str,value] kwparams)
    ;


//JCode makeConstructor(AType ctype, list[str] args){
//    if(isEmpty(ctype.kwFields))
//        return "$VF.constructor(<ctype.adt.adtName>_<ctype.label>, new IValue[]{<intercalate(", ", args)>})";
//    return "$VF.constructor(<ctype.adt.adtName>_<ctype.label>, new IValue[]{<intercalate(", ", args[0..-1])>}, <args[-1]>)";
//}

Result evalPrim("aadt_field_access", [rvalue(Constructor cons), rvalue(str fieldName)], Env env){
    fields = cons.ctype.fields;
    for(int i <- index(fields)){
        fld = fields[i];
        if(fld.label == fieldName){
            return <rvalue(cons.fields[i]), env>;
        }
    }
    return <rvalue(cons.kwparams[fieldName]), env>;
}

//JCode trans(muFieldAccess("aadt", MuExp cons, str fieldName), JGenie jg)
//    = "<trans(cons, jg)>.get(\"<fieldName>\")";
//JCode transPrim("aadt_field_access", [str cons, str fieldName], JGenie jg)
//    = "<cons>.get(\"<fieldName>\")";

//Result evalPrim("aadt_field_update", [rvalue(Constructor cons), rvalue(str fieldName), RValue repl], Env env){
//    fields = cons.ctype.fields;
//    for(int i <- index(fields)){
//        fld = fields[i];
//        if(fld.label == fieldName){
//            cons.fields[i] = repl;
//            return <rvalue(cons), env>;
//        }
//    }
//    cons.kwparams[fieldName] = repl;
//    return <rvalue(cons), env>;
//}