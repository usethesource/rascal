module lang::rascalcore::compile::Examples::Tst3

syntax Program = ;

data Tree;

@javaClass{org.rascalmpl.library.Prelude}
java &T (value input, loc origin) parser(type[&T] grammar);

alias Parser           = Tree (str /*input*/, loc /*origin*/);
alias Outliner         = list[DocumentSymbol] (Tree /*input*/);
data DocumentSymbol;

data LanguageService
    = parser(Parser parser)
    | outliner(Outliner outliner)
    ;

set[LanguageService] SL = {
 outliner(picoOutliner),
    parser(parser(#start[Program]))
   
    };

list[DocumentSymbol] picoOutliner(start[Program] _input) = [];
 