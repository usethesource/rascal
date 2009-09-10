module TestStrategy
	
import IO;
import Strategy;
import UnitTest;

data A = a()
       | f(B I, B J);
       
data B = g(B I)
       | b()
       | c();

&T(&T) rules = &T(&T t) {
    switch (t) {
	case g(b()): return b();
	default: return t;
    };
 };

&T(&T) rules2 = &T(&T t) {
    switch (t) {
	case c(): return b();
	case g(c()): return c();
	default: return t;
    };
 };

&T(&T) rules3 = &T(&T t) {
    switch (t) {
	case b(): return c();
	default: return t;
    };
 };


public B rules4(B t) {
   switch (t) {
	case b(): return c();
	default: return t;
   };
}

public void main() {
    test();
}
 
public bool test() {
     A t = f(g(g(b())),g(g(b())));
     assertEqual(top_down(rules)(t), f(g(b()),g(b())));
     assertEqual(bottom_up(rules)(t), f(b(),b()));
     assertEqual(innermost(rules)(t), f(b(),b()));
     assertEqual(outermost(rules)(t), f(b(),b()));

     B t2 = g(c());
     assertEqual(once_top_down(rules2)(t2), c());
     assertEqual(once_bottom_up(rules2)(t2), g(b()));
 
     list[B] l = [g(c()),c()];
     assertEqual(makeAll(rules2)(l),[c(),b()]);
 
     tuple[A,B] t3 = <a(),c()>;
     assertEqual(top_down(rules2)(t3),<a(),b()>);

     rel[A, B] r = {<a(), b()>, <f(b(),b()), c()>};
     assertEqual(top_down(rules3)(r),{<a(), c()>, <f(c(),c()), c()>});

     A t4 = f(g(b()),g(b()));
     assertEqual(top_down(makeStrategy(rules4))(t4),f(g(c()),g(c())));
 
     return report("Strategies");

}
