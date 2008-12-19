module Bubble-Gen

list[&E] sort(list[&E] Elements, bool (&E, &E) GreaterThan){
  for(int I : [0 .. size(Elements) - 2]){
     if(#GreaterThan(Elements[I],  Elements[I+1])){
       <Elements[I], Elements[I+1]> = <Elements[I+1], Elements[I]>;
       return sort(Elements);
     }
  }
  return Elements;
}