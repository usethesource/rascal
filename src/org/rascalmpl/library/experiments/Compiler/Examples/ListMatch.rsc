module experiments::Compiler::Examples::ListMatch

//void work(int n){
//
//  while(n > 0){
//    res = [];
// 	for([*int a, *int b, *int c, *int d] := [1,2,3,4,5,6,7,8,9]) { res = res + [a,b,c,d]; }
// 	n = n - 1;
//  }
//  return;
//}

value main(list[value] args) { 
	res = [];
 	for([*int a, *int b, *int c, *int d] := [1,2,3,4,5,6,7,8,9]) { res = res + [[a,b,c,d]]; }
    return res;
   //return [*int x, 3, 4] := [2]; 
   
}