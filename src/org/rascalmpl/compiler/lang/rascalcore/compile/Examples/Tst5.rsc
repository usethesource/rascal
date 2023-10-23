module lang::rascalcore::compile::Examples::Tst5

data B = and(B lhs, B rhs) | or(B lhs, B rhs) | t() | f();


value main(){ //test bool curryAConstructor() {
    &S(&U) c(&S(&T, &U) f, &T t) = &S (&U u) { 
      return f(t, u); 
    };

    B (B) f = c(and, t());

    return f(t()) == and(t(), t());
}

//------------------------------------------


//import util::Math;
//
//list[&T <: num] abs(list[&T <: num] nums) 
//    = [abs(n) | n <- nums]; 
//
//(&T<:num) assureRange(&T <: num n, num low, num high) {
//    ab = abs(n);
//    if (ab >= low && ab <= high) {
//        return n;   
//    }
//    if (ab <= high) {
//        if (n < 0) {
//            return n - low;
//        }
//        return n + low; 
//    }
//    return makeSmallerThan(n, toInt(high));
//}
//
////list[&T <: num] assureRange(list[&T <: num] nums, num low, num high)
////    = [ assureRange(n, low, high) | n <- nums];
//    
//int makeSmallerThanInt(int n, int limit) = n % limit;
//real makeSmallerThanReal(real n, int limit) {
//    if (abs(n) < limit) {
//        return n;
//    }
//    f = toInt(n);
//    r = n - f;
//    return (f % limit) + r;
//}
//rat makeSmallerThanRat(rat n, int limit) {
//    if (abs(n) < limit) {
//        return n;
//    }
//    return toRat(1, denominator(n));
//}
//
//&T <: num makeSmallerThan(&T <: num n, int limit) {
//    if (int i := n) {
//        return makeSmallerThanInt(i, limit);    
//    }
//    if (real r := n) {
//        return makeSmallerThanReal(r, limit);   
//    }
//    if (rat r := n) {
//        return makeSmallerThanRat(r, limit);    
//    }
//    throw "Forgot about a different number type <n>";
//}
//
//list[&T <: num] makeSmallerThan(list[&T <: num] nums, int limit) 
//    = [ makeSmallerThan(n, limit) | n <- nums];
//
//
//value main() = assureRange(0, 0.1, 30);

//------------------------------------------

//&T avoidEmpty(list[&T] _) { return 1; }
//&T avoidEmpty(list[&T] _) { throw "this should happen"; }
//
//value main(){ //test bool voidReturnIsNotAllowed() {
//   try {
//     return avoidEmpty([]); 
//   } catch "this should happen":
//     return true;
//}
