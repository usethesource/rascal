module box::Tst
import ParseTree;
import IO;
import box::Box;


import languages::tst::syntax::Tst;

public text toList(loc asf){
     PROGRAM a = parse(#PROGRAM, asf);
     println(a);
     println(`->`:=a);
     println(`x`:=a);
     return ["finished"];
     }
