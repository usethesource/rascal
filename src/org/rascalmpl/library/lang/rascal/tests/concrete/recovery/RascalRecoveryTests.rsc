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

module lang::rascal::tests::concrete::recovery::RascalRecoveryTests

import lang::rascal::\syntax::Rascal;

import lang::rascal::tests::concrete::recovery::RecoveryCheckSupport;

bool debugging = false;

test bool rascalOk() = checkRecovery(#start[Module], "
    module A

    int inc(int i) {
        return i+1;
    }
    ", []);

test bool rascalFunctionDeclarationOk() = checkRecovery(#FunctionDeclaration, "void f(){}", []);

test bool rascalModuleFollowedBySemi() = checkRecovery(#start[Module], "
    module A
    ;
    ", [";"]);

test bool rascalOperatorTypo() = checkRecovery(#start[Module], "
    module A

    int f() = 1 x 1;
    ", ["x 1;"]);

test bool rascalIllegalStatement() = checkRecovery(#start[Module], "module A void f(){a}", ["a}"]);

test bool rascalMissingCloseParen() = checkRecovery(#start[Module], "module A void f({} void g(){}", ["("]);

test bool rascalFunctionDeclarationMissingCloseParen() = checkRecovery(#FunctionDeclaration, "void f({} void g() {}", ["("]);

test bool rascalIfMissingExpr() = checkRecovery(#FunctionDeclaration, "void f(){if(){1;}}", [")"]);

test bool rascalIfBodyEmpty() = checkRecovery(#start[Module], "module A void f(){1;} void g(){if(1){}} void h(){1;}", ["{", "} "]);

test bool rascalMissingOpeningParen() = checkRecovery(#start[Module], 
"module A
void f) {
    }
    void g() { 
    }", ["}
",") {"]);

test bool rascalFunFunMissingCloseParen() = checkRecovery(#start[Module], "module A void f(){void g({}} void h(){}", ["void g({}", "} "]);

test bool rascalIfMissingOpeningParen() = checkRecovery(#start[Module], 
"module A void f() {
  if 1) {
    1;
  }}", ["1) "]);

test bool rascalIfMissingCloseParen() = checkRecovery(#start[Module], 
"module A
void f() {
  if (1 {
    1;
  }}", ["1 "]);

test bool rascalIfMissingSemi() = checkRecovery(#start[Module], 
"module A
void f() {
  if (true) {
    a
  }
}", ["{", "}
"]);
