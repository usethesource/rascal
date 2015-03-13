module experiments::Compiler::Examples::Tst1

value main(list[value] args){
	int n = 0; switch(2){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} 
	return n == 2;
}
