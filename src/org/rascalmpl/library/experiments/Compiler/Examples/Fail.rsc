module experiments::Compiler::Examples::Fail


int f(int n) {
   if(n < 10)
      fail;
   else
      return n * n;
}

value main(list[value] args) { 

	f(1);
}