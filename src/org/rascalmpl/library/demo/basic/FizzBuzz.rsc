@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@synopsis{Typical code example: FizzBuzz}
module demo::basic::FizzBuzz

import IO; 

@synopsis{fizzbuzz1 revolves around ternary conditions}
void fizzbuzz1() {
   for (int n <- [1 .. 101]){
      fb = ((n % 3 == 0) ? "Fizz" : "") + ((n % 5 == 0) ? "Buzz" : "");
      println((fb == "") ?"<n>" : fb);
   }
}

@synopsis{fizzbuzz2 embraces pattern matching and the switch statement}
void fizzbuzz2() {
  for (n <- [1..101]) 
    switch(<n % 3 == 0, n % 5 == 0>) {
      case <true,true>  : println("FizzBuzz");
      case <true,false> : println("Fizz");
      case <false,true> : println("Buzz");
      default: println(n);
    }
}
 
@synopsis{fizzbuzz3 uses classical structured if-then-else} 
void fizzbuzz3() {
  for (n <- [1..101]) {
    if (n % 3 == 0) {
      print("Fizz");
    }
    if (n % 5 == 0) {
      print("Buzz");
    } else if (n % 3 != 0) {
      print(n);
    }
    println("");
  }
}

