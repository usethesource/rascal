@bootstrapParser
module lang::rascalcore::check::tests::SyntaxDeclarationTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool Lexical1()
    = checkModuleOK("module Lexical1 lexical INT = [0-9]+;");

test bool Syntax1()
    = checkModuleOK("module Syntax1 syntax EXP = EXP "+" EXP;");

test bool SyntaxWithLabels() 
    = checkModuleOK("module SyntaxWithLabels syntax EXP = EXP lhs \"+\" EXP rhs;");

test bool SyntaxWithConstructorAndLabels() 
    = checkModuleOK("module SyntaxWithConstructorAndLabels syntax EXP = add: EXP lhs \"+\" EXP rhs;");

test bool LexicalAndSyntax1() = checkModuleOK("
    module LexicalAndSyntax1
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | add: EXP lhs \"+\" EXP rhs
                   ;
");

test bool UndefinedLexicalName() = unexpectedTypeInModule("
    module UndefinedLexicalName 
        lexical INT = [0-9]+; 
        syntax EXP = intcon: Int intcon 
                   | add: EXP lhs \"+\" EXP rhs 
                   ;
");
 
test bool LexicalAndSyntax2() = checkModuleOK("
    module LexicalAndSyntax2
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | add: EXP lhs \"+\" EXP rhs 
                   ;
        EXP exp;
");

test bool UndefinedSyntaxName() = unexpectedTypeInModule("
    module UndefinedSyntaxName
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | add: EXP lhs \"+\" EXP rhs 
                   ;
        Exp exp;
");

test bool Field1() = checkModuleOK("
    module Field1
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | add: EXP lhs \"+\" EXP rhs ;
        EXP exp1;
        EXP exp2 = exp1.lhs;
");

test bool Field2() = checkModuleOK("
    module Field2
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | add: EXP lhs \"+\" EXP rhs 
                   ;
        EXP exp;
        INT n = exp.intcon;
");

test bool ITER1() = checkModuleOK("
    module ITER1
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | exps: {EXP \",\"}+ exps 
                   ;
");

test bool ITER2() = checkModuleOK("
    module ITER2
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | exps: {EXP \",\"}+ exps
                   ;
        EXP exp;
        {EXP \",\"}+ es = exp.exps;
");

test bool ITER3() = checkModuleOK("
    module ITER3
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | exps: EXP+ exps
                   ;
        EXP exp;
        EXP+ es = exp.exps;
");

test bool START1() = checkModuleOK("
    module START1
        lexical INT = [0-9]+; 
        start syntax EXP = intcon: INT intcon 
                         | exps: EXP+ exps
                         ;
        EXP exp;
        EXP+ es = exp.exps;
");

test bool KEYWORD1() = checkModuleOK("
    module KEYWORD1
        keyword KEY = \"a\" | \"b\" | \"c\";
               
        KEY k;
");
                 
test bool LAYOUT1() = checkModuleOK("
    module LAYOUT1
        layout L = \" \"+;
");

test bool PRIO1() = checkModuleOK("
    module PRIO1
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | EXP \"*\" EXP
                   \> EXP \"+\" EXP
                   ;
");

test bool PRIO2() = checkModuleOK("
    module PRIO2
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | mul: EXP lhs \"*\" EXP rhs
                   \> add: EXP lhs \"+\" EXP rhs
                   ;
");

test bool PRIO3() = checkModuleOK("
    module PRIO3
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | mul: EXP lhs \"*\" EXP rhs
                   \> add: EXP lhs \"+\" EXP rhs
                   ;
        EXP exp1;
        EXP exp2 = exp1.lhs;
");

test bool LEFT1() = checkModuleOK("
    module LEFT1
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | left( mul: EXP lhs \"*\" EXP rhs
                         \> add: EXP lhs \"+\" EXP rhs
                        )
                   ;
        EXP exp1;
        EXP exp2 = exp1.lhs;
");

test bool RIGHT1() = checkModuleOK("
    module RIGHT1
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                  | right( mul: EXP lhs \"*\" EXP rhs
                        \> add: EXP lhs \"+\" EXP rhs
                         )
                  ;
        EXP exp1;
        EXP exp2 = exp1.lhs;
");

test bool NONASSOC1() = checkModuleOK("
    module NONASSOC1
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | non-assoc( mul: EXP lhs \"*\" EXP rhs
                              \> add: EXP lhs \"+\" EXP rhs
                              )
                   ;
        EXP exp1;
        EXP exp2 = exp1.lhs;
");

test bool ESC1() = checkModuleOK("
    module ESC1
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | \\add: EXP lhs \"+\" EXP rhs ;
        EXP exp1;
        EXP exp2 = add(exp1, exp1);
");

test bool COND1() = checkModuleOK("
    module COND1
        lexical INT = [0-9]+; 
        syntax EXP = intcon: INT intcon 
                   | right( mul: EXP lhs \"*\" EXP rhs
                          \> add: EXP lhs \"+\" EXP rhs !\>\> [0-9]
                          )
                   ;
        EXP exp1;
        EXP exp2 = add(exp1, exp1);
");

test bool SynSynOverloadOk() = checkModuleOK("
    module SynSynOverloadOk
        syntax A = \"a\"; 
        syntax A = \"b\";
");

test bool LexLexOverloadOk() = checkModuleOK("
    module LexLexOverloadOk
        lexical A = \"a\"; 
        lexical A = \"b\";
");

test bool LayoutLayoutOverloadOk() = checkModuleOK("
    module LayoutLayoutOverloadOk
        layout A = \"a\"; 
        layout A = \"b\";
");

test bool LexSynOverloadNotOk() = unexpectedDeclarationInModule("
    module LexSynOverloadNotOk 
        lexical A = [a-z]+;
        syntax A = \"a\"; 
");

test bool LexLayoutOverloadNotOk() = unexpectedDeclarationInModule("
    module LexLayoutOverloadNotOk 
        lexical A = [a-z]+;
        layout A = \"a\"; 
");

test bool LexKeywordOverloadNotOk() = unexpectedDeclarationInModule("
    module LexKeywordOverloadNotOk 
        lexical A = [a-z]+;
        keyword A = \"a\"; 
");

test bool DataLexOverloadOk() = checkModuleOK("
    module DataLexOverloadOk
        lexical A = [a-z]+;
        data A = a(); 
");

test bool DataSynOverloadOk() = checkModuleOK("
    module DataSynOverloadOk 
        syntax A = [a-z]+;
        data A = a();   
");

test bool DataLayoutOverloadOk() = checkModuleOK("
    module A 
        layout A = [a-z]+;
        data A = a(); 
");

test bool DataKeywordOverloadOk() = checkModuleOK("
    module DataKeywordOverloadOk 
        keyword A = \"if\";
        data A = a(); 
");

test bool NestingLexicalOk() = checkModuleOK("
    module NestingLexicalOk 
        lexical Id = [a-z]+;
        syntax EXP = Id; 
");

test bool InvertedLexicalNestingOk() = checkModuleOK("
    module InvertedLexicalNestingOk 
        syntax EXP =\"a\"; 
        lexical Id = [a-z]+ | \"-\" EXP;
");

test bool LexicalInLayoutOk() = checkModuleOK("
    module LexicalInLayoutOk 
        syntax EXP = \"a\"; 
        lexical Id = [a-z]+;
        layout WS = [\\ \\t\\n]* | \"/*\" Id+ \"*/\";
");

test bool MissingNonterminal() = unexpectedDeclarationInModule("
    module MissingNonterminal 
        syntax EXP = \"a\" | EXP \"+\" E; 
        data E = e();
");

test bool MayNotBeginWithLayout() = unexpectedDeclarationInModule("
    module MayNotBeginWithLayout 
        layout L = \" \";
        syntax EXP = L \"a\" | EXP \"+\" EXP; 
");

test bool MayNotEndWithLayout() =  unexpectedDeclarationInModule("
    module MayNotEndWithLayout 
        layout L = \" \";
        syntax EXP = \"a\" L | EXP \"+\" EXP; 
");

test bool SingleInternalLayoutOk() = checkModuleOK("
    module SingleInternalLayoutOk 
        layout L = \" \";
        syntax EXP = \"a\" | EXP  L \"+\" EXP; 
");

test bool ConsecutiveInternalLayoutNotOk() = unexpectedDeclarationInModule("
    module ConsecutiveInternalLayoutNotOk 
        layout L = \" \";
        syntax EXP = \"a\" | EXP  L L \"+\" EXP; 
");

test bool LayoutInLexOk() = checkModuleOK("
    module LayoutInLexOk 
        layout WS = [\\ \\t\\n]* ;
        lexical Dig = [0-9]+;
        lexical Real = Dig WS \".\" WS Dig;
");

test bool LitInKeywordOk() = checkModuleOK("
    module LitInKeywordOk 
        keyword Keywords = \"if\" | \"then\"| \"else\";
");

test bool CiLitInKeywordOk() = checkModuleOK("
    module CiLitInKeywordOk 
        keyword Keywords = \"if\" | \"then\"| \'else\';
");

test bool LexInKeywordOk() = checkModuleOK("
    module LexInKeywordOk 
        lexical Dig = [0-9]+;
        keyword Keywords = \"if\" | \"then\" | \"else\" | Dig;
");

test bool ExcludeKeywordOk() = checkModuleOK("
    module ExcludeKeywordOk 
        lexical Id = [a-z]+;
        
        keyword Keywords = \"if\" | \"then\" | \"else\";
        syntax EXP = Id \\ Keywords;
");

test bool ExcludeNonKeywordNotOk() = unexpectedDeclarationInModule("
    module ExcludeNonKeywordNotOk 
        lexical Id = [a-z]+;
        lexical Dig = [0-9]+;
        keyword Keywords = \"if\" | \"then\" | \"else\";
        syntax EXP = Id \\ Dig;
");

test bool RegexpInKeywordOk() = checkModuleOK("
    module RegexpInKeywordOk 
        keyword Keywords = \"if\" | \"then\" | [a-z]+;
");

test bool NonterminalInKeywordOk1() = checkModuleOK("
    module NonterminalInKeywordOk1
        keyword K = \"a\" | B;
        syntax B = \"b\" | \"bb\" | \"bbb\";
");

test bool RecursiveNonterminalInKeywordOk() = checkModuleOK("
    module RecursiveNonterminalInKeywordOk
        keyword K = \"a\" | B;
        syntax B = \"b\" | \"bb\" | \"bbb\" | K;
");

test bool NonterminalInKeywordOk2() = checkModuleOK("
    module NonterminalInKeywordOk2
        keyword K = \"a\" | B;
        syntax B = \"b\" | \"bb\" | [a-z];
");

test bool CharClassesLitsOk() = checkModuleOK("
    module CharClassesLitsOk 
        layout A = [a-z] \'hello\' \"goodbye\";
        lexical B = [a-z] \'hello\' \"goodbye\";
        syntax C = [a-z] \'hello\' \"goodbye\"; 
");

test bool LexAsSepOk() = checkModuleOK("
    module LexAsSepOk 
        lexical Id = [a-z]+;
        syntax EXP = Id;
        lexical SEP = \"X\"+;
        syntax Block = \"{\" {EXP SEP}* \"}\";
");

test bool KeywordAsSepOk () = checkModuleOK("
    module KeywordAsSepOk 
        lexical Id = [a-z]+;
        syntax EXP = Id;
        keyword Keywords =  \"if\" | \"then\" | \"else\";
        syntax Block = \"{\" {EXP Keywords}* \"}\";
");

test bool LayoutAsSepOk () = checkModuleOK("
    module LayoutAsSepOk 
        layout WS = [\\ \\t\\n]* ;
        lexical Id = [a-z]+;
        syntax EXP = Id;
        syntax Block = \"{\" {EXP WS}* \"}\";
");

test bool WarnNestedSS() = unexpectedDeclarationInModule("
    module WarnNestedSS 
        lexical A = \"a\";
        syntax B = A**;
");

test bool WarnNestedSP() = unexpectedDeclarationInModule("
    module WarnNestedSP 
        lexical A = \"a\";
        syntax B = A*+;
");

test bool WarnNestedPS() = unexpectedDeclarationInModule("
    module WarnNestedPS 
        lexical A = \"a\";
        syntax B = A+*;
");

test bool WarnNestedPP() = unexpectedDeclarationInModule("
    module WarnNestedPP 
        lexical A = \"a\";
        syntax B = A++;
");

test bool WarnNestedSepSS() = unexpectedDeclarationInModule("
    module WarnNestedSepSS 
        lexical A = \"a\";
        syntax B = {A* \",\"}*;
");

test bool WarnNestedSepSP() = unexpectedDeclarationInModule("
    module WarnNestedSepSP 
        lexical A = \"a\";
        syntax B = {A* \",\"}+;
");

test bool WarnNestedSepPS() = unexpectedDeclarationInModule("
    module WarnNestedSepPS 
        lexical A = \"a\";
        syntax B = {A+ \",\"}*;
");

test bool WarnNestedSepPP() = unexpectedDeclarationInModule("
    module WarnNestedSepPP 
        lexical A = \"a\";
        syntax B = {A+ \",\"}+;
");

test bool Param1() = checkModuleOK("
    module Param1
        syntax EXP[&T]  = con: &T con;
");

test bool Param2() = checkModuleOK("
    module Param2
        lexical INT     = [0-9]+;
        lexical STR     = \"\\\"\" ![\\\"]* \"\\\"\";
        syntax EXP[&T]  = con: &T con;
        EXP[INT] exp1;
        EXP[STR] exp2;
");

test bool Param3() = checkModuleOK("
    module Param3
    lexical INT     = [0-9]+;
    lexical STR     = \"\\\"\" ![\\\"]* \"\\\"\";

    syntax EXP[&T]  = con: &T con 
                    | right( mul: EXP[&T] lhs \"*\" EXP[&T] rhs
                    \> add: EXP[&T] lhs \"+\" EXP[&T] rhs !\>\> [0-9]
                    );
    EXP[INT] exp1;
    EXP[INT] exp2 = add(exp1, exp2);
");

@ignore{Does not generate error}
test bool UndefinedParam1() = undeclaredVariableInModule("
    module UndefinedParam1

    syntax EXP[&T]  = con: &T con 
                    | right( mul: EXP[&U] lhs \"*\" EXP[&T] rhs
                    \> add: EXP[&T] lhs \"+\" EXP[&T] rhs !\>\> [0-9]
                    );
");

test bool WrongNumberOfParams1() = argumentMismatchInModule("
    module WrongNumberOfParams1

    syntax EXP[&T]  = con: &T con 
                    | right( mul: EXP[&T,&T] lhs \"*\" EXP[&T] rhs
                    \> add: EXP[&T] lhs \"+\" EXP[&T] rhs !\>\> [0-9]
                    );
");

test bool WrongNumberOfParams2() = argumentMismatchInModule("
    module WrongNumberOfParams2

    syntax EXP[&T]  = con: &T con 
                    | right( mul: EXP lhs \"*\" EXP[&T] rhs
                    \> add: EXP[&T] lhs \"+\" EXP[&T] rhs !\>\> [0-9]
                    );
");

test bool WrongNonterminal1() = unexpectedTypeInModule("
    module WrongNonterminal1
  
    syntax EXP[&T]  = con: &T con 
                    | right( mul: ZZZ lhs \"*\" EXP[&T] rhs
                    \> add: EXP[&T] lhs \"+\" EXP[&T] rhs !\>\> [0-9]
                    );
    alias ZZZ = int;
");

test bool WrongNonterminal2() = unexpectedTypeInModule("
    module WrongNonterminal2
  
    syntax EXP[&T]  = con: &T con 
                    | right( mul: ZZZ[&T] lhs \"*\" EXP[&T] rhs
                    \> add: EXP[&T] lhs \"+\" EXP[&T] rhs !\>\> [0-9]
                    );
    alias ZZZ = int;
");

// https://github.com/cwi-swat/rascal/issues/442

test bool Issue442() = checkModuleOK("
    module Issue442
	    syntax A = \"a\";
		value my_main() = [A] \"a\" := [A] \"a\";
    ");


// https://github.com/cwi-swat/rascal/issues/465

test bool Issue465a(){									
	writeModule("module MMM
                    lexical IntegerLiteral = [0-9]+;           
					start syntax Exp = con: IntegerLiteral;");
	return checkModuleOK("
        module Issue465a
            import MMM;
            data Exp = con(int n);
        ");
}

test bool Issue465b(){			                                     								
	writeModule("module MMM
                    lexical IntegerLiteral = [0-9]+;           
					start syntax Exp = con: IntegerLiteral;");
	return checkModuleOK("
        module Issue465b
            import MMM;
            data Exp = con(int n);
            void main() { c = con(5); }
        ");
}

test bool Issue465c(){			                                     								
	writeModule("module MMM
                    lexical IntegerLiteral = [0-9]+;           
					start syntax Exp = con: IntegerLiteral;");
	return checkModuleOK("
        module Issue465c
            import MMM;
            data Exp = con(int n);
            void main() { Exp c = con(5); }
        ");
}

test bool Issue465d(){			                                     								
	writeModule("module MMM
                    lexical IntegerLiteral = [0-9]+;           
					start syntax Exp = con: IntegerLiteral;");
	return checkModuleOK("
        module Issue465d
            import MMM;
            data Exp = con(int n);
            void main() { MMM::Exp c = [MMM::Exp] \"3\"; }
        ");
}

// https://github.com/usethesource/rascal/issues/1353
test bool Issue1353() {
   writeModule("module MC
                    syntax A 
                       = \"a\"
                       | left two: A lhs A rhs; 
                     
                       A hello() {
                         A given_a = (A) `a`;
                          return (A) `\<A given_a\> \<A given_a\>`;
                       }");
   return checkModuleOK("
        module Issue1353
            import MC;
            value main() = hello();
    ");               
}