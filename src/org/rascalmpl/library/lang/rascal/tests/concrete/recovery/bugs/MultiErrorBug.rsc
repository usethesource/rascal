module lang::rascal::tests::concrete::recovery::bugs::MultiErrorBug

import ParseTree;
import IO;
import util::ErrorRecovery;
import List;
import vis::Text;

start syntax Module	= SyntaxDefinition* ;

syntax SyntaxDefinition =  Prod ";" ;

lexical Name = [A-Z];

syntax Sym
	= Sym NonterminalLabel
	| StringConstant
	;

lexical StringConstant = "\"" StringCharacter* "\"" ;

lexical StringCharacter = ![\"] ;

lexical NonterminalLabel = [a-z] ;

syntax Prod = Sym* ;

// This is an open issue: instead of two small errors, one big error tree is returned.
bool multiErrorBug() {
    str input = "#\"a\";#\"b\";";
    Tree t = parser(#start[Module], allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=false|);

    println(prettyTree(t));

    list[Tree] errors = findAllErrors(t);
    println("<size(errors)> Errors");
    for (Tree error <- errors) {
        Tree skipped = getSkipped(error);
        println("  <skipped@\loc>: <getErrorText(error)>");
    }
    Tree disambiguated = disambiguateErrors(t);
    list[Tree] disambiguatedErrors = findAllErrors(disambiguated);
    println("After disambiguating:");
    println("<size(disambiguatedErrors)> Errors");
    for (Tree error <- disambiguatedErrors) {
        Tree skipped = getSkipped(error);
        println("  <skipped@\loc>: <getErrorText(error)>");
    }
    return true;
}

