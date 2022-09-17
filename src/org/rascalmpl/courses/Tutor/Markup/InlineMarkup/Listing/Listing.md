---
title: Listing
---

#### Synopsis

Include some source code in the document

#### Syntax

``````
```language-name
int fac(int n) {
    if (n <= 0) {
        return 1;
    }
    else {
        return n * fac(n-1);
    }
}
``````

#### Types

#### Function

#### Description

You can use the triple backquotes to encapsulate a piece of literal source text. The language name indicates
which syntax highlighter to use.

If you use `rascal-shell` as a language name, then the code is executed line-by-line by a Rascal shell
and the output is collected.

#### Examples

``````
```rascal-shell
x = 1 + 1;
```
``````

Would produce:

```rascal-shell
x = 1 + 1;
```

Using `continue` you can continue where you've left off:

``````
```rascal-shell,continue
x + x
```
``````

Which results in:

```rascal-shell,continue
x + 1
```

Simply using `rascal` as a language does not have all these effects:

``````
```rascal
int fac(0) = 1;
default int fac(int n) = n * fac(n - 1);
```
``````

That block simply produces this:

```rascal
int fac(0) = 1;
default int fac(int n) = n * fac(n - 1);
```

#### Benefits

#### Pitfalls



