module lang::rascalcore::compile::Examples::Tst1

//@javaClass{org.rascalmpl.library.Prelude}
//public java int (value input) parse(int GRAMMAR);

//public int (value input) parse(int GRAMMAR) = int(value v){return GRAMMAR; };

data Tree;

@javaClass{org.rascalmpl.library.Prelude}
public java &T (value input, loc origin) parser(type[&T] grammar, bool allowAmbiguity=false, bool hasSideEffects=false, bool firstAmbiguity=false, set[Tree(Tree)] filters={}); 


//import ParseTree;
//import Exception;
//import IO;
//import lang::rascal::tests::concrete::OtherSyntax;
//
//syntax A = "a";
//layout WS = [\ \t\n\r]*;
//
//value main() = (A) `a` := parse(#A,"a");
