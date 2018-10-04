module lang::rascalcore::compile::muRascal2RVM::Relocate

import String;
import ParseTree;
import IO;

import lang::rascalcore::compile::muRascal::AST;

loc relocLoc(loc org, loc reloc, list[loc] srcs){
    for(src <- srcs){
        opath = org.path;
        if(startsWith(opath, src.path)){
           npath = opath[size(src.path) .. ];
           return org.offset? ? (reloc + npath)[offset=org.offset][length=org.length][begin=org.begin][end=org.end]
                                 : reloc + npath;
        }
    }
    //if(org != reloc){
    //    println("Not relocated: <org>");
    //}
    return org;
}

MuModule relocMuModule(MuModule m, loc reloc, list[loc] srcs){
    //if("<m.name>" != "ConsoleInput"){
    //    println("relocMuModule:<m.name>, <reloc>");
    //}
    if(reloc.scheme == "noreloc"){
        return m;
    }
    m.src = relocLoc(m.src, reloc, srcs);
    m.functions = for(f <- m.functions){
                      f.src = relocLoc(f.src, reloc, srcs);
                      f.body = relocBody(f.body, reloc, srcs);
                      append f;
                  }
   return m;                                 
}

MuExp relocBody(MuExp body, loc reloc, list[loc] srcs){
 return
        visit(body){ 
        case muOCall3(MuExp fun, list[MuExp] largs, loc src)    => muOCall3(fun, largs, relocLoc(src, reloc, srcs))
        case muOCall4(MuExp fun, AType types, list[MuExp] largs, loc src)
                                                                => muOCall4(fun, types, largs, relocLoc(src, reloc, srcs))
        case muCallPrim2(str name, loc src)                     => muCallPrim2(name, relocLoc(src, reloc, srcs))                
        case muCallPrim3(str name, list[MuExp] exps, loc src)   => muCallPrim3(name, exps, relocLoc(src, reloc, srcs))
        case muThrow(MuExp exp, loc src)                        => muThrow(exp, relocLoc(src, reloc, srcs))
        };    
}