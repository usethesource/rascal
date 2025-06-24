/**
 * Copyright (c) 2025, NWO-I Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/
 @description{
    This module contains tests for error tree semantics in Rascal. As most of this functionality is not implemented yet (in the interprter),
    most tests currently fail.
 }
module lang::rascal::tests::concrete::recovery::ErrorTreeSemanticsTests


// We need to produce an error tree to test with
import lang::pico::\syntax::Main;

import ParseTree;
import util::ParseErrorRecovery;
import IO;
import vis::Text;
import Set;
import Exception;

// Ambiguous syntax to check amb memoization
syntax Amb = AmbWord () | () AmbWord;
syntax AmbWord = "^" [a-z] "$" () | "^" () [a-z] "$";

@synopsis{Check if a tree is an error tree.}
private bool isParseError(appl(error(_, _, _), _)) = true;
private default bool isParseError(Tree tree) = false;

@synopsis{Check if a tree is an amb cluster}
private bool isAmbCluster(amb(_)) = true;
private default bool isAmbCluster(Tree tree) = false;

@synopsis{Get first amb child}
private set[Tree] getAmbAlternatives(amb(alts)) = alts;

@synopsis{Check equality modulo location information}
private bool equals(appl(prod, args1), appl(prod, args2)) = allEqual(args1, args2);
private bool equals(amb(alts1), amb(alts2)) = size(alts1) == size(alts2) && allEqual(alts1, alts2);
private bool equals(cycle(Symbol sym, int length), cycle(sym, lenght)) = true;
private bool equals(char(int c), char(c)) = true;
private default bool equals(Tree tree1, Tree tree2) = false;

private bool allEqual(list[Tree] args1, list[Tree] args2) {
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

private bool allEqual(set[Tree] args1, set[Tree] args2) {
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

private str sortName(Tree tree) = printSymbol(tree.prod.def, true);

private str getLabel(Tree tree) = tree.prod.def.name;

private Program parsePico(str input) = parse(#Program, input, allowRecovery=true, allowAmbiguity=true);

private Program getTestProgram() = parsePico(
 "begin declare;
  while input do
    input x= 14;
    output := 0
  od
end");

private Statement getTestStatement() {
    Program prg = getTestProgram();
    for (/(Statement)stat := prg, isParseError(stat), "<stat>" == "input x= 14") {
        return stat;
    }

    fail;
}

private Statement getWhileStatement() {
    Program prg = getTestProgram();
    for (/(Statement)stat := prg, stat is loop, !isParseError(stat)) {
        return stat;
    }

    fail;
}

test bool verifyTestTree() {
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

@synopsis{Do some basic sanity checks on the test program}
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

@synopsis{Test that all error trees are visited}
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

test bool testHasBeforeDot() = getTestStatement() has var;

test bool testHasAfterDot() = !(getTestStatement() has val);

test bool testIsDefinedBeforeDot() = getTestStatement().var?;

test bool testIsDefinedAfterDot() = !getTestStatement().val?;

test bool testFieldAccessBeforeDot() = "<getTestStatement().var>" == "input";

bool testFieldAccessAfterDot() {
    try {
        getTestStatement().val;
        return false;
    } catch ParseErrorRecovery(NoSuchField("val"), _): {
        return true;
    }
}

test bool testFieldAssignmentBeforeDot() {
    Statement stat = getTestStatement();
    stat.var = (Id)`hello`;
    return "<stat>" == "hello x= 14";
}

test bool testFieldAssignmentAfterDot() {
    try {
        stat = getTestStatement();
        stat.val = (Expression)`hello`;
        return false;
    } catch ParseErrorRecovery(NoSuchField("val"), _): {
        return true;
    }
}

test bool testBracketFieldAssignmentBeforeDot() {
    stat = getTestStatement();
    return "<stat[var=(Id)`hello`]>" == "hello x= 14";
}

test bool testBracketFieldAssignmentAfterDot() {
    stat = getTestStatement();
    try {
        stat[val=(Expression)`hello`];
        return false;
    } catch ParseErrorRecovery(NoSuchField("val"), _): {
        return true;
    }
}

test bool testIndexedFieldBeforeDot() = equals(getTestStatement()[0], (Id)`input`);

test bool testIndexedFieldAfterDot() {
    try {
        getTestStatement()[1];
        return false;
    } catch ParseErrorRecovery(IndexOutOfBounds(1), l): {
        return l == |unknown:///|(36,11,<3,4>,<3,15>);
    }
}

test bool testIndexedFieldTrueOutOfBounds() {
    try {
        getTestStatement()[100];
        return false;
    } catch IndexOutOfBounds(100): {
        return true;
    }
}

test bool testIndexedFieldAssignmentBeforeDot() {
    // Note that this currently does also not work on regular trees (in the interpreter)!
    Statement stat = getTestStatement();
    stat[0] = (Id)`hello`;
    return "<stat>" == "hello x= 14";
}

test bool testIndexedFieldAssignmentAtOrAfterDot() {
    Statement stat = getTestStatement();
    try {
        stat[1] = (Id)`hello`;
        return false;
    } catch ParseErrorRecovery(IndexOutOfBounds(2), _): {
        return true;
    }
}

@description{Check that concrete syntax can be used to match holes with error subtrees.
Also check that error trees cannot be deconstructed using concrete syntax.}
test bool testConcreteMatchWithErrors() {
    Statement whileStat = getWhileStatement();

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

@description{
This function a test tree that has plenty of oppoertunities to memo amb children:
 ❖
 ├─ Amb = AmbWord  () 
 │  ├─ ❖
 │  │  ├─ !error dot=4: AmbWord = "^"  ()  [a-z]  "$"
 │  │  │  ├─ ()
 │  │  │  └─ skipped
 │  │  │     ├─ X
 │  │  │     └─ $
 │  │  ├─ !error dot=1: AmbWord = "^"  [a-z]  "$"  ()
 │  │  │  └─ skipped
 │  │  │     ├─ X
 │  │  │     └─ $
 │  │  └─ !error dot=2: AmbWord = "^"  [a-z]  "$"  ()
 │  │     └─ skipped
 │  │        ├─ X
 │  │        └─ $
 │  └─ ()
 └─ Amb = ()  AmbWord
    ├─ ()
    └─ ❖
       ├─ !error dot=4: AmbWord = "^"  ()  [a-z]  "$"
       │  ├─ ()
       │  └─ skipped
       │     ├─ X
       │     └─ $
       ├─ !error dot=1: AmbWord = "^"  [a-z]  "$"  ()
       │  └─ skipped
       │     ├─ X
       │     └─ $
       └─ !error dot=2: AmbWord = "^"  [a-z]  "$"  ()
          └─ skipped
             ├─ X
             └─ $
}
private Amb ambTestTree() = parse(#Amb, "^X$", allowRecovery=true, allowAmbiguity=true);

test bool testDeepMatchAmbMemo() {
    Amb ambTree = ambTestTree();

    // Count the number of errors that is actually found by a deep match
    int count = (0 | it + 1 | /appl(error(_,_,_),_) := ambTree);

    // There will only be 3 matches if deep matches are memoized, 6 if they are not.
    return count == 3;
}

test bool testVisitAmbMemo() {
    Amb ambTree = ambTestTree();

    int count = 0;
    visit(ambTree) {
        case appl(error(_,_,_),_): count = count + 1;
    }

    // There will only be 3 matches if deep matches are memoized, 6 if they are not.
    return count == 3;
}

test bool testVisitReplacementAmbMemo() {
    Amb ambTree = ambTestTree();

    // Return a different tree for each index
    AmbWord replacement(int index) {
        list[str] letters = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k" ];
        return parse(#AmbWord, "^" + letters[count] + "$", allowAmbiguity=true, maxAmbDepth=0);
    }

    int count = 0;
    visitedTree = visit(ambTree) {
        case appl(error(_,_,_),_) => {
            count = count + 1;
            replacement(count);
        }
    }

    /*
visitedTree without memoization:
 ❖
 ├─ Amb = AmbWord  ()
 │  ├─ ❖
 │  │  ├─ AmbWord = "^"  ()  [a-z]  "$"
 │  │  │  ├─ ()
 │  │  │  └─ b
 │  │  ├─ AmbWord = "^"  ()  [a-z]  "$"
 │  │  │  ├─ ()
 │  │  │  └─ c
 │  │  └─ AmbWord = "^"  ()  [a-z]  "$"
 │  │     ├─ ()
 │  │     └─ d
 │  └─ ()
 └─ Amb = ()  AmbWord
    ├─ ()
    └─ ❖
       ├─ AmbWord = "^"  ()  [a-z]  "$"
       │  ├─ ()
       │  └─ g
       ├─ AmbWord = "^"  ()  [a-z]  "$"
       │  ├─ ()
       │  └─ e
       └─ AmbWord = "^"  ()  [a-z]  "$"
          ├─ ()
          └─ f

Expected with memoization:
 ├─ Amb = AmbWord  ()
 │  ├─ ❖
 │  │  ├─ AmbWord = "^"  ()  [a-z]  "$"
 │  │  │  ├─ ()
 │  │  │  └─ b
 │  │  ├─ AmbWord = "^"  ()  [a-z]  "$"
 │  │  │  ├─ ()
 │  │  │  └─ c
 │  │  └─ AmbWord = "^"  ()  [a-z]  "$"
 │  │     ├─ ()
 │  │     └─ d
 │  └─ ()
 └─ Amb = ()  AmbWord
    ├─ ()
    └─ ❖
       ├─ AmbWord = "^"  ()  [a-z]  "$"
       │  ├─ ()
       │  └─ b
       ├─ AmbWord = "^"  ()  [a-z]  "$"
       │  ├─ ()
       │  └─ c
       └─ AmbWord = "^"  ()  [a-z]  "$"
          ├─ ()
          └─ d
    */

    // There will only be 3 matches if deep matches are memoized, 6 if they are not.
    return count == 3;
}
