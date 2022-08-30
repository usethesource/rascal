# Command History

.Synopsis
Use the command history.

.Description

RascalShell provides a history of previously entered commands. This can be accessed as follows:

:arrow-up: &#8593;
:arrow-down: &#8595;
:commandkey: &#8984;
:right-tab: &#8677;

[cols="1,3"]
|===
| `{arrow-up}`| Show previous command in history; Type `Return` to execute it.
| `{arrow-down}` | Next command in history; Type `Return` to execute it.
| `Ctrl+r`       |(After search text) Search backward in history.
| `Ctrl+s`       | (After search text) Forward search in history
|===

.Examples

```rascal-shell
a = 1;
b = 2;
c = 3;
```
Typing `{arrow-up}` will then show the text (not followed by `Return`!):
```rascal
rascal>c = 3;
```
Typing `Return` will reexecute `c = 3`.

Typing the letter `a` (the search text in this example) followed by `Ctrl+r` will show the text:
```rascal,subs="verbatim,quotes"
(reverse-i-search)`a': a = 3;
```
And again, typing `Return` will reexecute `a = 3`.
