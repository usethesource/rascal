module experiments::Compiler::Examples::Simple

//int pad() {
//	return 3 ;
//}
//int fud(int vo) {
//	return vo * 2  ;
//}

//int ocallStress(int j) {
//	if ( j == 0 ) return 1 ;
//	return 1 + ocallStress(j-1) ;
//}

// int fac(int n) = (n <= 1) ? 1 : n * fac(n-1);
int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));

value main(list[value] args){
 	//int j = 0 ;
 	////
 	//j = fac(7) ;
 	//int p = pad() ;
 	////if ( j == 5 ) return p * j ;
 	//while ( j > 0 ) {
 	//	p = (j * p) +  ( j - p ) / j ;  
 	//	j =  j - 1 ;
 	//}
 	////for ( int i <- [1..10] ) {
 	////	j = j + i ;
 	////}
 	//
 	//return p ;
 	//
 	//
 	return fib(30) ;
 	//return ocallStress(999999) ; // Kills the jvm version with a stackoverflow.
}