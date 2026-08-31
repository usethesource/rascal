@synopsis{Demonstrates the use of ((util::Formatting)) for constructing a declarative and HiFi Pico formatting pipeline}
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
import lang::pico::\syntax::Main;
import util::Formatters;
import analysis::diff::edits::TextEdits;

@synopsis{In-place formatting of an entire Pico file}
void (loc) formatPicoFile = fileFormatter(#start[Program], toBox);

@synopsis{Format a string that contains an entire Pico program}
str (str file) formatPicoString = stringFormatter(#start[Program], toBox);
    
@synopsis{Pico Format function for reuse in file, str or IDE-based formatting contexts}
list[TextEdit] (start[Program] file) formatPicoTree = treeEditFormatter(#start[Program], toBox);
    
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