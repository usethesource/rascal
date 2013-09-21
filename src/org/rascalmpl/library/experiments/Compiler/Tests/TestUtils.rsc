module experiments::Compiler::Tests::TestUtils

import  experiments::Compiler::Execute;

value run(str exp, bool listing=false, bool debug=false, bool recompile=false) = 
	execute("module TMP data D = d1(int n, str s) | d2(str s, bool b); value main(list[value] args) = <exp>;", [], listing=listing, debug=debug, recompile=recompile);
	
value run(str before, str exp, bool listing=false, bool debug=false, bool recompile=false) = 
	execute("module TMP data D = d1(int n, str s) | d2(str s, bool b); value main(list[value] args) {<before> ; return <exp>;}", [], listing=listing, debug=debug, recompile=recompile);
	
data D = d1(int n, str s) | d2(str s, bool b);