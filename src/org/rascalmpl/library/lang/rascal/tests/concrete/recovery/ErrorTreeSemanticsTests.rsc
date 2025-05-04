module lang::rascal::tests::concrete::recovery::ErrorTreeSemanticsTests

// We need to produce an error tree to test with
import lang::pico::\syntax::Main;

import ParseTree;
import util::ParseErrorRecovery;
import IO;
import vis::Text;
import Set;
import Exception;

// Will be defined in ParseTree module:
data RuntimeException = ParseErrorRecovery(RuntimeException trigger, loc src);

@synopsis{Check if a tree is an error tree.}
bool isParseError(appl(error(_, _, _), _)) = true;
default bool isParseError(Tree tree) = false;

bool isSkipped(appl(skipped(_),_)) = true;
default bool isSkkipped(Tree tree) = false;

@synopsis{Check if a tree is an amb cluster}
bool isAmbCluster(amb(_)) = true;
default bool isAmbCluster(Tree tree) = false;

@synopsis{Get first amb child}
set[Tree] getAmbAlternatives(amb(alts)) = alts;

@synopsis{Check equality modulo location information}
bool equals(appl(prod, args1), appl(prod, args2)) = allEqual(args1, args2);
bool equals(amb(alts1), amb(alts2)) = size(alts1) == size(alts2) && allEqual(alts1, alts2);
bool equals(cycle(Symbol sym, int length), cycle(sym, lenght)) = true;
bool equals(char(int c), char(c)) = true;
default bool equals(Tree tree1, Tree tree2) = false;

bool allEqual(list[Tree] args1, list[Tree] args2) {
    if (size(args1) != size(args2)) {
        return false;
    }

    for (i <- [0..size(args1)-1]) {
        if (!equals(args1[i], args2[i])) {
            return false;
        }
    }
    return true;
}

bool allEqual(set[Tree] args1, set[Tree] args2) {
    if (size(args1) != size(args2)) {
        return false;
    }

    for (Tree arg1 <- args1) {
        bool found = false;
        for (Tree arg2 <- args2, !found) {
            if (equals(arg1, arg2)) {
                found = true;
                args2 -= arg2;
                break;
            }
        }
        if (!found) {
            return false;
        }
    }

    return true;
}

str sortName(Tree tree) = printSymbol(tree.prod.def, true);

str getLabel(Tree tree) = tree.prod.def.name;

Program parsePico(str input) = parse(#Program, input, allowRecovery=true, allowAmbiguity=true);

Program getTestProgram() = parsePico(
 "begin declare;
  while input do
    input x= 14;
    output := 0
  od
end");

Statement getTestStatement() {
    Program prg = getTestProgram();
    for (/(Statement)stat := prg, isParseError(stat), "<stat>" == "input x= 14") {
        return stat;
    }

    fail;
}

Statement getWhileStatement() {
    Program prg = getTestProgram();
    for (/(Statement)stat := prg, stat is loop, !isParseError(stat)) {
        return stat;
    }

    fail;
}

bool verifyTestTree() {
    Program prg = getTestProgram();
    println("tree:\n<prettyTree(prg)>");
    println("all errors:");
    for (Tree error <- findAllParseErrors(prg)) {
        println("error <sortName(error)>: <getErrorText(error)>");
    }

    assert "<getTestStatement()>" == "input x= 14";
    assert "<getWhileStatement()>" == "while input do
    input x= 14;
    output := 0
  od";
    return true;
}

test bool testDeepMatch() {
    Program prg = getTestProgram();
    list[str] expected = ["assign", "assign", "loop"]; // Multiset of expected labels

    // Find the error statements
    for (/(Statement)stat := prg, isParseError(stat)) {
        str label = getLabel(stat);
        assert label in expected;
        expected -= getLabel(stat); // Remove the label from the expected list
    }

    assert size(expected) == 0; // All expected labels should be found

    return true;
}

test bool testVisit() {
    Program prg = getTestProgram();
    list[str] expected = ["assign", "assign", "loop"]; // Multiset of expected labels
    visit (prg) {
        case (Statement) stat: if (isParseError(stat)) {
            str label = getLabel(stat);
            assert label in expected;
            expected -= getLabel(stat); // Remove the label from the expected list
        }
    }

    assert size(expected) == 0; // All expected labels should be found

    return true;
}

test bool testIs() = !(getTestStatement() is assign);

bool testHasBeforeDot() = getTestStatement() has var;
bool testHasAfterDot() = !(getTestStatement() has val);

bool testIsDefinedBeforeDot() = getTestStatement().var?;
bool testIsDefineAfterDot() = !getTestStatement().val?;

bool testFieldAccessBeforeDot() = "<getTestStatement().var>" == "input";
bool testFieldAccessAfterDot() {
    try {
        getTestStatement().val;
        return false;
    } catch ParseErrorRecovery(NoSuchField("val"), _): {
        return true;
    }
}

bool testFieldAssignmentBeforeDot() {
    Statement stat = getTestStatement();
    stat.var = (Id)`hello`;
    return "<stat>" == "hello x= 14";
}

bool testFieldAssignmentAfterDot() {
    try {
        stat = getTestStatement();
        stat.val = (Expression)`hello`;
        return false;
    } catch ParseErrorRecovery(NoSuchField("val"), _): {
        return true;
    }
}

bool testBracketFieldAssignmentBeforeDot() {
    stat = getTestStatement();
    return "<stat[var=(Id)`hello`]>" == "hello x= 14";
}

bool testBracketFieldAssignmentAfterDot() {
    stat = getTestStatement();
    try {
        stat[val=(Expression)`hello`];
        return false;
    } catch ParseErrorRecovery(NoSuchField("val"), _): {
        return true;
    }
}

test bool testIndexedFieldBeforeDot() = equals(getTestStatement()[0], (Id)`input`);
bool testIndexedFieldAtOrAfterDot() {
    try {
        getTestStatement()[1];
        return false;
    } catch ParseErrorRecovery(IndexOutOfBounds(1), _): {
        return true;
    }
}

bool testIndexedFieldAssignmentBeforeDot() {
    // Note that this currently does also not work on regular trees (in the interpreter)!
    Statement stat = getTestStatement();
    stat[0] = (Id)`hello`;
    return "<stat>" == "hello x= 14";
}

bool testIndexedFieldAssignmentAtOrAfterDot() {
    Statement stat = getTestStatement();
    try {
        stat[1] = (Id)`hello`;
        return false;
    } catch ParseErrorRecovery(IndexOutOfBounds(2), _): {
        return true;
    }
}

bool testConcreteMatchWithErrors() {
    Statement whileStat = getWhileStatement();

    println("whileStat: <prettyTree(whileStat)>");
    // A tree with error children should match
    if ((Statement)`while <Expression _> do <Statement stat1>; <Statement _> od` := whileStat) {
        assert isAmbCluster(stat1);
        Tree tree = getFirstFrom(getAmbAlternatives(stat1));
        assert "<tree>" == "input x= 14";
        // An error tree should not match
        if ((Statement)`<Id _> := <Expression _>` := tree) {
            return false;
        }

        // Although a single hole should match
        assert (Statement)`<Statement _>` := tree;

        return true;
    }

    return false;
}
