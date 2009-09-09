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


public void main() {
    test();
}
 
 public bool test(){
     A t = f(g(g(b())),g(g(b())));
     assertEqual(top_down(rules)(t), f(g(b()),g(b())));
     assertEqual(bottom_up(rules)(t), f(b(),b()));
     assertEqual(innermost(rules)(t), f(b(),b()));
     assertEqual(outermost(rules)(t), f(b(),b()));
     return report("Strategies");
}