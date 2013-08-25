module experiments::Compiler::Tests::Types

import  experiments::Compiler::Compile;

//import lang::rascal::\syntax::Rascal;

import ParseTree;
import IO;

layout LAYOUT = [\ \n\t]*; 
syntax D2 = "d";

data D1 = d1(int \int) | d2(str \str);


value run1(str \type) = execute("module TMP data D1 = d1(int \\int) | d2(str \\str); value main(list[value] args) = #<\type>;");
value run2() = execute("module TMP layout LAYOUT = [\\ \\n\\t]*; syntax D2 = \"d\"; value main(list[value] args) = #D2;");
//value run3() = execute("module TMP import lang::rascal::\\syntax::Rascal; value main(list[value] args) = #Module;");

test bool tst1() = run1("list[int]") == #list[int];
test bool tst2() = run1("lrel[int id]") == #lrel[int id];
test bool tst3() = run1("lrel[int \\int]") == #lrel[int \int];
test bool tst4() = run1("D1") == #D1;
test bool tst5() = run2() == #D2;
//test bool tst6() = run3() == #Module;
