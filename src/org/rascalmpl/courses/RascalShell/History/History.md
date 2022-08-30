# Command History

.Synopsis
Use the command history.

.Description

RascalShell provides a history of previously entered commands. This can be accessed as follows:

|keystroke | description|
| ---- | ---- |
| `↑`| Show previous command in history; Type `Return` to execute it. |
| `↓` | Next command in history; Type `Return` to execute it. |
| `Ctrl+r`       |(After search text) Search backward in history. |
| `Ctrl+s`       | (After search text) Forward search in history |

.Examples

```rascal-shell
a = 1;
b = 2;
c = 3;
```
Typing `↑` will then show the text (not followed by `Return`!):

```rascal
rascal>c = 3;
```
Typing `Return` will reexecute `c = 3`.

Typing the letter `a` (the search text in this example) followed by `Ctrl+r` will show the text:

```rascal
(reverse-i-search)`a': a = 3;
```
And again, typing `Return` will re-execute `a = 3`.
