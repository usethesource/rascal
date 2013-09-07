module experiments::Compiler::Tests::TestUtils

import  experiments::Compiler::Compile;

value run(str exp, bool listing=false, bool debug=false) = 
	execute("module TMP data D = d1(int n, str s) | d2(str s, bool b); value main(list[value] args) = <exp>;", listing=listing, debug=debug);
	
value run(str before, str exp, bool listing=false, bool debug=false) = 
	execute("module TMP data D = d1(int n, str s) | d2(str s, bool b); value main(list[value] args) {<before> ; return <exp>;}", listing=listing, debug=debug);
	
data D = d1(int n, str s) | d2(str s, bool b);