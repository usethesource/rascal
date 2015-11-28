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

tuple[set[loc] differ, set[loc] mising] compare(loc iloc){
    ival = readBinaryValueFile(#value, iloc);
    cloc = iloc.path = "c" + iloc.path[2..];
    
    if(exists(cloc)){
        cval = readBinaryValueFile(#value, cloc);
        res = diff(ival, cval);
        if(res == "no diff"){
           return <{}, {}>;
        } else  {
            println("<iloc>: <res>");
            return <{iloc}, {}>;
        }
    } else {
        println("<iloc>: <cloc> does not exist");
        return <{}, {iloc}>;
    }
}
void main(){
    loc ibin = |compressed+home:///ibin|;
    loc cbin = |compressed+home:///cbin|;
    
    serialized = {};
    differ = {};
    missing = {};
    for(loc iloc <- files(ibin), iloc.extension == "gz"){
        if(!contains(iloc.path, ".ser.")){
           <d, m> = compare(iloc);
           differ += d;
           missing += m;
        } else {
          serialized += iloc;
        }
    }
    
    println("\n+++++++++++++++++++++++++++++++\n");
    
    println("Number of foles: <size(files(ibin))>");
    
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

value build(loc IDEST, loc CDEST){
     ISTDLIB = IDEST + "stdlib";
     CSTDLIB = CDEST + "stdlib";
     i_pcfg = pathConfig(srcPath=[|std:///|], binDir=ISTDLIB, libPath=[ISTDLIB]);
     c_pcfg = pathConfig(srcPath=[|std:///|], binDir=CSTDLIB, libPath=[CSTDLIB]);
     
     report("Compiling standard library modules");
     for(moduleName <- libraryModules + ["lang::rascal::grammar::ParserGenerator","lang::rascal::boot::Kernel"] ){
         compile(moduleName, i_pcfg, recompile=true, verbose=true);
         rascalc("--srcPath <c_pcfg.srcPath> --libPath <c_pcfg.libPath> --binDir <c_pcfg.binDir> <moduleName>");
     }
     
    return true;
}

value build(){
    return build(|file:///Users/paulklint/ibin/|, |file:///Users/paulklint/cbin/|);
}
