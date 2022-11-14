---
title: Ambiguity
---

#### Synopsis

Ambiguity found in parsed text. 

#### Description

Rascal supports general context-free grammars and also provides
various mechanisms to disambiguate them.

This exception is thrown when, given a grammar and a sentence,
an ambiguity is found while parsing the sentence according to that grammar.

#### Examples

First declare a very simple expression language that should
recognize expressions like `a`, `a+a`, `a+(a+a)`:
```rascal-shell
syntax A = "a";
syntax E = A | "(" E ")" | E "+" E;
```
Next, import the ParseTree module that provides a `parse` function that we will use:
```rascal-shell,continue
import ParseTree;
```
Entering a first expression goes well, except that the parser generator already predicts future ambiguity. So it prints a warning.
```rascal-shell-error,continue
parse(#E, "a+a");
```

The following example triggers the predicted ambiguity indeed:

```rascal-shell,continue,errors
parse(#E, "a+a+a");
```
The conclusion is that there are two parses here: `a+(a+a)` and `(a+a)+a`, 
because we did forget to define the associativity of the `+` operator.

Let's fix this:

```rascal-shell,errors
syntax A = "a";
syntax E = A | "(" E ")" | left E "+" E;
import ParseTree;
parse(#E, "a+a+a");
```

However, one can also deal with ambiguity differently. For example we could have the parser build a tree
for all ambiguous interpretations and inspect the resulting data-structure:

```rascal-shell,errors
syntax A = "a";
syntax E = A | "(" E ")" | left E "+" E | left E "*" E;
import ParseTree;
t = parse(#E, "a+a*a", allowAmbiguity=true);
// Is the forest indeed ambiguous?
/amb(_) := t
// How many alternatives?
import Set;
import IO;
/amb(a) := t ? size(a) : 0; 
// Which rules are at the top of the alternatives?
if (/amb({a1, a2}) := t) 
  println("alternative 1: <a1.prod>
          'alternative 2: <a2.prod>");
```

Or, one could catch the ambiguity and report it like a ((RuntimeExceptions-ParseError)):

```rascal-shell,continue
import IO;
try 
  parse(#E, "a+a*a");
catch Ambiguity(loc l, str s, _): 
  println("the input is ambiguous for <s> on line <l.begin.line>");
```

Here are some pointers for further disambiguation help:

* [Syntax Definitions]((Rascal:Declarations-SyntaxDefinition)).
* [Disambiguation features]((Rascal:SyntaxDefinition-Disambiguation)).
