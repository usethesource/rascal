module experiments::Compiler::Examples::Tst4

import Boolean;
import List;

// Merge two lists without keeping their order.
public list[&T] mergeUnOrderedX(list[&T] A, list[&T] B) {
   res = [];
   while(!(isEmpty(A) && isEmpty(B))){
            if(arbBool() && size(A) > 0){
               <x, A> = takeOneFrom(A);
               res = res + [x];
            } else if(size(B) > 0){
               <x, B> = takeOneFrom(B);
               res = res + [x];
            };
           }
    return res;
}