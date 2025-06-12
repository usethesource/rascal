module lang::rascal::tests::concrete::recovery::PrefixSharingTest

syntax Stat = Expr ";";

syntax Expr = N "+" N | N "-" N;

syntax N = [0-9];

import ParseTree;
import util::ParseErrorRecovery;
import lang::rascal::tests::concrete::recovery::RecoveryCheckSupport;
import vis::Text;
import IO;

Tree parseStat(str input, bool visualize=false)
    = parser(#Stat, allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=<"<visualize>">|);

test bool exprOk() = checkRecovery(#Stat, "1+2;", []);

test bool exprUnknownTerminator() = checkRecovery(#Stat, "1+2:\n", [":\n"], visualize=false);

test bool exprUnknownOperator() = checkRecovery(#Stat, "1*2;", ["*2"], visualize=false);

// Used to manually inspect the resulting error tree
void exprPrefixSharing() {
    Tree t = parseStat("1*2;", visualize=false);
    println(prettyTree(t));
}


