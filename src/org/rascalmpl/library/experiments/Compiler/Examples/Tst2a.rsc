module experiments::Compiler::Examples::Tst2a

import List;

bool less(int a, int b) = a < b;
bool lesseq(int a, int b) = a <= b;
bool greater(int a, int b) = a > b;
bool greatereq(int a, int b) = a  >= b;

//value main(list[value] args)  = sort([1,2,3], bool(int a, int b){return a < b;}) ;
value main(list[value] args)  = sort([1,2,3], less) ;

test bool sort2g() = sort([1,2,3], less) == [1,2,3];
test bool sort2h() = sort([1,3,2], less) == [1,2,3];
test bool sort2i() = sort([1,3,2], greater) == [3,2,1];
test bool sort2j() = sort([3,2,1], greater) == [3,2,1];
@expected{IllegalArgument} test bool sort2k() {sort([1,2,3], lesseq); return false;}
@expected{IllegalArgument} test bool sort2l() {sort([1,2,3], greatereq); return false;}


