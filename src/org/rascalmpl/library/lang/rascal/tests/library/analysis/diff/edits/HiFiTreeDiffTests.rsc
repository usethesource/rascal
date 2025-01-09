module lang::rascal::tests::library::analysis::diff::edits::HiFiTreeDiffTests

extend analysis::diff::edits::ExecuteTextEdits;
extend analysis::diff::edits::HiFiTreeDiff;
extend lang::pico::\syntax::Main;

import ParseTree;
import IO;

public str simpleExample
    = "begin
      '  declare
      '    a : natural,
      '    b : natural;
      '  a := a + b;
      '  b := a - b;
      '  a := a - b
      'end    
      '";

@synopsis{Specification of what it means for `treeDiff` to be syntactically correct}
@description{
TreeDiff is syntactically correct if:
* The tree after rewriting _matches_ the tree after applying the edits tot the source text and parsing that.
* Note that _matching_ ignores case-insensitive literals and layout, indentation and comments
}
bool editsAreSyntacticallyCorrect(type[&T<:Tree] grammar, str example, (&T<:Tree)(&T<:Tree) transform) {
    println("Transforming: 
            '<example>");
    orig = parse(grammar, example);
    transformed = transform(orig);
    println("Transformed: 
            '<transformed>");
    edits = treeDiff(orig, transformed);
    println("Edits: 
            '<edits>");
    edited = executeTextEdits(example, edits);
    println("Edited:
            '<edited>");

    // the edited text should produce a tree that matches the rewritten tree
    return transformed := parse(grammar, edited);
}

(&X<:Tree) identity(&X<:Tree x) = x;

start[Program] swapAB(start[Program] p) = visit(p) {
    case (Id) `a` => (Id) `b`
    case (Id) `b` => (Id) `a`
};

test bool nulTestWithId() 
    = editsAreSyntacticallyCorrect(#start[Program], simpleExample, identity);

test bool simpleSwapper() 
    = editsAreSyntacticallyCorrect(#start[Program], simpleExample, swapAB);
