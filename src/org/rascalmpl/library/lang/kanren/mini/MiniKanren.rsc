module lang::kanren::mini::MiniKanren

import List;
import Map;
import IO;
import Time;
import Node;

// TODO: make ADT to manage goals, continuations etc.
// TODO: unify/walk etc. open for extension

data Var
 = var(int id)
 ;

data Subst
 = subst(map[Var var, value \value] bindings)
 | nil()
 ;


Subst unify(value u, value v, Subst s) {
  //println("Unifying u = <u> and v = <v>; s = <s>");
  u = walk(u, s);
  v = walk(v, s);
  
  if (u == v) {
    //println("u == v");
    return s;
  }
  else if (Var u1:var(_) := u) {
    if (Var v1:var(_) := v) {
      //println("Two vars");
      return extS(u1, v1, s);
    }
    else {
      //println("U is a var");
      return extSCheck(u1, v, s);
    }
  }
  else if (Var v1:var(_) := v) {
    //println("v is a var");
    return extSCheck(v1, u, s);
  }
  else if (<value a, value b> := u, <value c, value d> := v) {
    // only binary tuples for now.
    s = unify(a, c, s);
    if (nil() := s) {
      return nil();
    }
    return unify(b, d, s);
  }
  else if (node v1 := v, node u1 := u) {
    if (getName(v1) != getName(u1)) {
      return nil();
    }
    if (arity(v1) != arity(u1)) {
      return nil();
    }
    ksv = getChildren(v1);
    ksu = getChildren(u1);
    for (<a, b> <- zip(ksv, ksu)) {
      s = unify(a, b, s);
      if (nil() := s) {
        return nil();
      }
    }
    return s;
  }
  else if (list[value] v1 := v, list[value] u1 := u) {
    //println("Two lists");
    if (size(u1) != size(v1)) {
      //println("non-equal length");
      return nil();
    }
    if (u1 == [], v1 == []) {
      //println("empty");
      return s;
    }
    for (<a, b> <- zip(u1, v1)) {
      s = unify(a, b, s);
      if (nil() := s) {
        //break;
        return nil();
      }
    }
    return s;
  }
  //println("Returning nil for u = <u> and v = <v>");
  return nil();
}

value walk(value v, Subst s) {
  //println("Walking: v = <v>, s = <s>");
  if (Var v1:var(_) := v, v in s.bindings) {
    return walk(s.bindings[v], s);
  }
  return v;
}

Subst extSCheck(Var x, value v, Subst s) {
  if (occursCheck(x, v, s)) {
    return nil();
  }
  return extS(x, v, s);
}

bool occursCheck(Var x, value v, Subst s) {
  v = walk(v, s);
  
  if (var(_) := v) {
    return x == v;
  }
  
  if (list[value] l := v) {
    return any(vv <- l, occursCheck(x, vv, s));
  }
  
  if (<value a, value b> := v) {
    return occursCheck(x, a, s) || occursCheck(x, b, s);
  }
  
  if (node n := v) {
    return any(vv <- getChildren(n), occursCheck(x, vv, s));
  }
  
  return false;
}

Subst extS(Var x, value v, Subst s) 
  = s[bindings = s.bindings + (x: v)];

Subst reifyS(value v, Subst s) {
  v = walk(v, s);
  
  if (Var v1:var(_) := v) {
    return extS(v1, reifyName(size(s.bindings)), s);
  }
  
  if (list[value] l := v, size(l) > 0) {
    return ( s | reifyS(vv, it) | vv <- l );
  }
  
  if (<value a, value b> := v) {
    return reifyS(b, reifyS(a, s));
  }

  if (node n := v) {
    return ( s | reifyS(vv, it) | vv <- getChildren(n) );
  }  
  
  return s;
}

value reify(value v, Subst s) {
  v = walkAll(v, s);
  return walkAll(v, reifyS(v, subst(())));
}

value walkAll(value w, Subst s) {
  v = walk(w, s);
  
  if (list[value] l := v) {
    return [ walkAll(x, s) | x <- l ];
  }
  
  if (<value a, value b> := v) {
    return <walkAll(a, s), walkAll(b, s)>;
  }
  
  if (node n := v, !(n is var)) {
    return makeNode(getName(n), [ walkAll(x, s) | x <- getChildren(n) ]);
  }
  
  return v;
}

str reifyName(int n) = "_.<n>";

value mplus(value ss, value() f) {
  //println("Mplus on ss = <ss>");
  if (nil() := ss) {
    //println("ss is nil");
    return f();
  }
  if (value() ss2 := ss) {
    //println("ss is closure");
    return value() { return mplus(f(), ss2); };
  }
  if (list[value] l := ss) {
    //println("ss is list: <ss>");
    if (value() f2 := l[1]) {
      // bug
      return [l[0], value() { return mplus(f2(), f); }];
    }
    throw "Cannot happen";
  }
  //println("mplus Otherwise");
  return [ss, f];
}

list[value] take(int n, value() f) {
  //println("Taking <n> values... f = <f>");
  res = [];
  while (n > 0) {
    //println("n = <n>");
    ss = f();
    //println("SS = <ss>");
    if (nil() := ss) {
      return res;
    }
    value x = ss; // bug
    if (value() ss2 := x) {
      f = ss2;
    }
    else if (list[value] l := x) {
      //println("x is list of value");
      n -= 1; // if not nil
      res += [l[0]];
      //println("Result = <res>");
      //println("l[1] = <l[1]>");
      if (value() g := l[1]) {
        f = g; //l[1];
      }
      else throw "Cannot happen";
      //println("F = <f>");
    } 
    else {
      res += [ss];
      return res;
    }
  }
  return res;
}

value bind(value ss, value(Subst) goal) {
  //println("Bind for <ss>: <nil() := ss>");
  // bug: Subst nil() := ss werkt niet
  if (nil() := ss) {
    //println("ss == nil subst");
    return nil();
  }
  if (value() f := ss) {
    //println("ss is closure");
    return value() { return bind(f(), goal); };
  }
  if (list[value] l := ss) {
    //println("ss = list : <l>");
    return mplus(goal(l[0]), value() { return bind(l[1](), goal); });
  }
  //println("Calling goal <goal>");
  return goal(ss);
}

value mplusAll(list[value(Subst)] goals, Subst s) {
  //println("mplusAll");
  if (size(goals) == 1) {
    //println("One goal left");
    return goals[0](s);
  }
  // todo: use ranges
  //println("otherwise mplusAl");
  return mplus(goals[0](s), 
     value() { return mplusAll(tail(goals), s); }); 
}


// bug: should be before.
//Var() varMaker() {
//  //println("Making var maker.");
//  int count = 0;
//  return Var() {
//    //println("COUNT = <count>");
//    v = var(count);
//    count += 1;
//    return v;
//  };
//}

//public Var() newVar; // = varMaker();

Var fresh() = var(getNanoTime());

list[Var] fresh(int n) = [ fresh() | _ <- [0..n] ];

list[value] infer(Var v, list[value(Subst)] goals, int n = 1) {
  value(Subst) goal = size(goals) == 1 ? goals[0] : \all(goals); 
  list[value] ss = take(n, value() { return goal(subst(())); });
  return [ reify(v, s) | Subst s <- ss ];
}


