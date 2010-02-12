module Test

import IO;

data NODE2 = f(int N) | g(NODE2 V1, NODE2 V2) | h(NODE2 V1, NODE2 V2) | i(value V);

NODE2 drepl(NODE2 T) { 
  return bottom-up-break visit (T) { 
     case g(NODE2 T1, NODE2 T2) =>  h(T1, T2) 
  };
}

public bool test(){
    //res = drepl(g(1,f([g(2,3),4,5])));
    res = drepl(g(f(1),i([g(f(2),f(3)),f(4),f(5)])));
    println("res = <res>");
    return true;
}