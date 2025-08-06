@synopsis{Compare equal-modulo-layout parse trees and extract the exact whitespace text edits that will format the original file.}
@description{
This algorithm is the final component of a declarative high fidelity source code formatting pipeline.

We have the following assumptions:
1. One original text file exists.
2. One ((ParseTree)) of the original file to be formatted, containing all orginal layout and source code comments and case-insensitive literals in the exact order of the original text file. In other words,
nothing may have happened to the parse tree after parsing.
3. One ((ParseTree)) of the _same_ file, but formatted (using a formatting algorithm like ((Tree2Box)) `|` ((Box2Text)), or string templates, and then re-parsing). This is typically obtained by
translating the tree to a `str` using some formatting tools, and then reparsing the file.
4. Typically comments and specific capitalization of case-insensitive literals have been lost in step 3.
5. We use ((analysis::diff::edits::TextEdits)) to communicate the effect of formatting to the IDE context.
}
@pitfalls{
* if `originalTree !:= formattedTree` the algorithm will produce junk. It will break the syntactical correctness of the source code and forget source code comments.
}
@benefits{
* Recovers source code comments which have been lost during earlier steps in the formatting pipeline. This makes losing source code comments an independent concern of a declarative formatter.
* Recovers the original capitalization of case-insensitive literals which may have been lost during earlier steps in the formatting pipeline.
* Is agnostic towards the design of earlier steps in the formatting pipeline, so lang as `formattedTree := originalTree`. This means that
the pipeline may change layout (whitespace and comments and capitalization of case-insensitive literals), but nothing else.
}
module analysis::diff::edits::HiFiLayoutDiff

extend analysis::diff::edits::HiFiTreeDiff;

@synopsis{Extract TextEdits for the differences in whitespace between two otherwise identical ((ParseTree))s.}
// Equal trees
list[TextEdit] layoutDiff(Tree a, Tree b, bool copyComments = false)
    = [] when a == b;

// layout difference
// TODO: layout nodes typically do not have @\loc annotations, so we have to get them from somewhere
list[TextEdit] layoutDiff(
    t:appl(prod(layouts(str l), _, _), list[Tree] _),
    r:appl(prod(layouts(l), _, _), list[Tree] _),
    bool copyComments = false)
    = [replace(t@\loc, learnComments(t@\loc, "<r>", "<t>"))];

// matched literal trees generate empty diffs
list[TextEdit] layoutDiff(
    appl(prod(lit(str l), _, _), list[Tree] _),
    appl(prod(lit(l)    , _, _), list[Tree] _),
    bool copyComments = false)
    = [];

// matched case-insensitive literal trees generate empty diffs such that the original is maintained
list[TextEdit] layoutDiff(
    appl(prod(cilit(str l), _, _), list[Tree] _),
    appl(prod(cilit(l)    , _, _), list[Tree] _),
    bool copyComments = false)
    = [];

// recurse through the parse tree in the right order to collect layout edits
// this default fails when the two compared trees are unequal-modulo-layout, such that
// this precondition is checked and failure to comply is detected as early (high) as possible.
default list[TextEdit] layoutDiff(
    Tree t:appl(Production p, list[Tree] argsA),
    t:appl(p, list[Tree] argsB), // note the non-linear equality-modulo-layout check here 
    bool copyComments = false)
    = [*layoutDiff(a, b, copyComments=copyComments) | <a, b> <- zip2(argsA, argsB)];
    