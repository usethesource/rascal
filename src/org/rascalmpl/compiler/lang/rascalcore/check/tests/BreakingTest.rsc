module lang::rascalcore::check::tests::BreakingTest

import IO;
import String;
import Location;
import Message;
import Relation;
import Set;
import util::Reflective;
import ParseTree;
import util::FileSystem;
import lang::rascalcore::check::RascalConfig;

import lang::rascalcore::check::Checker;
import lang::rascal::\syntax::Rascal;

import analysis::typepal::LocationChecks;


loc root = |memory://6e17d46a-06e9-42aa-bd98-182ca2dbd8d3/|;
PathConfig pcfg = pathConfig(
    srcs = [root + "src"],
    bin = root + "bin",
    libs = []
);
str moduleName = "TestModule612d1";

loc writeModule() {
    loc moduleLoc = pcfg.srcs[0] + "<moduleName>.rsc";
    // the space before &T seems to matter!
    writeFile(moduleLoc, 
        "module <moduleName>
        '   &T \<: int f(&T \<: num _) = 42;
        '"
    );
    return moduleLoc;
}

set[Message] getErrorMessages(ModuleStatus r)
    =  { m | m <- getAllMessages(r), m is error };

set[Message] getWarningMessages(ModuleStatus r)
    = { m | m <- getAllMessages(r), m is warning };

set[Message] getAllMessages(ModuleStatus r)
	= { m | mname <- r.messages, m <- r.messages[mname] };

bool typecheckModule(loc m) {
    status = rascalTModelForLocs([m], rascalCompilerConfig(pcfg)[infoModuleChecked=true][verbose=true], dummy_compile1);
    println("All messages:");
    iprintln(getAllMessages(status));
    if (e <- getErrorMessages(status)) {
        println("Errors typechecking: ");
        iprintln(getErrorMessages(status));
        println("❌ Typecheck failed");
        return false;
    }
    else {
        println("✅ Typecheck success");
        return true;
    }
}


void main() {
    l = writeModule();
    typecheckModule(l);
}