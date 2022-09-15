---
title: IDE
---

#### Synopsis

An Integrated Development Environment for Pico.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

#### Examples

```rascal
include::{LibDir}demo/lang/Pico/Plugin.rsc[tags=module]
```

                
*  First the name of the language and its file name extension are defined (image:{images}/1.png).
*  Next the connection with the parser (image:{images}/2.png), checkers (image:{images}/3.png), evaluator (image:{images}/4.png),
  compiler (image:{images}/5.png), and visualizer (image:{images}/6.png) are defined.
*  (image:{images}/7.png) combines the above into a set of contributions to the Pico IDE.
*  The actual creation of the Pico IDE is done by `registerPico` (image:{images}/8.png) that:
   **  Registers the Pico language with name, file name extension and Parser. Whenever a user clicks on
       a `.pico` file an editor will opened and the parsed file will be displayed in it.
   **  Registers _annotators_ for Pico programs. Annotators run whenever a change is made to a Pico program in an open editor.
   **  Registers contributions to the context menu in the editor. When the user right-clicks, the context menu
       pops up and it will show a Pico entry with actions defined in the contributions.


Let's write a Pico program that produces a string of "a"s:


![]((IDE-Screenshot1.png))


As can be seen in the editor above, we get an error since we made a typo (missing comma) in the declarations. We correct it:



![]((IDE-Screenshot2.png))


Now it turns out that we had erroneously used the `+` operator on strings (it should be `||`). We correct it:


![]((IDE-Screenshot3.png))


Now we get a warning that variable `n` is not initialized. We correct it and get an error-free and warning-free program:


![]((Screenshot4.png))


#### Benefits

#### Pitfalls

