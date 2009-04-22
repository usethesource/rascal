module demo::GenericFeatherweightJava::Examples

import demo::GenericFeatherweightJava::GFJ;
import demo::GenericFeatherweightJava::Types;  
import IO;

public Type X = typeVar("X");
public Type Y = typeVar("Y");

public Class A = class("A", <[],[]>, Object, <[],[]>, cons(<[],[]>, super([]), []), []);
public Class B = class("B", <[],[]>, Object, <[],[]>, cons(<[],[]>, super([]), []), []);
public Class Fruit = class("Fruit", <[],[]>, Object, <[],[]>, cons(<[],[]>, super([]), []), []);
public Class Appel = class("Appel", <[],[]>, typeLit("Fruit",[]), <[],[]>, cons(<[],[]>, super([]), []), []);
public Class Banaan = class("Banaan", <[],[]>, typeLit("Fruit",[]), <[],[]>, cons(<[],[]>, super([]), []), []);
public Class Tuple = class("Tuple", <[],[]>, Object, <[Object,Object],["fst","snd"]>, cons(<[],[]>, super([]), []), [method(<[],[]>,Object,"getfst", <[],[]>, access(this,"fst"))]);
public Class GTuple = class("Tuple", <[X,Y],[Object,Object]>, Object, <[X,Y],["fst","snd"]>, cons(<[],[]>, super([]), []), [method(<[],[]>,X,"getfst", <[],[]>, access(this,"fst"))]);
  
public void init() {
  demo::GenericFeatherweightJava::Types::ClassTable = 
              ("A":A, 
               "B":B, 
               "Fruit":Fruit,
               "Appel":Appel, 
               "Banaan":Banaan, 
               // "Object":Object 
               "Tuple":Tuple, 
               "GTuple": GTuple
               );
}

public bool test() {
  init();
  
  assert fields(typeLit("Fruit",[])) == <[],[]>;   
  assert fields(typeLit("Tuple",[])) == <[Object,Object],["fst","snd"]>;  
  assert fields(typeLit("GTuple",[typeLit("A",[]),typeLit("B",[])])) == 
      <[typeLit("A",[]),typeLit("B",[])],["fst","snd"]>;
  assert mtype("getfst", typeLit("GTuple", [typeLit("Appel",[]),typeLit("Appel",[])])) == 
           <<[],[]>,typeLit("Appel",[]),[]>;  
  return true; 
}  

/*
public L Pair = class("Pair", [<var("X"), lit(Object)>, <var("Y"), lit(Object)>], lit(Object),
                      ("fst": \type(var("X")), "snd":\type(var("Y"))),
                      cons(("fst":\type(var("X")),"snd":\type(var("Y"))), super([]),[this("fst"),this("snd")]),
                      [
                      method([<var("Z"), lit(Object)>], 
                             \type(\lit("Pair",[\type(var("X")),\type(var("Y"))])), 
                             "setfst", 
                             ("newfst":\type(var("Z"))), 
                             new(lit("Pair", [\type(var("Z")),\type(var("Y"))]),[var("newfst"), access(this, "snd")]))
                      ]
                      );
*/
     