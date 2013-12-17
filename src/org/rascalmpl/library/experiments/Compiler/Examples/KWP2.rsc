module experiments::Compiler::Examples::KWP2

value main(list[value] args) { 
  	int incr(int x, int delta = 1) = x + delta;
  	return incr(3) == 4 && incr(3, delta = 2) == 5;
}