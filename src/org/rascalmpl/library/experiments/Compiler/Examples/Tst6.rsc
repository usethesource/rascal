module experiments::Compiler::Examples::Tst6

value main(list[value] args){

	int fac(int n) {
		switch(n){
			case 0: 
				return 1;
			default:
				return n * fac(n - 1);
		}
	
	}
	
	return fac(5);

}
