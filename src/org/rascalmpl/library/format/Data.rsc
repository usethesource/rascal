module \format::Data

extend \format::Explode;

default format(node x, type[&T<:Tree] grammar = #()) {
   try {
     return "~(explode(x, grammar))";
   }
   catch value _ : fail;
}
     
