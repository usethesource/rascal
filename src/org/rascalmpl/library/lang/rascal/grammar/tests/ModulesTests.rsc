module lang::rascal::grammar::tests::ModulesTests

// Used during development/testing, can be removed later

import Grammar;

import experiments::Compiler::Compile;
import experiments::Compiler::Execute;
import util::Reflective;
import IO;

PathConfig pcfg = pathConfig(srcs=[|test-modules:///|, |std:///|], 
                             bin=|home:///bin|, 
                             libs=[|home:///bin|],
                             boot=|file:///Users/paulklint/git/rascal/bootstrap/phase2|);
str getGrammarCode =
    "    
    import lang::rascal::\\syntax::Rascal;
    import ParseTree;
    import lang::rascal::grammar::definition::Modules;
    import IO;
    import Grammar;
    import util::Reflective;    
    
    public Grammar getGrammar(str moduleName, PathConfig pcfg) {
      println(\"parsing the rascal definition of \<moduleName\>\");
      Module \\module = parseModuleWithSpaces(getModuleLocation(moduleName, pcfg)).top;
      return modules2grammar(moduleName, {\\module});
    }";

value compiledGetGrammar(str moduleName, bool debug=false, bool debugRVM=false, bool recompile=true, bool profile=false, bool jvm=true) {
    ggName = "TST_GetGrammar";
    TMP = makeTMP(ggName);
    msrc = "module <ggName> 
           '<getGrammarCode>
           'value main() = getGrammar(\"<moduleName>\", <pcfg>);";
    writeFile(TMP, msrc);
    compileAndLink(ggName, pcfg, jvm=jvm); 
    return execute(TMP, pcfg, trace=true, debug=debug, debugRVM=debugRVM, recompile=false, profile=profile, jvm=jvm);
}

str makeGrammar(str def){
    gramName = "TST_Grammar";
    TMP = makeTMP(gramName);
    msrc = "module <gramName> 
           '<def>
           '";
    writeFile(TMP, msrc);
    println(def);
    return gramName; 
}

loc makeTMP(str name){
    mloc = |test-modules:///<name>.rsc|;
    return mloc;
}

bool compare(str moduleName){
    println("Comparing <moduleName>");
    Grammar g1 = getGrammar(moduleName, pcfg);
    if(Grammar g2 := compiledGetGrammar(moduleName)){
        df = diff(g1, g2);
        println(df);
        if(df == "no diff") return true;
        iprintToFile(|home:///g1_INT.txt|, g1);
        iprintToFile(|home:///g2_COMP.txt|, g2);
        return false;
    } else {
        println("compiled getGrammar does not return a grammar");
        return false;
    }
}

value main(){
     gram = makeGrammar("

        'syntax Exp = 
           \"aaa\" Exp* \"zzz\"
           ; 
     ");
     
    return compare(gram);
}