module lang::rascal::tests::concrete::recovery::bugs::ATermOutOfMemoryBug

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import String;
import vis::Text;
import util::ErrorRecovery;
import Set;

@memo
void treeDiff(appl(prod1, args1), appl(prod2, args2)) {
    if (prod1 != prod2) {
        println("<prod1>");
        println("<prod2>");
        throw "prods do not match";
    }

    if (size(args1) != size(args2)) {
        throw "argument size mismatch";
    }

    for (int i <- [0..size(args1)]) {
        arg1 = args1[i];
        arg2 = args2[i];
        treeDiff(arg1, arg2);
    }
}

@memo
void treeDiff(cycle(sym1, length1), cycle(sym2, length2)) {
    if (sym1 != sym2) {
        throw "cycle symbols do not match";
    }

    if (length1 != length2) {
        throw "cycle lengths do not match";
    }
}

@memo
void treeDiff(char(c1), char(c2)) {
    if (c1 != c2) {
        throw "character mismatch";
    }
}

@memo
void treeDiff(amb(alts1), amb(alts2)) {
    if (size(alts1) != size(alts2)) {
        throw "alts size mismatch";
    }

    while (!isEmpty(alts1)) {
        <alt1, alts1> = takeFirstFrom(alts1);
        <alt2, alts2> = takeFirstFrom(alts2);

        treeDiff(alt1, alt2);
    }
}

Tree testBug() {
    standardParser = parser(#start[Module], allowRecovery=false, allowAmbiguity=true);
    recoveryParser = parser(#start[Module], allowRecovery=true, allowAmbiguity=true);
    loc source = |std:///lang/aterm/syntax/ATerm.rsc|;
    loc sourceCache = |std:///lang/aterm/syntax/ATerm.rsc?enable-cache=true|;
    input = readFile(source);
    //modifiedInput = substring(input, 0, 369) + substring(input, 399);
    modifiedInput = substring(input, 0, 369) + substring(input, 399);
    println("without caching");
    Tree t1 = recoveryParser(modifiedInput, source);
    println("with caching");
    Tree t2 = recoveryParser(modifiedInput, source);
    if (t1 == t2) {
        println("equal");
    } else {
        println("not equal");
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
    return t1;
    //testDeleteUntilEol(standardParser, recoveryParser, source, input, 200, 150, 369, 369);
}
