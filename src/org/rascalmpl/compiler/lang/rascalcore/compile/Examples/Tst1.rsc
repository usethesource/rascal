module lang::rascalcore::compile::Examples::Tst1

//import  lang::rascalcore::compile::Examples::Tst2;
//import  lang::rascalcore::compile::Examples::Tst3;

data D = d(int n);
data D = d(str s);

bool isDint(d(int _)) = true;
default bool isDint(D _) = false;

bool isDstr(d(str _)) = true;
default bool isDstr(D _) = false;

value main() = isDstr(d(13));

//value main() //test bool Cycle1() 
//    = isDint(d(13)) == true;
