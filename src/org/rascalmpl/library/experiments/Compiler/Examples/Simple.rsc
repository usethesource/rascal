module experiments::Compiler::Examples::Simple

int pad() {
	return 3 ;
}


int fud() {
	return 5 ;
}

value main(list[value] args){
 	int j = fud() ;
 	int p = pad() ;
 	if ( j == 5 ) return p * j ;
 	return false;
}