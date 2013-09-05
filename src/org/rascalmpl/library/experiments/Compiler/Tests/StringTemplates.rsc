module experiments::Compiler::Tests::StringTemplates

import  experiments::Compiler::Compile;

value run(str exp, bool listing=false, bool debug=false) = 
	execute("module TMP value main(list[value] args) = <exp>;", listing=listing, debug=debug);
	
value run(str before, str exp, bool listing=false, bool debug=false) = 
	execute("module TMP value main(list[value] args) {<before> ; return <exp>;}", listing=listing, debug=debug);
	


test bool tst() = run("\"ab\"") == "ab";
test bool tst() = run("\"a\<12345\>b\"") == "a<12345>b";
test bool tst() = run("\"a\<5 + 7\>b\"") == "a<5 + 7>b";
test bool tst() = run("{x = 100; \"a\<x + 7\>b\";}") == {x = 100; "a<x + 7>b";};
