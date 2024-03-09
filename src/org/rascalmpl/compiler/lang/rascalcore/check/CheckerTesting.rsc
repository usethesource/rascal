@bootstrapParser
module lang::rascalcore::check::CheckerTesting

import lang::rascal::\syntax::Rascal;

import lang::rascalcore::check::Checker;
import analysis::typepal::TypePal;
import analysis::typepal::TestFramework;

start syntax Modules
    = Module+ modules;

// ---- Testing ---------------------------------------------------------------

TModel rascalTModelForTestModules(Tree pt, bool debug=false){
    ms = getInlineImportAndExtendGraph(pt, getDefaultTestingPathConfig());
    TypePalConfig config=rascalTypePalConfig(getDefaultTestingPathConfig());
    if(debug){
        config = config[logImports = true];
    }
    if(start[Modules] mds := pt){
        return rascalTModelComponent( {unescape("<md.header.name>") | md <- mds.top.modules }, ms, config)[1];
    } else if(Modules mds := pt){
        return rascalTModelComponent( {unescape("<md.header.name>") | md <- mds.modules }, ms, config)[1];
    } else
        throw "Cannot handle Modules";
}

void testModules(str names...) {
    if(isEmpty(names)) names = allTests;
    runTests([|project://rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/check/tests-ttl/<name>.ttl| | str name <- names], #Modules, rascalTModelForTestModules, verbose=false);
}

list[str] allTests = ["adt", "adtparam", "alias", "assignment", "datadecl", "exp", "fields", "fundecl", 
                     "imports", "operators", "pat", "scope", "splicepats", "stats"/*,"syntax1", "syntax2", "syntax3"*/];
                     
value main() = testModules();