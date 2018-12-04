module lang::rascalcore::compile::muRascal2Java::Env

import List;
import lang::rascalcore::compile::muRascal2Java::RValue;
import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::check::AType;
import lang::rascalcore::compile::muRascal::AST;

bool debug = false;

// ---- Environments and variables --------------------------------------------

alias Frame = tuple[str frameName, list[RValue] stack, map[str,RValue] temporaries];

data Env
    = environment(map[str,RValue] moduleVars, list[Frame] frames);
    
alias Result = tuple[RValue val, Env env];

// get/assign variables 

JCode assignValue(muVar(str name, str fuid, int pos, AType atype), str v, JGenie jg){
    return "<name> = <v>;\n";
}

// get/assign locals 

//RValue getValue(muLoc(str name, int pos), Env env){
//    return env.frames[0].stack[pos];
//}
//
//Result assignValue(muLoc(str name, int pos), RValue v, Env env){
//    env.frames[0].stack[pos] = v;
//    return <v, env>;
//}

// get/assign temporaries

JCode getValue(muTmp(str name, str fuid, AType atype), JGenie jg)
    = name;


JCode assignValue(muTmp(str name, str fuid, AType atype), str v, JGenie jg)
    = "<atype2java(atype)> <name> = <v>;\n";

// get/assign int temporaries

JCode getValue(muTmpInt(str name, str fuid), JGenie jg)
    = name;

JCode assignValue(muTmpInt(str name, str fuid), str v, JGenie jg)
    = "int <name> = <v>;\n";

// get/assign writer temporaries


JCode getValue(muTmpWriter(str name, str fuid), JGenie jg)
    = name;

JCode assignValue(muTmpWriter(str name, str fuid), str v, JGenie jg)
    = "IWriter <name> = <v>;\n";

// get/Assign keyword parameters

JCode  getValue(muVarKwp(str name, str fuid, AType atype), JGenie jg){
    return "(<atype2java(atype)>) ($kwpActuals.containsKey(\"<name>\") ? $kwpActuals.get(\"<name>\") : <jg.getKwpDefaults()>.get(\"<name>\"))";
}

JCode assignValue(muVarKwp(str name, str fuid, AType atype), str v, JGenie jg){
    return "$kwpActuals.put(\"<name>\", <v>)";
}