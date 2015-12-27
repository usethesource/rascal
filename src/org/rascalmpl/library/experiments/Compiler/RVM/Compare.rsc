module experiments::Compiler::RVM::Compare

import IO;
import ValueIO;
import String;
import Set;
import util::Reflective;
import util::FileSystem;
import experiments::Compiler::RVM::AST;

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
       println("<iloc>,<cloc>:\n<res>");
       return <{iloc}, {}>;
    }
}

void compareAll(str cbin, str extension = "gz"){
    str bootStdLib = "Users/paulklint/git/rascal/src/boot/stdlib";
    
    loc bootDir = (extension == "gz" ? |compressed+file:///| : |file:///|) + bootStdLib;
    loc cbinDir = (extension == "gz" ?  |compressed+home:///| : |home:///|) + cbin;
    str cbinDirPath = cbinDir.path;
    serialized = {};
    differ = {};
    missing = {};
    for(loc cloc <- files(cbinDir), cloc.extension == extension){
        if(!contains(cloc.path, ".ser.")){
           filePath = cloc.path[findFirst(cloc.path, cbinDir.path) + size(cbinDirPath) .. ];
           <d, m> = compare(bootDir + filePath, cbinDir + filePath);
           differ += d;
           missing += m;
        } else {
          serialized += cloc;
        }
    }
    
    println("\n+++++++++++++++++++++++++++++++\n");
    
    println("Number of files: <size(files(cbinDir))>");
    
    println("Different: <size(differ)>");
    for(d <- differ) println("\t<d>");
    
    println("\nSkipped serialized: <size(serialized)>");
    for(s <- serialized) println("\t<s>");
    
     println("\nMissing: <size(missing)>");
    for(m <- missing) println("\t<m>");
}

void allMessages(str cbin){
    str bootStdLib = "Users/paulklint/git/rascal/src/boot/stdlib";
   
    loc cbinDir =  |compressed+home:///| + cbin;
    str cbinDirPath = cbinDir.path;
    
    for(loc cloc <- files(cbinDir), cloc.extension == "gz"){
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