module lang::rascalcore::compile::Examples::Tst0



data Tree;

@synopsis{Generates parsers from a grammar (reified type), where all non-terminals in the grammar can be used as start-symbol.}
@description{
This parser generator behaves the same as the `parser` function, but it produces parser functions which have an additional
nonterminal parameter. This can be used to select a specific non-terminal from the grammar to use as start-symbol for parsing.
}
@javaClass{org.rascalmpl.library.Prelude}
java &U (type[&U] nonterminal, value input, loc origin) 
    parsers(type[&T] grammar, 
        bool allowAmbiguity=false, 
        bool hasSideEffects=false,  
        set[Tree(Tree)] filters={}); 

////
////import ParseTree;
////
////layout Whitespace = [\ \t\n]*;
////
////start syntax D = "d";
////start syntax DS = D+ ds;
////
////
////value main() //= test bool parseD1() = 
////   =  (D)`d` := parse(#D, "d");