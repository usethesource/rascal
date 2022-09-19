---
title: Parse
---

#### Synopsis

Parse a Func program from a string or a file.

#### Syntax

#### Types

#### Function

#### Description

Parsing uses the syntax rules for a given start non-terminnal to parse a string and turn it into a parse tree.
The work horse is the [parse]((Library:ParseTree-parse)) function that is available in the 
[ParseTree]((Library:ParseTree)) library.

#### Examples

Here is how to parse Func programs from a string or file:
```rascal-include
demo::lang::Func::Parse
```

                
Let's try this on example `F0.func`:
```rascal
((|project://rascal/src/org/rascalmpl/library/demo/lang/Func/programs/F0.func|))
```

First, we try the version with a string argument:
```rascal-shell,continue
import demo::lang::Func::Parse;
import demo::lang::Func::programs::F0;
parse(F0);
```
This must be defined as success: we get the original program and its parse tree back.
Next, we try the same from a file. We use the scheme `std` that refers to files that reside in the Rascal library.
See [$Rascal:Expressions/Values/Location] for further details on other schemes.
```rascal-shell,continue
parse(|std:///demo/lang/Func/programs/F0.func|);
```

#### Benefits

#### Pitfalls

