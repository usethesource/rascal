module demo::basic::Cursors

import util::Cursor;
import IO;


data Todo
  = todo(int key, bool done, str txt);
  
alias Todos = list[Todo];

bool markAsDone(int k, set[Todo] todos) {
  if (t <- todos, t.key == k) {
    return update(t.done, true);
  }
}

void todoExample() {
 todos = {todo(1, false, "reviewing"),
          todo(2, false, "email"),
          todo(3, false, "grading")};
          
 c = makeCursor(todos);
 b = markAsDone(2, c);
 newTodos = getRoot(#set[Todo], b);
 iprintln(newTodos);  
}


void mapping() {
  int twice(int x) = 2 * x;
  int half(int x) = x / 2;
  
  list[int] view(list[int] xs) = [ compute(x, twice, half) | x <- xs ];
  
  model = [1,2,3];
  curs = makeCursor(model);
  
  v = view(curs);
  
  iprintln(v);
  
  x = update(v[1], 6);
  
  
  iprintln(getRoot(#list[int], x));
}

void cfExample() {
  real c2f(real c) = c * (9.0/5.0) + 32.0;
  real f2c(real f) = (f - 32.0) * (5.0/9.0);
  
  tuple[real,real] view(real c) = <c, compute(c, c2f, f2c)>;

  
  model = 18.0;
  curs = makeCursor(model);
  
  v = view(curs);
  println("view = <v>");
  fahr = v[1]; 
  fahr = update(fahr, 100.0);
  
  model2 = getRoot(#real, fahr);
  println("model after update fahr to 100.0 = <model2>");
  println("redisplay: <view(model2)>");
  
  cels = v[0];
  cels = update(cels, 30.0);
  model3 = getRoot(#real, cels);
  println("model after update cels to 30.0 = <model3>");
  
  println("redisplay: <view(model3)>");
  
  
}

void tuples() {
  tuple[str first, str last, int age] p = <"Piet", "hein", 35>;
  
  c = makeCursor(p);
  
  lastName = c<first,last>.last;
  println("Lastname = <lastName>");
  println(toPath(lastName));
  
  lastName = update(lastName, "Hein");
  println("Lastname := Hein = <lastName>");
  
  iprintln(getRoot(#tuple[str,str,int], lastName));

}


data Exp = add(Exp l, Exp r) | lit(int n);

void main() {
  e = add(lit(1), lit(2));
  
  Exp ec = makeCursor(e);
  
  println("Cursor = <ec>");
  
  // Navigate to 2
  two = ec.r.n;
  
  println("Path = <toPath(two)>");
  
  println("two = <two>");
  
  // Update 2 to 10
  two = update(two, 10);
  println("two updated: <two>");
  
  // Get the updated expression
  println("exp = <getRoot(#Exp, two)>");

}