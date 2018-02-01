module lang::rascalcore::compile::Tests::Compare

import IO;
import ValueIO;
import String;
import Set;
import List;
import util::Reflective;
import util::FileSystem;
import lang::rascalcore::compile::RVM::AST;
import lang::rascal::types::CheckerConfig;
import Message;
import lang::rascal::types::AbstractName;


set[loc] serialized = {};

tuple[set[loc] differ, set[loc] missing] compare(loc iloc, loc cloc){

    if(!exists(iloc)){
       return <{}, {iloc}>;
    }
    if(!exists(cloc)){
       return <{}, {cloc}>;
    }
   
    ival = readBinaryValueFile(#value, iloc);
    cval = readBinaryValueFile(#value, cloc);
    
    if(Configuration iconf := ival && Configuration cconf := cval){
      iconf.pathConfiguration = pathConfig();
      iconf.dirtyModules = {};
      cconf.pathConfiguration = pathConfig();
      cconf.dirtyModules = {};
      ival = iconf;
      cval = cconf;
    }
    
    
    res = diff(ival, cval);
    if(res == "no diff"){
       return <{}, {}>;
    } else  {
       println("<iloc>,<cloc>:\n<res>");
       return <{iloc}, {}>;
    }
}

void compareAll(str other, str base = "Users/paulklint/git/rascal/src/boot/stdlib", str extension = "gz"){
    
    loc baseDir = (extension == "gz" ? |compressed+file:///| : |file:///|) + base;
    loc otherDir = (extension == "gz" ?  |compressed+home:///| : |home:///|) + other;
    str otherDirPath = otherDir.path;
    serialized = {};
    differ = {};
    missing = {};
    for(loc cloc <- files(otherDir), cloc.extension == extension){
        if(!contains(cloc.path, ".ser.")){
           filePath = cloc.path[findFirst(cloc.path, otherDir.path) + size(otherDirPath) .. ];
           <d, m> = compare(baseDir + filePath, otherDir + filePath);
           differ += d;
           missing += m;
        } else {
          serialized += cloc;
        }
    }
    
    println("\n+++++++++++++++++++++++++++++++\n");
    
    println("Number of files: <size(files(otherDir))>");
    
    println("Different: <size(differ)>");
    for(d <- differ) println("\t<d>");
    
    println("\nSkipped serialized: <size(serialized)>");
    for(s <- serialized) println("\t<s>");
    
     println("\nMissing: <size(missing)>");
    for(m <- missing) println("\t<m>");
}

void allMessages(str other){
    str base = "Users/paulklint/git/rascal/src/boot/stdlib";
   
    loc otherDir =  /*compressed+*/ |home:///| + other;
    str otherDirPath = otherDir.path;
    
    for(loc cloc <- files(otherDir), cloc.extension == "gz"){
        if(!contains(cloc.path, ".ser.")){
           rvmMod = readBinaryValueFile(#RVMModule, cloc);
           messages = rvmMod.messages;
           if(!isEmpty(messages)){
            println("<cloc>:");
            for(m <- messages){
                println("\t<m>");
            }
           }
        } else {
          serialized += cloc;
        }
    }
}