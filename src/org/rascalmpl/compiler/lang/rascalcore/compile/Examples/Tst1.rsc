module lang::rascalcore::compile::Examples::Tst1

//import  lang::rascalcore::compile::Examples::Tst2;
//import  lang::rascalcore::compile::Examples::Tst3;

data D = d(int n);
data D = d(str s);
//
//bool isDint(d(int _)) = true;
//default bool isDint(D _) = false;
//
//bool isDstr(d(str _)) = true;
//default bool isDstr(D _) = false;

value main() = (d(str _) := d(13)) == false;

//data F = 
//         fff(str s, int n)
//       | fff(int n, str s)
//       ;
//       
// int getN1(fff(str s, n)) = n;
 
 //int getN2(fff(n, str s)) = n;
// 
// test bool overloadedCons1(str s, int n) = getN1(fff(s, n)) == n;

//value main() = getN1(fff("abc", 123)) == 123;