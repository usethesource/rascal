@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::ConcretePico::Programs

import zoo::pico::syntax::Main;
import ParseTree;
import IO;

/*
 * A bunch of sample Pico programs.
 */
 

public PROGRAM small = (PROGRAM) `
begin
  declare x : natural,
          s : string;

  x := 10;
  while x do
   x := x - 1;
   s := s || "#"
  od
end
`;

public PROGRAM exampleTypeErrors = (PROGRAM) `
begin
  declare x : natural,
          s : string;
  x := "abc";
  s := 3
end
`;


/*********************************************/

public PROGRAM smallUninit = (PROGRAM) `
begin
  declare x : natural,
          s : string;

  while x do
   x := x - 1;
   s := s || "#"
  od
end
`;
       
/*********************************************/
/* do not change this program (regression tests!) */
public PROGRAM fac = (PROGRAM) `
begin
  declare
    input : natural,
    output  :  natural,
    repnr: natural,
    rep: natural;

  input := 10;
  output := 1;
  while input - 1 do
    rep := output;
    repnr := input;
    while repnr - 1 do
      output := output + rep;

      repnr := repnr - 1
    od;
    input := input - 1
  od
end
`;

/*********************************************/
       
public PROGRAM facUninit = (PROGRAM) `
begin
  declare
    input : natural,
    output  :  natural,
    repnr: natural,
    rep: natural;

  input := 10;

  while input - 1 do
    rep := output;
    repnr := input;
    while repnr - 1 do
      output := output + rep;

      repnr := repnr - 1
    od;
    input := input - 1
  od
end
`;

/*********************************************/

public PROGRAM big = (PROGRAM) `
begin
  declare
    input: natural,
    output: string,
    i: natural,
    j: natural,
    k: natural,
    s: string,
    t: string;
  input := 14;
  if input then
    i := input;
    s := "";
    while i do
      j := 0;
      k := 0;
      while i do
        i := i-1;
        if j then
          j := 0;
          k := k+1
        else
          j := 1
        fi
      od;
      if j then t := "1" else t := "0" fi;
      s := t||s;
      i := k
    od;
    output := s
  else
    output := "0"
  fi
end
`;

/*********************************************/
