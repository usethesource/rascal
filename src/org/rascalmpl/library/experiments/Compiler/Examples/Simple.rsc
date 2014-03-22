module experiments::Compiler::Examples::Simple

int pad() {
	return 3 ;
}
int fud(int vo) {
	return vo * 2  ;
}

int fac(int n) = (n <= 1) ? 1 : n * fac(n-1);
int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));

value main(list[value] args){
 	//int j = fac(4) ;
 	//int p = pad() ;
 	//if ( j == 5 ) return p * j ;
 	return fib(30) ;
}