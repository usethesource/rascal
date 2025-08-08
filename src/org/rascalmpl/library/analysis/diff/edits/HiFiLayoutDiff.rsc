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
See ((HiFiLayoutDiff)).
}
list[TextEdit] layoutDiff(Tree original, Tree formatted, bool copyComments = false) {
    assert original := formatted : "nothing except layout and keyword fields may be different for layoutDiff to work correctly.";

    @synopsis{rec is the recursive workhorse, doing a pairwise recursion over the original and the formatted tree}
    @description{
        We recursively skip over every "equal" pairs of nodes, until we detect two different _layout_ nodes. The original location
        of that node is used to construct a replace ((TextEdit)), and optionally the original layout is inspected for
        source code comments which may have been lost. Literals are skipped explicitly to avoid arbitrary edits for 
        case insensitive literals, and to safe some time.
    }

    // if layout differences are detected, here we produce a `replace` node:
    list[TextEdit] rec(
        t:appl(prod(Symbol tS, _, _), list[Tree] tArgs), // layout is not necessarily parsed with the same rules (i.e. comments are lost!)
        u:appl(prod(Symbol uS, _, _), list[Tree] uArgs))
        = [replace(t@\loc, copyComments ? learnComments(t@\loc, "<u>", "<t>") : "<u>") | tArgs != uArgs] 
        when 
            delabel(tS) is layouts, 
            delabel(uS) is layouts,
            tArgs != uArgs; 

    // matched literal trees generate empty diffs
    list[TextEdit] rec(
        appl(prod(lit(_), _, _), list[Tree] _),
        appl(prod(lit(_), _, _), list[Tree] _))
        = [];

    // matched case-insensitive literal trees generate empty diffs such that the original is maintained
    list[TextEdit] rec(
        appl(prod(cilit(_), _, _), list[Tree] _),
        appl(prod(cilit(_), _, _), list[Tree] _))
        = [];

    // recurse through the entire parse tree to collect layout edits:
    default list[TextEdit] rec(
        Tree t:appl(Production p, list[Tree] argsA),
        appl(p /* must be the same by the above assert */, list[Tree] argsB)) 
        = [*rec(a, b) | <a, b> <- zip2(argsA, argsB)]; 

    // first add required locations to layout nodes
    original = reposition(original, markLayout=true, markSubLayout=false);

    return rec(original, formatted);
}

    
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

private Symbol delabel(label(_, Symbol t)) = t;
private default Symbol delabel(Symbol x) = x; 