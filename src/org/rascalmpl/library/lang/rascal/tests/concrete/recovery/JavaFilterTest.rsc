module lang::rascal::tests::concrete::recovery::JavaFilterTest

syntax A = A B "$";

syntax A = "a";

syntax B = ("1" "b") | ("2" "b");

import ParseTree;

import IO;
import vis::Text;
import util::ParseErrorRecovery;



&T<:Tree disamb(&T<:Tree t) {
    return disambiguateParseErrors(t, allowAmbiguity=false);
}

test bool testFilter() {
    A a = parse(#A, "a?b$", allowRecovery=true, filters={disamb});
    println(prettyTree(a));
    return true;
}

test bool javaFilterTest() {
    A a = parse(#A, "a?b$", allowRecovery=true, filters={disambParseErrorsNoAmbiguity});
    println(prettyTree(a));
    return true;
}

test bool curryTestRegularParam() {
    int add(int a, int b) {
        return a + b;
    }
    int (int x) add41 = curry(add, 41);

    return add41(1) == 42;
}

test bool curryTestKeywordParam() {
    int add(int a, int b=10) {
        return a + b;
    }
    int (int x) add41 = curry(add, keywordParams=("b":41));

    return add41(1) == 42;
}

test bool javaCurriedFilterTest() {
    A a = parse(#A, "a?b$", allowRecovery=true, 
        filters={curry(disambiguateParseErrors, keywordParams=("allowAmbiguity":false))});
    println(prettyTree(a));
    return true;
}

