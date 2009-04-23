module demo::GenericFeatherweightJava::Examples

import demo::GenericFeatherweightJava::GFJ;
import demo::GenericFeatherweightJava::Types;  
import IO;

// Reusable type variables
public Type X = typeVar("X");
public Type Y = typeVar("Y");
public Type Z = typeVar("Z");
public Type T = typeVar("T");

// Generic library classes
public Class Collection = class("Collection", <[X],[Object]>, Object, 
                            <[],[]>,
                            cons(<[],[]>, super([]), []),
                            [
                              method(<[],[]>, typeLit("Number",[]), "size", <[],[]>, new(typeLit("Zero",[]),[]))
                            ]
                          );

public Class Map = class("Map",<[X,Y],[Object,Object]>, typeLit("Collection",[X]), // header
                      <[X,Y], ["key","value"]>, // fields    
                      cons(<[X,Y],["key","value"]>,super([]),[this("key"),this("value")]), // constructor
                      [
                        method(<[],[]>, Y, "get", <[X],["key"]>, access(var("this"),"value")),
                        method(<[],[]>, typeLit("Map",[X,Y]), "set", <[X,Y],["key","value"]>, new(typeLit("Map",[X,Y]),[var("key"), var("value")])),
                        method(<[],[]>, typeLit("Number",[]), "size", <[],[]>, new(typeLit("Succ",[]),[new(typeLit("Zero",[]),[])])) 
                      ]
                   );

public Class Tuple = class("Tuple", <[X,Y],[Object,Object]>, typeLit("Collection",[Object]), 
                        <[X,Y],["fst","snd"]>, 
                        cons(<[X,Y],["fst","snd"]>, super([]), [this("fst"), this("snd")]), 
                        [
                           method(<[],[]>,X,"getFst", <[],[]>, access(var("this"),"fst")),
                           method(<[],[]>,Y,"getSnd", <[],[]>, access(var("this"),"snd")),
                           method(<[Z],[Object]>, typeLit("Tuple",[Z,Y]), "setFst", <[Z],["fst"]>, new(typeLit("Tuple",[Z,Y]),[var("fst"), access(var("this"), "snd")])), 
                           method(<[Z],[Object]>, typeLit("Tuple",[X,Z]), "setSnd", <[Z],["snd"]>, new(typeLit("Tuple",[X,Z]),[access(var("this"), "fst"), var("snd")])),
                           method(<[],[]>, typeLit("Number",[]), "size", <[],[]>, new(typeLit("Succ",[]),[new(typeLit("Succ",[]),[new(typeLit("Zero",[]),[])])])) 
                        ]
                     );

public Class Number  = class("Number", <[],[]>, Object, <[],[]>, cons(<[],[]>, super([]), []), []);
public Class Zero = class("Zero", <[],[]>, typeLit("Number",[]), <[],[]>, cons(<[],[]>, super([]), []), []);
public Class Succ = class("Succ", <[],[]>, typeLit("Number",[]), 
                            <[typeLit("Number",[])],["prev"]>, 
                            cons(<[typeLit("Number",["prev"])],[this("prev")]>, super([]), [this("prev")]), 
                            [
                            ]
                         );

public Class A = class("A", <[],[]>, Object, <[],[]>, cons(<[],[]>, super([]), []), []);
public Class B = class("B", <[],[]>, Object, <[],[]>, cons(<[],[]>, super([]), []), []);

// Example classes that use the Raw versions of Generic types

// class Example1 { Tuple main() { return new Tuple(Zero, Succ(Zero)); }
// {c,sh,w}ould be:
// class Example1 { Tuple<Zero,Succ> main() { return new Tuple<Zero,Succ>(Zero, Succ(Zero)); }
public Class Example1 = class("Example1",<[],[]>, Object, 
                              <[],[]>,
                              cons(<[],[]>,super([]), []),
                              [
                                method(<[],[]>,typeLit("Tuple",[]),"main", <[],[]>, [new(typeLit("Tuple",[]),[new(typeLit("Zero",[]),[]),new(typeLit("Succ",[]),[new(typeLit("Zero",[]),[])])])])
                              ]
                             );


public void init() {
  demo::GenericFeatherweightJava::Types::ClassTable = 
              ("A":A, 
               "B":B, 
               "Number":Number,
               "Zero":Zero,
               "Succ":Succ, 
               "Tuple":Tuple, 
               "Collection":Container,
               "Map":Map,
               "Example1":Example1
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
     