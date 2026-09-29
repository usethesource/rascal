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

import IO;
import ParseTree;
import lang::pico::\syntax::Main;
import util::Formatters;
import analysis::diff::edits::TextEdits;

@synopsis{In-place formatting of an entire Pico file}
public void (loc) formatPicoFile = fileFormatter(#start[Program], toBox);

@synopsis{Format a string that contains an entire Pico program}
public str (str file) formatPicoString = stringFormatter(#start[Program], toBox);
    
@synopsis{Pico Format function for reuse in file, str or IDE-based formatting contexts}
public list[TextEdit] (start[Program] file) formatPicoTree = treeEditFormatter(#start[Program], toBox);
    
@synopsis{Format while}
Box toBox((Statement) `while <Expression e> do <{Statement ";"}* block> od`)
    = V(
        H(L("while"), HV(toBox(e)), L("do")),
        I(toBox(block)),
        L("od")
    ); 

@synopsis{Format if-then-else }
Box toBox((Statement) `if <Expression e> then <{Statement ";"}* thenPart> else <{Statement ";"}* elsePart> fi`)
    = V(
        H(L("if"), HV(toBox(e)), L("then")),
            I(toBox(thenPart)),
        L("else"),
            I(toBox(elsePart)),
        L("fi")
    ); 

@synopsis{Format if-then }
Box toBox((Statement) `if <Expression e> then <{Statement ";"}* thenPart> fi`)
    = V(
        H(L("if"), HV(toBox(e)), L("then")),
            I(toBox(thenPart)),
        L("fi")
    );

Box toBox((Declarations) `declare <{IdType ","}* decls>;`)
    = V(
        L("declare"),
        I(V(SL([toBox(d) | d <- decls], L(","), op=H0()))),
        L(";")
    );

Box toBox((Program) `begin <Declarations decls> <{Statement  ";"}* body> end`)
    = V(
        L("begin"),
        V(
            I(toBox(decls)),
            I(V(toBox(body)))
        , vs=1),
        L("end")
    );