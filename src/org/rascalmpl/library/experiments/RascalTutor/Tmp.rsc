module experiments::RascalTutor::Tmp
import IO;

public str check(int i){
  throw "no";
}

public list[int] f(){
  println("Enter f");
  res = [];
  for(int i <- [0 .. 10]){
     try {
         println("Inside f, entering try");
         res += check(i);
     } catch: {
        println("Inside f, catch");
        res += -1;
     }
  }   
      
  return res;
}