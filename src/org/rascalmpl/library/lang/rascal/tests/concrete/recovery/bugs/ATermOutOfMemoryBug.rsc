module lang::rascal::tests::concrete::recovery::bugs::ATermOutOfMemoryBug

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import String;
import vis::Text;
import util::ErrorRecovery;
import util::Benchmark;

void testBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    loc source = |std:///lang/aterm/syntax/ATerm.rsc|;
    loc sourceNoMemo = |std:///lang/aterm/syntax/ATerm.rsc?disable-memoization=true|;

    // "enable-cache" Does nothing for now
    loc sourceCache = |std:///lang/aterm/syntax/ATerm.rsc?enable-cache=true|;
    input = readFile(source);
    modifiedInput = substring(input, 0, 369) + substring(input, 399);

    println("without optimized cycles");    
    withoutCyclesStart = realTime();
    Tree t1 = recoveryParser(modifiedInput, source);
    withoutCyclesDuration = realTime() - withoutCyclesStart;
    println("without cycle optimization duration: <withoutCyclesDuration>");

/*
    println("without default memoization");
    withoutMemoStart = realTime();
    Tree tm = recoveryParser(modifiedInput, sourceNoMemo);
    withoutMemoDuration = realTime() - withoutMemoStart;
    println("without cycle optimization duration: <withoutMemoDuration>");
    */

    println("with caching");
    withCacheStart = realTime();
    Tree t2 = recoveryParser(modifiedInput, sourceCache);
    withCacheDuration = realTime() - withCacheStart;
    println("with cache duration: <withCacheDuration>");

    int equalityStart = realTime();
    if (treeEquality(t1, t2)) {
        println("equal");
        if (t1 != t2) {
            println("but not the same?");
        }
    } else {
        int equalityDuration = realTime() - equalityStart;
        println("not equal in <equalityDuration> ms");
        if ("<t1>" != "<t2>") {
            println("yields are not equal");
        } else {
            println("yields are equal");
        }

        if (false) {
            prettyT1 = prettyTree(t1);
            prettyT2 = prettyTree(t2);

            if (prettyT1 != prettyT2) {
                println("pretty trees are not equal");
            } else {
                println("pretty trees are equal");
            }
            writeFile(|cwd:///no-cache.txt|, "<prettyT1>");
            writeFile(|cwd:///with-cache.txt|, "<prettyT2>");
        }
    }

    //treeDiff(t1, t2);
    //println("trees are equal");
    //return t1;
    //testDeleteUntilEol(standardParser, recoveryParser, source, input, 200, 150, 369, 369);
}
