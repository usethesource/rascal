module \format::Tree

default format(appl(Production prod, list[Tree] args)) 
  = "~(t | t <- args)";
  
default format(amb({Tree a, *Tree _})) = format(a);

default format(Tree t:char(int _)) = "~~t"; 