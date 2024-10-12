/**
 * Copyright (c) 2024, NWO-I Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/

module lang::rascal::tests::concrete::recovery::PicoRecoveryTests

import lang::pico::\syntax::Main;

import ParseTree;
import util::ErrorRecovery;
import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;

Tree parsePico(str input, bool visualize=false)
    = parser(#Program, allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=<"<visualize>">|);

test bool picoOk() = checkRecovery(#Program, "begin declare input : natural,
              output : natural,
              repnr : natural,
              rep : natural;
      input := 14;
      output := 0;
      while input - 1 do
          rep := output;
          repnr := input;
          while repnr - 1 do
             output := output + rep;
             repnr := repnr - 1
          od;
          input := input - 1
      od
end", []);

test bool picoTypo() = checkRecovery(#Program, "begin declare input : natural,
              output : natural,
              repnr : natural,
              rep : natural;
      input := 14;
      output := 0;
      while input - 1 do
          rep := output;
          repnr := input;
          while repnr - 1 do
             output := output x rep;
             repnr := repnr - 1
          od;
          input := input - 1
      od
end", ["output x rep"]);

test bool picoMissingSemi() = checkRecovery(#Program, "begin declare input : natural,
              output : natural,
              repnr : natural,
              rep : natural;
      input := 14;
      output := 0;
      while input - 1 do
          rep := output;
          repnr := input;
          while repnr - 1 do
             output := output + rep;
             repnr := repnr - 1
          od
          input := input - 1
      od
end", ["input := input - 1
      od"]);

test bool picoTypoSmall() = checkRecovery(#Program, "begin declare;
  while input do
    input x= 14;
    output := 0
  od
end", ["x= 14"]);

test bool picoMissingSemiSmall() = checkRecovery(#Program, "begin declare;
  while input do
    input := 14
    output := 0
  od
end", ["output := 0
  od"]);

test bool picoEof() = checkRecovery(#Program, "begin declare; input := 0;", ["input := 0;"]);

test bool picoEofError() = checkRecovery(#Program, "begin declare x y; input := 0;", ["x y;", "input := 0;"]);
