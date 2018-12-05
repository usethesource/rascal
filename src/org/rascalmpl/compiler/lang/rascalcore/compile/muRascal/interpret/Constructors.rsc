module lang::rascalcore::compile::muRascal::interpret::Constructors

import List; 
import lang::rascalcore::check::AType;
import lang::rascalcore::compile::muRascal::interpret::RValue;
import lang::rascalcore::compile::muRascal::interpret::Env;
import lang::rascalcore::compile::muRascal::interpret::JavaGen;

// Constructors

data Constructor
    = constructor(AType ctype, list[value] fields, map[str,value] kwparams)
    ;

Constructor makeConstructor(AType ctype, list[RValue] args){
    if(size(ctype.fields) != size(args) - 1) throw "makeConstructor: nargs";
    if(rvalue(map[str,value] kwmap) := args[-1]){
        return constructor(ctype, [v | rvalue(v) <- args[0..-1]], kwmap);
    } else
        throw "makeConstructor: kwmap";
}

JCode makeConstructor(AType ctype, list[str] args){
    if(isEmpty(ctype.kwFields))
        return "$VF.constructor(<ctype.adt.adtName>_<ctype.label>, new IValue[]{<intercalate(", ", args)>})";
    return "$VF.constructor(<ctype.adt.adtName>_<ctype.label>, new IValue[]{<intercalate(", ", args[0..-1])>}, <args[-1]>)";
}

Constructor makeConstructor(AType ctype, list[value] args, map[str,value] kwmap){
    if(size(ctype.fields) != size(args)) throw "makeConstructor: nargs";
        return constructor(ctype, args, kwmap);
 
}

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

Result evalPrim("aadt_field_update", [rvalue(Constructor cons), rvalue(str fieldName), RValue repl], Env env){
    fields = cons.ctype.fields;
    for(int i <- index(fields)){
        fld = fields[i];
        if(fld.label == fieldName){
            cons.fields[i] = repl;
            return <rvalue(cons), env>;
        }
    }
    cons.kwparams[fieldName] = repl;
    return <rvalue(cons), env>;
}