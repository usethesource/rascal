module experiments::Compiler::Examples::Tst1

value main(list[value] args) {
   rel[int,int] R = {<1,2>, <2,3>, <3,4>};
   T = R;
   return solve (T) { T = T + (T o R);  };
}
  