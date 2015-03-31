
module experiments::Compiler::Examples::Tst2

data TREE = i(int N) | f(TREE a,TREE b) | g(TREE a, TREE b);
  	
// nodeGenerator()
  		
//test bool nodeGenerator1() = [ X | /int X <- f(i(1),g(i(2),i(3))) ] == [1,2,3];
//  		
//test bool nodeGenerator2() = [ X | /value X <- f(i(1),g(i(2),i(3))) ] == [1,i(1),2,i(2),3,i(3),g(i(2),i(3))];
//test bool nodeGenerator3() = [ X | value X <- f(i(1),g(i(2),i(3))) ] == [i(1),g(i(2),i(3))];
//  
//test bool nodeGenerator4() = [N | /value N <- f(i(1),i(2))] == [1,i(1),2,i(2)];
//test bool nodeGenerator5() = [N | value N <- f(i(1),i(2))] == [i(1), i(2)];
//  		
//test bool nodeGenerator6() = [N | /TREE N <- f(i(1),i(2))] == [i(1),i(2)];
//test bool nodeGenerator7() = [N | TREE N <- f(i(1),i(2))] == [i(1),i(2)];
//  		
//test bool nodeGenerator8() = [N | /int N <- f(i(1),i(2))] == [1,2];
//  		
//test bool nodeGenerator9() = [N | /value N <- f(i(1),g(i(2),i(3)))] == [1,i(1),2,i(2),3,i(3),g(i(2),i(3))];
//test bool nodeGenerator10() = [N | value N <- f(i(1),g(i(2),i(3)))] == [i(1),g(i(2),i(3))];
//  		
//test bool nodeGenerator11() = [N | /TREE N <- f(i(1),g(i(2),i(3)))] == [i(1),i(2),i(3),g(i(2),i(3))];
//test bool nodeGenerator12() = [N | TREE N <- f(i(1),g(i(2),i(3)))] == [i(1),g(i(2),i(3))];
  		
value main(list[value] args) = [N | /int N <- f(i(1),g(i(2),i(3)))] == [1,2,3];