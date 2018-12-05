module lang::rascalcore::compile::muRascal::interpret::Env

import List;
import lang::rascalcore::compile::muRascal::interpret::RValue;
import lang::rascalcore::compile::muRascal::interpret::JavaGen;
import lang::rascalcore::check::AType;

bool debug = false;

// ---- Environments and variables --------------------------------------------

alias Frame = tuple[str frameName, list[RValue] stack, map[str,RValue] temporaries];

data Env
    = environment(map[str,RValue] moduleVars, list[Frame] frames);
    
alias Result = tuple[RValue val, Env env];

// get/assign variables 

RValue getValue(muVar(str name, str fuid, int pos), Env env){
    for(<str frameName, list[RValue] stack, map[str,RValue] temporaries> <- env.frames){
        if(fuid == frameName){
            if(debug) println("getVar: <name>, <pos>, <stack[pos]>");
            return stack[pos];
        }
    }
    return env.moduleVars[name];
}

Result assignValue(muVar(str name, str fuid, int pos), RValue v, Env env){
    for(int i <- index(env.frames)){
        <frameName, stack, temporaries> = env.frames[i];
        if(fuid == frameName){
            stack[pos] = v;
            res = <v, environment(env.moduleVars, env.frames[0 .. i] + <frameName, stack, temporaries> + env.frames[i+1 ..])>;
            return res;
        }
    }
    env.moduleVars[name] = v;
    return < v, env >;
}

JCode assignValue(muVar(str name, str fuid, int pos), str v, JGenie jg){
    return "<name> = <v>;";
}

// get/assign locals 

RValue getValue(muLoc(str name, int pos), Env env){
    return env.frames[0].stack[pos];
}

Result assignValue(muLoc(str name, int pos), RValue v, Env env){
    env.frames[0].stack[pos] = v;
    return <v, env>;
}

// get/assign temporaries

RValue getValue(muTmp(str name, str fuid), Env env){
    for(<str frameName, list[RValue] stack, map[str,RValue] temporaries> <- env.frames){
        if(fuid == frameName){
            if (debug) println("getTmp: <name>, <temporaries[name]>");
            return temporaries[name];
        }
    }
    throw "getTmp: <name>";
}

Result assignValue(muTmp(str name, str fuid), RValue v, Env env){
    for(int i <- index(env.frames)){
        <frameName, stack, temporaries> = env.frames[i];
        if(fuid == frameName){
            temporaries[name] = v;
            res = <v, environment(env.moduleVars, env.frames[0 .. i] + <frameName, stack, temporaries> + env.frames[i+1 ..])>;
            return res;
        }
    }
    throw "assignTmp: <name>";
}

// get/assign int temporaries

RValue getValue(muTmpInt(str name, str fuid), Env env){
    for(<str frameName, list[RValue] stack, map[str,RValue] temporaries> <- env.frames){
        if(fuid == frameName){
            if (debug) println("getTmp: <name>, <temporaries[name]>");
            return temporaries[name];
        }
    }
    throw "getTmp: <name>";
}

Result assignValue(muTmpInt(str name, str fuid), RValue v, Env env){
    for(int i <- index(env.frames)){
        <frameName, stack, temporaries> = env.frames[i];
        if(fuid == frameName){
            temporaries[name] = v;
            res = <v, environment(env.moduleVars, env.frames[0 .. i] + <frameName, stack, temporaries> + env.frames[i+1 ..])>;
            return res;
        }
    }
    throw "assignTmp: <name>";
}

// get/assign writer temporaries

RValue getValue(muTmpWriter(str name, str fuid), Env env){
    for(<str frameName, list[RValue] stack, map[str,RValue] temporaries> <- env.frames){
        if(fuid == frameName){
            if (debug) println("getTmp: <name>, <temporaries[name]>");
            return temporaries[name];
        }
    }
    throw "getTmp: <name>";
}

Result assignValue(muTmpWriter(str name, str fuid), RValue v, Env env){
    for(int i <- index(env.frames)){
        <frameName, stack, temporaries> = env.frames[i];
        if(fuid == frameName){
            temporaries[name] = v;
            res = <v, environment(env.moduleVars,env.frames[0 .. i] + <frameName, stack, temporaries> + env.frame[i+1 ..])>;
            return res;
        }
    }
    throw "assignTmp: <name>";
}

// get/Assign keyword parameters

Result getValue(muVarKwp(str name, str fuid, AType atype), Env env){
    for(<str frameName, list[RValue] stack, map[str,RValue] temporaries> <- env.frames){
        if(fuid == frameName){
            nargs = muFunctions[fuid].nlocals;
            ikwactuals = nargs - 2;
            ikwdefaults = nargs - 1;
            if(rvalue(map[str, RValue] kwactuals) := stack[ikwactuals] &&
               rvalue(map[str, tuple[AType, RValue]] kwdefaults) := stack[ikwdefaults]){
               if(kwactuals[name]?) return <kwactuals[name], env>;
               return <kwdefaults[name]<1>, env>;
            } else {
                throw "getKwp, wrong stack values: <stack>";
            }
        }
    }
    throw "getKwp: <name>, <fuid>";
}

JCode  getValue(muVarKwp(str name, str fuid, AType atype), JGenie jg){
    return "(<atype2java(atype)>) ($kwpActuals.containsKey(\"<name>\") ? $kwpActuals.get(\"<name>\") : <jg.getKwpDefaults()>.get(\"<name>\"))";
}

Result assignValue(muVarKwp(str name, str fuid, AType atype), RValue v, Env env){
    for(int i <- index(env.frames)){
        <frameName, stack, temporaries> = env.frames[i];
        if(fuid == frameName){
            nargs = muFunctions[fuid].nlocals;
            ikwactuals = nargs - 2;
            if(rvalue(map[str,RValue] mp) := stack[ikwactuals]){
                mp[name] = v;
                stack[ikwactuals] = rvalue(mp);
                res = <v, environment(env.moduleVars, env.farmes[0 .. i] + <frameName, stack, temporaries> + env.farmes[i+1 ..])>;
                return res;
            } else {
                throw "assignKwp: illegal kwactuals: <stack>";
            }
        }
    }
    throw "assignVariable: <name>";
}

JCode assignValue(muVarKwp(str name, str fuid, AType atype), str v, JGenie jg){
    return "$kwpActuals.put(\"<name>\", <v>)";
}