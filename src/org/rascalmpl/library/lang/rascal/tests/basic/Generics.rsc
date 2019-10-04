@doc{tests specific aspects of generic functions and generic data-types in Rascal}
module lang::rascal::tests::basic::Generics

data Wrapper[&SAME] = something(&SAME wrapped);
alias Graph[&SAME] = rel[&SAME from, &SAME to];

@doc{it matters for testing that '&SAME' is the same name as in the definition of Wrapper}
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
   if (str name() := n) {
     return 0;
   }
   
   // test rebinding the generic type to another unrelated type: 
   assert recursiveGenericFunction(""()) == 0;
   
   if (str name(value arg) := n) {
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