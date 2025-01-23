module lang::rascal::tests::concrete::recovery::bugs::MemoNodeSharing

import ParseTree;
import vis::Text;
import IO;
import util::ErrorRecovery;
import String;
import lang::rascal::\syntax::Rascal;

void testNodeSharing() {
    loc source = |std:///lang/rascal/tests/extends2/CHECKER.rsc|;
    str input = readFile(source);
    str modifiedInput = substring(input, 0, 126) + substring(input, 127);

    Tree memoTree = parse(#start[Module], modifiedInput, |unknown:///?visualize-parse-result|, allowRecovery=true, allowAmbiguity=true);

    println("Node count: <countUniqueTreeNodes(memoTree)>");
    parseTree2Dot(memoTree, |tmp:///memo.dot|);
    parseTree2Dot(maximallyShareTree(memoTree), |tmp:///max-shared.dot|);
}
