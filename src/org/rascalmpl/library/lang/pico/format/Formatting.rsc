@synopsis{Demonstrates ((Tree2Box)), ((Box2Text)) and ((HiFiLayoutDiff)) for constructing a declarative and HiFi Pico formatting pipeline}
@description{
Using four generic or generated, "language parametric", building blocks we construct a Pico formatting pipeline:

* ((ParseTree)) is used to _generate_ a parser for Pico.   
* ((Tree2Box)) provides the extensible/overridable and declarative ((toBox)) function which maps language constructs to Box expressions. 
The ((toBox)) function combines generic language-parametric rules, as well as bespoke language specific rules..
* ((Box2Text)) is a _generic_ reusable algorithm for two-dimensional string layout.
* Finally, ((HiFiLayoutDiff)) _generically_ extracts ((TextEdit))s from two trees which are equal modulo whitespace and comments.
}
@benefits{
* The formatting is style is programmed _declaratively_ by mapping language patterns to Box expressions.
* The pipeline never loses source code comments, and this requires no attention from the language engineer.
}
@pitfalls{
* ((Box2Text)) must be _extended_ for the open recursive calls of ((toBox)) to reach the extensions in the current module. 
If you import ((Box2Text)) the extended ((toBox)) rules will only be found if they describe top-level tree nodes.
}
module lang::pico::format::Formatting

extend lang::box::util::Tree2Box;

import ParseTree;
import analysis::diff::edits::ExecuteTextEdits;
import analysis::diff::edits::HiFiLayoutDiff;
import lang::box::\syntax::Box;
import lang::box::util::Box2Text;
import lang::pico::\syntax::Main;

@synopsis{In-place formatting of an entire Pico file}
void formatPicoFile(loc file) {
    edits = formatPicoTree(parse(#start[Program], file));
    executeFileSystemChanges([changed(file, edits)]);
}

@synopsis{Format a string that contains an entire Pico program}
str formatPicoString(str file) {
    start[Program] tree = parse(#start[Program], file, |unknown:///|);
    return executeTextEdits(file, formatPicoTree(tree));
}

@synopsis{Pico Format function for reuse in file, str or IDE-based formatting contexts}
list[TextEdit] formatPicoTree(start[Program] file) {
    formatted = format(toBox(file));
    return layoutDiff(file, parse(#start[Program], formatted, file@\loc.top));
}

@synopsis{Format while}
Box toBox((Statement) `while <Expression e> do <{Statement ";"}* block> od`, FO opts = fo())
    = V(
        H(L("while"), HV(toBox(e, opts=opts)), L("do")),
        I(toClusterBox(block, opts=opts)),
        L("od")
    ); 

@synopsis{Format if-then-else }
Box toBox((Statement) `if <Expression e> then <{Statement ";"}* thenPart> else <{Statement ";"}* elsePart> fi`, FO opts = fo())
    = V(
        H(L("if"), HV(toBox(e, opts=opts)), L("then")),
            I(toClusterBox(thenPart, opts=opts)),
        L("else"),
            I(toClusterBox(elsePart, opts=opts)),
        L("fi")
    ); 