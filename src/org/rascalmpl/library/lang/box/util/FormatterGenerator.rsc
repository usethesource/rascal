@synopsis{Tools for generating and debugging Box-based formatting pipelines.}
@benefits{
* speed: these generators cache a generated parser for fast reuse between calls to formatting pipeline.
* safety: if the generated formatters produce illegal or ambiguous syntax, a warning is printed and no changes are produced.
* accuracy: the generated formatters retain all comments as long as comments are annotated with `@category("Comment")` in
* practicality: the generated formatters can produce ((TextEdit))s which are consumed by IDEs such as VScode.
}
module lang::box::util::FormatterGenerator

import ParseTree;
import analysis::diff::edits::ExecuteTextEdits;
import analysis::diff::edits::HiFiLayoutDiff;
import analysis::diff::edits::TextEdits;
import lang::box::util::Box2Text;
import lang::box::util::Tree2Box;
import util::Highlight;
import util::IDEServices;
import util::Monitor;
import util::PathConfig;
import util::Reflective;
import IO;
import Exception;

@synopsis{A style specification maps a ((Tree)) to a ((data:Box)) expression.}
alias Style = Box(Tree);

@synopsis{Repeats this option from ((util::LanguageServer)) to avoid a cyclic dependency.}
data FileSystemChange(bool needsConfirmation = false);

@synopsis{Generates a file formatter that immediately applies the new style to the input.}
void(loc) fileFormatter(type[&G <: Tree] grammar, Style style = toBox, bool needsConfirmation=false) {
    list[TextEdit](loc) toEdits = fileEdits(grammar, style=style);

    return void (loc input) {  
        executeFileSystemChanges([changed(input, toEdits(input), needsConfirmation=needsConfirmation)]);
    };
}

@synopsis{Generates a string formatter}
str(str) stringFormatter(type[&G <: Tree] grammar, Style style = toBox) {
    list[TextEdit](str) toEdits = stringEdits(grammar, style=style);

    return str (str input) {
        return executeTextEdits(input, toEdits(input));
    };
} 

@synopsis{Generates a file formatter that produces ((TextEdit))s for downstream processing.}
list[TextEdit](loc) fileEdits(type[&G <: Tree] grammar, Style style = toBox, bool needsConfirmation=false) {
    &G(loc, loc) p = parser(grammar);

    return list[TextEdit] (loc input) {
        &G tree = p(input, input);
        try {
            return layoutDiff(tree, p(format(style(tree)), input), needsConfirmation=needsConfirmation);
        }
        catch e:ParseError(loc place): { 
            writeFile(|tmp:///<input.file>|, format(style(tree)));
            warning("Ignoring formatter output, which contained a new parse error.", |tmp:///<input.file>|(place.offset, place.length));
            return [];
        }
        catch e:Ambiguity(loc place, str _, str _): { 
            writeFile(|tmp:///<input.file>|, format(style(tree)));
            warning("Ignoring formatter output, which contained a new ambiguity.", |tmp:///<input.file>|(place.offset, place.length));
            return [];
        }  
    };
}
    
@synopsis{Generates a string formatter that produces ((TextEdit))s for downstream processing.}
list[TextEdit](str) stringEdits(type[&G <: Tree] grammar, Style style = toBox) {
    &G(str, loc) p = parser(grammar);
    
    return list[TextEdit] (str input) {
        &G tree = p(input, |tmp:///unknown.txt|);
        try {
            return layoutDiff(tree, p(grammar, format(toBox(tree)), |tmp:///unknown.txt|));
        }
        catch e:ParseError(loc place): { 
            writeFile(|tmp:///unknown.txt|, format(toBox(tree)));
            warning("Ignoring formatter output, which contained a new parse error.", |tmp:///unknown.txt|(place.offset, place.length));
            return [];
        }
        catch e:Ambiguity(loc place, str _, str _): { 
            writeFile(|tmp:///<input.file>|, format(style(tree)));
            warning("Ignoring formatter output, which contained a new ambiguity.", |tmp:///unknown.txt|(place.offset, place.length));
            return [];
        }   
    };
}
    
