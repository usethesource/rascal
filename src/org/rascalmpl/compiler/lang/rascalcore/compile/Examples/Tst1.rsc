module lang::rascalcore::compile::Examples::Tst1

import Set;
import List;

value main(){
    xxx = {"ignore", "Ignore", "ignoreCompiler", "IgnoreCompiler"};
    yyy = [];
    zzz = xxx & yyy;
    ignored = !isEmpty(zzz);
    return ignored;
}    
    
//
//import lang::rascal::\syntax::Rascal;
//
//@doc{Just parse a module at a given location without any furter processing (i.e., fragment parsing) or side-effects (e.g. module loading) }
//public java lang::rascal::\syntax::Rascal::Module parseModule(loc location) = parseModuleWithSpaces(location).top;
//
//@doc{Parse a module (including surounding spaces) at a given location without any furter processing (i.e., fragment parsing) or side-effects (e.g. module loading) }
//@javaClass{org.rascalmpl.library.util.Reflective}
//public java start[Module] parseModuleWithSpaces(loc location);

//value main() // test bool testList6() 
//    = [1, [*int _, int N, *int _], 3] := [1, [10,20], 3] && N > 10;