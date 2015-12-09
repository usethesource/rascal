module experiments::Compiler::RVM::Compare

import IO;
import ValueIO;
import String;
import Set;
import util::Reflective;
import util::FileSystem;
import experiments::Compiler::Execute;
import experiments::Compiler::Compile;
import experiments::Compiler::CompileMuLibrary; 

import experiments::Compiler::Commands::Rascalc;

tuple[set[loc] differ, set[loc] missing] compare(loc given_loc, str ibin, str cbin){
   if(contains(given_loc.path, ibin)){
       iloc = given_loc;
       cloc = iloc[path = replaceFirst(iloc.path, ibin, cbin)];
       return compare(iloc, cloc);
   } else if(contains(given_loc.path, cbin)){
       cloc = given_loc;
       iloc = cloc.path[path=replaceFirst(cloc.path, cbin, ibin)];
       return compare(iloc, cloc);
   }
   throw "No <ibin> or <cbin> found in <given_loc>";
}

tuple[set[loc] differ, set[loc] missing] compare(loc iloc, loc cloc){

    if(!exists(iloc)){
        println("MISSING: <iloc>");
        return <{}, {iloc}>;
    }
    if(!exists(cloc)){
      println("MISSING: <cloc>");
       return <{}, {cloc}>;
    }
   
    ival = readBinaryValueFile(#value, iloc);
    cval = readBinaryValueFile(#value, cloc);
    
    res = diff(ival, cval);
    if(res == "no diff"){
       return <{}, {}>;
    } else  {
       println("<iloc>,<cloc>: <res>");
       return <{iloc}, {}>;
    }
}

void compareAll(str ibin, str cbin){
    loc ibinDir = |compressed+home:///| + ibin;
    loc cbinDir = |compressed+home:///| + cbin;
    serialized = {};
    differ = {};
    missing = {};
    for(loc iloc <- files(ibinDir), iloc.extension == "gz"){
        if(!contains(iloc.path, ".ser.")){
           <d, m> = compare(iloc, ibin, cbin);
           differ += d;
           missing += m;
        } else {
          serialized += iloc;
        }
    }
    
    println("\n+++++++++++++++++++++++++++++++\n");
    
    println("Number of files: <size(files(ibinDir))>");
    
    println("Different: <size(differ)>");
    for(d <- differ) println("\t<d>");
    
    println("\nSkipped serialized: <size(serialized)>");
    for(s <- serialized) println("\t<s>");
    
     println("\nMissing: <size(missing)>");
    for(m <- missing) println("\t<m>");
}

loc BOOT = |file:///Users/paulklint/git/rascal/src/boot/|;
loc SHELLSCRIPT = |file:///Users/paulklint/install.sh|;

// Library modules that will be included in boot/stdlib

list[str] libraryModules =
[
    //"APIGen",
    "Ambiguity",
    "Boolean",
    "DateTime",
    "Exception",
    "Grammar",
    "IO",
    "List",
    "ListRelation",
    "Map",
    "Message",
    "Node",
    //"Origins",
    "ParseTree",
    "Prelude",
    "Relation",
    "Set",
    "String",
    "ToString",
    //"Traversal",
    "Tuple",
    "Type",
    "ValueIO",

    "util::Benchmark",
    "util::Brackets",
    "util::Cursor",
    "util::Eval",
    "util::FileSystem",
    "util::Highlight",
//    "util::LOC", data type name Output conflicts with Rascal.rsc
    "util::Math",
    "util::Maybe",
    "util::Monitor",
    "util::PriorityQueue",
    "util::REPL",
    "util::Reflective",
    "util::ShellExec",
    "util::SystemAPI",
    "util::UUID",
    "util::Webserver"    
];

// Fancy reporting

void report(str msg){
    println("**** <msg> ****");
}

// Where happiness begins

void build1(loc IDEST, loc CDEST){
     ISTDLIB = IDEST + "stdlib";
     CSTDLIB = CDEST + "stdlib";
     i_pcfg = pathConfig(srcPath=[|std:///|], binDir=ISTDLIB, libPath=[ISTDLIB]);
     c_pcfg = pathConfig(srcPath=[|std:///|], binDir=CSTDLIB, libPath=[CSTDLIB]);
     
     report("build1: compiling standard library modules");
     for(moduleName <- libraryModules + ["lang::rascal::grammar::ParserGenerator","lang::rascal::boot::Kernel"] ){
         compile(moduleName, i_pcfg, recompile=true, verbose=true);
         rascalc("--srcPath <c_pcfg.srcPath> --libPath <c_pcfg.libPath> --binDir <c_pcfg.binDir> <moduleName>");
     }
}

void build2(){
    
    C1BIN = "/Users/paulklint/c1bin/";
    C2BIN = "/Users/paulklint/c2bin/";
    
    C1STDLIB = C1BIN + "stdlib";
    C2STDLIB = C2BIN + "stdlib";
    
    pgen_kernel = ["lang::rascal::grammar::ParserGenerator","lang::rascal::boot::Kernel"];
    
    rascalc("--noDefaults -noLinking --srcPath |std:///| --libPath <C1STDLIB> --binDir <C1STDLIB> <for(moduleName <- libraryModules){> <moduleName><}>");
    
    rascalc("--noDefaults --srcPath |std:///| --libPath <C1STDLIB> --binDir <C1STDLIB> <for(moduleName <- pgen_kernel){> <moduleName><}>");
     
    rascalc("--kernel <C1STDLIB>/lang/rascal/boot/Kernel.rvm.ser.gz --noDefaults --noLinking --srcPath |std:///| --libPath <C2STDLIB> --binDir <C2STDLIB><for(moduleName <- libraryModules){> <moduleName><}>");
    
    rascalc("--kernel <C1STDLIB>/lang/rascal/boot/Kernel.rvm.ser.gz --noDefaults --srcPath |std:///| --libPath <C2STDLIB> --binDir <C2STDLIB><for(moduleName <- pgen_kernel){> <moduleName><}>");
}

value build(){
    return build(|file:///Users/paulklint/ibin/|, |file:///Users/paulklint/c1bin/|);
}
