module demo::GenericFeatherweightJava::Examples

import demo::GenericFeatherweightJava::GFJ;
import demo::GenericFeatherweightJava::Types;  
import IO;

public T X = \type(\typevar("X"));
public T Y = \type(\typevar("Y"));

public L A = class("A", <[],[]>, Object, <[],[]>, cons(<[],[]>, super([]), []), []);
public L B = class("B", <[],[]>, Object, <[],[]>, cons(<[],[]>, super([]), []), []);
public L Fruit = class("Fruit", <[],[]>, Object, <[],[]>, cons(<[],[]>, super([]), []), []);
public L Appel = class("Appel", <[],[]>, lit("Fruit",[]), <[],[]>, cons(<[],[]>, super([]), []), []);
public L Banaan = class("Banaan", <[],[]>, lit("Fruit",[]), <[],[]>, cons(<[],[]>, super([]), []), []);
public L Tuple = class("Tuple", <[],[]>, Object, <[\type(Object),\type(Object)],["fst","snd"]>, cons(<[],[]>, super([]), []), [method(<[],[]>,\type(Object),"getfst", <[],[]>, access(this,"fst"))]);
public L GTuple = class("Tuple", <[X,Y],[ObjectType,ObjectType]>, Object, <[X,Y],["fst","snd"]>, cons(<[],[]>, super([]), []), [method(<[],[]>,X,"getfst", <[],[]>, access(this,"fst"))]);
  
public void init() {
  Types::CT = ("A":A, 
               "B":B, 
               "Fruit":Fruit,
               "Appel":Appel, 
               "Banaan":Banaan, 
               // "Object":Object 
               "Tuple":Tuple, 
               "GTuple": GTuple
               );
}

public void test() {
  init();
  
  assert fields(lit("Fruit",[])) == <[],[]>;   
  assert fields(lit("Tuple",[])) == <[\type(Object),\type(Object)],["fst","snd"]>;  
  assert fields(lit("GTuple",[\type(lit("A",[])),\type(lit("B",[]))])) == 
      <[\type(lit("A",[])),\type(lit("B",[]))],["fst","snd"]>;
  assert mtype("getfst", lit("GTuple", [\type(lit("Appel",[])),\type(lit("Appel",[]))])) == 
           <<[],[]>,\type(lit("Appel",[])),[]>;   
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
     