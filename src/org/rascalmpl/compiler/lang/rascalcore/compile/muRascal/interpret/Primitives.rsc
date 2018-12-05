module lang::rascalcore::compile::muRascal::interpret::Primitives

import lang::rascalcore::check::AType;
import lang::rascalcore::compile::muRascal::interpret::RValue;
import lang::rascalcore::compile::muRascal::interpret::Env;

import lang::rascalcore::compile::muRascal::interpret::JavaGen;

// MuPrimitives

//Result evalMuPrim("check_arg_type_and_copy", [rvalue(int from), rtype(AType tp), rvalue(int to)], Env env){
//    arg = env.frames[0].stack[from];
//    if(getType(arg) != tp) return <rvalue(false), env>;
//    env.frames[0].stack[to] = arg;
//    return <rvalue(true), env>;
//}



//JCode transPrim("check_arg_type_and_copy", int from, AType tp

AType getType(rvalue(v)) = getType(v);

AType getType(int n) = aint();
AType getType(list[&T] lst) = isEmpty(lst) ? alist(avoid()) : alist(getType(typeof(lst[0]))); // TODO

// Rascal primitives

Result evalPrim("aint_add_aint", [rvalue(int x), rvalue(int y)], Env env)           = <rvalue(x + y), env>;
JCode transPrim("aint_add_aint", [str x, str y], JGenie jg)                         = "<x>.add(<y>)";

Result evalPrim("aint_subtract_aint", [rvalue(int x), rvalue(int y)], Env env)      = <rvalue(x - y), env>;
JCode transPrim("aint_subtract_aint", [str x, str y], JGenie jg)                    = "<x>.subtract(<y>)";

Result evalPrim("aint_product_aint", [rvalue(int x), rvalue(int y)], Env env)       = <rvalue(x * y), env>;
JCode transPrim("aint_product_aint", [str x, str y], JGenie jg)                     = "<x>.multiply(<y>)";

Result evalPrim("aint_greater_aint", [rvalue(int x), rvalue(int y)], Env env)       = <rvalue(x > y), env>;
JCode transPrim("aint_greater_aint", [str x, str y], JGenie jg)                     = "<x>.greater(<y>)";

Result evalPrim("aint_greaterequal_aint", [rvalue(int x), rvalue(int y)], Env env)  = <rvalue(x >= y), env>;

Result evalPrim("aint_less_aint", [rvalue(int x), rvalue(int y)], Env env)          = <rvalue(x < y), env>;

Result evalPrim("aint_lessequal_aint", [rvalue(int x), rvalue(int y)], Env env)     = <rvalue(x <= y), env>;
JCode  transPrim("aint_lessequal_aint", [str x, str y], JGenie jg)                  = "<x>.lessEqual(<y>)";

Result evalPrim("equal", [rvalue(value x), rvalue(value y)], Env env)               = <rvalue(x == y), env>;

Result evalPrim("list_create", list[RValue] elms, Env env)
    = <rvalue([ v | rvalue(v) <- elms]), env>;
    
Result evalPrim("set_create", list[RValue] elms, Env env)
    = <rvalue({ v | rvalue(v) <- elms}), env>;
    
Result evalPrim("tuple_create", [rvalue(v1)], Env env)
    = <rvalue(<v1>), env>;
    
Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2)], Env env)
    = <rvalue(<v1, v2>), env>;
    
Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3)], Env env)
    = <rvalue(<v1, v2, v3>), env>;
    
Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4)], Env env)
    = <rvalue(<v1, v2, v3, v4>), env>;
    
Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4), rvalue(v5)], Env env)
    = <rvalue(<v1, v2, v3, v4, v5>), env>;

Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4), rvalue(v5), rvalue(v6)], Env env)
    = <rvalue(<v1, v2, v3, v4, v5, v6>), env>;

Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4), rvalue(v5), rvalue(v6), rvalue(v7)], Env env)
    = <rvalue(<v1, v2, v3, v4, v5, v6, v7>), env>;
    
Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4), rvalue(v5), rvalue(v6), rvalue(v7), rvalue(v8)], Env env)
    = <rvalue(<v1, v2, v3, v4, v5, v6, v7, v8>), env>;

Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4), rvalue(v5), rvalue(v6), rvalue(v7), rvalue(v8), rvalue(v9)], Env env)
    = <rvalue(<v1, v2, v3, v4, v5, v6, v7, v8, v9>), env>;
    
Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4), rvalue(v5), rvalue(v6), rvalue(v7), rvalue(v8), rvalue(v9), rvalue(v10)], Env env)
    = <rvalue(<v1, v2, v3, v4, v5, v6, v7, v8, v, v10>), env>;   
    
Result evalPrim("node_create", [rvalue(str name), *RValue args, rvalue(map[str, RValue] kwmap)], Env env)
    = makeNode(name, [v | rvalue(v) <- args], keywordParameters = (k : v | k <- kwmap, bprint(v), rvalue(v) := kwmap[k]));
