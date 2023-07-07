@synopsis{tests specific aspects of generic functions and generic data-types in Rascal}
module lang::rascal::tests::basic::Generics

import Exception;
import util::Maybe;

data Wrapper[&SAME] = something(&SAME wrapped);
alias Graph[&SAME] = rel[&SAME from, &SAME to];

@synopsis{it matters for testing that '&SAME' is the same name as in the definition of Wrapper}
&SAME getIt(Wrapper[&SAME] x) = x.wrapped;

test bool hygienicGenericADT() {
  // in the same context, we bind the same type parameter in
  // different ways to see if nothing is leaking.
  int i = something(1).wrapped;
  int j = getIt(something(2));
  int k = getIt(something(3));
  str x = something("x").wrapped;
  str y = getIt(something("y"));
  
  return i == 1 && j == 2 && k == 3
      && x == "x" && y == "y";
}

int recursiveGenericFunction(&T n) {
   if (str _() := n) {
     return 0;
   }
   
   // test rebinding the generic type to another unrelated type: 
   assert recursiveGenericFunction(""()) == 0;
   
   if (str _(value arg) := n) {
     // call a recursive function, if the type resolution is not hygienic,
     // then &T would be bound to itself or to a caller instance type
     // which might lead to infinite instantiation cycles in vallang
     // if not corrected for.
     return 1 + recursiveGenericFunction(arg);
   }
   
   return 1;
}

test bool genericFunction1() = recursiveGenericFunction("aap"("noot")) == 2;
test bool genericFunction2() = recursiveGenericFunction("aap"("noot"("mies"))) == 3;

bool less(&T a, &T b) = a < b;

test bool lessIsConsistentThroughTypeParameters(num x, num y) = (x < y) ==> less(x, y);

&T avoidEmpty(list[&T] _) { return 1; }
&T avoidEmpty(list[&T] _) { throw "this should happen"; }

test bool voidReturnIsNotAllowed() {
   try {
     return avoidEmpty([]); 
   } catch "this should happen":
     return true;
}

&T cast(type[&T] _, value x) = y when &T y := x;

test bool typeParametersAreCheckedStaticallyButAlsoBoundDynamically() {
   // statically type[num] but dynamically type[int]
   type[num] t = #int;
   num r = 1r2;
   
   try {
     // we can only guarantee cast will return `num`
     // but it should still fail because r is not an int
     // the run-time does not know the static types of all expressions
     // passed to a generic function, so all it has are the dynamic
     // types then. Even for reified types this is the case, due
     // to the co-variance of the type[_] type.
     num n = cast(t, r); 
     return false;
   }
   catch CallFailed(_):
     return true;
}

// the filter functies guarantees statically all elements will be sub-type of the static instance of &T at the call
// site, by making sure to use the _dynamic_ type of the reified type value t during pattern matching! The compiler
// does not require to maintain information about static types at run-time because of this.
list[&T] \filter(type[&T] _, list[value] elems) = [e | &T e <- elems];

test bool typeParametersAreCheckedStaticallyButAlsoBoundDynamically2() 
  = [1,2,3] == \filter(#int, [1, "1", 1r, 2, "2", 1r2, 3, "3", 1r3]);
  
test bool typeParametersAreCheckedStaticallyButAlsoBoundDynamically2_2() 
  = [1,2,3] == \filter(t, [1, "1", 1r, 2, "2", 1r2, 3, "3", 1r3]) when type[num] t := #int; 
  
test bool typeParametersAreCheckedStaticallyButAlsoBoundDynamically3() 
  = [1,1r,2,1r2,3,1r3] == \filter(#num, [1, "1", 1r, 2, "2", 1r2, 3, "3", 1r3]); 
  
test bool typeParametersAreCheckedStaticallyButAlsoBoundDynamically3_2() 
  = [1,1r,2,1r2,3,1r3] == \filter(t, [1, "1", 1r, 2, "2", 1r2, 3, "3", 1r3]) when type[value] t := #num; 
  
test bool typeParametersAreCheckedStaticallyButAlsoBoundDynamically3_3() 
  = [1,"1",1r,2,"2",1r2,3,"3",1r3] == \filter(t, [1, "1", 1r, 2, "2", 1r2, 3, "3", 1r3]) when type[value] t := #value;   
  
test bool staticTypeParametersKeepElementLabelsAlsoWithListMatch() {
   &T first([&T head, *&T _]) = head;
   
   lrel[int first, int second] myList = [<1,2>,<2,3>];
   
   myElem = first(myList);
   
   return myElem.first == 1 && myElem.second == 2;
}  

test bool staticTypeParametersKeepElementLabelsAlsoWithSetMatch() {
   &T take({&T some, *&T _}) = some;
   
   rel[int first, int second] mySet = {<1,2>,<2,3>};
   
   myElem = take(mySet);
   
   return myElem.first == 1 && myElem.second == 2;
}  

test bool recursiveOverloadedGenericFunction() {
   str f(int i) = "<i>";
   str f(map[&K, &V] m) = "(<for (k <- m) {><f(k)>:<f(m[k])>, <}>)";
   str f(list[&E] l) = "[<for (e <- l) {><f(e)>, <}>]";
   
   return f((1:(1:2))) == "(1:(1:2, ), )";
}


test bool voidMaybeShouldShouldThrowException() {
   &T testFunction(Maybe[&T] m) = m.val; 
    
   try {
      Maybe[value] m = nothing();
      value x = testFunction(m);
      return x != 123; // this comparison never happens
   }
   catch NoSuchField(_) :
     return true;
}

test bool voidListsShouldThrowException() {
  tuple[&T, list[&T]] headTail(list[&T] l) {
     if ([&T h, *&T t] := l) {
       return <h, t>;
     }
     
     fail;
  }
  
  try {
      list[value] m = [];
      tuple[value,list[value]] x = headTail(m);
      return x != <123,[456]>; // this comparison never happens
   }
   catch CallFailed([[]]) :
     return true;
}
