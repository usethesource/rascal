module experiments::Compiler::Tests::Types

import  experiments::Compiler::Compile;

import ParseTree;
import IO;

data D = d1(int \int) | d2(str \str);

value run1(str \type) = execute("module TMP data D = d1(int \\int) | d2(str \\str); value main(list[value] args) = #<\type>;");
value run2() = execute("module TMP syntax D = \"d\"; value main(list[value] args) = #D;");

test bool tst1() = run1("list[int]") == #list[int];
test bool tst2() = run1("lrel[int id]") == #lrel[int id];
test bool tst3() = run1("lrel[int \\int]") == #lrel[int \int];
//test bool tst3() = run1("D") == type();
