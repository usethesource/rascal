module demo::lang::Lisra::Test

import demo::lang::Lisra::Runtime;
import demo::lang::Lisra::Eval;

public test bool t01() =  eval("42").val == Integer(42);
public test bool t02() =  eval("x").val == Atom("x");

public test bool t03() =  eval("(quote 1)").val == Integer(1);
public test bool t04() =  eval("(quote 1 2)").val == List([Integer(1), Integer(2)]);
    
public test bool t05() = eval("(+ 1 2)").val == Integer(3);
public test bool t06() = eval("(- 5 3)").val == Integer(2);
public test bool t07() = eval("(* 5 3)").val == Integer(15);
public test bool t08() = eval("(\< 3 4)").val != FALSE;
public test bool t09() = eval("(\< 3 2)").val == FALSE;
public test bool t10() = eval("(\> 3 2)").val != FALSE;
public test bool t11() = eval("(\>3 4)").val == FALSE;
public test bool t12() = eval("(equal? 3 3)").val != FALSE;
public test bool t13() = eval("(equal? 3 2)").val == FALSE;

public test bool t14() = eval("(null? ())").val != FALSE;
public test bool t15() = eval("(null? (quote 1 2))").val == FALSE;
            
public test bool t16() = eval("(begin (define swap (lambda (a b) (list b a))) (swap 1 2))").val == List([Integer(2), Integer(1)]);
public test bool t17() = eval("(begin (define * (lambda (a b) (+ a b))) (* 1 2))"). val == Integer(3);

public test bool t18() = eval("(begin (set! x 1) x)").val == Integer(1);
public test bool t19() = eval("(if (\> 5 2) 10 20)").val == Integer(10);
public test bool t20() = eval("(if (\> 2 5) 10 20)").val == Integer(20);

public test bool t21() = eval("(begin (define fac (lambda (n) (if (\> n 1) (* n (fac (- n 1))) 1))) (fac 3))").val == Integer(6);
public test bool t22() = eval("(begin (define length (lambda (x) (if(null? x) 0 (+ 1 (length (cdr x)))))) (length (quote (1 2 3))))").val == Integer(3);
public test bool t23() = eval("(begin (define rev (lambda (x) (if (null? x) () (append (rev (cdr x)) (car x))))) (rev (quote 1 2 3)))").val == List([Integer(3), Integer(2), Integer(1)]);
public test bool t24() = eval("(begin (define F (lambda (x) y)) (set! y 10) (F 1))").val == Integer(10);