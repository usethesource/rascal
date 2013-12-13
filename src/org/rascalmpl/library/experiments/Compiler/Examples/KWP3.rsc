module experiments::Compiler::Examples::KWP3

value main(list[value] args) { 
	int sum(int x = 0, int y = 0) = x + y;
  	return sum() == 0 
  	       && sum(x = 5, y = 7) == 5 + 7 
  	       && sum(y = 7, x = 5) == 5 + 7;
}