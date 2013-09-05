module experiments::Compiler::Examples::Tmp

import Number;
//test bool tst1() = true == true;
//
//test bool tst2(int n) = n + n - n == n;
//
//test bool tst3(list[&T] lst, set[&T] st) = true;

//data D = d1(int n) | d2(str s);
//data E = e1(int n) | e2(str s);
//
//test bool tst4(list[D] ds) = true;
alias TUP = tuple[int n, str s];

value main(list[value] args){
    //L = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
    //L[2 .. 4] = [10];
    list[TUP] tup = [<3,"a">];
    return tup has m;
}