@bootstrapParser
module lang::rascalcore::check::CollectVarArgs

//extend analysis::typepal::TypePal;
extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeUtils;
import analysis::typepal::FailMessage;
import lang::rascal::\syntax::Rascal;
//import lang::rascalcore::check::NameUtils;
import lang::rascalcore::check::ScopeInfo;
import lang::rascalcore::check::BasicRascalConfig;


void collectAsVarArg(current: (Pattern) `<Type tp> <Name name>`, Collector c){
    uname = unescape("<name>");
    
    if(uname != "_"){
       if(inPatternNames(uname, c)){
          c.use(name, {formalId()});
          c.require("typed variable pattern", current, [tp, name], 
            void (Solver s){
                nameType = alist(s.getType(tp), label=uname);
                s.requireEqual(name, nameType, error(name, "Expected %t for %q, found %q", nameType, uname, name));
            });
       } else {
          c.push(patternNames, <uname, getLoc(name)>);
          c.define(uname, formalOrPatternFormal(c), name, defType([tp], AType(Solver s){ 
            res = alist(s.getType(tp))[label=uname];
            return res;
             }));
       }
    }
   c.calculate("var arg", current, [tp], AType(Solver s) { return s.getType(tp)[label=uname]; });
   c.enterScope(current);
        collect(tp, c);
   c.leaveScope(current);
}