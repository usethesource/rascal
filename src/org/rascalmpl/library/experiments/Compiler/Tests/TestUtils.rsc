module experiments::Compiler::Tests::TestUtils

import  experiments::Compiler::Execute;

value run(str exp, bool listing=false, bool debug=false, bool recompile=false, bool profile=false) = 
	execute("module TMP data D = d1(int n, str s) | d2(str s, bool b) | d3(list[int] l, list[int] r); value main(list[value] args) = <exp>;", [], listing=listing, debug=debug, recompile=recompile, profile=profile);
	
value run(str before, str exp, bool listing=false, bool debug=false, bool recompile=false, bool profile=false) = 
	execute("module TMP data D = d1(int n, str s) | d2(str s, bool b); value main(list[value] args) {<before> ; return <exp>;}", [], listing=listing, debug=debug, recompile=recompile, profile=profile);

value run(str exp, list[str] imports, bool listing=false, bool debug=false, bool recompile=false, bool profile=false) = 
	execute("module TMP <for(im <- imports){>import <im>; <}> data D = d1(int n, str s) | d2(str s, bool b); value main(list[value] args) = <exp>;", [], listing=listing, debug=debug, recompile=recompile, profile=profile);
	
data D = d1(int n, str s) | d2(str s, bool b) | d3(list[int] l, list[int] r);