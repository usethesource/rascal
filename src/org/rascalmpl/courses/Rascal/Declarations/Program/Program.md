---
title: Program
---

#### Synopsis

A Rascal program consists of a number of ((Declarations-Module))s.

#### Syntax

#### Types

#### Function

#### Description

A Rascal program consists of a number of ((Module Declarations)), each stored in a separate file with extension `.rsc`.

Rascal code comments are prefixed with `//`.

A Rascal program can have a `main()` function declared as its entry point.

Just like any other Rascal function, `main()` has arguments and a return type, 
but unlike other functions, the arguments of `main()` have a special interpreation.

There are two forms of `main()`:

1. Accepting just one argument of type `list[str]`.
2. Accepting keyword arguments of varying types

In the first case, the declaration of `main()`, its arguments and 
their effect is similar to languages such as C and Java, any command-line 
arguments are passed to the program without any further processing as a 
list of strings.

In the second case, any arguments to `main()` are [marshalled](https://en.wikipedia.org/wiki/Marshalling_(computer_science)) to and from their named keyword 
counterparts as well as validated.

#### Examples

1. Command line arguments as a list of string values

   ```
   module main_args_as_list

   import IO;

   int main(list[str] args){
       println(args);
       return 0;
   }
   ```

   Calling this program with:

   ```
   > java -jar rascal.jar main_args_as_list.rsc -p1 1 -p2 2 -p3 3
   ```

   Would produce:

   ```
   ["-p1","1","-p2","2","-p3","3"]
   ```

2. Command line arguments as named keywords of varying types.

   ```
   module main_args_as_kwd
   
   import IO;

   int main(int a=0, 
   	    str b="", 
	    bool c=false, 
	    list[int] d=[],
	    loc e=|file:///|){

       println(a);
       println(b);
       println(c);
       println(d);
       println(e);
       return 0;
   }
   ```

   Notice here, all keyword arguments require a default value as per Rascal's
   paradigm.

   Calling this program with:

   ```
   > java -jar rascal.jar main_args_as_kwd.rsc
   ```

   Would produce:

   ```
   0

   false
   []
   |file:///|
   ```

   Now, every keyword argument can be set at the comand line with its 
   "name". For example, calling the same program with:

   ```
   > java -jar rascal.jar main_args_as_kwd.rsc -b Blue
   ```

   Would produce:

   ```
   0
   blue
   false
   []
   |file:///|
   ```

   And calling it with:

   ```
   > java -jar rascal.jar main_args_as_kwd.rsc -c -d 1 2 3 4 5 -e \|file:///blue.tst\|
   ```

   Would produce:

   ```
   0

   true
   [1,2,3,4,5]
   |file:///blue.txt|
   ```

   Notice here that `-c`, being of type Bool, was simply specified in the 
   command line and that was enough to toggle its value to `true`.
   Notice also that `-d` has already been transformed to a list of integers 
   as well as location.
   


#### Benefits

* Keyword arguments offer automatic data marshalling and validation from 
  their command line format to valid values according to a keyword's defined
  data type.

#### Pitfalls

* Mandatory arguments require special handling

