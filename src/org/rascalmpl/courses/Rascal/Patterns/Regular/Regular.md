# Regular Expression Pattern

.Synopsis
Regular expression patterns.

.Index
/ \ [ ] ^ - . | ? * + { } $

.Syntax

.Types

.Function

.Details

.Description
Regular expressions are used to match a string value and to decompose it in parts and also to compose new strings. Regular expression patterns bind variables of type `str` when the match succeeds, otherwise they do not bind anything. 
They can occur in cases of visit and switch statements, 
on the left-hand side of the match operator (`:=` or `!:=`) and as declarator in enumerators.

We use a regular expression language that slightly extends/modifies the Java Regex language:

*  Regular expression are delimited by `/` and `/` optionally followed by modifiers (see below).

*  We allow _variable introductions_, syntax `<_Name_:_Regex_>`, which introduce a variable of type `str` named _Name_.
   A variable introduction corresponds to a group in a Java regexp. Each variable that is introduced should be unique, but may be referenced more than once later in the regular expression.

*  Regular expressions may also contain _references to variables_, syntax `<_Name_>`,
the string value of variable `_Name_` is used at the position of the variable reference. 
This can be used to define so-called _non-linear_ patterns.

*  Java regular expressions allow optional groups, which may introduce null bindings. Since uninitialized variables are not allowed in Rascal, we limit the kinds of expressions one can write here by not allowing nesting of variable introductions.

*  We allow variable references in a regular expression of the form: `<_Name_>` which inserts the string value of _Name_ in the pattern. $Name$ should have been introduced in the regular expression itself or in the context in which the regular expression occurs.

*  In Perl matching options _follow_ the regular expression, but Java uses the notation `(?Option)` 
at the _beginning_ of the regular expression to set matching options. We support both styles. 
The following modifiers are supported:

** _multi-line matching_: `(?m)` at the start of the regular expression or the modifier `m` at the end of the regular expression. The anchors `^` and `$` usually only match at the beginning and end of the subject string.
When this option is set they also match any begin or end of line that is embedded in the subject string. Examples:

** _case-insensitive matching_: `(?i)` or modifier `i`. Match characters irrespective of their case.

** _single-line mode_: `(?s)` or modifier `s`. The `.` expression does usually not match line terminators. When single-line mode is set, it will match any character including line terminators.

** _unix lines_: `(?d)` or modifier `d`. Usually newlines (`\n`), carriage return (`\r`) and new line carriage return (`\n\r`) sequences are all considered line terminators. When this option is set, only newline is considered to be a line terminator.


For convenience, we summarize the most frequently used constructs in regular expressions in the following table.


.Frequently used elements of Regular Expression Syntax
[cols="10,90"]
|====
|Operator   | Description

| `x`       | The single character `x` as long as it is not a punctuation character with a 
                                                        special meaning in the regular expression syntax
| `\p`      | The punctuation character `p`, this includes `!`, `"`, `#`, `$`, `%`, `&`, `'`, `(`, `)`, `*`, `+`, `,`, `-`, `.`, `/`, `:`, `;`, `<`, `=`, `>`, `?`, `@`, `[`, `\`, `]`, `^`, `_`, `{`, `\|`, `}`, and `~`.
| `\\`      | The backslash character
| `\n`      | Newline character
| `\t`      | Tab character
| `[...]`   | One of the characters between the brackets (also known as _character class_). 
                                                         Character ranges and set operations on character classes may be used.
| `[^...]`  | Any one character not between the brackets.
| `[a-z0-9]` | Character range: character between `a` and `z` or `0` and `9`.
| `.`       | Any character except a line terminator. If single-line mode is set (using `(?s)` or modifier `s`), then it matches any character including line terminators.
| `\d`      | Digit: `[0-9]`
| `\D`      | Non-digit:` [^0-9]`
| `\s`      | Whitespace
| `\S`      | Anything but whitespace.
| `\w`      | A word: `[a-zA-Z0-9_]`
| `\W`      | A non-word:` [^\w]`
| `xy`      | Match `x` followed by `y`
| `x|y`     | Match `x` or `y`
| `x?`      | Optional occurrence of `x`
| `x*`      | Zero or more occurrences of `x`
| `x+`      | One or more occurrences of `x`
| `x{n}`    | Exactly `n` occurrences of `x`
| `x{n,}`   | `n` or more occurrences of `x`
| `x{n,m}`  | At least `n`, at most `m` occurrences of `x`
| `^`       | The beginning of the subject string
| `$`       | The end of the input string
| `\b`      | Word boundary: position between a word and a non-word character
| `\B`      | Non-word boundary: position that is a not a word boundary
|====

.Examples
Here are some examples of regular expression patterns.
```rascal
/\brascal\b/i
```
does a case-insensitive match (`i`) of the word `rascal` between word boundaries (`\b`). And
```rascal
/^.*?<word:\w+><rest:.*$>/m
```
does a multi-line match (`m`), matches the first consecutive word characters (`\w`) and assigns them to the variable `word`. The remainder of the string is assigned to the variable `rest`. 


A variable reference used to make a non-linear pattern:
```rascal
```
----
matches strings like `abc---abc` that consist of two identical sequences of letters separated 
by three dashes. Variables that are referenced in a regular expression may also come from 
the context in which the regular expression occurs. For instance,
```rascal
/<x><n>/
```
will use the current values of `x` and `n` as regular expression. For values `"abc"`, respectively, `3` this would be equivalent to the regular expression:
```rascal
/abc3/
```
Observe that context variables may be of arbitrary type and that their value is first converted to 
a string before it is inserted in the regular expression. This can be used in many ways. 
For instance, regular expressions may contain restrictions on the number of repetitions 
of an element: `/a{3}/` will match exactly three letters a. Also minimum and maximum 
number of occurrences can be defined. 
Here is how the repetition count can be inserted by a variable reference 
(where `n` is assumed to have an integer value):

```rascal
/a{<n>}/
```
Taking this example one step further, we can even write

```rascal
/<x:a{<n>}>/
```
in other words, we introduce variable `x` and its defining regular expression contains a 
reference to a context variable.


Multi-line matching:
```rascal-shell
/XX$/ := "lineoneXX\nlinetwo";
/XX$/m := "lineoneXX\nlinetwo";
/(?m)XX$/ := "lineoneXX\nlinetwo";
```

Case-insensitive matching:
```rascal-shell
/XX/ := "some xx";
/XX/i := "some xx";
/(?i)XX/ := "some xx";
```

Single-line mode:
```rascal-shell
/a.c/ := "abc";
/a.c/ := "a\nc";
/a.c/s := "a\nc";
/(?s)a.c/ := "a\nc";
```

Here are examples, how to escape punctuation characters in regular expressions:
```rascal-shell
/a\/b/ := "a/b";
/a\+b/ := "a+b";
```

.Benefits

.Pitfalls

