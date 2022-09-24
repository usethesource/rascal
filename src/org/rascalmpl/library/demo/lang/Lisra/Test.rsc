module demo::lang::Lisra::Test

import demo::lang::Lisra::Runtime;
import demo::lang::Lisra::Eval;

test bool eval01() = eval("42").val == Integer(42);
test bool eval02() = eval("x").val == Atom("x");

test bool eval03() = eval("(quote 1)").val == Integer(1);
test bool eval04() = eval("(quote 1 2)").val == List([Integer(1), Integer(2)]);
    
test bool eval05() = eval("(+ 1 2)").val == Integer(3);
test bool eval06() = eval("(- 5 3)").val == Integer(2);
test bool eval07() = eval("(* 5 3)").val == Integer(15);
test bool eval08() = eval("(\< 3 4)").val != FALSE;
test bool eval09() = eval("(\< 3 2)").val == FALSE;
test bool eval10() = eval("(\> 3 2)").val != FALSE;
test bool eval11() = eval("(\>3 4)").val == FALSE;
test bool eval12() = eval("(equal? 3 3)").val != FALSE;
test bool eval13() = eval("(equal? 3 2)").val == FALSE;

test bool eval14() = eval("(null? ())").val != FALSE;
test bool eval15() = eval("(null? (quote 1 2))").val == FALSE;
            
test bool eval16() = eval("(begin (define swap (lambda (a b) (list b a))) (swap 1 2))").val == 
                     List([Integer(2), Integer(1)]);
test bool eval17() = eval("(begin (define * (lambda (a b) (+ a b))) (* 1 2))"). val == Integer(3);

test bool eval18() = eval("(begin (set! x 1) x)").val == Integer(1);
test bool eval19() = eval("(if (\> 5 2) 10 20)").val == Integer(10);
test bool eval20() = eval("(if (\> 2 5) 10 20)").val == Integer(20);

test bool eval21() = eval("(begin (define fac (lambda (n) (if (\> n 1) (* n (fac (- n 1))) 1))) (fac 3))").val == Integer(6);
test bool eval22() = eval("(begin (define length (lambda (x) (if(null? x) 0 (+ 1 (length (cdr x)))))) (length (quote (1 2 3))))").val == Integer(3);
test bool eval23() = eval("(begin (define rev (lambda (x) (if (null? x) () (append (rev (cdr x)) (car x))))) (rev (quote 1 2 3)))").val == List([Integer(3), Integer(2), Integer(1)]);
test bool eval24() = eval("(begin (define F (lambda (x) y)) (set! y 10) (F 1))").val == Integer(10);
