# Hello

.Synopsis
Variations on the ubiquitous _Hello World_ example.

.Syntax

.Types

.Function

.Details

.Description

.Examples
##  `hello` on command line 


We demonstrate hello via an interactive session with the Rascal system. First we get the prompt `rascal>` that shows that Rascal is ready for our input. 
Next, we import the library module [IO]((Libraries:Prelude-IO)) since hello world requires printing. Rascal responds with the feedback `ok` so we know that all went well. Finally, we call `println` and proudly observe our first Rascal output!
```rascal-shell
import IO;
println("Hello world, this is my first Rascal program");
```

##  `hello` as function 


A slightly more audacious approach is to wrap the print statement in a function and call it:
```rascal-shell
import IO;
void hello() {
   println("Hello world, this is my first Rascal program");
}
```
When you type in a command and continue it on a new line 
the Rascal systems prompts you with `>>>>>>>` to 
indicate that more input is needed. Don't get scared by 
the `void (): void hello();` that you get back 
when typing in the hello function. The first 
`void ()` part says the result is a function that 
returns nothing, and the second part 
`void hello()` summarizes its value 
(or would you prefer a hex dump?).
Finally, we call the `hello` function and enjoy its output.
```rascal-shell,continue
hello();
```

##  `hello` as module 

The summit of hello-engineering can be reached by placing all the above in a separate module:

```rascal
include::{LibDir}demo/basic/Hello.rsc[tags=module]
```

This module should be placed in <project dir>/src/demo/basic/Hello.rsc.

Using this Hello module is now simple:

```rascal-shell
import demo::basic::Hello;
hello();
```

The `hello` function is by default visible outside the `Hello` module.
We could stress this by adding writing `public void hello() { ... }`.
Restricting visibility to the module itself can be achieved by adding the keyword `private`
to the definition of `hello`.

.Benefits

.Pitfalls

