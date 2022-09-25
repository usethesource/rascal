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

* You can use the triple backquotes to encapsulate a piece of literal source text. The language name indicates
which syntax highlighter to use.
   * If you use `rascal-shell` as a language name, then the code is executed line-by-line by a Rascal shell
and the output is collected. 
   * With `rascal-prepare` the code is executed in the current environment but no output is shown.
   * Finally using `rascal-include` you can include modules from disk that are on the current search path.
   * Either `rascal-shell`, `rascal-prepare` or `rascal-include` code with an unexpected error in it will _fail_ the documentation build and an error message will be reported. The build will continue to find other issues and compile all the other files.
   * Using the `error` label to a code block, an error will be expected and simply rendered to the user. This is to demonstrate error messages to the reader.
* The "magic comments" `highlight-next-line`, `highlight-start`, and `highlight-end` give you a way to highlight
selected lines in the code. Also you can use ranges like this `{1,4--6,9}`.

Use `showLineNumbers` to render line numbers in the code examples.

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
x + 1
```
``````

Which results in:

```rascal-shell,continue
x + 1
```

With `error` you can show an error message without the build failing:
``````
```rascal-shell error
int x = 1;
y + y;
```
``````

```rascal-shell
int x = 1;
y + y;
```

Or you could prepare something in "secretly":
``````
```rascal-prepare
int x = 1;
```
``````

```rascal-prepare
int y = 1;
```

Nothing is shown; the `prepare` block is executed but hidden from view. Then use the context it created later. This is nice for generating example data in files or modules to import
without bothering the reader with the details. Watch out: it is not smart to
hide essential notions from the user. This feature is only for preparing a lesson, not to destroy it...

``````
```rascal-shell,continue
y + y
```
``````

```rascal-shell,continue
x + x
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

Now we demonstrate line highlighting:

``````
```rascal {2--7} showLineNumbers
int fac(int n) {
    if (n <= 0) {
        return 1;
    }
    else {
        return n * fac(n-1);
    }
}
```
``````

```rascal {2-7} showLineNumbers
int fac(int n) {
    if (n <= 0) {
        return 1;
    }
    else {
        return n * fac(n-1);
    }
}
```

``````
```rascal showLineNumbers
int fac(int n) {
    // highlight-next-line
    if (n <= 0) {
        return 1;
    }
    else {
    // highlight-start
        return n * fac(n-1);
    // highlight-end
    }
}
```
``````

```rascal showLineNumbers
int fac(int n) {
    // highlight-next-line
    if (n <= 0) {
        return 1;
    }
    else {
    // highlight-start
        return n * fac(n-1);
    // highlight-end
    }
}
```

#### Benefits

#### Pitfalls



