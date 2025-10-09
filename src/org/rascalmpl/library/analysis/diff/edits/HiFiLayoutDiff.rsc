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
* if comments are not marked with `@category("Comment")` in the original grammar, then this algorithm can not recover them.
}
@benefits{
* Recovers source code comments which have been lost during earlier steps in the formatting pipeline. This makes losing source code comments an independent concern of a declarative formatter.
* Recovers the original capitalization of case-insensitive literals which may have been lost during earlier steps in the formatting pipeline.
* Can standardize the layout of case insensitive literals to ALLCAPS, all lowercase, or capitalized. Or can leave the literal as it was formatted by an earlier stage.
* Is agnostic towards the design of earlier steps in the formatting pipeline, so lang as `formattedTree := originalTree`. This means that
the pipeline may change layout (whitespace and comments and capitalization of case-insensitive literals), but nothing else.
}
module analysis::diff::edits::HiFiLayoutDiff

extend analysis::diff::edits::HiFiTreeDiff;
import ParseTree; // this should not be necessary because imported by HiFiTreeDiff
import String; // this should not be be necessary because imported by HiFiTreeDiff

@synopsis{Normalization choices for case-insensitive literals.}
data CaseInsensitivity
    = toLower()
    | toUpper()
    | toCapitalized()
    | asIs()
    | asFormatted()
    ;

@synopsis{Extract TextEdits for the differences in whitespace between two otherwise identical ((ParseTree))s.}
@description{
See ((HiFiLayoutDiff)).
}
list[TextEdit] layoutDiff(Tree original, Tree formatted, bool recoverComments = true, CaseInsensitivity ci = asIs()) {
    assert original := formatted : "nothing except layout and keyword fields may be different for layoutDiff to work correctly. <treeDiff(original, formatted)>";

    @synopsis{rec is the recursive workhorse, doing a pairwise recursion over the original and the formatted tree}
    @description{
        We recursively skip over every "equal" pairs of nodes, until we detect two different _layout_ nodes. The original location
        of that node and the new contents of the formatted node is used to construct a replace ((TextEdit)), and 
        optionally the original layout is inspected for source code comments which may have been lost. Literals are skipped 
        explicitly to avoid arbitrary edits for case insensitive literals, and to safe some time.
    }

    // if layout differences are detected, here we produce a `replace` node:
    list[TextEdit] rec(
        t:appl(prod(Symbol tS, _, _), list[Tree] tArgs), // layout is not necessarily parsed with the same rules (i.e. comments are lost!)
        u:appl(prod(Symbol uS, _, _), list[Tree] uArgs))
        = [replace(t@\loc, recoverComments ? learnComments(t, u) : "<u>") | tArgs != uArgs, "<t>" != "<u>" /* avoid useless edits */] 
        when 
            delabel(tS) is layouts, 
            delabel(uS) is layouts,
            tArgs != uArgs; 

    // matched literal trees generate empty diffs
    list[TextEdit] rec(
        appl(prod(lit(_), _, _), list[Tree] _),
        appl(prod(lit(_), _, _), list[Tree] _))
        = [];

    // matched case-insensitive literal trees generate empty diffs such that the original is maintained.
    // however, we also offer some convenience functionality to standardize their formatting right here.
    list[TextEdit] rec(
        t:appl(prod(cilit(_), _, _), list[Tree] _),
        u:appl(prod(cilit(_), _, _), list[Tree] _)) {

        str yield = "<t>";

        switch (ci) {
            case asIs():
                return [];
            case asFormatted():
                return [replace(t@\loc, result) | str result := "<u>", result != yield];
            case toUpper():
                return [replace(t@\loc, result) | str result := toUpperCase(yield), result != yield]; 
            case toLower():
                return [replace(t@\loc, result) | str result := toLowerCase(yield), result != yield]; 
            case toCapitalized():
                return [replace(t@\loc, result) | str result := capitalize(yield), result != yield];
            default:
                throw "unexpected option: <ci>";
        }
    }

    list[TextEdit] rec(
        char(_),
        char(_)
    ) = [];

    list[TextEdit] rec(
        cycle(Symbol _, int _),
        cycle(_, _)
    ) = [];

    // recurse through the entire parse tree to collect layout edits:
    default list[TextEdit] rec(
        Tree t:appl(Production p, list[Tree] argsA),
        appl(p /* must be the same by the above assert */, list[Tree] argsB)) 
        = [*rec(argsA[i], argsB[i]) | i <- [0..size(argsA)]]; 

    // first add required locations to layout nodes
    // TODO: check if indeed repositioning is never needed
    // original = reposition(original, markLit=true, markLayout=true, markSubLayout=true);

    return rec(original, formatted);
}

@synopsis{Make sure the new layout still contains all the source code comments of the original layout}
@description{
This algorithm uses the `@category(/[cC]omments/)` tag to detect source code comments inside layout substrings. If the original
layout contains comments, we re-introduce the comments at the expected level of indentation. New comments present in the 
replacement are kept and will overwrite any original comments. 

This trick is complicated by the syntax of multiline comments and single line comments that have
to end with a newline.
}
@benefits{
* if comments are kepts and formatted by tools like Tree2Box, then this algorithm does not overwrite these.
* if comments were completely lost, then this algorithm _always_ puts them back (under assumptions of ((layoutDiff)))
* recovered comments are indented according to the indentation discovered in the _formatted_ replacement tree.
}
@pitfalls{
* if comments are not marked with `@category("Comment")` in the original grammar, then this algorithm recovers nothing.
}
private str learnComments(Tree original, Tree replacement) {
    originalComments = ["<c>" | /c:appl(prod(_,_,{\tag("category"(/^[Cc]omment$/)), *_}), _) := original];

    if (originalComments == []) {
        // if the original did not contain comments, stick with the replacements
        return "<replacement>";
    }
    
    replacementComments = ["<c>" | /c:appl(prod(_,_,{\tag("category"(/^[Cc]omment$/)), *_}), _) := replacement];

    if (replacementComments != []) {
        // if the replacement contains comments, we assume they've been accurately retained by a previous stage (like Tree2Box):
        return "<replacement>";
    }

    // At this point, we know that: 
    //   (a) comments are not present in the replacement and 
    //   (b) they used to be there in the original.
    // So the old comments are going to be copied to the new output.
    // But, we want to indent them using the style of the replacement.

    // The last line of the replacement string always has the indentation for the construct that follows:
    //   |    // a comment 
    //   |    if (true) {
    //    ^^^^
    //      newIndent
    
    str replString = "<replacement>";
    str newIndent  = split("\n", replString)[-1] ? "";

    // we always place sequential comments vertically, because we don't know if we are dealing
    // we a single line comment that has to end with newline by follow restriction or by a literal "\n".
    // TODO: a deeper analysis of the comment rule that's in use could also be used to discover this.
    str trimmedOriginals = "<for (c <- originalComments) {><c>
                           '<}>"[..-1]; // drop the final newline
    
    // we wrap the comment with the formatted whitespace to assure the proper indentation
    // of its first line, and the proper indentation of what comes after this layout node
    return replString 
        + indent(newIndent, trimmedOriginals, indentFirstLine=false) 
        + replString;
}

private Symbol delabel(label(_, Symbol t)) = t;
private default Symbol delabel(Symbol x) = x; 