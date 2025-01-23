module lang::rascal::tests::concrete::recovery::CycleTest

import ParseTree;
import vis::Text;
import IO;
import util::ErrorRecovery;
import Node;

syntax S = T | U;

syntax T = X T? | "$";

syntax U = X T? | "$";

syntax X = "b"? | "c";

void testCycles() {
    str input = "bc$";

    Tree noMemoTree = parse(#S, input, |unknown:///?parse-memoization=none&visualize-parse-result|, allowAmbiguity=true);
    Tree safeNodeMemoTree = parse(#S, input, |unknown:///?parse-memoization=safe-node|, allowAmbiguity=true);

    println("Tree without memoization:");
    println(prettyTree(noMemoTree));

    print("Safe node memoization: ");
    if (treeEquality(noMemoTree, safeNodeMemoTree)) {
        println("correct");
    } else {
        println("INCORRECT");
    }


    println("Node counts, no memo: <countTreeNodes(noMemoTree)>, with memo: <countTreeNodes(safeNodeMemoTree)>");
    parseTree2Dot(noMemoTree, |tmp:///no-memo.dot|);
    parseTree2Dot(safeNodeMemoTree, |tmp:///with-memo.dot|);

/*
    if ({appl1Level1, *_ } := getChildren(t1)[0] && {appl2Level1, *_ } := getChildren(t2)[0]) {
        println("appl1Level1:\n<prettyTree(appl1Level1)>");
        println("appl2Level1:\n<prettyTree(appl2Level1)>");

        if ([amb({appl1Level2,*_})] := getChildren(appl1Level1)[1] && [amb({appl2Level2,*_})] := getChildren(appl2Level1)[1]) {
            //println("Child 1:");
            //iprintln(appl1Level2);
            //println("Child 2:");
            //iprintln(appl2Level2);

            println("child 1 tree:\n<prettyTree(appl1Level2)>");
            println("child 2 tree:\n<prettyTree(appl2Level2)>");

            println("yield1: <appl1Level2>");
            println("yield2: <appl2Level2>");
         }
   }
   */
}
