module experiments::Compiler::Examples::Tst1

 data F1 = f1(int N, int M = 10, bool B = false) | f1(str S);
  		
test bool matchADTwithKeywords1() = f1(1)                   := f1(1);
test bool matchADTwithKeywords2() = f1(1, M=10)             := f1(1);
test bool matchADTwithKeywords3() = f1(1, B=false, M=10)    := f1(1);
test bool matchADTwithKeywords4() = f1(1, M=20)             := f1(1, B=false, M=20);


value main(list[value] args) = f1(1, M=10)             := f1(1);


//import ParseTree;
//
//layout Whitespace = [\ \t\n]*;
//
//start syntax D = "d";
//start syntax DS = D+;
//
//
//value main(list[value] args) { if( (DS)`d <D+ Xs>` := (DS)`d d`) return (DS)`d <D+ Xs>` == (DS)`d d`; }
