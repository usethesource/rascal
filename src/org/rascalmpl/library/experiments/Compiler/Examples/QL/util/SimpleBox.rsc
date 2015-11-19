module experiments::Compiler::Examples::QL::util::Box

data Box
  = h(list[value] xs, int hs)
  | v(list[value] xs, int vs)
  | i(list[value] xs, int vs, int is)
  ;
  
Box H(value xs..., int hs = 0) = h(xs, hs);
Box V(value xs..., int vs = 1) = v(xs, vs);
Box I(value xs..., int vs = 1, int is = 2) = i(xs, vs, is);

str \format(Box b) {
  str out = "";
  void write(str x) {
    out += x;
  }
  eval(b, 0, false, write);
  return out;
}

void eval(h(bs, hs), int ind, bool vert, void(str) write) {
  if (vert) {
    write(spaces(ind));
  }
  first = true;
  for (b <- bs) {
    if (!first) {
      write(spaces(hs));
    }
    evalKid(b, ind, false, write);
    first = false;
  }
}

void eval(v(bs, vs), int ind, bool vert, void(str) write) {
  first = true;
  for (b <- bs) {
    if (!first) {
      write(newlines(vs));
    }
    evalKid(b, ind, true, write);
    first = false;
  }
}

void eval(i(bs, vs, \is), int ind, bool vert, void(str) write) 
  = eval(v(bs, vs), ind + \is, true, write);

str spaces(int n) = ( "" | it + " " | int i <- [0..n] ); 
str newlines(int n) = ( "" | it + "\n" | int i <- [0..n] ); 


void evalKid(value x, int ind, bool vert, void(str) write) {
  if (Box b := x) {
    eval(b, ind, vert, write);
  }
  else {
    if (vert) {
      write(spaces(ind));
    }
    write("<x>");
  }
}
