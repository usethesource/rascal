@synopsis{Tools for generating and debugging Box-based formatting pipelines.}
@benefits{
* speed: these generators cache a generated parser for fast reuse between calls to formatting pipeline.
* safety: if the generated formatters produce illegal or ambiguous syntax, a warning is printed and no changes are produced.
* accuracy: the generated formatters retain all comments as long as comments are annotated with `@category("Comment")` in
* practicality: the generated formatters can produce ((TextEdit))s which are consumed by IDEs such as VScode.
}
@pitfalls{
* the formatters capture the current state of the grammar and the style function, so if you
change their source code, you have to regenerate the formatters.
}
module lang::box::util::FormatterGenerator

import Content;
import Exception;
import IO;
import Location;
import ParseTree;
import Set;
import analysis::diff::edits::ExecuteTextEdits;
import analysis::diff::edits::HiFiLayoutDiff;
import analysis::diff::edits::TextEdits;
import lang::box::util::Box2Text;
import lang::box::util::Tree2Box;
import util::FileSystem;
import util::Highlight;
import util::IDEServices;
import util::Monitor;
import util::PathConfig;
import util::Reflective;

@synopsis{A style specification maps a ((Tree)) to a ((data:Box)) expression.}
alias Style = Box(Tree);

@synopsis{Repeats this option from ((util::LanguageServer)) to avoid a cyclic dependency.}
data FileSystemChange(bool needsConfirmation = false);

@synopsis{Generates a file formatter that immediately applies the new style to the input.}
void(loc) fileFormatter(type[&G <: Tree] grammar, Style style, bool needsConfirmation=false) {
    list[TextEdit](loc) toEdits = fileEdits(grammar, style);

    return void (loc input) {  
        executeFileSystemChanges([changed(input, toEdits(input), needsConfirmation=needsConfirmation)]);
    };
}

@synopsis{Generates a string formatter}
str(str) stringFormatter(type[&G <: Tree] grammar, Style style) {
    list[TextEdit](str) toEdits = stringEdits(grammar, style);

    return str (str input) {
        return executeTextEdits(input, toEdits(input));
    };
} 

@synopsis{Generates a file formatter that produces ((TextEdit))s for downstream processing.}
list[TextEdit](loc) fileEdits(type[&G <: Tree] grammar, Style style) {
    &G(value, loc) p = parser(grammar);

    return list[TextEdit] (loc input) {
        &G tree = p(input, input);
        try {
            return layoutDiff(tree, p(format(style(tree)), input));
        }
        catch ParseError(loc place): { 
            writeFile(|tmp:///<input.file>|, format(style(tree)));
            warning("Ignoring formatter output, which contained a new parse error.", |tmp:///<input.file>|(place.offset, place.length));
            return [];
        }
        catch Ambiguity(loc place, str _, str _): { 
            writeFile(|tmp:///<input.file>|, format(style(tree)));
            warning("Ignoring formatter output, which contained a new ambiguity.", |tmp:///<input.file>|(place.offset, place.length));
            return [];
        }  
    };
}
    
@synopsis{Generates a string formatter that produces ((TextEdit))s for downstream processing.}
list[TextEdit](str) stringEdits(type[&G <: Tree] grammar, Style style) {
    &G(str, loc) p = parser(grammar);
    loc stub = |memory:///formatted.txt|;
    
    return list[TextEdit] (str input) {
        &G tree = p(input, stub);
        try {
            return layoutDiff(tree, p(format(toBox(tree)), stub));
        }
        catch ParseError(loc place): { 
            writeFile(stub, format(toBox(tree)));
            warning("Ignoring formatter output, which contained a new parse error.", stub(place.offset, place.length));
            return [];
        }
        catch Ambiguity(loc place, str _, str _): { 
            writeFile(stub, format(style(tree)));
            warning("Ignoring formatter output, which contained a new ambiguity.", stub(place.offset, place.length));
            return [];
        } 
        catch AssertionFailed(str msg): {
            writeFile(stub, format(style(tree)));
            warning("Ignoring formatter output, which changed the syntax or semantics of the file: <msg>", stub);
            return [];
        }  
    };
}
    
@synopsis{Generates an HTML-based preview of the result of formatting.}
@benefits{
* uses ((util::Highight)) to create an HTML preview of the formatted code
* can also print directly to the console with ANSI coloring
* regenerates parser and style functions every time to avoid looking at stale code.
}
@pitfalls{
* not useful for production contexts
}
void debugFileFormat(type[&G <: Tree] grammar, Style style, loc input, bool console=false) {
    str(str) formatter = stringFormatter(grammar, style);
    str pretty = formatter(readFile(input));

    if (!console) {
        str highlighted = toHTML(parse(grammar, pretty, input), withStyle=true);
        showInteractiveContent(html(highlighted)[title="formatted <input>"]);
    }
    else {
        str highlighted = toANSI(parse(grammar, pretty, input));
        println(highlighted);
    }
}

@synopsis{Generates previews of the result of formatting for all files in a directory structure.}
@benefits{
* uses ((IO::writeFile)) to generate a file preview of the formatted code in a shadow directory structure (easy for using `git` or `diff`)
* can also stream to the console with ANSI colors for quickly checking formatted style.
* regenerates parser and style functions every time to avoid looking at stale code.
}
@pitfalls{
* not useful for production contexts
* may have a very long running time
}
void debugFilesFormat(type[&G <: Tree] grammar, Style style, loc root, str extension, bool ansi=false, bool console=false) {
    loc shadowRoot = root.parent + "formatted-<root.file>";
    str(str) formatter = stringFormatter(grammar, style);
    &G(value, loc) p = parser(grammar);
    list[loc] files = sort(find(root, extension));

    job("formatting", bool (void (str message, int worked) step) {
        for (loc input <- files) {
            step("<input>", 1);
            str pretty = formatter(readFile(input));
            if (ansi) {
                pretty = toANSI(p(pretty, input));
            }
            if (console) {
                println("###$ <input>");
                println(pretty);
            }
            else {
                writeFile(shadowRoot + relativize(root, input).path, pretty);;
            }
        }

        return true;
    }, totalWork=size(files));   
}

@synopsis{Generates an in-memory preview of the result of formatting and opens an editor for it.}
@benefits{
* opens the new file with the same file extension to activate possible language servers (syntax highlighting)
* regenerates parser and style functions every time to avoid looking at stale code.
}
@pitfalls{
* not useful for production contexts
}
void debugStringFormat(type[&G <: Tree] grammar, Style style, str input) {
    str(str) formatter = stringFormatter(grammar, style);
    str pretty = formatter(input);
    str highlighted = toHTML(parse(grammar, pretty, |unknown:///|));
    showInteractiveContent(html(highlighted)[title="formatted string"]);
}