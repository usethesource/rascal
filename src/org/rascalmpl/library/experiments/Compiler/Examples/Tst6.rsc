module experiments::Compiler::Examples::Tst6

import String; 

loc rebase(loc org, loc newBase, list[loc] srcs){
    for(src <- srcs){
        opath = org.path;
        if(startsWith(opath, src.path)){
           npath = opath[size(src.path) .. ];
           return org.offset? ? (newBase + npath)[offset=org.offset][length=org.length][begin=org.begin][end=org.end]
                                 : newBase + npath;
        }
    }
    throw "Cannot rebase <org>";
}

value main() = rebase(|home:///tst1.rsc|, |std:///|, [|home:///|]);