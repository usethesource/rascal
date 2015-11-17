module experiments::Compiler::Commands::Rascal

import String;
import IO;
import ParseTree;
import util::Reflective;
import experiments::Compiler::Execute;

layout L = [\ \t]* !>> [\ \t];

start syntax CompileArgs 
    = Option* options ModuleName moduleToCompile CommandArgument* commandArguments;

syntax Option 
    = "--srcPath" Path path
    | "--libPath" Path path
    | "--binDir" Path path
    | "--jvm"
    | "--verbose"
    | "--version"
    | "--help"
    | "--debug"
    | "--debugRVM"
    | "--testsuite"
    | "--profile"
    | "--trackCalls"
    | "--coverage"
    | "--serialize"
    ;

syntax CommandArgument
    = "--" ArgName argName ArgValue argValue
    ;

lexical ArgName
    = [a-zA-Z] [A-Za-z0-9_]*!>> [A-Za-z0-9_];
    
lexical ArgValue
    = ![\ \t]* !>>[\ \t];
    
lexical ModuleName 
    = rascalName: {NamePart "::"}+
    | fileName: "/"? {NamePart "/"}+ ".rsc"
    ;

lexical NamePart 
    = ([A-Za-z_][A-Za-z0-9_]*) !>> [A-Za-z0-9_];
    
lexical Path 
    = normal: (![ \t\n\"\'\\] | ("\\" ![])) * !>> ![ \t\n\"\'\\]
    | quoted: [\"] InsideQuote [\"]
    ;
lexical InsideQuote = ![\"]*;
    
loc toLocation((Path)`"<InsideQuote inside>"`) = toLocation("<inside>");
default loc toLocation(Path p) = toLocation("<p>");

loc toLocation(/^<fullPath:[\/].*>$/) = |file:///| + fullPath;
default loc toLocation(str relativePath) = |cwd:///| + relativePath;

str getModuleName(ModuleName mn) {
    result = "<mn>";
    if (mn is rascalName) {
        return result;
    }
    if (startsWith(result, "/")) {
        result = result[1..];
    }
    return replaceAll(result, "/", "::")[..-4];
}
    
value rascal(str commandLine) {
    try {
        t = ([start[CompileArgs]]commandLine).top;
        if ((Option)`--help` <- t.options) {
            printHelp();
            return 0;
        }
        else if ((Option)`--version` <- t.options) {
            printHelp();
            return 0;
        }
        else {
            pcfg = pathConfig();
            pcfg.libPath = [ toLocation(p) | (Option)`--libPath <Path p>` <- t.options ] + pcfg.libPath;
            if ((Option)`--srcPath <Path _>` <- t.options) {
                pcfg.srcPath = [ toLocation(p) | (Option)`--srcPath <Path p>` <- t.options ] + pcfg.srcPath;
            }
            else {
                pcfg.srcPath = [|cwd:///|, *pcfg.srcPath];
            }
            if ((Option)`--binDir <Path p>` <- t.options) {
                pcfg.binDir = toLocation(p);
            }

            bool verbose = (Option)`--verbose` <- t.options;
            bool useJVM = (Option)`--jvm` <- t.options;
            bool debug = (Option)`--debug` <- t.options;
            bool debugRVM = (Option)`--debugRVM` <- t.options;
            bool testsuite = (Option)`--testsuite` <- t.options;
            bool profile = (Option)`--profile` <- t.options;
            bool trackCalls = (Option)`--trackCalls` <- t.options;
            bool coverage = (Option)`--coverage` <- t.options;
            bool serialize = (Option)`--serialize` <- t.options;

            moduleName = getModuleName(t.moduleToCompile);
            
            args = ("<argName>" : "<argValue>" | (CommandArgument) `--<ArgName argName> <ArgValue argValue>` <- t.commandArguments);
            println("executing: <moduleName> <args>");
            return execute(moduleName, pcfg, keywordArguments = args,
                                       useJVM = useJVM, serialize=serialize, verbose = verbose,
                                       debug = debug, debugRVM = debugRVM, testsuite = testsuite, 
                                       profile = profile, trackCalls = trackCalls, coverage = coverage
                          );
        }
    }
    catch e: {
        println("Something went wrong:");
        println(e);
        return 2;
    }
}

void printHelp() {
    println("Usage: rascal [OPTION] ModulesToCompile");
    println("Compile and link one or more modules, the compiler will automatically compile all dependencies.");
    println("Options:");
    println("--srcPath path");
    println("\tAdd new source path, use multiple --srcPaths for multiple paths");
    println("--libPath path");
    println("\tAdd new lib paths, use multiple --libPaths for multiple paths");
    println("--binDir directory");
    println("\tSet the target directory for the bin files");
    println("--jvm");
    println("\tUse the JVM implemementation of the RVM");
    println("--verbose");
    println("\tMake the compiler verbose");
    println("--help");
    println("\tPrint this help message");
    println("--version");
    println("\tPrint version number");
}
