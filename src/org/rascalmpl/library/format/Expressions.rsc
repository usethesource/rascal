module \format::Expressions

extend \format::Comments;
import ParseTree;

// brackets
default format(appl(prod(Symbol E1, [lit(str _), layouts(str _), Symbol E2, layouts(str _), lit(str _)], set[Attr] _),
                    [Tree bo, Tree l1, Tree e, Tree l2, Tree bc]))
  = "~~bo~l1~e~l2~~bc" 
  when delabel(E1) == delabel(E2);

// binary operator  
default format(appl(prod(Symbol E1, [Symbol E2, layouts(str _), lit(str _), layouts(str _), Symbol E3], set[Attr] _),
                    [Tree lr, Tree l1, Tree op, Tree l2, Tree rr]))
  = "~ll ~l1~~op~l2 ~lr" 
  when delabel(E1) == delabel(E2) && delabel(E2) == delabel(E3);

// prefix
default format(appl(prod(Symbol E1, [lit(str _), layouts(str _), Symbol E2], set[Attr] _),
                    [Tree op, Tree l, Tree rr]))
  = "~~op~l~rr" 
  when delabel(E1) == delabel(E2);
  
// complex prefix
default format(appl(prod(Symbol E1, [lit(str _), *Symbol _, lit(str _), layouts(str _), Symbol E2], set[Attr] _),
                    [Tree bo, *Tree m, Tree bc, Tree l, Tree rr]))
  = "~~bo~(e | e <- m)~~bc~rr" 
  when delabel(E1) == delabel(E2);
  
// postfix
default format(appl(prod(Symbol E1, [Symbol E2, layouts(str _), lit(str _)], set[Attr] _),
                    [Tree lr, Tree l, Tree op]))
  = "~lr~~op" 
  when delabel(E1) == delabel(E2);
  
// complex postfix
default format(appl(prod(Symbol E1, [Symbol E2, layouts(str _), lit(str _), *Symbol _, lit(str _)], set[Attr] _),
                    [Tree lr, Tree l, Tree bo, *Tree m, Tree bc]))
  = "~lr~~bo~(e | e <- m)~bc" 
  when delabel(E1) == delabel(E2);  

private Symbol delabel(Symbol t) = label(str _, x) := t ? x : t;

