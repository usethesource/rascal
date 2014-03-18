module experiments::Compiler::Examples::Simple

value main(list[value] args){
 	int j = 2 ;
 	while ( j > 0 ) {
 		j = j - 1 ;
 	}
    return j ;
}