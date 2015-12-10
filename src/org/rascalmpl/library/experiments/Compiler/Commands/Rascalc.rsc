module experiments::Compiler::Commands::Rascalc

import String;
import IO;
import ValueIO;
import ParseTree;
import Message;
import util::Reflective;
import experiments::Compiler::Compile;
import experiments::Compiler::Execute;

import experiments::Compiler::RVM::AST;

layout L = [\ \t]* !>> [\ \t];

start syntax CompileArgs 
    = Option* options ModuleName* modulesToCompile;

syntax Option 
    = "--srcPath" Path path
    | "--libPath" Path path
    | "--binDir" Path path
    | "--bootDir" Path path
    | "--noLinking"
    | "--noDefaults"
    | "--jvm"
    | "--verbose"
    | "--version"
    | "--help"
    | fallback: FallbackOption option >> [\-]
    | fallback: FallbackOption option !>> [\-] Path path
    ;
    
lexical FallbackOption
    = "--" [a-zA-Z]+ !>> [a-zA-Z] \ ArgumentNames;
    
keyword ArgumentNames
    = "srcPath"
    | "libPath"
    | "binDir"
    | "bootDir"
    | "noLinking"
    | "noDefaults"
    | "jvm"
    | "verbose"
    | "version"
    | "help"
    ;

lexical ModuleName 
    = rascalName: {NamePart "::"}+
    | fileName: "/"? {NamePart "/"}+ ".rsc"
    ;

lexical NamePart 
    = ([A-Za-z_][A-Za-z0-9_]*) !>> [A-Za-z0-9_];
    
lexical Path 
    = normal: (![\ \t\"\\] | ("\\" ![])) * !>> ![\ \t\"\\]
    | quoted: [\"] InsideQuote [\"]
    ;
lexical InsideQuote = ![\"]*;
    
loc toLocation((Path)`"<InsideQuote inside>"`) = toLocation1("<inside>");
default loc toLocation(Path p) = toLocation1("<p>");

// TODO: the following code does not work in the compiled compiler
//loc toLocation(/^<locPath:[|].*[|]>$/) = readTextValueString(#loc, locPath);
//loc toLocation(/^[\/]<fullPath:.*>$/) = |file:///| + fullPath;
//default loc toLocation(str relativePath) = |cwd:///| + relativePath;

//loc toLocation(str path){
//    println("toLocation: <path>");
//    if(/^<locPath:[|].*[|]>$/ := path){
//       return readTextValueString(#loc, locPath);
//    }
//    if(/^[\/]<fullPath:.*>$/ := path){
//       return |file:///| + fullPath;
//    }
//    return |cwd:///| + path;
//}

loc toLocation1(str path){
    if(path[0] == "|"){
       return readTextValueString(#loc, path);
    }
    if(path[0] == "/"){
       return |file:///| + path[1..];
    }
    return  |cwd:///| + path;
}

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
    
int rascalc(str commandLine) {
    println("rascalc <commandLine>");
    try {
        t = parse(#start[CompileArgs], commandLine).top;
        if (fb <- t.options, fb is fallback) {
            println("error, <fb> is not a recognized option"); 
            printHelp();
            return 1;
        }
        else if ((Option)`--help` <- t.options) {
            printHelp();
            return 0;
        }
        else if ((Option)`--version` <- t.options) {
            printHelp();
            return 0;
        }
        else if (_ <- t.modulesToCompile) {
            pcfg = pathConfig();
            if ((Option)`--noDefaults` <- t.options) {
                if ((Option)`--binDir <Path _>` <- t.options) {
                    pcfg.binDir = |incorrect:///|;
                }
                else {
                    println("A bindir is needed when there are no defaults");
                    return 1;
                }
                 
                if ((Option)`--srcPath <Path _>` <- t.options) {
                    pcfg.srcPath = [];
                }
                else {
                    println("At least one srcPath is needed when there are no defaults");
                    return 1;
                }
                if ((Option)`--bootDir <Path _>` <- t.options) {
                    pcfg.bootDir = |incorrect:///|;
                }
                // lib path can be empty, do not need to check it
                pcfg.libPath = [];
            }
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
            if ((Option)`--bootDir <Path p>` <- t.options) {
                pcfg.bootDir = toLocation(p);
            }

            bool verbose = (Option)`--verbose` <- t.options;
            bool useJVM = (Option)`--jvm` <- t.options;
            bool nolinking = (Option)`--noLinking` <- t.options;

            println("bootDir: <pcfg.bootDir>");
            println("srcPath: <pcfg.srcPath>");
            println("libPath: <pcfg.libPath>");
            println("binDir: <pcfg.binDir>");
           
            set[Message] messages = {};
            for (m <- t.modulesToCompile) {
                moduleName = getModuleName(m);
               
                if(nolinking){
                   println("compiling: <moduleName>");
                   RVMModule rvmModule = compile(moduleName, pcfg, verbose = verbose);
                   messages += rvmModule.messages;
                } else {
                   println("compiling and linking: <moduleName>");
                   RVMProgram rvmProgram = compileAndLink(moduleName, pcfg, useJVM = useJVM, serialize=true, verbose = verbose);
                   messages += rvmProgram.main_module.messages;
                }
            }
            
            for (Message msg <- messages) {
                println(msg);
            }
   
            return error(_,_) <- messages ? 2 : 0;
        }
        else {
            printHelp();
            return 1;
        }
    }
    catch ParseError(loc l): {
        println("Parsing the command line failed:");
        println(commandLine);
        print(("" | it + " " | _ <- [0..l.begin.column]));
        println(("" | it + "^" | _ <- [l.begin.column..l.end.column]));
        print(("" | it + " " | _ <- [0..l.begin.column]));
        println("| around this point");
        return 1;
    }
    catch e: {
        println("Something went wrong:");
        println(e);
        return 2;
    }
}

void printHelp() {
    println("Usage: rascalc [OPTION] ModulesToCompile");
    println("Compile and link one or more modules, the compiler will automatically compile all dependencies.");
    println("Options:");
    println("--srcPath path");
    println("\tAdd new source path, use multiple --srcPaths for multiple paths");
    println("--libPath path");
    println("\tAdd new lib paths, use multiple --libPaths for multiple paths");
    println("--binDir directory");
    println("\tSet the target directory for the bin files");
    println("--bootDir directory");
    println("\tSet the source directory for the boot files");
    println("--noLinking");
    println("\tOnly compile, don\'t link");
    println("--noDefaults");
    println("\tDo not use defaults for the srcPaths, libPaths and binDirs");
    println("--jvm");
    println("\tUse the JVM implemementation of the RVM");
    println("--verbose");
    println("\tMake the compiler verbose");
    println("--help");
    println("\tPrint this help message");
    println("--version");
    println("\tPrint version number");
}
