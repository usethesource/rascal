module demo::ConcretePico::Programs

import languages::pico::syntax::Pico;
import IO;
import UnitTest;

/*
 * A bunch of sample Pico programs.
 */
 

public PROGRAM small = [|
begin
  declare x : natural,
          s : string;

  x := 10;
  while x do
   x := x - 1;
   s := s || "#"
  od
end
|];

/*********************************************/

public PROGRAM smallUninit =[|
begin
  declare x : natural,
          s : string;

  while x do
   x := x - 1;
   s := s || "#"
  od
end
|];
       
/*********************************************/

public PROGRAM fac = [|
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
|];

/*********************************************/
       
public PROGRAM facUninit = [|
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
|];

/*********************************************/

public PROGRAM big = [|
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
|];

/*********************************************/
