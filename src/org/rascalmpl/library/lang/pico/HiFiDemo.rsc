@synopsis{Demonstrates HiFi source-to-source transformations through concrete syntax rewrites and text edits.}
module lang::pico::HiFiDemo

import lang::pico::\syntax::Main;
import IO;
import ParseTree;
import analysis::diff::edits::HiFiTreeDiff;
import analysis::diff::edits::ExecuteTextEdits;

@synopsis{Blindly swaps the branches of all the conditionals in a program}
@description{
This rule is syntactically correct and has a clear semantics. The 
layout of the resulting if-then-else-fi statement is also clear.
}
start[Program] flipConditionals(start[Program] program) = visit(program) {
    case (Statement) `if <Expression e> then 
                     '  <{Statement ";"}* ifBranch>
                     'else 
                     '  <{Statement ";"}* elseBranch>
                     'fi` =>
         (Statement) `if <Expression e> then 
                     '  <{Statement ";"}* elseBranch>
                     'else 
                     '  <{Statement ";"}* ifBranch>
                     'fi` 
};

void main() {
    t = parse(#start[Program], |project://rascal/src/org/rascalmpl/library/lang/pico/examples/flip.pico|);
    println("The original: 
            '<t>");

    u = flipConditionals(t);
    println("Branches swapped, comments and indentation lost:
            '<u>");

    edits = treeDiff(t, u);
    println("Smaller text edits:");
    iprintln(edits);

    newContent = executeTextEdits("<t>", edits);
    println("Better output after executeTextEdits:
            '<newContent>");

    newU = parse(#start[Program], newContent);

    assert u := newU : "the rewritten tree matches the newly parsed";
}
