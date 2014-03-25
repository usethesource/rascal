module experiments::Compiler::Examples::Simple

int pad() {
	return 3 ;
}
int fud(int vo) {
	return vo * 2  ;
}

int ocallStress(int j) {
	if ( j == 1 ) return 1;
	return 1 + ocallStress(j-1) ;
}

int fac(int n) = (n <= 1) ? 1 : n * fac(n-1);
int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));

value dain(list[value] args){
   res = 0;
   for(i <- [0,1,2,3,4,5,6,7,8,9])
      res = res + i;
    return res;
}

value main(list[value] args){
 	return ocallStress(10) ; // Kills the jvm version with a stackoverflow.
}

value gain(list[value] args){
 	int j = 0 ;
 	j = fac(8) ;
 	int p = pad() ;
 	while ( j > 0 ) {
 		p = (j * p) +  (j - p) / j ;  
 		j =  j - 1 ;
 	}
	return p ;
}

value fain(list[value] args){	
 	return fib(5) ;
}