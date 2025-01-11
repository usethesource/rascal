module lang::rascal::tests::library::analysis::diff::edits::HiFiTreeDiffTests

extend analysis::diff::edits::ExecuteTextEdits;
extend analysis::diff::edits::HiFiTreeDiff;
extend lang::pico::\syntax::Main;

import IO;
import ParseTree;
import String;

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
    orig        = parse(grammar, example);
    transformed = transform(orig);
    edits       = treeDiff(orig, transformed);
    edited      = executeTextEdits(example, edits);

    try {
        return transformed := parse(grammar, edited);
    }
    catch ParseError(loc l): {
        println("<transform> caused a parse error <l> in:");
        println(edited);
        return false;
    }
}

@synopsis{Extract the leading spaces of each line of code}
list[str] indentationLevels(str example)
    = [ i | /^<i:[\ ]*>[^\ ]*/ <- split("\n", example)];

@synopsis{In many cases, but not always, treeDiff maintains the indentation levels}
@description{
Typically when a rewrite does not change the lines of code count, 
and when the structure of the statements remains comparable, treeDiff
can guarantee that the indentation of a file remains unchanged, even if
significant changes to the code have been made.
}
@pitfalls{
* This specification is not true for any transformation. Only apply it to 
a test case if you can expect indentation-preservation for _the entire file_.
}
bool editsMaintainIndentationLevels(type[&T<:Tree] grammar, str example, (&T<:Tree)(&T<:Tree) transform) {
    orig        = parse(grammar, example);
    transformed = transform(orig);
    edits       = treeDiff(orig, transformed);
    edited      = executeTextEdits(example, edits);
    
    return indentationLevels(example) == indentationLevels(edited);
}

(&X<:Tree) identity(&X<:Tree x) = x;

start[Program] swapAB(start[Program] p) = visit(p) {
    case (Id) `a` => (Id) `b`
    case (Id) `b` => (Id) `a`
};

start[Program] naturalToString(start[Program] p) = visit(p) {
    case (Type) `natural` => (Type) `string`
};

start[Program] addDeclarationToEnd(start[Program] p) = visit(p) {
    case (Program) `begin declare <{IdType ","}* decls>; <{Statement  ";"}* body> end`
        => (Program) `begin
                     '  declare
                     '    <{IdType ","}* decls>,
                     '    c : natural;
                     '  <{Statement  ";"}* body>
                     'end`
};

start[Program] addDeclarationToStart(start[Program] p) = visit(p) {
    case (Program) `begin declare <{IdType ","}* decls>; <{Statement  ";"}* body> end`
        => (Program) `begin
                     '  declare
                     '    c : natural,
                     '    <{IdType ","}* decls>;
                     '  <{Statement  ";"}* body>
                     'end`
};

test bool nulTestWithId() 
    = editsAreSyntacticallyCorrect(#start[Program], simpleExample, identity);

test bool simpleSwapper() 
    = editsAreSyntacticallyCorrect(#start[Program], simpleExample, swapAB)
    && editsMaintainIndentationLevels(#start[Program], simpleExample, swapAB);

test bool addDeclarationToEndTest() 
    = editsAreSyntacticallyCorrect(#start[Program], simpleExample, addDeclarationToEnd);

test bool addDeclarationToStartTest() 
    = editsAreSyntacticallyCorrect(#start[Program], simpleExample, addDeclarationToStart);

test bool addDeclarationToStartAndEndTest() 
    = editsAreSyntacticallyCorrect(#start[Program], simpleExample, addDeclarationToStart o addDeclarationToEnd);

test bool addDeclarationToEndAndSwapABTest() 
    = editsAreSyntacticallyCorrect(#start[Program], simpleExample, addDeclarationToEnd o swapAB);

test bool addDeclarationToStartAndSwapABTest() 
    = editsAreSyntacticallyCorrect(#start[Program], simpleExample, addDeclarationToStart o swapAB);

test bool addDeclarationToStartAndEndAndSwapABTest() 
    = editsAreSyntacticallyCorrect(#start[Program], simpleExample, addDeclarationToStart o addDeclarationToEnd o swapAB);

test bool naturalToStringTest() 
    = editsAreSyntacticallyCorrect(#start[Program], simpleExample, naturalToString);
