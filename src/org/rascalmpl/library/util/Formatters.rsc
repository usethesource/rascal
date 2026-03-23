@synopsis{Tools for generating and debugging ((Box-Box))-based formatting pipelines.}
@description{
This module provides a number of generative functions (functions that produce functions) to construct
efficient and accurate formatters for programming languages and domain-specific languages.

All formatter generators take two specifications as parameter:
* `type[Tree] grammar` which specifies the input type and/or the parser to generate to re-parse formatted output.
* `Style style`, which uses the ((Style)) type to pass in your specification that maps all language constructors to a ((Box-Box)) term. Typically this is an extension of the ((Tree2Box-toBox)) default style.

We generate formatting functions here with different input parameter types (((ParseTree-Tree)), `loc` or `str`) 
and different return types `void` for IO side-effects, `list[TextEdit]` for IDE integration and `str` for normal string output:

| generator | generated formatter | comment | 
| --------- |  ------------------ | ------- | 
| ((fileFormatter)) | `void (loc)` | replaces the current file |
| ((stringFormatter)) | `str (str) ` |     |
| ((subStringFormatter)) | str (type[Tree], str) | can be used on any non-terminal in the grammar |
| ((fileEdits)) | `list[TextEdit] (loc)` | use ((executeTextEdits)) or ((executeFileSystemChanges)) or ((applyFileSystemEdits)) later |
| ((treeEdits)) | `list[TextEdit] (Tree)` |  |
| ((subTreeEdits)) | `list[TextEdit](Tree)` | this uses a subparser based on the top-level type of the input tree` |
| ((stringEdits)) | `list[TextEdit](str)` | |
| ((subStringEdits)) | `list[TextEdit](type[Tree], str)` | |

If the pipeline you require is not in the above set, then it is advised to copy on of the above and adapt. 

All of the above generators capture the current state of the grammar and the current state of the style function. So if you
change the grammar or the ((Tree2Box-toBox)) style, then you have to "regenerate" and call these functions again.

During the development of a formatting style, regeneration is the default and visual feedback is essential. To accommodate
the activity of developing a style incrementally, or by trial-and-error, the following debugging functions come in handy:
* ((debugFileFormat)) generates a HTML or ANSI based preview of an example file
* ((debugFilesFormat)) generates formatted versions of all files in an entire file hierarchy, or appends all their formatted examples to one big file, or prints that file to the console. Adds ANSI codes optionally.
* ((debugStringFormat)) generates a formatter version in an HTML preview or on the console with ANSI codes.

The debug functions always take the grammar and the ((Tree2Box-toBox)) style parameter and these are never captured. This guarantees
that while you are debugging you are always looking at the current version of your specifications.

As ((FormattingOptions)) you can configure different parts of the pipeline in one go using the `formattingOptions()` constructor and these optional parameters:
* `maxWidth` - indicates a hard limit for wrapping and switching to vertical mode
* `breakAfter` - indicates hard limit under which wrapping and switching to vertical mode must be avoided
* `tabSize` - indicates the default indentation size
* `caseInsensitivity` - ((CaseInsensitivity)) to influence how the state of each individual case-insensitive keyword us either propagated or normalized while formatting.
* `needsConfirmation` - triggers UI behavior with confirmation dialogs and possible previews
* `insertSpaces``, when set to true it prefers spaces over tabs, when set to false we use tabs for indentation (see `tabSize`)
* `trimTrailingWhitespace` when `true` the formatter can not leave spaces or tabs after the last non-whitespace character,
when false it does not matter. (origin: ((util::LanguageServer)))
* `insertFinalNewline`,  insert a newline character at the end of the file if one does not exist. (origin: ((util::LanguageServer)))
* `trimFinalNewlines`, trim all newlines after the final newline at the end of the file. (origin: ((util::LanguageServer))
}
@benefits{
* speed: 
   1. the generators cache a generated parser for fast reuse between calls to formatting pipeline, and a fully composed ((Tree2Box-toBox)) function with a pre-computed dispatch hashtable.
   2. ((layoutDiff)) isolates only what changes and leaves what is unchanged alone.
* safety: if the generated formatters produce illegal or ambiguous syntax, a warning is printed and no changes are produced.
* accuracy: the generated formatters retain all comments, as long as comments are annotated with `@category("Comment")` or `@category("comment")` in the grammar.
* practicality: the generated formatters can produce ((TextEdit))s which are consumed by IDEs such as VScode.
* shyness: this setup guarantees a minimal amount of specification from the language engineer because:
   1. the default ((Tree2Box-toBox)) formatter takes typically 40% of the (more boring) work.
   2. the ((layoutDiff)) algorithm recovers comments, so case distinctions for existence of comments _everywhere_ are unnecessary.
   3. the ((Box-Box)) constraints allow for adaptive layout strategies, based on available space, which are still style-consistent _for free_.
   4. robustness: built-in post-formatter parser check guarantees the user is _never_ confronted with syntactically broken output.
}
@pitfalls{
* the formatters capture the current state of the grammar and the style function, so if you
change their source code, you have to regenerate the formatters.
* case insensitivy normalization is implemented by both ((Tree2Box)) and ((HiFiLayoutDiff)), for efficiency reasons we only use the ((HiFiLayoutDiff)) implementation here.
* all pipelines here use ((Tree2Box)), then ((Box2Text)), and then parse again and then produce diffs using ((HiFiLayoutDiff)). Strictly speaking if you only need the full string, then
you can use ((Tree2Box)) and ((Box2Text)) and get the formatted result a bit faster. We include ((HiFiLayoutDiff)) here always because debugging and testing IDE features like formatting
is then simulated as accurately as possible.
}
module util::Formatters

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

@synopsis{A style specification maps a ((Tree)) to a ((Box-Box)) expression.}
alias Style = Box(Tree);

@synopsis{Additional formatter options}
@description{
* `needsConfirmation` when set to true triggers a confirmation dialog for applying the proposed edits. The UI may even show a preview or a diff editor.
* `caseInsensitivity` can be used to keep or normalize the style of case insensitivy keywords. See ((CaseInsensitivity)) for the options.
}
data FormattingOptions(
    bool needsConfirmation=false,
    CaseInsensitivity caseInsensitivity = asIs()
);

@synopsis{short-hand}
private FormattingOptions fo() = formattingOptions();

@synopsis{Repeats this option from ((util::LanguageServer)) to avoid a cyclic dependency.}
data FileSystemChange(bool needsConfirmation = false);

@synopsis{Generates a file formatter that immediately applies the new style to the input.}
void(loc) fileFormatter(type[&G <: Tree] grammar, Style style, FormattingOptions opts=fo()) {
    list[TextEdit](loc) toEdits = fileEdits(grammar, style, opts=opts);

    return void (loc input) {  
        executeFileSystemChanges([changed(input, toEdits(input), needsConfirmation=opts.needsConfirmation)]);
    };
}

@synopsis{Generates a string formatter}
str(str) stringFormatter(type[&G <: Tree] grammar, Style style, FormattingOptions opts=fo()) {
    list[TextEdit](str) toEdits = stringEdits(grammar, style, opts=opts);

    return str (str input) {
        return executeTextEdits(input, toEdits(input));
    };
}

@synopsis{Generates a substring formatter}
str(type[Tree], str) subStringFormatter(type[&G <: Tree] grammar, Style style, FormattingOptions opts=fo()) {
    list[TextEdit](type[Tree], str) toEdits = subStringEdits(grammar, style, opts=opts);

    return str (type[Tree] nonterminal, str input) {
        return executeTextEdits(input, toEdits(nonterminal, input));
    };
}

@synopsis{Generates a file formatter that produces ((TextEdit))s for downstream processing.}
list[TextEdit](loc) fileEdits(type[&G <: Tree] grammar, Style style, FormattingOptions opts=fo()) {
    &G(value, loc) p = parser(grammar);

    return list[TextEdit] (loc input) {
        &G tree = p(input, input);
        try {
            return layoutDiff(tree, p(format(style(tree), opts=opts[ci=asIs()]), input), ci=opts.caseInsensitivity);
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

@synopsis{Generates a file formatter that produces ((TextEdit))s from a ((Tree)) for downstream processing.}
list[TextEdit](&G <: Tree) treeEdits(type[&G <: Tree] grammar, Style style, FormattingOptions opts=fo()) {
    &G(value, loc) p = parser(grammar);

    return list[TextEdit] (&G <: Tree tree) {
        try {
            return layoutDiff(tree, p(format(style(tree), opts=opts[ci=asIs()]), tree@\loc.top), ci=opts.caseInsensitivity);
        }
        catch ParseError(loc place): { 
            writeFile(|tmp:///<tree@\loc.top.file>|, format(style(tree), opts=opts[ci=asIs()]));
            warning("Ignoring formatter output, which contained a new parse error.", |tmp:///<tree@\loc.top.file>|(place.offset, place.length));
            return [];
        }
        catch Ambiguity(loc place, str _, str _): { 
            writeFile(|tmp:///<tree@\loc.top.file>|, format(style(tree), opts=opts[ci=asIs()]));
            warning("Ignoring formatter output, which contained a new ambiguity.", |tmp:///<tree@\loc.top.file>|(place.offset, place.length));
            return [];
        }  
    };
}

@synopsis{Generates a file formatter that produces ((TextEdit))s from sub((Trees)s for downstream processing.}
list[TextEdit](Tree) subTreeEdits(type[&G <: Tree] grammar, Style style, FormattingOptions opts=fo()) {
    Tree (type[Tree], value, loc) p = parsers(grammar);

    return list[TextEdit] (Tree tree) {
        try {
            return layoutDiff(tree, p(type(delabel(tree.prod.def), ()), format(style(tree), opts=opts[ci=asIs()]), tree@\loc), ci=opts.caseInsensitivity);
        }
        catch ParseError(loc place): { 
            writeFile(|tmp:///<tree@\loc.top.file>|, format(style(tree), opts=opts[ci=asIs()]));
            warning("Ignoring formatter output, which contained a new parse error.", |tmp:///<tree@\loc.top.file>|(tree@\loc.offset + place.offset, place.length));
            return [];
        }
        catch Ambiguity(loc place, str _, str _): { 
            writeFile(|tmp:///<tree@\loc.top.file>|, format(style(tree), opts=opts[ci=asIs()]));
            warning("Ignoring formatter output, which contained a new ambiguity.", |tmp:///<tree@\loc.file>|(tree@\loc.offset + place.offset, place.length));
            return [];
        }  
    };
}

private Symbol delabel(label(_, Symbol s)) = delabel(s);
private Symbol delabel(conditional(Symbol s, _)) = delabel(s);
private default Symbol delabel(Symbol s) = s;

int stubCounter = 1;

@synopsis{Generates a string formatter that produces ((TextEdit))s for downstream processing.}
list[TextEdit](str) stringEdits(type[&G <: Tree] grammar, Style style, FormattingOptions opts=fo()) {
    &G(str, loc) p = parser(grammar);
    loc stub = |tmp:///| + "formatted<stubCounter>.txt";
    stubCounter = stubCounter + 1;
    
    return list[TextEdit] (str input) {
        &G tree = p(input, stub);
        try {
            return layoutDiff(tree, p(format(style(tree), opts=opts[ci=asIs()]), stub), ci=opts.caseInsensitivity);
        }
        catch ParseError(loc place): { 
            writeFile(stub, format(style(tree), opts=opts[ci=asIs()]));
            warning("Ignoring formatter output, which contained a new parse error.", stub(place.offset, place.length));
            println("Ignoring formatter output, which contained a new parse error. <stub(place.offset, place.length)>");
            return [];
        }
        catch Ambiguity(loc place, str _, str _): { 
            writeFile(stub, format(style(tree), opts=opts[ci=asIs()]));
            warning("Ignoring formatter output, which contained a new ambiguity.", stub(place.offset, place.length));
            return [];
        } 
        catch AssertionFailed(str msg): {
            writeFile(stub, format(style(tree), opts=opts[ci=asIs()]));
            warning("Ignoring formatter output, which changed the syntax or semantics of the file: <msg>", stub);
            println("Ignoring formatter output, which changed the syntax or semantics of the file: <msg>, <stub>");
            return [];
        }  
    };
}

@synopsis{Generates a string formatter that produces ((TextEdit))s, from substrings with a given nonterminal, for downstream processing.}
@description{
* `grammar` should be the entire grammar for the language
* `style` should be the entire `toBox` function for this formatter
* the function returns a binary function that asks for the right non-terminal of the language to use to parse the second
parameter (a substring). For example: `#Statement` and `if (true) false;`.
}
list[TextEdit](type[Tree], str) subStringEdits(type[&G <: Tree] grammar, Style style, FormattingOptions opts=fo()) {
    Tree(type[Tree], str, loc) p = parsers(grammar);
    loc stub = |tmp:///| + "formatted-<stubCounter>.txt";
    stubCounter = stubCounter + 1;

    return list[TextEdit] (type[Tree] nonterminal, str input) {
        &G tree = p(nonterminal, input, stub);
        try {
            return layoutDiff(tree, p(nonterminal, format(style(tree), opts=opts[ci=asIs()]), stub), ci=opts.caseInsensitivity);
        }
        catch ParseError(loc place): { 
            writeFile(stub, format(style(tree), opts=opts[ci=asIs()]));
            warning("Ignoring formatter output, which contained a new parse error.", stub(place.offset, place.length));
            return [];
        }
        catch Ambiguity(loc place, str _, str _): { 
            writeFile(stub, format(style(tree), opts=opts[ci=asIs()]));
            warning("Ignoring formatter output, which contained a new ambiguity.", stub(place.offset, place.length));
            return [];
        } 
        catch AssertionFailed(str msg): {
            writeFile(stub, format(style(tree), opts=opts[ci=asIs()]));
            warning("Ignoring formatter output, which changed the syntax or semantics of the file: <msg>", stub);
            return [];
        }  
    };
}
    
@synopsis{Generates an HTML-based preview of the result of formatting.}
@benefits{
* uses ((util::Highlight)) to create an HTML preview of the formatted code
* can also print directly to the console with ANSI coloring
* regenerates parser and style functions every time to avoid looking at stale code.
}
@pitfalls{
* not useful for production contexts
}
void debugFileFormat(type[&G <: Tree] grammar, Style style, loc input, FormattingOptions opts=fo(), bool HTML=true, bool console=!HTML) {
    str(str) formatter = stringFormatter(grammar, style, opts=opts);
    str pretty = formatter(readFile(input));
    &G reparsed = parse(grammar, pretty, input);

    if (HTML) {
        showInteractiveContent(html(toHTML(reparsed, withStyle=true))[title="formatted <input>"]);
    }
    
    if (console) {
        println(toANSI(reparsed));
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
void debugFilesFormat(type[&G <: Tree] grammar, Style style, loc root, str extension, FormattingOptions opts=fo(), bool shadowFiles = true, bool appendFile=!shadowFiles, bool console=!shadowFiles && !appendFile, bool ansi=console || appendFile) {
    loc shadowRoot = root.parent + "formatted-<root.file>";
    loc appendLoc = root + "format.log";
    str(str) formatter = stringFormatter(grammar, style, opts=opts);
    &G(value, loc) p = parser(grammar);
    list[loc] files = sort(find(root, extension));

    if (shadowFiles) {
        remove(shadowRoot, recursive=true);
        println("The shadow file hierarchy is being written under <shadowRoot> and ANSI codes are <if (!ansi) {>not<}> used.");
    }

    if (appendFile) {
        remove(appendLoc);
        println("The append file is <appendLoc> and does <if (!ansi) {>not<}> use ANSI codes.");
    }

    if (console) {
        println("All formatted files are written to the console and ANSI is <if (!ansi) {>not<}> being used");
    }

    job("formatting", bool (void (str message, int worked) step) {
        for (loc input <- files) {
            step("<input>", 1);
            str pretty = formatter(readFile(input));

            if (ansi) {
                pretty = toANSI(p(pretty, input));
            }

            if (shadowFiles) {
                writeFile(shadowRoot + relativize(root, input).path, pretty);;
            }
            
            if (console) {
                println("### <input>");
                println(pretty);
            }

            if (appendFile) {
                appendToFile(appendLoc, 
                    "### <input>
                    '<pretty>"
                );
            }
        }

        return true;
    }, totalWork=size(files));   
}

@synopsis{Generates an in-memory preview of the result of formatting and opens an HTML viewer for it, or prints the results directly to the console.}
@benefits{
* opens the new file with the same file extension to activate possible language servers (syntax highlighting)
* regenerates parser and style functions every time to avoid looking at stale code.
}
@pitfalls{
* not useful for production contexts
}
void debugStringFormat(type[&G <: Tree] grammar, Style style, str input, FormattingOptions opts=fo(), bool HTML = true, bool console = !HTML) {
    str(str) formatter = stringFormatter(grammar, style, opts=opts);
    str pretty = formatter(input);
    &G reparsed = parse(grammar, pretty, |unknown:///|);
    
    if (HTML) {
        showInteractiveContent(html(toHTML(reparsed))[title="formatted string"]);
    }

    if (console) {
        println(toANSI(reparsed));
    }
}