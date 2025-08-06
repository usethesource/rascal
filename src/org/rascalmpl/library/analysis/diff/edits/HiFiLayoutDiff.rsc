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
import ParseTree; // this should not be necessary because imported by HiFiTreeDiff
import String; // this should not be be necessary because imported by HiFiTreeDiff


@synopsis{Extract TextEdits for the differences in whitespace between two otherwise identical ((ParseTree))s.}
@description{
This is the top-level wrapper that starts a recursion over the entire parse tree.
We need to keep the span of the current node in order to fill in the possible gaps 
where sub-trees are not annotated with source locations.
}
list[TextEdit] layoutDiff(Tree a, Tree b, bool copyComments = false)
    = layoutDiff(a@\loc, a, b, copyComments=copyComments);

// Equal trees
list[TextEdit] layoutDiff(loc _span, Tree a, Tree b, bool copyComments = false)
    = [] when a == b;

// layout differences are detected, so here we produce a `replace` node:
list[TextEdit] layoutDiff(loc span,
    t:appl(prod(layouts(str l), _, _), list[Tree] _),
    u:appl(prod(layouts(l), _, _), list[Tree] _),
    bool copyComments = false)
    = [replace(span, learnComments(t@\loc, "<u>", "<t>"))] when eq(t, u);

// the layout was the same as before
list[TextEdit] layoutDiff(loc span,
    t:appl(prod(layouts(str l), _, _), list[Tree] _),
    t,
    bool copyComments = false)
    = []; 

// matched literal trees generate empty diffs
list[TextEdit] layoutDiff(loc _span,
    appl(prod(lit(str l), _, _), list[Tree] _),
    appl(prod(lit(l)    , _, _), list[Tree] _),
    bool copyComments = false)
    = [];

// matched case-insensitive literal trees generate empty diffs such that the original is maintained
list[TextEdit] layoutDiff(loc _span,
    appl(prod(cilit(str l), _, _), list[Tree] _),
    appl(prod(cilit(l)    , _, _), list[Tree] _),
    bool copyComments = false)
    = [];

// recurse through the parse tree in the right order to collect layout edits
// this default fails when the two compared trees are unequal-modulo-layout, such that
// this precondition is checked and failure to comply is detected as early (high) as possible.
default list[TextEdit] layoutDiff(loc span,
    Tree t:appl(Production p, list[Tree] argsA),
    t:appl(p, list[Tree] argsB), // note the non-linear equality-modulo-layout check here 
    bool copyComments = false)
    = [*layoutDiff(|todo:///|, a, b, copyComments=copyComments) | <a, b> <- zip2(argsA, argsB)]; // TODO: here we have to recover the loc of the outermost layout node
    
@synopsis{Make sure the new layout still contains all the source code comments of the original layout}
@description{
This algorithm uses a heuristic to detect source code comments inside layout substrings. If the original
layout contains comments, but the replacement layout does not, we re-introduce the comments at the
expected level of indentation.
}
private str learnComments(loc span, str replacement, str original, bool copyComments = false) {
    if (!copyComments) {
        return replacement;
    }
    else {
        // TODO: 1. detect "non-whitespace" in `original`
        //       2. strip leading indentation from the non-whitespace if multiple lines are detected
        //       3. re-indent the multiple lines
        //       4. integrate the new comments with the new whitespace in a smart manner
        throw "not yet implemented";
    }
}