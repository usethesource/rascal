module lang::rascalcore::compile::Examples::Tst1
                
bool main() = !([1, list[int] L, 2, list[int] L] := [1,2,3]); 

 //int f(int n) { return n += 1; }      
          
//data D = d1(int n) | d2(int n, int m);
//
//set[int] f(list[D] ds)
//    = { n | d <- ds, d1(n) := d || d2(m, _) := d };
    

//set[int] f(list[int] ns)
//    = { n | x <- ns, n := 3 || m := 4};
    
//set[int] g(set[set[int]] ns)
//    = { *x | st <- ns, set[int] X:{1, *x} := st || {2, *y} := st };
    
//    
//test bool in1(map[&K,&V] M) = isEmpty(M) || all(&K k <- M, k in M);