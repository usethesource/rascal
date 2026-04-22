module lang::rascalcore::check::tests::BreakingTest

import IO;
import ValueIO;
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
loc root = |memory://36a14c42-e4e6-41e0-a59a-ac11f637070c|;

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
        "module TestModule612d1\n \n &T \<: int f(&T \<: num _) = 1;"
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
    locs = [ v | /value v := m ];
    println("Found <size(locs)> subvalues");
    locsSet = {*locs};
    println("Became: <size(locsSet)> subvalues when putting in set");
    for (l <- locs) {
        bool found = false;
        for (l2 <- locsSet, l1 == l2, "<l2>" == "<l>") {
            found = true;
        }
        if (!found) {
            println("❌ <l> got dropped from set");
        }
    }
}

void findTModelCollisions() {
    t = readBinaryValueFile(pcfg.bin + "rascal/$<moduleName>.tpl");
    map[int, str] seen = ();
    for (/value v := t) {
        h = getHashCode(v);
        s = "<v>";
        if (h notin seen) {
            seen[h] = s;
            continue;
        }
        e = seen[h];
        if (s != e && v != {} && v != []) {
            if ("[<v>]" == e || "{<v>}" == e) {
                continue;
            }
            if ("[<e>]" == s || "{<e>}" == s) {
                continue;
            }

            println("⚠️ hash collision between: ");
            println(e);
            println("and");
            println(s);
        }
    }

}


void main() {
    remove(root, recursive = true);
    l = writeModule();
    typecheckModule(l);
    //findCollission(l);
    //findTModelCollisions();
}