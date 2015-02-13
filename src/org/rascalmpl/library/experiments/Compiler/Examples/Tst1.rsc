module experiments::Compiler::Examples::Tst1
	
	
data A = a(int n) | a (int n, int m);

data B = b(str s) | b(str s, str t);

data C = c(bool b) | c(A ca, B cB) | c(int n, C cc);


C cnt(C t) {
	return 
	  top-down visit(t) {
		case true => false
	}
}	


value main(list[value] args) = cnt(c(true));


//visit("abcabca"){ case "a": insert "AA"; case /b/: insert "BB";} ; //== "aBBcaBBcAA";