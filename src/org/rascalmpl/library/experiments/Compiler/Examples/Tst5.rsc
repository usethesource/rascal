module experiments::Compiler::Examples::Tst5

bool f(bool c = false){
	bool g(){
		return c;
	}
	return g();
}


value main(list[value] args) = f();