module lang::rascalcore::check::tests::BreakingTest

import IO;
import String;
import Location;
import Message;
import Set;
import util::Reflective;
import ParseTree;
import lang::rascalcore::check::RascalConfig;

import lang::rascalcore::check::Checker;
import lang::rascal::\syntax::Rascal;


// this uuid matters
loc root = |memory://6e17d46a-06e9-42aa-bd98-182ca2dbd8d3/|;
PathConfig pcfg = pathConfig(
    srcs = [root + "src"],
    bin = root + "bin",
    libs = []
);
// this name matters
str moduleName = "TestModule612d1";

loc writeModule() {
    loc moduleLoc = pcfg.srcs[0] + "<moduleName>.rsc";
    // the spaces before &T seems to matter?
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

void findCollission(loc l) {
    m = parseModule(l);
    locs = [ t.src | /Tree t := m, t.src?];
    println("Found <size(locs)> locs");
    locsSet = {*locs};
    println("Became: <size(locsSet)> locs when putting in set");
    for (l <- locs) {
        bool found = false;
        for (l2 <- locsSet, "<l2>" == "<l>") {
            found = true;
        }
        if (!found) {
            println("❌ <l> got dropped from set");
        }
    }
}


void main() {
    remove(root, recursive = true);
    l = writeModule();
    typecheckModule(l);
    findCollission(l);
}