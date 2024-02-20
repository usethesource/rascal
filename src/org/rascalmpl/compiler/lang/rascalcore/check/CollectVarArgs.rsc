@bootstrapParser
module lang::rascalcore::check::CollectVarArgs

/*
    Check variable arguments
*/

extend lang::rascalcore::check::CheckerCommon;

import lang::rascal::\syntax::Rascal;

void collectAsVarArg(current: (Pattern) `<Type tp> <Name name>`, Collector c){
    uname = unescape("<name>");
    
    if(!isWildCard(uname)){
       if(inPatternNames(uname, c)){
          c.use(name, {formalId()});
          c.require("typed variable pattern", current, [tp, name], 
            void (Solver s){
                nameType = alist(s.getType(tp), alabel=uname);
                s.requireEqual(name, nameType, error(name, "Expected %t for %q, found %q", nameType, uname, name));
            });
       } else {
          c.push(patternNames, <uname, getLoc(name)>);
          c.define(uname, formalOrPatternFormal(c), name, defType([tp], AType(Solver s){ 
            res = alist(s.getType(tp))[alabel=uname];
            return res;
             }));
       }
    }
   c.calculate("var arg", current, [tp], AType(Solver s) { return s.getType(tp)[alabel=uname]; });
   c.enterScope(current);
        collect(tp, c);
   c.leaveScope(current);
}