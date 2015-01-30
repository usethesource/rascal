module experiments::Compiler::Examples::PerfIndexOf


import List;
import util::Benchmark;
import IO;

public int indexOf_Pavol(list[&T] lst, &T elt) {
    if (elt in lst){
        s=size(lst);
        if (s==1){
            return 0;
        }
        else{
            fst=indexOf_Pavol(lst[..s/2],elt);
            if (fst!=-1){
                return fst;
            }
            else {
                return (s/2)+indexOf_Pavol(lst[s/2..],elt);
            }
        }
    }
    else{
        return -1;
    }
}

int indexOf_Davy(list[&T] lst, &T elt) {
  
  n = size(lst);
  i = 0;
  while (i < n) {
    if (lst[i] == elt) return i;
    i += 1;
  }
  return -1;
}

int indexOf_Davy2(list[&T] lst, &T elt) {
 
  i = 0;
  for (e <- lst) {
    if (e == elt) return i;
    i += 1;
  }
  return -1;
}


int indexOf_Jurgen([*(&T) pre, &T elt, *(&T) _], elt) = size(pre);
default int indexof_Jurgen(list[&T] lst, &T elt) = -1;

//@javaClass{IndexOffTest}
//java int indexOf_Java(list[&T] lst, &T elt);

void main(list[value] args) {
    dt = [1..10];
    
    
    //benchFun = void() (int (list[&T], &T) func) {
    //    return void() {
    //        for (d <- dt) {
    //            func(dt, d);
    //        }
    //    };
    //};
	
  
    actual = ();
//    int now = realTime();
//    
//	for (d <- dt) {
//    	indexOf(dt, d);
//    }
//    actual["Original"] = realTime() - now;
//    
//    now = realTime();
//    
//    for (d <- dt) {
//    	indexOf_Pavol(dt, d);
//    }
//    
//    actual["Pavol"] = realTime() - now;
//    now = realTime();
//
//    for (d <- dt) {
//    	indexOf_Davy(dt, d);
//    }
//    
//    actual["Davy"] = realTime() - now;
//    now = realTime();
//    
//    for (d <- dt) {
//    	indexOf_Davy2(dt, d);
//    }
//    
//    actual["Davy2"] = realTime() - now;
    now = realTime();
    
    for (d <- dt) {
    	indexOf_Jurgen(dt, d);
    }
    
    actual["Jurgen"] = realTime() - now;
    now = realTime();
    
//    functions = (
//        "Original" : benchFun(indexOf),
//        "Pavol": benchFun(indexOf_Pavol), 
//        //"Jurgen":benchFun(indexOf_Jurgen), 
//        "Davy": benchFun(indexOf_Davy), 
//        "Davy2": benchFun(indexOf_Davy2)
//        //"Java" : benchFun(indexOf_Java)
//    );
//
//    warmup = benchmark(functions);
//    actual = benchmark(functions);
    for (n <- actual) {
        println("<n>: <actual[n]>ms");
    }
    return;
}