module lang::rascalcore::compile::ValidateAnswer::ValidateAnswer

import IO;
import  lang::rascalcore::compile::Compile;
import  lang::rascalcore::compile::Execute;
import util::Reflective;


PathConfig pcfg = 
    pathConfig(srcs=[|test-modules:///|, |std:///|], 
               bin=|home:///bin|, 
               libs=[|home:///bin|, |home:///git/rascal/bin/boot|]);
               
private str substituteHoles(str smod, map[str,str] holes){
    n = 0;
    substituted = 
        visit(smod){
        case /^_/: {
            holeN = "hole<n>";
            if(holes[holeN]?){
             n += 1;
             insert holes[holeN];
            } else {
              fail;
            }
        } 
    };
    return substituted;
} 

//private map[str,str] validate1(str qid, str listing, map[str,str] holes){
//    listing1 = substituteHoles(listing, holes);
//    for(f <- pcfg.bin.ls){
//        if(/Question/ := "<f>"){
//           remove(f);
//        }
//    }
//    mloc = |test-modules:///| + "Question.rsc";
//    writeFile(mloc, listing1);
//    try {
//       compileAndLink("Question", pcfg); 
//       res = execute(mloc, pcfg, testsuite=true);
//       if(!printTestReport(res, [])){
//          return ("exercise" : qid, "validation" : "true", "feedback" : "");
//       } else {
//        return ("exercise" : qid, "validation" : "true", "feedback" : "");
//       }
//    } catch e: {
//       return ("exercise" : qid, "validation" : "false", "feedback" : "<e>");
//    }
//}
//
//str validate(str qid, str listing, map[str,str] holes) =
//    XMLResponses(validate1(qid, listing, holes));

private str escapeForHtml(str txt){
  return
    visit(txt){
      case /^\</ => "&lt;"
      case /^\>/ => "&gt;"
      case /^"/ => "&quot;"
      case /^&/ => "&amp;"
    }
}

private str XMLResponses(map[str,str] values){
    R = "\<responses\><for(field <- values){>\<response id=\"<field>\"\><escapeForHtml(values[field])>\</response\><}>\</responses\>";
    println("R = <R>");
    return R;
}