module lang::rascalcore::compile::Examples::Tst1



value main()
    = [1, [*int X, int N, *int Y], 3] := [1, [10,20], 3] && N > 10;


///value main() // test bool testList6() 
//    = [1, [*int _, int N, *int _], 3] := [1, [10,20], 3] && N > 10;

//import lang::rascal::\syntax::Rascal;
//
//@doc{Just parse a module at a given location without any furter processing (i.e., fragment parsing) or side-effects (e.g. module loading) }
//public java lang::rascal::\syntax::Rascal::Module parseModule(loc location) = parseModuleWithSpaces(location).top;
//
//@doc{Parse a module (including surounding spaces) at a given location without any furter processing (i.e., fragment parsing) or side-effects (e.g. module loading) }
//@javaClass{org.rascalmpl.library.util.Reflective}
//public java start[Module] parseModuleWithSpaces(loc location);
