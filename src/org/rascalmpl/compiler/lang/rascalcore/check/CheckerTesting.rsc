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
    RascalCompilerConfig config = getTypePalCompilerConfig(getDefaultTestingPathConfig());
    ms = getInlineImportAndExtendGraph(pt, config);
    if(debug){
        config = config[logImports = true];
    }
    if(start[Modules] mds := pt){
        <tm, ms> = rascalTModelComponent( { unescape("<md.header.name>") | md <- mds.top.modules }, ms);
        return tm;
    } else 
    if(Modules mds := pt){
        <tm, ms> = rascalTModelComponent( { unescape("<md.header.name>") | md <- mds.modules }, ms);
        return tm;
    } else
        throw "Cannot handle Module";
}

void testModules(str names...) {
    if(isEmpty(names)) names = allTests;
    runTests([|project://rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/check/tests-ttl/<name>.ttl| | str name <- names], #Modules, rascalTModelForTestModules, verbose=true);
}

list[str] allTests = ["adt", "adtparam", "alias", "assignment", "datadecl", "exp", "fields", "fundecl", 
                     "imports", "operators", "pat", "scope", "splicepats", "stats"/*,"syntax1", "syntax2", "syntax3"*/];
                     
void main() = testModules();