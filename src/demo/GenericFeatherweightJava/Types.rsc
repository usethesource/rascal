module demo::GenericFeatherweightJava::Types

import demo::GenericFeatherweightJava::GFJ;
import List;
import IO;  

// global class table
public map[C,L] CT = (); 

public N Object = lit("Object",[]);
public T ObjectType = \type(Object);  
  
// representing method types
alias MethodType = tuple[tuple[list[T Ys],list[T] Ps] YsPs, T U, list[T] Us];

// type environment
alias Delta = map[T v,T T];
 
// variable environment
alias Gamma = map[x v, T T];  

data Error = NoSuchMethod(m m) | NoSuchField(f f) | NoType(e e);
    
public rel[C,C] subclasses() { 
  return { <CT[c].C,CT[c].extends.C> | C c <- CT }*;
}

public bool subclass(C c1, C c2) {
  return <c1,c2> in subclasses();
}  

// TODO check if this works  
public rel[T,T] subtypes(Delta d) {
  return { <l,s> | X x <- d, N n := d[x], L l := CT[n.C], s := inst(l.N, l.XsNs, n.Ts)}*;  
}

public bool subtype(Delta d, T t1, T t2) {
  return <t1,t2> in subtypes(d);
}
  
public bool subtypes(Delta d, list[T] t1, list[T] t2) {
  if (length(t1) != length(t2)) return false;
  if ((int i <- domain(t1)) && !subtype(d, t1[i], t2[i])) return false;
  return true;
}
      
public tuple[list[T] ts,list[f] fs] fields(N n) {
  if (n == Object) return <[],[]>;
  
  L l = CT[n.C];
      
  <sT,sf> = fields(inst(l.N, l.XsNs, n.Ts));
  <tT,tf> = inst(l.Tsfs, l.XsNs, n.Ts);
  
  return <sT + tT, sf + tf>;
}

public T ftype(N n, f f) {
  fields = fields(n);
  if (int i <- domain(fields.fs) && fields.fs[i] == f) return fields.ts[i];
}

public map[T,T] bindings(tuple[list[T] Xs, list[T] Ns] formals, list[T] actuals ) {
  return (formals.Xs[i] : actuals[i] | int i <- domain(formals.Xs));  
}  

public &T inst(&T arg, tuple[list[T] Xs, list[T] Ns] formals, list[T] actuals) {
  map[T,T] subs = bindings(formals, actuals);
  return visit (arg) { case T t => subs[t] ? t };
}

public MethodType mtype(m name, N n) {
   list[tuple[X,N]] let;
   L l = CT[n.C];

   if (n == Object) throw NoSuchMethod(name);

   // find a method with the same name using list matching  
   methods = CT[n.C].Ms; 
   if (int i <- domain(methods) && methods[i].m == name) {
     return inst(<methods[i].XsNs, methods[i].T, methods[i].Tsxs.Ts>, l.XsNs, n.Ts);
   }
   else { // if not found, go to super class
     return mtype(name, inst(l.extends, l.formals, n.params));    
   } 
}   

public e mbody(m name, list[T] bindings, N n) {
   list[tuple[X,N]] let;
   L l = CT[n.name];

   if (n == Object) throw NoSuchMethod(name);

   ms = CT[n.name].methods;
   if (int i <- domain(ms) && ms[i].m == name) 
     return inst(inst(expr, l.formals, n.params), ms[i].XsNs, bindings);
   else
     return mtype(name, inst(l.extends, l.formals, n.params));     
}

public T bound(T t, Delta d) {
  switch (t) {
    case \type(N n) : return \type(n);
    case \type(X x) : return \type(d[x]);
  }
}

public T etype(Gamma g, Delta d, e expr) {
  switch (expr) {
    case var(x name) : return d[x];
    case this : return d["this"];
    case access(e e0, f fi) : {
      T T0    = typeOf(g, d, e0);
      <Ts,fs> = fields(bound(T0, d).n);
      if (int i <- domain(Ts) && fs[i] == fi) return Ts[i];
    }
    case call(e e0, m name, list[T] Vs, list[e] es) : {
      T t0 = typeOf(g, d, e0);
      <<Ys,Ps>, U, Us> = mtype(bound(t0,d).n); 
      
      if (subtypes(d, <Ys,Ps>, inst(Ps, Vs, Ys))) {
        Ss = [ typeOf(g, d, e) | e <- es];
        if (subtypes(d, Ss, inst(Us,Vs,Ys))) return inst(U, Vs, Ys);  
      }
    } 
    case new(T N, list[e] es) : {
       <Ts,fs> = fields(N);
       Ss = [ typeOf(g, d, e) | e <- es];
       if (subtypes(d, Ss, Ts)) return N;
    }
    case cast(T N, e e0) : {
      T0 = typeOf(g, d, e0);
      BT0 = bound(d, T0);
      if (subtype(BT0, N)) return N;

      D = BT0.N.C;
      Us = BT0.N.Ts;
      if (subtype(N, BT0) && dcast(N, D)) return N;
    }
  }
    
  throw NoType(e);
}  