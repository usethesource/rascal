
@synopsis{Test how md5hashes change for certain changes in a module}

module lang::rascalcore::check::tests::ChangeAndHashTests

import lang::rascalcore::check::Checker;
import lang::rascalcore::compile::Compile;
import util::Reflective;
import IO;
import lang::rascalcore::check::Import;
import String;
import Map;
import Set;
import lang::rascalcore::check::ATypeBase;

import lang::rascalcore::check::tests::StaticTestingUtils;

TModel check(str moduleName, RascalCompilerConfig compilerConfig){

        ModuleStatus ms = rascalTModelForNames([moduleName],
                                                  compilerConfig,
                                                  dummy_compile1);
       <found, tm, ms> = getTModelForModule(moduleName, ms);
       if(found && verbose && !isEmpty(tm.messages)){
            iprintln(tm.messages);
       }
       if(found && /error(_,_) := tm.messages){
            throw tm.messages;
       }
       return tm;
}

bool verbose = false;

bool expectEqual(str oldM, str newM, set[str] restrict = {}) {
    <old, new> = checkAndReduce(oldM, newM, restrict);
    if(verbose) {println("old: <old>"); println("new: <new>"); }
    return old == new;
}
bool expectNotEqual(str oldM, str newM, set[str] restrict = {}){
    <old, new> = checkAndReduce(oldM, newM, restrict);
    if(verbose) {println("old: <old>"); println("new: <new>"); }
    return old != new;
}
bool expectSubset(str oldM, str newM, set[str] restrict = {}){
    <old, new> = checkAndReduce(oldM, newM, restrict);
    if(verbose) {println("old: <old>"); println("new: <new>"); }
    return old < new;
}
bool expectSuperset(str oldM, str newM, set[str] restrict = {}){
    <old, new> = checkAndReduce(oldM, newM, restrict);
    if(verbose) {println("old: <old>"); println("new: <new>"); }
    return old > new;
}

AGrammar getGrammar(TModel tm){
    if(!tm.store[key_grammar]?){
        throw "`grammar` not found in store";
    } else if([*AGrammar gs] := tm.store[key_grammar]){
        if(verbose) iprintln(gs[0]);
        return gs[0];
    } else {
        throw "`grammar` has incorrect format in store";
    }
}

bool expectEqualGrammar(str oldM, str newM) {
    <old, new> = checkBoth(oldM, newM);
    if(verbose) {println("old: <old>"); println("new: <new>"); }
    return getGrammar(old) == getGrammar(new);
}

bool expectNotEqualGrammar(str oldM, str newM) {
    <old, new> = checkBoth(oldM, newM);
    if(verbose) {println("old: <old>"); println("new: <new>"); }
    return getGrammar(old) != getGrammar(new);
}

str getRole(loc l){
    i = findFirst(l.scheme, "+");
    if(i >= 0){
        //println("getRole: <l> =\> <l.scheme[i+1..]>");
        return l.scheme[i+1..];
    }
    throw "Cannot get role in <l>";
}

set[loc] reduce(set[loc] locs, set[str] restrict){
    return isEmpty(restrict) ? locs : {l | l <- locs, getRole(l) in restrict };
}

tuple[TModel old, TModel new] checkBoth(str oldM, str newM){
    remove(|memory://stableHashProject/|, recursive=true);

    pcfg = pathConfig(
        srcs=[|memory://stableHashProject/src|],
        bin=|memory://stableHashProject/bin|,
        generatedSources=|memory://stableHashProject/generated|,
        resources=|memory://stableHashProject/resources|,
        libs=[]
    );

     // First create old version of M
    writeFile(|memory://stableHashProject/src/M.rsc|, "module M\n<oldM>\n");

    ccfg = rascalCompilerConfig(pcfg)[verbose=false][logPathConfig=false];

    tmOld = check("M", ccfg);
    if(verbose && !isEmpty(tmOld.messages)) println("old: <tmOld.messages>");

    // Remove source and generated tpls
    remove(|memory://stableHashProject/|, recursive=true);
     // Next create new version of M
    writeFile(|memory://stableHashProject/src/M.rsc|, "module M\n<newM>");

    tmNew = check("M", ccfg);
    if(verbose && !isEmpty(tmNew.messages))  println("new: <tmNew.messages>");

    return <tmOld, tmNew>;
}

tuple[set[loc] old, set[loc] new] checkAndReduce(str oldM, str newM, set[str] restrict){
    <tmOld, tmNew> = checkBoth(oldM, newM);
    return <reduce(domain(tmOld.logical2physical), restrict),
            reduce(domain(tmNew.logical2physical), restrict)
           >;
}

// Modules

test bool moduleContentChanged() =
    expectSubset("", "int f(int n) = n + 1;");


// Variable declarations

test bool varDeclLayoutChanged()
    = expectEqual("int n = 1 + 2;", "int    n    = 1 +   2  ;");

test bool varDeclTagChanged()
    = expectNotEqual("int n = 0;", "public int n = 0;");

test bool varDeclChanged()
    = expectNotEqual("int n = 1 + 2;", "int m = 1 + 2;");

test bool varDeclSameStr()
    = expectEqual("str n = \"a\";", "str n = \"a\";");

// Annotations

test bool annoLayoutChanged()
    = expectEqual("anno int node@i;", "anno   int   node@i;");

test bool annoChanged()
    = expectNotEqual("anno int node@i;", "anno   real   node@i;");

// Functions

test bool funTagChanged()
    = expectEqual("int f(int n) = n + 1;", "@synopsis{A brilliant function} int f(int n) = n + 1;");

test bool funJavaTagChanged()
    = expectNotEqual("@javaClass{org.rascalmpl.library.Prelude123}
                    'java list[&T] delete(list[&T] _lst, int _n);",
                    "@javaClass{org.rascalmpl.library.Prelude456}
                    'java list[&T] delete(list[&T] _lst, int _n);");
test bool funVisibilityChanged()
    = expectNotEqual("int f(int n) = n + 1;",  "private int f(int n) = n + 1;");

test bool funDefaultChanged()
    = expectNotEqual("int f(int n) = n + 1;",  "default int f(int n) = n + 1;");

test bool funLayoutChanged()
    = expectEqual("int f(int n) = n + 1;",  "int f  (int n) = n + 1;");

test bool funResultChanged()
    = expectNotEqual("int f(int n) = n + 1;",  "value f(int n) = n + 1;");

 test bool funParamChanged()
    = expectNotEqual("num f(int n) = n + 1;",  "num f(real n) = n + 1;");

test bool funParamLayoutChanged()
    = expectEqual("int f(int n, int m) = n + m;",  "int f(  int  n , int m ) = n + m;");

test bool funKwParamChanged()
    = expectNotEqual("int f(int n, int delta = 1) = n + delta;",  "int f(int n, int delta1) = n + delta1;");

test bool funKwParamLayoutChanged()
    = expectEqual("int f(int n, int delta = 1) = n + delta;",  "int f(int n, int   delta   =   1) = n + delta;");

test bool funBodyChanged1()
    = expectEqual("int f(int n) = n + 1;",  "int f(int n) = n + 2;");

test bool funBodyChanged2()
    = expectEqual("int f(int n) { return n + 1; }",  "int f(int n) { return n + 2; }");

test bool funWhenChanged()
    = expectNotEqual("int f(int n) = n + 1 when n == 0;",  "int f(int n) = n + 2 when n == 1;");

test bool funWithFailChanged()
    = expectNotEqual("int f(int n) { x = n + 1; fail; }",  "int f(int n) { x = n + 2; fail; }");

test bool funFailRemoved()
    = expectNotEqual("int f(int n) { x = n + 1; fail; }",  "int f(int n) { x = n + 2; return x; }");

test bool funsSwitched()
    = expectEqual("int f(int n) = n + 1; int g(int n) = n + 1;",
                  "int g(int n) = n + 1; int f(int n) = n + 1;");

test bool funsCommentAdded()
    =  expectEqual("int f(int n) { return n + 1; } int g(int n) { return n + 1; }",
                   "int f(int n) { /*C1*/ return n + 1; } /*C2*/ int g(int n) { /*C3*/ return n + 1; }");

test bool funAdded()
    = expectSubset("int f(int n) = n + 1;",
                   "int f(int n) = n + 1; int g(int n) = n + 2;");

test bool funDeleted()
    = expectSuperset("int f(int n) = n + 1; int g(int n) = n + 2;",
                    "int f(int n) = n + 1;");

test bool nestedCloneOK() = checkOK("
   void foo(str _){
        int bar(){
            return 1;
        }
        bar();
   }

   void foo(int _){
        int bar(){
            return 1;
        }
        bar();
   }");

test bool clonesNotOK() = unexpectedDeclarationInModule("
    module TwoBars
        int bar(int _){
            return 1;
        }
        int bar(int _){
            return 2;
        }
    ");

test bool nearClonesDifferentParameterNameOK() = checkModuleOK("
    module TwoBars
        int bar(int a){
            return 1;
        }
        int bar(int b){
            return 2;
        }
    ");

// Data declarations

test bool commonKwFieldAdded()
    = expectEqual("data D;", "data D(int x = 0);");

test bool commenKwFieldDeleted()
    = expectEqual("data D(int x = 0);", "data D;");

test bool consAdded()
    = expectSubset("data D;", "data D = d(int n);");

test bool consDeleted()
    = expectSuperset("data D = d(int n);", "data D;");

test bool consSwitched()
    = expectEqual("data D = d(int n) | e(str s);", "data D = e(str s) | d(int n);");

test bool altLayoutChanged()
    = expectEqual("data D = d(int n) | e(str s);", "data D = d(int n)   |    e(str s);");

test bool consFieldChanged()
    = expectNotEqual("data D = d(int n);", "data D = d(int m);");

test bool consFieldsSwitched()
    = expectNotEqual("data D = d(int n, int m);", "data D = d(int m, int n);");

test bool consFieldLayoutChanged1()
    = expectEqual("data D = d(int n);", "data D = d(  int   n  );");

test bool consFieldLayoutChanged2()
    = expectEqual("data D = d(int n);", "data D = d  (int n);");


// Keyword fields n and m generate separate locs, therefore we filter on constructors
test bool consKwFieldChanged()
    = expectEqual("data D = d(int n = 0);", "data D = d(int m = 0);", restrict = {"constructor"});

test bool consKwFieldLayoutChanged()
    = expectEqual("data D = d(int n = 0);", "data D = d(int   n    = 0);");

test bool consKwFieldsSwitched()
    = expectEqual("data D = d(int n = 0, int m = 1);", "data D = d(int m = 1, int n = 0);");

// Alias declaration

test bool aliasLayoutChanged()
    = expectEqual("alias A = list[int];", "alias A  = list[int]  ;");

test bool aliasChanged()
    = expectNotEqual("alias A = list[int];", "alias B  = list[int]  ;");

test bool aliasParameterChanged()
    = expectNotEqual("alias A[&T] = list[&T];", "alias B[&U]  = list[&U]  ;");

// Syntax declaration

test bool synProdChanged()
    = expectNotEqualGrammar("syntax A=aaa:\"a\";", "syntax A=aaa:\"b\";");

test bool synLayoutChanged()
    = expectEqualGrammar("syntax A = \"a\"; syntax BA = \"b\" A;",
                  "syntax   A =     \"a\" ; syntax BA   = \"b\"    A; ");

test bool synParameterChanged()
    = expectNotEqualGrammar("syntax A[&T] = &T;", "syntax A[&U] = &U;");

test bool synParameterLayoutChanged()
     = expectEqualGrammar("syntax A[&T] = &T;", "syntax A[ &T ] = &T ;");

test bool synStartChanged()
    = expectNotEqualGrammar("syntax A = \"a\";", "start syntax A = \"a\";");

test bool synStartLayoutChanged()
    = expectEqualGrammar("start syntax A = \"a\";", "start   syntax   A   =   \"a\"  ;");

test bool synLabeledChanged()
    = expectNotEqualGrammar("syntax A = \"a\" the_a;", "syntax A = \"a\" the_other_a;");

test bool synLabeledLayoutChanged()
    = expectEqualGrammar("syntax A = \"a\" the_a;", "syntax A = \"a\"    the_a;");

test bool synIterStarChanged1()
    = expectNotEqualGrammar("syntax A = \"a\"*;", "syntax A = \"a\"+;");

test bool synIterStarChanged2()
    = expectNotEqualGrammar("syntax A = \"a\"*;", "syntax A = \"b\"*;");

test bool synIterStarLayoutChanged1()
    = expectEqualGrammar("syntax A = \"a\"*;",  "syntax A = \"a\" *;");

test bool synIterStarLayoutChanged2()
    = expectEqualGrammar("syntax A = \"a\"+;",  "syntax A = \"a\" +;");

test bool synIterStarSepChanged1()
    = expectNotEqualGrammar("syntax A = {\"a\" \"x\"}*;",  "syntax A = {\"a\" \"x\"}+;");

test bool synIterStarSepChanged2()
    = expectNotEqualGrammar("syntax A = {\"a\" \"x\"}*;",  "syntax A = {\"b\" \"x\"}*;");

test bool synIterStarSepChanged3()
    = expectNotEqualGrammar("syntax A = {\"a\" \"x\"}*;",  "syntax A = {\"a\" \"y\"}*;");

test bool synIterStarSepLayoutChanged()
    = expectEqualGrammar("syntax A = {\"a\" \"x\"}*;",  "syntax A = { \"a\"   \"x\"  }  *  ;");

test bool synIterPlusChanged1()
    = expectNotEqualGrammar("syntax A = {\"a\" \"x\"}+;",  "syntax A = {\"a\" \"x\"}*;");

test bool synIterPlusChanged2()
    = expectNotEqualGrammar("syntax A = {\"a\" \"x\"}+;",  "syntax A = {\"b\" \"x\"}+;");

test bool synIterPlusChanged3()
    = expectNotEqualGrammar("syntax A = {\"a\" \"x\"}+;",  "syntax A = {\"a\" \"y\"}+;");

test bool synIterPlusLayoutChanged()
    = expectEqualGrammar("syntax A = {\"a\" \"x\"}+;",  "syntax A = { \"a\"   \"x\"  }  +  ;");

test bool synIterPlusSepChanged1()
    = expectNotEqualGrammar("syntax A = {\"a\" \"x\"}+;",  "syntax A = {\"a\" \"x\"}*;");

test bool synIterPlusSepChanged2()
    = expectNotEqualGrammar("syntax A = {\"a\" \"x\"}+;",  "syntax A = {\"b\" \"x\"}+;");

test bool synIterPlusSepChanged3()
    = expectNotEqualGrammar("syntax A = {\"a\" \"x\"}+;",  "syntax A = {\"a\" \"y\"}+;");

test bool synIterPlusSepLayoutChanged()
    = expectEqualGrammar("syntax A = {\"a\" \"x\"}+;",  "syntax A = { \"a\"   \"x\"  }  + ;");

test bool synOptionalChanged()
    = expectNotEqualGrammar("syntax A = \"a\"?;", "syntax A = \"b\"?;");

test bool synOptionalLayoutChanged()
    = expectEqualGrammar("syntax A = \"a\"?;", "syntax A = \"a\"   ?;");

test bool synAltAdded()
    = expectNotEqualGrammar("syntax A = \"a\";",
                   "syntax A = \"a\" | \"aa\";");

test bool synAltsSwitched()
    = expectEqualGrammar("syntax A = \"a\" | \"b\";", "syntax A = \"b\" | \"a\";");

test bool synAltsChanged()
    = expectNotEqualGrammar("syntax A = \"a\" | \"b\";", "syntax A = \"a\" | \"c\";");

test bool synAltsLayoutChanged()
    = expectEqualGrammar("syntax A = \"a\" | \"b\";", "syntax A =   \"a\"   |   \"b\"  ;");

test bool synSeqChanged()
    = expectNotEqualGrammar("syntax A = (\"a\" \"b\");", "syntax A = (\"a\" \"c\");");

test bool synSeqLayoutChanged()
    = expectEqualGrammar("syntax A = (\"a\" \"b\");", "syntax A = (  \"a\"   \"b\"  );");

test bool synEmptyLayoutChanged()
    = expectEqualGrammar("syntax A = ();", "syntax A = (   );");

test bool synColumnChanged1()
    = expectNotEqualGrammar("syntax A = \"a\"@10;", "syntax A = \"b\"@10;");

test bool synColumnChanged2()
    = expectNotEqualGrammar("syntax A = \"a\"@10;", "syntax A = \"a\"@20;");

test bool synColumnLayoutChanged()
    = expectEqualGrammar("syntax A = \"a\"@10;", "syntax A = \"a\"  @  10  ;");

test bool synEndOfLineChanged()
    = expectNotEqualGrammar("syntax A = \"a\" $;", "syntax A = \"b\" $;");

test bool synEndOfLineLayoutChanged()
    = expectEqualGrammar("syntax A = \"a\" $;", "syntax A =   \"a\"   $  ;");

test bool synStartOfLineChanged()
    = expectNotEqualGrammar("syntax A = ^ \"a\";", "syntax A = ^ \"b\";");

test bool synStartOfLineLayoutChanged()
    = expectEqualGrammar("syntax A = ^\"a\";", "syntax A =  ^   \"a\"  ;");


test bool synExceptChanged1()
    = expectNotEqualGrammar("syntax A = aaa:\"a\"; syntax B = bbb:\"b\"; syntax C= \"c\" ! aaa;",
                     "syntax A = aaa:\"a\"; syntax B = bbb:\"b\"; syntax C= \"d\" ! aaa;");

test bool synExceptChanged2()
    = expectNotEqualGrammar("syntax A = aaa:\"a\"; syntax B = bbb:\"b\"; syntax C= \"c\" ! aaa;",
                     "syntax A = aaa:\"a\"; syntax B = bbb:\"b\"; syntax C= \"c\" ! bbb;");

test bool synExceptLayoutChanged()
    = expectEqualGrammar("syntax A = aaa:\"a\"; syntax B = bbb:\"b\"; syntax C= \"c\" ! aaa;",
                         "syntax A = aaa:\"a\"; syntax B = bbb:\"b\"; syntax C= \"c\"   !   aaa ;");

test bool synCharClassChanged1()
    = expectNotEqualGrammar("lexical A = [a-z];", "lexical A = [a-y];");

test bool synCharClassChanged2()
    = expectNotEqualGrammar("lexical A = [abc];", "lexical A = [abcd];");

test bool synCharClassLayoutChanged1()
    = expectEqualGrammar("lexical A = [a-z];", "lexical A = [ a - z ];");

test bool synCharClassLayoutChanged2()
    = expectEqualGrammar("lexical A = [abc];", "lexical A = [ a b c ];");

test bool synFollowChanged1()
    = expectNotEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = A \>\> \"a\";",
                     "syntax A = \"a\"; syntax B = \"b\"; syntax C = B \>\> \"b\";");
test bool synFollowChanged2()
    = expectNotEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = A \>\> \"b\";",
                     "syntax A = \"a\"; syntax B = \"b\"; syntax C = A \>\> \"a\";");

test bool synFollowCLayouthanged()
    = expectEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = A \>\> \"b\";",
                  "syntax A = \"a\"; syntax B = \"b\"; syntax C = A   \>\>   \"b\";");

test bool synNotFollowChanged1()
    = expectNotEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = A !\>\> \"c\";",
                     "syntax A = \"a\"; syntax B = \"b\"; syntax C = B !\>\> \"c\";");

test bool synNotFollowChanged2()
    = expectNotEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = A !\>\> \"c\";",
                     "syntax A = \"a\"; syntax B = \"b\"; syntax C = A !\>\> \"d\";");

test bool synNotFollowCLayouthanged()
    = expectEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = A !\>\> \"c\";",
                  "syntax A = \"a\"; syntax B = \"b\"; syntax C = A   !\>\>   \"c\";");


test bool synPrecedeChanged1()
    = expectNotEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\" \<\< B;",
                     "syntax A = \"a\"; syntax B = \"b\"; syntax C = \"d\" \<\< B;");

test bool synPrecedeChanged2()
    = expectNotEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\" \<\< B;",
                     "syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\" \<\< A;");

test bool synPrecedeLayoutChanged()
    = expectEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\" \<\< B;",
                  "syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\"   \<\<  B;");


test bool synNotPrecedeChanged1()
    = expectNotEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\" !\<\< B;",
                     "syntax A = \"a\"; syntax B = \"b\"; syntax C = \"d\" !\<\< B;");
test bool synNotPrecedeChanged2()
    = expectNotEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\" !\<\< B;",
                     "syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\" !\<\< A;");

test bool synNotPrecedeLayoutChanged()
    = expectEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\" !\<\< B;",
                  "syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\"  !\<\<  B;");

test bool synUnequalChanged1()
    = expectNotEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\"; syntax D = A \\ \"b\";",
                    "syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\"; syntax D = A \\ \"c\";");

test bool synUnequalChanged2()
    = expectNotEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\"; syntax D = A \\ \"b\";",
                    "syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\"; syntax D = C \\ \"b\";");

test bool synUnequalLayoutChanged()
    = expectEqualGrammar("syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\"; syntax D = A \\ \"b\";",
                  "syntax A = \"a\"; syntax B = \"b\"; syntax C = \"c\"; syntax D =   A   \\   \"b\" ;");