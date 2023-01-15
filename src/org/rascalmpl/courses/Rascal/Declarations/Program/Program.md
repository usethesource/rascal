---
title: Program
---

#### Synopsis

A Rascal program consists of a number of ((Declarations-Module))s.

#### Syntax

#### Types

#### Function

#### Description

A Rascal program consists of a number of ((Module Declarations)), each stored in a separate file with the extension `.rsc`.

Throughout a Rascal program, comments are prefixed by `//` and the language also provides more meaningful code comments, 
described in ((Doc Annotations)). 

A Rascal program can have *one* `main()` function declared as its entry point.

Just like any other Rascal function, `main()` has typed arguments and a return type, but unlike other functions, the 
keyword arguments of `main()` can be automatically translated from same name parameters passed at the command line.

There are two variants of `main()`:

1. Accepting just one argument of type `list[str]`
2. Accepting keyword arguments of varying types

In the first case, the declaration of `main()`, its arguments and  their effect are similar to the way ``main()`` is declared 
and used in languages such as C and Java. That is, any command-line arguments are passed to the program without any further 
processing as an array of strings. 

But in the second case, any arguments to `main()` are automatically [marshalled](https://en.wikipedia.org/wiki/Marshalling_(computer_science)) 
to their named keyword counterparts and get validated according to their declared type.

#### Examples

In the following examples, it is assumed that the user has access to the ``rascal-shell-stable.jar`` file and that the files with the code
describing each of the example programs below are placed in the same directory as the ``.jar`` file for simplicity.

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
   > java -jar rascal-shell-stable.jar main_args_as_list.rsc -p1 1 -p2 2 -p3 3
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

   Notice here, as Rascal is a *value-oriented language*, all keyword arguments must have a default value 
   (for more information see ((Immutable Values))).

   Calling this program with:

   ```
   > java -jar rascal-shell-stable.jar main_args_as_kwd.rsc
   ```

   Would produce:

   ```
   0

   false
   []
   |file:///|
   ```

   Now, every keyword argument to ``main()`` can be set at the comand line with its 
   "name". For example, calling the same program with:

   ```
   > java -jar rascal-shell-stable.jar main_args_as_kwd.rsc -b Blue
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
   > java -jar rascal-shell-stable.jar main_args_as_kwd.rsc -c -d 1 2 3 4 5 -e \|file:///blue.tst\|
   ```

   Would produce:

   ```
   0

   true
   [1,2,3,4,5]
   |file:///blue.txt|
   ```

   Notice here that `-c`, being of type `bool`, was simply specified in the 
   command line as a flag and that was enough to set its value to `true`.
   Notice also that `-d` has already been transformed to a list of integers
   as per its specification. The same applies for `e`, which is of type 
   location, although the "pipe" characters might require escaping.

   Finally, if no keyword parameters are passed on the command line (i.e. no command line 
   argument is prefixed by a `-` character), the arguments are assumed to be given 
   in the command line in the same order they are defined in `main()`. Therefore, 
   the following call *will produce an error*:

   ```
   > java -jar rascal-shell-stable.jar main_args_as_kwd.rsc SomeString
   ```

#### Benefits

* Keyword arguments offer automatic data marshalling and validation from 
  their command line format to valid values according to a keyword's defined
  data type. No extra libraries are required.


#### Pitfalls

* Mandatory positional arguments require special handling.

