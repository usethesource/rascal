@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module demo::GenericFeatherweightJava::Examples

import demo::GenericFeatherweightJava::AbstractSyntax;
import demo::GenericFeatherweightJava::Types;  
import demo::GenericFeatherweightJava::Extract;
import demo::GenericFeatherweightJava::SolveConstraints;
import demo::GenericFeatherweightJava::TypeConstraints;

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
                            cons(<[typeLit("Number",[])],["prev"]>, super([]), [this("prev")]), 
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
                                method(<[],[]>,typeLit("Tuple",[]),"main", <[],[]>, call(new(typeLit("Tuple",[]),[new(typeLit("Zero",[]),[]),new(typeLit("Succ",[]),[new(typeLit("Zero",[]),[])])]), "getFst", [], []))
                              ]
                             );

  
public bool init() {
  demo::GenericFeatherweightJava::Types::ClassTable += 
              ("A":A, 
               "B":B, 
               "Number":Number,
               "Zero":Zero,
               "Succ":Succ, 
               "Tuple":Tuple, 
               "Collection":Collection,
               "Map":Map,
               "Example1":Example1
               );
  registerLibraries({"Tuple", "Collection", "Map"});
  return true;
}

public bool testAll() {
  init();
  
  assert fields(typeLit("A",[])) == <[],[]>;   
  assert fields(typeLit("Tuple",[])) == <[Object,Object],["fst","snd"]>;  
  assert fields(typeLit("Tuple",[typeLit("A",[]),typeLit("B",[])])) == <[typeLit("A",[]),typeLit("B",[])],["fst","snd"]>;
  assert mtype("getFst", typeLit("Tuple", [typeLit("A",[]),typeLit("A",[])])) == 
           <<[],[]>,typeLit("A",[]),[]>;  

  result = solveConstraints();
  assert result[typeof(new(typeLit("Tuple",[]),[new(typeLit("Zero",[]),[]),new(typeLit("Succ",[]),[new(typeLit("Zero",[]),[])])]))]
         == Set({typeLit("Tuple",[typeLit("Zero",[]),typeLit("Succ",[])])});
  return true; 
}  

test testAll();

