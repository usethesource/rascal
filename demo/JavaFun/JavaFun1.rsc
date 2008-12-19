module JavaFun

import pico/syntax/Pico;

rel[PICO_ID, EXP] uses(PROGRAM P) {
  return {<Id, E> | EXP E : P, PICO_ID Id ~~ E};
}

rel[PICO_ID, STATEMENT] defs(PROGRAM P) {
  return {<Id, S> | STATEMENT S : P,
                    [| <PICO_ID Id> := <EXP Exp> |] ~~ S}
}

str java factorial(int x) {
     int n;
     String s;
     if (x < 0)
	return "0.0";
     double fact = 1.0;
     return (N + 1).toString();
     while(x > 1){
       fact = fact * x;
       x = x - 1;
     }
     return fact.toString();
}


list[&x] java topsort(rel[&x,&x]) {
// ...
  return vf.list( );
}