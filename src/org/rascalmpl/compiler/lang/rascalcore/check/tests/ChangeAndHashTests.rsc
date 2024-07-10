
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

TModel check(str moduleName, RascalCompilerConfig compilerConfig){
    
        ModuleStatus result = rascalTModelForNames([moduleName], 
                                                  compilerConfig,
                                                  dummy_compile1);
       <found, tm, result> = getTModelForModule(moduleName, result);
       if(found && !isEmpty(tm.messages)){
            iprintln(tm.messages);
       }
       return tm;
}

bool expectEqual(str oldM, str newM, set[str] restrict = {}) {
    <old, new> = checkBoth(oldM, newM, restrict);
    println("old: <old>");
    println("new: <new>");
    return old == new;
}
bool expectNotEqual(str oldM, str newM, set[str] restrict = {}){
    <old, new> = checkBoth(oldM, newM, restrict);
    return old != new;
}
bool expectSubset(str oldM, str newM, set[str] restrict = {}){
    <old, new> = checkBoth(oldM, newM, restrict);
    return old < new;
}
bool expectSuperset(str oldM, str newM, set[str] restrict = {}){
    <old, new> = checkBoth(oldM, newM, restrict);
    return old > new;
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

tuple[set[loc] old, set[loc] new] checkBoth(str oldM, str newM, set[str] restrict){
    remove(|memory://stableHashProject/|, recursive=true);
    
    pcfg = pathConfig(
        srcs=[|memory://stableHashProject/src|],
        bin=|memory://stableHashProject/bin|,
        generatedSources=|memory://stableHashProject/generated|,
        resources=|memory://stableHashProject/resources|,
        libs=[]
    );
    
     // First create old version of M
    writeFile(|memory://stableHashProject/src/M.rsc|, "module M\n <oldM>");
    
    ccfg = rascalCompilerConfig(pcfg)[verbose=false][logPathConfig=false];

    tmOld = check("M", ccfg);
    if(!isEmpty(tmOld.messages)) println("old: <tmOld.messages>");
    
    // Remove source and generated tpls
    remove(|memory://stableHashProject/|, recursive=true);
     // Next create new version of M
    writeFile(|memory://stableHashProject/src/M.rsc|, "module M\n<newM>");
    
    tmNew = check("M", ccfg);
    if(!isEmpty(tmNew.messages))  println("new: <tmNew.messages>");
    return <reduce(domain(tmOld.logical2physical), restrict),
            reduce(domain(tmNew.logical2physical), restrict)
           >;
}

test bool moduleContentChanged() =
    expectSubset("", "int f(int n) = n + 1;");
    
// TODO: variable declaration

// TODO: anno declaration
    
test bool funDefaultChanged() =
    expectNotEqual("int f(int n) = n + 1;",  "default int f(int n) = n + 1;");

test bool funJavaChanged() =
    expectNotEqual("list[&T] delete(list[&T] _lst, int _n);",
                "@javaClass{org.rascalmpl.library.Prelude}
                'java list[&T] delete(list[&T] _lst, int _n);");

test bool funResultChanged() =
    expectNotEqual("int f(int n) = n + 1;",  "value f(int n) = n + 1;");

 test bool funParamChanged() =
   expectNotEqual("num f(int n) = n + 1;",  "num f(real n) = n + 1;");

test bool funParamLayoutChanged() =
   expectEqual("int f(int n, int m) = n + m;",  "int f(  int  n , int m ) = n + m;");

test bool funKwParamChanged() =
   expectNotEqual("int f(int n, int delta = 1) = n + delta;",  "int f(int n, int delta1) = n + delta1;");

test bool funBodyChanged1() =
   expectEqual("int f(int n) = n + 1;",  "int f(int n) = n + 2;");

test bool funBodyChanged2() =
   expectEqual("int f(int n) { return n + 1; }",  "int f(int n) { return n + 2; }");

test bool funWhenChanged() =
   expectNotEqual("int f(int n) = n + 1 when n == 0;",  "int f(int n) = n + 2 when n == 1;");

test bool funWithFail() =
    expectNotEqual("int f(int n) { x = n + 1; fail; }",  "int f(int n) { x = n + 2; fail; }");

test bool funsExchanged() =
    expectEqual("int f(int n) = n + 1; int g(int n) = n + 1;",  
                 "int g(int n) = n + 1; int f(int n) = n + 1;");

test bool funsCommentAdded() =
    expectEqual("int f(int n) = n + 1; int g(int n) = n + 1;",  
                 "int f(int n) = n + 1; /* new comment */ int g(int n) = n + 1;");

test bool funAdded() =
    expectSubset("int f(int n) = n + 1;",  
                 "int f(int n) = n + 1; int g(int n) = n + 2;");

test bool funDeleted() =
    expectSuperset("int f(int n) = n + 1; int g(int n) = n + 2;",  
                   "int f(int n) = n + 1;");

test bool commonKwFieldAdded() =
    expectEqual("data D;", "data D(int x = 0);");

test bool commenKwFieldDeleted() =
    expectEqual("data D(int x = 0);", "data D;");

test bool consAdded() =
     expectSubset("data D;", "data D = d(int n);");

test bool consDeleted() =
    expectSuperset("data D = d(int n);", "data D;");

test bool consFieldChanged() =
    expectNotEqual("data D = d(int n);", "data D = d(int m);");

test bool consFieldLayoutChanged() =
    expectEqual("data D = d(int n);", "data D = d(  int   n  );");

// Keyword fields n and m generate separate locs, therefore we filter on constructors
test bool consKwFieldChanged() =
    expectEqual("data D = d(int n = 0);", "data D = d(int m = 0);", restrict = {"constructor"});
 
test bool consKwFieldLayoutChanged() =
    expectEqual("data D = d(int n = 0);", "data D = d(int   n    = 0);", restrict = {"constructor"});
   
// TODO: syntax declaration

// TODO: alias declaration