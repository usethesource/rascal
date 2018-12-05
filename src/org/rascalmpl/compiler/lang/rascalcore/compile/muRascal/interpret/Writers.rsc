module lang::rascalcore::compile::muRascal::interpret::Writers

import lang::rascalcore::check::AType;
import lang::rascalcore::compile::muRascal::interpret::RValue;
import lang::rascalcore::compile::muRascal::interpret::Env;

// ListWriter

data Writer
    = writer(
        void (value v) add,
        void (value v) splice,
        RValue() close
      );
      
Writer makeListWriter(){
    list[value] lst = [];
    void add(value v) { lst += [v]; }
    void splice(value elms) { if(list[value] lelms:= elms) lst += lelms; else throw "ListWriter.splice: <elms>"; }
    RValue close() = rvalue(lst);
    
    return writer(add, splice, close);
}

Result evalPrim("listwriter_open", [], Env env)
    = <rvalue(makeListWriter()), env>;
   
Result evalPrim("listwriter_add", [rvalue(Writer w), rvalue(v)], Env env){
    w.add(v);
    return <rvalue(w), env>;
}

Result evalPrim("listwriter_splice",  [rvalue(Writer w), rvalue(v)], Env env){
    w.splice(v);
    return <rvalue(w), env>;
}
    
Result evalPrim("listwriter_close",  [rvalue(Writer w)], Env env)
    = <rvalue(w.close()), env>;

// SetWriter

Writer makeSetWriter(){
    set[value] st = {};
    void add(value v) { st += {v}; }
    void splice(value elms) { if(set[value] selms := elms) st += elms; else throw "SetWriter.splice: <elms>"; }
    RValue close() = rvalue(st);
    
    return writer(add, splice, close);
}

Result evalPrim("setwriter_open", [], Env env)
    = <rvalue(makeSetWriter()), env>;
   
Result evalPrim("setwriter_add", [rvalue(Writer w), rvalue(v)], Env env){
    w.add(v);
    return <rvalue(w), env>;
}

Result evalPrim("setwriter_splice",  [rvalue(Writer w), rvalue(v)], Env env){
    w.splice(v);
    return <rvalue(w), env>;
}
    
Result evalPrim("setwriter_close",  [rvalue(Writer w)], Env env)
    = <rvalue(w.close()), env>;
    
// MapWriter

Writer makeMapWriter(){
    map[value, value] mp = ();
    void add(value v) { if(<key, val> := v) mp[key] = val; else throw "MapWriter.add: <v>"; }
    void splice(value elms) { if(set[value] selms := elms) st += elms; else throw "MapWriter.splice: <elms>"; }
    RValue close() = rvalue(mp);
    
    return writer(add, splice, close);
}

Result evalPrim("mapwriter_open", [], Env env)
    = <rvalue(makeMapWriter()), env>;
   
Result evalPrim("mapwriter_add", [rvalue(Writer w), rvalue(key), rvalue(val)], Env env){
    w.add(<key,val>);
    return <rvalue(w), env>;
}

Result evalPrim("mapwriter_splice",  [rvalue(Writer w), rvalue(v)], Env env){
    w.splice(v);
    return <rvalue(w), env>;
}
    
Result evalPrim("mapwriter_close",  [rvalue(Writer w)], Env env)
    = <rvalue(w.close()), env>;