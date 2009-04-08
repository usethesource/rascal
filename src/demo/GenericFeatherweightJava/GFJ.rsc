/* This module defines the abstract syntax of Generic Featherweight Java.
 *
 * Caveat Emptor: To be able to easily compare to the paper I have chosen to copy 
 * the identifiers used in there. These are very short and not so meaningful names, 
 * which hampers the understandability of this code and all code that depends on it.
 */
module GFJ

alias f = str; // field name
alias C = str; // class name
alias m = str; // method name
alias x = str; // variable name

data T = \type(X X) | \type(N N); // types are either variables or class literals

// type variable
data X = typevar(C C); 

// class name literal
data N = lit(C C, list[T] Ts); 

// class definition
data L = class(C C, tuple[list[T] Xs,list[T] Ns] XsNs, N N, tuple[list[T] Ts,list[f] fs] Tsfs, K K, list[M] Ms); 

// constructor definition
data K = cons(tuple[list[T] Ts,list[f] fs] Tsfs, S S, list[I] Is); 
  
// call to super
data S = super(list[f] fs); 

// initialize field
data I = this(f f);    

// method definition
data M = method(tuple[list[T] Xs,list[T] Ns] XsNs ,T T, m m, tuple[list[T] Ts,list[x] xs] Tsxs, e e); 

// expressions 
data e = var(x x) 
       | access(e e, f f) 
       | call(e e, m m, list[T] Ts, list[e] es) 
       | new(T T, list[e] es) 
       | cast(T T, e e) 
       | this;